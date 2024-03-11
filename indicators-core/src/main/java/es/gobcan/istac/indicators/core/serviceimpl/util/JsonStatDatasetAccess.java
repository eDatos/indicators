package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class JsonStatDatasetAccess {

    private String[]                  observations;

    private List<String>              dimensionsOrderedForData;

    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    public JsonStatDatasetAccess(JsonStatData jsonStatData, List<String> geographicalDimensionsId, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        initializeObservations(jsonStatData);
        initializeDimensionsForData(jsonStatData, geographicalDimensionsId, variableElementsByCodesOfCodelist);
    }

    public String[] getObservations() {
        return observations;
    }

    public List<String> getDimensionsOrderedForData() {
        return dimensionsOrderedForData;
    }

    public List<String> getDimensionValuesOrderedForData(String dimensionId) {
        return dimensionValuesOrderedForDataByDimensionId.get(dimensionId);
    }

    private void initializeDimensionsForData(JsonStatData jsonStatData, List<String> geographicalDimensionsId, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        List<String> dimensionRepresentations = jsonStatData.getId();
        this.dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        this.dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (String dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = jsonStatData.getDimension(dimensionRepresentation).getLabel();
            this.dimensionsOrderedForData.add(dimensionId);

            Map<String, Integer> categories = jsonStatData.getDimension(dimensionRepresentation).getCategory().getIndex();

            this.dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(categories.size()));
            for (int i = 0; i < categories.size(); i++) {

                if (geographicalDimensionsId.contains(dimensionId)) {
                    String variableElement = variableElementsByCodesOfCodelist.get(getKey(categories, i));

                    if (variableElement != null) {
                        this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(variableElement);
                    } else {
                        throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VARIABLE_ELEMENT_NOT_FOUND_WITH_CODE, getKey(categories, i));
                    }
                } else {
                    this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(getKey(categories, i));
                }
            }
        }
    }

    private void initializeObservations(JsonStatData jsonStatData) {
        this.observations = jsonStatData.getValue().toArray(new String[0]);
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
