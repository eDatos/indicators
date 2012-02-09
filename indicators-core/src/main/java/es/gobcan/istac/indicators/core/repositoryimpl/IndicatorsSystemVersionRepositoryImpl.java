package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;

/**
 * Repository implementation for IndicatorsSystemVersion
 */
@Repository("indicatorsSystemVersionRepository")
public class IndicatorsSystemVersionRepositoryImpl extends IndicatorsSystemVersionRepositoryBase {

    public IndicatorsSystemVersionRepositoryImpl() {
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemVersion(String uuid, String versionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        parameters.put("versionNumber", versionNumber);
        List<IndicatorsSystemVersion> result = findByQuery("from IndicatorsSystemVersion isv where isv.indicatorsSystem.uuid = :uuid and isv.versionNumber = :versionNumber", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemVersions(String uri, IndicatorsSystemStateEnum state) {
        
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session)getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorsSystemVersion.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (uri != null) {
            criteria.add(Restrictions.eq("uri", uri));
        }
        if (state != null) {
            criteria.add(Restrictions.eq("state", state));
        }
        criteria.addOrder(Order.asc("id"));
        
        // Find
        List<IndicatorsSystemVersion> result = criteria.list();
        return result;        
    }
}
