package es.gobcan.istac.indicators.web.server.utils;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedStringList;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;


public class DtoUtils {

    /**
     * Fills {@link IndicatorsSystemDto} from a {@link OperationBase}
     * 
     * @param indicatorsSystemDto
     * @param operationBase
     * @return
     */
    public static IndicatorsSystemDto getIndicatorsSystemDtoFromOperationBase(IndicatorsSystemDto indicatorsSystemDto, OperationBase operationBase) {
        indicatorsSystemDto.setUriGopestat(operationBase.getUri());
        indicatorsSystemDto.setCode(operationBase.getCode());
        indicatorsSystemDto.setTitle(getInternationalStringDtoFromInternationalString(operationBase.getTitle()));
        indicatorsSystemDto.setAcronym(getInternationalStringDtoFromInternationalString(operationBase.getAcronym()));
        indicatorsSystemDto.setDescription(getInternationalStringDtoFromInternationalString(operationBase.getObjetive()));
        indicatorsSystemDto.setObjetive(getInternationalStringDtoFromInternationalString(operationBase.getObjetive()));
        indicatorsSystemDto.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return indicatorsSystemDto;
    }

    /**
     * Returns an {@link InternationalStringDto} from an {@link InternationalString}
     * 
     * @param internationalString
     * @return
     */
    public static InternationalStringDto getInternationalStringDtoFromInternationalString(InternationalString internationalString) {
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
