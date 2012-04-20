package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataType;

public class DataTypeUtil {

    public static DataType createDataType(List<GeographicalValue> geographicalValues, List<String> timeValues, List<MeasureDimensionTypeEnum> measureValues, Map<String, ObservationDto> observationMap) {
        List<String> observations = new ArrayList<String>();
        List<String> formatIds = new ArrayList<String>();
        List<Integer> formatSizes = new ArrayList<Integer>();
        Map<String, DataDimensionType> dimension = new LinkedHashMap<String, DataDimensionType>();

        formatIds.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        formatIds.add(IndicatorDataDimensionTypeEnum.TIME.name());
        formatIds.add(IndicatorDataDimensionTypeEnum.MEASURE.name());
        formatSizes.add(geographicalValues.size());
        formatSizes.add(timeValues.size());
        formatSizes.add(measureValues.size());

        DataDimensionType dataDimensionTypeGeographical = new DataDimensionType();
        dataDimensionTypeGeographical.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dataDimensionTypeGeographical);

        DataDimensionType dataDimensionTypeTime = new DataDimensionType();
        dataDimensionTypeTime.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.TIME.name(), dataDimensionTypeTime);

        DataDimensionType dataDimensionTypeMeasure = new DataDimensionType();
        dataDimensionTypeMeasure.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), dataDimensionTypeMeasure);

        for (int i = 0; i < geographicalValues.size(); i++) {
            GeographicalValue geographicalValue = geographicalValues.get(i);
            dimension.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name()).getRepresentationIndex().put(geographicalValue.getCode(), i);

            for (int j = 0; j < timeValues.size(); j++) {
                String timeValue = timeValues.get(j);
                dimension.get(IndicatorDataDimensionTypeEnum.TIME.name()).getRepresentationIndex().put(timeValue, j);

                for (int k = 0; k < measureValues.size(); k++) {
                    MeasureDimensionTypeEnum measureValue = measureValues.get(k);
                    dimension.get(IndicatorDataDimensionTypeEnum.MEASURE.name()).getRepresentationIndex().put(measureValue.name(), k);

                    // Observation ID: Be careful!!! don't change order of ids
                    String id = new StringBuilder().append(geographicalValue.getCode()).append("#").append(timeValue).append("#").append(measureValue).toString();
                    ObservationDto observationDto = observationMap.get(id);
                    String value = null;
                    if (observationDto != null) {
                        value = observationDto.getPrimaryMeasure();
                    }
                    observations.add(value);
                }
            }
        }

        DataType dataType = new DataType();
        dataType.setObservation(observations);
        dataType.setFormatId(formatIds);
        dataType.setFormatSize(formatSizes);
        dataType.setDimension(dimension);
        return dataType;
    }
}
