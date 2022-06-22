package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.indicators.core.stream.message.RelatedResourceTypeEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class RelatedResourceTypeEnumDo2AvroMapper implements Do2AvroMapper<TypeExternalArtefactsEnum, RelatedResourceTypeEnumAvro> {

    @Override
    public RelatedResourceTypeEnumAvro toAvro(TypeExternalArtefactsEnum source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceTypeEnumAvro.valueOf(source.name());
    }
}
