package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;


public class RecordUtils {

    public static IndicatorRecord getIndicatorRecord(IndicatorDto indicatorDto) {
        IndicatorRecord record = new IndicatorRecord(indicatorDto.getUuid(), 
                indicatorDto.getCode(), 
                getLocalisedString(indicatorDto.getTitle()), 
                getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorDto.getProcStatus().getName()));
        return record;
    }
    
    public static IndicatorSystemRecord getIndicatorsSystemRecord(IndicatorsSystemDto indicatorsSystemDto) {
        IndicatorSystemRecord record = new IndicatorSystemRecord(indicatorsSystemDto.getUuid(), 
                indicatorsSystemDto.getCode(), 
                getLocalisedString(indicatorsSystemDto.getTitle()),
                getCoreMessages().getString(getCoreMessages().indicatorsSystemProcStatusEnum() + indicatorsSystemDto.getProcStatus().getName()));
        return record;
    }
    
    public static DataSourceRecord getDataSourceRecord(DataSourceDto dataSourceDto) {
        DataSourceRecord record = new DataSourceRecord(dataSourceDto.getUuid(), 
                dataSourceDto.getQueryGpe(), 
                dataSourceDto.getPx(),
                dataSourceDto);
        return record;
    }
    
}
