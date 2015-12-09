package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.ARCHIVED_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.ARCHIVED_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.ARCHIVED_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.ARCHIVED_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.CODE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.CREATION_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.CREATION_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.CREATION_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.CREATION_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.DIFFUSION_VALIDATION_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.DIFFUSION_VALIDATION_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.DIFFUSION_VALIDATION_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.DIFFUSION_VALIDATION_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.NEEDS_UPDATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.NEEDS_UPDATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PROC_STATUS;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PROC_STATUS_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PRODUCTION_VALIDATION_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PRODUCTION_VALIDATION_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PRODUCTION_VALIDATION_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PRODUCTION_VALIDATION_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_FAILED_DATE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_FAILED_DATE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_FAILED_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_FAILED_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_USER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.PUBLICATION_USER_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.SUBJECT_TITLE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.SUBJECT_TITLE_DIFF;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.SUBJECT_TITLE_PROD;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.TITLE;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.VERSION_NUMBER;
import static es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS.VERSION_NUMBER_DIFF;

import org.siemac.metamac.web.common.client.widgets.CustomListGrid;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridField;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class IndicatorListGrid extends CustomListGrid {

    public IndicatorListGrid() {
        setAutoFitData(Autofit.VERTICAL);
        setAutoFitMaxRecords(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
        setDataSource(new IndicatorDS());
        setUseAllDataSourceFields(false);
        setHeaderHeight(40);

        ListGridField code = new ListGridField(CODE, getConstants().indicListHeaderIdentifier());
        code.setAlign(Alignment.LEFT);
        ListGridField name = new ListGridField(TITLE, getConstants().indicDetailTitle());
        ListGridField subject = new ListGridField(SUBJECT_TITLE, getConstants().indicDetailSubject());

        ListGridField version = new ListGridField(VERSION_NUMBER, getConstants().indicDetailVersion());
        ListGridField status = new ListGridField(PROC_STATUS, getConstants().indicDetailProcStatus());
        ListGridField needsUpdate = new ListGridField(NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        needsUpdate.setWidth(140);
        needsUpdate.setType(ListGridFieldType.IMAGE);
        needsUpdate.setAlign(Alignment.CENTER);

        ListGridField subjectProd = new ListGridField(SUBJECT_TITLE_PROD, getConstants().indicDetailSubject());
        subjectProd.setHidden(true);
        ListGridField productionValidationDate = new ListGridField(PRODUCTION_VALIDATION_DATE, getConstants().indicDetailProductionValidationDate());
        productionValidationDate.setHidden(true);
        ListGridField productionValidationUser = new ListGridField(PRODUCTION_VALIDATION_USER, getConstants().indicDetailProductionValidationUser());
        productionValidationUser.setHidden(true);
        ListGridField diffusionValidationDate = new ListGridField(DIFFUSION_VALIDATION_DATE, getConstants().indicDetailDiffusionValidationDate());
        diffusionValidationDate.setHidden(true);
        ListGridField diffusionValidationUser = new ListGridField(DIFFUSION_VALIDATION_USER, getConstants().indicDetailDiffusionValidationUser());
        diffusionValidationUser.setHidden(true);
        ListGridField publicationDate = new ListGridField(PUBLICATION_DATE, getConstants().indicDetailPublicationDate());
        publicationDate.setHidden(true);
        ListGridField publicationUser = new ListGridField(PUBLICATION_USER, getConstants().indicDetailPublicationUser());
        publicationUser.setHidden(true);
        ListGridField publicationFailedDate = new ListGridField(PUBLICATION_FAILED_DATE, getConstants().indicDetailPublicationFailedDate());
        publicationFailedDate.setHidden(true);
        ListGridField publicationFailedUser = new ListGridField(PUBLICATION_FAILED_USER, getConstants().indicDetailPublicationFailedUser());
        publicationFailedUser.setHidden(true);
        ListGridField archivedDate = new ListGridField(ARCHIVED_DATE, getConstants().indicDetailArchivedDate());
        archivedDate.setHidden(true);
        ListGridField archivedUser = new ListGridField(ARCHIVED_USER, getConstants().indicDetailArchivedUser());
        archivedUser.setHidden(true);
        ListGridField creationDate = new ListGridField(CREATION_DATE, getConstants().indicDetailCreatedDate());
        creationDate.setHidden(true);
        ListGridField creationUser = new ListGridField(CREATION_USER, getConstants().indicDetailCreatedUser());
        creationUser.setHidden(true);

        ListGridField diffusionVersion = new ListGridField(VERSION_NUMBER_DIFF, getConstants().indicDetailVersion());
        ListGridField diffusionStatus = new ListGridField(PROC_STATUS_DIFF, getConstants().indicDetailProcStatus());
        ListGridField diffusionNeedsUpdate = new ListGridField(NEEDS_UPDATE_DIFF, getConstants().indicatorUpdateStatus());
        diffusionNeedsUpdate.setWidth(140);
        diffusionNeedsUpdate.setType(ListGridFieldType.IMAGE);
        diffusionNeedsUpdate.setAlign(Alignment.CENTER);

        ListGridField subjectDiff = new ListGridField(SUBJECT_TITLE_DIFF, getConstants().indicDetailSubject());
        subjectDiff.setHidden(true);
        ListGridField productionValidationDateDiff = new ListGridField(PRODUCTION_VALIDATION_DATE_DIFF, getConstants().indicDetailProductionValidationDate());
        productionValidationDateDiff.setHidden(true);
        ListGridField productionValidationUserDiff = new ListGridField(PRODUCTION_VALIDATION_USER_DIFF, getConstants().indicDetailProductionValidationUser());
        productionValidationUserDiff.setHidden(true);
        ListGridField diffusionValidationDateDiff = new ListGridField(DIFFUSION_VALIDATION_DATE_DIFF, getConstants().indicDetailDiffusionValidationDate());
        diffusionValidationDateDiff.setHidden(true);
        ListGridField diffusionValidationUserDiff = new ListGridField(DIFFUSION_VALIDATION_USER_DIFF, getConstants().indicDetailDiffusionValidationUser());
        diffusionValidationUserDiff.setHidden(true);
        ListGridField publicationDateDiff = new ListGridField(PUBLICATION_DATE_DIFF, getConstants().indicDetailPublicationDate());
        publicationDateDiff.setHidden(true);
        ListGridField publicationUserDiff = new ListGridField(PUBLICATION_USER_DIFF, getConstants().indicDetailPublicationUser());
        publicationUserDiff.setHidden(true);
        ListGridField publicationFailedDateDiff = new ListGridField(PUBLICATION_FAILED_DATE_DIFF, getConstants().indicDetailPublicationFailedDate());
        publicationFailedDateDiff.setHidden(true);
        ListGridField publicationFailedUserDiff = new ListGridField(PUBLICATION_FAILED_USER_DIFF, getConstants().indicDetailPublicationFailedUser());
        publicationFailedUserDiff.setHidden(true);
        ListGridField archivedDateDiff = new ListGridField(ARCHIVED_DATE_DIFF, getConstants().indicDetailArchivedDate());
        archivedDateDiff.setHidden(true);
        ListGridField archivedUserDiff = new ListGridField(ARCHIVED_USER_DIFF, getConstants().indicDetailArchivedUser());
        archivedUserDiff.setHidden(true);
        ListGridField creationDateDiff = new ListGridField(CREATION_DATE_DIFF, getConstants().indicDetailCreatedDate());
        creationDateDiff.setHidden(true);
        ListGridField creationUserDiff = new ListGridField(CREATION_USER_DIFF, getConstants().indicDetailCreatedUser());
        creationUserDiff.setHidden(true);

        setFields(code, name, subject, version, status, needsUpdate, subjectProd, productionValidationDate, productionValidationUser, diffusionValidationDate, diffusionValidationUser,
                publicationDate, publicationUser, publicationFailedDate, publicationFailedUser, archivedDate, archivedUser, creationDate, creationUser, diffusionVersion, diffusionStatus,
                diffusionNeedsUpdate, subjectDiff, productionValidationDateDiff, productionValidationUserDiff, diffusionValidationDateDiff, diffusionValidationUserDiff, publicationDateDiff,
                publicationUserDiff, publicationFailedDateDiff, publicationFailedUserDiff, archivedDateDiff, archivedUserDiff, creationDateDiff, creationUserDiff);

        // @formatter:off
        setHeaderSpans(new HeaderSpan(getConstants().indicator(), new String[]{CODE, TITLE, SUBJECT_TITLE}), 
                new HeaderSpan(getConstants().indicatorProductionEnvironment(), new String[]{VERSION_NUMBER, 
                                                                                            PROC_STATUS, 
                                                                                            NEEDS_UPDATE,
                                                                                            SUBJECT_TITLE_PROD,
                                                                                            PRODUCTION_VALIDATION_DATE,
                                                                                            PRODUCTION_VALIDATION_USER,
                                                                                            DIFFUSION_VALIDATION_DATE,
                                                                                            DIFFUSION_VALIDATION_USER,
                                                                                            PUBLICATION_DATE,
                                                                                            PUBLICATION_USER,
                                                                                            PUBLICATION_FAILED_DATE,
                                                                                            PUBLICATION_FAILED_USER,
                                                                                            ARCHIVED_DATE,
                                                                                            ARCHIVED_USER,
                                                                                            CREATION_DATE,
                                                                                            CREATION_USER
                                                                                            }),

                new HeaderSpan(getConstants().indicatorDiffusionEnvironment(), new String[]{VERSION_NUMBER_DIFF, 
                                                                                            PROC_STATUS_DIFF, 
                                                                                            NEEDS_UPDATE_DIFF,
                                                                                            SUBJECT_TITLE_DIFF,
                                                                                            PRODUCTION_VALIDATION_DATE_DIFF,
                                                                                            PRODUCTION_VALIDATION_USER_DIFF,
                                                                                            DIFFUSION_VALIDATION_DATE_DIFF,
                                                                                            DIFFUSION_VALIDATION_USER_DIFF,
                                                                                            PUBLICATION_DATE_DIFF,
                                                                                            PUBLICATION_USER_DIFF,
                                                                                            PUBLICATION_FAILED_DATE_DIFF,
                                                                                            PUBLICATION_FAILED_USER_DIFF,
                                                                                            ARCHIVED_DATE_DIFF,
                                                                                            ARCHIVED_USER_DIFF,
                                                                                            CREATION_DATE_DIFF,
                                                                                            CREATION_USER_DIFF
                                                                                            }));
        // @formatter:on
    }
}
