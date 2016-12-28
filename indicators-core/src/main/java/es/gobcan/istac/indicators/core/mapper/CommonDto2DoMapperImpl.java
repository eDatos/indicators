package es.gobcan.istac.indicators.core.mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.utils.TypeExternalArtefactsEnumUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

@org.springframework.stereotype.Component("commonDto2DoMapper")
public class CommonDto2DoMapperImpl extends BaseDto2DoMapperImpl implements CommonDto2DoMapper {

    @Autowired
    private InternationalStringRepository internationalStringRepository;

    @Autowired
    private ExternalItemRepository        externalItemRepository;

    // ------------------------------------------------------------
    // INTERNATIONAL STRINGS
    // ------------------------------------------------------------
    @Override
    public InternationalString internationalStringDtoToDo(InternationalStringDto source, InternationalString target, String metadataName) throws MetamacException {
        // Check it is valid
        checkInternationalStringDtoValid(source, metadataName);

        // Transform
        if (source == null) {
            if (target != null) {
                // Delete old entity
                internationalStringRepository.delete(target);
            }

            return null;
        }

        if (ValidationUtils.isEmpty(source)) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, metadataName);
        }

        if (target == null) {
            target = new InternationalString();
        }

        Set<LocalisedString> localisedStringEntities = localisedStringDtoToDo(source.getTexts(), target.getTexts(), target);
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);

        return target;
    }

    protected Set<LocalisedString> localisedStringDtoToDo(Set<LocalisedStringDto> sources, Set<LocalisedString> targets, InternationalString internationalStringTarget) {

        Set<LocalisedString> targetsBefore = targets;
        targets = new HashSet<LocalisedString>();

        for (LocalisedStringDto source : sources) {
            boolean existsBefore = false;
            for (LocalisedString target : targetsBefore) {
                if (source.getLocale().equals(target.getLocale())) {
                    targets.add(localisedStringDtoToDo(source, target, internationalStringTarget));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(localisedStringDtoToDo(source, internationalStringTarget));
            }
        }
        return targets;
    }

    private LocalisedString localisedStringDtoToDo(LocalisedStringDto source, InternationalString internationalStringTarget) {
        LocalisedString target = new LocalisedString();
        localisedStringDtoToDo(source, target, internationalStringTarget);
        return target;
    }

    private LocalisedString localisedStringDtoToDo(LocalisedStringDto source, LocalisedString target, InternationalString internationalStringTarget) {
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        target.setIsUnmodifiable(source.getIsUnmodifiable());
        target.setInternationalString(internationalStringTarget);
        return target;
    }

    // ------------------------------------------------------------
    // EXTERNAL ITEMS
    // ------------------------------------------------------------

    @Override
    public ExternalItem externalItemDtoToDo(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException {
        target = externalItemDtoToDoWithoutUrls(source, target, metadataName);

        if (target != null) {
            if (TypeExternalArtefactsEnumUtils.isExternalItemOfCommonMetadataApp(source.getType())) {
                target = commonMetadataExternalItemDtoToDo(source, target);
            } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfSrmApp(source.getType())) {
                target = srmExternalItemDtoToDo(source, target);
            } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfStatisticalOperationsApp(source.getType())) {
                target = statisticalOperationsExternalItemDtoToDo(source, target);
            } else {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Type of externalItem not defined for externalItemDtoToEntity");
            }
        }

        return target;
    }

    private ExternalItem externalItemDtoToDoWithoutUrls(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                externalItemRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            target = new ExternalItem();
        }
        target.setCode(source.getCode());
        target.setCodeNested(source.getCodeNested());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setType(source.getType());
        target.setTitle(internationalStringDtoToDo(source.getTitle(), target.getTitle(), metadataName));
        return target;
    }

    private ExternalItem commonMetadataExternalItemDtoToDo(ExternalItemDto source, ExternalItem target) throws MetamacException {
        target.setUri(commonMetadataExternalApiUrlDtoToDo(source.getUri()));
        target.setManagementAppUrl(commonMetadataInternalWebAppUrlDtoToDo(source.getManagementAppUrl()));
        return target;
    }

    private ExternalItem srmExternalItemDtoToDo(ExternalItemDto source, ExternalItem target) throws MetamacException {
        target.setUri(srmInternalApiUrlDtoToDo(source.getUri()));
        target.setManagementAppUrl(srmInternalWebAppUrlDtoToDo(source.getManagementAppUrl()));
        return target;
    }

    private ExternalItem statisticalOperationsExternalItemDtoToDo(ExternalItemDto source, ExternalItem target) throws MetamacException {
        target.setUri(statisticalOperationsInternalApiUrlDtoToDo(source.getUri()));
        target.setManagementAppUrl(statisticalOperationsInternalWebAppUrlDtoToDo(source.getManagementAppUrl()));
        return target;
    }

    protected void deleteExternalItemsNotFoundInSource(Collection<ExternalItem> targetBefore, Collection<ExternalItem> source, String metadataName) throws MetamacException {
        for (ExternalItem oldTarget : targetBefore) {
            boolean found = false;
            for (ExternalItem newTarget : source) {
                found = found || (StringUtils.equals(oldTarget.getUrn(), newTarget.getUrn()));
            }
            if (!found) {
                // Delete
                externalItemDtoToDo(null, oldTarget, metadataName);
            }
        }
    }

}
