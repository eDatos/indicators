package es.gobcan.istac.indicators.web.widgets;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.ws.StatisticalOperationsInternalWebServiceFacade;

@Controller
public class WidgetsCreatorController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;
    
    @RequestMapping(value = "/widgets/creator", method = RequestMethod.GET)
    public ModelAndView creator() throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView("widgets/creator");
        //modelAndView.addObject("indicatorsSystems", indicatorsSystemsJson);
        
        return modelAndView;
    }

    @RequestMapping(value = "/api/systems", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PaginateResult> systems(@RequestParam("start") Integer start, @RequestParam("size") Integer size) throws Exception {
        //Método mock para probar la páginación hasta que esté lista la api

        PaginateResult paginateResult = new PaginateResult();
        for(int i = start; i < start + size; i++){
            System system = new System("System " + i);
            paginateResult.results.add(system);
        }
        paginateResult.total = 100;
        ResponseEntity<PaginateResult> responseEntity = new ResponseEntity<PaginateResult>(paginateResult, HttpStatus.OK);
        return responseEntity;
    }

    private class PaginateResult {
        public List<Object> results = new ArrayList<Object>();
        public int total;
    }

    private class System {
        public String name;

        public System(String name){
            this.name = name;
        }
    }

}
