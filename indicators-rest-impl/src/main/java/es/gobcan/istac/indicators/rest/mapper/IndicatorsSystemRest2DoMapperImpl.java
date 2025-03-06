package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;

@Component
public class IndicatorsSystemRest2DoMapperImpl implements IndicatorsSystemRest2DoMapper {

    private final RestCriteria2SculptorCriteria<IndicatorsSystem> parser;

    public IndicatorsSystemRest2DoMapperImpl() {
        parser = new RestCriteria2SculptorCriteria<IndicatorsSystem>(IndicatorsSystem.class, IndicatorsSystemPropertyOrder.class, IndicatorsSystemPropertyRestriction.class,
                new IndicatorsSystemCriteriaCallback());
    }

    private String integerParam2String(Integer parameter) {
        return parameter != null ? parameter.toString() : null;
    }

    @Override
    public SculptorCriteria queryParams2SculptorCriteria(String q, String order, Integer limit, Integer offset) {
        return parser.restCriteriaToSculptorCriteria(q, order, integerParam2String(limit), integerParam2String(offset));
    }

    private enum IndicatorsSystemPropertyOrder {
        ID
    }

    public enum IndicatorsSystemPropertyRestriction {
        OPERATIONAL, ID
    }

    private class IndicatorsSystemCriteriaCallback implements RestCriteria2SculptorCriteria.CriteriaCallback {

        private RestException createInvalidParameterException(String parameter) throws RestException {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.PARAMETER_INCORRECT, parameter);
            return new RestException(exception, Response.Status.INTERNAL_SERVER_ERROR);
        }

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            IndicatorsSystemPropertyRestriction propertyNameCriteria = IndicatorsSystemPropertyRestriction.valueOf(propertyRestriction.getPropertyName());
            String value = propertyRestriction.getValue();
            switch (propertyNameCriteria) {

                case ID: {
                    if (propertyRestriction.getValue() != null) {
                        return new SculptorPropertyCriteria(IndicatorsSystemVersionProperties.indicatorsSystem().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                    } else if (propertyRestriction.getValueList() != null) {
                        List<Object> valueList = new ArrayList<Object>(propertyRestriction.getValueList());
                        return new SculptorPropertyCriteria(IndicatorsSystemVersionProperties.indicatorsSystem().code(), valueList, propertyRestriction.getOperationType());
                    }
                }

                case OPERATIONAL: {
                    return new SculptorPropertyCriteria(IndicatorsSystemVersionProperties.indicatorsSystem().isOperational(), Boolean.valueOf(value), propertyRestriction.getOperationType());
                }

            }
            throw createInvalidParameterException("q");
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            IndicatorsSystemPropertyOrder propertyNameCriteria = IndicatorsSystemPropertyOrder.valueOf(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID: {
                    return IndicatorsSystemVersionProperties.indicatorsSystem().code();
                }
            }
            throw createInvalidParameterException("order");
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return IndicatorsSystemVersionProperties.id();
        }
    }
}
