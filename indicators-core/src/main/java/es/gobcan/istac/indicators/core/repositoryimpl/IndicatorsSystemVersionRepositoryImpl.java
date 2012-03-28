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
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.IndicatorsSystemCriteriaPropertyInternalEnum;

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
        List<IndicatorsSystemVersion> result = findByQuery("from IndicatorsSystemVersion isv where isv.indicatorsSystem.uuid = :uuid and isv.versionNumber = :versionNumber", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemsVersions(IndicatorsCriteria indicatorsCriteria) throws MetamacException {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorsSystemVersion.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (indicatorsCriteria != null && indicatorsCriteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : indicatorsCriteria.getConjunctionRestriction().getRestrictions()) {
                IndicatorsSystemCriteriaPropertyInternalEnum propertyName = IndicatorsSystemCriteriaPropertyInternalEnum.fromValue(propertyRestriction.getPropertyName());
                switch (propertyName) {
                    case CODE:
                        criteria.createAlias("indicatorsSystem", "is");
                        criteria.add(Restrictions.eq("is.code", propertyRestriction.getStringValue()).ignoreCase());
                        break;
                    case PROC_STATUS:
                        criteria.add(Restrictions.eq("procStatus", propertyRestriction.getEnumValue()));
                        break;
                    case IS_LAST_VERSION:
                        criteria.add(Restrictions.eq("isLastVersion", propertyRestriction.getBooleanValue()));
                        break;
                    case VERSION_NUMBER:
                        criteria.add(Restrictions.eq("versionNumber", propertyRestriction.getStringValue()));
                        break;
                    default:
                        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
                }
            }
        }
        criteria.addOrder(Order.asc("id"));

        // Find
        List<IndicatorsSystemVersion> result = criteria.list();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorsSystemVersion.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("procStatus", IndicatorsSystemProcStatusEnum.PUBLISHED));

        criteria.createAlias("childrenAllLevels", "children");
        criteria.createAlias("children.indicatorInstance", "indicatorInstance");
        criteria.createAlias("indicatorInstance.indicator", "indicator");
        criteria.add(Restrictions.like("indicator.uuid", indicatorUuid));

        criteria.addOrder(Order.asc("id"));
        
        // Find
        List<IndicatorsSystemVersion> result = criteria.list();
        return result;
    }
}
