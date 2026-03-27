package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ABSOLUTE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ANNUAL_PERCENTAGE_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_ANNUAL_PUNTUAL_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_INTERPERIOD_PERCENTAGE_RATE;
import static es.gobcan.istac.indicators.rest.i18n.Translations.MEASURE_INTERPERIOD_PUNTUAL_RATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.i18n.Translations;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.JsonStatCategoryType;
import es.gobcan.istac.indicators.rest.types.JsonStatDimensionType;
import es.gobcan.istac.indicators.rest.types.JsonStatExtensionType;
import es.gobcan.istac.indicators.rest.types.JsonStatUnitType;
import es.gobcan.istac.indicators.rest.types.QuantityType;
import es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum;

@Component
public class Do2JsonStatMapperUtil {

    public static final String          GEO_ROLE              = "geo";
    public static final String          TIME_ROLE             = "time";
    public static final String          METRIC_ROLE           = "metric";

    @Autowired
    private IndicatorsApiService        indicatorsApiService;

    @Autowired
    private Translations                translations;

    @Autowired
    private MetadataProperties          metadataProperties;

    @Autowired
    private final SrmRestInternalFacade srmRestInternalFacade = null;

    public List<String> createJsonStatId() {
        List<String> format = new ArrayList<>();
        format.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        format.add(IndicatorDataDimensionTypeEnum.TIME.name());
        format.add(IndicatorDataDimensionTypeEnum.MEASURE.name());
        return format;
    }

