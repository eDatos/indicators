package es.gobcan.istac.indicators.core.mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

@Component
public class Do2DtoMapperImpl implements Do2DtoMapper {
    
    @Override
    public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion source) {

        IndicatorsSystemDto target = new IndicatorsSystemDto();

        target.setUuid(source.getIndicatorsSystem().getUuid());
        target.setVersionNumber(source.getVersionNumber());
        target.setCode(source.getIndicatorsSystem().getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setAcronym(internationalStringToDto(source.getAcronym()));
        target.setUriGopestat(source.getUriGopestat());
        target.setObjetive(internationalStringToDto(source.getObjetive()));
        target.setDescription(internationalStringToDto(source.getDescription()));
        target.setState(source.getState());
        target.setProductionVersion(source.getIndicatorsSystem().getProductionVersion() != null ? source.getIndicatorsSystem().getProductionVersion().getVersionNumber() : null);
        target.setDiffusionVersion(source.getIndicatorsSystem().getDiffusionVersion() != null ? source.getIndicatorsSystem().getDiffusionVersion().getVersionNumber() : null);

        target.setProductionValidationDate(dateDoToDto(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(dateDoToDto(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setPublicationDate(dateDoToDto(source.getPublicationDate()));
        target.setPublicationUser(source.getPublicationUser());
        target.setArchiveDate(dateDoToDto(source.getArchiveDate()));
        target.setArchiveUser(source.getArchiveUser());

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    // Note: We not put relation @Dimension parent because Sculptor generates a wrong constraint when this is combined with Bag<@Dimension> subdimensions
    // If it is necesary, obtain parent with a query
    @Override
    public DimensionDto dimensionDoToDto(Dimension source) {
        DimensionDto target = new DimensionDto();
        target.setUuid(source.getUuid());
        target.setParentDimensionUuid(source.getParent() != null ? source.getParent().getUuid() : null);
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setOrderInLevel(source.getOrderInLevel());
        for (Dimension dimensionChildren : source.getSubdimensions()) {
            target.getSubdimensions().add(dimensionDoToDto(dimensionChildren));
        }
        return target;
    }

    private InternationalStringDto internationalStringToDto(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalStringDto target = new InternationalStringDto();
        target.getTexts().addAll(localisedStringDoToDto(source.getTexts()));
        return target;
    }

    private Set<LocalisedStringDto> localisedStringDoToDto(Set<LocalisedString> sources) {
        Set<LocalisedStringDto> targets = new HashSet<LocalisedStringDto>();
        for (LocalisedString source : sources) {
            LocalisedStringDto target = new LocalisedStringDto();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            targets.add(target);
        }
        return targets;
    }

    private Date dateDoToDto(DateTime source) {
        if (source == null) {
            return null;
        }
        return source.toDate();
    }
}
