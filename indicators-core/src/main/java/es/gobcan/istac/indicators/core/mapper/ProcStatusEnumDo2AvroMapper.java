package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import org.siemac.metamac.indicators.core.stream.message.ProcStatusEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class ProcStatusEnumDo2AvroMapper implements Do2AvroMapper<IndicatorProcStatusEnum, ProcStatusEnumAvro> {

    @Override
    public ProcStatusEnumAvro toAvro(IndicatorProcStatusEnum source) {
        if (source == null) {
            return null;
        }
        return ProcStatusEnumAvro.valueOf(source.name());
    }
}
