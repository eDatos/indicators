package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Embeddable;

/**
 * BasicType representing IndicatorSystemVersionInformation.
 * <p>
 * This class is responsible for the domain object related
 * business logic for IndicatorSystemVersionInformation. Properties and associations are
 * implemented in the generated base class {@link es.gobcan.istac.indicators.core.domain.IndicatorSystemVersionInformationBase}.
 */
@Embeddable
public class IndicatorSystemVersionInformation
    extends IndicatorSystemVersionInformationBase {
    private static final long serialVersionUID = 1L;

    public IndicatorSystemVersionInformation() {
    }

    public IndicatorSystemVersionInformation(Long idIndicatorSystemVersion, Long versionNumber) {
        super(idIndicatorSystemVersion, versionNumber);
        if (idIndicatorSystemVersion == null) {
            throw new IllegalArgumentException("'idIndicatorSystemVersion' can not be null");
        } else if (versionNumber == null) {
            throw new IllegalArgumentException("'versionNumber' can not be null");
        }
    }
}
