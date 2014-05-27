package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionTimeCoverage;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Repository implementation for IndicatorVersionTimeCoverage
 */
@Repository("indicatorVersionTimeCoverageRepository")
public class IndicatorVersionTimeCoverageRepositoryImpl extends IndicatorVersionTimeCoverageRepositoryBase {

    public IndicatorVersionTimeCoverageRepositoryImpl() {
    }

    @Override
    public List<TimeGranularity> retrieveGranularityCoverage(IndicatorVersion indicatorVersion) {
        String queryHql = "select distinct coverage.timeGranularity, coverage.granularityTranslation ";
        queryHql += "from IndicatorVersionTimeCoverage coverage left outer join coverage.granularityTranslation ";
        queryHql += "where coverage.indicatorVersion = :indicatorVersion ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorVersion", indicatorVersion);

        List<TimeGranularity> timeGranularities = new ArrayList<TimeGranularity>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            String timeGranularityCode = (String) fields[0];
            Translation translation = (Translation) fields[1];

            TimeGranularity granularity = new TimeGranularity();
            granularity.setGranularity(TimeGranularityEnum.valueOf(timeGranularityCode));
            if (translation != null) {
                granularity.setTitle(translation.getTitle());
                granularity.setTitleSummary(translation.getTitleSummary());
            } else {
                granularity.setTitle(ServiceUtils.generateInternationalStringInDefaultLocales(timeGranularityCode));
                granularity.setTitleSummary(ServiceUtils.generateInternationalStringInDefaultLocales(timeGranularityCode));
            }
            timeGranularities.add(granularity);
        }
        return timeGranularities;
    }

    @Override
    public List<TimeGranularity> retrieveGranularityCoverageFilteredByInstanceTimeValues(IndicatorVersion indicatorVersion, List<String> instanceTimeValues) throws MetamacException {
        String queryHql = "select distinct coverage.timeGranularity, coverage.granularityTranslation ";
        queryHql += "from IndicatorVersionTimeCoverage coverage left outer join coverage.granularityTranslation ";
        queryHql += "where coverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and   ( ";

        List<List<String>> batchedList = splitList(instanceTimeValues, 1000);

        for (int i = 0; i < batchedList.size(); i++) {
            if (i > 0) {
                queryHql += "or ";
            }
            queryHql += " coverage.timeValue in (:timeCodes_" + i + ") ";
        }
        queryHql += ") ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorVersion", indicatorVersion);
        for (int i = 0; i < batchedList.size(); i++) {
            query.setParameter("timeCodes_" + i, batchedList.get(i));
        }

        List<TimeGranularity> timeGranularities = new ArrayList<TimeGranularity>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            String timeGranularityCode = (String) fields[0];
            Translation translation = (Translation) fields[1];

            TimeGranularity timeGranularity = TimeVariableUtils.buildTimeGranularity(TimeGranularityEnum.valueOf(timeGranularityCode), translation);
            timeGranularities.add(timeGranularity);
        }

        return timeGranularities;
    }
    @Override
    public List<TimeValue> retrieveCoverage(IndicatorVersion indicatorVersion) throws MetamacException {
        String queryHql = "from IndicatorVersionTimeCoverage where indicatorVersion = :indicatorVersion";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indicatorVersion", indicatorVersion);
        List<IndicatorVersionTimeCoverage> results = findByQuery(queryHql, parameters);
        List<TimeValue> timeValues = new ArrayList<TimeValue>();
        for (IndicatorVersionTimeCoverage coverage : results) {
            String timeCode = coverage.getTimeValue();
            Translation translation = coverage.getTranslation();

            TimeValue timeValue = TimeVariableUtils.buildTimeValue(timeCode, translation);
            timeValues.add(timeValue);
        }
        return timeValues;
    }

    @Override
    public List<TimeValue> retrieveCoverageByGranularity(IndicatorVersion indicatorVersion, String timeGranularityCode) throws MetamacException {
        String queryHql = "select distinct coverage.timeValue, coverage.translation ";
        queryHql += "from IndicatorVersionTimeCoverage coverage left outer join coverage.translation ";
        queryHql += "where coverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and   coverage.timeGranularity = :timeGranularity ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorVersion", indicatorVersion);
        query.setParameter("timeGranularity", timeGranularityCode);

        List<TimeValue> timeValues = new ArrayList<TimeValue>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            String timeCode = (String) fields[0];
            Translation translation = (Translation) fields[1];

            TimeValue timeValue = TimeVariableUtils.buildTimeValue(timeCode, translation);
            timeValues.add(timeValue);
        }
        return timeValues;
    }

    @Override
    public List<TimeValue> retrieveCoverageFilteredByInstanceTimeValues(IndicatorVersion indicatorVersion, List<String> instanceTimeValues) throws MetamacException {
        String queryHql = "select distinct coverage.timeValue, coverage.translation ";
        queryHql += "from IndicatorVersionTimeCoverage coverage left outer join coverage.translation ";
        queryHql += "where coverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and   coverage.timeValue in (:timeCodes) ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorVersion", indicatorVersion);
        query.setParameter("timeCodes", instanceTimeValues);

        List<TimeValue> timeValues = new ArrayList<TimeValue>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            String timeCode = (String) fields[0];
            Translation translation = (Translation) fields[1];

            TimeValue timeValue = TimeVariableUtils.buildTimeValue(timeCode, translation);
            timeValues.add(timeValue);
        }

        return timeValues;
    }
    @Override
    public void deleteCoverageForIndicatorVersion(IndicatorVersion indicatorVersion) {
        Query query = getEntityManager().createQuery("delete IndicatorVersionTimeCoverage where indicatorVersion = :indicatorVersion");
        query.setParameter("indicatorVersion", indicatorVersion);
        query.executeUpdate();
    }

    private List<List<String>> splitList(List<String> values, int size) {
        List<List<String>> multiList = new LinkedList<List<String>>();
        for (int i = 0; i < values.size(); i += size) {
            int endIndex = i + size;
            if (endIndex > values.size()) {
                endIndex = values.size();
            }
            multiList.add(values.subList(i, endIndex));
        }
        return multiList;
    }
}