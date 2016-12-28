package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.utils.TypeExternalArtefactsEnumUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.mapper.BaseDo2DtoMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

@org.springframework.stereotype.Component("commonDo2DtoMapper")
public class CommonDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements CommonDo2DtoMapper {

    // ------------------------------------------------------------
    // EXTERNAL ITEM
    // ------------------------------------------------------------

    @Override
    public Collection<ExternalItemDto> externalItemDoCollectionToDtoCollection(Collection<ExternalItem> source) throws MetamacException {
        HashSet<ExternalItemDto> result = new HashSet<ExternalItemDto>();

        if (source != null) {
            for (ExternalItem externalItem : source) {
                result.add(externalItemDoToDto(externalItem));
            }
        }
        return result;
    }

    @Override
    public List<ExternalItemDto> externalItemDoCollectionToDtoList(Collection<ExternalItem> source) throws MetamacException {
        List<ExternalItemDto> result = new ArrayList<ExternalItemDto>();

        if (source != null) {
            for (ExternalItem externalItem : source) {
                result.add(externalItemDoToDto(externalItem));
            }
        }
        return result;
    }

    @Override
    public ExternalItemDto externalItemDoToDto(ExternalItem source) throws MetamacException {
        ExternalItemDto target = externalItemDoToDtoWithoutUrls(source);
        if (target != null) {
            if (TypeExternalArtefactsEnumUtils.isExternalItemOfCommonMetadataApp(source.getType())) {
                target = commonMetadataExternalItemDoToDto(source, target);
            } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfSrmApp(source.getType())) {
                target = srmExternalItemDoToDto(source, target);
            } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfStatisticalOperationsApp(source.getType())) {
                target = statisticalOperationsExternalItemDoToDto(source, target);
            } else {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Type of externalItem not defined for externalItemDoToDto");
            }
        }

        return target;
    }

    private ExternalItemDto externalItemDoToDtoWithoutUrls(ExternalItem source) {
        if (source == null) {
            return null;
        }
        ExternalItemDto target = new ExternalItemDto();
        target.setId(source.getId());
        target.setCode(source.getCode());
        target.setCodeNested(source.getCodeNested());
        target.setUri(source.getUri());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setType(source.getType());
        target.setManagementAppUrl(source.getManagementAppUrl());
        target.setTitle(internationalStringDoToDto(source.getTitle()));
        return target;
    }

    private ExternalItemDto commonMetadataExternalItemDoToDto(ExternalItem source, ExternalItemDto target) throws MetamacException {
        target.setUri(commonMetadataExternalApiUrlDoToDto(source.getUri()));
        target.setManagementAppUrl(commonMetadataInternalWebAppUrlDoToDto(source.getManagementAppUrl()));
        return target;
    }

    private ExternalItemDto srmExternalItemDoToDto(ExternalItem source, ExternalItemDto target) throws MetamacException {
        target.setUri(srmInternalApiUrlDoToDto(source.getUri()));
        target.setManagementAppUrl(srmInternalWebAppUrlDoToDto(source.getManagementAppUrl()));
        return target;
    }

    private ExternalItemDto statisticalOperationsExternalItemDoToDto(ExternalItem source, ExternalItemDto target) throws MetamacException {
        target.setUri(statisticalOperationsInternalApiUrlDoToDto(source.getUri()));
        target.setManagementAppUrl(statisticalOperationsInternalWebAppUrlDoToDto(source.getManagementAppUrl()));
        return target;
    }


    // ------------------------------------------------------------
    // INTERNATIONAL STRINGS
    // ------------------------------------------------------------

    @Override
    public InternationalStringDto internationalStringDoToDto(InternationalString source) {
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
            target.setIsUnmodifiable(source.getIsUnmodifiable());
            targets.add(target);
        }
        return targets;
    }
    
}
