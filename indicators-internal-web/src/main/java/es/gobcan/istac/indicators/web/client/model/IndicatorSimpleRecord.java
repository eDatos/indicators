package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.web.common.client.resources.GlobalResources;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class IndicatorSimpleRecord extends Record {

    public IndicatorSimpleRecord(IndicatorSummaryDto indicatorSummaryDto) {
        setUuid(indicatorSummaryDto.getUuid());
        setCode(indicatorSummaryDto.getCode());
        // Diffusion version
        if (indicatorSummaryDto.getDiffusionVersion() != null && indicatorSummaryDto.getProductionVersion() == null) {
            setName(getLocalisedString(indicatorSummaryDto.getDiffusionVersion().getTitle()));
            setProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorSummaryDto.getDiffusionVersion().getProcStatus()));
            setNeedsUpdate(indicatorSummaryDto.getDiffusionVersion().getNeedsUpdate());
            setVersionNumber(indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
        } else {
            setName(getLocalisedString(indicatorSummaryDto.getProductionVersion().getTitle()));
            setProcStatus(CommonUtils.getIndicatorProcStatusName(indicatorSummaryDto.getProductionVersion().getProcStatus()));
            setNeedsUpdate(indicatorSummaryDto.getProductionVersion().getNeedsUpdate());
            setVersionNumber(indicatorSummaryDto.getProductionVersion().getVersionNumber());
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

    public String getUuid() {
        return getAttribute(IndicatorDS.UUID);
    }

    public void setProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS, value);
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

    public void setVersionNumber(String value) {
        setAttribute(IndicatorDS.VERSION_NUMBER, value);
    }

    public void setIndicatorDto(IndicatorSummaryDto indicatorDto) {
        setAttribute(IndicatorDS.DTO, indicatorDto);
    }

    public IndicatorSummaryDto getIndicatorDto() {
        return (IndicatorSummaryDto) getAttributeAsObject(IndicatorDS.DTO);
    }
}
