package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryMetadataBase;

public class QueryMetamacDatasetAccess {

    public static String              DATA_SEPARATOR = " | ";

    // Data
    private String[]                  observations;
    private List<String>              dimensionsOrderedForData;
    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    public QueryMetamacDatasetAccess(Query query, Map<String, String> variableElementsByCode) throws MetamacException {

        initializeObservations(query);
        initializeDimensionsForData(query, variableElementsByCode);
    }

    public List<String> getDimensionsOrderedForData() {
        return dimensionsOrderedForData;
    }

    public List<String> getDimensionValuesOrderedForData(String dimensionId) {
        return dimensionValuesOrderedForDataByDimensionId.get(dimensionId);
    }

    public String[] getObservations() {
        return observations;
    }

    /**
     * Init observations values
     */
    private void initializeObservations(Query query) {
        this.observations = dataToDataArray(query.getData().getObservations());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(Query query, Map<String, String> variableElementsByCode) throws MetamacException {
        List<DimensionRepresentation> dimensionRepresentations = query.getData().getDimensions().getDimensions();
        this.dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        this.dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            this.dimensionsOrderedForData.add(dimensionId);

            List<CodeRepresentation> codesRepresentations = dimensionRepresentation.getRepresentations().getRepresentations();
            this.dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(codesRepresentations.size()));
            String geographicalDimensionId = getGeographicalDimensionId(query.getMetadata());
            for (CodeRepresentation codeRepresentation : codesRepresentations) {
                if (geographicalDimensionId != null && geographicalDimensionId.equals(dimensionId)) {
                    String variableElement = variableElementsByCode.get(codeRepresentation.getCode());

                    if (variableElement != null) {
                        this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(variableElement);
                    } else {
                        // TODO EDATOS-3827 ERROR PORQUE NO SE ENCUENTRA.
                    }
                } else {
                    this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(codeRepresentation.getCode());
                }
            }
        }
    }

    private String getGeographicalDimensionId(QueryMetadataBase queryMetadata) {

        if (queryMetadata == null) {
            return null;
        }

        for (Dimension dimension : queryMetadata.getDimensions().getDimensions()) {
            if (DimensionType.GEOGRAPHIC_DIMENSION.equals(dimension.getType())) {
                return dimension.getId();
            }
        }
        return null;
    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, DATA_SEPARATOR);
    }
}
