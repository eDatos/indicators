package es.gobcan.istac.indicators.web.client.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.web.client.model.DataDefinitionRecord;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.DataSourceVariableRecord;
import es.gobcan.istac.indicators.web.client.model.GeoGranularityRecord;
import es.gobcan.istac.indicators.web.client.model.GeoValueRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorSimpleRecord;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.QuantityUnitRecord;
import es.gobcan.istac.indicators.web.client.model.UnitMultiplierRecord;
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
        DataSourceRecord record = new DataSourceRecord(dataSourceDto.getUuid(), dataSourceDto.getQueryUuid(), dataSourceDto.getQueryUrn(), dataSourceDto);
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

    public static QuantityUnitRecord getQuantityUnitRecord(QuantityUnitDto quantityUnitDto) {
        QuantityUnitRecord record = new QuantityUnitRecord(quantityUnitDto.getUuid(), getLocalisedString(quantityUnitDto.getTitle()), quantityUnitDto);
        return record;
    }

    public static GeoGranularityRecord getGeoGranularityRecord(GeographicalGranularityDto dto) {
        GeoGranularityRecord record = new GeoGranularityRecord(dto.getUuid(), getLocalisedString(dto.getTitle()), dto.getCode(), dto);
        return record;
    }

    public static GeoValueRecord getGeoValueRecord(GeographicalValueDto dto) {
        GeoValueRecord record = new GeoValueRecord(dto.getUuid(), getLocalisedString(dto.getTitle()), dto.getCode(), getLocalisedString(dto.getGranularity().getTitle()), dto.getOrder(), dto);
        return record;
    }

    public static UnitMultiplierRecord getUnitMultiplierRecord(UnitMultiplierDto dto) {
        UnitMultiplierRecord record = new UnitMultiplierRecord(dto.getUuid(), getLocalisedString(dto.getTitle()), dto.getUnitMultiplier(), dto);
        return record;
    }

}
