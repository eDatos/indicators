package es.gobcan.istac.indicators.web.indicatorssystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.BaseController;
import es.gobcan.istac.indicators.web.WebConstants;
import es.gobcan.istac.indicators.web.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.ws.WsToDtoMapperUtils;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    // TODO tratamiento de excepciones
    // TODO paginación?
    @RequestMapping(value = "/indicators-systems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems() throws Exception {
        
        // Retrieves all indicators system published from web service and from indicators core
        OperationBaseList operationBaseList = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem();
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.findIndicatorsSystemsPublished(getServiceContext(), null);
        Map<String, OperationBase> operationsByCode = new HashMap<String, OperationBase>();        
        if (operationBaseList != null) {
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                operationsByCode.put(operationBase.getCode(), operationBase);
            }
        }
        
        // Merges information and retrieves only created in indicators core
        List<IndicatorsSystemWebDto> indicatorsSystemsWebDto = new ArrayList<IndicatorsSystemWebDto>();
        for (IndicatorsSystemDto indicatorsSystemDto : indicatorsSystemsDto) {
            OperationBase operationBase = operationsByCode.get(indicatorsSystemDto.getCode());
            if (operationBase != null) {
                IndicatorsSystemWebDto indicatorsSystemWebDto = WsToDtoMapperUtils.getIndicatorsSystemDtoFromOperationBase(operationBase);
                indicatorsSystemsWebDto.add(indicatorsSystemWebDto);
            } else {
                // TODO dar error si no existe la operacion en statistical operations?
            }
        }
        
        // To Json
        ObjectMapper mapper = new ObjectMapper();
        String indicatorsSystemsJson = mapper.writeValueAsString(indicatorsSystemsWebDto);

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        modelAndView.addObject("indicatorsSystems", indicatorsSystemsJson);
 
        return modelAndView;
    }
    
    
}