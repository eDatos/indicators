package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionMeasureCoverage;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Repository implementation for IndicatorVersionMeasureCoverage
 */
@Repository("indicatorVersionMeasureCoverageRepository")
public class IndicatorVersionMeasureCoverageRepositoryImpl extends IndicatorVersionMeasureCoverageRepositoryBase {

    public IndicatorVersionMeasureCoverageRepositoryImpl() {
    }

    @Override
    public List<MeasureValue> retrieveCoverage(IndicatorVersion indicatorVersion) {
        String queryHql = "from IndicatorVersionMeasureCoverage measureCoverage ";
        queryHql += "where measureCoverage.indicatorVersion = :indicatorVersion ";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indicatorVersion", indicatorVersion);
        List<IndicatorVersionMeasureCoverage> results = findByQuery(queryHql, parameters);

        List<MeasureValue> measureValues = new ArrayList<MeasureValue>();
        for (IndicatorVersionMeasureCoverage coverage : results) {
            MeasureValue measure = new MeasureValue();
            measure.setMeasureValue(MeasureDimensionTypeEnum.valueOf(coverage.getMeasureCode()));
            Translation translation = coverage.getTranslation();
            if (translation != null) {
                measure.setTitle(translation.getTitle());
                measure.setTitleSummary(translation.getTitleSummary());
            } else {
                measure.setTitle(ServiceUtils.generateInternationalStringInDefaultLocales(coverage.getMeasureCode()));
                measure.setTitleSummary(ServiceUtils.generateInternationalStringInDefaultLocales(coverage.getMeasureCode()));
            }
            measureValues.add(measure);
        }
        return measureValues;
    }

    @Override
    public void deleteCoverageForIndicatorVersion(IndicatorVersion indicatorVersion) {
        Query query = getEntityManager().createQuery("delete IndicatorVersionMeasureCoverage where indicatorVersion = :indicatorVersion");
        query.setParameter("indicatorVersion", indicatorVersion);
        query.executeUpdate();
    }

}
