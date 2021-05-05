package es.gobcan.istac.indicators.web.server.utils;

import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.appendConditionToQuery;
import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.fieldComparison;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.QueryCriteriaPropertyRestriction;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasSimpleCriteria;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.QuantityUnitCriteriaPropertyEnum;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class MetamacWebCriteriaUtils {

    public static MetamacCriteriaRestriction buildMetamacCriteriaFromWebcriteria(IndicatorCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction indicatorCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                indicatorCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                indicatorCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
                indicatorCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(indicatorCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getTitle())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), criteria.getTitle(), OperationType.ILIKE));
            }

            if (criteria.getProductionVersionProcStatus() != null) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.PRODUCTION_PROC_STATUS.name(), criteria.getProductionVersionProcStatus(), OperationType.EQ));
            }

            if (criteria.getDiffusionVersionProcStatus() != null) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.DIFFUSION_PROC_STATUS.name(), criteria.getDiffusionVersionProcStatus(), OperationType.EQ));
            }

            if (!StringUtils.isBlank(criteria.getSubjectCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), criteria.getSubjectCode(), OperationType.EQ));
            }

            if (criteria.getNotifyPopulationErrors() != null) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.NOTIFY_POPULATION_ERRORS.name(), criteria.getNotifyPopulationErrors(), OperationType.EQ));
            }
        }

        return conjunctionRestriction;
    }

    public static MetamacCriteriaRestriction buildMetamacCriteriaFromWebcriteria(GeoValueCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction geoValueCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                geoValueCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.UUID.name(), criteria.getCriteria(), OperationType.ILIKE));
                geoValueCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                geoValueCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
                geoValueCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(geoValueCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getGranularityCode())) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), criteria.getGranularityCode(), OperationType.EQ));
            }
        }

        return conjunctionRestriction;
    }

    public static MetamacCriteriaRestriction buildMetamacCriteriaFromWebcriteria(QuantityUnitCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction quantityUnitCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                quantityUnitCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(QuantityUnitCriteriaPropertyEnum.UUID.name(), criteria.getCriteria(), OperationType.ILIKE));
                quantityUnitCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(QuantityUnitCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(quantityUnitCriteriaDisjuction);
        }

        return conjunctionRestriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // STATISTICAL OPERATION
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryStatisticalOperation(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, OperationCriteriaPropertyRestriction.TITLE, OperationCriteriaPropertyRestriction.ID);
        }
        return queryBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    private static void addSimpleRestCriteria(StringBuilder queryBuilder, HasSimpleCriteria criteria, Enum... fields) {
        String simpleCriteria = criteria.getCriteria();
        if (StringUtils.isNotBlank(simpleCriteria)) {
            StringBuilder conditionBuilder = new StringBuilder();

            List<String> conditions = new ArrayList<String>();
            for (Enum field : fields) {
                conditions.add(fieldComparison(field, ComparisonOperator.ILIKE, simpleCriteria));
            }

            conditionBuilder.append("(");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    conditionBuilder.append(" ").append(LogicalOperator.OR).append(" ");
                }
                conditionBuilder.append(conditions.get(i));
            }
            conditionBuilder.append(")");
            appendConditionToQuery(queryBuilder, conditionBuilder.toString());
        }
    }

    // -------------------------------------------------------------------------------------------------------------
    // QUERY
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryForQueryVersion(MetamacWebCriteria webCriteria) {

        QueryWebCriteria queryWebCriteria = (QueryWebCriteria) webCriteria;

        StringBuilder queryBuilder = new StringBuilder();
        if (queryWebCriteria != null) {

            addSimpleRestCriteria(queryBuilder, webCriteria, QueryCriteriaPropertyRestriction.NAME, QueryCriteriaPropertyRestriction.ID);

            String statisticalOperationUrn = queryWebCriteria.getStatisticalOperationUrn();
            if (StringUtils.isNotBlank(statisticalOperationUrn)) {
                String schemeCondition = fieldComparison(QueryCriteriaPropertyRestriction.STATISTICAL_OPERATION_URN, ComparisonOperator.EQ, statisticalOperationUrn);
                appendConditionToQuery(queryBuilder, schemeCondition);
            }

            // // Only published
            // String dsdCodeCondition = fieldComparison(QueryCriteriaPropertyRestriction.PROC_STATUS, ComparisonOperator.EQ, ProcStatusType.PUBLISHED);
            // appendConditionToQuery(queryBuilder, dsdCodeCondition);

        }

        return queryBuilder.toString();
    }

}
