package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.web.common.client.resources.GlobalResources;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class IndicatorRecord extends Record {

    public IndicatorRecord(IndicatorDto indicatorDto) {
        setUuid(indicatorDto.getUuid());
        setName(getLocalisedString(indicatorDto.getTitle()));
        setCode(indicatorDto.getCode());
        setProcStatus(CommonUtils.getIndicatorProcStatus(indicatorDto));
        // TODO setDiffusionProcStatus();
        setNeedsUpdate(indicatorDto.getNeedsUpdate());
        // TODO setDiffusionNeedsUpdate();
        setVersionNumber(indicatorDto.getVersionNumber());
        // TODO setDifussionVersionNumber();
        setIndicatorDto(indicatorDto);
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

    public void setIndicatorDto(IndicatorDto indicatorDto) {
        setAttribute(IndicatorDS.DTO, indicatorDto);
    }

    public IndicatorDto getIndicatorDto() {
        return (IndicatorDto) getAttributeAsObject(IndicatorDS.DTO);
    }

}
