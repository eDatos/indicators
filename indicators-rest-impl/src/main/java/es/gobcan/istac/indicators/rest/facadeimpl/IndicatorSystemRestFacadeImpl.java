package es.gobcan.istac.indicators.rest.facadeimpl;

import java.text.MessageFormat;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

@Service("indicatorSystemRestFacade")
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    public static Integer           MAXIMUM_RESULT_SIZE_DEFAULT = Integer.valueOf(25);
    public static Integer           MAXIMUM_RESULT_SIZE_ALLOWED = Integer.valueOf(1000);

    private Logger                   logger                      = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService    = null;

    @Autowired
    private Do2TypeMapper            dto2TypeMapper              = null;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(UriComponentsBuilder uriComponentsBuilder, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = createPagingParameter(paginator);
        PagedResult<IndicatorsSystemVersion> indicatorsSystemVersions = indicatorsSystemsService.findIndicatorsSystemsPublished(RestConstants.SERVICE_CONTEXT, null, pagingParameter);

        List<IndicatorsSystemBaseType> result = dto2TypeMapper.indicatorsSystemDoToBaseType(indicatorsSystemVersions.getValues(), uriComponentsBuilder);

        PagedResultType<IndicatorsSystemBaseType> resultType = new PagedResultType<IndicatorsSystemBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATOR_SYSTEMS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsSystemVersions.getTotalRows());
        resultType.setItems(result);
        return resultType;
    }

    @Override
    public IndicatorsSystemType retrieveIndicatorsSystem(UriComponentsBuilder uriComponentsBuilder, String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);        
        IndicatorsSystemType result = dto2TypeMapper.indicatorsSystemDoToType(indicatorsSystemVersion, uriComponentsBuilder);
        return result;
    }

    @Override
    public List<IndicatorInstanceType> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder, final String idIndicatorSystem) throws Exception {
        // TODO PAGING
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        List<IndicatorInstance> indicatorInstances = indicatorsSystemsService.retrieveIndicatorsInstancesByIndicatorsSystem(RestConstants.SERVICE_CONTEXT, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), indicatorsSystemVersion.getVersionNumber());
        List<IndicatorInstanceType> result = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstances, uriComponentsBuilder);
        return result;
    }

    @Override
    public IndicatorInstanceType retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder, final String idIndicatorSystem, final String uuidIndicatorInstance) throws Exception {
        IndicatorInstance indicatorInstance = indicatorsSystemsService.retrieveIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance);
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        if (!indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode().equals(indicatorsSystemVersion.getIndicatorsSystem().getCode())) {
            String text = MessageFormat.format("IndicatorInstance: {0}, not in indicatorSystem: {1}", idIndicatorSystem, uuidIndicatorInstance);
            logger.warn(text);
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, text);
        }
        IndicatorInstanceType result = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstance, uriComponentsBuilder);
        return result;
    }

    private PagingParameter createPagingParameter(final RestCriteriaPaginator paginator) {
        if (paginator.getOffset() == null || paginator.getOffset() < 0) {
            paginator.setOffset(0);
        }

        if (paginator.getLimit() == null || paginator.getLimit() < 0) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_DEFAULT);
        }

        if (paginator.getLimit() > MAXIMUM_RESULT_SIZE_ALLOWED) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_ALLOWED);
        }
        return PagingParameter.rowAccess(paginator.getOffset(), paginator.getOffset() + paginator.getLimit(), Boolean.TRUE);
    }

}
