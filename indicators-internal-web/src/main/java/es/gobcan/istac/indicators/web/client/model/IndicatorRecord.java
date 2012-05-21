package es.gobcan.istac.indicators.web.client.model;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.web.common.client.resources.GlobalResources;

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
        setProcStatus(CommonUtils.getIndicatorProcStatus(indicatorDto));
        setNeedsUpdate(indicatorDto.getNeedsUpdate());
        setVersionNumber(indicatorDto.getVersionNumber());
    }

    public IndicatorRecord(IndicatorSummaryDto indicatorSummaryDto) {
        setUuid(indicatorSummaryDto.getUuid());
        setCode(indicatorSummaryDto.getCode());
        // Diffusion version
        if (indicatorSummaryDto.getDiffusionVersion() != null) {
            setName(getLocalisedString(indicatorSummaryDto.getDiffusionVersion().getTitle()));
            setDiffusionProcStatus(CommonUtils.getIndicatorProcStatus(indicatorSummaryDto.getDiffusionVersion().getProcStatus()));
            setDiffusionNeedsUpdate(indicatorSummaryDto.getDiffusionVersion().getNeedsUpdate());
            setDiffusionVersionNumber(indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
        }
        // Production version
        if (indicatorSummaryDto.getProductionVersion() != null) {
            // Overwrite name if production version exists
            setName(getLocalisedString(indicatorSummaryDto.getProductionVersion().getTitle()));
            setProcStatus(CommonUtils.getIndicatorProcStatus(indicatorSummaryDto.getProductionVersion().getProcStatus()));
            setNeedsUpdate(indicatorSummaryDto.getProductionVersion().getNeedsUpdate());
            setVersionNumber(indicatorSummaryDto.getProductionVersion().getVersionNumber());
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

}
