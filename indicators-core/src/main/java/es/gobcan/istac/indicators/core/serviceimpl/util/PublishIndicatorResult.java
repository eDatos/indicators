package es.gobcan.istac.indicators.core.serviceimpl.util;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public class PublishIndicatorResult {

    private IndicatorVersion indicatorVersion;
    private MetamacException publicationFailedReason;

    public IndicatorVersion getIndicatorVersion() {
        return indicatorVersion;
    }

    public void setIndicatorVersion(IndicatorVersion indicatorVersion) {
        this.indicatorVersion = indicatorVersion;
    }

    public MetamacException getPublicationFailedReason() {
        return publicationFailedReason;
    }

    public void setPublicationFailedReason(MetamacException publicationFailedReason) {
        this.publicationFailedReason = publicationFailedReason;
    }
}
