package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.enume.domain.StreamMessageStatusEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class IndicatorSystemRecord extends Record {

    public IndicatorSystemRecord(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb) {
        setUuid(indicatorsSystemDtoWeb.getUuid());
        setCode(indicatorsSystemDtoWeb.getCode());
        setTitle(getLocalisedString(indicatorsSystemDtoWeb.getTitle()));
        setProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb));
        setStreamStatus(indicatorsSystemDtoWeb.getStreamMessageStatus());
        setVersionNumber(indicatorsSystemDtoWeb.getVersionNumber());
        setIsOperational(BooleanWebUtils.getBooleanLabel(indicatorsSystemDtoWeb.getIsOperational()));
    }

    public IndicatorSystemRecord(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb) {
        setUuid(indicatorsSystemDtoWeb.getUuid());
        setCode(indicatorsSystemDtoWeb.getCode());
        setTitle(getLocalisedString(indicatorsSystemDtoWeb.getTitle()));
        setIsOperational(BooleanWebUtils.getBooleanLabel(indicatorsSystemDtoWeb.getIsOperational()));
        // Diffusion version
        if (indicatorsSystemDtoWeb.getDiffusionVersion() != null) {
            setDiffusionProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getDiffusionVersion().getProcStatus()));
            setDiffusionVersionNumber(indicatorsSystemDtoWeb.getDiffusionVersion().getVersionNumber());
            setDiffusionStreamStatus(indicatorsSystemDtoWeb.getDiffusionVersion().getStreamMessageStatus());
            // Force to show diffusion version as production version (if there is a production version, these values will be overwritten)
            setProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getDiffusionVersion().getProcStatus()));
            setVersionNumber(indicatorsSystemDtoWeb.getDiffusionVersion().getVersionNumber());
            setStreamStatus(indicatorsSystemDtoWeb.getDiffusionVersion().getStreamMessageStatus());
        }
        // Production version
        if (indicatorsSystemDtoWeb.getProductionVersion() != null) {
            setProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getProductionVersion().getProcStatus()));
            setVersionNumber(indicatorsSystemDtoWeb.getProductionVersion().getVersionNumber());
            setStreamStatus(indicatorsSystemDtoWeb.getProductionVersion().getStreamMessageStatus());
        }
    }

    public void setUuid(String uuid) {
        setAttribute(IndicatorsSystemsDS.UUID, uuid);
    }

    public void setCode(String code) {
        setAttribute(IndicatorsSystemsDS.CODE, code);
    }

    public void setTitle(String title) {
        setAttribute(IndicatorsSystemsDS.TITLE, title);
    }

    public void setIsOperational(String isOperational) {
        setAttribute(IndicatorsSystemsDS.OPERATIONAL, isOperational);
    }

    public void setProcStatus(String value) {
        setAttribute(IndicatorsSystemsDS.PROC_STATUS, value);
    }

    public void setDiffusionProcStatus(String value) {
        setAttribute(IndicatorsSystemsDS.PROC_STATUS_DIFF, value);
    }

    public void setStreamStatus(StreamMessageStatusEnum value) {
        setAttribute(IndicatorsSystemsDS.STREAM_STATUS, streamMessageStatusToImageUrl(value));
    }

    public void setDiffusionStreamStatus(StreamMessageStatusEnum value) {
        setAttribute(IndicatorsSystemsDS.STREAM_STATUS_DIFF, streamMessageStatusToImageUrl(value));
    }

    public void setVersionNumber(String value) {
        setAttribute(IndicatorsSystemsDS.VERSION, value);
    }

    public void setDiffusionVersionNumber(String value) {
        setAttribute(IndicatorsSystemsDS.VERSION_DIFF, value);
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
}
