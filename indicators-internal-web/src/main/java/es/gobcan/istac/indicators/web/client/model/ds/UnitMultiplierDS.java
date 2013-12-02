package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class UnitMultiplierDS extends DataSource {

    public static String UUID       = "unit-multiplier-uuid";
    public static String MULTIPLIER = "unit-multiplier-multiplier";
    public static String TITLE      = "unit-multiplier-title";

    public static String DTO        = "unit-multiplier-dto";

    public UnitMultiplierDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().unitMultiplierUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }

}
