package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemVersions(String uri, IndicatorsSystemStateEnum state) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (uri != null) {
            parameters.put("uri", uri);
        }
        if (state != null) {
            parameters.put("state", state);
        }
        StringBuilder query = new StringBuilder("from IndicatorsSystemVersion isv ");
        StringBuilder queryWhere = new StringBuilder(" where ");
        if (uri != null) {
            queryWhere.append(" isv.uri = :uri");
            queryWhere.append(" and ");
        }
        if (state != null) {
            queryWhere.append(" isv.state = :state ");
            queryWhere.append(" and ");
        }
        if (!queryWhere.toString().equals(" where ")) {
            query.append(StringUtils.removeEnd(queryWhere.toString(), " and "));
        }
        query.append("order by isv.id asc");
        List<IndicatorsSystemVersion> result = findByQuery(query.toString(), parameters);
        return result;
    }
}
