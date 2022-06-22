package es.gobcan.istac.indicators.core.serviceimpl.result;

import es.gobcan.istac.indicators.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.core.common.exception.MetamacException;

import java.util.List;

public class SendStreamMessageResult extends Result<StreamMessageStatusEnum> {

    public SendStreamMessageResult() {
    }

    public SendStreamMessageResult(StreamMessageStatusEnum status, List<MetamacException> exceptions) {
        super(status, exceptions);
    }

    @Override
    public boolean isOk() {
        return super.isOk() && content != StreamMessageStatusEnum.FAILED;
    }
}
