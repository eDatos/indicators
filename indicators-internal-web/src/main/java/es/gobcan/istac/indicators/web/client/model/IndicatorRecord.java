package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.Date;

import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorVersionSummaryDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class IndicatorRecord extends Record {

    public IndicatorRecord(IndicatorDto indicatorDto) {
        setUuid(indicatorDto.getUuid());
        setName(getLocalisedString(indicatorDto.getTitle()));
        setCode(indicatorDto.getCode());
        setProductionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorDto));
        setProductionIndicatorNeedsUpdate(indicatorDto.getNeedsUpdate());
        setProductionIndicatorVersionNumber(indicatorDto.getVersionNumber());
        setIndicatorDto(indicatorDto);
    }

    public IndicatorRecord(IndicatorSummaryDto indicatorSummaryDto) {
        setUuid(indicatorSummaryDto.getUuid());
        setCode(indicatorSummaryDto.getCode());
        
        IndicatorVersionSummaryDto visibleDiffusionVersion = indicatorSummaryDto.getDiffusionVersion();
        // We´ll show the same info on production as the one on diffusion
        IndicatorVersionSummaryDto visibleProductionVersion = indicatorSummaryDto.getProductionVersion() != null ? indicatorSummaryDto.getProductionVersion() : visibleDiffusionVersion;

        if (visibleProductionVersion != null) {
            setName(getLocalisedString(visibleProductionVersion.getTitle()));
            setSubject(getLocalisedString(visibleProductionVersion.getSubjectTitle()));
        }
        
        setProductionIndicatorVersionSummary(visibleProductionVersion);
        setDiffusionIndicatorVersionSummary(visibleDiffusionVersion);
        
        setIndicatorDto(indicatorSummaryDto);
    }

    private void setProductionIndicatorVersionSummary(IndicatorVersionSummaryDto productionVersion) {
        if (productionVersion != null) {
            setProductionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(productionVersion.getProcStatus()));
            setProductionIndicatorNeedsUpdate(productionVersion.getNeedsUpdate());
            setProductionIndicatorVersionNumber(productionVersion.getVersionNumber());
            setProductionIndicatorProductionValidationDate(productionVersion.getProductionValidationDate());
            setProductionIndicatorProductionValidationUser(productionVersion.getProductionValidationUser());
            setProductionIndicatorDiffusionValidationDate(productionVersion.getDiffusionValidationDate());
            setProductionIndicatorDiffusionValidationUser(productionVersion.getDiffusionValidationUser());
            setProductionIndicatorPublicationDate(productionVersion.getPublicationDate());
            setProductionIndicatorPublicationUser(productionVersion.getPublicationUser());
            setProductionIndicatorPublicationFailedDate(productionVersion.getPublicationFailedDate());
            setProductionIndicatorPublicationFailedUser(productionVersion.getPublicationFailedUser());
            setProductionIndicatorArchivedDate(productionVersion.getArchiveDate());
            setProductionIndicatorArchivedUser(productionVersion.getArchiveUser());
            setProductionIndicatorCreationDate(productionVersion.getCreatedDate());
            setProductionIndicatorCreationUser(productionVersion.getCreatedBy());
        }
    }

    private void setDiffusionIndicatorVersionSummary(IndicatorVersionSummaryDto diffusionVersion) {  
        if (diffusionVersion != null) {
            setDiffusionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(diffusionVersion.getProcStatus()));
            setDiffusionIndicatorNeedsUpdate(diffusionVersion.getNeedsUpdate());
            setDiffusionIndicatorVersionNumber(diffusionVersion.getVersionNumber());               
            setDiffusionIndicatorProductionValidationDate(diffusionVersion.getProductionValidationDate());
            setDiffusionIndicatorProductionValidationUser(diffusionVersion.getProductionValidationUser());
            setDiffusionIndicatorDiffusionValidationDate(diffusionVersion.getDiffusionValidationDate());
            setDiffusionIndicatorDiffusionValidationUser(diffusionVersion.getDiffusionValidationUser());
            setDiffusionIndicatorPublicationDate(diffusionVersion.getPublicationDate());
            setDiffusionIndicatorPublicationUser(diffusionVersion.getPublicationUser());
            setDiffusionIndicatorPublicationFailedDate(diffusionVersion.getPublicationFailedDate());
            setDiffusionIndicatorPublicationFailedUser(diffusionVersion.getPublicationFailedUser());
            setDiffusionIndicatorArchivedDate(diffusionVersion.getArchiveDate());
            setDiffusionIndicatorArchivedUser(diffusionVersion.getArchiveUser());
            setDiffusionIndicatorCreationDate(diffusionVersion.getCreatedDate());
            setDiffusionIndicatorCreationUser(diffusionVersion.getCreatedBy());
        }
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

    public void setProductionIndicatorProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS, value);
    }

    public void setDiffusionIndicatorProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS_DIFF, value);
    }

    public void setProductionIndicatorNeedsUpdate(Boolean value) {
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

    public void setDiffusionIndicatorNeedsUpdate(Boolean value) {
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

    public void setProductionIndicatorVersionNumber(String value) {
        setAttribute(IndicatorDS.VERSION_NUMBER, value);
    }

    public void setDiffusionIndicatorVersionNumber(String value) {
        setAttribute(IndicatorDS.VERSION_NUMBER_DIFF, value);
    }

    public void setIndicatorDto(Object indicatorDto) {
        setAttribute(IndicatorDS.DTO, indicatorDto);
    }

    public Object getIndicatorDto() {
        return getAttributeAsObject(IndicatorDS.DTO);
    }

    public void setProductionIndicatorProductionValidationDate(Date value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorProductionValidationUser(String value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_USER, value);
    }

    public void setProductionIndicatorDiffusionValidationDate(Date value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorDiffusionValidationUser(String value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_USER, value);
    }

    public void setProductionIndicatorPublicationDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorPublicationUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_USER, value);
    }

    public void setProductionIndicatorPublicationFailedDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorPublicationFailedUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_USER, value);
    }

    public void setProductionIndicatorArchivedDate(Date value) {
        setAttribute(IndicatorDS.ARCHIVED_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorArchivedUser(String value) {
        setAttribute(IndicatorDS.ARCHIVED_USER, value);
    }

    public void setProductionIndicatorCreationDate(Date value) {
        setAttribute(IndicatorDS.CREATION_DATE, DateUtils.getFormattedDate(value));
    }

    public void setProductionIndicatorCreationUser(String value) {
        setAttribute(IndicatorDS.CREATION_USER, value);
    }

    public void setDiffusionIndicatorProductionValidationDate(Date value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorProductionValidationUser(String value) {
        setAttribute(IndicatorDS.PRODUCTION_VALIDATION_USER_DIFF, value);
    }

    public void setDiffusionIndicatorDiffusionValidationDate(Date value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorDiffusionValidationUser(String value) {
        setAttribute(IndicatorDS.DIFFUSION_VALIDATION_USER_DIFF, value);
    }

    public void setDiffusionIndicatorPublicationDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorPublicationUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_USER_DIFF, value);
    }

    public void setDiffusionIndicatorPublicationFailedDate(Date value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorPublicationFailedUser(String value) {
        setAttribute(IndicatorDS.PUBLICATION_FAILED_USER_DIFF, value);
    }

    public void setDiffusionIndicatorArchivedDate(Date value) {
        setAttribute(IndicatorDS.ARCHIVED_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorArchivedUser(String value) {
        setAttribute(IndicatorDS.ARCHIVED_USER_DIFF, value);
    }

    public void setDiffusionIndicatorCreationDate(Date value) {
        setAttribute(IndicatorDS.CREATION_DATE_DIFF, DateUtils.getFormattedDate(value));
    }

    public void setDiffusionIndicatorCreationUser(String value) {
        setAttribute(IndicatorDS.CREATION_USER_DIFF, value);
    }
}
