package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.DataBasic;

/**
 * Repository implementation for DataGpe
 */
@Repository("dataGpeRepository")
public class DataGpeRepositoryImpl extends DataGpeRepositoryBase {
    public DataGpeRepositoryImpl() {
    }

    public List<DataBasic> findCurrentDataDefinitions() {
        List<DataBasic> finalResult = new ArrayList<DataBasic>();
        LocalDate now = LocalDate.fromCalendarFields(Calendar.getInstance());
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataBasic.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.lt("availableStartDate", now));
        criteria.add(Restrictions.isNull("availableEndDate"));
        
        criteria.addOrder(Order.asc("availableStartDate"));
        // find
        List<DataBasic> result = criteria.list();
        List<String> processed = new ArrayList<String>();
        
        //For every uuid we add only the last query, getting the first item
        //provided the list is sorted by date
        for (DataBasic dataBasic : result) {
            if (!processed.contains(dataBasic.getUuid())) {
                processed.add(dataBasic.getUuid());
                finalResult.add(dataBasic);
            }
        }
        
        return finalResult;
    }
    
    public DataBasic findCurrentDataDefinition(String uuid) {
        LocalDate now = LocalDate.fromCalendarFields(Calendar.getInstance());
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataBasic.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.lt("availableStartDate", now));
        criteria.add(Restrictions.isNull("availableEndDate"));
        criteria.add(Restrictions.eq("uuid",uuid));
        
        criteria.addOrder(Order.asc("availableStartDate"));
        // first result is the current available query
        List<DataBasic> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
