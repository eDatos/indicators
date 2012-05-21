package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class IndicatorSystemRecord extends Record {

    public IndicatorSystemRecord(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb) {
        setUuid(indicatorsSystemDtoWeb.getUuid());
        setCode(indicatorsSystemDtoWeb.getCode());
        setTitle(getLocalisedString(indicatorsSystemDtoWeb.getTitle()));
        setProcStatus(CommonUtils.getIndicatorSystemProcStatus(indicatorsSystemDtoWeb));
        // TODO setDiffusionProcStatus();
        setVersionNumber(indicatorsSystemDtoWeb.getVersionNumber());
        // TODO setDiffusionVersionNumber();
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
