package es.gobcan.istac.indicators.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Data, contains values
 */
public class DataGpe extends Data {

    private List<DataContent> dataList;

    @Override
    @JsonProperty("data")
    public void processData(List<DataContent> dataList) {
        this.dataList = dataList;
    }

    public void processData(Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {

        List<Integer> indexSpatialVariable = getIndexSpatialVariable();

        setData(new HashMap<String, DataContent>());
        for (DataContent content : dataList) {

            for (int spatialIndex : indexSpatialVariable) {
                changeCodeOfCodelistForVariableElement(variableElementsByCodesOfCodelist, spatialIndex, content);

                String key = StringUtils.join(content.getDimCodes(), "#");
                getData().put(key, content);
            }
        }
    }

    private List<Integer> getIndexSpatialVariable() {
        List<Integer> indexSpatialVariable = new ArrayList<Integer>();
        List<String> dimensions = new ArrayList<String>();

        for (String dim : this.getStub()) {
            dimensions.add(dim);
        }

        for (String dim : this.getHeading()) {
            dimensions.add(dim);
        }

        if (this.getSpatialVariables() != null && !this.getSpatialVariables().isEmpty() && !dimensions.isEmpty()) {
            for (String spatialVariableName : this.getSpatialVariables()) {
                indexSpatialVariable.add(dimensions.indexOf(spatialVariableName));
            }
        }
        return indexSpatialVariable;
    }

    private void changeCodeOfCodelistForVariableElement(Map<String, String> variableElementsByCodesOfCodelist, int indexSpatialVariable, DataContent content) throws MetamacException {
        if (indexSpatialVariable > -1) {
            String codeSpatialVariable = content.getDimCodes().get(indexSpatialVariable);
            String variableElement = variableElementsByCodesOfCodelist.get(codeSpatialVariable);

            if (variableElement != null) {
                content.getDimCodes().set(indexSpatialVariable, variableElement);
            } else {
                throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VARIABLE_ELEMENT_NOT_FOUND_WITH_CODE, codeSpatialVariable);
            }
        }
    }

}
