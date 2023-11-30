package es.gobcan.istac.indicators.web.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.ListBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.web.common.server.utils.DtoUtils;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;

public class ExternalItemWebUtils extends org.siemac.metamac.web.common.client.utils.ExternalItemUtils {

    public static ExternalItemsResult createExternalItemsResultFromListBase(ListBase listBase, List<ExternalItemDto> dtos) {
        ExternalItemsResult result = new ExternalItemsResult();
        if (listBase != null) {
            result.setFirstResult(listBase.getOffset() != null ? listBase.getOffset().intValue() : 0);
            result.setTotalResults(listBase.getOffset() != null ? listBase.getTotal().intValue() : 0);
            result.setExternalItemDtos(dtos);
        }
        return result;
    }

    // Categories
    @SuppressWarnings("unchecked")
    public static ExternalItemsResult getCategoryElementsAsExternalItemsResult(List<ResourceInternal> categoryElements) {
        ExternalItemsResult result = new ExternalItemsResult();
        result.setExternalItemDtos(getExternalItemDtosFromItemResourceInternals((List<ItemResourceInternal>) (List<?>) categoryElements));
        return result;
    }

    private static List<ExternalItemDto> getExternalItemDtosFromItemResourceInternals(List<ItemResourceInternal> resources) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>(resources.size());
        for (ResourceInternal resource : resources) {
            externalItemDtos.add(getExternalItemDtoFromSrmResourceInternal(resource));
        }
        return externalItemDtos;
    }

    private static List<ExternalItemDto> getExternalItemDtosFromResourceInternals(List<ResourceInternal> resources) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>(resources.size());
        for (ResourceInternal resource : resources) {
            externalItemDtos.add(getExternalItemDtoFromSrmResourceInternal(resource));
        }
        return externalItemDtos;
    }

    private static List<ExternalItemDto> getExternalItemDtosFromCodeResourceInternals(List<CodeResourceInternal> resources) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>(resources.size());
        for (ResourceInternal resource : resources) {
            externalItemDtos.add(getExternalItemDtoFromSrmResourceInternal(resource));
        }
        return externalItemDtos;
    }

    // Codelist

    @SuppressWarnings("unchecked")
    public static ExternalItemsResult getCodelistsAsExternalItemsResult(Codelists codelists) {
        ExternalItemsResult result = getListBaseAsExternalItemsResult(codelists);
        result.setExternalItemDtos(getExternalItemDtosFromResourceInternals((List<ResourceInternal>) (List<?>) codelists.getCodelists()));
        return result;
    }

    // Codes

    public static ExternalItemsResult getCodesAsExternalItemsResult(Codes codes) {
        ExternalItemsResult result = getListBaseAsExternalItemsResult(codes);
        result.setExternalItemDtos(getExternalItemDtosFromCodeResourceInternals(codes.getCodes()));
        return result;
    }

    //
    // COMMON METHODS
    //

    private static ExternalItemsResult getListBaseAsExternalItemsResult(ListBase listBase) {
        ExternalItemsResult result = new ExternalItemsResult();
        if (listBase != null) {
            result.setFirstResult(listBase.getOffset() != null ? listBase.getOffset().intValue() : 0);
            result.setTotalResults(listBase.getOffset() != null ? listBase.getTotal().intValue() : 0);
        }
        return result;
    }

    private static ExternalItemDto getExternalItemDtoFromSrmResourceInternal(ResourceInternal resourceInternal) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(resourceInternal.getId());
        externalItemDto.setCodeNested(resourceInternal.getNestedId());
        externalItemDto.setUri(resourceInternal.getSelfLink().getHref());
        externalItemDto.setUrn(resourceInternal.getUrn());
        externalItemDto.setUrnProvider(resourceInternal.getUrnProvider());
        externalItemDto.setType(TypeExternalArtefactsEnum.fromValue(resourceInternal.getKind()));
        externalItemDto.setTitle(DtoUtils.getInternationalStringDtoFromInternationalString(resourceInternal.getName()));
        externalItemDto.setManagementAppUrl(resourceInternal.getManagementAppLink());
        return externalItemDto;
    }

}
