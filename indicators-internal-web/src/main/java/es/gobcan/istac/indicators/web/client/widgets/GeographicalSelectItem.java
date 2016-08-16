package es.gobcan.istac.indicators.web.client.widgets;

import java.util.LinkedHashMap;

import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class GeographicalSelectItem extends CustomCanvasItem {

    private CustomSelectItem  geoGranularitItem;
    private CustomSelectItem  geoValueItem;
    private CustomDynamicForm form;

    public GeographicalSelectItem(String name, String title) {
        super(name, title);
        create(FormItemUtils.FORM_ITEM_WIDTH);
    }

    public GeographicalSelectItem(String name, String title, String formItemWidth) {
        super(name, title);
        create(formItemWidth);
    }

    private void create(String formItemWidth) {
        setTitleVAlign(VerticalAlignment.TOP);
        geoGranularitItem = new CustomSelectItem("geo-gran", "geo-gran");
        geoGranularitItem.setWidth(formItemWidth);
        geoGranularitItem.setShowTitle(false);
        geoValueItem = new CustomSelectItem("geo-val", "geo-val");
        geoValueItem.setWidth(formItemWidth);
        geoValueItem.setShowTitle(false);
        geoValueItem.setStartRow(true);
        form = new CustomDynamicForm();
        form.setWidth(formItemWidth);
        form.setColWidths("100%");
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

    @Override
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
