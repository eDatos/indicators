package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.rest.i18n.Translations;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.JsonStatCategoryType;
import es.gobcan.istac.indicators.rest.types.JsonStatDimensionType;
import es.gobcan.istac.indicators.rest.types.JsonStatUnitType;

import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ABSOLUTE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ANNUAL_PERCENTAGE_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ANNUAL_PUNTUAL_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_INTERPERIOD_PERCENTAGE_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_INTERPERIOD_PUNTUAL_RATE;

@Component
public class Do2JsonStatMapperUtil {
    public static final String JSON_STAT_VERSION = "2.0";
    public static final String JSON_STAT_CLASS = "dataset";

    @Autowired
    private IndicatorsApiService indicatorsApiService;

    @Autowired
    private Translations translations;

    public List<String> createJsonStatId() {
        List<String> format = new ArrayList<>();
        format.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        format.add(IndicatorDataDimensionTypeEnum.TIME.name());
        format.add(IndicatorDataDimensionTypeEnum.MEASURE.name());
        return format;
    }

    public Map<String, JsonStatDimensionType> toJsonStatDimensions(IndicatorVersion source) throws MetamacException {
        Map<String, JsonStatDimensionType> jsonStatDimensionsMap = new HashMap<>();

        // Geographical
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);
        JsonStatDimensionType geographicalDimension = createGeographicalDimension(geographicalValues);
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geographicalDimension);

        // Time
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);
        JsonStatDimensionType timeDimension = createTimeDimension(timeValues);
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.TIME.name(), timeDimension);

        // Measure
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);
        JsonStatDimensionType measureDimension = createMeasureDimension(measureValues, source);
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureDimension);

        return jsonStatDimensionsMap;
    }

    private JsonStatDimensionType createGeographicalDimension(List<GeographicalValueVO> geographicalValues) {
        JsonStatDimensionType geographicalDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_GEOGRAPHIC_NAME);
        geographicalDimension.setLabel(dimensionLabel);
        geographicalDimension.setCategory(createGeographicalCategory(geographicalValues));
        return geographicalDimension;
    }

    private JsonStatCategoryType createGeographicalCategory(List<GeographicalValueVO> geographicalValues) {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }
        JsonStatCategoryType category = new JsonStatCategoryType();
        for (int i = 0; i < geographicalValues.size(); i++) {
            GeographicalValueVO geographicalValue = geographicalValues.get(i);
            String categoryCode = geographicalValue.getCode();
            category.getIndex().put(categoryCode, (long) i); // TODO EDATOS-3663: who cares about self-generated indices, right?????
            category.getLabel().put(categoryCode, MapperUtil.getDefaultValue(geographicalValue.getTitle()));
        }
        return category;
    }

    private JsonStatDimensionType createTimeDimension(List<TimeValue> timeValues) {
        JsonStatDimensionType timeDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_TIME_NAME);
        timeDimension.setLabel(dimensionLabel);
        timeDimension.setCategory(createTimeCategory(timeValues));
        return timeDimension;
    }

    private JsonStatCategoryType createTimeCategory(List<TimeValue> timeValues) {
        if (CollectionUtils.isEmpty(timeValues)) {
            return null;
        }
        JsonStatCategoryType category = new JsonStatCategoryType();
        for (int i = 0; i < timeValues.size(); i++) {
            TimeValue timeValue = timeValues.get(i);
            String categoryCode = timeValue.getTimeValue();
            category.getIndex().put(categoryCode, (long) i); // TODO EDATOS-3663: who cares about self-generated indices, right?????
            category.getLabel().put(categoryCode, MapperUtil.getDefaultValue(timeValue.getTitle()));
        }
        return category;
    }

    private JsonStatDimensionType createMeasureDimension(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion) throws MetamacException {
        JsonStatDimensionType measureDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_MEASURE_NAME);
        measureDimension.setLabel(dimensionLabel);
        measureDimension.setCategory(measureValueDoToMeasureRepresentationType(measureValues, indicatorVersion));
        return measureDimension;
    }

    private JsonStatCategoryType measureValueDoToMeasureRepresentationType(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion) throws MetamacException {
        if (CollectionUtils.isEmpty(measureValues)) {
            return null;
        }
        JsonStatCategoryType category = new JsonStatCategoryType();
        for (int i = 0; i < measureValues.size(); i++) {
            MeasureValue measureValue = measureValues.get(i);
            Quantity quantity = getQuantityForMeasure(measureValue.getMeasureValue(), indicatorVersion);
            String categoryCode = measureValue.getMeasureValue().name();
            String categoryLabel = toJsonStatCategoryCode(measureValue.getMeasureValue());
            category.getLabel().put(categoryCode, categoryLabel);
            category.getIndex().put(categoryCode, (long) i);
            category.getUnit().put(categoryCode, toJsonStatUnit(quantity));
        }
        return category;
    }

    private String toJsonStatCategoryCode(MeasureDimensionTypeEnum measureValue) {
        switch (measureValue) {
            case ABSOLUTE:
                return translations.get(MEASURE_ABSOLUTE);
            case ANNUAL_PUNTUAL_RATE:
                return translations.get(MEASURE_ANNUAL_PUNTUAL_RATE);
            case ANNUAL_PERCENTAGE_RATE:
                return translations.get(MEASURE_ANNUAL_PERCENTAGE_RATE);
            case INTERPERIOD_PUNTUAL_RATE:
                return translations.get(MEASURE_INTERPERIOD_PUNTUAL_RATE);
            case INTERPERIOD_PERCENTAGE_RATE:
                return translations.get(MEASURE_INTERPERIOD_PERCENTAGE_RATE);
            default:
                return null;
        }
    }

    private JsonStatUnitType toJsonStatUnit(Quantity quantity) {
        JsonStatUnitType unit = new JsonStatUnitType();
        unit.setDecimals(quantity.getDecimalPlaces());
        unit.setMultiplier(quantity.getUnitMultiplier().getUnitMultiplier());
        unit.setSymbol(quantity.getUnit().getSymbol());
        QuantityUnitSymbolPositionEnum symbolPosition = quantity.getUnit().getSymbolPosition();
        if (symbolPosition != null) {
            unit.setPosition(symbolPosition.toString());
        }
        unit.setLabel(MapperUtil.getDefaultValue(quantity.getUnit().getTitle()));
        unit.setType(quantity.getQuantityType().toString());
        return unit;
    }

    private Quantity getQuantityForMeasure(MeasureDimensionTypeEnum measure, IndicatorVersion indicatorVersion) {
        switch (measure) {
            case ABSOLUTE:
                return indicatorVersion.getQuantity();
            default:
                RateDerivation rate = getRateDerivationForMeasure(measure, indicatorVersion); // TODO EDATOS-3663: test this, how it works??
                if (rate != null) {
                    return rate.getQuantity();
                }
        }
        return null;
    }

    private RateDerivation getRateDerivationForMeasure(MeasureDimensionTypeEnum measure, IndicatorVersion indicatorVersion) {
        for (DataSource datasource : indicatorVersion.getDataSources()) {
            switch (measure) {
                case ANNUAL_PERCENTAGE_RATE:
                    if (datasource.getAnnualPercentageRate() != null) {
                        return datasource.getAnnualPercentageRate();
                    }
                    break;
                case ANNUAL_PUNTUAL_RATE:
                    if (datasource.getAnnualPuntualRate() != null) {
                        return datasource.getAnnualPuntualRate();
                    }
                    break;
                case INTERPERIOD_PERCENTAGE_RATE:
                    if (datasource.getInterperiodPercentageRate() != null) {
                        return datasource.getInterperiodPercentageRate();
                    }
                    break;
                case INTERPERIOD_PUNTUAL_RATE:
                    if (datasource.getInterperiodPuntualRate() != null) {
                        return datasource.getInterperiodPuntualRate();
                    }
                    break;
            }
        }
        return null;
    }

    public List<Long> toJsonStatSize(IndicatorVersion source) throws MetamacException {
        long geographicalValuesSize = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source).size();
        long timeValuesSize = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source).size();
        long measureValuesSize = indicatorsApiService.retrieveMeasureValuesInIndicator(source).size();
        return Arrays.asList(geographicalValuesSize, timeValuesSize, measureValuesSize);
    }
}
