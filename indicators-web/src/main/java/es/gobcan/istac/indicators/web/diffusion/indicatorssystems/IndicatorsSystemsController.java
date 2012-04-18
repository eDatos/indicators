package es.gobcan.istac.indicators.web.diffusion.indicatorssystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.ProcStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.diffusion.ws.WsToDtoMapperUtils;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicators-systems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems() throws Exception {
        
        // Find indicators systems published
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);
        MetamacCriteriaResult<IndicatorsSystemDto> result = indicatorsServiceFacade.findIndicatorsSystemsPublished(getServiceContext(), metamacCriteria);
        if (result.getPaginatorResult().getTotalResults().intValue() != result.getResults().size()) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Pagination is not implemented and results size is greater of supported");
        }
        List<IndicatorsSystemDto> indicatorsSystemsDto = result.getResults();
        
        // Find operations metadata from web service
        List<String> indicatorsSystemsCodes = new ArrayList<String>();
        for (IndicatorsSystemDto indicatorsSystemDto : result.getResults()) {
            indicatorsSystemsCodes.add(indicatorsSystemDto.getCode());
        }
        OperationBaseList operationBaseList = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem(indicatorsSystemsCodes);

        // Merges information and retrieves only created in indicators core
        Map<String, OperationBase> operationsByCode = new HashMap<String, OperationBase>();        
        if (operationBaseList != null) {
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                operationsByCode.put(operationBase.getCode(), operationBase);
            }
        }
        
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
    
    @RequestMapping(value = "/indicators-systems/{code}", method = RequestMethod.GET)
    public ModelAndView setupForm(@PathVariable("code") String code, Model model) throws Exception {

        // Retrieve indicators system
        OperationBase operationBase = statisticalOperationsInternalWebServiceFacade.retrieveOperation(code);
        if (!ProcStatusType.PUBLISH_EXTERNALLY.equals(operationBase.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Operation not published externally with code" + code);
            
        }
        IndicatorsSystemWebDto indicatorsSystemWebDto = WsToDtoMapperUtils.getIndicatorsSystemDtoFromOperationBase(operationBase);
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);
        
        // Retrieve dimensions and indicators instances
        IndicatorsSystemStructureDto structureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), indicatorsSystemDto.getUuid(), indicatorsSystemDto.getVersionNumber());
        
        // To Json
        ObjectMapper mapper = new ObjectMapper();
        String indicatorsSystemJson = mapper.writeValueAsString(indicatorsSystemWebDto);
        String structureJson = mapper.writeValueAsString(structureDto.getElements());

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEM_VIEW);
        modelAndView.addObject("indicatorsSystem", indicatorsSystemJson);
        modelAndView.addObject("indicatorsSystemStructure", structureJson);
 
        return modelAndView;
    }
}