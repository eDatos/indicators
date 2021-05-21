package es.gobcan.istac.indicators.core.repositoryimpl;

import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.GRANULARITY_UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATOR_INSTANCE_UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATOR_VERSION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalGranularityVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;

/**
 * Repository implementation for IndicatorVersionGeoCoverage
 */
@Repository("indicatorVersionGeoCoverageRepository")
public class IndicatorVersionGeoCoverageRepositoryImpl extends IndicatorVersionGeoCoverageRepositoryBase {

    public IndicatorVersionGeoCoverageRepositoryImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GeographicalGranularity> retrieveGranularityCoverage(IndicatorVersion indicatorVersion) {

        String queryHql = "select distinct(geoCoverage.geographicalValue.granularity) ";
        queryHql += "from IndicatorVersionGeoCoverage geoCoverage ";
        queryHql += "where geoCoverage.indicatorVersion = :indicatorVersion";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_VERSION, indicatorVersion);

        return query.getResultList();
    }

    @Override
    public List<GeographicalCodeVO> retrieveCodeCoverage(IndicatorVersion indicatorVersion) {
        String sql = "select geoval.code as geoCode, geoGra.code as granularityCode ";
        sql += "from tb_ind_version_geo_cov geoCov, tb_lis_geogr_values geoVal, ";
        sql += "tb_lis_geogr_granularities geoGra ";
        sql += "where geocov.indicator_version_fk = " + indicatorVersion.getId() + " ";
        sql += "and geoVal.id = geocov.geographical_value_fk ";
        sql += "and geoval.granularity_fk = geoGra.id ";

        Query query = getEntityManager().createNativeQuery(sql);

        return extractGeographicalCodes(query.getResultList());
    }

    @Override
    public List<GeographicalValueVO> retrieveCoverage(IndicatorVersion indicatorVersion) {
        String sql = "select geoval.code as geoCode, geoval.global_order as globalOrder, loc.locale as geoLocale, loc.label as geoLabel, ";
        sql += "geoGra.code as granularityCode, locGra.locale as granularityLocale, locGra.label as granularityLabel, geoval.latitude as latitude, geoval.longitude as longitude ";
        sql += "from tb_ind_version_geo_cov geoCov, tb_localised_strings loc, tb_lis_geogr_values geoVal, ";
        sql += "tb_lis_geogr_granularities geoGra, tb_localised_strings locGra ";
        sql += "where geocov.indicator_version_fk = " + indicatorVersion.getId() + " ";
        sql += "and geoVal.id = geocov.geographical_value_fk ";
        sql += "and geoval.granularity_fk = geoGra.id ";
        sql += "and loc.international_string_fk = geoval.title_fk ";
        sql += "and locGra.international_string_fk = geoGra.title_fk ";
        sql += "order by geoval.global_order ";

        Query query = getEntityManager().createNativeQuery(sql);

        List<GeographicalValueVO> values = extractGeographicalValueVO(query.getResultList());
        return values;
    }

    @Override
    public List<GeographicalCodeVO> retrieveCodeCoverageFilteredByGranularity(IndicatorVersion indicatorVersion, String geographicalGranularityUuid) {
        String queryHql = "select geoCoverage.geographicalValue.code,  geoCoverage.geographicalValue.granularity.code ";
        queryHql += "from IndicatorVersionGeoCoverage geoCoverage ";
        queryHql += "where geoCoverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and geoCoverage.geographicalValue.granularity.uuid = :granularityUuid ";
        queryHql += "order by geoCoverage.geographicalValue.order";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_VERSION, indicatorVersion);
        query.setParameter(GRANULARITY_UUID, geographicalGranularityUuid);

        return extractGeographicalCodes(query.getResultList());
    }

    @Override
    public List<GeographicalValueVO> retrieveCoverageFilteredByGranularity(IndicatorVersion indicatorVersion, String geographicalGranularityUuid) {
        String sql = "select geoval.code as geoCode, geoval.global_order as globalOrder, loc.locale as geoLocale, loc.label as geoLabel, ";
        sql += "geoGra.code as granularityCode, locGra.locale as granularityLocale, locGra.label as granularityLabel, geoval.latitude as latitude, geoval.longitude as longitude ";
        sql += "from tb_ind_version_geo_cov geoCov, tb_localised_strings loc, tb_lis_geogr_values geoVal, ";
        sql += "tb_lis_geogr_granularities geoGra, tb_localised_strings locGra ";
        sql += "where geocov.indicator_version_fk = " + indicatorVersion.getId() + " ";
        sql += "and geoVal.id = geocov.geographical_value_fk ";
        sql += "and geoval.granularity_fk = geoGra.id ";
        sql += "and geoGra.uuid = '" + geographicalGranularityUuid + "' ";
        sql += "and loc.international_string_fk = geoval.title_fk ";
        sql += "and locGra.international_string_fk = geoGra.title_fk ";
        sql += "order by geoval.global_order ";

        Query query = getEntityManager().createNativeQuery(sql);
        return extractGeographicalValueVO(query.getResultList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GeographicalValue> retrieveValueCoverageFilteredByGranularity(IndicatorVersion indicatorVersion, String geographicalGranularityUuid) {
        String hql = "select geoCov.geographicalValue ";
        hql += "from IndicatorVersionGeoCoverage geoCov ";
        hql += "where  geoCov.indicatorVersion = :indicatorVersion ";
        hql += "and geoCov.geographicalValue.granularity.uuid = :granularityUuid ";
        hql += "order by geoCov.geographicalValue.order ";

        Query query = getEntityManager().createQuery(hql);
        query.setParameter(INDICATOR_VERSION, indicatorVersion);
        query.setParameter(GRANULARITY_UUID, geographicalGranularityUuid);

        return query.getResultList();
    }

    @Override
    public List<GeographicalCodeVO> retrieveCodeCoverageFilteredByInstanceGeoValues(IndicatorVersion indicatorVersion, String indicatorInstanceUuid) {
        String queryHql = "select geoCoverage.geographicalValue.code,  geoCoverage.geographicalValue.granularity.code ";
        queryHql += "from IndicatorVersionGeoCoverage geoCoverage, IndicatorInstance instance inner join instance.geographicalValues as instanceGeoValue ";
        queryHql += "where geoCoverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and instance.uuid = :indicatorInstanceUuid ";
        queryHql += "and geoCoverage.geographicalValue = instanceGeoValue ";
        queryHql += "order by geoCoverage.geographicalValue.order";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_VERSION, indicatorVersion);
        query.setParameter(INDICATOR_INSTANCE_UUID, indicatorInstanceUuid);

        return extractGeographicalCodes(query.getResultList());
    }

    @Override
    public List<GeographicalValueVO> retrieveCoverageFilteredByInstanceGeoValues(IndicatorVersion indicatorVersion, String indicatorInstanceUuid) {
        String sql = "select geoval.code as geoCode, geoval.global_order as globalOrder, loc.locale as geoLocale, loc.label as geoLabel, ";
        sql += "geoGra.code as granularityCode, locGra.locale as granularityLocale, locGra.label as granularityLabel, geoval.latitude as latitude, geoval.longitude as longitude ";
        sql += "from tb_ind_version_geo_cov geoCov, tb_localised_strings loc, tb_lis_geogr_values geoVal, ";
        sql += "tb_lis_geogr_granularities geoGra, tb_localised_strings locGra, tb_indicators_instances instance, tb_indic_inst_geo_values instance_geo ";
        sql += "where geocov.indicator_version_fk = " + indicatorVersion.getId() + " ";
        sql += "and instance.uuid = '" + indicatorInstanceUuid + "' ";
        sql += "and instance_geo.indicator_instance_fk = instance.id ";
        sql += "and geoVal.id = geocov.geographical_value_fk ";
        sql += "and geoVal.id = instance_geo.geographical_value_fk ";
        sql += "and geoval.granularity_fk = geoGra.id ";
        sql += "and loc.international_string_fk = geoval.title_fk ";
        sql += "and locGra.international_string_fk = geoGra.title_fk ";
        sql += "order by geoval.global_order ";

        Query query = getEntityManager().createNativeQuery(sql);

        return extractGeographicalValueVO(query.getResultList());
    }

    @Override
    public List<GeographicalCodeVO> retrieveCodeCoverageFilteredByInstanceGeoValuesByGranularity(IndicatorVersion indicatorVersion, String indicatorInstanceUuid, String geographicalGranularityUuid) {
        String queryHql = "select geoCoverage.geographicalValue.code, geoCoverage.geographicalValue.granularity.code ";
        queryHql += "from IndicatorVersionGeoCoverage geoCoverage, IndicatorInstance instance inner join instance.geographicalValues as instanceGeoValue ";
        queryHql += "where geoCoverage.indicatorVersion = :indicatorVersion ";
        queryHql += "and instance.uuid = :indicatorInstanceUuid ";
        queryHql += "and geoCoverage.geographicalValue.granularity.uuid = :granularityUuid ";
        queryHql += "and geoCoverage.geographicalValue = instanceGeoValue ";
        queryHql += "order by geoCoverage.geographicalValue.order";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_VERSION, indicatorVersion);
        query.setParameter(INDICATOR_INSTANCE_UUID, indicatorInstanceUuid);
        query.setParameter(GRANULARITY_UUID, geographicalGranularityUuid);

        return extractGeographicalCodes(query.getResultList());
    }

    @Override
    public List<GeographicalValueVO> retrieveCoverageFilteredByInstanceGeoValuesByGranularity(IndicatorVersion indicatorVersion, String indicatorInstanceUuid, String geographicalGranularityUuid) {

        String sql = "select geoval.code as geoCode, geoval.global_order as globalOrder, loc.locale as geoLocale, loc.label as geoLabel, ";
        sql += "geoGra.code as granularityCode, locGra.locale as granularityLocale, locGra.label as granularityLabel, geoval.latitude as latitude, geoval.longitude as longitude ";
        sql += "from tb_ind_version_geo_cov geoCov, tb_localised_strings loc, tb_lis_geogr_values geoVal, ";
        sql += "tb_lis_geogr_granularities geoGra, tb_localised_strings locGra, tb_indicators_instances instance, tb_indic_inst_geo_values instance_geo ";
        sql += "where geocov.indicator_version_fk = " + indicatorVersion.getId() + " ";
        sql += "and instance.uuid = '" + indicatorInstanceUuid + "' ";
        sql += "and instance_geo.indicator_instance_fk = instance.id ";
        sql += "and geoVal.id = geocov.geographical_value_fk ";
        sql += "and geoVal.id = instance_geo.geographical_value_fk ";
        sql += "and geoval.granularity_fk = geoGra.id ";
        sql += "and geoGra.uuid = '" + geographicalGranularityUuid + "' ";
        sql += "and loc.international_string_fk = geoval.title_fk ";
        sql += "and locGra.international_string_fk = geoGra.title_fk ";
        sql += "order by geoval.global_order ";

        Query query = getEntityManager().createNativeQuery(sql);

        return extractGeographicalValueVO(query.getResultList());
    }

    @Override
    public void deleteCoverageForIndicatorVersion(IndicatorVersion indicatorVersion) {
        Query query = getEntityManager().createQuery("delete IndicatorVersionGeoCoverage where indicatorVersion = :indicatorVersion");
        query.setParameter(INDICATOR_VERSION, indicatorVersion);
        query.executeUpdate();
    }

    private List<GeographicalCodeVO> extractGeographicalCodes(List<Object> resultList) {
        List<GeographicalCodeVO> codes = new ArrayList<GeographicalCodeVO>();
        for (Object result : resultList) {
            Object[] fields = (Object[]) result;
            String geoCode = (String) fields[0];
            String granularityCode = (String) fields[1];

            GeographicalCodeVO geo = new GeographicalCodeVO();
            geo.setCode(geoCode);
            geo.setGranularityCode(granularityCode);
            codes.add(geo);
        }
        return codes;
    }

    private List<GeographicalValueVO> extractGeographicalValueVO(List<Object> resultList) {
        Map<String, GeographicalValueVO> geoValues = new LinkedHashMap<String, GeographicalValueVO>();
        List<GeographicalValueVO> values = new ArrayList<GeographicalValueVO>();
        for (Object result : resultList) {
            Object[] fields = (Object[]) result;
            String geoCode = (String) fields[0];
            String geoOrder = (String) fields[1];
            String geoLocale = (String) fields[2];
            String geoLabel = (String) fields[3];
            String granularityCode = (String) fields[4];
            String granularityLocale = (String) fields[5];
            String granularityLabel = (String) fields[6];
            BigDecimal latitude = toBigDecimal(fields[7]);
            BigDecimal longitude = toBigDecimal(fields[8]);

            GeographicalValueVO geoVal = geoValues.get(geoCode);
            GeographicalGranularityVO granularity = null;

            if (geoVal == null) {
                geoVal = new GeographicalValueVO();
                geoVal.setCode(geoCode);
                geoVal.setOrder(geoOrder);
                geoVal.setTitle(new InternationalString());
                if (latitude != null) {
                    geoVal.setLatitude(latitude.doubleValue());
                }
                if (longitude != null) {
                    geoVal.setLongitude(longitude.doubleValue());
                }
                granularity = new GeographicalGranularityVO();
                granularity.setCode(granularityCode);
                granularity.setTitle(new InternationalString());
                geoVal.setGranularity(granularity);

                geoValues.put(geoCode, geoVal);
                values.add(geoVal);
            }

            granularity = geoVal.getGranularity();

            if (geoVal.getTitle().getLocalisedLabel(geoLocale) == null) {
                LocalisedString localised = new LocalisedString();
                localised.setLabel(geoLabel);
                localised.setLocale(geoLocale);
                geoVal.getTitle().addText(localised);
            }

            if (granularity.getTitle().getLocalisedLabel(granularityLocale) == null) {
                LocalisedString localised = new LocalisedString();
                localised.setLabel(granularityLabel);
                localised.setLocale(granularityLocale);
                granularity.getTitle().addText(localised);
            }
        }
        return values;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }
        return null;
    }
}
