package es.gobcan.istac.indicators.web.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ComponentType;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemVersionSummaryDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class DtoUtils {

    /**
     * Updates {@link IndicatorsSystemDtoWeb}
     * 
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto) {
        return updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto, null);
    }

    /**
     * Fills an {@link IndicatorsSystemDtoWeb} from {@link IndicatorsSystemDto} and {@link Operation}
     * 
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto, Operation operation) {
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setVersionNumber(indicatorsSystemDto.getVersionNumber());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setPublishedVersion(indicatorsSystemDto.getPublishedVersion());
            indicatorsSystemDtoWeb.setArchivedVersion(indicatorsSystemDto.getArchivedVersion());
            indicatorsSystemDtoWeb.setProductionValidationDate(indicatorsSystemDto.getProductionValidationDate());
            indicatorsSystemDtoWeb.setProductionValidationUser(indicatorsSystemDto.getProductionValidationUser());
            indicatorsSystemDtoWeb.setDiffusionValidationDate(indicatorsSystemDto.getDiffusionValidationDate());
            indicatorsSystemDtoWeb.setDiffusionValidationUser(indicatorsSystemDto.getDiffusionValidationUser());
            indicatorsSystemDtoWeb.setPublicationDate(indicatorsSystemDto.getPublicationDate());
            indicatorsSystemDtoWeb.setPublicationUser(indicatorsSystemDto.getPublicationUser());
            indicatorsSystemDtoWeb.setArchiveDate(indicatorsSystemDto.getArchiveDate());
            indicatorsSystemDtoWeb.setArchiveUser(indicatorsSystemDto.getArchiveUser());
            indicatorsSystemDtoWeb.setProcStatus(indicatorsSystemDto.getProcStatus());
            indicatorsSystemDtoWeb.setCreatedDate(indicatorsSystemDto.getCreatedDate());
            indicatorsSystemDtoWeb.setCreatedBy(indicatorsSystemDto.getCreatedBy());
            indicatorsSystemDtoWeb.setLastUpdated(indicatorsSystemDto.getLastUpdated());
            indicatorsSystemDtoWeb.setLastUpdatedBy(indicatorsSystemDto.getLastUpdatedBy());
        }
        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));
            indicatorsSystemDtoWeb.setAcronym(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getAcronym()));
            indicatorsSystemDtoWeb.setDescription(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getDescription()));
            indicatorsSystemDtoWeb.setObjective(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getObjective()));
            indicatorsSystemDtoWeb.setOperationExternallyPublished(ProcStatus.EXTERNALLY_PUBLISHED.equals(operation.getProcStatus()));
        }
        return indicatorsSystemDtoWeb;
    }

    /**
     * Fills an {@link IndicatorsSystemSummaryDtoWeb} from {@link IndicatorsSystemSummaryDto} and {@link Resource}
     * 
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb updateIndicatorsSystemSummaryDtoWeb(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemSummaryDto indicatorsSystemDto,
            Resource operation) {
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setDiffusionVersion(indicatorsSystemDto.getDiffusionVersion());
        }
        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));
        }
        return indicatorsSystemDtoWeb;
    }

    /**
     * Create an {@link IndicatorsSystemDtoWeb} from a {@link Operation}
     * 
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb createIndicatorsSystemDtoWeb(Operation operation) {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemDtoWeb();
        indicatorsSystemDtoWeb.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, null, operation);
    }

    /**
     * Create an {@link IndicatorsSystemSummaryDtoWeb} from a {@link Resource}
     * 
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb createIndicatorsSystemSummaryDtoWeb(Resource operation) {
        IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemSummaryDtoWeb();
        IndicatorsSystemVersionSummaryDto summaryDto = new IndicatorsSystemVersionSummaryDto();
        summaryDto.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemDtoWeb.setProductionVersion(summaryDto);
        return updateIndicatorsSystemSummaryDtoWeb(indicatorsSystemDtoWeb, null, operation);
    }

    /**
     * Create a {@link DataStructureDto} from a {@link Query}
     * 
     * @param query
     * @return
     */
    public static DataStructureDto createDataStructureDto(Query query) {
        if (query == null) {
            return null;
        }

        DataStructureDto dataStructureDto = new DataStructureDto();

        // UUid
        dataStructureDto.setUuid(query.getUrn());

        // Title
        dataStructureDto.setTitle(extractValueForDefaultLanguage(query.getName()));

        // PX Uri
        dataStructureDto.setPxUri(query.getUrn());

        ResourceInternal statisticalOperation = query.getMetadata().getStatisticalOperation();

        // Survey Code
        dataStructureDto.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        dataStructureDto.setSurveyTitle(extractValueForDefaultLanguage(statisticalOperation.getName()));

        // Maintainer
        ResourceInternal maintainer = query.getMetadata().getMaintainer();
        dataStructureDto.setPublishers(Arrays.asList(extractValueForDefaultLanguage(maintainer.getName())));

        // Variables
        dataStructureDto.setVariables(extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        // Temporal Variables
        String temporalDimension = extractSpecificDimensionFromDimensions(query.getMetadata().getDimensions(), DimensionType.TIME_DIMENSION);
        dataStructureDto.setTemporalVariable(temporalDimension);

        // Spatial Variables
        String geographicDimension = extractSpecificDimensionFromDimensions(query.getMetadata().getDimensions(), DimensionType.GEOGRAPHIC_DIMENSION);
        dataStructureDto.setSpatialVariables(Arrays.asList(geographicDimension));

        // Cont Variable
        String measureDimension = extractSpecificDimensionFromDimensions(query.getMetadata().getDimensions(), DimensionType.MEASURE_DIMENSION);
        dataStructureDto.setContVariable(measureDimension);

        // Value Labels
        // TODO METAMAC-2503, cubrimientos???? ValueLabels
        Map<String, List<String>> values = extractCoverages(query.getData());
        dataStructureDto.setValueLabels(values);

        // Value Codes
        // TODO METAMAC-2503, cubrimientos???? ValueCodes
        dataStructureDto.setValueCodes(values);

        return dataStructureDto;
    }

    public static String extractValueForDefaultLanguage(InternationalString internationalString) {
        if (internationalString != null && !internationalString.getTexts().isEmpty()) {
            LocalisedString localisedString = internationalString.getTexts().iterator().next();
            return localisedString.getValue();
        }
        return null;
    }

    public static List<String> extractVariablesFromDimensions(Dimensions dimensions) {
        List<String> result = new ArrayList<String>();
        for (Dimension dimension : dimensions.getDimensions()) {
            result.add(dimension.getId());
        }

        return result;
    }

    public static String extractSpecificDimensionFromDimensions(Dimensions dimensions, DimensionType dimensionType) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimensionType.equals(dimension.getType())) {
                return dimension.getId();
            }
        }

        return null;
    }

    public static String extractSpecificAttributeFromAttributes(Attributes attributes, ComponentType componentType) {

        for (Attribute attribute : attributes.getAttributes()) {
            if (componentType.equals(attribute.getType())) {
                return attribute.getId();
            }
        }

        return null;
    }

    public static Map<String, List<String>> extractCoverages(Data data) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        if (data.getDimensions() == null) {
            return result;
        }

        for (DimensionRepresentation dimensionRepresentation : data.getDimensions().getDimensions()) {
            List<String> values = new LinkedList<String>();
            for (CodeRepresentation codeRepresentation : dimensionRepresentation.getRepresentations().getRepresentations()) {
                values.add(codeRepresentation.getCode());
            }
            result.put(dimensionRepresentation.getDimensionId(), values);
        }

        return result;
    }
}
