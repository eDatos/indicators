package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;
import static es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter.MAX_RESULTS;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
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
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoGranularitiesUiHandlers;
import es.gobcan.istac.indicators.web.client.model.GeoGranularityRecord;
import es.gobcan.istac.indicators.web.client.model.ds.GeoGranularityDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class AdminGeoGranularitiesTabViewImpl extends ViewWithUiHandlers<AdminGeoGranularitiesUiHandlers> implements AdminGeoGranularitiesTabView {

    private HLayout                panel;

    private PaginatedCheckListGrid listGrid;

    private ListGridToolStrip      toolStrip;

    private GeoGranularityPanel    geoGranularityPanel;

    public AdminGeoGranularitiesTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().geoGranularitiesDeleteTitle(), getMessages().geoGranularitiesConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectGeoGranularity(new GeographicalGranularityDto());
                geoGranularityPanel.setEditionMode();
            }

        });
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateGeographicalGranularity() ? Visibility.VISIBLE : Visibility.HIDDEN);

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
                getUiHandlers().deleteGeoGranularities(getSelectedGeoGranularities());
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveGeoGranularities(firstResult);
            }
        });

        listGrid.setHeight100();
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
                    if (listGrid.getListGrid().getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        geoGranularityPanel = new GeoGranularityPanel();
        geoGranularityPanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(toolStrip);
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
    private List<String> getSelectedGeoGranularities() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : listGrid.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(GeoGranularityDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteGeoGranularity()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private void selectGeoGranularity(GeographicalGranularityDto dto) {
        if (dto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            listGrid.getListGrid().deselectAllRecords();
        }

        geoGranularityPanel.setGeoGranularityDto(dto);
    }

    private void deselectQuantityUnit() {
        toolStrip.getDeleteButton().hide();

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
    public void onGeoGranularityCreated(GeographicalGranularityDto dto) {
        listGrid.goToLastPageAfterCreate();
    }

    @Override
    public void onGeoGranularityUpdated(GeographicalGranularityDto dto) {
        selectGeoGranularity(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class GeoGranularityPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private GeographicalGranularityDto  geoGranularityDto;

        public GeoGranularityPanel() {
            mainFormLayout = new InternationalMainFormLayout(ClientSecurityUtils.canEditGeographicalGranularity());
            mainFormLayout.setTitleLabelContents(getConstants().geoGranularity());

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
                        getUiHandlers().saveGeoGranularity(listGrid.getPageNumber(), getGeoGranularityDto());
                    }
                }
            });

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (geoGranularityDto.getUuid() == null || (geoGranularityDto.getUuid() != null && geoGranularityDto.getUuid().isEmpty())) {
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
            this.geoGranularityDto = dto;

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

        private GeographicalGranularityDto getGeoGranularityDto() {
            geoGranularityDto.setTitle(generalEditionForm.getValueAsInternationalStringDto(GeoGranularityDS.TITLE));
            geoGranularityDto.setCode(generalEditionForm.getValueAsString(GeoGranularityDS.CODE));
            return geoGranularityDto;
        }
    }
}
