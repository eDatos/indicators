package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.indicator.widgets.AskVersionWindow;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.widgets.SystemMainFormLayout;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemGeneralPanel extends VLayout {

    private SystemUiHandler        uiHandlers;

    private SystemMainFormLayout   mainFormLayout;

    /* VIEW FORM */
    private GroupDynamicForm       identifiersForm;
    private GroupDynamicForm       productionForm;
    private GroupDynamicForm       diffusionForm;
    private GroupDynamicForm       contentForm;
    private GroupDynamicForm       publicationForm;

    private IndicatorsSystemDtoWeb indicatorsSystemDto;

    public SystemGeneralPanel() {
        super();
        mainFormLayout = new SystemMainFormLayout();

        // Show/Hide Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Life Cycle
        mainFormLayout.getProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToProductionValidation(indicatorsSystemDto);
            }
        });
        mainFormLayout.getDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToDiffusionValidation(indicatorsSystemDto);
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(indicatorsSystemDto);
            }
        });
        mainFormLayout.getPublish().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publish(indicatorsSystemDto);
            }
        });
        mainFormLayout.getArchive().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.archive(indicatorsSystemDto);
            }
        });
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final AskVersionWindow versionWindow = new AskVersionWindow(getConstants().indicatorVersionType());
                versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (versionWindow.validateForm()) {
                            uiHandlers.versioningIndicatorsSystem(indicatorsSystemDto, versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });

        createViewForm();
        this.addMember(mainFormLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {

        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().systemDetailIdentifiers());
        ViewTextItem versionField = new ViewTextItem(IndicatorsSystemsDS.VERSION, getConstants().systemDetailVersion());
        ViewTextItem codeField = new ViewTextItem(IndicatorsSystemsDS.CODE, getConstants().systemDetailIdentifier());
        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(IndicatorsSystemsDS.TITLE, getConstants().systemDetailTitle());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(IndicatorsSystemsDS.ACRONYM, getConstants().systemDetailAcronym());
        ViewTextItem procStatus = new ViewTextItem(IndicatorsSystemsDS.PROC_STATUS, getConstants().systemDetailProcStatus());
        identifiersForm.setFields(codeField, versionField, title, acronym, procStatus);

        // Production Descriptors
        productionForm = new GroupDynamicForm(getConstants().systemDetailProductionDescriptors());
        ViewTextItem prodVersion = new ViewTextItem(IndicatorsSystemsDS.PROD_VERSION, getConstants().systemDetailProdVersion());
        ViewTextItem prodValidDate = new ViewTextItem(IndicatorsSystemsDS.PROD_VALID_DATE, getConstants().systemDetailProdValidDate());
        ViewTextItem prodValidUser = new ViewTextItem(IndicatorsSystemsDS.PROD_VALID_USER, getConstants().systemDetailProdValidUser());
        productionForm.setFields(prodVersion, prodValidDate, prodValidUser);

        // Diffusion Descriptors
        diffusionForm = new GroupDynamicForm(getConstants().systemDetailDiffusionDescriptors());
        ViewTextItem diffValidDate = new ViewTextItem(IndicatorsSystemsDS.DIFF_VALID_DATE, getConstants().systemDetailDiffValidDate());
        ViewTextItem diffValidUser = new ViewTextItem(IndicatorsSystemsDS.DIFF_VALID_USER, getConstants().systemDetailDiffValidUser());
        diffusionForm.setFields(diffValidDate, diffValidUser);

        // Content Descriptors
        contentForm = new GroupDynamicForm(getConstants().systemDetailContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(IndicatorsSystemsDS.DESCRIPTION, getConstants().systemDetailDescription());
        ViewMultiLanguageTextItem objective = new ViewMultiLanguageTextItem(IndicatorsSystemsDS.OBJECTIVE, getConstants().systemDetailObjective());
        contentForm.setFields(description, objective);

        // Publication Descriptors
        publicationForm = new GroupDynamicForm(getConstants().systemDetailPublicationDescriptors());
        ViewTextItem publicationVersion = new ViewTextItem(IndicatorsSystemsDS.PUBL_VERSION, getConstants().systemPublicationVersion());
        ViewTextItem publicationDate = new ViewTextItem(IndicatorsSystemsDS.PUBL_DATE, getConstants().systemDetailPublicationDate());
        ViewTextItem publicationUser = new ViewTextItem(IndicatorsSystemsDS.PUBL_USER, getConstants().systemDetailPublicationUser());
        ViewTextItem archiveVersion = new ViewTextItem(IndicatorsSystemsDS.ARCH_VERSION, getConstants().systemArchivedVersion());
        ViewTextItem archiveDate = new ViewTextItem(IndicatorsSystemsDS.ARCH_DATE, getConstants().systemDetailArchiveDate());
        ViewTextItem archiveUser = new ViewTextItem(IndicatorsSystemsDS.ARCH_USER, getConstants().systemDetailArchiveUser());
        publicationForm.setFields(publicationVersion, publicationDate, publicationUser, archiveVersion, archiveDate, archiveUser);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(productionForm);
        mainFormLayout.addViewCanvas(diffusionForm);
        mainFormLayout.addViewCanvas(contentForm);
        mainFormLayout.addViewCanvas(publicationForm);
    }

    public void setIndicatorsSystem(IndicatorsSystemDtoWeb indicatorSystemDto) {
        this.indicatorsSystemDto = indicatorSystemDto;
        mainFormLayout.setIndicatorsSytemCode(indicatorSystemDto.getCode());
        mainFormLayout.updatePublishSection(indicatorSystemDto.getProcStatus());

        // Identifiers
        identifiersForm.setValue(IndicatorsSystemsDS.VERSION, indicatorSystemDto.getVersionNumber());
        identifiersForm.setValue(IndicatorsSystemsDS.CODE, indicatorSystemDto.getCode());
        identifiersForm.setValue(IndicatorsSystemsDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorSystemDto.getTitle()));
        identifiersForm.setValue(IndicatorsSystemsDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorSystemDto.getAcronym()));
        identifiersForm.setValue(IndicatorsSystemsDS.PROC_STATUS, CommonUtils.getIndicatorSystemProcStatus(indicatorSystemDto));

        // Production Descriptors
        productionForm.setValue(IndicatorsSystemsDS.PROD_VERSION, indicatorSystemDto.getProductionVersion());
        productionForm.setValue(IndicatorsSystemsDS.PROD_VALID_DATE, DateUtils.getFormattedDate(indicatorSystemDto.getProductionValidationDate()));
        productionForm.setValue(IndicatorsSystemsDS.PROD_VALID_USER, indicatorSystemDto.getProductionValidationUser());

        // Diffusion Descriptors
        diffusionForm.setValue(IndicatorsSystemsDS.DIFF_VALID_DATE, DateUtils.getFormattedDate(indicatorSystemDto.getDiffusionValidationDate()));
        diffusionForm.setValue(IndicatorsSystemsDS.DIFF_VALID_USER, indicatorSystemDto.getDiffusionValidationUser());

        // Content Descriptors
        contentForm.setValue(IndicatorsSystemsDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(indicatorSystemDto.getDescription()));
        contentForm.setValue(IndicatorsSystemsDS.OBJECTIVE, RecordUtils.getInternationalStringRecord(indicatorSystemDto.getObjective()));

        // Publication Descriptors
        publicationForm.setValue(IndicatorsSystemsDS.PUBL_VERSION, indicatorSystemDto.getPublishedVersion());
        publicationForm.setValue(IndicatorsSystemsDS.PUBL_DATE, DateUtils.getFormattedDate(indicatorSystemDto.getPublicationDate()));
        publicationForm.setValue(IndicatorsSystemsDS.PUBL_USER, indicatorSystemDto.getPublicationUser());
        publicationForm.setValue(IndicatorsSystemsDS.ARCH_VERSION, indicatorSystemDto.getArchivedVersion());
        publicationForm.setValue(IndicatorsSystemsDS.ARCH_DATE, DateUtils.getFormattedDate(indicatorSystemDto.getArchiveDate()));
        publicationForm.setValue(IndicatorsSystemsDS.ARCH_USER, indicatorSystemDto.getArchiveUser());

        mainFormLayout.setViewMode();
    }

    public void setUiHandlers(SystemUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
