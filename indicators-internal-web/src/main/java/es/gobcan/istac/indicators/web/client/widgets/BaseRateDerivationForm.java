package es.gobcan.istac.indicators.web.client.widgets;

import es.gobcan.istac.indicators.web.client.enums.DataSourceQuantityType;

public class BaseRateDerivationForm extends BaseQuantityForm {

    protected DataSourceQuantityType dataSourceQuantityType;

    public BaseRateDerivationForm(String groupTitle, DataSourceQuantityType dataSourceQuantityType) {
        super(groupTitle);

    }

}
