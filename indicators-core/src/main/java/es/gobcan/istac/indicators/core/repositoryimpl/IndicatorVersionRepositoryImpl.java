package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;

/**
 * Repository implementation for IndicatorVersion
 */
@Repository("indicatorVersionRepository")
public class IndicatorVersionRepositoryImpl extends IndicatorVersionRepositoryBase {

    public IndicatorVersionRepositoryImpl() {
    }

    @Override
    public IndicatorVersion retrieveIndicatorVersion(String uuid, String versionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        parameters.put("versionNumber", versionNumber);
        List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.indicator.uuid = :uuid and iv.versionNumber = :versionNumber", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorVersion> findIndicatorsVersions(IndicatorStateEnum state) {
        
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session)getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorVersion.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (state != null) {
            criteria.add(Restrictions.eq("state", state));
        }
        criteria.addOrder(Order.asc("id"));
        
        // Find
        List<IndicatorVersion> result = criteria.list();
        return result;        
    }    
}
