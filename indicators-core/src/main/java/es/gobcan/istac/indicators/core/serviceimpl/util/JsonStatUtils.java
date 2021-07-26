package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;

public class JsonStatUtils {

    private static final String OPERATION_SEPARATOR = "_";

    private JsonStatUtils() {
    }

    public static Data jsonStatDataToData(String uuid, JsonStatData jsonStatData) {
        if (jsonStatData == null) {
            return null;
        }

        Data target = new Data();

        // TODO EDATOS-3380 Aclarar con Rita/Javi? si estos mapeos son correctos y ver que pasa con los que faltan

        // QueryEnvironmentEnum
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.JSON_STAT);

        // Uuid
        target.setUuid(uuid);

        // Title
        target.setTitle(jsonStatData.getLabel());

        // PX Uri
        // target.setPxUri(pxUri);

        // Stub
        // target.setStub(stub);

        // Heading
        // target.setHeading(heading);

        // Value Labels
        target.setValueLabels(jsonStatData.getValueLabels());

        // Value Codes
        target.setValueCodes(jsonStatData.getValueCodes());

        // Temporal Variables
        target.setTemporalVariable(jsonStatData.getTemporalVariable());

        // Temporal Value
        // target.setTemporalValue(temporalValue);

        // Spatial Variables
        target.setSpatialVariables(JsonStatUtils.toList(jsonStatData.getSpatialVariable()));
        // target.setGeographicalValueDto(geographicalValueDto);

        // Cont Variable
        target.setContVariable(jsonStatData.getContVariable());

        // Survey Code
        target.setSurveyCode(JsonStatUtils.getOperationCode(jsonStatData.getSurveyCode()));

        // Survey Title
        target.setSurveyTitle(jsonStatData.getSurveyTitle());

        // Publishers
        target.setPublishers(JsonStatUtils.toList(jsonStatData.getSource()));

        // Data
        // target.processData(dataList);

        // VariablesInOrder
        // target.setVariablesInOrder(variablesInOrder);

        return target;
    }

    public static Map<String, DataContent> jsonStatDataValuesToDataContent(JsonStatData jsonStatData) {
        Map<String, DataContent> data = new HashMap<>();

        // for (String dimensionKey : jsonStatData.getDimension().keySet()) {
        // jsonStatData.getDimension(dimensionKey).getCategory()
        // }

        return data;
    }

    public static String getOperationCode(String surveyCode) {
        if (surveyCode != null) {
            return surveyCode.split(OPERATION_SEPARATOR)[0];
        }

        return null;
    }

    public static List<String> toList(String value) {
        List<String> returnedList = new ArrayList();

        if (value != null) {
            returnedList.add(value);
        }

        return returnedList;
    }

}
