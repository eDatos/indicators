package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.indicators.core.stream.message.IndicatorVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

@Component
public class IndicatorVersionDo2AvroMapper implements Do2AvroMapper<IndicatorVersion, IndicatorVersionAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private DatetimeDo2AvroMapper            datetimeDo2AvroMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper      procStatusMapper;

    @Override
    public IndicatorVersionAvro toAvro(IndicatorVersion source) {
        if (source == null) {
            return null;
        }
        return IndicatorVersionAvro.newBuilder().setCode(source.getIndicator().getCode()).setDiffusionVersionNumber(source.getIndicator().getDiffusionVersionNumber())
                .setDiffusionProcStatus(procStatusMapper.toAvro(source.getIndicator().getDiffusionProcStatus())).setIsPublished(source.getIndicator().getIsPublished()).setVersion(source.getVersion())
                .setTitle(internationalStringMapper.toAvro(source.getTitle())).setAcronym(internationalStringMapper.toAvro(source.getAcronym())).setSubjectCode(source.getCategoryElement().getCode())
                .setSubjectTitle(internationalStringMapper.toAvro(source.getCategoryElement().getTitle())).setConceptDescription(internationalStringMapper.toAvro(source.getConceptDescription()))
                .setComments(internationalStringMapper.toAvro(source.getComments())).setNotes(internationalStringMapper.toAvro(source.getNotes()))
                .setPublicationDate(datetimeDo2AvroMapper.toAvro(source.getPublicationDate())).setPublicationUser(source.getPublicationUser())
                .setArchiveDate(datetimeDo2AvroMapper.toAvro(source.getArchiveDate())).setLastPopulateDate(datetimeDo2AvroMapper.toAvro(source.getLastPopulateDate())).build();
    }
}
