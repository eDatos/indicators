package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;

/**
 * Repository implementation for IndicatorsSystem
 */
@Repository("indicatorsSystemRepository")
public class IndicatorsSystemRepositoryImpl extends IndicatorsSystemRepositoryBase {

    public IndicatorsSystemRepositoryImpl() {
    }

    public IndicatorsSystem retrieveIndicatorsSystem(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<IndicatorsSystem> result = findByQuery("from IndicatorsSystem i where i.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorsSystem> findIndicatorsSystems(String code) {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorsSystem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (code != null) {
            criteria.add(Restrictions.eq("code", code).ignoreCase());
        }
        criteria.addOrder(Order.asc("id"));

        // Find
        List<IndicatorsSystem> result = criteria.list();
        return result;
    }
}
