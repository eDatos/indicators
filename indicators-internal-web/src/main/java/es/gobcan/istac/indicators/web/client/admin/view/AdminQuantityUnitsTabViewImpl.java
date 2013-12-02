package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;
import static es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter.MAX_RESULTS;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminQuantityUnitsUiHandlers;
import es.gobcan.istac.indicators.web.client.model.QuantityUnitRecord;
import es.gobcan.istac.indicators.web.client.model.ds.QuantityUnitDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class AdminQuantityUnitsTabViewImpl extends ViewWithUiHandlers<AdminQuantityUnitsUiHandlers> implements AdminQuantityUnitsTabView {

    private VLayout                panel;

    private PaginatedCheckListGrid listGrid;

    private ListGridToolStrip      toolStrip;

    private QuantityUnitPanel      quantityUnitPanel;

    public AdminQuantityUnitsTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().quantityUnitsDeleteTitle(), getMessages().quantityUnitsConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectQuantityUnit(new QuantityUnitDto());
                quantityUnitPanel.setEditionMode();
            }

        });
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateQuantityUnit() ? Visibility.VISIBLE : Visibility.HIDDEN);

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
                getUiHandlers().deleteQuantityUnits(getSelectedQuantityUnits());
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveQuantityUnits(firstResult, maxResults);
            }
        });
        listGrid.setHeight(150);
        ListGridField uuidField = new ListGridField(QuantityUnitDS.UUID, IndicatorsWeb.getConstants().quantityUnitUuid());
        ListGridField titleField = new ListGridField(QuantityUnitDS.TITLE, IndicatorsWeb.getConstants().quantityUnitTitle());
        listGrid.getListGrid().setFields(uuidField, titleField);
        // Show data source details when record clicked
        listGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getListGrid().getSelectedRecords() != null && listGrid.getListGrid().getSelectedRecords().length == 1) {
                    QuantityUnitRecord record = (QuantityUnitRecord) listGrid.getListGrid().getSelectedRecord();
                    QuantityUnitDto selected = record.getDto();
                    selectQuantityUnit(selected);
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

        quantityUnitPanel = new QuantityUnitPanel();
        quantityUnitPanel.hide();

        panel = new VLayout();
        panel.setMargin(15);
        panel.addMember(toolStrip);
        panel.addMember(listGrid);
        panel.addMember(quantityUnitPanel);

    }

    // UTILS
    private List<String> getSelectedQuantityUnits() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : listGrid.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(QuantityUnitDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteQuantityUnit()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private void selectQuantityUnit(QuantityUnitDto dto) {
        if (dto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            listGrid.getListGrid().deselectAllRecords();
        }

        quantityUnitPanel.setQuantityUnitDto(dto);
    }

    private void deselectQuantityUnit() {
        toolStrip.getDeleteButton().hide();

        quantityUnitPanel.hide();
    }

    // DATA

    @Override
    public void setQuantityUnits(int firstResult, List<QuantityUnitDto> quantityUnits, int totalResults) {
        quantityUnitPanel.hide();

        QuantityUnitRecord[] records = new QuantityUnitRecord[quantityUnits.size()];
        int index = 0;
        for (QuantityUnitDto ds : quantityUnits) {
            records[index++] = RecordUtils.getQuantityUnitRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, quantityUnits.size(), totalResults);
    }

    @Override
    public void onQuantityUnitCreated(QuantityUnitDto dto) {
        listGrid.goToLastPage();
        selectQuantityUnit(dto);
    }

    @Override
    public void onQuantityUnitUpdated(QuantityUnitDto dto) {
        selectQuantityUnit(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class QuantityUnitPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private QuantityUnitDto             quantityUnitDto;

        public QuantityUnitPanel() {
            mainFormLayout = new InternationalMainFormLayout(ClientSecurityUtils.canEditQuantityUnit());
            mainFormLayout.setTitleLabelContents(getConstants().quantityUnit());

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
                        getUiHandlers().saveQuantityUnit(listGrid.getPageNumber(), getQuantityUnitDto());
                    }
                }
            });

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (quantityUnitDto.getUuid() == null || (quantityUnitDto.getUuid() != null && quantityUnitDto.getUuid().isEmpty())) {
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

            createViewForm();
            createEditionForm();

            addMember(mainFormLayout);
        }

        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        private void createViewForm() {
            generalForm = new GroupDynamicForm(getConstants().quantityUnitGeneral());

            ViewTextItem uuid = new ViewTextItem(QuantityUnitDS.UUID, getConstants().quantityUnitUuid());
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(QuantityUnitDS.TITLE, getConstants().quantityUnitTitle());
            ViewTextItem symbol = new ViewTextItem(QuantityUnitDS.SYMBOL, getConstants().quantityUnitSymbol());
            ViewTextItem symbolPosition = new ViewTextItem(QuantityUnitDS.SYMBOL_POSITION, getConstants().quantityUnitSymbolPosition());

            generalForm.setFields(uuid, title, symbol, symbolPosition);

            mainFormLayout.addViewCanvas(generalForm);
        }

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().quantityUnitGeneral());

            ViewTextItem uuid = new ViewTextItem(QuantityUnitDS.UUID, getConstants().quantityUnitUuid());
            MultiLanguageTextItem title = new MultiLanguageTextItem(QuantityUnitDS.TITLE, getConstants().quantityUnitTitle());
            title.setRequired(true);
            CustomTextItem symbol = new CustomTextItem(QuantityUnitDS.SYMBOL, getConstants().quantityUnitSymbol());
            symbol.addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    generalEditionForm.markForRedraw();
                }
            });

            CustomSelectItem symbolPosition = new CustomSelectItem(QuantityUnitDS.SYMBOL_POSITION, getConstants().quantityUnitSymbolPosition());
            symbolPosition.setValueMap(CommonUtils.getQuantityUnitsSymbolPositionValueMap());
            symbolPosition.setValidators(new RequiredIfValidator(new RequiredIfFunction() {

                @Override
                public boolean execute(FormItem formItem, Object value) {
                    return !StringUtils.isEmpty(generalEditionForm.getValueAsString(QuantityUnitDS.SYMBOL));
                }
            }));

            symbolPosition.setShowIfCondition(new FormItemIfFunction() {

                @Override
                public boolean execute(FormItem item, Object value, DynamicForm form) {
                    return !StringUtils.isEmpty(form.getValueAsString(QuantityUnitDS.SYMBOL));
                }
            });

            generalEditionForm.setFields(uuid, title, symbol, symbolPosition);

            mainFormLayout.addEditionCanvas(generalEditionForm);
        }
        public void setQuantityUnitDto(QuantityUnitDto quantityUnitDto) {
            this.quantityUnitDto = quantityUnitDto;

            fillViewForm(quantityUnitDto);
            fillEditForm(quantityUnitDto);

            mainFormLayout.setViewMode();

            show();
        }

        private void fillViewForm(QuantityUnitDto dto) {
            generalForm.setValue(QuantityUnitDS.UUID, dto.getUuid());
            generalForm.setValue(QuantityUnitDS.SYMBOL, dto.getSymbol());
            if (dto.getSymbolPosition() != null) {
                generalForm.setValue(QuantityUnitDS.SYMBOL_POSITION, getCoreMessages().getString(getCoreMessages().quantityUnitSymbolPositionEnum() + dto.getSymbolPosition().getName()));
            } else {
                generalForm.setValue(QuantityUnitDS.SYMBOL_POSITION, (String) null);
            }
            generalForm.setValue(QuantityUnitDS.TITLE, org.siemac.metamac.web.common.client.utils.RecordUtils.getInternationalStringRecord(dto.getTitle()));
        }

        private void fillEditForm(QuantityUnitDto dto) {
            generalEditionForm.setValue(QuantityUnitDS.UUID, dto.getUuid());
            generalEditionForm.setValue(QuantityUnitDS.SYMBOL, dto.getSymbol());
            generalEditionForm.setValue(QuantityUnitDS.SYMBOL_POSITION, dto.getSymbolPosition() != null ? dto.getSymbolPosition().name() : null);
            generalEditionForm.setValue(QuantityUnitDS.TITLE, org.siemac.metamac.web.common.client.utils.RecordUtils.getInternationalStringRecord(dto.getTitle()));
        }

        private QuantityUnitDto getQuantityUnitDto() {
            MultiLanguageTextItem titleItem = (MultiLanguageTextItem) generalEditionForm.getItem(QuantityUnitDS.TITLE);
            quantityUnitDto.setTitle(titleItem.getValue());

            quantityUnitDto.setSymbol(generalEditionForm.getValueAsString(QuantityUnitDS.SYMBOL));
            if (generalEditionForm.getItem(QuantityUnitDS.SYMBOL_POSITION).isVisible()) {
                try {
                    String symbolPositionStr = generalEditionForm.getValueAsString(QuantityUnitDS.SYMBOL_POSITION);
                    quantityUnitDto.setSymbolPosition(QuantityUnitSymbolPositionEnum.valueOf(symbolPositionStr));
                } catch (Exception e) {
                    quantityUnitDto.setSymbolPosition(null);
                }
            } else {
                quantityUnitDto.setSymbolPosition(null);
            }

            return quantityUnitDto;
        }
    }
}
