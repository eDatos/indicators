package es.gobcan.istac.indicators.web.indicators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.BaseController;
import es.gobcan.istac.indicators.web.WebConstants;

@Controller
public class IndicatorsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    // TODO tratamiento de excepciones
    // TODO paginación
    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators() throws Exception {
        
        // Retrieve subjects and all indicators published
        List<SubjectDto> subjectsDto = indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContext());
        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext(), null);
        
        // Classify indicators by subject
        Map<String, IndicatorsBySubjectView> indicatorsBySubjectsViewMap = new HashMap<String, IndicatorsBySubjectView>();
        for (SubjectDto subjectDto : subjectsDto) {
            IndicatorsBySubjectView indicatorsBySubjectsView = new IndicatorsBySubjectView();
            indicatorsBySubjectsView.setSubject(subjectDto);
            indicatorsBySubjectsViewMap.put(subjectDto.getCode(), indicatorsBySubjectsView);
        }
        for (IndicatorDto indicatorDto : indicatorsDto) {
            String subjectCode = indicatorDto.getSubjectCode();
            IndicatorsBySubjectView indicatorsBySubjectsView = indicatorsBySubjectsViewMap.get(subjectCode);
            indicatorsBySubjectsView.getIndicators().add(indicatorDto);
        }
        
        // To Json
        ObjectMapper mapper = new ObjectMapper();
        String indicatorsBySubjectJson = mapper.writeValueAsString(indicatorsBySubjectsViewMap.values()); // TODO reducir tamaño del json

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);
        modelAndView.addObject("indicatorsBySubject", indicatorsBySubjectJson);
 
        return modelAndView;
    }
}