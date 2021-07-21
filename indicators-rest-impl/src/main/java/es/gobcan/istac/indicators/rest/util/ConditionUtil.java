package es.gobcan.istac.indicators.rest.util;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.MetamacTimeUtils;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public final class ConditionUtil {

    private ConditionUtil() {
    }

    public static IndicatorsDataTimeDimensionFilterVO normalizeAndFilterTimeDimension(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        List<String> selectedValues = MetamacTimeUtils.normalizeToMetamacTimeValues(selectedRepresentations.get(IndicatorDataDimensionTypeEnum.TIME.name()));
        List<String> selectedGranularityCodes = selectedGranularities.get(IndicatorDataDimensionTypeEnum.TIME.name());

        IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
        filter.setCodes(selectedValues);
        filter.setGranularityCodes(selectedGranularityCodes);

        return filter;
    }

    public static IndicatorsDataGeoDimensionFilterVO filterGeographicalDimension(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) {
        // GEOGRAPHICAL
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        List<String> geographicalSelectedGranularities = selectedGranularities.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());

        IndicatorsDataGeoDimensionFilterVO filter = new IndicatorsDataGeoDimensionFilterVO();
        filter.setCodes(geographicalSelectedValues);
        filter.setGranularityCodes(geographicalSelectedGranularities);

        return filter;
    }

    public static IndicatorsDataMeasureDimensionFilterVO filterMeasureDimension(Map<String, List<String>> selectedRepresentations) {
        List<String> selectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.MEASURE.name());

        IndicatorsDataMeasureDimensionFilterVO filter = new IndicatorsDataMeasureDimensionFilterVO();
        filter.setCodes(selectedValues);

        return filter;
    }

}
