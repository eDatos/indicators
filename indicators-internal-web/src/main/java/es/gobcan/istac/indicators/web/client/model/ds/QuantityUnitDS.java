package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class QuantityUnitDS extends DataSource {

    public static String UUID            = "quantity-unit-uuid";
    public static String SYMBOL          = "quantity-unit-symbol";
    public static String SYMBOL_POSITION = "quantity-unit-symbol-position";
    public static String TITLE           = "quantity-unit-title";

    public static String ORDER_BY        = "quantity-unit-order-by";
    public static String ORDER_TYPE      = "quantity-unit-order-type";

    public static String DTO             = "quantity-unit-dto";

    public QuantityUnitDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().quantityUnitUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }

}
