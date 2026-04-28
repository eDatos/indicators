package es.gobcan.istac.indicators.core.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;

public class IndicatorObservationsBaseVO {

    private List<String>               geographicalCodes;
    private List<String>               timeCodes;
    private List<String>               measureCodes;
    private List<AttributeInstanceDto>           datasetAndDimensionAttributes  = new ArrayList<AttributeInstanceDto>();
    private Set<String>                          multilingualAttributeIds       = new HashSet<String>();
    private Map<String, String>                  datasetEnumLabels              = new HashMap<String, String>();
    private Map<String, Map<String, String>>     dimensionEnumLabelMaps         = new HashMap<String, Map<String, String>>();

    public List<String> getGeographicalCodes() {
        return geographicalCodes;
    }

    public void setGeographicalCodes(List<String> geographicalCodes) {
        this.geographicalCodes = geographicalCodes;
    }

    public List<String> getTimeCodes() {
        return timeCodes;
    }

    public void setTimeCodes(List<String> timeCodes) {
        this.timeCodes = timeCodes;
    }

    public List<String> getMeasureCodes() {
        return measureCodes;
    }

    public void setMeasureCodes(List<String> measureCodes) {
        this.measureCodes = measureCodes;
    }

    public List<AttributeInstanceDto> getDatasetAndDimensionAttributes() {
        return datasetAndDimensionAttributes;
    }

    public void setDatasetAndDimensionAttributes(List<AttributeInstanceDto> datasetAndDimensionAttributes) {
        this.datasetAndDimensionAttributes = datasetAndDimensionAttributes;
    }

    public Set<String> getMultilingualAttributeIds() {
        return multilingualAttributeIds;
    }

    public void setMultilingualAttributeIds(Set<String> multilingualAttributeIds) {
        this.multilingualAttributeIds = multilingualAttributeIds;
    }

    public Map<String, String> getDatasetEnumLabels() {
        return datasetEnumLabels;
    }

    public void setDatasetEnumLabels(Map<String, String> datasetEnumLabels) {
        this.datasetEnumLabels = datasetEnumLabels;
    }

    public Map<String, Map<String, String>> getDimensionEnumLabelMaps() {
        return dimensionEnumLabelMaps;
    }

    public void setDimensionEnumLabelMaps(Map<String, Map<String, String>> dimensionEnumLabelMaps) {
        this.dimensionEnumLabelMaps = dimensionEnumLabelMaps;
    }

}
