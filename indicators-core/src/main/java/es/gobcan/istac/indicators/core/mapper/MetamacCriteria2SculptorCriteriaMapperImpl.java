package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaOrderEnum;
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
        geographicalValueCriteriaMapper = new MetamacCriteria2SculptorCriteria<GeographicalValue>(GeographicalValue.class, GeographicalValueCriteriaOrderEnum.class, GeographicalValueCriteriaPropertyEnum.class,
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
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            GeographicalValueCriteriaPropertyEnum propertyNameCriteria = GeographicalValueCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.granularity().uuid(), propertyRestriction.getStringValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalValue> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            GeographicalValueCriteriaOrderEnum propertyNameCriteria = GeographicalValueCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return GeographicalValueProperties.granularity();
                case ORDER:
                    return GeographicalValueProperties.order();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalValue> retrievePropertyOrderDefault() throws MetamacException {
            return GeographicalValueProperties.id();
        }
    }
    private class IndicatorVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorCriteriaPropertyEnum propertyNameCriteria = IndicatorCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), propertyRestriction.getStringValue());
                case SUBJECT_CODE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.subjectCode(), propertyRestriction.getStringValue());
                case TITLE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.title().texts().label(), propertyRestriction.getStringValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            return null; // put default order
        }

        @Override
        public Property<IndicatorVersion> retrievePropertyOrderDefault() throws MetamacException {
            return IndicatorVersionProperties.id();
        }
    }
    private class IndicatorsSystemVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorsSystemCriteriaPropertyEnum propertyNameCriteria = IndicatorsSystemCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(IndicatorsSystemVersionProperties.indicatorsSystem().code(), propertyRestriction.getStringValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorsSystemVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            return null; // put default order
        }

        @Override
        public Property<IndicatorsSystemVersion> retrievePropertyOrderDefault() throws MetamacException {
            return IndicatorsSystemVersionProperties.id();
        }
    }
}
