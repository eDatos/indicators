package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Service
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    protected Logger                logger = LoggerFactory.getLogger(IndicatorRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper           dto2TypeMapper;

    @Autowired
    protected IndicatorsApiService  indicatorsApiService;

    @Autowired
    private IndicatorsRest2DoMapper indicatorsRest2DoMapper;

    @Override
    public PagedResultType<IndicatorBaseType> findIndicators(String baseUrl, String q, String order, final RestCriteriaPaginator paginator, String fields, Map<String, List<String>> representation)
            throws Exception {
        long timeInit = System.currentTimeMillis();
        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorsRest2DoMapper.queryParams2SculptorCriteria(q, order, paginator.getLimit(), paginator.getOffset());

        // Find
        PagedResult<IndicatorVersion> indicatorsVersions = findIndicators(sculptorCriteria);

        // Parse fields
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        // Mapping to type
        List<IndicatorBaseType> result = dto2TypeMapper.indicatorDoToBaseType(indicatorsVersions.getValues(), baseUrl);

        long timeBeforeMetadata = System.currentTimeMillis();
        logger.info("Tiempo find " + (timeBeforeMetadata - timeInit));

        // Fields filter. Only support for +metadata, +data
        if (fieldsToAdd.contains("+metadata")) {
            for (int i = 0; i < result.size(); i++) {
                IndicatorBaseType baseType = result.get(i);
                IndicatorVersion indicatorVersion = indicatorsVersions.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorDoToMetadataType(indicatorVersion, metadataType, baseUrl);
                baseType.setMetadata(metadataType);
            }
        }
        long timeAfterMetadata = System.currentTimeMillis();
        logger.info("Tiempo metadata " + (timeAfterMetadata - timeBeforeMetadata));

        if (fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");
            for (IndicatorBaseType indicatorType : result) {
                Map<String, List<String>> selectedGranularities = MapUtils.EMPTY_MAP;
                DataType dataType = retrieveIndicatorData(baseUrl, indicatorType.getCode(), representation, selectedGranularities, includeObservationsAttributes);
                indicatorType.setData(dataType);
            }
        }
        long timeAfterData = System.currentTimeMillis();
        logger.info("Tiempo data " + (timeAfterData - timeAfterMetadata));

        // Pagegd result
        PagedResultType<IndicatorBaseType> resultType = new PagedResultType<IndicatorBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATORS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsVersions.getTotalRows());
        resultType.setItems(result);

        logger.info("Tiempo total " + (System.currentTimeMillis() - timeInit));
        return resultType;
    }

    protected PagedResult<IndicatorVersion> findIndicators(SculptorCriteria sculptorCriteria) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.findIndicators(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.retrieveIndicatorByCode(indicatorCode);
    }

    @Override
    public IndicatorType retrieveIndicator(String baseUrl, String indicatorCode) throws Exception {
        IndicatorVersion indicatorsVersion = retrieveIndicatorByCode(indicatorCode);
        IndicatorType result = dto2TypeMapper.indicatorDoToType(indicatorsVersion, baseUrl);
        return result;
    }

    @Override
    public DataType retrieveIndicatorData(String baseUrl, String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            boolean includeObservationMetadata) throws Exception {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

        long time = System.currentTimeMillis();

        IndicatorsDataGeoDimensionFilterVO geoFilter = ConditionUtil.filterGeographicalDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataTimeDimensionFilterVO timeFilter = ConditionUtil.filterTimeDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataMeasureDimensionFilterVO measureFilter = ConditionUtil.filterMeasureDimension(selectedRepresentations);

        IndicatorsDataFilterVO dataFilter = new IndicatorsDataFilterVO();
        dataFilter.setGeoFilter(geoFilter);
        dataFilter.setTimeFilter(timeFilter);
        dataFilter.setMeasureFilter(measureFilter);

        long timeDimensions = System.currentTimeMillis();

        DataType dataType;
        if (includeObservationMetadata) {
            IndicatorObservationsExtendedVO indicatorObservationsExtended = indicatorsApiService.findObservationsExtendedInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);
            long timeData = System.currentTimeMillis();
            logger.info("Data dimensiones " + (timeDimensions - time));
            logger.info("Data repository " + (timeData - timeDimensions));

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorVersion, indicatorObservationsExtended.getGeographicalCodes(), indicatorObservationsExtended.getTimeCodes(),
                    indicatorObservationsExtended.getMeasureCodes(), indicatorObservationsExtended.getObservations());
            dataType = dto2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);
            long timeOutput = System.currentTimeMillis();
            logger.info("Data output " + (timeOutput - timeData));
        } else {
            IndicatorObservationsVO indicatorObservations = indicatorsApiService.findObservationsInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);
            long timeData = System.currentTimeMillis();
            logger.info("Data dimensiones " + (timeDimensions - time));
            logger.info("Data repository " + (timeData - timeDimensions));

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorVersion, indicatorObservations.getGeographicalCodes(), indicatorObservations.getTimeCodes(),
                    indicatorObservations.getMeasureCodes(), indicatorObservations.getObservations());
            dataType = dto2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);
            long timeOutput = System.currentTimeMillis();
            logger.info("Data output " + (timeOutput - timeData));
        }
        return dataType;
    }
}
