package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter.AdminGeoValuesTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoValuesUiHandlers;
import es.gobcan.istac.indicators.web.client.model.GeoValueRecord;
import es.gobcan.istac.indicators.web.client.model.ds.GeoValueDS;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.GeographicalValuesSearchSectionStack;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

public class AdminGeoValuesTabViewImpl extends ViewWithUiHandlers<AdminGeoValuesUiHandlers> implements AdminGeoValuesTabView {

    private HLayout                              panel;

    private PaginatedCheckListGrid               listGrid;

    private GeographicalValuesSearchSectionStack searchSectionStack;

    private GeoValuePanel                        geoValuePanel;

    public AdminGeoValuesTabViewImpl() {
        super();

        // Search

        searchSectionStack = new GeographicalValuesSearchSectionStack();

        // ListGrid

        listGrid = new PaginatedCheckListGrid(IndicatorsWebConstants.LISTGRID_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                GeoValueCriteria criteria = getGeoValueCriteria();
                criteria.setFirstResult(firstResult);
                criteria.setMaxResults(maxResults);
                getUiHandlers().retrieveGeoValues(criteria);
            }
        });

        listGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        listGrid.getListGrid().setAutoFitMaxRecords(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
        ListGridField uuidField = new ListGridField(GeoValueDS.UUID, IndicatorsWeb.getConstants().geoValueUuid());
        ListGridField codeField = new ListGridField(GeoValueDS.CODE, IndicatorsWeb.getConstants().geoValueCode());
        ListGridField titleField = new ListGridField(GeoValueDS.TITLE, IndicatorsWeb.getConstants().geoValueTitle());
        ListGridField granularityField = new ListGridField(GeoValueDS.GRANULARITY_TITLE, IndicatorsWeb.getConstants().geoValueGranularity());
        ListGridField orderField = new ListGridField(GeoValueDS.ORDER, IndicatorsWeb.getConstants().geoValueOrder());
        listGrid.getListGrid().setFields(uuidField, codeField, titleField, granularityField, orderField);

        // Show data source details when record clicked
        listGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getListGrid().getSelectedRecords() != null && listGrid.getListGrid().getSelectedRecords().length == 1) {
                    GeoValueRecord record = (GeoValueRecord) listGrid.getListGrid().getSelectedRecord();
                    GeographicalValueDto selected = record.getDto();
                    selectGeoGranularity(selected);
                } else {
                    // No record selected
                    deselectQuantityUnit();
                }
            }
        });

        geoValuePanel = new GeoValuePanel();
        geoValuePanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(searchSectionStack);
        listPanel.addMember(listGrid);

        HLayout leftLayout = new HLayout();
        leftLayout.setWidth("50%");
        leftLayout.addMember(listPanel);

        HLayout rightLayout = new HLayout();
        rightLayout.setWidth("50%");
        rightLayout.addMember(geoValuePanel);

        panel = new HLayout();
        panel.setMargin(15);
        panel.setMembersMargin(5);
        panel.addMember(leftLayout);
        panel.addMember(rightLayout);

    }

    @Override
    public void setUiHandlers(AdminGeoValuesUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    private void selectGeoGranularity(GeographicalValueDto dto) {
        if (dto.getUuid() == null) {
            listGrid.getListGrid().deselectAllRecords();
        }

        geoValuePanel.setGeoValueDto(dto);
    }

    private void deselectQuantityUnit() {
        geoValuePanel.hide();
    }

    // DATA

    @Override
    public void setGeoValues(int firstResult, List<GeographicalValueDto> dtos, int totalResults) {
        geoValuePanel.hide();

        listGrid.getListGrid().resetSort();
        GeoValueRecord[] records = new GeoValueRecord[dtos.size()];
        int index = 0;
        for (GeographicalValueDto ds : dtos) {
            records[index++] = RecordUtils.getGeoValueRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, dtos.size(), totalResults);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public GeoValueCriteria getGeoValueCriteria() {
        return searchSectionStack.getGeoValueCriteria();
    }

    public class GeoValuePanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalForm;

        public GeoValuePanel() {
            mainFormLayout = new InternationalMainFormLayout(false);
            mainFormLayout.setCanDelete(false);
            mainFormLayout.setTitleLabelContents(getConstants().geoValue());

            mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    generalForm.setTranslationsShowed(translationsShowed);
                }
            });

            mainFormLayout.setStyleName("mainFormSide");

            createViewForm();

            addMember(mainFormLayout);
        }
        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        private void createViewForm() {
            generalForm = new GroupDynamicForm(getConstants().geoValueGeneral());

            ViewTextItem uuid = new ViewTextItem(GeoValueDS.UUID, getConstants().geoValueUuid());
            ViewTextItem code = new ViewTextItem(GeoValueDS.CODE, getConstants().geoValueCode());
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(GeoValueDS.TITLE, getConstants().geoValueTitle());
            ViewMultiLanguageTextItem granularity = new ViewMultiLanguageTextItem(GeoValueDS.GRANULARITY_TITLE, getConstants().geoValueGranularity());
            ViewTextItem latitude = new ViewTextItem(GeoValueDS.LATITUDE, getConstants().geoValueLatitude());
            ViewTextItem longitude = new ViewTextItem(GeoValueDS.LONGITUDE, getConstants().geoValueLongitude());
            ViewTextItem order = new ViewTextItem(GeoValueDS.ORDER, getConstants().geoValueOrder());
            generalForm.setFields(uuid, code, title, granularity, latitude, longitude, order);

            mainFormLayout.addViewCanvas(generalForm);
        }

        public void setGeoValueDto(GeographicalValueDto dto) {

            fillViewForm(dto);

            mainFormLayout.setViewMode();

            show();
        }

        private void fillViewForm(GeographicalValueDto dto) {
            generalForm.setValue(GeoValueDS.UUID, dto.getUuid());
            generalForm.setValue(GeoValueDS.CODE, dto.getCode());
            generalForm.setValue(GeoValueDS.TITLE, dto.getTitle());
            generalForm.setValue(GeoValueDS.GRANULARITY_TITLE, dto.getGranularity() != null ? dto.getGranularity().getTitle() : null);
            if (dto.getLatitude() != null) {
                generalForm.setValue(GeoValueDS.LATITUDE, dto.getLatitude());
            } else {
                generalForm.clearValue(GeoValueDS.LATITUDE);
            }
            if (dto.getLongitude() != null) {
                generalForm.setValue(GeoValueDS.LONGITUDE, dto.getLongitude());
            } else {
                generalForm.clearValue(GeoValueDS.LONGITUDE);
            }
            generalForm.setValue(GeoValueDS.ORDER, dto.getOrder());

        }
    }
}
