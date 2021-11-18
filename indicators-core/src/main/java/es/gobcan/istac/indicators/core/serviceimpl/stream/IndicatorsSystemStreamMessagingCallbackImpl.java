package es.gobcan.istac.indicators.core.serviceimpl.stream;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.*;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.StreamMessageStatusEnum;
import es.gobcan.istac.indicators.core.mapper.IndicatorsSystemVersionDo2AvroMapper;
import es.gobcan.istac.indicators.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.indicators.core.stream.message.IndicatorsSystemVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("indicatorsSystemStreamMessagingCallback")
class IndicatorsSystemStreamMessagingCallbackImpl implements StreamMessagingCallback<IndicatorsSystemVersion, IndicatorsSystemVersionAvro, IndicatorsSystemVersionDo2AvroMapper> {

    @Autowired
    private IndicatorsSystemVersionDo2AvroMapper indicatorsSystemVersionDo2AvroMapper;

    @Autowired
    private IndicatorsSystemVersionRepository indicatorsSystemVersionRepository;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Override
    public String getUniqueIdentifier(IndicatorsSystemVersion messageContent) {
        return messageContent.getCode() + "(" + messageContent.getVersionNumber() + ")";
    }

    @Override
    public String getProducerRecordKey(IndicatorsSystemVersion messageContent){
        return messageContent.getCode() + "(" + messageContent.getVersionNumber() + ")";
    }

    @Override
    public List<IndicatorsSystemVersion> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(IndicatorsSystemVersion.class)
                .lbrace()
                .withProperty(IndicatorsSystemVersionProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(IndicatorsSystemVersionProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .withProperty(IndicatorsSystemVersionProperties.procStatus()).eq(IndicatorProcStatusEnum.PUBLISHED)
                .build();
        // @formatter:on
        return indicatorsSystemVersionRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public IndicatorsSystemVersionDo2AvroMapper getMapper() {
        return indicatorsSystemVersionDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(IndicatorsSystemVersion messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(IndicatorsSystemVersion messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() throws MetamacException {
        return configurationService.retrieveKafkaTopicIndicatorSystemsPublication();
    }
}