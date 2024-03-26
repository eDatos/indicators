package es.gobcan.istac.indicators.rest.clients;

import java.util.List;

import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestExternalService;
import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestExternalService.QueryFetchEnum;

@Component(StatisticalResourceRestExternalFacade.BEAN_ID)
public class StatisticalResourceRestExternalFacadeImpl implements StatisticalResourceRestExternalFacade {

    @Autowired
    private StatisticalResoucesRestExternalService statisticalResoucesRestExternalService;

    @Override
    public Query retrieveQueryByUrn(String queryUrn, List<String> lang, QueryFetchEnum onlyMetadata) {
        return statisticalResoucesRestExternalService.retrieveQueryByUrn(queryUrn, lang, onlyMetadata);
    }
}
