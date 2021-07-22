package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Arrays;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;

public class JsonStatUtils {

    private JsonStatUtils() {
    }

    public static Data jsonStatDataToData(JsonStatData jsonStatData) {
        if (jsonStatData == null) {
            return null;
        }

        Data target = new Data();

        // TODO EDATOS-3380 Aclarar con Rita/Javi? si estos mapeos son correctos y ver que pasa con los que faltan

        // QueryEnvironmentEnum
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.JSON_STAT);

        // UUid
        // target.setUuid(query.getUrn());

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
        // target.setSpatialVariables(spatialVariable);
        // target.setGeographicalValueDto(geographicalValueDto);

        // Cont Variable
        target.setContVariable(jsonStatData.getContVariable());

        // Survey Code
        target.setSurveyCode(jsonStatData.getSurveyCode());

        // Survey Title
        target.setSurveyTitle(jsonStatData.getSurveyTitle());

        // Publishers
        target.setPublishers(Arrays.asList(jsonStatData.getSource()));

        // Data
        // target.processData(dataList);

        // VariablesInOrder
        // target.setVariablesInOrder(variablesInOrder);

        return target;
    }

}
