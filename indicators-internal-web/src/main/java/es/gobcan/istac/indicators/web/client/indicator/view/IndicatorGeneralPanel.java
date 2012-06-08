package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalViewMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaAndUrlItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextAndUrlItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.indicator.widgets.AskVersionWindow;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.widgets.IndicatorMainFormLayout;
import es.gobcan.istac.indicators.web.client.widgets.QuantityForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewQuantityForm;

public class IndicatorGeneralPanel extends VLayout {

    /* Data */
    private IndicatorDto                    indicator;

    /* UiHandlers */
    private IndicatorUiHandler              uiHandlers;

    private IndicatorMainFormLayout         mainFormLayout;

    private InternationalViewMainFormLayout diffusionMainFormLayout;
    private GroupDynamicForm                diffusionIdentifiersForm;

    /* View Form */
    private GroupDynamicForm                identifiersForm;
    private GroupDynamicForm                contentClassifiersForm;
    private GroupDynamicForm                contentDescriptorsForm;
    private GroupDynamicForm                productionDescriptorsForm;
    private ViewQuantityForm                quantityForm;
    private GroupDynamicForm                diffusionDescriptorsForm;
    private GroupDynamicForm                publicationDescriptorsForm;
    private GroupDynamicForm                annotationsForm;

    /* Edit Form */
    private GroupDynamicForm                identifiersEditionForm;
    private GroupDynamicForm                contentClassifiersEditionForm;
    private GroupDynamicForm                contentDescriptorsEditionForm;
    private GroupDynamicForm                productionDescriptorsEditionForm;
    private QuantityForm                    quantityEditionForm;
    private GroupDynamicForm                diffusionDescriptorsEditionForm;
    private GroupDynamicForm                publicationDescriptorsEditionForm;
    private GroupDynamicForm                annotationsEditionForm;

    private List<SubjectDto>                subjectDtos;

