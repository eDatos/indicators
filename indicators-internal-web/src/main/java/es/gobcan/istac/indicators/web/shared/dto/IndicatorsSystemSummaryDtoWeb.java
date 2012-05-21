package es.gobcan.istac.indicators.web.shared.dto;

import org.siemac.metamac.core.common.dto.InternationalStringDto;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;

public class IndicatorsSystemSummaryDtoWeb extends IndicatorsSystemSummaryDto {

    private static final long      serialVersionUID = 1L;

    private InternationalStringDto title;

    public InternationalStringDto getTitle() {
        return title;
    }

    public void setTitle(InternationalStringDto title) {
        this.title = title;
    }

}
