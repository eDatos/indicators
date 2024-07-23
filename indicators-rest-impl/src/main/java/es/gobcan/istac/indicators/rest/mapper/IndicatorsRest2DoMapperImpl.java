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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.util.GeographicalValuesOldVersionCompatibilityUtils;

@Component
public class IndicatorsRest2DoMapperImpl implements IndicatorsRest2DoMapper {

    private static final Logger                                   log = LoggerFactory.getLogger(IndicatorsRest2DoMapperImpl.class);

    @Autowired
    private SrmRestInternalFacade                                 srmRestInternalFacade;

    @Autowired
    private IndicatorsConfigurationService                        configurationService;

    @Autowired
    GeographicalValuesRestFacade                                  geographicalValuesRestFacade;

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
        UPDATE, ID
    }

    // The words of a value of an enumerated should be separated by underscores. In this case, the values don't have the underscore so we don't have to change the API and associated documentation
    public enum IndicatorsPropertyRestriction {
        GEOGRAPHICALVALUE, SUBJECTCODE, ID, GEOGRAPHICALGRANULARITY, TEMPORALGRANULARITY
    }

    private class IndicatorsCriteriaCallback implements RestCriteria2SculptorCriteria.CriteriaCallback {

        private RestException createInvalidParameterException(String parameter) throws RestException {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.PARAMETER_INCORRECT, parameter);
            return new RestException(exception, Response.Status.INTERNAL_SERVER_ERROR);
        }

        private String getCategoryElementFromSubjectCode(String subjectCode) throws RestException {
            try {
                Category category = srmRestInternalFacade.retrieveCategoryByCode(configurationService.retrieveDefaultCategoryScheme(), subjectCode);

                if (category != null && category.getCategoryElement() != null) {
                    return category.getCategoryElement().getId();
                }
            } catch (Exception e) {
                log.error("category element linked to category (subjectCode) not found in srm resource", e);
                throw createInvalidParameterException("q->SUBJECT_CODE->CATEGORY_ELEMENT");
            }
            return null;
        }

        private String getGeographicalValue(String geographicalValue) throws RestException {
            try {
                GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = new GeographicalValuesOldVersionCompatibilityUtils();

                return geoValuesOldVersionCompatibilityUtils.setGeographicalValue(geographicalValuesRestFacade, srmRestInternalFacade, geographicalValue,
                        configurationService.retrieveDefaultTerritoryVariable());

            } catch (Exception e) {
                log.error("An unexpected error ocurred searching geographical value. ", e);
            }
            return geographicalValue;
        }

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            IndicatorsPropertyRestriction propertyNameCriteria = IndicatorsPropertyRestriction.valueOf(propertyRestriction.getPropertyName());
            String value = propertyRestriction.getValue();
            switch (propertyNameCriteria) {
                case SUBJECTCODE: {
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.categoryElement().code(), getCategoryElementFromSubjectCode(value), propertyRestriction.getOperationType());
                }
                case ID: {
                    if (propertyRestriction.getValue() != null) {
                        return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                    } else if (propertyRestriction.getValueList() != null) {
                        List<Object> valueList = new ArrayList<Object>(propertyRestriction.getValueList());
                        return new SculptorPropertyCriteria(IndicatorVersionProperties.indicator().code(), valueList, propertyRestriction.getOperationType());
                    }
                }

                case GEOGRAPHICALVALUE: {
                    // We can use "lastValuesCache" because this cache have all the geographicalValues of the indicator with the lastData for each value.
                    // The lastValue for geocode01 and geocode02 can be different points of time.
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.lastValuesCache().geographicalCode(), getGeographicalValue(value), propertyRestriction.getOperationType());
                }
                case GEOGRAPHICALGRANULARITY: {
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.geoCoverages().geographicalValue().granularity().code(), value, propertyRestriction.getOperationType());
                }
                case TEMPORALGRANULARITY:
                    return new SculptorPropertyCriteria(IndicatorVersionProperties.timeCoverages().timeGranularity(), value, propertyRestriction.getOperationType());
            }
            throw createInvalidParameterException("q");
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            IndicatorsPropertyOrder propertyNameCriteria = IndicatorsPropertyOrder.valueOf(order.getPropertyName());
            switch (propertyNameCriteria) {
                case UPDATE: {
                    return new LeafProperty<IndicatorVersion>(IndicatorVersionProperties.lastPopulateDate().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true,
                            IndicatorVersion.class);
                }
                case ID: {
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
