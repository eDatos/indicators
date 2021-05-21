package es.gobcan.istac.indicators.core.service;

import static org.siemac.edatos.core.common.constants.shared.UrnConstants.COLON;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(StatisticalResoucesRestExternalService.BEAN_ID)
public class StatisticalResoucesRestExternalServiceImpl implements StatisticalResoucesRestExternalService {

    private static Logger        logger = LoggerFactory.getLogger(StatisticalResoucesRestExternalService.class);

    @Autowired
    private RestApiLocator       restApiLocator;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            return restApiLocator.getStatisticalResourcesRestExternalFacacadeV10().findQueries(query, orderBy, limit, offset, lang);
        } catch (Exception e) {
            logger.error("Unable to find Queries", e);
            throw toRestException(e);
        }
    }

    @Override
    public Query retrieveQueryByUrnInDefaultLang(String queryUrn, QueryFetchEnum onlyMetadata) {
        String languageDefault;
        try {
            languageDefault = configurationService.retrieveLanguageDefault();
            return retrieveQueryByUrn(queryUrn, Arrays.asList(languageDefault), onlyMetadata);
        } catch (MetamacException e) {
            logger.error("Unable to find Queries", e);
            throw toRestException(e);
        }
    }

    @Override
    public Query retrieveQueryByUrn(String queryUrn, List<String> lang, QueryFetchEnum onlyMetadata) {
        try {
            String universalIdentifier = UrnUtils.removePrefix(queryUrn);
            String agencyID = StringUtils.substringBefore(universalIdentifier, COLON.toString());
            String resourceID = StringUtils.substringAfter(universalIdentifier, COLON);

            String fields = null;
            switch (onlyMetadata) {
                case ONLY_DATA:
                    fields = "-metadata";
                    break;
                case ONLY_METADATA:
                    fields = "-data";
                    break;
                default:
                    break;
            }

            return restApiLocator.getStatisticalResourcesRestExternalFacacadeV10().retrieveQuery(agencyID, resourceID, lang, fields);
        } catch (Exception e) {
            logger.error("Unable to find Queries", e);
            throw toRestException(e);
        }
    }

    private RestException toRestException(Exception e) {
        throw toRestException(e);
    }
}
