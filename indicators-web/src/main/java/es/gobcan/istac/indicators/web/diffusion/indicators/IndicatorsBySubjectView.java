package es.gobcan.istac.indicators.web.indicators;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;

/**
 * Contains one subject and all indicators published with this subject
 */
public class IndicatorsBySubjectView {
    
    private SubjectDto subject;
    private List<IndicatorDto> indicators = new ArrayList<IndicatorDto>();
    
    public SubjectDto getSubject() {
        return subject;
    }    
    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }
    
    public List<IndicatorDto> getIndicators() {
        return indicators;
    }
}
