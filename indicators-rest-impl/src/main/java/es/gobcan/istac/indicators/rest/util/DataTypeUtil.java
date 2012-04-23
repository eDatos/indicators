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
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;
import es.gobcan.istac.indicators.rest.types.DataType;

public class DataTypeUtil {

    public static DataType createDataType(List<GeographicalValue> geographicalValues, List<String> timeValues, List<MeasureDimensionTypeEnum> measureValues, Map<String, ObservationDto> observationMap) {
        List<String> observations = new ArrayList<String>();
        List<String> format = new ArrayList<String>();
        Map<String, DataDimensionType> dimension = new LinkedHashMap<String, DataDimensionType>();

        format.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        format.add(IndicatorDataDimensionTypeEnum.TIME.name());
        format.add(IndicatorDataDimensionTypeEnum.MEASURE.name());

        DataRepresentationType dataRepresentationTypeGeographical = new DataRepresentationType();
        dataRepresentationTypeGeographical.setSize(geographicalValues.size());
        dataRepresentationTypeGeographical.setIndex(new LinkedHashMap<String, Integer>());
        DataDimensionType dataDimensionTypeGeographical = new DataDimensionType();
        dataDimensionTypeGeographical.setRepresentation(dataRepresentationTypeGeographical);        
        dimension.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dataDimensionTypeGeographical);

        DataRepresentationType dataRepresentationTypeTime = new DataRepresentationType();
        dataRepresentationTypeTime.setSize(timeValues.size());
        dataRepresentationTypeTime.setIndex(new LinkedHashMap<String, Integer>());
        DataDimensionType dataDimensionTypeTime = new DataDimensionType();
        dataDimensionTypeTime.setRepresentation(dataRepresentationTypeTime);
        dimension.put(IndicatorDataDimensionTypeEnum.TIME.name(), dataDimensionTypeTime);

        DataRepresentationType dataRepresentationTypeMeasure = new DataRepresentationType();
        dataRepresentationTypeMeasure.setSize(measureValues.size());
        dataRepresentationTypeMeasure.setIndex(new LinkedHashMap<String, Integer>());
        DataDimensionType dataDimensionTypeMeasure = new DataDimensionType();
        dataDimensionTypeMeasure.setRepresentation(dataRepresentationTypeMeasure);
        dimension.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), dataDimensionTypeMeasure);

        for (int i = 0; i < geographicalValues.size(); i++) {
            GeographicalValue geographicalValue = geographicalValues.get(i);
            dataRepresentationTypeGeographical.getIndex().put(geographicalValue.getCode(), i);

            for (int j = 0; j < timeValues.size(); j++) {
                String timeValue = timeValues.get(j);
                dataRepresentationTypeTime.getIndex().put(timeValue, j);

                for (int k = 0; k < measureValues.size(); k++) {
                    MeasureDimensionTypeEnum measureValue = measureValues.get(k);
                    dataRepresentationTypeMeasure.getIndex().put(measureValue.name(), k);

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
        dataType.setFormat(format);
        dataType.setDimension(dimension);
        dataType.setObservation(observations);
        return dataType;
    }
}
