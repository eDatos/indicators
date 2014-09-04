package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomFloatItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.client.IndicatorsValues;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter.AdminGeoValuesTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoValuesUiHandlers;
import es.gobcan.istac.indicators.web.client.model.GeoValueRecord;
import es.gobcan.istac.indicators.web.client.model.ds.GeoValueDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class AdminGeoValuesTabViewImpl extends ViewWithUiHandlers<AdminGeoValuesUiHandlers> implements AdminGeoValuesTabView {

    private HLayout                panel;

    private PaginatedCheckListGrid listGrid;

    private ListGridToolStrip      toolStrip;

    private GeoValuePanel          geoValuePanel;

    public AdminGeoValuesTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().geoValuesDeleteTitle(), getMessages().geoValuesConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectGeoGranularity(new GeographicalValueDto());
                geoValuePanel.setEditionMode();
                geoValuePanel.show();
            }

        });
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateGeographicalValue() ? Visibility.VISIBLE : Visibility.HIDDEN);

        toolStrip.getDeleteHandlerRegistration().removeHandler();
        toolStrip.getDeleteButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                toolStrip.getDeleteConfirmationWindow().show();
            }
        });

        toolStrip.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteGeoValues(getSelectedGeoValues());
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(AdminGeoValuesTabPresenter.MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveGeoValues(firstResult);
            }
        });

        listGrid.setHeight100();
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
                    if (listGrid.getListGrid().getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        geoValuePanel = new GeoValuePanel();
        geoValuePanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(toolStrip);
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
    // UTILS
    private List<String> getSelectedGeoValues() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : listGrid.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(GeoValueDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteGeoValue()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private void selectGeoGranularity(GeographicalValueDto dto) {
        if (dto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            listGrid.getListGrid().deselectAllRecords();
        }

        geoValuePanel.setGeoValueDto(dto);
    }

    private void deselectQuantityUnit() {
        toolStrip.getDeleteButton().hide();

        geoValuePanel.hide();
    }

    // DATA

    @Override
    public void setGeoValues(int firstResult, List<GeographicalValueDto> dtos, int totalResults) {
        geoValuePanel.hide();

        GeoValueRecord[] records = new GeoValueRecord[dtos.size()];
        int index = 0;
        for (GeographicalValueDto ds : dtos) {
            records[index++] = RecordUtils.getGeoValueRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, dtos.size(), totalResults);
    }

    @Override
    public void onGeoValueCreated(GeographicalValueDto dto) {
        listGrid.goToLastPageAfterCreate();
    }

    @Override
    public void onGeoValueUpdated(GeographicalValueDto dto) {
        selectGeoGranularity(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class GeoValuePanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private GeographicalValueDto        geoValueDto;

        public GeoValuePanel() {
            mainFormLayout = new InternationalMainFormLayout(ClientSecurityUtils.canEditGeographicalValue());
            mainFormLayout.setTitleLabelContents(getConstants().geoValue());

            // Edit: Add a custom handler to check indicator status before start editing
            mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    mainFormLayout.setEditionMode();
                }
            });

            mainFormLayout.getSave().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (generalEditionForm.validate(false)) {
                        getUiHandlers().saveGeoValue(listGrid.getPageNumber(), getGeoValueDto());
                    }
                }
            });

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (geoValueDto.getUuid() == null || (geoValueDto.getUuid() != null && geoValueDto.getUuid().isEmpty())) {
                        hide();
                    }
                }
            });

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

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().geoValueGeneral());

            ViewTextItem uuid = new ViewTextItem(GeoValueDS.UUID, getConstants().geoValueUuid());
            CustomTextItem code = new CustomTextItem(GeoValueDS.CODE, getConstants().geoValueCode());
            code.setRequired(true);
            MultiLanguageTextItem title = new MultiLanguageTextItem(GeoValueDS.TITLE, getConstants().geoValueTitle());
            title.setRequired(true);
            CustomSelectItem granularity = new CustomSelectItem(GeoValueDS.GRANULARITY, getConstants().geoValueGranularity());
            granularity.setValueMap(CommonUtils.getGeographicalGranularituesValueMap(IndicatorsValues.getGeographicalGranularities()));
            granularity.setRequired(true);
            CustomFloatItem latitude = new CustomFloatItem(GeoValueDS.LATITUDE, getConstants().geoValueLatitude());
            latitude.setRequired(true);
            CustomFloatItem longitude = new CustomFloatItem(GeoValueDS.LONGITUDE, getConstants().geoValueLongitude());
            longitude.setRequired(true);
            CustomTextItem order = new CustomTextItem(GeoValueDS.ORDER, getConstants().geoValueOrder());
            order.setRequired(true);

            generalEditionForm.setFields(uuid, code, title, granularity, latitude, longitude, order);

            mainFormLayout.addEditionCanvas(generalEditionForm);
        }
        public void setGeoValueDto(GeographicalValueDto dto) {
            this.geoValueDto = dto;

            fillViewForm(dto);
            fillEditForm(dto);

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

        private void fillEditForm(GeographicalValueDto dto) {
            generalEditionForm.setValue(GeoValueDS.UUID, dto.getUuid());
            generalEditionForm.setValue(GeoValueDS.CODE, dto.getCode());
            generalEditionForm.setValue(GeoValueDS.TITLE, dto.getTitle());
            generalEditionForm.setValue(GeoValueDS.GRANULARITY, dto.getGranularity() != null ? dto.getGranularity().getUuid() : null);
            if (dto.getLatitude() != null) {
                generalEditionForm.setValue(GeoValueDS.LATITUDE, dto.getLatitude());
            } else {
                generalEditionForm.clearValue(GeoValueDS.LATITUDE);
            }
            if (dto.getLongitude() != null) {
                generalEditionForm.setValue(GeoValueDS.LONGITUDE, dto.getLongitude());
            } else {
                generalEditionForm.clearValue(GeoValueDS.LONGITUDE);
            }
            generalEditionForm.setValue(GeoValueDS.ORDER, dto.getOrder());
        }

        private GeographicalValueDto getGeoValueDto() {
            geoValueDto.setTitle(generalEditionForm.getValueAsInternationalStringDto(GeoValueDS.TITLE));
            geoValueDto.setCode(generalEditionForm.getValueAsString(GeoValueDS.CODE));
            geoValueDto.setOrder(generalEditionForm.getValueAsString(GeoValueDS.ORDER));

            String granularityUuid = generalEditionForm.getValueAsString(GeoValueDS.GRANULARITY);
            GeographicalGranularityDto granularity = new GeographicalGranularityDto();
            granularity.setUuid(granularityUuid);
            geoValueDto.setGranularity(granularity);

            geoValueDto.setLatitude(Double.valueOf(generalEditionForm.getValueAsString(GeoValueDS.LATITUDE)));
            geoValueDto.setLongitude(Double.valueOf(generalEditionForm.getValueAsString(GeoValueDS.LONGITUDE)));
            return geoValueDto;
        }
    }
}
