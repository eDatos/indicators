package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueProperties;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.dspl.DsplConcept;
import es.gobcan.istac.indicators.core.dspl.DsplConceptAttribute;
import es.gobcan.istac.indicators.core.dspl.DsplData;
import es.gobcan.istac.indicators.core.dspl.DsplData.DateColumn;
import es.gobcan.istac.indicators.core.dspl.DsplData.FloatColumn;
import es.gobcan.istac.indicators.core.dspl.DsplData.Row;
import es.gobcan.istac.indicators.core.dspl.DsplData.TextColumn;
import es.gobcan.istac.indicators.core.dspl.DsplDataset;
import es.gobcan.istac.indicators.core.dspl.DsplInfo;
import es.gobcan.istac.indicators.core.dspl.DsplLocalisedValue;
import es.gobcan.istac.indicators.core.dspl.DsplSimpleAttribute;
import es.gobcan.istac.indicators.core.dspl.DsplSlice;
import es.gobcan.istac.indicators.core.dspl.DsplTable;
import es.gobcan.istac.indicators.core.dspl.DsplTopic;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.serviceimpl.IndicatorsDataServiceImpl;

public class DsplTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(DsplTransformer.class);
    private IndicatorsSystemsService indicatorsSystemsService;
    private IndicatorsDataService    indicatorsDataService;
    private IndicatorsService        indicatorsService;
    private ConfigurationService     configurationService;

    private static final String      PROP_PROVIDER_NAME                = "indicators.dspl.provider.name";
    private static final String      PROP_PROVIDER_DESCRIPTION         = "indicators.dspl.provider.description";
    private static final String      PROP_PROVIDER_URL                 = "indicators.dspl.provider.url";
    private static final String      PROP_SYSTEM_URL_TEMPLATE          = "indicators.dspl.indicators.system.url";

    private static final String      GEO_CONCEPT_BASE                  = "geo:location";
    private static final String      UNIT_CONCEPT_BASE                 = "unit:unit";
    private static final String      GEO_CONCEPT_PREFIX                = "geo_";

    private static final String      QUANTITY_CONCEPT_BASE             = "quantity:quantity";
    private static final String      QUANTITY_AMOUNT_CONCEPT_BASE      = "quantity:amount";
    private static final String      QUANTITY_MAGNITUDE_CONCEPT_BASE   = "quantity:magnitude";
    private static final String      QUANTITY_FRACTION_CONCEPT_BASE    = "quantity:fraction";
    private static final String      QUANTITY_RATE_CONCEPT_BASE        = "quantity:rate";
    private static final String      QUANTITY_RATIO_CONCEPT_BASE       = "quantity:ratio";
    private static final String      QUANTITY_INDEX_CONCEPT_BASE       = "quantity:index";
    private static final String      QUANTITY_CHANGE_RATE_CONCEPT_BASE = "quantity:change_rate";

    public DsplTransformer(IndicatorsSystemsService indicatorsSystemsService, IndicatorsDataService indicatorsDataService, IndicatorsService indicatorsService,
            ConfigurationService configurationService) {
        this.indicatorsSystemsService = indicatorsSystemsService;
        this.indicatorsDataService = indicatorsDataService;
        this.indicatorsService = indicatorsService;
        this.configurationService = configurationService;
    }
    public List<DsplDataset> transformIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, InternationalString title, InternationalString description) throws MetamacException {
        try {
            LOG.info("Building dspl for indicators System "+indicatorsSystemUuid);
            
            IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublished(ctx, indicatorsSystemUuid);
            List<ElementLevel> structure = indicatorsSystemsService.retrieveIndicatorsSystemStructure(ctx, indicatorsSystemVersion.getIndicatorsSystem().getUuid(),
                    indicatorsSystemVersion.getVersionNumber());

            LOG.info("Retrieving indicators instances...");
            List<IndicatorInstance> instances = filterIndicatorsInstances(structure);

            // organize by time granularity
            Map<TimeGranularityEnum, List<IndicatorInstance>> instancesByGranularity = organizeIndicatorsInstancesByTimeGranularity(ctx, instances);

            List<DsplDataset> datasets = new ArrayList<DsplDataset>();
            for (TimeGranularityEnum timeGranularity : instancesByGranularity.keySet()) {
                LOG.info("Processing indicators instances with granularity "+timeGranularity+" ...");
                
                List<IndicatorInstance> instancesInGranularity = instancesByGranularity.get(timeGranularity);

                // topics
                LOG.info("Building topics with granularity "+timeGranularity+" ...");
                Set<DsplTopic> topics = buildTopicsForInstances(instancesInGranularity);

                // concepts
                LOG.info("Building concepts with granularity "+timeGranularity+" ...");
                List<DsplConcept> concepts = new ArrayList<DsplConcept>();
                concepts.addAll(buildStandardConceptsForGeoDimensions(ctx, instancesInGranularity));
                List<DsplConcept> metrics = buildMetricsForInstances(ctx, instancesInGranularity);
                concepts.addAll(metrics);

                // slides
                LOG.info("Computing slices with granularity "+timeGranularity+" ...");
                Set<DsplSlice> slices = createSlicesForInstancesWithTimeGranularity(ctx, instancesInGranularity, timeGranularity);

                if (slices.size() > 0) {
                    LOG.info("Building slices with granularity "+timeGranularity+" ...");
                    DsplInfo datasetInfo = buildDatasetInfo(ctx, indicatorsSystemVersion, title, description, timeGranularity);
                    DsplInfo providerInfo = buildProviderInfo();
                    String datasetId = buildDatasetId(indicatorsSystemVersion, timeGranularity);
                    DsplDataset dataset = new DsplDataset(datasetId, datasetInfo, providerInfo);

                    dataset.addConcepts(concepts);
                    dataset.addTopics(topics);
                    dataset.addSlices(slices);

                    datasets.add(dataset);
                    LOG.info("Dataset with granularity "+timeGranularity+" has been built");
                }
            }
            LOG.info("Dspl succesfully built for Indicators System: "+indicatorsSystemUuid);
            return datasets;
        } catch (MetamacException e) {
            throw new MetamacException(e, ServiceExceptionType.DSPL_STRUCTURE_CREATE_ERROR, indicatorsSystemUuid);
        }
    }

    private DsplInfo buildProviderInfo() throws MetamacException {
        DsplInfo providerInfo = new DsplInfo();

        String providerName = getProviderName();
        String providerDescription = getProviderDescription();
        String providerUrl = getProviderUrl();

        if (providerName != null) {
            providerInfo.getName().setValue(providerName);
        }

        if (providerDescription != null) {
            providerInfo.getDescription().setValue(providerDescription);
        }

        if (providerUrl != null) {
            providerInfo.getUrl().setValue(providerUrl);
        }

        return providerInfo;
    }

    private DsplInfo buildDatasetInfo(ServiceContext ctx, IndicatorsSystemVersion systemVersion, InternationalString title, InternationalString description, TimeGranularityEnum timeGranularity)
            throws MetamacException {
        DsplInfo datasetInfo = new DsplInfo();

        TimeGranularity granularity = indicatorsSystemsService.retrieveTimeGranularity(ctx, timeGranularity);

        for (LocalisedString text : title.getTexts()) {
            String baseTitle = buildTitleWithGranularityInLocale(text, granularity);
            datasetInfo.getName().setText(text.getLocale(), baseTitle);

            String localisedUrl = buildLocalisedSystemUrl(systemVersion, text.getLocale());
            datasetInfo.getUrl().setText(text.getLocale(), localisedUrl);
        }

        populateDsplLocalisedTextForInternString(datasetInfo.getDescription(), description);

        return datasetInfo;
    }

    private String buildTitleWithGranularityInLocale(LocalisedString text, TimeGranularity granularity) {
        String granularitySuffix = granularity.getTitle().getLocalisedLabel(text.getLocale());
        if (granularitySuffix == null) {
            granularitySuffix = granularity.getGranularity().name().toLowerCase();
        }
        return text.getLabel() + " (" + granularitySuffix + ")";
    }

    private String buildLocalisedSystemUrl(IndicatorsSystemVersion systemVersion, String locale) {
        String systemUrlBase = configurationService.getProperty(PROP_SYSTEM_URL_TEMPLATE);
        String url = systemUrlBase.replaceAll("\\[SYSTEM\\]", systemVersion.getIndicatorsSystem().getCode());
        return url + "?language=" + locale;
    }

    private String buildDatasetId(IndicatorsSystemVersion indicatorsSystemVersion, TimeGranularityEnum timeGranularity) {
        return indicatorsSystemVersion.getIndicatorsSystem().getCode() + "_" + timeGranularity.name().toLowerCase();
    }

    private List<IndicatorInstance> filterIndicatorsInstances(List<ElementLevel> structure) {
        List<IndicatorInstance> instances = new ArrayList<IndicatorInstance>();
        for (ElementLevel element : structure) {
            if (element.isIndicatorInstance()) {
                instances.add(element.getIndicatorInstance());
            } else if (element.isDimension()) {
                instances.addAll(filterIndicatorsInstances(element.getChildren()));
            }
        }
        return instances;
    }

    private Map<TimeGranularityEnum, List<IndicatorInstance>> organizeIndicatorsInstancesByTimeGranularity(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {
        Map<TimeGranularityEnum, List<IndicatorInstance>> instancesByGranularity = new HashMap<TimeGranularityEnum, List<IndicatorInstance>>();

        for (IndicatorInstance instance : instances) {
            List<TimeGranularity> granularities = indicatorsDataService.retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid());

            for (TimeGranularity granularity : granularities) {
                List<IndicatorInstance> instancesWithGranularity = instancesByGranularity.get(granularity.getGranularity());
                if (instancesWithGranularity == null) {
                    instancesWithGranularity = new ArrayList<IndicatorInstance>();
                    instancesByGranularity.put(granularity.getGranularity(), instancesWithGranularity);
                }
                instancesWithGranularity.add(instance);
            }

        }
        return instancesByGranularity;
    }

    private Set<DsplTopic> buildTopicsForInstances(List<IndicatorInstance> instances) {
        Set<DsplTopic> topics = new HashSet<DsplTopic>();
        for (IndicatorInstance instance : instances) {
            ElementLevel parentLevel = instance.getElementLevel().getParent();
            if (parentLevel != null) {
                getHierarchyTopics(parentLevel, topics);
            }
        }
        return topics;
    }

    private DsplTopic getHierarchyTopics(ElementLevel level, Set<DsplTopic> foundTopics) {
        if (level.isDimension()) {
            Dimension dim = level.getDimension();
            DsplTopic topic = buildTopicFromDimension(dim);
            foundTopics.add(topic);

            if (level.getParent() != null) {
                DsplTopic parentTopic = getHierarchyTopics(level.getParent(), foundTopics);
                if (parentTopic != null) {
                    topic.setParentTopic(parentTopic);
                }
            }
            return topic;
        }
        return null;
    }

    private DsplTopic buildTopicFromDimension(Dimension dimension) {
        DsplInfo info = new DsplInfo();
        for (LocalisedString text : dimension.getTitle().getTexts()) {
            info.getName().setText(text.getLocale(), text.getLabel());
        }
        return new DsplTopic(getIdForTopic(dimension), info);
    }

    private List<DsplConcept> buildStandardConceptsForGeoDimensions(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {
        Set<GeographicalGranularity> granularitiesUsed = calculateGeoGranularitiesUsedInInstances(ctx, instances);

        List<DsplConcept> concepts = new ArrayList<DsplConcept>();
        for (GeographicalGranularity granularity : granularitiesUsed) {
            concepts.add(createConceptForGeoGranularity(ctx, granularity));
        }
        return concepts;
    }

    private Set<GeographicalGranularity> calculateGeoGranularitiesUsedInInstances(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {
        Set<GeographicalGranularity> granularitiesUsed = new HashSet<GeographicalGranularity>();

        for (IndicatorInstance instance : instances) {
            granularitiesUsed.addAll(indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid()));
        }
        return granularitiesUsed;
    }

    private DsplConcept createConceptForGeoGranularity(ServiceContext ctx, GeographicalGranularity granularity) throws MetamacException {
        DsplInfo info = new DsplInfo();
        populateDsplLocalisedTextForInternString(info.getName(), granularity.getTitle());

        String id = getIdForGeoConcept(granularity);
        DsplConcept concept = new DsplConcept(id, info);
        concept.setExtend(GEO_CONCEPT_BASE);

        DsplTable table = createTableForGeoGranularity(ctx, granularity);
        concept.setTable(table);

        return concept;
    }

    private void populateDsplLocalisedTextForInternString(DsplLocalisedValue localisedText, InternationalString intStr) {
        if (intStr != null) {
            for (LocalisedString text : intStr.getTexts()) {
                localisedText.setText(text.getLocale(), text.getLabel());
            }
        }
    }

    private DsplTable createTableForGeoGranularity(ServiceContext ctx, GeographicalGranularity granularity) throws MetamacException {
        List<GeographicalValue> geoValues = getGeographicalValuesByGranularity(ctx, granularity);
        DsplTable table = new DsplTable(getTableIdForGeoConcept(granularity));

        DsplData data = createTableDataForGeoValues(granularity, geoValues);
        table.setData(data);

        return table;
    }

    private List<GeographicalValue> getGeographicalValuesByGranularity(ServiceContext ctx, GeographicalGranularity granularity) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeographicalValue.class).distinctRoot().withProperty(GeographicalValueProperties.granularity().code())
                .eq(granularity.getCode()).build();

        PagingParameter paging = PagingParameter.pageAccess(Integer.MAX_VALUE);

        PagedResult<GeographicalValue> result = indicatorsSystemsService.findGeographicalValues(ctx, conditions, paging);

        return result.getValues();
    }

    private DsplData createTableDataForGeoValues(GeographicalGranularity granularity, List<GeographicalValue> geoValues) {
        DsplData data = new DsplData();
        String idColumnName = getIdForGeoConcept(granularity);
        for (GeographicalValue geoValue : geoValues) {
            Row row = new Row();
            row.addColumn(new TextColumn(idColumnName), geoValue.getCode().toUpperCase());
            for (LocalisedString localisedStr : geoValue.getTitle().getTexts()) {
                row.addColumn(new TextColumn("name", localisedStr.getLocale()), localisedStr.getLabel());
            }
            row.addColumn(new FloatColumn("latitude"), String.valueOf(geoValue.getLatitude()));
            row.addColumn(new FloatColumn("longitude"), String.valueOf(geoValue.getLongitude()));
            data.setRows(Arrays.asList(row));
        }
        // Sort by col id
        data.setColumnsToOrder(Arrays.asList(idColumnName));

        return data;
    }

    private List<DsplConcept> buildMetricsForInstances(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {

        Set<IndicatorVersion> usedIndicators = calculateUsedIndicators(ctx, instances);

        List<DsplConcept> concepts = new ArrayList<DsplConcept>();

        concepts.addAll(createConceptsForUsedUnits(usedIndicators));

        concepts.addAll(createConceptsForUsedQuantitiesNotInstances(usedIndicators, instances));

        concepts.addAll(createConceptsForInstances(ctx, instances, usedIndicators));

        return concepts;
    }

    private Set<IndicatorVersion> calculateUsedIndicators(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {
        Set<IndicatorVersion> indicators = new HashSet<IndicatorVersion>();

        for (IndicatorInstance instance : instances) {
            Indicator indicator = instance.getIndicator();
            addReferencedIndicators(ctx, indicator.getUuid(), indicators);
        }

        return indicators;
    }

    private void addReferencedIndicators(ServiceContext ctx, String indicatorUuid, Set<IndicatorVersion> usedIndicators) throws MetamacException {
        IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicatorPublished(ctx, indicatorUuid);

        if (!usedIndicators.contains(indicatorVersion)) {
            usedIndicators.add(indicatorVersion);

            // TODO: referenced quantities seem not to work in data explorer. if a concept can link to other quantities in a near future this code should be uncommented

            /*
             * // For numerator and denominator quantities
             * if (isEqualsAny(indicatorVersion.getQuantity().getQuantityType(), QuantityTypeEnum.FRACTION, QuantityTypeEnum.RATIO, QuantityTypeEnum.INDEX, QuantityTypeEnum.CHANGE_RATE,
             * QuantityTypeEnum.RATE)) {
             * addReferencedIndicators(ctx, indicatorVersion.getQuantity().getNumerator().getUuid(), usedIndicators);
             * addReferencedIndicators(ctx, indicatorVersion.getQuantity().getDenominator().getUuid(), usedIndicators);
             * }
             * // Base quantity
             * if (isEqualsAny(indicatorVersion.getQuantity().getQuantityType(), QuantityTypeEnum.INDEX, QuantityTypeEnum.CHANGE_RATE)) {
             * addReferencedIndicators(ctx, indicatorVersion.getQuantity().getBaseQuantity().getUuid(), usedIndicators);
             * }
             */
        }
    }

    private Set<DsplConcept> createConceptsForUsedUnits(Set<IndicatorVersion> usedIndicators) {
        Set<QuantityUnit> units = calculateUsedQuantityUnits(usedIndicators);

        Set<DsplConcept> concepts = new HashSet<DsplConcept>();
        for (QuantityUnit unit : units) {
            concepts.add(createConceptForUnit(unit));
        }
        return concepts;
    }

    private Set<QuantityUnit> calculateUsedQuantityUnits(Set<IndicatorVersion> indicators) {
        Set<QuantityUnit> units = new HashSet<QuantityUnit>();

        for (IndicatorVersion indicatorVersion : indicators) {
            units.add(indicatorVersion.getQuantity().getUnit());
        }
        return units;
    }

    private DsplConcept createConceptForUnit(QuantityUnit unit) {
        DsplInfo info = new DsplInfo();
        populateDsplLocalisedTextForInternString(info.getName(), unit.getTitle());

        DsplConcept concept = new DsplConcept(getIdForUnitConcept(unit), info);
        concept.setExtend(UNIT_CONCEPT_BASE);

        DsplTable table = createTableForUnit(unit);
        concept.setTable(table);
        return concept;
    }

    private DsplTable createTableForUnit(QuantityUnit unit) {
        DsplTable table = new DsplTable(getTableIdForUnitConcept(unit));

        DsplData data = createTableDataForUnit(unit);
        table.setData(data);

        return table;
    }

    private DsplData createTableDataForUnit(QuantityUnit unit) {
        DsplData data = new DsplData();

        String idColumnName = getIdForUnitConcept(unit);

        Row row = new Row();
        row.addColumn(new TextColumn(idColumnName), unit.getUuid());
        for (LocalisedString localisedStr : unit.getTitle().getTexts()) {
            row.addColumn(new TextColumn("unit_text", localisedStr.getLocale()), localisedStr.getLabel());
        }
        if (unit.getSymbol() != null) {
            row.addColumn(new TextColumn("symbol"), unit.getSymbol());
            row.addColumn(new TextColumn("symbol_position"), unit.getSymbolPosition().name());
        }

        data.setRows(Arrays.asList(row));

        data.setColumnsToOrder(Arrays.asList(idColumnName)); // id is first column

        return data;
    }

    private Set<DsplConcept> createConceptsForUsedQuantitiesNotInstances(Set<IndicatorVersion> usedIndicators, List<IndicatorInstance> instances) {
        // TODO: Referenced quantities seem not to work in data explorer, if they are this method should be
        // implemented creating concepts associated to used quantities, but those quantities can't be instances quantities.
        return new HashSet<DsplConcept>();
    }

    private Set<DsplConcept> createConceptsForInstances(ServiceContext ctx, List<IndicatorInstance> instances, Set<IndicatorVersion> usedIndicators) {
        Map<String, List<IndicatorInstance>> instancesByIndicator = groupInstancesByIndicator(instances);

        Set<DsplConcept> concepts = new HashSet<DsplConcept>();
        for (IndicatorVersion indicatorVersion : usedIndicators) {

            DsplConcept concept = createConceptForQuantityIndicator(indicatorVersion);

            List<IndicatorInstance> instancesIndicator = instancesByIndicator.get(indicatorVersion.getIndicator().getUuid());
            if (instancesIndicator != null) {
                setConceptTopicBasedOnInstancesDimensions(concept, instancesIndicator);
            }
            concepts.add(concept);
        }
        return concepts;
    }

    private Map<String, List<IndicatorInstance>> groupInstancesByIndicator(List<IndicatorInstance> instances) {
        Map<String, List<IndicatorInstance>> instancesByIndicator = new HashMap<String, List<IndicatorInstance>>();

        for (IndicatorInstance instance : instances) {
            String indicatorUuid = instance.getIndicator().getUuid();

            List<IndicatorInstance> instancesIndicator = instancesByIndicator.get(indicatorUuid);
            if (instancesIndicator == null) {
                instancesIndicator = new ArrayList<IndicatorInstance>();
                instancesByIndicator.put(indicatorUuid, instancesIndicator);
            }
            instancesIndicator.add(instance);
        }
        return instancesByIndicator;
    }

    private DsplConcept createConceptForQuantityIndicator(IndicatorVersion quantityIndicator) {
        DsplInfo info = new DsplInfo();
        populateDsplLocalisedTextForInternString(info.getName(), quantityIndicator.getTitle());

        DsplConcept concept = new DsplConcept(getIdForQuantityIndicatorConcept(quantityIndicator.getIndicator()), info);
        Quantity quantity = quantityIndicator.getQuantity();

        setConceptAttributesBasedOnQuantity(concept, quantity);

        return concept;
    }

    private void setConceptAttributesBasedOnQuantity(DsplConcept concept, Quantity quantity) {
        switch (quantity.getQuantityType()) {
            case QUANTITY:
                applyConceptAttributesForQuantity(concept, quantity);
                break;
            case AMOUNT:
                applyConceptAttributesForAmountQuantity(concept, quantity);
                break;
            case MAGNITUDE:
                applyConceptAttributesForMagnitudeQuantity(concept, quantity);
                break;
            case FRACTION:
                applyConceptAttributesForFractionQuantity(concept, quantity);
                break;
            case RATIO:
                applyConceptAttributesForRatioQuantity(concept, quantity);
                break;
            case RATE:
                applyConceptAttributesForRateQuantity(concept, quantity);
                break;
            case INDEX:
                applyConceptAttributesForIndexQuantity(concept, quantity);
                break;
            case CHANGE_RATE:
                applyConceptAttributesForChangeRateQuantity(concept, quantity);
                break;
        }
    }

    private void applyConceptAttributesForAmountQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForQuantity(concept, quantity);
        concept.setExtend(QUANTITY_AMOUNT_CONCEPT_BASE);
    }

    private void applyConceptAttributesForMagnitudeQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForQuantity(concept, quantity);
        concept.setExtend(QUANTITY_MAGNITUDE_CONCEPT_BASE);
    }

    private void applyConceptAttributesForFractionQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForMagnitudeQuantity(concept, quantity);

        // TODO: Are numerator and numerator really accepted in data explorer?
        // https://developers.google.com/public-data/forum?place=topic%2Fdspl-discuss%2FQ-seXWg1b0c%2Fdiscussion
        concept.setExtend(QUANTITY_FRACTION_CONCEPT_BASE);
    }

    private void applyConceptAttributesForRatioQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForFractionQuantity(concept, quantity);

        if (quantity.getIsPercentage() != null) {
            DsplSimpleAttribute isPercAttribute = new DsplSimpleAttribute("is_percentage", "boolean", quantity.getIsPercentage().toString());
            concept.addAttribute(isPercAttribute);
        }

        if (quantity.getPercentageOf() != null) {
            DsplSimpleAttribute percOfAttribute = new DsplSimpleAttribute("percentage_of", "string", quantity.getPercentageOf());
            concept.addAttribute(percOfAttribute);
        }
        concept.setExtend(QUANTITY_RATIO_CONCEPT_BASE);
    }

    private void applyConceptAttributesForRateQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForRatioQuantity(concept, quantity);
        concept.setExtend(QUANTITY_RATE_CONCEPT_BASE);
    }

    private void applyConceptAttributesForIndexQuantity(DsplConcept concept, Quantity quantity) {
        // Data explorer does not support index. Ratio or magnitude should be used
        applyConceptAttributesForRatioQuantity(concept, quantity);
        // concept.setExtend(QUANTITY_INDEX_CONCEPT_BASE);
    }

    private void applyConceptAttributesForChangeRateQuantity(DsplConcept concept, Quantity quantity) {
        applyConceptAttributesForRateQuantity(concept, quantity);

        // TODO: links to concepts seem not to work in data explorer wait for answer in discussion forum
        // https://developers.google.com/public-data/forum?place=topic%2Fdspl-discuss%2FQ-seXWg1b0c%2Fdiscussion
        concept.setExtend(QUANTITY_CHANGE_RATE_CONCEPT_BASE);
    }

    private void applyConceptAttributesForQuantity(DsplConcept concept, Quantity quantity) {
        if (quantity.getUnit() != null) {
            String unitValue = quantity.getUnit().getUuid();
            DsplConceptAttribute attribute = new DsplConceptAttribute("unit", getIdForUnitConcept(quantity.getUnit()), unitValue);
            concept.addAttribute(attribute);
        }

        if (quantity.getSignificantDigits() != null) {
            DsplSimpleAttribute attribute = new DsplSimpleAttribute("significant_digits", "integer", quantity.getSignificantDigits().toString());
            concept.addAttribute(attribute);
        }

        if (quantity.getDecimalPlaces() != null) {
            DsplSimpleAttribute attribute = new DsplSimpleAttribute("decimal_places", "integer", quantity.getDecimalPlaces().toString());
            concept.addAttribute(attribute);
        }
        concept.setExtend(QUANTITY_CONCEPT_BASE);
    }

    private void setConceptTopicBasedOnInstancesDimensions(DsplConcept concept, List<IndicatorInstance> instancesIndicator) {
        Set<String> topicsIds = new HashSet<String>();
        for (IndicatorInstance instance : instancesIndicator) {
            ElementLevel parentElement = instance.getElementLevel().getParent();
            if (parentElement != null && parentElement.isDimension()) {
                topicsIds.add(getIdForTopic(parentElement.getDimension()));
            }
        }

        for (String topicId : topicsIds) {
            concept.addTopic(topicId);
        }
    }

    private Set<DsplSlice> createSlicesForInstancesWithTimeGranularity(ServiceContext ctx, List<IndicatorInstance> instances, TimeGranularityEnum timeGranularity) throws MetamacException {
        Set<DsplSlice> slices = new HashSet<DsplSlice>();

        Set<GeographicalGranularity> granularitiesUsed = calculateGeoGranularitiesUsedInInstances(ctx, instances);

        for (GeographicalGranularity geoGranularity : granularitiesUsed) {
            Set<IndicatorInstance> instancesUsingGeoGranularity = new HashSet<IndicatorInstance>();
            for (IndicatorInstance instance : instances) {
                if (indicatorInstanceUsesGeoGranularity(ctx, instance, geoGranularity)) {
                    instancesUsingGeoGranularity.add(instance);
                }
            }
            DsplSlice slice = createSlice(ctx, geoGranularity, timeGranularity, instancesUsingGeoGranularity);
            if (slice != null) {
                slices.add(slice);
            }
        }
        return slices;
    }

    private boolean indicatorInstanceUsesGeoGranularity(ServiceContext ctx, IndicatorInstance instance, GeographicalGranularity geoGranularity) throws MetamacException {
        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid());
        return granularities.contains(geoGranularity);
    }

    private DsplSlice createSlice(ServiceContext ctx, GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity, Set<IndicatorInstance> instancesUsingGeoGranularity)
            throws MetamacException {
        DsplTable table = createTableForSlice(ctx, geoGranularity, timeGranularity, instancesUsingGeoGranularity);
        if (table.getData().getColumnNames().size() == 0) { // No data
            return null;
        }

        DsplSlice slice = new DsplSlice(getIdForSlice(geoGranularity, timeGranularity));
        slice.setTable(table);

        slice.addDimension(getIdForGeoConcept(geoGranularity));
        slice.addDimension(getIdForTimeConcept(timeGranularity));

        for (IndicatorInstance instance : instancesUsingGeoGranularity) {
            String conceptId = getIdForQuantityIndicatorConcept(instance.getIndicator());
            if (!slice.getMetrics().contains(conceptId)) {
                slice.addMetric(conceptId);
            }
        }

        return slice;
    }

    private DsplTable createTableForSlice(ServiceContext ctx, GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity, Set<IndicatorInstance> instancesUsingGeoGranularity)
            throws MetamacException {
        String sliceId = getIdForSlice(geoGranularity, timeGranularity);
        DsplTable table = new DsplTable(getTableIdForSlice(sliceId));

        DsplData data = createTableDataForSlice(ctx, geoGranularity, timeGranularity, instancesUsingGeoGranularity);
        table.setData(data);

        return table;
    }

    private DsplData createTableDataForSlice(ServiceContext ctx, GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity, Set<IndicatorInstance> instances) throws MetamacException {
        DsplData data = new DsplData();

        Set<Row> rows = createTableDataRowsForSliceInIndicatorsInstances(ctx, instances, geoGranularity, timeGranularity);
        data.setRows(rows);

        // Set data order, first geo dim then time
        TextColumn colGeo = getColumnForGeo(geoGranularity);
        DateColumn colTime = getColumnForTime(timeGranularity);

        data.setColumnsToOrder(Arrays.asList(colGeo.getName(), colTime.getName()));

        return data;
    }
    private Set<Row> createTableDataRowsForSliceInIndicatorsInstances(ServiceContext ctx, Set<IndicatorInstance> instances, GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity)
            throws MetamacException {
        List<String> geoCodes = getCodesForInstancesGeoValues(ctx, instances, geoGranularity);
        List<String> timeCodes = getCodesForInstancesTimeValues(ctx, instances, timeGranularity);
        String measureCode = MeasureDimensionTypeEnum.ABSOLUTE.name();

        Map<String, Map<String, ObservationDto>> observationsByInstanceUuid = new HashMap<String, Map<String, ObservationDto>>();

        for (IndicatorInstance instance : instances) {
            Map<String, ObservationDto> observations = findObservationsForIndicatorInstance(ctx, instance, geoCodes, timeCodes, measureCode);
            observationsByInstanceUuid.put(instance.getUuid(), observations);
        }

        Set<Row> rows = new HashSet<DsplData.Row>();
        for (String geoCode : geoCodes) {
            for (String timeCode : timeCodes) {
                // CAUTION: Data Explorer does not support all kind of time granularities, so some time codes must be converted
                String dsplTimeCode = transformTimeCodeToDataExplorerCompatible(timeCode);
                Row row = new Row();
                row.addColumn(getColumnForGeo(geoGranularity), geoCode);
                row.addColumn(getColumnForTime(timeGranularity), dsplTimeCode);

                for (IndicatorInstance instance : instances) {
                    Map<String, ObservationDto> observations = observationsByInstanceUuid.get(instance.getUuid());

                    String observationKey = DtoUtils.generateUniqueKeyWithCodes(Arrays.asList(geoCode, timeCode, measureCode));
                    ObservationDto obs = observations.get(observationKey);
                    if (obs != null) {
                        row.addColumn(getColumnForInstanceMetric(instance), obs.getPrimaryMeasure());
                    } else {
                        row.addColumn(getColumnForInstanceMetric(instance), null);
                    }
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private List<String> getCodesForInstancesGeoValues(ServiceContext ctx, Set<IndicatorInstance> instances, GeographicalGranularity geoGranularity) throws MetamacException {
        Set<String> geoCodes = new HashSet<String>();
        for (IndicatorInstance instance : instances) {
            geoCodes.addAll(getCodesForInstanceGeoValues(ctx, instance, geoGranularity));
        }
        return new ArrayList<String>(geoCodes);
    }

    private List<String> getCodesForInstancesTimeValues(ServiceContext ctx, Set<IndicatorInstance> instances, TimeGranularityEnum timeGranularity) throws MetamacException {
        Set<String> timeCodes = new HashSet<String>();
        for (IndicatorInstance instance : instances) {
            timeCodes.addAll(getCodesForTimeValues(ctx, instance, timeGranularity));
        }
        return new ArrayList<String>(timeCodes);
    }

    private String transformTimeCodeToDataExplorerCompatible(String timeCode) throws MetamacException {
        TimeValue timeValue = TimeVariableUtils.parseTimeValue(timeCode);

        TimeValue compatTimeValue = timeValue;
        switch (timeValue.getGranularity()) {
            case BIYEARLY:
            case QUARTERLY:
                compatTimeValue = TimeVariableUtils.convertToLastMonth(timeValue);
                break;
            case WEEKLY:
                compatTimeValue = TimeVariableUtils.convertToLastDay(timeValue);
        }
        return compatTimeValue.getTimeValue();
    }

    private Map<String, ObservationDto> findObservationsForIndicatorInstance(ServiceContext ctx, IndicatorInstance instance, List<String> geoCodes, List<String> timeCodes, String measureCode)
            throws MetamacException {
        List<ConditionDimensionDto> conditions = new ArrayList<ConditionDimensionDto>();

        ConditionDimensionDto geoCondition = new ConditionDimensionDto();
        geoCondition.setDimensionId(IndicatorsDataServiceImpl.GEO_DIM);
        geoCondition.setCodesDimension(geoCodes);
        conditions.add(geoCondition);

        ConditionDimensionDto timeCondition = new ConditionDimensionDto();
        timeCondition.setDimensionId(IndicatorsDataServiceImpl.TIME_DIM);
        timeCondition.setCodesDimension(timeCodes);
        conditions.add(timeCondition);

        ConditionDimensionDto measureCondition = new ConditionDimensionDto();
        measureCondition.setDimensionId(IndicatorsDataServiceImpl.MEASURE_DIM);
        measureCondition.setCodesDimension(Arrays.asList(measureCode));
        conditions.add(measureCondition);

        return indicatorsDataService.findObservationsByDimensionsInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid(), conditions);
    }

    private List<String> getCodesForInstanceGeoValues(ServiceContext ctx, IndicatorInstance instance, GeographicalGranularity geoGranularity) throws MetamacException {
        List<GeographicalValue> geoValues = calculateGeoValuesByGranularityInIndicatorInstance(ctx, instance, geoGranularity);

        List<String> codes = new ArrayList<String>();

        for (GeographicalValue geoValue : geoValues) {
            codes.add(geoValue.getCode());
        }
        return codes;
    }

    private List<GeographicalValue> calculateGeoValuesByGranularityInIndicatorInstance(ServiceContext ctx, IndicatorInstance instance, GeographicalGranularity granularity) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(ctx, instance.getUuid(), granularity.getUuid());
    }

    private List<String> getCodesForTimeValues(ServiceContext ctx, IndicatorInstance instance, TimeGranularityEnum timeGranularity) throws MetamacException {
        List<TimeValue> timeValues = calculateTimeValuesByGranularityInIndicatorInstance(ctx, instance, timeGranularity);

        List<String> codes = new ArrayList<String>();

        for (TimeValue timeValue : timeValues) {
            codes.add(timeValue.getTimeValue());
        }
        return codes;
    }

    private List<TimeValue> calculateTimeValuesByGranularityInIndicatorInstance(ServiceContext ctx, IndicatorInstance instance, TimeGranularityEnum granularity) throws MetamacException {
        return indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorInstance(ctx, instance.getUuid(), granularity);
    }

    private DateColumn getColumnForTime(TimeGranularityEnum timeGranularity) {
        switch (timeGranularity) {
            case YEARLY:
                return new DateColumn("year", "yyyy");
            case BIYEARLY:
            case QUARTERLY:
            case MONTHLY:
                return new DateColumn("month", "yyyy'M'MM");
            case WEEKLY:
            case DAILY:
                return new DateColumn("day", "yyyyMMdd");
        }
        return null;
    }

    private FloatColumn getColumnForInstanceMetric(IndicatorInstance instance) {
        String name = getIdForQuantityIndicatorConcept(instance.getIndicator());
        return new FloatColumn(name);
    }

    private TextColumn getColumnForGeo(GeographicalGranularity geoGranularity) {
        return new TextColumn(getIdForGeoConcept(geoGranularity));
    }

    // Id builders
    private String getIdForTimeConcept(TimeGranularityEnum timeGranularity) {
        switch (timeGranularity) {
            case YEARLY:
                return "time:year";
            case BIYEARLY:
            case QUARTERLY:
            case MONTHLY:
                return "time:month";
            case WEEKLY:
            case DAILY:
                return "time:day";
        }
        return null;
    }
    
    private String getIdForTopic(Dimension dim) {
        return "topic_"+dim.getUuid();
    }
    

    private String getIdForSlice(GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity) {
        return "slice_" + geoGranularity.getCode().toLowerCase() + "_" + timeGranularity.name().toLowerCase();
    }

    private String getIdForGeoConcept(GeographicalGranularity geoGranularity) {
        return GEO_CONCEPT_PREFIX + geoGranularity.getCode().toLowerCase();
    }

    private String getTableIdForSlice(String idSlice) {
        return idSlice + "_table";
    }

    private String getIdForUnitConcept(QuantityUnit unit) {
        return "unit_" + unit.getUuid();
    }

    private String getIdForQuantityIndicatorConcept(Indicator indicatorQuantity) {
        return "quantity_"+indicatorQuantity.getUuid();
    }

    private String getTableIdForUnitConcept(QuantityUnit unit) {
        return getIdForUnitConcept(unit) + "_table";
    }

    private String getTableIdForGeoConcept(GeographicalGranularity granularity) {
        return getIdForGeoConcept(granularity) + "_table";
    }

    private String getProviderName() {
        return configurationService.getProperty(PROP_PROVIDER_NAME);
    }

    private String getProviderDescription() {
        return configurationService.getProperty(PROP_PROVIDER_DESCRIPTION);
    }

    private String getProviderUrl() {
        return configurationService.getProperty(PROP_PROVIDER_URL);
    }
}
