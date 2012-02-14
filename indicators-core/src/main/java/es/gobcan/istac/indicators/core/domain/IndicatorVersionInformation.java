package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Embeddable;

/**
 * BasicType representing IndicatorVersionInformation.
 * <p>
 * This class is responsible for the domain object related
 * business logic for IndicatorVersionInformation. Properties and associations are
 * implemented in the generated base class {@link es.gobcan.istac.indicators.core.domain.IndicatorVersionInformationBase}.
 */
@Embeddable
public class IndicatorVersionInformation extends IndicatorVersionInformationBase {
    private static final long serialVersionUID = 1L;

    public IndicatorVersionInformation() {
    }

    public IndicatorVersionInformation(Long idIndicatorVersion,
        String versionNumber) {
        super(idIndicatorVersion, versionNumber);
    }
}
