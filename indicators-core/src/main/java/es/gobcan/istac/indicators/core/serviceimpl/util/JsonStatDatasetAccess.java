package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;

public class JsonStatDatasetAccess {

    private String[]                  observations;

    private List<String>              dimensionsOrderedForData;

    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    public JsonStatDatasetAccess(JsonStatData jsonStatData) {
        initializeObservations(jsonStatData);
        initializeDimensionsForData(jsonStatData);
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

    private void initializeDimensionsForData(JsonStatData jsonStatData) {
        List<String> dimensionRepresentations = jsonStatData.getId();
        this.dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        this.dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (String dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = jsonStatData.getDimension(dimensionRepresentation).getLabel();
            this.dimensionsOrderedForData.add(dimensionId);

            Map<String, Integer> categories = jsonStatData.getDimension(dimensionRepresentation).getCategory().getIndex();

            this.dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(categories.size()));
            for (int i = 0; i < categories.size(); i++) {
                this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(getKey(categories, i));
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
