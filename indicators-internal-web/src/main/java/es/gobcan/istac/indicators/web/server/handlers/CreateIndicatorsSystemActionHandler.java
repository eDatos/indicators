package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

@Component
public class CreateIndicatorsSystemActionHandler extends SecurityActionHandler<CreateIndicatorsSystemAction, CreateIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade                 indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public CreateIndicatorsSystemActionHandler() {
        super(CreateIndicatorsSystemAction.class);
    }

    @Override
    public CreateIndicatorsSystemResult executeSecurityAction(CreateIndicatorsSystemAction action) throws ActionException {
        try {

            if (checkExistIndicatorsSystemWithOperation(action.getIndicatorsSystemDto().getCode())) {
                fireDuplicateIndicatorsSystemException(action.getIndicatorsSystemDto().getCode());
            }

            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = DtoUtils.createIndicatorsSystemDtoWeb(action.getIndicatorsSystemDto());
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb);

            return new CreateIndicatorsSystemResult(DtoUtils.createIndicatorsSystemDtoWeb(indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    /*
     * When an operation marked as indicators system is created, it is not inserted in indicators app only an "on fly" register is created in the main indicators system view.
     * It is created when user changes something. For example he creates an indicator instance.
     * So we must check if an operation exists with a operation api call.
     */

    private boolean checkExistIndicatorsSystemWithOperation(String indicatorsSytemCode) throws MetamacWebException {

        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemSummaryDtoWebs = new ArrayList<>();
        Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(ServiceContextHolder.getCurrentServiceContext(), indicatorsSytemCode);
        return operation != null && operation.getIndicatorSystem();

    }

    private MetamacWebException fireDuplicateIndicatorsSystemException(String indicatorsSystemCode) throws MetamacWebException {
        MetamacExceptionItem exceptionItems = new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, indicatorsSystemCode);

        MetamacException exception = MetamacExceptionBuilder.builder().withExceptionItems(Arrays.asList(exceptionItems)).build();
        throw WebExceptionUtils.createMetamacWebException(exception);
    }

}
