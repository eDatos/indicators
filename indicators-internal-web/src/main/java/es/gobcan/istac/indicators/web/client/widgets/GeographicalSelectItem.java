package es.gobcan.istac.indicators.web.client.widgets;

import java.util.LinkedHashMap;

import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class GeographicalSelectItem extends CustomCanvasItem {

    private SelectItem        geoGranularitItem;
    private SelectItem        geoValueItem;
    private CustomDynamicForm form;

    public GeographicalSelectItem(String name, String title) {
        super(name, title);
        setTitleVAlign(VerticalAlignment.TOP);
        geoGranularitItem = new SelectItem("geo-gran", "geo-gran");
        geoGranularitItem.setShowTitle(false);
        geoValueItem = new SelectItem("geo-val", "geo-val");
        geoValueItem.setShowTitle(false);
        geoValueItem.setStartRow(true);
        form = new CustomDynamicForm();
        form.setFields(geoGranularitItem, geoValueItem);
        form.setStyleName("canvasCellStyle");
        setCanvas(form);
    }

    public void setRequired(boolean required) {
        setTitleStyle("requiredFormLabel");
        geoGranularitItem.setRequired(required);
        geoValueItem.setRequired(required);
    }

    public void setGeoGranularitiesValueMap(LinkedHashMap<String, String> map) {
        geoGranularitItem.setValueMap(map);
    }

    public void setGeoValuesValueMap(LinkedHashMap<String, String> map) {
        geoValueItem.setValueMap(map);
    }

    public void setGeoGranularity(String granularity) {
        geoGranularitItem.setValue(granularity);
    }

    public void setGeoValue(String value) {
        geoValueItem.setValue(value);
    }

    public SelectItem getGeoGranularitySelectItem() {
        return geoGranularitItem;
    }

    public SelectItem getGeoValueSelectItem() {
        return geoValueItem;
    }

    public String getSelectedGeoValue() {
        return geoValueItem.getValueAsString();
    }

    public void clearValue() {
        form.clearErrors(true);
        geoGranularitItem.clearValue();
        geoValueItem.clearValue();
        geoValueItem.setValueMap(new String());
    }

    public boolean validateItem() {
        return super.validate() && (form.isVisible() ? form.validate(false) : true);
    }

    @Override
    public Boolean validate() {
        return validateItem();
    }

}
