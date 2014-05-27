package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;

@Component
public class IndicatorsRest2DoMapperImpl implements IndicatorsRest2DoMapper {

    private final RestCriteria2SculptorCriteria<IndicatorVersion> parser;

    public IndicatorsRest2DoMapperImpl() {
        parser = new RestCriteria2SculptorCriteria<IndicatorVersion>(IndicatorVersion.class, IndicatorsPropertyOrder.class, IndicatorsPropertyRestriction.class, new IndicatorsCriteriaCallback());
    }

    private String integerParam2String(Integer parameter) {
        return parameter != null ? parameter.toString() : null;
    }

    @Override
    public SculptorCriteria queryParams2SculptorCriteria(String q, String order, Integer limit, Integer offset) {
        return parser.restCriteriaToSculptorCriteria(q, order, integerParam2String(limit), integerParam2String(offset));
    }

    private enum IndicatorsPropertyOrder {
        update, id
    }

    public enum IndicatorsPropertyRestriction {
        geographicalValue, subjectCode, id
    }

    private class IndicatorsCriteriaCallback implements RestCriteria2SculptorCriteria.CriteriaCallback {

        private RestException createInvalidParameterException(String parameter) throws RestException {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.PARAMETER_INCORRECT, parameter);
            return new RestException(exception, Response.Status.INTERNAL_SERVER_ERROR);
        }

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            IndicatorsPropertyRestriction propertyNameCriteria = IndicatorsPropertyRestriction.valueOf(propertyRestriction.getPropertyName());
            String value = propertyRestriction.getValue();
            switch (propertyNameCriteria) {
                case subjectCode: {
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.subjectCode(), value, propertyRestriction.getOperationType());
                }
                case id: {
                    if (propertyRestriction.getValue() != null) {
                        return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                    } else if (propertyRestriction.getValueList() != null) {
                        List<Object> valueList = new ArrayList<Object>(propertyRestriction.getValueList());
                        return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), valueList, propertyRestriction.getOperationType());
                    }
                }

                case geographicalValue: {
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.lastValuesCache().geographicalCode(), value, propertyRestriction.getOperationType());
                }
            }
            throw createInvalidParameterException("q");
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            IndicatorsPropertyOrder propertyNameCriteria = IndicatorsPropertyOrder.valueOf(order.getPropertyName());
            switch (propertyNameCriteria) {
                case update: {
                    return new LeafProperty<IndicatorVersion>(IndicatorVersionProperties.lastUpdated().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, IndicatorVersion.class);
                }
                case id: {
                    return IndicatorVersionProperties.id();
                }
            }
            throw createInvalidParameterException("order");
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return IndicatorVersionProperties.id();
        }
    }
}
