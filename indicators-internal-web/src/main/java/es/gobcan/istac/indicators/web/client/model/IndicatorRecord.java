package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.Date;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorVersionSummaryDto;
import es.gobcan.istac.indicators.core.enume.domain.StreamMessageStatusEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class IndicatorRecord extends ListGridRecord {

    public IndicatorRecord(IndicatorDto indicatorDto) {
        setUuid(indicatorDto.getUuid());
        setName(getLocalisedString(indicatorDto.getTitle()));
        setCode(indicatorDto.getCode());
        setNotifyPopulationErrors(indicatorDto.getNotifyPopulationErrors());
        setProductionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorDto));
        setProductionIndicatorNeedsUpdate(indicatorDto.getNeedsUpdate());
        setProductionIndicatorStreamStatus(indicatorDto.getStreamMessageStatus());
        setProductionIndicatorVersionNumber(indicatorDto.getVersionNumber());
        setIndicatorDto(indicatorDto);
    }

    public IndicatorRecord(IndicatorSummaryDto indicatorSummaryDto) {
        setUuid(indicatorSummaryDto.getUuid());
        setCode(indicatorSummaryDto.getCode());
        setNotifyPopulationErrors(indicatorSummaryDto.getNotifyPopulationErrors());
        IndicatorVersionSummaryDto visibleDiffusionVersion = indicatorSummaryDto.getDiffusionVersion();
        // We´ll show the same info on production as the one on diffusion
        IndicatorVersionSummaryDto visibleProductionVersion = indicatorSummaryDto.getProductionVersion() != null ? indicatorSummaryDto.getProductionVersion() : visibleDiffusionVersion;

        if (visibleProductionVersion != null) {
            setName(getLocalisedString(visibleProductionVersion.getTitle()));
            setCategoryElement(getLocalisedString(visibleProductionVersion.getCategoryElementTitle()));
        }

        setProductionIndicatorVersionSummary(visibleProductionVersion);
        setDiffusionIndicatorVersionSummary(visibleDiffusionVersion);

        setIndicatorDto(indicatorSummaryDto);
    }

    private void setProductionIndicatorVersionSummary(IndicatorVersionSummaryDto productionVersion) {
        if (productionVersion != null) {
            setProductionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(productionVersion.getProcStatus()));
            setProductionIndicatorNeedsUpdate(productionVersion.getNeedsUpdate());
            setProductionIndicatorStreamStatus(productionVersion.getStreamMessageStatus());
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
            setProductionIndicatorIsMainIndicator(productionVersion.getIsMainIndicator());
        }
    }

    private void setDiffusionIndicatorVersionSummary(IndicatorVersionSummaryDto diffusionVersion) {
        if (diffusionVersion != null) {
            setDiffusionIndicatorProcStatus(CommonUtils.getIndicatorProcStatusName(diffusionVersion.getProcStatus()));
            setDiffusionIndicatorNeedsUpdate(diffusionVersion.getNeedsUpdate());
            setDiffusionIndicatorStreamStatus(diffusionVersion.getStreamMessageStatus());
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
            setDiffusionIndicatorIsMainIndicator(diffusionVersion.getIsMainIndicator());
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

    public void setCategoryElement(String categoryElement) {
        setAttribute(IndicatorDS.CATEGORY_ELEMENT, categoryElement);
    }

    private void setNotifyPopulationErrors(Boolean value) {
        String imageURL = new String();
        if (value != null && value) {
            imageURL = GlobalResources.RESOURCE.success().getURL();
        } else {
            imageURL = GlobalResources.RESOURCE.disable().getURL();
        }
        setAttribute(IndicatorDS.NOTIFY_POPULATION_ERRORS, value);
        setAttribute(IndicatorDS.NOTIFY_POPULATION_ERRORS_IMAGE, imageURL);
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

    public void setProductionIndicatorStreamStatus(StreamMessageStatusEnum value) {
        setAttribute(IndicatorDS.STREAM_STATUS, streamMessageStatusToImageUrl(value));
    }

    public void setDiffusionIndicatorStreamStatus(StreamMessageStatusEnum value) {
        setAttribute(IndicatorDS.STREAM_STATUS_DIFF, streamMessageStatusToImageUrl(value));
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

    private String streamMessageStatusToImageUrl(StreamMessageStatusEnum value) {
        String imageURL = "";
        if (StreamMessageStatusEnum.SENT == value) {
            imageURL = GlobalResources.RESOURCE.success().getURL();
        } else if (StreamMessageStatusEnum.PENDING == value) {
            imageURL = GlobalResources.RESOURCE.warn().getURL();
        } else if (StreamMessageStatusEnum.FAILED == value) {
            imageURL = GlobalResources.RESOURCE.errorSmart().getURL();
        }
        return imageURL;
    }

    public void setProductionIndicatorIsMainIndicator(Boolean value) {
        setMainIndicator(value, IndicatorDS.MAIN_INDICATOR);
    }

    public void setDiffusionIndicatorIsMainIndicator(Boolean value) {
        setMainIndicator(value, IndicatorDS.MAIN_INDICATOR_DIFF);
    }

    private void setMainIndicator(Boolean value, String attribute) {
        String mainIndicatorValue = (Boolean.TRUE.equals(value)) ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no();
        setAttribute(attribute, mainIndicatorValue);
    }
}
