package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;
import es.gobcan.istac.edatos.dataset.repository.util.DtoUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.dspl.DsplConcept;
import es.gobcan.istac.indicators.core.dspl.DsplDataset;
import es.gobcan.istac.indicators.core.dspl.DsplInfo;
import es.gobcan.istac.indicators.core.dspl.DsplInstanceData;
import es.gobcan.istac.indicators.core.dspl.DsplSlice;
import es.gobcan.istac.indicators.core.dspl.DsplTopic;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsCoverageService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;

public class DsplTransformerTimeTranslator extends DsplTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(DsplTransformerTimeTranslator.class);

    public DsplTransformerTimeTranslator(IndicatorsSystemsService indicatorsSystemsService, IndicatorsDataService indicatorsDataService, IndicatorsCoverageService indicatorsCoverageService,
            IndicatorsService indicatorsService, IndicatorsConfigurationService configurationService) {
        super(indicatorsSystemsService, indicatorsDataService, indicatorsCoverageService, indicatorsService, configurationService);
    }
    @Override
    public List<DsplDataset> transformIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, InternationalString title, InternationalString description) throws MetamacException {
        try {
            LOG.info("Building dspl for indicators System " + indicatorsSystemUuid);

            IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublished(ctx, indicatorsSystemUuid);
            List<ElementLevel> structure = indicatorsSystemsService.retrieveIndicatorsSystemStructure(ctx, indicatorsSystemVersion.getIndicatorsSystem().getUuid(),
                    indicatorsSystemVersion.getVersionNumber());

            LOG.info("Retrieving indicators instances...");
            List<IndicatorInstance> instances = filterIndicatorsInstances(structure);

            // organize by time granularity

            List<DsplDataset> datasets = new ArrayList<DsplDataset>();

            LOG.info("Processing indicators instances ");

            // topics
            LOG.info("Building topics ...");
            Set<DsplTopic> topics = buildTopicsForInstances(instances);

            // concepts
            LOG.info("Building concepts ...");
            List<DsplConcept> concepts = new ArrayList<DsplConcept>();
            concepts.addAll(buildStandardConceptsForGeoDimensions(ctx, instances));
            List<DsplConcept> metrics = buildMetricsForInstances(ctx, instances);
            concepts.addAll(metrics);

            TimeGranularityEnum minTimeGranularity = computeMinTimeGranularity(getIndicatorsInstancesTimeGranularities(ctx, instances));

            // Min granularity weekly can cause problems,
            // 2009 will be translated to 2009W53 and then translated to 3/01/2010
            if (TimeGranularityEnum.WEEKLY.equals(minTimeGranularity)) {
                minTimeGranularity = TimeGranularityEnum.DAILY;
            }

            // slides
            LOG.info("Computing slices ...");
            Set<DsplSlice> slices = createSlicesForInstancesWithTimeGranularity(ctx, instances, minTimeGranularity);

            if (slices.size() > 0) {
                LOG.info("Building slices ...");
                DsplInfo datasetInfo = buildDatasetInfo(ctx, indicatorsSystemVersion, title, description, minTimeGranularity);
                DsplInfo providerInfo = buildProviderInfo();
                String datasetId = buildDatasetId(indicatorsSystemVersion, minTimeGranularity);
                DsplDataset dataset = new DsplDataset(datasetId, datasetInfo, providerInfo);

                dataset.addConcepts(concepts);
                dataset.addTopics(topics);
                dataset.addSlices(slices);

                datasets.add(dataset);
                LOG.info("Dataset has been built");
            }

            LOG.info("Dspl succesfully built for Indicators System: " + indicatorsSystemUuid);
            return datasets;
        } catch (MetamacException e) {
            throw new MetamacException(e, ServiceExceptionType.DSPL_STRUCTURE_CREATE_ERROR, indicatorsSystemUuid);
        }
    }

    private Set<TimeGranularityEnum> getIndicatorsInstancesTimeGranularities(ServiceContext ctx, List<IndicatorInstance> instances) throws MetamacException {
        Set<TimeGranularityEnum> granularities = new HashSet<TimeGranularityEnum>();
        for (IndicatorInstance instance : instances) {
            List<TimeGranularity> instanceGranularities = indicatorsCoverageService.retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid());

            for (TimeGranularity granularity : instanceGranularities) {
                granularities.add(granularity.getGranularity());
            }
            if (granularities.size() == TimeGranularityEnum.values().length) {
                // All granularities found, no more searching
                return granularities;
            }
        }
        return granularities;
    }

    private TimeGranularityEnum computeMinTimeGranularity(Set<TimeGranularityEnum> timeGranularities) {
        TimeGranularityEnum min = null;
        for (TimeGranularityEnum candidate : timeGranularities) {
            if (min != null) {
                if (TimeVariableUtils.getTimeGranularityOrder(candidate) < TimeVariableUtils.getTimeGranularityOrder(min)) {
                    min = candidate;
                }
            } else {
                min = candidate;
            }
        }
        return min;
    }

    @Override
    protected DsplInstanceData buildInstanceData(ServiceContext ctx, IndicatorInstance instance, GeographicalGranularity geoGranularity, TimeGranularityEnum timeGranularity) throws MetamacException {

        IndicatorsDataFilterVO dataFilter = new IndicatorsDataFilterVO();
        dataFilter.setGeoFilter(IndicatorsDataGeoDimensionFilterVO.buildGeoGranularityFilter(geoGranularity.getCode()));
        String measureCode = MeasureDimensionTypeEnum.ABSOLUTE.name();
        dataFilter.setMeasureFilter(IndicatorsDataMeasureDimensionFilterVO.buildCodesFilter());

        IndicatorObservationsVO indicatorsObservations = findObservationsForIndicatorInstance(ctx, instance, dataFilter);

        Map<String, String> validTimeValuesTransformed = transformTimeValuesOnlyIfValid(indicatorsObservations.getTimeCodes(), timeGranularity);

        DsplInstanceData data = new DsplInstanceData();
        for (String geoCode : indicatorsObservations.getGeographicalCodes()) {
            for (String timeCode : indicatorsObservations.getTimeCodes()) {
                String transformedTimeCode = validTimeValuesTransformed.get(timeCode);
                if (transformedTimeCode != null) {
                    String observationKey = DtoUtils.generateUniqueKeyWithCodes(Arrays.asList(geoCode, timeCode, measureCode));
                    ObservationDto obs = indicatorsObservations.getObservations().get(observationKey);

                    if (obs != null) {
                        data.put(geoCode, transformedTimeCode, obs.getPrimaryMeasure());
                    } else {
                        data.put(geoCode, transformedTimeCode, null);
                    }
                }
            }
        }
        return data;
    }

    /**
     * This piece of code transform all time values to the same granularity, in case of two different values are translated to the same
     * value we'll keep the value with lower original granularity.
     * 
     * @param timeCodes
     * @param timeGranularity
     * @return
     * @throws MetamacException
     */
    private Map<String, String> transformTimeValuesOnlyIfValid(List<String> timeCodes, TimeGranularityEnum timeGranularity) throws MetamacException {
        BiMap<String, String> mapping = new HashBiMap<String, String>();
        for (String timeCode : timeCodes) {
            String transformed = transformTimeValueToGranularity(timeCode, timeGranularity);
            String previousTransformed = mapping.inverse().get(transformed);
            if (previousTransformed != null) {
                TimeValue currentValue = TimeVariableUtils.parseTimeValue(timeCode);
                TimeValue previousValue = TimeVariableUtils.parseTimeValue(previousTransformed);
                if (TimeVariableUtils.getTimeValueOrderByGranularity(currentValue) < TimeVariableUtils.getTimeValueOrderByGranularity(previousValue)) {
                    mapping.forcePut(timeCode, transformed);
                }
            } else {
                mapping.put(timeCode, transformed);
            }
        }
        return mapping;
    }
    private String transformTimeValueToGranularity(String timeCode, TimeGranularityEnum timeGranularity) throws MetamacException {
        TimeValue timeValue = TimeVariableUtils.parseTimeValue(timeCode);
        DateTime date = new DateTime(TimeVariableUtils.timeValueToLastPossibleDate(timeValue));

        switch (timeGranularity) {
            case YEARLY:
                return buildYearlyTimeValue(date.getYear());
            case BIYEARLY:
                if (date.getMonthOfYear() <= 6) {
                    return buildBiyearlyTimeValue(date.getYear(), 1);
                } else {
                    return buildBiyearlyTimeValue(date.getYear(), 2);
                }
            case QUARTERLY:
                if (date.getMonthOfYear() <= 3) {
                    return buildQuarterlyTimeValue(date.getYear(), 1);
                } else if (date.getMonthOfYear() <= 6) {
                    return buildQuarterlyTimeValue(date.getYear(), 2);
                } else if (date.getMonthOfYear() <= 9) {
                    return buildQuarterlyTimeValue(date.getYear(), 3);
                } else {
                    return buildQuarterlyTimeValue(date.getYear(), 4);
                }
            case MONTHLY:
                return buildMonthlyTimeValue(date.getYear(), date.getMonthOfYear());
            case WEEKLY:
                return buildWeeklyTimeValue(date.getYear(), date.getWeekOfWeekyear());
            case DAILY:
                return buildDailyTimeValue(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
            default:
                break;
        }

        return null;
    }

    private String buildYearlyTimeValue(int year) {
        return String.valueOf(year);
    }

    private String buildBiyearlyTimeValue(int year, int sem) {
        return year + "H" + sem;
    }

    private String buildQuarterlyTimeValue(int year, int quarter) {
        return year + "Q" + quarter;
    }

    private String buildMonthlyTimeValue(int year, int month) {
        if (month < 10) {
            return year + "M0" + month;
        } else {
            return year + "M" + month;
        }
    }

    private String buildWeeklyTimeValue(int year, int week) {
        if (week < 10) {
            return year + "W0" + week;
        } else {
            return year + "W" + week;
        }
    }

    private String buildDailyTimeValue(int year, int month, int day) {
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);
        String dayStr = day < 10 ? "0" + day : String.valueOf(day);
        return year + monthStr + dayStr;
    }

}
