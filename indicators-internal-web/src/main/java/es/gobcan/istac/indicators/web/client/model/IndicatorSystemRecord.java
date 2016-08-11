package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import com.smartgwt.client.data.Record;

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
        setVersionNumber(indicatorsSystemDtoWeb.getVersionNumber());
    }

    public IndicatorSystemRecord(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb) {
        setUuid(indicatorsSystemDtoWeb.getUuid());
        setCode(indicatorsSystemDtoWeb.getCode());
        setTitle(getLocalisedString(indicatorsSystemDtoWeb.getTitle()));
        // Diffusion version
        if (indicatorsSystemDtoWeb.getDiffusionVersion() != null) {
            setDiffusionProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getDiffusionVersion().getProcStatus()));
            setDiffusionVersionNumber(indicatorsSystemDtoWeb.getDiffusionVersion().getVersionNumber());
            // Force to show diffusion version as production version (if there is a production version, these values will be overwritten)
            setProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getDiffusionVersion().getProcStatus()));
            setVersionNumber(indicatorsSystemDtoWeb.getDiffusionVersion().getVersionNumber());
        }
        // Production version
        if (indicatorsSystemDtoWeb.getProductionVersion() != null) {
            setProcStatus(CommonUtils.getIndicatorSystemProcStatusName(indicatorsSystemDtoWeb.getProductionVersion().getProcStatus()));
            setVersionNumber(indicatorsSystemDtoWeb.getProductionVersion().getVersionNumber());
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

    public void setProcStatus(String value) {
        setAttribute(IndicatorsSystemsDS.PROC_STATUS, value);
    }

    public void setDiffusionProcStatus(String value) {
        setAttribute(IndicatorsSystemsDS.PROC_STATUS_DIFF, value);
    }

    public void setVersionNumber(String value) {
        setAttribute(IndicatorsSystemsDS.VERSION, value);
    }

    public void setDiffusionVersionNumber(String value) {
        setAttribute(IndicatorsSystemsDS.VERSION_DIFF, value);
    }
}
