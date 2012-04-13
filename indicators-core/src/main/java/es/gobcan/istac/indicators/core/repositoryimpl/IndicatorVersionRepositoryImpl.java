package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.IndicatorCriteriaPropertyInternalEnum;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

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
    public List<IndicatorVersion> findIndicatorsVersions(IndicatorsCriteria indicatorsCriteria) throws MetamacException {

        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(IndicatorVersion.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (indicatorsCriteria != null && indicatorsCriteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : indicatorsCriteria.getConjunctionRestriction().getRestrictions()) {
                IndicatorCriteriaPropertyInternalEnum propertyName = IndicatorCriteriaPropertyInternalEnum.fromValue(propertyRestriction.getPropertyName());
                switch (propertyName) {
                    case CODE:
                        criteria.createAlias("indicator", "ind");
                        criteria.add(Restrictions.eq("ind.code", propertyRestriction.getStringValue()).ignoreCase());
                        break;
                    case NEEDS_UPDATE:
                        criteria.add(Restrictions.eq("ind.needsUpdate", propertyRestriction.getBooleanValue()));
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
                    case SUBJECT_CODE:
                        criteria.add(Restrictions.eq("subjectCode", propertyRestriction.getStringValue()));
                        break;
                    default:
                        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyName);
                }
            }
        }
        criteria.addOrder(Order.asc("id"));

        // Find
        List<IndicatorVersion> result = criteria.list();
        return result;
    }

    @Override
    public IndicatorVersion findOneIndicatorVersionLinkedToIndicator(String indicatorUuid) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", indicatorUuid);

        // Important! Queries must be executed separately because indicator can have different value of indicator in numerator, denominator..

        // Numerator
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.numerator.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        // Denominator
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.denominator.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        // Base quantity
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.baseQuantity.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SubjectIndicatorResult> findSubjectsInPublishedIndicators() throws MetamacException {
        Query query = getEntityManager().createQuery("select iv.subjectCode, min(iv.subjectTitle) from IndicatorVersion iv where iv.procStatus = :procStatus group by iv.subjectCode");
        query.setParameter("procStatus", IndicatorProcStatusEnum.PUBLISHED);
        List<Object> results = query.getResultList();
        
        List<SubjectIndicatorResult> subjectsResults = new ArrayList<SubjectIndicatorResult>();
        if (results != null) {
            for (Object result : results) {
                String subjectCode = (String)((Object[]) result)[0];
                InternationalString subjectTitle = (InternationalString)((Object[]) result)[1];
                
                SubjectIndicatorResult subjectIndicatorResult = new SubjectIndicatorResult();
                subjectIndicatorResult.setId(subjectCode);
                subjectIndicatorResult.setTitle(subjectTitle);
                subjectsResults.add(subjectIndicatorResult);
            }
        } 
        return subjectsResults;
    }
}
