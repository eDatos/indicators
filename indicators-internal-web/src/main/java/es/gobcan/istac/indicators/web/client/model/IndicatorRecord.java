package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.Date;

import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class IndicatorRecord extends Record {

    public IndicatorRecord(IndicatorDto indicatorDto) {
        setUuid(indicatorDto.getUuid());
        setName(getLocalisedString(indicatorDto.getTitle()));
        setCode(indicatorDto.getCode());
        setProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorDto));
        setNeedsUpdate(indicatorDto.getNeedsUpdate());
        setVersionNumber(indicatorDto.getVersionNumber());
        setIndicatorDto(indicatorDto);
    }

    public IndicatorRecord(IndicatorSummaryDto indicatorSummaryDto) {
        setUuid(indicatorSummaryDto.getUuid());
        setCode(indicatorSummaryDto.getCode());
        // Diffusion version
        if (indicatorSummaryDto.getDiffusionVersion() != null) {
            setName(getLocalisedString(indicatorSummaryDto.getDiffusionVersion().getTitle()));
            setSubject(getLocalisedString(indicatorSummaryDto.getDiffusionVersion().getSubjectTitle()));
            setDiffusionProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorSummaryDto.getDiffusionVersion().getProcStatus()));
            setDiffusionNeedsUpdate(indicatorSummaryDto.getDiffusionVersion().getNeedsUpdate());
            setDiffusionVersionNumber(indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
            // Force to show diffusion version as production version (if there is a production version, these values will be overwritten)
            setProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorSummaryDto.getDiffusionVersion().getProcStatus()));
            setNeedsUpdate(indicatorSummaryDto.getDiffusionVersion().getNeedsUpdate());
            setVersionNumber(indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
            setProductionValidationDateDiff(indicatorSummaryDto.getDiffusionVersion().getProductionValidationDate());
            setProductionValidationUserDiff(indicatorSummaryDto.getDiffusionVersion().getProductionValidationUser());
            setDiffusionValidationDateDiff(indicatorSummaryDto.getDiffusionVersion().getDiffusionValidationDate());
            setDiffusionValidationUserDiff(indicatorSummaryDto.getDiffusionVersion().getDiffusionValidationUser());
            setPublicationDateDiff(indicatorSummaryDto.getDiffusionVersion().getPublicationDate());
            setPublicationUserDiff(indicatorSummaryDto.getDiffusionVersion().getPublicationUser());
            setPublicationFailedDateDiff(indicatorSummaryDto.getDiffusionVersion().getPublicationFailedDate());
            setPublicationFailedUserDiff(indicatorSummaryDto.getDiffusionVersion().getPublicationFailedUser());
            setArchivedDateDiff(indicatorSummaryDto.getDiffusionVersion().getArchiveDate());
            setArchivedUserDiff(indicatorSummaryDto.getDiffusionVersion().getArchiveUser());
            setCreationDateDiff(indicatorSummaryDto.getDiffusionVersion().getCreatedDate());
            setCreationUserDiff(indicatorSummaryDto.getDiffusionVersion().getCreatedBy());
        }
        // Production version
        if (indicatorSummaryDto.getProductionVersion() != null) {
            // Overwrite name if production version exists
            setName(getLocalisedString(indicatorSummaryDto.getProductionVersion().getTitle()));
            // Overwrite subject if production version exists
            setSubject(getLocalisedString(indicatorSummaryDto.getProductionVersion().getSubjectTitle()));
            setProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorSummaryDto.getProductionVersion().getProcStatus()));
            setNeedsUpdate(indicatorSummaryDto.getProductionVersion().getNeedsUpdate());
            setVersionNumber(indicatorSummaryDto.getProductionVersion().getVersionNumber());
            setProductionValidationDate(indicatorSummaryDto.getProductionVersion().getProductionValidationDate());
            setProductionValidationUser(indicatorSummaryDto.getProductionVersion().getProductionValidationUser());
            setDiffusionValidationDate(indicatorSummaryDto.getProductionVersion().getDiffusionValidationDate());
            setDiffusionValidationUser(indicatorSummaryDto.getProductionVersion().getDiffusionValidationUser());
            setPublicationDate(indicatorSummaryDto.getProductionVersion().getPublicationDate());
            setPublicationUser(indicatorSummaryDto.getProductionVersion().getPublicationUser());
            setPublicationFailedDate(indicatorSummaryDto.getProductionVersion().getPublicationFailedDate());
            setPublicationFailedUser(indicatorSummaryDto.getProductionVersion().getPublicationFailedUser());
            setArchivedDate(indicatorSummaryDto.getProductionVersion().getArchiveDate());
            setArchivedUser(indicatorSummaryDto.getProductionVersion().getArchiveUser());
            setCreationDate(indicatorSummaryDto.getProductionVersion().getCreatedDate());
            setCreationUser(indicatorSummaryDto.getProductionVersion().getCreatedBy());
        }
        setIndicatorDto(indicatorSummaryDto);
    }

    public void setUuid(String uuid) {
        setAttribute(IndicatorDS.UUID, uuid);
    }

    public void setName(String name) {
        setAttribute(IndicatorDS.TITLE, name);
    }

    public void setCode(String code) {
        setAttribute(IndicatorDS.CODE, code);
    }

    public void setSubject(String subject) {
        setAttribute(IndicatorDS.SUBJECT_TITLE, subject);
    }

    public String getUuid() {
        return getAttribute(IndicatorDS.UUID);
    }

    public void setProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS, value);
    }

    public void setDiffusionProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS_DIFF, value);
    }

    public void setNeedsUpdate(Boolean value) {
        String imageURL = new String();
        if (value != null && value) {
            // Needs to be updated update
            imageURL = GlobalResources.RESOURCE.errorSmart().getURL();
        } else {
            // Does not need to be updated
            imageURL = GlobalResources.RESOURCE.success().getURL();
        }
        setAttribute(IndicatorDS.NEEDS_UPDATE, imageURL);
    }

    public void setDiffusionNeedsUpdate(Boolean value) {
        String imageURL = new String();
        if (value) {
            // Needs to be updated
            imageURL = GlobalResources.RESOURCE.errorSmart().getURL();
        } else {
            // Does not need to be updated
            imageURL = GlobalResources.RESOURCE.success().getURL();
        }
        setAttribute(IndicatorDS.NEEDS_UPDATE_DIFF, imageURL);
    }

    public void setVersionNumber(String value) {
        setAttribute(IndicatorDS.VERSION_NUMBER, value);
    }

    public void setDiffusionVersionNumber(String value) {
        setAttribute(IndicatorDS.VERSION_NUMBER_DIFF, value);
    }

    public void setIndicatorDto(Object indicatorDto) {
        setAttribute(IndicatorDS.DTO, indicatorDto);
    }

    public Object getIndicatorDto() {
        return getAttributeAsObject(IndicatorDS.DTO);
    }

    public void setProductionValidationDate(Date value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionValidationUser(String value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_USER, value);
    }

    public void setDiffusionValidationDate(Date value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionValidationUser(String value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_USER, value);
    }

    public void setPublicationDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setPublicationUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_USER, value);
    }

    public void setPublicationFailedDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(value));
    }

    public void setPublicationFailedUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_USER, value);
    }

    public void setArchivedDate(Date value) {
        setAttribute(IndicatorDS.ARCHIVED_DATE, DateUtils.getFormattedDate(value));
    }

    public void setArchivedUser(String value) {
        setAttribute(IndicatorDS.ARCHIVED_USER, value);
    }

    public void setCreationDate(Date value) {
        setAttribute(IndicatorDS.CREATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setCreationUser(String value) {
        setAttribute(IndicatorDS.CREATION_USER, value);
    }

    public void setProductionValidationDateDiff(Date value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setProductionValidationUserDiff(String value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_USER_DIFF, value);
    }

    public void setDiffusionValidationDateDiff(Date value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionValidationUserDiff(String value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_USER_DIFF, value);
    }

    public void setPublicationDateDiff(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setPublicationUserDiff(String value) {
        setAttribute(IndicatorDS.PUBLICATION_USER_DIFF, value);
    }

    public void setPublicationFailedDateDiff(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setPublicationFailedUserDiff(String value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_USER_DIFF, value);
    }

    public void setArchivedDateDiff(Date value) {
        setAttribute(IndicatorDS.ARCHIVED_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setArchivedUserDiff(String value) {
        setAttribute(IndicatorDS.ARCHIVED_USER_DIFF, value);
    }

    public void setCreationDateDiff(Date value) {
        setAttribute(IndicatorDS.CREATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setCreationUserDiff(String value) {
        setAttribute(IndicatorDS.CREATION_USER_DIFF, value);
    }
}
