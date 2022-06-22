package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import org.siemac.metamac.indicators.core.stream.message.IndicatorsSystemVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndicatorsSystemVersionDo2AvroMapper implements Do2AvroMapper<IndicatorsSystemVersion, IndicatorsSystemVersionAvro> {

    @Autowired
    private DatetimeDo2AvroMapper datetimeDo2AvroMapper;

    @Override
    public IndicatorsSystemVersionAvro toAvro(IndicatorsSystemVersion source) {
        if (source == null) {
            return null;
        }
        return IndicatorsSystemVersionAvro.newBuilder()
                .setCode(source.getIndicatorsSystem().getCode())
                .setIsPublished(source.getIndicatorsSystem().getIsPublished())
                .setVersionNumber(source.getIndicatorsSystem().getDiffusionVersion().getVersionNumber())
                .setId(source.getId())
                .setVersion(source.getVersion())
                .setPublicationDate(datetimeDo2AvroMapper.toAvro(source.getPublicationDate()))
                .setPublicationUser(source.getPublicationUser())
                .setArchiveDate(datetimeDo2AvroMapper.toAvro(source.getArchiveDate())).build();
    }
}
