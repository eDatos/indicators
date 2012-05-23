package es.gobcan.istac.indicators.web.client.utils;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.web.client.model.DataDefinitionRecord;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.DataSourceVariableRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorSimpleRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class RecordUtils {

    public static IndicatorRecord getIndicatorRecord(IndicatorDto indicatorDto) {
        IndicatorRecord record = new IndicatorRecord(indicatorDto);
        return record;
    }

    public static IndicatorRecord getIndicatorRecord(IndicatorSummaryDto indicatorSummaryDto) {
        IndicatorRecord record = new IndicatorRecord(indicatorSummaryDto);
        return record;
    }

    public static IndicatorSimpleRecord getIndicatorSimpleRecord(IndicatorSummaryDto indicatorSummaryDto) {
        IndicatorSimpleRecord record = new IndicatorSimpleRecord(indicatorSummaryDto);
        return record;
    }

    public static IndicatorSystemRecord getIndicatorsSystemRecord(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb) {
        IndicatorSystemRecord record = new IndicatorSystemRecord(indicatorsSystemDtoWeb);
        return record;
    }

    public static IndicatorSystemRecord getIndicatorsSystemRecord(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb) {
        IndicatorSystemRecord record = new IndicatorSystemRecord(indicatorsSystemDtoWeb);
        return record;
    }

    public static DataSourceRecord getDataSourceRecord(DataSourceDto dataSourceDto) {
        DataSourceRecord record = new DataSourceRecord(dataSourceDto.getUuid(), dataSourceDto.getDataGpeUuid(), dataSourceDto.getPxUri(), dataSourceDto);
        return record;
    }

    public static DataSourceVariableRecord getDataSourceVariableRecord(DataSourceVariableDto dataSourceVariableDto) {
        DataSourceVariableRecord record = new DataSourceVariableRecord(dataSourceVariableDto.getVariable(), dataSourceVariableDto.getCategory(), dataSourceVariableDto);
        return record;
    }

    public static DataDefinitionRecord getDataDefinitionRecord(DataDefinitionDto dataDefinitionDto) {
        DataDefinitionRecord record = new DataDefinitionRecord(dataDefinitionDto.getUuid(), dataDefinitionDto.getName(), dataDefinitionDto.getPxUri(), dataDefinitionDto);
        return record;
    }

}
