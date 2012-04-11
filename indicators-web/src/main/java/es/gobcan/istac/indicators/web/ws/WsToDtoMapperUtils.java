package es.gobcan.istac.indicators.web.ws;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedStringList;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;

import es.gobcan.istac.indicators.web.indicatorssystems.IndicatorsSystemWebDto;

public class WsToDtoMapperUtils {

    /**
     * Fills {@link IndicatorsSystemWebDto} from a {@link OperationBase}
     * 
     * @param operationBase
     * @return
     */
    public static IndicatorsSystemWebDto getIndicatorsSystemDtoFromOperationBase(OperationBase operationBase) {
        IndicatorsSystemWebDto indicatorsSystemWebDto = new IndicatorsSystemWebDto();
        indicatorsSystemWebDto.setCode(operationBase.getCode());
        indicatorsSystemWebDto.setTitle(getInternationalStringDtoFromInternationalString(operationBase.getTitle()));
        indicatorsSystemWebDto.setAcronym(getInternationalStringDtoFromInternationalString(operationBase.getAcronym()));
        indicatorsSystemWebDto.setDescription(getInternationalStringDtoFromInternationalString(operationBase.getDescription()));
        indicatorsSystemWebDto.setObjective(getInternationalStringDtoFromInternationalString(operationBase.getObjective()));
        return indicatorsSystemWebDto;
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
