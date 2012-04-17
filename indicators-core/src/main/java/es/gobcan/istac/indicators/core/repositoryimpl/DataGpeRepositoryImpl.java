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
 * TODO utilizar criteria de sculptor o cambiarla a HQL
 */
@Repository("dataGpeRepository")
public class DataGpeRepositoryImpl extends DataGpeRepositoryBase {
    public DataGpeRepositoryImpl() {
    }

    public List<DataDefinition> findCurrentDataDefinitions() {
        Date now = Calendar.getInstance().getTime();
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataDefinition.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        //filter future published 
        criteria.add(Restrictions.lt("availableStartDate", now));
        //filter archived
        criteria.add(Restrictions.isNull("availableEndDate"));
        
        criteria.addOrder(Order.asc("uuid"));
        criteria.addOrder(Order.asc("availableStartDate"));
        // find
        List<DataDefinition> result = criteria.list();
        
        return filterNotAvailableDataDefinitions(result);
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
    @Override
    public List<String> findDataDefinitionsWithDataUpdatedAfter(Date date) {
        Date now = Calendar.getInstance().getTime();
        // Criteria
        org.hibernate.Session session = (org.hibernate.Session) getEntityManager().getDelegate();
        
        Criteria criteria = session.createCriteria(DataDefinition.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        //data has been updated?
        criteria.add(Restrictions.gt("dataUpdateDate", date));
        //is available right now?
        criteria.add(Restrictions.lt("availableStartDate", now));
        criteria.add(Restrictions.isNull("availableEndDate"));
        
        criteria.addOrder(Order.asc("uuid"));
        criteria.addOrder(Order.asc("availableStartDate"));
        
        // first result is the current available query
        List<DataDefinition> result = criteria.list();
        List<DataDefinition> dataDefinitions = filterNotAvailableDataDefinitions(result);
        List<String> dataDefinitionsUuids = new ArrayList<String>();
        for (DataDefinition dataDefinition : dataDefinitions) {
            dataDefinitionsUuids.add(dataDefinition.getUuid());
        }
        return dataDefinitionsUuids;
    }
    
    private List<DataDefinition> filterNotAvailableDataDefinitions(List<DataDefinition> dataDefinitions) {
        List<String> processed = new ArrayList<String>();
        List<DataDefinition> finalResult = new ArrayList<DataDefinition>();
        //For every uuid we add only the last query, getting the first item
        //provided the list is sorted by date
        for (DataDefinition dataDefinition: dataDefinitions) {
            if (!processed.contains(dataDefinition.getUuid())) {
                processed.add(dataDefinition.getUuid());
                finalResult.add(dataDefinition);
            }
        }
        
        return finalResult;
    }
}
