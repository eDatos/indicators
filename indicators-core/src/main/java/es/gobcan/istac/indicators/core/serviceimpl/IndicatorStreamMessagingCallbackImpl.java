package es.gobcan.istac.indicators.core.serviceimpl;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.StreamMessageStatusEnum;
import es.gobcan.istac.indicators.core.mapper.IndicatorVersionDo2AvroMapper;
import es.gobcan.istac.indicators.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.indicators.core.stream.message.IndicatorVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("indicatorStreamMessagingCallback")
class IndicatorStreamMessagingCallbackImpl implements StreamMessagingCallback<IndicatorVersion, IndicatorVersionAvro, IndicatorVersionDo2AvroMapper> {

    @Autowired
    private IndicatorVersionDo2AvroMapper indicatorVersionDo2AvroMapper;

    @Autowired
    private IndicatorVersionRepository indicatorVersionRepository;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Override
    public String getUniqueIdentifier(IndicatorVersion messageContent) {
        return messageContent.getCode() + "(" + messageContent.getVersionNumber() + ")";
    }

    @Override
    public String getProducerRecordKey(IndicatorVersion messageContent){
        return messageContent.getCode() + "(" + messageContent.getVersionNumber() + ")";
    }

    @Override
    public List<IndicatorVersion> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(IndicatorVersion.class)
                .lbrace()
                .withProperty(IndicatorVersionProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(IndicatorVersionProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .withProperty(IndicatorVersionProperties.procStatus()).eq(IndicatorProcStatusEnum.PUBLISHED)
                .build();
        // @formatter:on
        //getStatisticalOperationsBaseService().findOperationByCondition(ctx, criteria);
        return indicatorVersionRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public IndicatorVersionDo2AvroMapper getMapper() {
        return indicatorVersionDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(IndicatorVersion messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(IndicatorVersion messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() throws MetamacException {
        return configurationService.retrieveKafkaTopicIndicatorsPublication();
    }
}