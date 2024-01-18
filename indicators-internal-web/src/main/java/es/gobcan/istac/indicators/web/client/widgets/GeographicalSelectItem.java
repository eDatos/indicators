package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.LinkedHashMap;

import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.widgets.windows.search.SearchRelatedResourceLinkItem;
import es.gobcan.istac.indicators.web.shared.GetRelatedResourcesResult;

public class GeographicalSelectItem extends CustomCanvasItem {

    private CustomSelectItem                     geoGranularitItem;
    private SearchRelatedResourceLinkItem        geoValueItem;
    private SearchRelatedResourcePaginatedWindow searchGeoValueWindow;

    public SearchRelatedResourceLinkItem getGeoValueItem() {
        return geoValueItem;
    }

    public void setGeoValueItem(SearchRelatedResourceLinkItem geoValueItem) {
        this.geoValueItem = geoValueItem;
    }

    private CustomDynamicForm  form;
    private IndicatorUiHandler uiHandlers;

    public IndicatorUiHandler getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public GeographicalSelectItem(String name, String title, IndicatorUiHandler uiHandlers) {
        super(name, title);
        this.uiHandlers = uiHandlers;
        create(FormItemUtils.FORM_ITEM_WIDTH);
    }

    public GeographicalSelectItem(String name, String title, String formItemWidth, IndicatorUiHandler uiHandlers) {
        super(name, title);
        this.uiHandlers = uiHandlers;
        create(formItemWidth);
    }

    private void create(String formItemWidth) {
        setTitleVAlign(VerticalAlignment.TOP);
        geoGranularitItem = new CustomSelectItem(DataSourceDS.GEO_VALUE_GRANULARITY, "geo-gran");
        geoGranularitItem.setWidth(formItemWidth);
        geoGranularitItem.setShowTitle(false);
        geoValueItem = createGeoValueItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
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

    public void setGeoValuesValueMap(GetRelatedResourcesResult result) {
        if (this.searchGeoValueWindow != null) {
            searchGeoValueWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchGeoValueWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
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

    @Override
    public void clearValue() {
        form.clearErrors(true);
        geoGranularitItem.clearValue();
        geoValueItem.clearValue();
    }

    public void clearGeographicalValue() {
        form.clearErrors(true);
        geoValueItem.clearValue();
    }

    public boolean validateItem() {
        return super.validate() && (form.isVisible() ? form.validate(false) : true);
    }

    @Override
    public Boolean validate() {
        return validateItem();
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }

    public RelatedResourceDto getSelectedGeoValue() {
        return ((SearchRelatedResourceLinkItem) form.getItem(DataSourceDS.GEO_VALUE)).getRelatedResourceDto();
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEMS CREATION
    // ------------------------------------------------------------------------------------------------------------

    private SearchRelatedResourceLinkItem createGeoValueItem(String name, String title) {

        SearchRelatedResourceLinkItem geoValueItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        geoValueItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                showSearchGeographicalValueWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedGeoValue = searchGeoValueWindow.getSelectedRelatedResource();
                        searchGeoValueWindow.markForDestroy();
                        // Set selected family in form
                        ((SearchRelatedResourceLinkItem) form.getItem(DataSourceDS.GEO_VALUE)).setRelatedResource(selectedGeoValue);
                    }
                });
            }
        });
        return geoValueItem;
    }

    private void showSearchGeographicalValueWindow(com.smartgwt.client.widgets.form.fields.events.ClickHandler acceptButtonHandler) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        searchGeoValueWindow = new SearchRelatedResourcePaginatedWindow(getConstants().dataSourceGeographicalValueSelection(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                String granularityUuid = form.getValueAsString(DataSourceDS.GEO_VALUE_GRANULARITY);
                getUiHandlers().retrieveGeographicalValuesByGranularity(firstResult, maxResults, searchGeoValueWindow.getRelatedResourceCriteria(), granularityUuid);
            }
        });

        String granularityUuid = form.getValueAsString(DataSourceDS.GEO_VALUE_GRANULARITY);
        getUiHandlers().retrieveGeographicalValuesByGranularity(FIRST_RESULST, MAX_RESULTS, null, granularityUuid);

        searchGeoValueWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchGeoValueWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                String granularityUuid = form.getValueAsString(DataSourceDS.GEO_VALUE_GRANULARITY);
                getUiHandlers().retrieveGeographicalValuesByGranularity(firstResult, maxResults, criteria, granularityUuid);
            }
        });
        searchGeoValueWindow.getSave().addClickHandler(acceptButtonHandler);
    }
}
