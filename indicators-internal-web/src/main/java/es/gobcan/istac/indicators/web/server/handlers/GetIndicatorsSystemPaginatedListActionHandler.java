package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.constants.CriteriaConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

@Component
public class GetIndicatorsSystemPaginatedListActionHandler extends SecurityActionHandler<GetIndicatorsSystemPaginatedListAction, GetIndicatorsSystemPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade                 indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public GetIndicatorsSystemPaginatedListActionHandler() {
        super(GetIndicatorsSystemPaginatedListAction.class);
    }

    /*
     * This list will not be paginated. Their elements come from different systems so it will not be necessary to paginate while the number of elements is small.
     */
    @Override
    public GetIndicatorsSystemPaginatedListResult executeSecurityAction(GetIndicatorsSystemPaginatedListAction action) throws ActionException {
        List<String> indicatorsSystemCodeErrors = new ArrayList<>();
        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemSummaryDtoWebs = getIndicatorsSystemSummaryDtoWebsFromOperations(indicatorsSystemCodeErrors);
        indicatorsSystemSummaryDtoWebs.addAll(getIndicatorsSystemSummaryDtoWebsWithoutOperations());

        MetamacWebException resultException = getExceptions(indicatorsSystemCodeErrors);
        return new GetIndicatorsSystemPaginatedListResult(indicatorsSystemSummaryDtoWebs, 0, indicatorsSystemSummaryDtoWebs.size(), resultException);
    }

    private MetamacWebException getExceptions(List<String> indicatorsSystemCodeErrors) {
        if (!indicatorsSystemCodeErrors.isEmpty()) {
            String codes = String.join(",", indicatorsSystemCodeErrors);
            MetamacExceptionItem exceptionItems = new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_DUPLICATED_CODES, codes);

            MetamacException exception = MetamacExceptionBuilder.builder().withExceptionItems(Arrays.asList(exceptionItems)).build();
            return WebExceptionUtils.createMetamacWebException(exception);
        }
        return null;

    }

    private List<IndicatorsSystemSummaryDtoWeb> getIndicatorsSystemSummaryDtoWebsWithoutOperations() throws MetamacWebException {
        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemSummaryDtoWebs = new ArrayList<>();

        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());

        try {
            MetamacCriteriaPropertyRestriction restriction = new MetamacCriteriaPropertyRestriction(IndicatorsSystemCriteriaPropertyEnum.IS_OPERATIONAL.name(), Boolean.FALSE, OperationType.EQ);
            criteria.setRestriction(restriction);
            MetamacCriteriaResult<IndicatorsSystemSummaryDto> systems = indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHolder.getCurrentServiceContext(), criteria);
            for (IndicatorsSystemSummaryDto indicatorsSystemSummaryDto : systems.getResults()) {
                IndicatorsSystemSummaryDtoWeb indicatorsSystemSummaryDtoWeb = DtoUtils.updateIndicatorsSystemSummaryDtoWebCommon(new IndicatorsSystemSummaryDtoWeb(), indicatorsSystemSummaryDto);
                indicatorsSystemSummaryDtoWebs.add(indicatorsSystemSummaryDtoWeb);
            }

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
        return indicatorsSystemSummaryDtoWebs;
    }

    private List<IndicatorsSystemSummaryDtoWeb> getIndicatorsSystemSummaryDtoWebsFromOperations(List<String> indicatorsSystemCodeErrors) throws MetamacWebException {
        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemSummaryDtoWebs = new ArrayList<>();
        Operations result = statisticalOperationsRestInternalFacade.findOperationsIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), 0, CriteriaConstants.MAXIMUM_RESULT_SIZE_ALLOWED);
        if (result != null && result.getOperations() != null) {

            for (Resource resource : result.getOperations()) {
                // Check if operation (indicators system) exists in the DB
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setMaximumResultSize(1);
                MetamacCriteriaPropertyRestriction restriction = new MetamacCriteriaPropertyRestriction(IndicatorsSystemCriteriaPropertyEnum.CODE.name(), resource.getId(), OperationType.EQ);
                criteria.setRestriction(restriction);
                try {

                    MetamacCriteriaResult<IndicatorsSystemSummaryDto> systems = indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHolder.getCurrentServiceContext(), criteria);

                    IndicatorsSystemSummaryDtoWeb indicatorsSystemSummaryDtoWeb = getIndicatorsSystemFromOperation(resource, indicatorsSystemCodeErrors, systems);

                    if (indicatorsSystemSummaryDtoWeb != null) {
                        indicatorsSystemSummaryDtoWebs.add(indicatorsSystemSummaryDtoWeb);
                    }

                } catch (MetamacException e) {
                    throw WebExceptionUtils.createMetamacWebException(e);
                }
            }
        }
        return indicatorsSystemSummaryDtoWebs;
    }

    private IndicatorsSystemSummaryDtoWeb getIndicatorsSystemFromOperation(Resource operation, List<String> indicatorsSystemCodeErrors,
            MetamacCriteriaResult<IndicatorsSystemSummaryDto> indicatorsSystems) {
        IndicatorsSystemSummaryDtoWeb indicatorsSystemSummaryDtoWeb = null;

        if (CollectionUtils.isEmpty(indicatorsSystems.getResults())) {
            // If not, create a new indicators system
            indicatorsSystemSummaryDtoWeb = DtoUtils.createIndicatorsSystemSummaryDtoWeb(operation);
        } else {
            // If exists, updates indicators system
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystems.getResults().get(0);

            if (Boolean.TRUE.equals(indicatorsSystemSummaryDto.getIsOperational())) {
                indicatorsSystemSummaryDtoWeb = DtoUtils.updateIndicatorsSystemSummaryDtoWeb(new IndicatorsSystemSummaryDtoWeb(), indicatorsSystemSummaryDto, operation);
            } else {
                indicatorsSystemCodeErrors.add(operation.getId());
            }
        }

        return indicatorsSystemSummaryDtoWeb;
    }

}
