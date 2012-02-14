package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Indicator;

/**
 * Repository implementation for Indicator
 */
@Repository("indicatorRepository")
public class IndicatorRepositoryImpl extends IndicatorRepositoryBase {

    public IndicatorRepositoryImpl() {
    }

    public Indicator retrieveIndicator(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<Indicator> result = findByQuery("from Indicator i where i.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    // TODO paginación
    @SuppressWarnings("unchecked")
    @Override
    public List<Indicator> findIndicators(String code) {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(Indicator.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (code != null) {
            criteria.add(Restrictions.eq("code", code).ignoreCase());
        }
        criteria.addOrder(Order.asc("id"));

        // Find
        List<Indicator> result = criteria.list();
        return result;
    }
}