    public IndicatorGeneralPanel() {
        super();

        // ........................
        // PRODUCTION ENVIRONMENT
        // ........................

        mainFormLayout = new IndicatorMainFormLayout(ClientSecurityUtils.canEditIndicator());
        mainFormLayout.setTitleLabelContents(getConstants().indicatorProductionEnvironment());

        createViewForm();
        createEditionForm();

        this.addMember(mainFormLayout);
        bindEvents();

        // ......................
        // DIFFUSION ENVIRONMENT
        // ......................

        diffusionMainFormLayout = new InternationalViewMainFormLayout();
        diffusionMainFormLayout.setTitleLabelContents(getConstants().indicatorDiffusionEnvironment());
        diffusionMainFormLayout.setVisibility(Visibility.HIDDEN);
        diffusionMainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = diffusionMainFormLayout.getTranslateToolStripButton().isSelected();
                diffusionIdentifiersForm.setTranslationsShowed(translationsShowed);
            }
        });

        createDiffusionViewForm();
        this.addMember(diffusionMainFormLayout);

    }

    private void bindEvents() {
        // Show/Hide Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);
                contentClassifiersForm.setTranslationsShowed(translationsShowed);
                contentClassifiersEditionForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
                quantityForm.setTranslationsShowed(translationsShowed);
                quantityEditionForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
                publicationDescriptorsForm.setTranslationsShowed(translationsShowed);
                publicationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
                annotationsForm.setTranslationsShowed(translationsShowed);
                annotationsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check indicator status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (IndicatorProcStatusEnum.PUBLISHED.equals(indicator.getProcStatus()) || IndicatorProcStatusEnum.ARCHIVED.equals(indicator.getProcStatus())) {
                    // Create a new version of the indicator
                    final InformationWindow window = new InformationWindow(getMessages().indicatorEditionInfo(), getMessages().indicatorEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    setEditionMode();
                }
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                saveIndicator();
            }
        });

        // Life Cycle
        mainFormLayout.getProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToProductionValidation(indicator.getUuid());
            }
        });
        mainFormLayout.getDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToDiffusionValidation(indicator.getUuid());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(indicator);
            }
        });
        mainFormLayout.getPublish().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publish(indicator.getUuid());
            }
        });
        mainFormLayout.getArchive().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.archive(indicator.getUuid());
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
                            uiHandlers.versioningIndicator(indicator.getUuid(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });

        // Populate data
        mainFormLayout.getPopulateData().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.populateData(indicator.getUuid(), indicator.getVersionNumber());
            }
        });

    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
        ViewTextItem code = new ViewTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        ViewTextItem uuid = new ViewTextItem(IndicatorDS.UUID, getConstants().indicDetailUuid());
        ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(IndicatorDS.ACRONYM, getConstants().indicDetailAcronym());
        ViewTextItem dataRepositoryTableName = new ViewTextItem(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, getConstants().indicatorDataTableName());
        ViewTextItem needsUpdate = new ViewTextItem(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        identifiersForm.setFields(code, uuid, version, procStatus, title, acronym, dataRepositoryTableName, needsUpdate);

        // Content Classifiers Form
        contentClassifiersForm = new GroupDynamicForm(getConstants().indicDetailContentClassifiers());
        ViewTextItem subjectCode = new ViewTextItem(IndicatorDS.SUBJECT_CODE, getConstants().indicDetailSubjectCode());
        ViewMultiLanguageTextItem subjectTitle = new ViewMultiLanguageTextItem(IndicatorDS.SUBJECT_TITLE, getConstants().indicDetailSubjectTitle());
        contentClassifiersForm.setFields(subjectCode, subjectTitle);

        // Content Descriptors Form
        contentDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailContentDescriptors());
        ViewMultiLanguageTextItem conceptDescription = new ViewMultiLanguageTextItem(IndicatorDS.CONCEPT_DESCRIPTION, getConstants().indicDetailConceptDescription());
        contentDescriptorsForm.setFields(conceptDescription);

        // Production Descriptors Form
        productionDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailProductionDescriptors());
        ViewTextItem prodVersion = new ViewTextItem(IndicatorDS.PRODUCTION_VERSION, getConstants().indicDetailProductionVersion());
        ViewTextItem prodValDate = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_DATE, getConstants().indicDetailProductionValidationDate());
        ViewTextItem prodValUser = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_USER, getConstants().indicDetailProductionValidationUser());
        productionDescriptorsForm.setFields(prodVersion, prodValDate, prodValUser);

        // Quantity Form
        quantityForm = new ViewQuantityForm(getConstants().indicDetailQuantity());

        // Diffusion Descriptors Form
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailDiffusionDescriptors());
        ViewTextItem diffValDate = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_DATE, getConstants().indicDetailDiffusionValidationDate());
        ViewTextItem diffValUser = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_USER, getConstants().indicDetailDiffusionValidationUser());
        diffusionDescriptorsForm.setFields(diffValDate, diffValUser);

        // Publication Descriptors Form
        publicationDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailPublicationDescriptors());
        ViewTextItem pubVersion = new ViewTextItem(IndicatorDS.PUBLICATION_VERSION, getConstants().indicatorPublicationVersion());
        ViewTextItem pubFailedDate = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_DATE, getConstants().indicDetailPublicationFailedDate());
        ViewTextItem pubFailedUser = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_USER, getConstants().indicDetailPublicationFailedUser());
        ViewTextItem pubDate = new ViewTextItem(IndicatorDS.PUBLICATION_DATE, getConstants().indicDetailPublicationDate());
        ViewTextItem pubUser = new ViewTextItem(IndicatorDS.PUBLICATION_USER, getConstants().indicDetailPublicationUser());
        ViewTextItem archVersion = new ViewTextItem(IndicatorDS.ARCHIVED_VERSION, getConstants().indicatorArchivedVersion());
        ViewTextItem archDate = new ViewTextItem(IndicatorDS.ARCHIVED_DATE, getConstants().indicDetailArchivedDate());
        ViewTextItem archUser = new ViewTextItem(IndicatorDS.ARCHIVED_USER, getConstants().indicDetailArchivedUser());
        publicationDescriptorsForm.setFields(pubVersion, pubFailedDate, pubFailedUser, pubDate, pubUser, archVersion, archDate, archUser);

        // Annotations Form
        annotationsForm = new GroupDynamicForm(getConstants().indicDetailAnnotations());
        ViewMultiLanguageTextAndUrlItem notes = new ViewMultiLanguageTextAndUrlItem(IndicatorDS.NOTES, getConstants().indicDetailNotes());
        ViewMultiLanguageTextAndUrlItem comments = new ViewMultiLanguageTextAndUrlItem(IndicatorDS.COMMENTS, getConstants().indicDetailComments());
        annotationsForm.setFields(notes, comments);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentClassifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(quantityForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(publicationDescriptorsForm);
        mainFormLayout.addViewCanvas(annotationsForm);
    }

    private void createDiffusionViewForm() {
        // Identifiers Form
        diffusionIdentifiersForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
        ViewTextItem code = new ViewTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        ViewTextItem uuid = new ViewTextItem(IndicatorDS.UUID, getConstants().indicDetailUuid());
        ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(IndicatorDS.ACRONYM, getConstants().indicDetailAcronym());
        ViewTextItem dataRepositoryTableName = new ViewTextItem(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, getConstants().indicatorDataTableName());
        ViewTextItem needsUpdate = new ViewTextItem(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        diffusionIdentifiersForm.setFields(code, uuid, version, procStatus, title, acronym, dataRepositoryTableName, needsUpdate);

        diffusionMainFormLayout.addViewCanvas(diffusionIdentifiersForm);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
        ViewTextItem code = new ViewTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        ViewTextItem uuid = new ViewTextItem(IndicatorDS.UUID, getConstants().indicDetailUuid());
        ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        MultiLanguageTextItem title = new MultiLanguageTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        title.setRequired(true);
        MultiLanguageTextItem acronym = new MultiLanguageTextItem(IndicatorDS.ACRONYM, getConstants().indicDetailAcronym());
        ViewTextItem dataRepositoryTableName = new ViewTextItem(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, getConstants().indicatorDataTableName());
        ViewTextItem needsUpdate = new ViewTextItem(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        identifiersEditionForm.setFields(code, uuid, version, procStatus, title, acronym, dataRepositoryTableName, needsUpdate);

        // Status Form
        contentClassifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailContentClassifiers());
        RequiredSelectItem subject = new RequiredSelectItem(IndicatorDS.SUBJECT, getConstants().indicDetailSubject());
        contentClassifiersEditionForm.setFields(subject);

        // Content Descriptors Form
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailContentDescriptors());
        MultiLanguageTextItem conceptDescription = new MultiLanguageTextItem(IndicatorDS.CONCEPT_DESCRIPTION, getConstants().indicDetailConceptDescription());

        contentDescriptorsEditionForm.setFields(conceptDescription);

        // Production Descriptors Form
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailProductionDescriptors());
        ViewTextItem prodVersion = new ViewTextItem(IndicatorDS.PRODUCTION_VERSION, getConstants().indicDetailProductionVersion());
        ViewTextItem prodValDate = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_DATE, getConstants().indicDetailProductionValidationDate());
        ViewTextItem prodValUser = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_USER, getConstants().indicDetailProductionValidationUser());
        productionDescriptorsEditionForm.setFields(prodVersion, prodValDate, prodValUser);

        // Quantity Form
        quantityEditionForm = new QuantityForm(getConstants().indicDetailQuantity());

        // Diffusion Descriptors Form
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailDiffusionDescriptors());
        ViewTextItem diffValDate = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_DATE, getConstants().indicDetailDiffusionValidationDate());
        ViewTextItem diffValUser = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_USER, getConstants().indicDetailDiffusionValidationUser());
        diffusionDescriptorsEditionForm.setFields(diffValDate, diffValUser);

        // Publication Descriptors
        publicationDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailPublicationDescriptors());
        ViewTextItem pubVersion = new ViewTextItem(IndicatorDS.PUBLICATION_VERSION, getConstants().indicatorPublicationVersion());
        ViewTextItem pubFailedDate = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_DATE, getConstants().indicDetailPublicationFailedDate());
        ViewTextItem pubFailedUser = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_USER, getConstants().indicDetailPublicationFailedUser());
        ViewTextItem pubDate = new ViewTextItem(IndicatorDS.PUBLICATION_DATE, getConstants().indicDetailPublicationDate());
        ViewTextItem pubUser = new ViewTextItem(IndicatorDS.PUBLICATION_USER, getConstants().indicDetailPublicationUser());
        ViewTextItem archVersion = new ViewTextItem(IndicatorDS.ARCHIVED_VERSION, getConstants().indicatorArchivedVersion());
        ViewTextItem archDate = new ViewTextItem(IndicatorDS.ARCHIVED_DATE, getConstants().indicDetailArchivedDate());
        ViewTextItem archUser = new ViewTextItem(IndicatorDS.ARCHIVED_USER, getConstants().indicDetailArchivedUser());
        publicationDescriptorsEditionForm.setFields(pubVersion, pubFailedDate, pubFailedUser, pubDate, pubUser, archVersion, archDate, archUser);

        // Annotations Form
        annotationsEditionForm = new GroupDynamicForm(getConstants().indicDetailAnnotations());
        MultiLanguageTextAreaAndUrlItem notes = new MultiLanguageTextAreaAndUrlItem(IndicatorDS.NOTES, getConstants().indicDetailNotes());
        MultiLanguageTextAreaAndUrlItem comments = new MultiLanguageTextAreaAndUrlItem(IndicatorDS.COMMENTS, getConstants().indicDetailComments());
        annotationsEditionForm.setFields(notes, comments);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentClassifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(quantityEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(publicationDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionForm);
    }

    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicator = indicatorDto;

        // DIFFUSION ENVIRONMENT

        // Load indicator in diffusion version (if exists)
        diffusionMainFormLayout.hide();
        String diffusionVersion = null;
        if (indicatorDto.getPublishedVersion() != null) {
            diffusionVersion = indicatorDto.getPublishedVersion();
        } else if (indicatorDto.getArchivedVersion() != null) {
            diffusionVersion = indicatorDto.getArchivedVersion();
        }
        if (diffusionVersion != null && !diffusionVersion.equals(indicatorDto.getVersionNumber())) {
            // There is a previous version published or archived
            uiHandlers.retrieveDiffusionIndicator(indicatorDto.getCode(), diffusionVersion);
        } else {
            // If there is no previous version but procStatus is published or archived, force to show production and diffusion environment form with the same indicators data
            if (IndicatorProcStatusEnum.PUBLISHED.equals(indicatorDto.getProcStatus()) || IndicatorProcStatusEnum.ARCHIVED.equals(indicatorDto.getProcStatus())) {
                setDiffusionIndicator(indicatorDto);
            }
        }

        // PRODUCTION ENVIRONMENT

        mainFormLayout.updatePublishSection(indicatorDto.getProcStatus());
        mainFormLayout.setViewMode();

        setIndicatorViewMode(indicatorDto);
        setIndicatorEditionMode(indicatorDto);

        // Clear errors
        identifiersEditionForm.clearErrors(true);
    }

    public void setDiffusionIndicator(IndicatorDto indicatorDto) {
        diffusionIdentifiersForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
        diffusionIdentifiersForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
        diffusionIdentifiersForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
        diffusionIdentifiersForm.setValue(IndicatorDS.PROC_STATUS, CommonUtils.getIndicatorProcStatus(indicatorDto));
        diffusionIdentifiersForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getTitle()));
        diffusionIdentifiersForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
        diffusionIdentifiersForm.setValue(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, indicatorDto.getDataRepositoryTableName());
        diffusionIdentifiersForm.getItem(IndicatorDS.NEEDS_UPDATE).setIcons(getNeedsUpdateIcon(indicatorDto.getNeedsUpdate()));

        diffusionMainFormLayout.show();
    }

    private void setIndicatorViewMode(IndicatorDto indicatorDto) {
        // Identifiers Form
        identifiersForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
        identifiersForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
        identifiersForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
        identifiersForm.setValue(IndicatorDS.PROC_STATUS, CommonUtils.getIndicatorProcStatus(indicatorDto));
        identifiersForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getTitle()));
        identifiersForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
        identifiersForm.setValue(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, indicatorDto.getDataRepositoryTableName());
        identifiersForm.getItem(IndicatorDS.NEEDS_UPDATE).setIcons(getNeedsUpdateIcon(indicatorDto.getNeedsUpdate()));

        // Content Classifiers
        contentClassifiersForm.setValue(IndicatorDS.SUBJECT_CODE, indicatorDto.getSubjectCode());
        contentClassifiersForm.setValue(IndicatorDS.SUBJECT_TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getSubjectTitle()));

        // Content Descriptors
        contentDescriptorsForm.setValue(IndicatorDS.CONCEPT_DESCRIPTION, RecordUtils.getInternationalStringRecord(indicatorDto.getConceptDescription()));

        // Production Descriptors
        productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VERSION, indicatorDto.getProductionVersion());
        productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_DATE, DateUtils.getFormattedDate(indicatorDto.getProductionValidationDate()));
        productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_USER, indicatorDto.getProductionValidationUser());

        // Quantity
        quantityForm.setValue(indicatorDto.getQuantity());

        // Diffusion Descriptors
        diffusionDescriptorsForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(indicatorDto.getDiffusionValidationDate()));
        diffusionDescriptorsForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_USER, indicatorDto.getDiffusionValidationUser());

        // Publication Descriptors
        publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_VERSION, indicatorDto.getPublishedVersion());
        publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(indicatorDto.getPublicationFailedDate()));
        publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_FAILED_USER, indicatorDto.getPublicationFailedUser());
        publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_DATE, DateUtils.getFormattedDate(indicatorDto.getPublicationDate()));
        publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_USER, indicatorDto.getPublicationUser());
        publicationDescriptorsForm.setValue(IndicatorDS.ARCHIVED_VERSION, indicatorDto.getArchivedVersion());
        publicationDescriptorsForm.setValue(IndicatorDS.ARCHIVED_DATE, DateUtils.getFormattedDate(indicatorDto.getArchiveDate()));
        publicationDescriptorsForm.setValue(IndicatorDS.ARCHIVED_USER, indicatorDto.getArchiveUser());

        // Annotations
        ((ViewMultiLanguageTextAndUrlItem) annotationsForm.getItem(IndicatorDS.NOTES)).setValue(indicatorDto.getNotes(), indicatorDto.getNotesUrl());
        ((ViewMultiLanguageTextAndUrlItem) annotationsForm.getItem(IndicatorDS.COMMENTS)).setValue(indicatorDto.getComments(), indicatorDto.getCommentsUrl());
    }

    private void setIndicatorEditionMode(IndicatorDto indicatorDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
        identifiersEditionForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
        identifiersEditionForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
        identifiersEditionForm.setValue(IndicatorDS.PROC_STATUS, CommonUtils.getIndicatorProcStatus(indicatorDto));
        identifiersEditionForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getTitle()));
        identifiersEditionForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
        identifiersEditionForm.setValue(IndicatorDS.DATA_REPOSITORY_TABLE_NAME, indicatorDto.getDataRepositoryTableName());
        identifiersEditionForm.getItem(IndicatorDS.NEEDS_UPDATE).setIcons(getNeedsUpdateIcon(indicatorDto.getNeedsUpdate()));

        // Content Classifiers
        contentClassifiersEditionForm.setValue(IndicatorDS.SUBJECT, indicatorDto.getSubjectCode());

        // Content Descriptors
        contentDescriptorsEditionForm.setValue(IndicatorDS.CONCEPT_DESCRIPTION, RecordUtils.getInternationalStringRecord(indicatorDto.getConceptDescription()));

        // Production Descriptors
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VERSION, indicatorDto.getProductionVersion());
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_DATE, DateUtils.getFormattedDate(indicatorDto.getProductionValidationDate()));
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_USER, indicatorDto.getProductionValidationUser());

        // Quantity
        quantityEditionForm.setIndicator(indicatorDto);
        quantityEditionForm.setValue(indicatorDto.getQuantity());

        // Diffusion Descriptors
        diffusionDescriptorsEditionForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(indicatorDto.getDiffusionValidationDate()));
        diffusionDescriptorsEditionForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_USER, indicatorDto.getDiffusionValidationUser());

        // Publication Descriptors
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_VERSION, indicatorDto.getPublishedVersion());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(indicatorDto.getPublicationFailedDate()));
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_FAILED_USER, indicatorDto.getPublicationFailedUser());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_DATE, DateUtils.getFormattedDate(indicatorDto.getPublicationDate()));
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_USER, indicatorDto.getPublicationUser());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.ARCHIVED_VERSION, indicatorDto.getArchivedVersion());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.ARCHIVED_DATE, DateUtils.getFormattedDate(indicatorDto.getArchiveDate()));
        publicationDescriptorsEditionForm.setValue(IndicatorDS.ARCHIVED_USER, indicatorDto.getArchiveUser());

        // Annotations
        ((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.NOTES)).setValue(indicatorDto.getNotes(), indicatorDto.getNotesUrl());
        ((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).setValue(indicatorDto.getComments(), indicatorDto.getCommentsUrl());

    }

    private void saveIndicator() {
        if (identifiersEditionForm.validate(false) && contentClassifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && quantityEditionForm.validate(false)) {
            // Identifiers
            indicator.setTitle((InternationalStringDto) identifiersEditionForm.getValue(IndicatorDS.TITLE));
            indicator.setAcronym((InternationalStringDto) identifiersEditionForm.getValue(IndicatorDS.ACRONYM));
            // Content Classifiers
            indicator.setSubjectCode(contentClassifiersEditionForm.getValueAsString(IndicatorDS.SUBJECT));
            indicator.setSubjectTitle(CommonUtils.getSubjectTitleFromCode(subjectDtos, contentClassifiersEditionForm.getValueAsString(IndicatorDS.SUBJECT)));
            // Content Descriptors
            indicator.setConceptDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(IndicatorDS.CONCEPT_DESCRIPTION));
            // Quantity
            indicator.setQuantity(quantityEditionForm.getValue());
            // Annotations
            indicator.setNotes(((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.NOTES)).getTextValue());
            indicator.setNotesUrl(((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.NOTES)).getUrlValue());
            indicator.setComments(((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).getTextValue());
            indicator.setCommentsUrl(((MultiLanguageTextAreaAndUrlItem) annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).getUrlValue());

            uiHandlers.saveIndicator(indicator);
        }
    }

    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
        quantityForm.setUiHandlers(uiHandlers);
        quantityEditionForm.setUiHandlers(uiHandlers);
    }

    public void setQuantityUnits(List<QuantityUnitDto> units) {
        quantityForm.setQuantityUnits(units);
        quantityEditionForm.setQuantityUnits(units);
    }

    public void setIndicatorListQuantityDenominator(List<IndicatorSummaryDto> indicators) {
        quantityEditionForm.setIndicatorListQuantityDenominator(indicators);
    }

    public void setIndicatorListQuantityNumerator(List<IndicatorSummaryDto> indicators) {
        quantityEditionForm.setIndicatorListQuantityNumerator(indicators);
    }

    public void setIndicatorListQuantityIndicatorBase(List<IndicatorSummaryDto> indicators) {
        quantityEditionForm.setIndicatorListQuantityIndicatorBase(indicators);
    }

    public void setSubjectsList(List<SubjectDto> subjectDtos) {
        this.subjectDtos = subjectDtos;
        LinkedHashMap<String, String> valueMap = CommonUtils.getSubjectsValueMap(subjectDtos);
        ((SelectItem) contentClassifiersEditionForm.getItem(IndicatorDS.SUBJECT)).setValueMap(valueMap);
    }

    public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
        quantityEditionForm.setUnitMultipliers(unitMultiplierDtos);
    }

    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        quantityEditionForm.setGeographicalGranularities(geographicalGranularityDtos);
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        quantityEditionForm.setGeographicalValues(geographicalValueDtos);
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        quantityForm.setGeographicalValue(geographicalValueDto);
        quantityEditionForm.setGeographicalValue(geographicalValueDto);
    }

    private void setEditionMode() {
        uiHandlers.retrieveSubjects();
        mainFormLayout.setEditionMode();
    }

    public void setIndicatorQuantityDenominator(IndicatorDto indicator) {
        quantityForm.setIndicatorQuantityDenominator(indicator);
        quantityEditionForm.setIndicatorQuantityDenominator(indicator);
    }

    public void setIndicatorQuantityNumerator(IndicatorDto indicator) {
        quantityForm.setIndicatorQuantityNumerator(indicator);
        quantityEditionForm.setIndicatorQuantityNumerator(indicator);
    }

    public void setIndicatorQuantityIndicatorBase(IndicatorDto indicator) {
        quantityForm.setIndicatorQuantityIndicatorBase(indicator);
        quantityEditionForm.setIndicatorQuantityIndicatorBase(indicator);
    }

    private FormItemIcon getNeedsUpdateIcon(Boolean needsUpdate) {
        FormItemIcon icon = new FormItemIcon();
        icon.setSrc(needsUpdate ? GlobalResources.RESOURCE.errorSmart().getURL() : GlobalResources.RESOURCE.success().getURL());
        return icon;
    }

}
