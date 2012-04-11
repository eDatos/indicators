package es.gobcan.istac.indicators.web.server.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedStringList;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

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
     * Fills an {@link IndicatorsSystemDtoWeb} from {@link IndicatorsSystemDto} and {@link OperationBase}
     * 
     * @param indicatorsSystemDtoWeb
     * @param indicatorsSystemDto
     * @param operationBase
     * @return
     */
    public static IndicatorsSystemDtoWeb updateIndicatorsSystemDtoWeb(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb, IndicatorsSystemDto indicatorsSystemDto, OperationBase operationBase) {
        if (indicatorsSystemDto != null) {
            indicatorsSystemDtoWeb.setUuid(indicatorsSystemDto.getUuid());
            indicatorsSystemDtoWeb.setVersionNumber(indicatorsSystemDto.getVersionNumber());
            indicatorsSystemDtoWeb.setCode(indicatorsSystemDto.getCode());
            indicatorsSystemDtoWeb.setProductionVersion(indicatorsSystemDto.getProductionVersion());
            indicatorsSystemDtoWeb.setDiffusionVersion(indicatorsSystemDto.getDiffusionVersion());
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
        if (operationBase != null) {
            indicatorsSystemDtoWeb.setStatisticalOperationUri(operationBase.getUri());
            indicatorsSystemDtoWeb.setCode(operationBase.getCode());
            indicatorsSystemDtoWeb.setTitle(getInternationalStringDtoFromInternationalString(operationBase.getTitle()));
            indicatorsSystemDtoWeb.setAcronym(getInternationalStringDtoFromInternationalString(operationBase.getAcronym()));
            indicatorsSystemDtoWeb.setDescription(getInternationalStringDtoFromInternationalString(operationBase.getDescription()));
            indicatorsSystemDtoWeb.setObjective(getInternationalStringDtoFromInternationalString(operationBase.getObjective()));
        }
        return indicatorsSystemDtoWeb;
    }

    /**
     * Create an {@link IndicatorsSystemDtoWeb} from a {@link OperationBase}
     * 
     * @param operationBase
     * @return
     */
    public static IndicatorsSystemDtoWeb createIndicatorsSystemDtoWeb(OperationBase operationBase) {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = new IndicatorsSystemDtoWeb();
        indicatorsSystemDtoWeb.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, null, operationBase);
    }

    /**
     * Returns an {@link InternationalStringDto} from an {@link InternationalString}
     * 
     * @param internationalString
     * @return
     */
    private static InternationalStringDto getInternationalStringDtoFromInternationalString(InternationalString internationalString) {
        if (internationalString != null) {
            InternationalStringDto internationalStringDto = new InternationalStringDto();
            LocalisedStringList localisedStringList = internationalString.getLocalisedStrings();
            if (localisedStringList != null && localisedStringList.getLocalisedString() != null) {
                for (LocalisedString localisedString : localisedStringList.getLocalisedString()) {
                    LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                    localisedStringDto.setLocale(localisedString.getLocale());
                    localisedStringDto.setLabel(localisedString.getLabel());
                    internationalStringDto.addText(localisedStringDto);
                }
            }
            return internationalStringDto;
        }
        return null;
    }

}
