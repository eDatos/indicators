package es.gobcan.istac.indicators.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestExternalService;

public class DsdProcessor {

    public static Map<String, String> getDsdMetadata(SrmRestInternalService srmRestInternalService, StatisticalResoucesRestExternalService statisticalResoucesRestExternalService,
            List<DataSource> dataSources) throws MetamacException {

        for (DataSource dataSource : dataSources) {
            if (!QueryEnvironmentEnum.METAMAC.equals(dataSource.getQueryEnvironment())) {
                continue;
            }

            String queryUuid = dataSource.getQueryUuid();
            String dsdUrn = extractDsdUrnFromQueryUuid(statisticalResoucesRestExternalService, queryUuid);

            if (StringUtils.isNotEmpty(dsdUrn)) {
                return getDimensions(srmRestInternalService, dsdUrn, true);
            }
        }

        return new HashMap<>();
    }

    private static String extractDsdUrnFromQueryUuid(StatisticalResoucesRestExternalService statisticalResoucesRestExternalService, String queryUuid) throws MetamacException {
        if (StringUtils.startsWithIgnoreCase(queryUuid, UrnUtils.URN_SIEMAC_CLASS_QUERY_PREFIX)) {
            Query queryMetadata = statisticalResoucesRestExternalService.retrieveQueryByUrnInDefaultLang(queryUuid, StatisticalResoucesRestExternalService.QueryFetchEnum.ONLY_METADATA);

            if (queryMetadata.getMetadata() != null) {
                return getUrnDsd(queryMetadata.getMetadata().getRelatedDsd());
            }
        } else if (StringUtils.startsWithIgnoreCase(queryUuid, UrnUtils.URN_SIEMAC_CLASS_DATASET_PREFIX)) {
            Dataset datasetMetadata = statisticalResoucesRestExternalService.retrieveDatasetByUrnInDefaultLang(queryUuid, StatisticalResoucesRestExternalService.QueryFetchEnum.ONLY_METADATA);

            if (datasetMetadata.getMetadata() != null) {
                return getUrnDsd(datasetMetadata.getMetadata().getRelatedDsd());
            }
        }

        return null;
    }

    private static String getUrnDsd(DataStructureDefinition dsdDefinition) throws MetamacException {
        return dsdDefinition != null ? dsdDefinition.getUrn() : null;
    }

    private static Map<String, String> getDimensions(SrmRestInternalService srmRestInternalService, String dsdUrn, boolean onlyGeographicalDimension) throws MetamacException {
        org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
        Map<String, String> representationByDimension = new HashMap<>();

        DataStructureComponents components = dsd.getDataStructureComponents();
        if (components == null || components.getDimensions() == null) {
            return representationByDimension;
        }

        Dimensions dimensions = components.getDimensions();
        for (DimensionBase dimObj : dimensions.getDimensions()) {

            if (dimObj instanceof Dimension) {
                Dimension dim = (Dimension) dimObj;
                if (Boolean.TRUE.equals(dim.isIsSpatial())) {
                    representationByDimension.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), extractRepresentation(dim.getLocalRepresentation()));
                    if (onlyGeographicalDimension) {
                        return representationByDimension;
                    }
                }
            }

            else if (!onlyGeographicalDimension && dimObj instanceof MeasureDimension) {
                representationByDimension.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), extractRepresentation(dimObj.getLocalRepresentation()));
            }
        }

        final String geoKey = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();
        if (!representationByDimension.containsKey(geoKey)) {
            String urn = getAttributeSpatialRepresentation(components);
            if (urn != null) {
                representationByDimension.put(geoKey, urn);
            }
        }
        return representationByDimension;
    }

    private static String getAttributeSpatialRepresentation(DataStructureComponents components) {
        if (components.getAttributes() != null && components.getAttributes().getAttributes() != null) {
            for (AttributeBase attribute : components.getAttributes().getAttributes()) {
                if (AttributeQualifierType.SPATIAL.equals(attribute.getType())) {
                    return extractRepresentation(attribute.getLocalRepresentation());
                }
            }
        }
        return null;
    }

    private static String extractRepresentation(Representation representation) {

        if (representation.getEnumerationCodelist() != null) {
            return representation.getEnumerationCodelist().getUrn();
        } else if (representation.getEnumerationConceptScheme() != null) {
            return representation.getEnumerationConceptScheme().getUrn();
        }
        return null;
    }

}
