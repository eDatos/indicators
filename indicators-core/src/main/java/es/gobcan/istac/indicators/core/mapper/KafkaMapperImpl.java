package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemCodesDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemDto;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.InternationalStringUtils;

@org.springframework.stereotype.Component("kafkaMapper")
public class KafkaMapperImpl implements KafkaMapper {

    private final SrmRestInternalService srmService;

    @Autowired
    public KafkaMapperImpl(SrmRestInternalService srmService) {
        this.srmService = srmService;
    }

    @Override
    public ExternalItemDto kafkaMessageToRepositoryExternalItemDto(ServiceContext ctx, SpecificRecordBase messageSource) throws MetamacException {
        if (messageSource instanceof CodelistAvro) {
            return processSrmCodelistKafkaMessage(ctx, messageSource);
        }
        return null;
    }

    private ExternalItemDto processSrmCodelistKafkaMessage(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {

        CodelistAvro codelistAvro = (CodelistAvro) message;

        ExternalItemDto externalItemDto = new ExternalItemDto();

        externalItemDto.setType(TypeExternalArtefactsEnum.CODELIST.getValue());
        externalItemDto.setUrn(codelistAvro.getUrn());

        List<String> elementCodeUsed = new ArrayList<>();

        Codes codes = srmService.retrieveCodesOfCodelist(codelistAvro.getUrn(), false);
        for (CodeResourceInternal srmCode : codes.getCodes()) {
            ExternalItemCodesDto code = itemResourceInternalToExternalItemCodesDto(srmCode);
            if (elementCodeUsed.contains(srmCode.getVariableElement().getId())) {
                code.setElementCode(srmCode.getVariableElement().getId() + "$");
            } else {
                code.setElementCode(srmCode.getVariableElement().getId());
                elementCodeUsed.add(srmCode.getVariableElement().getId());
            }
            externalItemDto.addCode(code);

        }

        return externalItemDto;

    }

    private ExternalItemCodesDto itemResourceInternalToExternalItemCodesDto(ItemResourceInternal item) {
        ExternalItemCodesDto externalItemCodeDto = new ExternalItemCodesDto();
        externalItemCodeDto.setCode(item.getId());
        externalItemCodeDto.setTitle(InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(item.getName()));

        return externalItemCodeDto;
    }

}
