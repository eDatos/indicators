package es.gobcan.istac.indicators.core.mapper;

import java.util.Collection;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.mapper.BaseDo2DtoMapper;

public interface CommonDo2DtoMapper extends BaseDo2DtoMapper {

    // External Item
    public Collection<ExternalItemDto> externalItemDoCollectionToDtoCollection(Collection<ExternalItem> source) throws MetamacException;
    public List<ExternalItemDto> externalItemDoCollectionToDtoList(Collection<ExternalItem> source) throws MetamacException;
    public ExternalItemDto externalItemDoToDto(ExternalItem source) throws MetamacException;
    
    // International String
    public InternationalStringDto internationalStringDoToDto(InternationalString source);

}