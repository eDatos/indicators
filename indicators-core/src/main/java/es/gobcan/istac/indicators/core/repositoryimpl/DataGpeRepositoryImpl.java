package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.DataDefinition;

/**
 * Repository implementation for DataGpe
 */
@Repository("dataGpeRepository")
public class DataGpeRepositoryImpl extends DataGpeRepositoryBase {
    public DataGpeRepositoryImpl() {
    }

    public List<DataDefinition> findCurrentDataDefinitions() {
        List<DataDefinition> finalResult = new ArrayList<DataDefinition>();
        Date now = Calendar.getInstance().getTime();
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataDefinition.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.lt("availableStartDate", now));
        criteria.add(Restrictions.isNull("availableEndDate"));
        
        criteria.addOrder(Order.asc("availableStartDate"));
        // find
        List<DataDefinition> result = criteria.list();
        List<String> processed = new ArrayList<String>();
        
        //For every uuid we add only the last query, getting the first item
        //provided the list is sorted by date
        for (DataDefinition dataDefinition: result) {
            if (!processed.contains(dataDefinition.getUuid())) {
                processed.add(dataDefinition.getUuid());
                finalResult.add(dataDefinition);
            }
        }
        
        return finalResult;
    }
    
    public DataDefinition findCurrentDataDefinition(String uuid) {
        Date now = Calendar.getInstance().getTime();
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataDefinition.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.lt("availableStartDate", now));
        criteria.add(Restrictions.isNull("availableEndDate"));
        criteria.add(Restrictions.eq("uuid",uuid));
        
        criteria.addOrder(Order.asc("availableStartDate"));
        // first result is the current available query
        List<DataDefinition> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
