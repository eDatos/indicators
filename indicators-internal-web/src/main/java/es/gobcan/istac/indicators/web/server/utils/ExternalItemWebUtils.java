package es.gobcan.istac.indicators.web.server.utils;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.rest.common.v1_0.domain.ListBase;
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
}
