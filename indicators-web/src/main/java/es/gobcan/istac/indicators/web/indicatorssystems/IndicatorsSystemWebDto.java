package es.gobcan.istac.indicators.web.indicatorssystems;

import java.util.Date;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;

/**
 * Dto for indicators system
 */
public class IndicatorsSystemWebDto {

    private String                         uuid;
    private String                         versionNumber;
    private String                         code;
    protected InternationalStringDto       title;
    protected InternationalStringDto       acronym;
    protected InternationalStringDto       description;
    protected InternationalStringDto       objective;
    private Date                           publicationDate;

    public IndicatorsSystemWebDto() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
