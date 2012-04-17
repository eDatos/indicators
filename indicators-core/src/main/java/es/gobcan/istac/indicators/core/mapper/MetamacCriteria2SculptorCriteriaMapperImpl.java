package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

@Component
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<GeographicalValue>       geographicalValueCriteriaMapper       = null;
    private MetamacCriteria2SculptorCriteria<IndicatorVersion>        indicatorVersionCriteriaMapper        = null;
    private MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> indicatorsSystemVersionCriteriaMapper = null;

    public MetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        geographicalValueCriteriaMapper = new MetamacCriteria2SculptorCriteria<GeographicalValue>(GeographicalValue.class, null, GeographicalValueCriteriaPropertyEnum.class,
                new GeographicalValueCriteriaCallback());
        indicatorVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<IndicatorVersion>(IndicatorVersion.class, null, IndicatorCriteriaPropertyEnum.class,
                new IndicatorVersionCriteriaCallback());
        indicatorsSystemVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion>(IndicatorsSystemVersion.class, null, IndicatorsSystemCriteriaPropertyEnum.class,
                new IndicatorsSystemVersionCriteriaCallback());
    }

    @Override
    public MetamacCriteria2SculptorCriteria<GeographicalValue> getGeographicalValueCriteriaMapper() {
        return geographicalValueCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> getIndicatorsSystemVersionCriteriaMapper() {
        return indicatorsSystemVersionCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<IndicatorVersion> getIndicatorVersionCriteriaMapper() {
        return indicatorVersionCriteriaMapper;
    }

    private class GeographicalValueCriteriaCallback implements CriteriaCallback {

        @Override
        public Property<GeographicalValue> retrieveProperty(String propertyName) throws MetamacException {
            GeographicalValueCriteriaPropertyEnum propertyNameCriteria = GeographicalValueCriteriaPropertyEnum.fromValue(propertyName);
            switch (propertyNameCriteria) {
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return new LeafProperty<GeographicalValue>("granularity", "uuid", false, GeographicalValue.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
            }
        }

        @Override
        public Object retrievePropertyValue(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            GeographicalValueCriteriaPropertyEnum propertyNameCriteria = GeographicalValueCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return propertyRestriction.getStringValue();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalValue> retrievePropertyOrderDefault() throws MetamacException {
            return GeographicalValueProperties.id();
        }
    }
    private class IndicatorVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public Property<IndicatorVersion> retrieveProperty(String propertyName) throws MetamacException {
            IndicatorCriteriaPropertyEnum propertyNameCriteria = IndicatorCriteriaPropertyEnum.fromValue(propertyName);
            switch (propertyNameCriteria) {
                case CODE:
                    return new LeafProperty<IndicatorVersion>("indicator", "code", false, IndicatorVersion.class);
                case SUBJECT_CODE:
                    return IndicatorVersionProperties.subjectCode();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
            }
        }

        @Override
        public Object retrievePropertyValue(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorCriteriaPropertyEnum propertyNameCriteria = IndicatorCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return propertyRestriction.getStringValue();
                case SUBJECT_CODE:
                    return propertyRestriction.getStringValue();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorVersion> retrievePropertyOrderDefault() throws MetamacException {
            return IndicatorVersionProperties.id();
        }
    }
    private class IndicatorsSystemVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public Property<IndicatorsSystemVersion> retrieveProperty(String propertyName) throws MetamacException {
            IndicatorsSystemCriteriaPropertyEnum propertyNameCriteria = IndicatorsSystemCriteriaPropertyEnum.fromValue(propertyName);
            switch (propertyNameCriteria) {
                case CODE:
                    return new LeafProperty<IndicatorsSystemVersion>("indicatorsSystem", "code", false, IndicatorsSystemVersion.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
            }
        }

        @Override
        public Object retrievePropertyValue(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorsSystemCriteriaPropertyEnum propertyNameCriteria = IndicatorsSystemCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return propertyRestriction.getStringValue();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorsSystemVersion> retrievePropertyOrderDefault() throws MetamacException {
            return IndicatorsSystemVersionProperties.id();
        }
    }
}