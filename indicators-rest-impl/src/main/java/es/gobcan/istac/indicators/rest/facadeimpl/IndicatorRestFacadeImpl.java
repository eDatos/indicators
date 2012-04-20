package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.CriteriaUtil;

@Service("indicatorRestFacadeImpl")
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    protected Logger          logger                      = LoggerFactory.getLogger(getClass());

    @Autowired
    private Do2TypeMapper     dto2TypeMapper              = null;

    @Autowired
    private IndicatorsService indicatorsService           = null;

    @Override
    public PagedResultType<IndicatorBaseType> findIndicators(final String baseUrl, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = CriteriaUtil.createPagingParameter(paginator);
        PagedResult<IndicatorVersion> indicatorsVersions = indicatorsService.findIndicatorsPublished(RestConstants.SERVICE_CONTEXT, null, pagingParameter);

        List<IndicatorBaseType> result = dto2TypeMapper.indicatorDoToBaseType(indicatorsVersions.getValues(), baseUrl);

        PagedResultType<IndicatorBaseType> resultType = new PagedResultType<IndicatorBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATORS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsVersions.getTotalRows());
        resultType.setItems(result);
        return resultType;
    }
    
    @Override
    public IndicatorType retrieveIndicator(String baseUrl, String indicatorCode) throws Exception {
        IndicatorVersion indicatorsVersion = indicatorsService.retrieveIndicatorByCode(RestConstants.SERVICE_CONTEXT, indicatorCode, null);
        IndicatorType result = dto2TypeMapper.indicatorDoToType(indicatorsVersion, baseUrl);
        return result;
    }

}