    public Map<String, JsonStatDimensionType> toJsonStatDimensions(IndicatorVersion source, IndicatorObservationsExtendedVO observations) throws MetamacException {
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);
        return toJsonStatDimensions(geographicalValues, timeValues, measureValues, source, observations);
    }

    public Map<String, JsonStatDimensionType> toJsonStatDimensions(IndicatorInstance indicatorInstance, IndicatorVersion source, IndicatorObservationsExtendedVO observations) throws MetamacException {
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(indicatorInstance.getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(indicatorInstance.getUuid());
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(indicatorInstance.getUuid());
        return toJsonStatDimensions(geographicalValues, timeValues, measureValues, source, observations);
    }

    public Map<String, JsonStatDimensionType> toJsonStatDimensions(List<GeographicalValueVO> geographicalValues, List<TimeValue> timeValues, List<MeasureValue> measureValues, IndicatorVersion source,
            IndicatorObservationsExtendedVO observations) throws MetamacException {
        Map<String, JsonStatDimensionType> jsonStatDimensionsMap = new HashMap<>();

        // Geographical
        JsonStatDimensionType geographicalDimension = createGeographicalDimension(geographicalValues, observations.getGeographicalCodes());
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geographicalDimension);

        // Time
        JsonStatDimensionType timeDimension = createTimeDimension(timeValues, observations.getTimeCodes());
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.TIME.name(), timeDimension);

        // Measure
        JsonStatDimensionType measureDimension = createMeasureDimension(measureValues, source, observations.getMeasureCodes());
        jsonStatDimensionsMap.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureDimension);

        return jsonStatDimensionsMap;
    }

    private JsonStatDimensionType createGeographicalDimension(List<GeographicalValueVO> geographicalValues, List<String> filterGeographicalCodes) throws MetamacException {
        JsonStatDimensionType geographicalDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_GEOGRAPHIC_NAME);
        geographicalDimension.setLabel(dimensionLabel);
        geographicalDimension.setCategory(createGeographicalCategory(geographicalValues, filterGeographicalCodes));
        return geographicalDimension;
    }

    private JsonStatCategoryType createGeographicalCategory(List<GeographicalValueVO> geographicalValues, List<String> filterGeographicalCodes) throws MetamacException {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }

        Map<String, String> geographicalValuesCodes = srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn());

        JsonStatCategoryType category = new JsonStatCategoryType();
        int i = 0;
        for (GeographicalValueVO geographicalValue : geographicalValues) {
            String categoryCode = geographicalValue.getCode();
            String codeId = geographicalValuesCodes.get(categoryCode);
            if (filterGeographicalCodes.contains(categoryCode)) {
                category.getIndex().put(codeId, (long) i);
                category.getLabel().put(codeId, MapperUtil.getDefaultValue(geographicalValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
                i++;
            }
        }
        return category;
    }

    private JsonStatDimensionType createTimeDimension(List<TimeValue> timeValues, List<String> filterTimeCodes) {
        JsonStatDimensionType timeDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_TIME_NAME);
        timeDimension.setLabel(dimensionLabel);
        timeDimension.setCategory(createTimeCategory(timeValues, filterTimeCodes));
        return timeDimension;
    }

    private JsonStatCategoryType createTimeCategory(List<TimeValue> timeValues, List<String> filterTimeCodes) {
        if (CollectionUtils.isEmpty(timeValues)) {
            return null;
        }
        JsonStatCategoryType category = new JsonStatCategoryType();
        int i = 0;
        for (TimeValue timeValue : timeValues) {
            String categoryCode = timeValue.getTimeValue();
            if (filterTimeCodes.contains(categoryCode)) {
                category.getIndex().put(categoryCode, (long) i);
                category.getLabel().put(categoryCode, MapperUtil.getDefaultValue(timeValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
                i++;
            }
        }
        return category;
    }

    private JsonStatDimensionType createMeasureDimension(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, List<String> filterMeasureCodes) throws MetamacException {
        JsonStatDimensionType measureDimension = new JsonStatDimensionType();
        String dimensionLabel = translations.get(Translations.DIMENSIONS_MEASURE_NAME);
        measureDimension.setLabel(dimensionLabel);
        measureDimension.setCategory(measureValueDoToMeasureRepresentationType(measureValues, indicatorVersion, filterMeasureCodes));
        return measureDimension;
    }

    private JsonStatCategoryType measureValueDoToMeasureRepresentationType(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, List<String> filterMeasureCodes)
            throws MetamacException {
        if (CollectionUtils.isEmpty(measureValues)) {
            return null;
        }
        JsonStatCategoryType category = new JsonStatCategoryType();
        int i = 0;
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        for (MeasureValue measureValue : measureValues) {
            String categoryCode = measureValue.getMeasureValue().name();
            if (filterMeasureCodes.contains(categoryCode)) {
                String categoryLabel = toJsonStatCategoryCode(measureValue.getMeasureValue());
                Quantity quantity = getQuantityForMeasure(measureValue.getMeasureValue(), indicatorVersion);
                category.getLabel().put(categoryCode, categoryLabel);
                category.getIndex().put(categoryCode, (long) i);
                category.getUnit().put(categoryCode, toJsonStatUnit(quantity, srmRestObjectsMapper));
                i++;
            }
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

    private JsonStatUnitType toJsonStatUnit(Quantity quantity, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        if (quantity == null) {
            return null;
        }
        JsonStatUnitType unit = new JsonStatUnitType();
        unit.setDecimals(quantity.getDecimalPlaces());
        unit.setMultiplier(quantity.getUnitMultiplier().getUnitMultiplier());

        QuantityType quantityType = new QuantityType();
        MapperUtil.setQuantityUnitMetadata(quantity, quantityType, metadataProperties, srmRestInternalFacade, srmRestObjectsMapper);

        unit.setSymbol(quantityType.getUnitSymbol());
        QuantityUnitSymbolPositionEnum symbolPosition = quantityType.getUnitSymbolPosition();
        if (symbolPosition != null) {
            unit.setPosition(symbolPosition.toString());
        }

        unit.setLabel(MapperUtil.getDefaultValue(quantity.getUnit().getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        unit.setType(quantity.getQuantityType().toString());
        return unit;
    }

    private Quantity getQuantityForMeasure(MeasureDimensionTypeEnum measure, IndicatorVersion indicatorVersion) {
        switch (measure) {
            case ABSOLUTE:
                return indicatorVersion.getQuantity();
            default:
                RateDerivation rate = getRateDerivationForMeasure(measure, indicatorVersion);
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

    public List<Long> toJsonStatSize(IndicatorObservationsExtendedVO source) {
        long geographicalValuesSize = source.getGeographicalCodes().size();
        long timeValuesSize = source.getTimeCodes().size();
        long measureValuesSize = source.getMeasureCodes().size();
        return Arrays.asList(geographicalValuesSize, timeValuesSize, measureValuesSize);
    }

    public List<String> toJsonStatValue(IndicatorObservationsExtendedVO dataTypeRequest) {
        List<String> geographicalCodes = dataTypeRequest.getGeographicalCodes();
        List<String> timeValues = dataTypeRequest.getTimeCodes();
        List<String> measureValues = dataTypeRequest.getMeasureCodes();
        Map<String, ObservationExtendedDto> observationMap = dataTypeRequest.getObservations();

        List<String> observations = new ArrayList<>();
        if (!observationMap.isEmpty()) {
            for (String geographicalCode : geographicalCodes) {
                for (String timeValueCode : timeValues) {
                    for (String measureValueCode : measureValues) {
                        // Observation ID: Be careful!!! don't change order of ids
                        String id = geographicalCode + "#" + timeValueCode + "#" + measureValueCode;
                        ObservationExtendedDto observationDto = observationMap.get(id);
                        if (observationDto == null) {
                            observations.add(null);
                        } else {
                            observations.add(observationDto.getPrimaryMeasure());
                        }
                    }
                }
            }
        }
        return observations;
    }

    public JsonStatExtensionType toJsonStatExtension(IndicatorVersion source) {
        return getJsonStatExtensionType(source.getCode(), source.getUuid(), source.getTitle());
    }

    public JsonStatExtensionType toJsonStatExtension(IndicatorInstance source) {
        return getJsonStatExtensionType(source.getCode(), source.getUuid(), source.getTitle());
    }

    private JsonStatExtensionType getJsonStatExtensionType(String code, String uuid, InternationalString title) {
        JsonStatExtensionType extension = new JsonStatExtensionType();
        extension.setIndicatorId(code);
        extension.setIndicatorUuid(uuid);
        StringJoiner joiner = new StringJoiner(",");
        for (LocalisedString localisedString : title.getTexts()) {
            String locale = localisedString.getLocale();
            joiner.add(locale);
        }
        extension.setLang(joiner.toString());
        return extension;
    }

    public Map<String, List<String>> createJsonStatRole() {
        Map<String, List<String>> role = new HashMap<>();
        role.put(GEO_ROLE, Collections.singletonList(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name()));
        role.put(TIME_ROLE, Collections.singletonList(IndicatorDataDimensionTypeEnum.TIME.name()));
        role.put(METRIC_ROLE, Collections.singletonList(IndicatorDataDimensionTypeEnum.MEASURE.name()));
        return role;
    }

    public List<String> toJsonStatNotes(InternationalString existingNotes, IndicatorObservationsExtendedVO observations,
            Map<String, JsonStatDimensionType> dimensions) throws MetamacException {
        List<String> notes = new ArrayList<String>();

        // 1. Existing indicator notes (preserve current behavior)
        String indicatorNoteText = MapperUtil.getDefaultValue(existingNotes, metadataProperties.getDefaultInternationalizationLanguage());
        if (indicatorNoteText != null) {
            notes.add(indicatorNoteText);
        }

        // 2. DATASET/DIMENSION/GROUP attribute notes
        String defaultLang = metadataProperties.getDefaultInternationalizationLanguage();
        List<AttributeInstanceDto> attrInstances = observations.getDatasetAndDimensionAttributes();
        if (attrInstances != null) {
            // Pre-compute geo element ID → codelist code map once (for resolving GEO codes to labels)
            Map<String, String> geoElementToCodelistCode = buildGeoElementIdToCodelistCodeMap();
            Map<String, String> datasetEnumLabels = observations.getDatasetEnumLabels();
            for (AttributeInstanceDto instance : attrInstances) {
                String noteText = buildAttributeNote(instance, defaultLang, dimensions, geoElementToCodelistCode, datasetEnumLabels);
                if (noteText != null && !noteText.trim().isEmpty()) {
                    notes.add(noteText);
                }
            }
        }

        return notes.isEmpty() ? null : notes;
    }

    private Map<String, String> buildGeoElementIdToCodelistCodeMap() throws MetamacException {
        return srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn());
    }

    private String buildAttributeNote(AttributeInstanceDto instance, String defaultLang,
            Map<String, JsonStatDimensionType> dimensions, Map<String, String> geoElementToCodelistCode,
            Map<String, String> datasetEnumLabels) {
        InternationalStringDto value = instance.getValue();
        if (value == null) {
            return null;
        }
        String valueText = MapperUtil.getDefaultLabel(value, defaultLang);
        if (valueText == null || valueText.trim().isEmpty()) {
            return null;
        }

        Map<String, List<String>> codesByDimension = instance.getCodesByDimension();
        if (codesByDimension == null || codesByDimension.isEmpty()) {
            // DATASET level: usar etiqueta resuelta si disponible, fallback al valor raw
            String label = (datasetEnumLabels != null) ? datasetEnumLabels.get(instance.getAttributeId()) : null;
            return label != null ? label : valueText;
        }

        // DIMENSION/GROUP level: "label1, label2. value"
        List<String> allLabels = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : codesByDimension.entrySet()) {
            String dimKey = entry.getKey();
            for (String code : entry.getValue()) {
                String label = resolveDimensionCodeToLabel(dimKey, code, dimensions, geoElementToCodelistCode);
                allLabels.add(label);
            }
        }
        return StringUtils.join(allLabels, ", ") + ". " + valueText;
    }

    private String resolveDimensionCodeToLabel(String dimKey, String code, Map<String, JsonStatDimensionType> dimensions,
            Map<String, String> geoElementToCodelistCode) {
        JsonStatDimensionType dimension = dimensions != null ? dimensions.get(dimKey) : null;
        if (IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name().equals(dimKey)) {
            // code is element ID (e.g. MUN_GARAFIA); bridge via codelist code to label
            String codelistCode = geoElementToCodelistCode.get(code);
            if (codelistCode != null && dimension != null && dimension.getCategory() != null) {
                String label = dimension.getCategory().getLabel().get(codelistCode);
                if (label != null) {
                    return label;
                }
            }
        } else if (dimension != null && dimension.getCategory() != null) {
            // MEASURE and TIME: code is a direct key in category.label
            String label = dimension.getCategory().getLabel().get(code);
            if (label != null) {
                return label;
            }
        }
        // Fallback: return raw code
        return code;
    }

}
