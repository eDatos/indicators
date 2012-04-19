package es.gobcan.istac.indicators.web.diffusion.indicators;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.web.diffusion.BaseController;

@Controller
public class IndicatorsController extends BaseController {

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators() throws Exception {

        // TODO llamar a API REST
        
//        // Retrieve subjects and all indicators published
//        List<SubjectDto> subjectsDto = indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContext());
//        
//        // Find indicators
//        MetamacCriteria metamacCriteria = new MetamacCriteria();
//        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
//        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
//        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);
//        MetamacCriteriaResult<IndicatorDto> result = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext(), metamacCriteria);
//        if (result.getPaginatorResult().getTotalResults().intValue() != result.getResults().size()) {
//            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Pagination is not implemented and results size is greater of supported");
//        }
//        List<IndicatorDto> indicatorsDto = result.getResults();
//        
//        // Classify indicators by subject
//        Map<String, IndicatorsBySubjectView> indicatorsBySubjectsViewMap = new HashMap<String, IndicatorsBySubjectView>();
//        for (SubjectDto subjectDto : subjectsDto) {
//            IndicatorsBySubjectView indicatorsBySubjectsView = new IndicatorsBySubjectView();
//            indicatorsBySubjectsView.setSubject(subjectDto);
//            indicatorsBySubjectsViewMap.put(subjectDto.getCode(), indicatorsBySubjectsView);
//        }
//        for (IndicatorDto indicatorDto : indicatorsDto) {
//            String subjectCode = indicatorDto.getSubjectCode();
//            IndicatorsBySubjectView indicatorsBySubjectsView = indicatorsBySubjectsViewMap.get(subjectCode);
//            indicatorsBySubjectsView.getIndicators().add(indicatorDto);
//        }
//        
//        // To Json
//        ObjectMapper mapper = new ObjectMapper();
//        String indicatorsBySubjectJson = mapper.writeValueAsString(indicatorsBySubjectsViewMap.values());
//
//        // View
//        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);
//        modelAndView.addObject("indicatorsBySubject", indicatorsBySubjectJson);
// 
//        return modelAndView;
        
        return null;
    }
}