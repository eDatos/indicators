package es.gobcan.istac.indicators.web.server.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemVersionSummaryDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.QueryMetamacUtils;
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
     * Fills an {@link IndicatorsSystemDtoWeb} from {@link IndicatorsSystemDto} and {@link Operation}
     * 
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operation
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto, Operation operation) {
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
        }
        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));
            indicatorsSystemDtoWeb.setAcronym(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getAcronym()));
            indicatorsSystemDtoWeb.setDescription(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getDescription()));
            indicatorsSystemDtoWeb.setObjective(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getObjective()));
            indicatorsSystemDtoWeb.setOperationExternallyPublished(ProcStatus.EXTERNALLY_PUBLISHED.equals(operation.getProcStatus()));
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
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setDiffusionVersion(indicatorsSystemDto.getDiffusionVersion());
        }
        if (operation != null) {
            indicatorsSystemDtoWeb.setCode(operation.getId());
            indicatorsSystemDtoWeb.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(operation.getName()));
        }
        return indicatorsSystemDtoWeb;
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
     * Create a {@link DataStructureDto} from a {@link Query}
     * 
     * @param query
     * @return
     * @throws MetamacException
     */
    public static DataStructureDto createDataStructureDto(Query query) throws MetamacException {
        if (query == null) {
            return null;
        }

        DataStructureDto dataStructureDto = new DataStructureDto();

        // UUid
        dataStructureDto.setUuid(query.getUrn());

        // Title
        dataStructureDto.setTitle(QueryMetamacUtils.extractValueForDefaultLanguage(query.getName()));

        // PX Uri
        dataStructureDto.setPxUri(query.getUrn());

        ResourceInternal statisticalOperation = query.getMetadata().getStatisticalOperation();

        // Survey Code
        dataStructureDto.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        dataStructureDto.setSurveyTitle(QueryMetamacUtils.extractValueForDefaultLanguage(statisticalOperation.getName()));

        // Maintainer
        ResourceInternal maintainer = query.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = QueryMetamacUtils.extractValueForDefaultLanguage(maintainer.getName());
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            dataStructureDto.setPublishers(Collections.emptyList());
        } else {
            dataStructureDto.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        // Variables
        dataStructureDto.setVariables(QueryMetamacUtils.extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        // Temporal Variables
        dataStructureDto.setTemporalVariable(QueryMetamacUtils.extractTemporalVariable(query));

        // Temporal Value
        dataStructureDto.setTemporalValue(QueryMetamacUtils.extractTemporalValue(query));

        // Spatial Variables
        dataStructureDto.setSpatialVariables(QueryMetamacUtils.extractSpatialVariableList(query));

        // Spatial Value
        dataStructureDto.setGeographicalValueDto(QueryMetamacUtils.extractGeographicalValueDto(query));

        // Cont Variable
        dataStructureDto.setContVariable(QueryMetamacUtils.extractContVariable(query));

        // Value Labels
        // TODO METAMAC-2503 Valores y Códigos idénditcos. No tenemos cubrimiento de Labels por ahora en Metamac.
        // TODO METAMAC-2503 Perfomance: Si existiese una forma de obtener el cubrimiento sin hacer petición de datos a la Query, no necesitaríamos recibir la Query con datos, lo cuál sería más
        // eficiente, en ese caso pasarle "?fields=-data" a la petición
        Map<String, List<String>> codes = QueryMetamacUtils.extractCoverages(query.getData());
        dataStructureDto.setValueLabels(codes);

        // Value Codes
        dataStructureDto.setValueCodes(codes);

        return dataStructureDto;
    }

}
