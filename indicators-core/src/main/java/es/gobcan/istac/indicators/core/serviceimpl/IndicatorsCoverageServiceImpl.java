package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;

/**
 * Implementation of IndicatorsCoverageService.
 */
@Service("indicatorsCoverageService")
public class IndicatorsCoverageServiceImpl extends IndicatorsCoverageServiceImplBase {

    public IndicatorsCoverageServiceImpl() {
    }

    /* GEOGRAPHICAL GRANULARITIES */

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicator(indicatorVersion, null);

        return retrieveGeoGranularityCoverageFromCache(indicatorVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return calculateGeoGranularityCoverageInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return calculateGeoGranularityCoverageInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(ServiceContext ctx, String systemCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(systemCode, null);

        List<IndicatorInstance> instances = getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystem(systemCode);

        Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
        for (IndicatorInstance instance : instances) {
            granularities.addAll(retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid()));
        }
        return new ArrayList<GeographicalGranularity>(granularities);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(ServiceContext ctx, String subjectCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(subjectCode, null);

        // IDEA: Could be one single query

        List<IndicatorVersion> indicatorVersions = getIndicatorVersionRepository().findPublishedIndicatorVersionWithSubjectCode(subjectCode);

        Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
        for (IndicatorVersion indicatorVersion : indicatorVersions) {
            granularities.addAll(retrieveGeoGranularityCoverageFromCache(indicatorVersion));
        }

        return new ArrayList<GeographicalGranularity>(granularities);
    }

    private List<GeographicalGranularity> calculateGeoGranularityCoverageInIndicatorInstance(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indInstance.isFilteredByGeographicalValues()) {
            Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
            for (GeographicalValue geoValue : indInstance.getGeographicalValues()) {
                granularities.add(geoValue.getGranularity());
            }
            return new ArrayList<GeographicalGranularity>(granularities);
        } else if (indInstance.isFilteredByGeographicalGranularity()) {
            return Arrays.asList(indInstance.getGeographicalGranularity());
        } else {
            return retrieveGeoGranularityCoverageFromCache(indicatorVersion);
        }
    }

    private List<GeographicalGranularity> retrieveGeoGranularityCoverageFromCache(IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        return getIndicatorVersionGeoCoverageRepository().retrieveGranularityCoverage(indicatorVersion);
    }

