package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.criteria.GeographicalGranularityCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.GeographicalGranularityCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.QuantityUnitCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.QuantityUnitCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.UnitMultiplierCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.UnitMultiplierCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularityProperties;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.QuantityUnitProperties;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.domain.UnitMultiplierProperties;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

@Component
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<GeographicalGranularity> geographicalGranularityCriteriaMapper = null;
    private MetamacCriteria2SculptorCriteria<GeographicalValue>       geographicalValueCriteriaMapper       = null;
    private MetamacCriteria2SculptorCriteria<QuantityUnit>            quantityUnitCriteriaMapper            = null;
    private MetamacCriteria2SculptorCriteria<UnitMultiplier>          unitMultiplierCriteriaMapper          = null;
    private MetamacCriteria2SculptorCriteria<IndicatorVersion>        indicatorVersionCriteriaMapper        = null;
    private MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> indicatorsSystemVersionCriteriaMapper = null;

    public MetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        geographicalGranularityCriteriaMapper = new MetamacCriteria2SculptorCriteria<GeographicalGranularity>(GeographicalGranularity.class, GeographicalGranularityCriteriaOrderEnum.class,
                GeographicalGranularityCriteriaPropertyEnum.class, new GeographicalGranularityCriteriaCallback());
        geographicalValueCriteriaMapper = new MetamacCriteria2SculptorCriteria<GeographicalValue>(GeographicalValue.class, GeographicalValueCriteriaOrderEnum.class,
                GeographicalValueCriteriaPropertyEnum.class, new GeographicalValueCriteriaCallback());
        unitMultiplierCriteriaMapper = new MetamacCriteria2SculptorCriteria<UnitMultiplier>(UnitMultiplier.class, UnitMultiplierCriteriaOrderEnum.class, UnitMultiplierCriteriaPropertyEnum.class,
                new UnitMultiplierCriteriaCallback());
        quantityUnitCriteriaMapper = new MetamacCriteria2SculptorCriteria<QuantityUnit>(QuantityUnit.class, QuantityUnitCriteriaOrderEnum.class, QuantityUnitCriteriaPropertyEnum.class,
                new QuantityUnitCriteriaCallback());
        indicatorVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<IndicatorVersion>(IndicatorVersion.class, IndicatorCriteriaOrderEnum.class, IndicatorCriteriaPropertyEnum.class,
                new IndicatorVersionCriteriaCallback());
        indicatorsSystemVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion>(IndicatorsSystemVersion.class, null, IndicatorsSystemCriteriaPropertyEnum.class,
                new IndicatorsSystemVersionCriteriaCallback());
    }

    // -------------------------------------------------------------------------------------
    // GEOGRAPHICAL GRANULARITY
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<GeographicalGranularity> getGeographicalGranularityCriteriaMapper() {
        return geographicalGranularityCriteriaMapper;
    }

    private class GeographicalGranularityCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            GeographicalGranularityCriteriaPropertyEnum propertyNameCriteria = GeographicalGranularityCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(GeographicalGranularityProperties.code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(GeographicalGranularityProperties.title().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalGranularity> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            GeographicalGranularityCriteriaOrderEnum propertyNameCriteria = GeographicalGranularityCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return GeographicalGranularityProperties.code();
                case TITLE:
                    return GeographicalGranularityProperties.title().texts().label();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalGranularity> retrievePropertyOrderDefault() throws MetamacException {
            return GeographicalGranularityProperties.id();
        }
    }

    // -------------------------------------------------------------------------------------
    // GEOGRAPHICAL VALUE
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<GeographicalValue> getGeographicalValueCriteriaMapper() {
        return geographicalValueCriteriaMapper;
    }

    private class GeographicalValueCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            GeographicalValueCriteriaPropertyEnum propertyNameCriteria = GeographicalValueCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.granularity().uuid(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case GEOGRAPHICAL_GRANULARITY_CODE:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.granularity().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case GEOGRAPHICAL_GRANULARITY_TITLE:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.granularity().title(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case UUID:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.uuid(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case CODE:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(GeographicalValueProperties.title().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalValue> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            GeographicalValueCriteriaOrderEnum propertyNameCriteria = GeographicalValueCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case UUID:
                    return GeographicalValueProperties.uuid();
                case GEOGRAPHICAL_GRANULARITY_UUID:
                    return GeographicalValueProperties.granularity();
                case ORDER:
                    return GeographicalValueProperties.order();
                case CODE:
                    return GeographicalValueProperties.code();
                case TITLE:
                    return GeographicalValueProperties.title().texts().label();
                case GEOGRAPHICAL_GRANULARITY_TITLE:
                    return GeographicalValueProperties.granularity().title().texts().label();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<GeographicalValue> retrievePropertyOrderDefault() throws MetamacException {
            return GeographicalValueProperties.id();
        }
    }

    // -------------------------------------------------------------------------------------
    // UNIT MULTIPLIER
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<UnitMultiplier> getUnitMultiplierCriteriaMapper() {
        return unitMultiplierCriteriaMapper;
    }

    private class UnitMultiplierCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            UnitMultiplierCriteriaPropertyEnum propertyNameCriteria = UnitMultiplierCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case UNIT_MULTIPLIER:
                    return new SculptorPropertyCriteria(UnitMultiplierProperties.unitMultiplier(), propertyRestriction.getIntegerValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(UnitMultiplierProperties.title().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<UnitMultiplier> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            UnitMultiplierCriteriaOrderEnum propertyNameCriteria = UnitMultiplierCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case UNIT_MULTIPLIER:
                    return UnitMultiplierProperties.unitMultiplier();
                case TITLE:
                    return UnitMultiplierProperties.title().texts().label();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<UnitMultiplier> retrievePropertyOrderDefault() throws MetamacException {
            return UnitMultiplierProperties.id();
        }
    }

    // -------------------------------------------------------------------------------------
    // QUANTITY UNIT
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<QuantityUnit> getQuantityUnitCriteriaMapper() {
        return quantityUnitCriteriaMapper;
    }

    private class QuantityUnitCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            QuantityUnitCriteriaPropertyEnum propertyNameCriteria = QuantityUnitCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case UUID:
                    return new SculptorPropertyCriteria(QuantityUnitProperties.uuid(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case SYMBOL_POSITION:
                    return new SculptorPropertyCriteria(QuantityUnitProperties.symbolPosition(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(QuantityUnitProperties.title().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<QuantityUnit> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            QuantityUnitCriteriaOrderEnum propertyNameCriteria = QuantityUnitCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case UUID:
                    return QuantityUnitProperties.uuid();
                case TITLE:
                    return QuantityUnitProperties.title().texts().label();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<QuantityUnit> retrievePropertyOrderDefault() throws MetamacException {
            return QuantityUnitProperties.id();
        }
    }

    // -------------------------------------------------------------------------------------
    // INDICATOR SYSTEM
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> getIndicatorsSystemVersionCriteriaMapper() {
        return indicatorsSystemVersionCriteriaMapper;
    }

    private class IndicatorsSystemVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorsSystemCriteriaPropertyEnum propertyNameCriteria = IndicatorsSystemCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(IndicatorsSystemVersionProperties.indicatorsSystem().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
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

    // -------------------------------------------------------------------------------------
    // INDICATOR
    // -------------------------------------------------------------------------------------

    @Override
    public MetamacCriteria2SculptorCriteria<IndicatorVersion> getIndicatorVersionCriteriaMapper() {
        return indicatorVersionCriteriaMapper;
    }

    private class IndicatorVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            IndicatorCriteriaPropertyEnum propertyNameCriteria = IndicatorCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case SUBJECT_CODE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.subjectCode(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.title().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case PRODUCTION_PROC_STATUS:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().productionProcStatus(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case DIFFUSION_PROC_STATUS:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().diffusionProcStatus(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            IndicatorCriteriaOrderEnum propertyOrderEnum = IndicatorCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return IndicatorVersionProperties.indicator().code();
                case TITLE:
                    return IndicatorVersionProperties.title().texts().label();
                case PRODUCTION_VERSION:
                    return IndicatorVersionProperties.indicator().productionVersionNumber();
                case PRODUCTION_PROC_STATUS:
                    return IndicatorVersionProperties.indicator().productionProcStatus();
                case DIFFUSION_VERSION:
                    return IndicatorVersionProperties.indicator().diffusionVersionNumber();
                case DIFFUSION_PROC_STATUS:
                    return IndicatorVersionProperties.indicator().diffusionProcStatus();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<IndicatorVersion> retrievePropertyOrderDefault() throws MetamacException {
            return IndicatorVersionProperties.id();
        }
    }
}
