package es.gobcan.istac.indicators.web.server.utils;

import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnItemScheme;
import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnQueryGlobal;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemVersionSummaryDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.CommonMetamacUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.DatasetMetamacUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.QueryMetamacUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class DtoUtils {

    /**
     * Updates {@link IndicatorsSystemDtoWeb}
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto) {
        return updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto, null);
    }

    /**
     * Create operationSystemDtoWeb from code and title introduced by user. This indicators system is not linked to and operation.
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param code
     * @param title
     * @return
     */
    public static IndicatorsSystemDtoWeb createOrUpdateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto) {

        updateIndicatorsSystemDtoWebCommon(indicatorsSystemDtoWeb, indicatorsSystemDto);

        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setTitle(indicatorsSystemDto.getTitle());
            indicatorsSystemDtoWeb.setAcronym(indicatorsSystemDto.getAcronym());
            indicatorsSystemDtoWeb.setDescription(indicatorsSystemDto.getDescription());
            indicatorsSystemDtoWeb.setObjective(indicatorsSystemDto.getObjective());

        }
        return indicatorsSystemDtoWeb;
    }

    /**
     * Fills an {@link IndicatorsSystemDtoWeb} from {@link IndicatorsSystemDto} and {@link Operation}
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto, Operation operation) {

        updateIndicatorsSystemDtoWebCommon(indicatorsSystemDtoWeb, indicatorsSystemDto);

        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));
            indicatorsSystemDtoWeb.setAcronym(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getAcronym()));
            indicatorsSystemDtoWeb.setDescription(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getDescription()));
            indicatorsSystemDtoWeb.setObjective(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getObjective()));
            indicatorsSystemDtoWeb.setOperationExternallyPublished(ProcStatus.EXTERNALLY_PUBLISHED.equals(operation.getProcStatus()));
            indicatorsSystemDtoWeb.setIsOperational(true);

        } else if (!Boolean.TRUE.equals(indicatorsSystemDto != null && indicatorsSystemDto.getIsOperational())) {
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setTitle(indicatorsSystemDto.getTitle());
            indicatorsSystemDtoWeb.setAcronym(indicatorsSystemDto.getAcronym());
            indicatorsSystemDtoWeb.setDescription(indicatorsSystemDto.getDescription());
            indicatorsSystemDtoWeb.setObjective(indicatorsSystemDto.getObjective());

        }
        return indicatorsSystemDtoWeb;
    }

    /**
     * Fills an {@link IndicatorsSystemDtoWeb} from {@link IndicatorsSystemDto} and {@link Operation}
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWebCommon(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto) {
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setVersionNumber(indicatorsSystemDto.getVersionNumber());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setPublishedVersion(indicatorsSystemDto.getPublishedVersion());
            indicatorsSystemDtoWeb.setArchivedVersion(indicatorsSystemDto.getArchivedVersion());
            indicatorsSystemDtoWeb.setProductionValidationDate(indicatorsSystemDto.getProductionValidationDate());
            indicatorsSystemDtoWeb.setProductionValidationUser(indicatorsSystemDto.getProductionValidationUser());
            indicatorsSystemDtoWeb.setDiffusionValidationDate(indicatorsSystemDto.getDiffusionValidationDate());
            indicatorsSystemDtoWeb.setDiffusionValidationUser(indicatorsSystemDto.getDiffusionValidationUser());
            indicatorsSystemDtoWeb.setPublicationDate(indicatorsSystemDto.getPublicationDate());
            indicatorsSystemDtoWeb.setPublicationUser(indicatorsSystemDto.getPublicationUser());
            indicatorsSystemDtoWeb.setArchiveDate(indicatorsSystemDto.getArchiveDate());
            indicatorsSystemDtoWeb.setArchiveUser(indicatorsSystemDto.getArchiveUser());
            indicatorsSystemDtoWeb.setProcStatus(indicatorsSystemDto.getProcStatus());
            indicatorsSystemDtoWeb.setCreatedDate(indicatorsSystemDto.getCreatedDate());
            indicatorsSystemDtoWeb.setCreatedBy(indicatorsSystemDto.getCreatedBy());
            indicatorsSystemDtoWeb.setLastUpdated(indicatorsSystemDto.getLastUpdated());
            indicatorsSystemDtoWeb.setLastUpdatedBy(indicatorsSystemDto.getLastUpdatedBy());
            indicatorsSystemDtoWeb.setStreamMessageStatus(indicatorsSystemDto.getStreamMessageStatus());
            indicatorsSystemDtoWeb.setIsOperational(indicatorsSystemDto.getIsOperational());
            indicatorsSystemDtoWeb.setVersionOptimisticLocking(indicatorsSystemDto.getVersionOptimisticLocking());
        }

        return indicatorsSystemDtoWeb;
    }

    /**
     * Fills an {@link IndicatorsSystemSummaryDtoWeb} from {@link IndicatorsSystemSummaryDto} and {@link Resource}
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb updateIndicatorsSystemSummaryDtoWeb(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemSummaryDto indicatorsSystemDto,
            Resource operation) {

        updateIndicatorsSystemSummaryDtoWebCommon(indicatorsSystemDtoWeb, indicatorsSystemDto);

        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));

        }
        return indicatorsSystemDtoWeb;

    }

    /**
     * Fills an {@link IndicatorsSystemSummaryDtoWeb} from {@link IndicatorsSystemSummaryDto} and {@link Resource}
     *
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb updateIndicatorsSystemSummaryDtoWeb(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemSummaryDto indicatorsSystemDto, String code,
            InternationalStringDto title) {
        updateIndicatorsSystemSummaryDtoWebCommon(indicatorsSystemDtoWeb, indicatorsSystemDto);
        indicatorsSystemDtoWeb.setCode(code);
        indicatorsSystemDtoWeb.setTitle(title);
        return indicatorsSystemDtoWeb;
    }

    public static IndicatorsSystemSummaryDtoWeb updateIndicatorsSystemSummaryDtoWebCommon(IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemSummaryDto indicatorsSystemDto) {
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setDiffusionVersion(indicatorsSystemDto.getDiffusionVersion());
            indicatorsSystemDtoWeb.setIsOperational(indicatorsSystemDto.getIsOperational());

            if (!Boolean.TRUE.equals(indicatorsSystemDto.getIsOperational())) {
                indicatorsSystemDtoWeb.setTitle(indicatorsSystemDto.getTitle());
                indicatorsSystemDtoWeb.setDescription(indicatorsSystemDto.getDescription());
                indicatorsSystemDtoWeb.setAcronym(indicatorsSystemDto.getAcronym());
                indicatorsSystemDtoWeb.setObjective(indicatorsSystemDto.getObjective());
            }
        }

        return indicatorsSystemDtoWeb;

    }

    /**
     * Create operationSystemDtoWeb from code and title introduced by user. This indicators system is not linked to and operation.
     * 
     * @param code
     * @param title
     * @return
     */
    public static IndicatorsSystemDtoWeb createIndicatorsSystemDtoWeb(IndicatorsSystemDto indicatorsSystemDto) {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemDtoWeb();
        indicatorsSystemDtoWeb.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return createOrUpdateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto);
    }

    /**
     * Create an {@link IndicatorsSystemDtoWeb} from a {@link Operation}
     *
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb createIndicatorsSystemDtoWeb(Operation operation) {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemDtoWeb();
        indicatorsSystemDtoWeb.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, null, operation);
    }

    /**
     * Create an {@link IndicatorsSystemSummaryDtoWeb} from a {@link Resource}
     *
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb createIndicatorsSystemSummaryDtoWeb(Resource operation) {
        IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemSummaryDtoWeb();
        IndicatorsSystemVersionSummaryDto summaryDto = new IndicatorsSystemVersionSummaryDto();
        summaryDto.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemDtoWeb.setProductionVersion(summaryDto);
        return updateIndicatorsSystemSummaryDtoWeb(indicatorsSystemDtoWeb, null, operation);
    }

    /**
     * Create an {@link IndicatorsSystemSummaryDtoWeb} from a {@link Resource}
     *
     * @param operation
     * @return
     */
    public static IndicatorsSystemSummaryDtoWeb createIndicatorsSystemSummaryDtoWeb(String code, InternationalStringDto title) {
        IndicatorsSystemSummaryDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemSummaryDtoWeb();
        IndicatorsSystemVersionSummaryDto summaryDto = new IndicatorsSystemVersionSummaryDto();
        summaryDto.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemDtoWeb.setProductionVersion(summaryDto);
        return updateIndicatorsSystemSummaryDtoWeb(indicatorsSystemDtoWeb, null, code, title);
    }

    /**
     * Create a {@link DataStructureDto} from a {@link Query}
     *
     * @param query
     * @return
     * @throws MetamacException
     */
    public static DataStructureDto createDataStructureDto(Query query, SrmRestInternalService srmRestInternalService, String languageDefault) throws MetamacException {
        if (query == null) {
            return null;
        }
        QueryMetamacUtils queryMetamacUtils = new QueryMetamacUtils(srmRestInternalService, query, languageDefault);
        DataStructureDto dataStructureDto = new DataStructureDto();

        // UUid
        dataStructureDto.setUuid(query.getUrn());

        // Title
        dataStructureDto.setTitle(CommonMetamacUtils.extractValueForDefaultLanguage(query.getName(), languageDefault));

        // PX Uri
        dataStructureDto.setQueryUrn(query.getUrn());

        dataStructureDto.setResourceId(DtoUtils.getResourceIdFromMetamacUrn(query.getUrn()));

        Resource statisticalOperation = query.getMetadata().getStatisticalOperation();

        // Survey Code
        dataStructureDto.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        dataStructureDto.setSurveyTitle(CommonMetamacUtils.extractValueForDefaultLanguage(statisticalOperation.getName(), languageDefault));

        // Maintainer
        Resource maintainer = query.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = CommonMetamacUtils.extractValueForDefaultLanguage(maintainer.getName(), languageDefault);
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            dataStructureDto.setPublishers(Collections.emptyList());
        } else {
            dataStructureDto.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        // Variables
        dataStructureDto.setVariables(CommonMetamacUtils.extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        // Temporal Variables
        dataStructureDto.setTemporalVariable(queryMetamacUtils.extractTemporalVariable());

        // Temporal Value
        dataStructureDto.setTemporalValue(queryMetamacUtils.extractTemporalValue());

        // Spatial Variables
        dataStructureDto.setSpatialVariables(queryMetamacUtils.extractSpatialVariableList());

        // Spatial Value
        dataStructureDto.setGeographicalValueDto(queryMetamacUtils.extractGeographicalValueDto());

        // Cont Variable
        dataStructureDto.setContVariable(queryMetamacUtils.extractContVariable());

        // Value Labels
        dataStructureDto.setValueLabels(queryMetamacUtils.extractValuesCoverages());

        // Value Codes
        dataStructureDto.setValueCodes(queryMetamacUtils.extractCodesCoverages());

        // Geographical codelist urn
        dataStructureDto.setGeographicalCodelistUrn(queryMetamacUtils.extractGeographicalCodelistUrn());

        return dataStructureDto;
    }

    public static String getUrnWithoutVersion(String datasetVersionUrn) {
        String[] params = splitUrnItemScheme(datasetVersionUrn);
        String[] agencyId = {params[0]};
        String resourceId = params[1];

        return GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetUrn(agencyId, resourceId);
    }

    private static String getResourceIdFromMetamacUrn(String datasetVersionUrn) {
        // check if urn has version
        String[] params = splitUrnItemScheme(datasetVersionUrn);

        if (params != null && params[1] != null) {
            return params[1];
        }

        // urn without version example query urn o dataset urn without version
        params = splitUrnQueryGlobal(datasetVersionUrn);
        if (params == null) {
            return "";
        }
        return params[1];
    }

    public static DataStructureDto createDataStructureDatasetDto(Dataset dataset, SrmRestInternalService srmRestInternalService, String languageDefault) throws MetamacException {
        if (dataset == null) {
            return null;
        }
        DatasetMetamacUtils datasetMetamacUtils = new DatasetMetamacUtils(srmRestInternalService, dataset, languageDefault);
        DataStructureDto dataStructureDto = new DataStructureDto();

        String datasetUrn = getUrnWithoutVersion(dataset.getUrn());

        // UUid
        dataStructureDto.setUuid(datasetUrn);

        // Title
        dataStructureDto.setTitle(CommonMetamacUtils.extractValueForDefaultLanguage(dataset.getName(), languageDefault));

        // PX Uri
        dataStructureDto.setQueryUrn(datasetUrn);

        dataStructureDto.setResourceId(getResourceIdFromMetamacUrn(datasetUrn));

        Resource statisticalOperation = dataset.getMetadata().getStatisticalOperation();

        // Survey Code
        dataStructureDto.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        dataStructureDto.setSurveyTitle(CommonMetamacUtils.extractValueForDefaultLanguage(statisticalOperation.getName(), languageDefault));

        // Maintainer
        Resource maintainer = dataset.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = CommonMetamacUtils.extractValueForDefaultLanguage(maintainer.getName(), languageDefault);
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            dataStructureDto.setPublishers(Collections.emptyList());
        } else {
            dataStructureDto.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        // Variables
        dataStructureDto.setVariables(CommonMetamacUtils.extractVariablesFromDimensions(dataset.getMetadata().getDimensions()));

        // Temporal Variables
        dataStructureDto.setTemporalVariable(datasetMetamacUtils.extractTemporalVariable());

        // Temporal Value
        dataStructureDto.setTemporalValue(datasetMetamacUtils.extractTemporalValue());

        // Spatial Variables
        dataStructureDto.setSpatialVariables(datasetMetamacUtils.extractSpatialVariableList());

        // Spatial Value
        dataStructureDto.setGeographicalValueDto(datasetMetamacUtils.extractGeographicalValueDto());

        // Cont Variable
        dataStructureDto.setContVariable(datasetMetamacUtils.extractContVariable());

        // Value Labels
        dataStructureDto.setValueLabels(datasetMetamacUtils.extractValuesCoverages());

        // Value Codes
        dataStructureDto.setValueCodes(datasetMetamacUtils.extractCodesCoverages());

        // Geographical codelist urn
        dataStructureDto.setGeographicalCodelistUrn(datasetMetamacUtils.extractGeographicalCodelistUrn());

        return dataStructureDto;
    }

}