    /* GEOGRAPHICAL VALUES BY GRANULARITY */

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, String granularityUuid)
            throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicator(indicatorUuid, indicatorVersionNumber, granularityUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return retrieveGeoValueCoverageByGranularityInIndicatorVersionFromCache(granularityUuid, indicatorVersion);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(ServiceContext ctx, String systemCode, String granularityUuid)
            throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(systemCode, granularityUuid, null);

        // IDEA: cOULD BE ONE SINGLE QUERY

        List<IndicatorInstance> instances = getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystem(systemCode);

        Set<GeographicalValueVO> geoValues = new HashSet<GeographicalValueVO>();
        for (IndicatorInstance instance : instances) {
            IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(instance.getIndicator().getUuid());
            geoValues.addAll(calculateGeoCoverageByGranularityInIndicatorInstance(granularityUuid, instance, indicatorVersion));
        }
        List<GeographicalValueVO> geoValuesList = new ArrayList<GeographicalValueVO>(geoValues);
        ServiceUtils.sortGeographicalValuesVOList(geoValuesList);
        return geoValuesList;
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(ServiceContext ctx, String subjectCode, String granularityUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(subjectCode, granularityUuid, null);

        // IDEA: could be one single query

        List<IndicatorVersion> indicatorVersions = getIndicatorVersionRepository().findPublishedIndicatorVersionWithSubjectCode(subjectCode);

        Set<GeographicalValueVO> geoValues = new HashSet<GeographicalValueVO>();
        for (IndicatorVersion indicatorVersion : indicatorVersions) {
            geoValues.addAll(retrieveGeoVOCoverageByGranularityInIndicatorVersionFromCache(granularityUuid, indicatorVersion));
        }
        List<GeographicalValueVO> geoValuesList = new ArrayList<GeographicalValueVO>(geoValues);
        ServiceUtils.sortGeographicalValuesVOList(geoValuesList);
        return geoValuesList;
    }

    private List<GeographicalValueVO> calculateGeoCoverageByGranularityInIndicatorInstance(String granularityUuid, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indicatorInstance.isFilteredByGeographicalValues()) { // Fixed values
            return getIndicatorVersionGeoCoverageRepository().retrieveCoverageFilteredByInstanceGeoValuesByGranularity(indicatorVersion, indicatorInstance.getUuid(), granularityUuid);
        }

        if (indicatorInstance.isFilteredByGeographicalGranularity() && !granularityUuid.equals(indicatorInstance.getGeographicalGranularity().getUuid())) {
            return new ArrayList<GeographicalValueVO>();
        } else {
            return retrieveGeoVOCoverageByGranularityInIndicatorVersionFromCache(granularityUuid, indicatorVersion);
        }
    }

    private List<GeographicalCodeVO> calculateGeoCodeCoverageByGranularityInIndicatorInstance(String granularityUuid, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indicatorInstance.isFilteredByGeographicalValues()) { // Fixed values
            return getIndicatorVersionGeoCoverageRepository().retrieveCodeCoverageFilteredByInstanceGeoValuesByGranularity(indicatorVersion, indicatorInstance.getUuid(), granularityUuid);
        }

        if (indicatorInstance.isFilteredByGeographicalGranularity() && !granularityUuid.equals(indicatorInstance.getGeographicalGranularity().getUuid())) {
            return new ArrayList<GeographicalCodeVO>();
        } else {
            return retrieveGeoCodeCoverageByGranularityInIndicatorVersionFromCache(granularityUuid, indicatorVersion);
        }
    }

    private List<GeographicalValueVO> retrieveGeoVOCoverageByGranularityInIndicatorVersionFromCache(String granularityUuid, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        List<GeographicalValueVO> geographicalValuesInIndicator = getIndicatorVersionGeoCoverageRepository().retrieveCoverageFilteredByGranularity(indicatorVersion, granularityUuid);
        return geographicalValuesInIndicator;
    }

    private List<GeographicalValue> retrieveGeoValueCoverageByGranularityInIndicatorVersionFromCache(String granularityUuid, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        List<GeographicalValue> geographicalValuesInIndicator = getIndicatorVersionGeoCoverageRepository().retrieveValueCoverageFilteredByGranularity(indicatorVersion, granularityUuid);
        return geographicalValuesInIndicator;
    }

    private List<GeographicalCodeVO> retrieveGeoCodeCoverageByGranularityInIndicatorVersionFromCache(String granularityUuid, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        List<GeographicalCodeVO> geographicalValuesInIndicator = getIndicatorVersionGeoCoverageRepository().retrieveCodeCoverageFilteredByGranularity(indicatorVersion, granularityUuid);
        return geographicalValuesInIndicator;
    }

    /* GEOGRAPHICAL VALUES */

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorVersion(indicatorVersion, null);

        return retrieveGeoCoverageFromCache(indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalCodesInIndicatorVersion(indicatorVersion, null);

        return retrieveGeoCodeCoverageFromCache(indicatorVersion);
    }

    private List<GeographicalCodeVO> retrieveGeoCodeCoverageFromCache(IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        List<GeographicalCodeVO> geoCodes = getIndicatorVersionGeoCoverageRepository().retrieveCodeCoverage(indicatorVersion);
        return geoCodes;
    }

    private List<GeographicalValueVO> retrieveGeoCoverageFromCache(IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        List<GeographicalValueVO> geoValues = getIndicatorVersionGeoCoverageRepository().retrieveCoverage(indicatorVersion);
        return geoValues;
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());
        return calculateGeoCoverageInIndicatorInstance(indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return calculateGeoCoverageInIndicatorInstance(indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalCodesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());
        return calculateGeoCodeCoverageInIndicatorInstance(indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalCodesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return calculateGeoCodeCoverageInIndicatorInstance(indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstanceWithIndicatorVersion(ServiceContext ctx, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        return calculateGeoCodeCoverageInIndicatorInstance(indicatorInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesByGranularityInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid, String granularityUuid)
            throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalCodesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return calculateGeoCodeCoverageByGranularityInIndicatorInstance(granularityUuid, indInstance, indicatorVersion);
    }

    private List<GeographicalCodeVO> calculateGeoCodeCoverageInIndicatorInstance(IndicatorInstance indInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indInstance.isFilteredByGeographicalValues()) { // Fixed values
            return getIndicatorVersionGeoCoverageRepository().retrieveCodeCoverageFilteredByInstanceGeoValues(indicatorVersion, indInstance.getUuid());
        } else if (indInstance.isFilteredByGeographicalGranularity()) { // fixed granularity
            return retrieveGeoCodeCoverageByGranularityInIndicatorVersionFromCache(indInstance.getGeographicalGranularity().getUuid(), indicatorVersion);
        } else { // nothing is fixed
            return retrieveGeoCodeCoverageFromCache(indicatorVersion);
        }
    }

    private List<GeographicalValueVO> calculateGeoCoverageInIndicatorInstance(IndicatorInstance indInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indInstance.isFilteredByGeographicalValues()) { // Fixed values
            return getIndicatorVersionGeoCoverageRepository().retrieveCoverageFilteredByInstanceGeoValues(indicatorVersion, indInstance.getUuid());
        } else if (indInstance.isFilteredByGeographicalGranularity()) { // fixed granularity
            return retrieveGeoVOCoverageByGranularityInIndicatorVersionFromCache(indInstance.getGeographicalGranularity().getUuid(), indicatorVersion);
        } else { // nothing is fixed
            return retrieveGeoCoverageFromCache(indicatorVersion);
        }
    }

    /* TIME GRANULARITIES */

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);

        return retrieveTimeGranularityCoverageFromCache(ctx, indicatorVersion);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);

        return retrieveTimeGranularityCoverageFromCache(ctx, indicatorVersion);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        List<TimeGranularity> timeGranularitiesFixedInIndicatorInstance = calculateTimeGranularityCoverageInIndicatorInstance(ctx, indicatorVersion, indInstance);
        return timeGranularitiesFixedInIndicatorInstance;
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);

        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        List<TimeGranularity> timeGranularitiesFixedInIndicatorInstance = calculateTimeGranularityCoverageInIndicatorInstance(ctx, indicatorVersion, indInstance);

        return timeGranularitiesFixedInIndicatorInstance;
    }

    private List<TimeGranularity> calculateTimeGranularityCoverageInIndicatorInstance(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorInstance indInstance) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indInstance.isFilteredByTimeValues()) {
            return getIndicatorVersionTimeCoverageRepository().retrieveGranularityCoverageFilteredByInstanceTimeValues(indicatorVersion, indInstance.getTimeValuesAsList());
        } else if (indInstance.isFilteredByTimeGranularity()) {
            TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, indInstance.getTimeGranularity());
            return Arrays.asList(timeGranularity);
        } else {
            return retrieveTimeGranularityCoverageFromCache(ctx, indicatorVersion);
        }
    }

    private List<TimeGranularity> retrieveTimeGranularityCoverageFromCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        return getIndicatorVersionTimeCoverageRepository().retrieveGranularityCoverage(indicatorVersion);
    }

    /* TIME VALUES BY GRANULARITY */

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicator(indicatorUuid, indicatorVersionNumber, granularity, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return retrieveTimeCoverageByGranularityFromCache(granularity, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicatorPublished(ServiceContext ctx, String indicatorUuid, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicatorPublished(indicatorUuid, granularity, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return retrieveTimeCoverageByGranularityFromCache(granularity, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicatorInstance(indicatorInstanceUuid, granularity, null);

        IndicatorInstance indicatorInstance = getIndicatorInstance(indicatorInstanceUuid);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorInstance.getIndicator().getUuid());

        return calculateTimeCoverageByGranularityInIndicatorInstance(ctx, granularity, indicatorInstance, indicatorVersion);
    }

    private List<TimeValue> calculateTimeCoverageByGranularityInIndicatorInstance(ServiceContext ctx, TimeGranularityEnum granularity, IndicatorInstance indicatorInstance,
            IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        if (indicatorInstance.isFilteredByTimeValues()) {
            List<TimeValue> timeValuesInIndicatorInstance = new ArrayList<TimeValue>();
            for (String timeStr : indicatorInstance.getTimeValuesAsList()) {
                TimeValue value = TimeVariableUtils.parseTimeValue(timeStr);
                if (granularity.equals(value.getGranularity())) {
                    timeValuesInIndicatorInstance.add(value);
                }
            }

            TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValuesInIndicatorInstance);

            return timeValuesInIndicatorInstance;
        }

        if (indicatorInstance.isFilteredByTimeGranularity() && !granularity.equals(indicatorInstance.getTimeGranularity())) {
            return new ArrayList<TimeValue>();
        } else {
            return retrieveTimeCoverageByGranularityFromCache(granularity, indicatorVersion);
        }
    }

    private List<TimeValue> retrieveTimeCoverageByGranularityFromCache(TimeGranularityEnum granularity, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        List<TimeValue> timeValuesInIndicator = getIndicatorVersionTimeCoverageRepository().retrieveCoverageByGranularity(indicatorVersion, granularity.getName());
        TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValuesInIndicator);
        return timeValuesInIndicator;
    }

    /* TIME VALUES */

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicator(indicatorVersion, null);

        return retrieveTimeCoverageFromCache(ctx, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return calculateTimeCoverageInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return calculateTimeCoverageInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstanceWithIndicatorVersion(ServiceContext ctx, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        return calculateTimeCoverageInIndicatorInstance(ctx, indicatorInstance, indicatorVersion);
    }

    private List<TimeValue> calculateTimeCoverageInIndicatorInstance(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        if (indInstance.isFilteredByTimeValues()) {
            List<TimeValue> timeValues = getIndicatorVersionTimeCoverageRepository().retrieveCoverageFilteredByInstanceTimeValues(indicatorVersion, indInstance.getTimeValuesAsList());
            TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValues);
            return timeValues;
        } else if (indInstance.isFilteredByTimeGranularity()) {
            List<TimeValue> timeValues = getIndicatorVersionTimeCoverageRepository().retrieveCoverageByGranularity(indicatorVersion, indInstance.getTimeGranularity().getName());
            TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValues);
            return timeValues;
        } else {
            return retrieveTimeCoverageFromCache(ctx, indicatorVersion);
        }
    }

    private List<TimeValue> retrieveTimeCoverageFromCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        long time = System.currentTimeMillis();
        List<TimeValue> timeValues = getIndicatorVersionTimeCoverageRepository().retrieveCoverage(indicatorVersion);
        long timeRetrieve = System.currentTimeMillis();
        TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValues);
        long timeSort = System.currentTimeMillis();
        System.out.println("Rtrieve: " + (timeRetrieve - time) + " sort: " + (timeSort - timeRetrieve));
        return timeValues;
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicator(indicatorVersion, null);

        return retrieveMeasureCoverageFromCache(ctx, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorInstance(indicatorInstanceUuid, null);
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return retrieveMeasureCoverageFromCache(ctx, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorInstance(indicatorInstanceUuid, null);
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return retrieveMeasureCoverageFromCache(ctx, indicatorVersion);
    }

    private List<MeasureValue> retrieveMeasureCoverageFromCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        return getIndicatorVersionMeasureCoverageRepository().retrieveCoverage(indicatorVersion);
    }

    private void checkIndicatorVersionHasDataPopulated(IndicatorVersion indicatorVersion) throws MetamacException {
        if (indicatorVersion.getDataRepositoryId() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    private IndicatorVersion getIndicatorVersion(String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        if (indicatorVersion != null) {
            return indicatorVersion;
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND, indicatorUuid, indicatorVersionNumber);
        }
    }

    private IndicatorVersion getIndicatorPublishedVersion(String indicatorUuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);
        if (indicator.getIsPublished()) {
            return getIndicatorVersion(indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, indicatorUuid);
        }
    }

    private IndicatorInstance getIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstanceRepository().findIndicatorInstance(indicatorInstanceUuid);
        if (indInstance != null) {
            return indInstance;
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND, indicatorInstanceUuid);
        }
    }

    private IndicatorVersion getIndicatorLastVersion(String indicatorUuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);

        if (indicator.getProductionVersion() != null) {
            return getIndicatorVersion(indicatorUuid, indicator.getProductionVersion().getVersionNumber());
        } else {
            if (indicator.getIsPublished()) {
                return getIndicatorVersion(indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_LAST_ARCHIVED, indicatorUuid);
            }
        }
    }
}