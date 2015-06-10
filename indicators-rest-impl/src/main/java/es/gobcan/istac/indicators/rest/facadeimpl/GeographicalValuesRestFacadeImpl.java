package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsCoverageService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;

@Service("geographicalValuesRestFacade")
public class GeographicalValuesRestFacadeImpl implements GeographicalValuesRestFacade {

    @Autowired
    private IndicatorsCoverageService              indicatorsCoverageService;

    @Autowired
    private IndicatorsSystemsService               indicatorsSystemsService;

    @Autowired
    private Do2TypeMapper                          mapper;

    @Autowired
    private MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapper;

    private String getGranularityUuidByCode(String granularityCode) throws MetamacException {
        GeographicalGranularity granularity = indicatorsSystemsService.retrieveGeographicalGranularityByCode(IndicatorsRestConstants.SERVICE_CONTEXT, granularityCode);
        return granularity.getUuid();
    }

    @Override
    public List<GeographicalValueType> findGeographicalValuesByIndicatorsSystemCode(String indicatorsSystemCode, String granularityCode) throws MetamacException {
        String granularityUuid = getGranularityUuidByCode(granularityCode);
        List<GeographicalValueVO> geographicalValues = indicatorsCoverageService.retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(IndicatorsRestConstants.SERVICE_CONTEXT,
                indicatorsSystemCode, granularityUuid);
        return mapper.geographicalValuesVOToType(geographicalValues);
    }

    @Override
    public List<GeographicalValueType> findGeographicalValuesByGranularity(String granularityCode) throws MetamacException {
        String granularityUuid = null;
        if (granularityCode != null) {
            granularityUuid = getGranularityUuidByCode(granularityCode);
        }

        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
        criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

        if (granularityUuid != null) {
            criteria.setRestriction(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), granularityUuid, OperationType.EQ));
        }

        // order
        MetamacCriteriaOrder globalOrder = new MetamacCriteriaOrder();
        globalOrder.setPropertyName(GeographicalValueCriteriaOrderEnum.ORDER.name());
        globalOrder.setType(OrderTypeEnum.ASC);
        criteria.getOrdersBy().add(globalOrder);

        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getGeographicalValueCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<GeographicalValue> result = indicatorsSystemsService.findGeographicalValues(IndicatorsRestConstants.SERVICE_CONTEXT, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        List<GeographicalValue> geographicalValues = result.getValues();
        return mapper.geographicalValuesDoToType(geographicalValues);
    }

    @Override
    public List<GeographicalValueType> findGeographicalValuesBySubjectCode(String subjectCode, String granularityCode) throws MetamacException {
        String granularityUuid = getGranularityUuidByCode(granularityCode);
        List<GeographicalValueVO> geographicalValues = indicatorsCoverageService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(IndicatorsRestConstants.SERVICE_CONTEXT, subjectCode,
                granularityUuid);
        return mapper.geographicalValuesVOToType(geographicalValues);
    }

}
