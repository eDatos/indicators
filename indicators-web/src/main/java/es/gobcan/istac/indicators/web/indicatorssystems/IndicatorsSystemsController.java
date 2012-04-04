package es.gobcan.istac.indicators.web.indicatorssystems;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.BaseController;
import es.gobcan.istac.indicators.web.WebConstants;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    // TODO tratamiento de excepciones
    // TODO paginación
    @RequestMapping(value = "/indicators-systems", method = RequestMethod.GET)
    public String findIndicatorsSystems(Model model) throws MetamacException {

        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.findIndicatorsSystemsPublished(getServiceContext(), null);
        model.addAttribute("indicatorsSystems", indicatorsSystemsDto);

        return WebConstants.VIEW_NAME_INDICATOR_LIST;
    }
}
