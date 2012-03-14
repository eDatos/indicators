package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.criteria.GeographicalValueCriteriaPropertyInternalEnum;

/**
 * Repository implementation for GeographicalValue
 */
@Repository("geographicalValueRepository")
public class GeographicalValueRepositoryImpl extends GeographicalValueRepositoryBase {

    public GeographicalValueRepositoryImpl() {
    }

    public GeographicalValue retrieveGeographicalValue(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<GeographicalValue> result = findByQuery("from GeographicalValue gv where gv.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GeographicalValue> findGeographicalValues(IndicatorsCriteria indicatorsCriteria) throws MetamacException {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(GeographicalValue.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (indicatorsCriteria != null && indicatorsCriteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : indicatorsCriteria.getConjunctionRestriction().getRestrictions()) {
                GeographicalValueCriteriaPropertyInternalEnum propertyName = GeographicalValueCriteriaPropertyInternalEnum.fromValue(propertyRestriction.getPropertyName());
                switch (propertyName) {
                    case GEOGRAPHICAL_GRANULARITY_UUID:
                        criteria.createAlias("granularity", "gr");
                        criteria.add(Restrictions.eq("gr.uuid", propertyRestriction.getStringValue()).ignoreCase());
                        break;
                    default:
                        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
                }
            }
        }
        criteria.addOrder(Order.asc("id"));

        // Find
        List<GeographicalValue> result = criteria.list();
        return result;
    }
}
