package es.gobcan.istac.indicators.web.shared.dto;

import org.siemac.metamac.core.common.dto.InternationalStringDto;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

public class IndicatorsSystemDtoWeb extends IndicatorsSystemDto {

    private static final long      serialVersionUID = 1L;

    private String                 statisticalOperationUri;
    private InternationalStringDto title;
    private InternationalStringDto acronym;
    private InternationalStringDto description;
    private InternationalStringDto objective;

    public String getStatisticalOperationUri() {
        return statisticalOperationUri;
    }

    public void setStatisticalOperationUri(String statisticalOperationUri) {
        this.statisticalOperationUri = statisticalOperationUri;
    }

    public InternationalStringDto getTitle() {
        return title;
    }

    public void setTitle(InternationalStringDto title) {
        this.title = title;
    }

    public InternationalStringDto getAcronym() {
        return acronym;
    }

    public void setAcronym(InternationalStringDto acronym) {
        this.acronym = acronym;
    }

    public InternationalStringDto getDescription() {
        return description;
    }

    public void setDescription(InternationalStringDto description) {
        this.description = description;
    }

    public InternationalStringDto getObjective() {
        return objective;
    }

    public void setObjective(InternationalStringDto objective) {
        this.objective = objective;
    }

}
