package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * BasicType representing IndicatorsSystemVersionInformation.
 * <p>
 * This class is responsible for the domain object related business logic for IndicatorsSystemVersionInformation. Properties and associations are implemented in the generated base class
 * {@link es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformationBase}.
 */
@Embeddable
public class IndicatorsSystemVersionInformation extends IndicatorsSystemVersionInformationBase {

    private static final long serialVersionUID = 1L;

    public IndicatorsSystemVersionInformation() {
    }

    public IndicatorsSystemVersionInformation(Long idIndicatorsSystemVersion, String versionNumber) {
        super(idIndicatorsSystemVersion, versionNumber);
        if (idIndicatorsSystemVersion == null) {
            throw new IllegalArgumentException("'idIndicatorsSystemVersion' can not be null");
        } else if (StringUtils.isBlank(versionNumber)) {
            throw new IllegalArgumentException("'versionNumber' can not be null");
        }
    }
}
