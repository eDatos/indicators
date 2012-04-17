package es.gobcan.istac.indicators.web.diffusion.indicators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;

@Controller
public class IndicatorsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    // TODO paginación
    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators() throws Exception {
        
        // Retrieve subjects and all indicators published
        List<SubjectDto> subjectsDto = indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContext());
        
        // Find indicators
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE); // TODO paginación
        MetamacCriteriaResult<IndicatorDto> result = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext(), metamacCriteria);
        List<IndicatorDto> indicatorsDto = result.getResults();
        
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