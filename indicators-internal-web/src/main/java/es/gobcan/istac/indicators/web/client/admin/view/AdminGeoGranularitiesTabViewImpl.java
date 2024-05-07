package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
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

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoGranularitiesUiHandlers;
import es.gobcan.istac.indicators.web.client.model.GeoGranularityRecord;
import es.gobcan.istac.indicators.web.client.model.ds.GeoGranularityDS;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class AdminGeoGranularitiesTabViewImpl extends ViewWithUiHandlers<AdminGeoGranularitiesUiHandlers> implements AdminGeoGranularitiesTabView {

    private HLayout                panel;

    private PaginatedCheckListGrid listGrid;

    private GeoGranularityPanel    geoGranularityPanel;

    public AdminGeoGranularitiesTabViewImpl() {
        super();

        // ListGrid

        listGrid = new PaginatedCheckListGrid(IndicatorsWebConstants.LISTGRID_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveGeoGranularities(firstResult);
            }
        });

        listGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        listGrid.getListGrid().setAutoFitMaxRecords(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
        ListGridField uuidField = new ListGridField(GeoGranularityDS.UUID, IndicatorsWeb.getConstants().geoGranularityUuid());
        ListGridField codeField = new ListGridField(GeoGranularityDS.CODE, IndicatorsWeb.getConstants().geoGranularityCode());
        ListGridField titleField = new ListGridField(GeoGranularityDS.TITLE, IndicatorsWeb.getConstants().geoGranularityTitle());
        listGrid.getListGrid().setFields(uuidField, codeField, titleField);

        // Show data source details when record clicked
        listGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getListGrid().getSelectedRecords() != null && listGrid.getListGrid().getSelectedRecords().length == 1) {
                    GeoGranularityRecord record = (GeoGranularityRecord) listGrid.getListGrid().getSelectedRecord();
                    GeographicalGranularityDto selected = record.getDto();
                    selectGeoGranularity(selected);
                } else {
                    // No record selected
                    deselectQuantityUnit();
                }
            }
        });

        geoGranularityPanel = new GeoGranularityPanel();
        geoGranularityPanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(listGrid);

        HLayout leftLayout = new HLayout();
        leftLayout.setWidth("50%");
        leftLayout.addMember(listPanel);

        HLayout rightLayout = new HLayout();
        rightLayout.setWidth("50%");
        rightLayout.addMember(geoGranularityPanel);

        panel = new HLayout();
        panel.setMargin(15);
        panel.setMembersMargin(5);
        panel.addMember(leftLayout);
        panel.addMember(rightLayout);
    }

    // UTILS

    private void selectGeoGranularity(GeographicalGranularityDto dto) {
        if (dto.getUuid() == null) {
            listGrid.getListGrid().deselectAllRecords();
        }

        geoGranularityPanel.setGeoGranularityDto(dto);
    }

    private void deselectQuantityUnit() {
        geoGranularityPanel.hide();
    }

    // DATA

    @Override
    public void setGeoGranularities(int firstResult, List<GeographicalGranularityDto> dtos, int totalResults) {
        geoGranularityPanel.hide();

        GeoGranularityRecord[] records = new GeoGranularityRecord[dtos.size()];
        int index = 0;
        for (GeographicalGranularityDto ds : dtos) {
            records[index++] = RecordUtils.getGeoGranularityRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, dtos.size(), totalResults);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class GeoGranularityPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        public GeoGranularityPanel() {
            mainFormLayout = new InternationalMainFormLayout(false);
            mainFormLayout.setCanDelete(false);
            mainFormLayout.setTitleLabelContents(getConstants().geoGranularity());

            mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    generalForm.setTranslationsShowed(translationsShowed);
                    generalEditionForm.setTranslationsShowed(translationsShowed);
                }
            });

            mainFormLayout.setStyleName("mainFormSide");

            createViewForm();
            createEditionForm();

            addMember(mainFormLayout);
        }

        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        private void createViewForm() {
            generalForm = new GroupDynamicForm(getConstants().geoGranularityGeneral());

            ViewTextItem uuid = new ViewTextItem(GeoGranularityDS.UUID, getConstants().geoGranularityUuid());
            ViewTextItem code = new ViewTextItem(GeoGranularityDS.CODE, getConstants().geoGranularityCode());
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(GeoGranularityDS.TITLE, getConstants().geoGranularityTitle());

            generalForm.setFields(uuid, code, title);

            mainFormLayout.addViewCanvas(generalForm);
        }

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().geoGranularityGeneral());

            ViewTextItem uuid = new ViewTextItem(GeoGranularityDS.UUID, getConstants().geoGranularityUuid());
            MultiLanguageTextItem title = new MultiLanguageTextItem(GeoGranularityDS.TITLE, getConstants().geoGranularityTitle());
            title.setRequired(true);
            CustomTextItem code = new CustomTextItem(GeoGranularityDS.CODE, getConstants().geoGranularityCode());

            generalEditionForm.setFields(uuid, code, title);

            mainFormLayout.addEditionCanvas(generalEditionForm);
        }

        public void setGeoGranularityDto(GeographicalGranularityDto dto) {

            fillViewForm(dto);
            fillEditForm(dto);

            mainFormLayout.setViewMode();

            show();
        }

        private void fillViewForm(GeographicalGranularityDto dto) {
            generalForm.setValue(GeoGranularityDS.UUID, dto.getUuid());
            generalForm.setValue(GeoGranularityDS.CODE, dto.getCode());
            generalForm.setValue(GeoGranularityDS.TITLE, dto.getTitle());
        }

        private void fillEditForm(GeographicalGranularityDto dto) {
            generalEditionForm.setValue(GeoGranularityDS.UUID, dto.getUuid());
            generalEditionForm.setValue(GeoGranularityDS.CODE, dto.getCode());
            generalEditionForm.setValue(GeoGranularityDS.TITLE, dto.getTitle());
        }
    }
}
