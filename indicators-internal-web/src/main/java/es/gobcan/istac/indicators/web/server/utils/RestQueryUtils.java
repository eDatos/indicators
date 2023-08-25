package es.gobcan.istac.indicators.web.server.utils;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public class RestQueryUtils {

    //
    // CATEGORY SCHEME
    //

    public static String buildCategorySchemeQuery(SrmExternalResourceRestCriteria itemSchemeRestCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        String criteria = itemSchemeRestCriteria.getCriteria();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(");
            queryBuilder.append(CategorySchemeCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CategorySchemeCriteriaPropertyRestriction.NAME).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CategorySchemeCriteriaPropertyRestriction.URN).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(")");
        }

        // Only last versions
        String lastVersionQuery = buildIsLastVersionQuery(CategorySchemeCriteriaPropertyRestriction.LATEST, itemSchemeRestCriteria.isOnlyLastVersion());
        addConditionToQueryBuilderIfAny(queryBuilder, lastVersionQuery, LogicalOperator.AND);

        return queryBuilder.toString();
    }

    //
    // CATEGORY
    //

    public static String buildCategoryQuery(SrmItemRestCriteria itemRestCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        String criteria = itemRestCriteria.getCriteria();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(");
            queryBuilder.append(CategoryCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CategoryCriteriaPropertyRestriction.NAME).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CategoryCriteriaPropertyRestriction.URN).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(")");
        }

        // Filter by category scheme
        if (StringUtils.isNotBlank(itemRestCriteria.getItemSchemeUrn())) {
            if (StringUtils.isNotBlank(queryBuilder.toString())) {
                queryBuilder.append(" ").append(LogicalOperator.AND.name()).append(" ");
            }
            queryBuilder.append("(");
            queryBuilder.append(CategoryCriteriaPropertyRestriction.CATEGORY_SCHEME_URN).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(itemRestCriteria.getItemSchemeUrn())
                    .append("\"");
            queryBuilder.append(")");
        }

        // Find categories with one of the specified URNs
        String urnsQuery = buildUrnsQuery(CategoryCriteriaPropertyRestriction.URN, itemRestCriteria.getUrns());
        addConditionToQueryBuilderIfAny(queryBuilder, urnsQuery, LogicalOperator.AND);

        // Find categories that are externally published
        if (BooleanUtils.isTrue(itemRestCriteria.isItemSchemeExternallyPublished())) {
            String externallyPublishedQuery = buildSrmProcStatusQuery(CategoryCriteriaPropertyRestriction.CATEGORY_SCHEME_PROC_STATUS, ProcStatus.EXTERNALLY_PUBLISHED);
            addConditionToQueryBuilderIfAny(queryBuilder, externallyPublishedQuery, LogicalOperator.AND);
        }

        // Only last versions
        if (BooleanUtils.isTrue(itemRestCriteria.isItemSchemeLastVersion())) {
            String lastVersionQuery = buildIsLastVersionQuery(CategoryCriteriaPropertyRestriction.CATEGORY_SCHEME_LATEST, itemRestCriteria.isItemSchemeLastVersion());
            addConditionToQueryBuilderIfAny(queryBuilder, lastVersionQuery, LogicalOperator.AND);
        }

        return queryBuilder.toString();
    }

    //
    // CODELIST
    //

    public static String buildCodelistQuery(SrmExternalResourceRestCriteria itemSchemeRestCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        String criteria = itemSchemeRestCriteria.getCriteria();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(");
            queryBuilder.append(CodelistCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CodelistCriteriaPropertyRestriction.NAME).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CodelistCriteriaPropertyRestriction.URN).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(")");
        }

        // Find codelists with one of the specified URNs
        String urnsQuery = buildUrnsQuery(CodelistCriteriaPropertyRestriction.URN, itemSchemeRestCriteria.getUrns());
        addConditionToQueryBuilderIfAny(queryBuilder, urnsQuery, LogicalOperator.AND);

        // Find codelists that are externally published
        if (BooleanUtils.isTrue(itemSchemeRestCriteria.isOnlyExternallyPublished())) {
            String query = buildSrmProcStatusQuery(CodelistCriteriaPropertyRestriction.PROC_STATUS, ProcStatus.EXTERNALLY_PUBLISHED);
            addConditionToQueryBuilderIfAny(queryBuilder, query, LogicalOperator.AND);
        }

        // Only last versions
        String lastVersionQuery = buildIsLastVersionQuery(CodelistCriteriaPropertyRestriction.LATEST, itemSchemeRestCriteria.isOnlyLastVersion());
        addConditionToQueryBuilderIfAny(queryBuilder, lastVersionQuery, LogicalOperator.AND);

        return queryBuilder.toString();
    }

    //
    // CODE
    //

    public static String buildCodeQuery(SrmItemRestCriteria itemRestCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        String criteria = itemRestCriteria.getCriteria();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(");
            queryBuilder.append(CodeCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CodeCriteriaPropertyRestriction.NAME).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(CodeCriteriaPropertyRestriction.URN).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(")");
        }
        if (StringUtils.isNotBlank(itemRestCriteria.getItemSchemeUrn())) {
            if (StringUtils.isNotBlank(queryBuilder.toString())) {
                queryBuilder.append(" ").append(LogicalOperator.AND.name()).append(" ");
            }
            queryBuilder.append("(");
            queryBuilder.append(CodeCriteriaPropertyRestriction.CODELIST_URN).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(itemRestCriteria.getItemSchemeUrn()).append("\"");
            queryBuilder.append(")");
        }

        // Find codes with one of the specified URNs
        String urnsQuery = buildUrnsQuery(CodeCriteriaPropertyRestriction.URN, itemRestCriteria.getUrns());
        addConditionToQueryBuilderIfAny(queryBuilder, urnsQuery, LogicalOperator.AND);

        // Find codes that are externally published
        if (BooleanUtils.isTrue(itemRestCriteria.isItemSchemeExternallyPublished())) {
            String externallyPublishedQuery = buildSrmProcStatusQuery(CodeCriteriaPropertyRestriction.CODELIST_PROC_STATUS, ProcStatus.EXTERNALLY_PUBLISHED);
            addConditionToQueryBuilderIfAny(queryBuilder, externallyPublishedQuery, LogicalOperator.AND);
        }

        // Only last versions
        if (BooleanUtils.isTrue(itemRestCriteria.isItemSchemeLastVersion())) {
            String lastVersionQuery = buildIsLastVersionQuery(CodeCriteriaPropertyRestriction.CODELIST_LATEST, itemRestCriteria.isItemSchemeLastVersion());
            addConditionToQueryBuilderIfAny(queryBuilder, lastVersionQuery, LogicalOperator.AND);
        }

        return queryBuilder.toString();
    }

    private static void addConditionToQueryBuilderIfAny(StringBuilder queryBuilder, String condition, LogicalOperator operator) {
        if (StringUtils.isNotBlank(condition)) {
            if (StringUtils.isNotBlank(queryBuilder.toString())) {
                queryBuilder.append(" ").append(operator.name()).append(" ");
            }
            queryBuilder.append(condition);
        }
    }

    @SuppressWarnings("rawtypes")
    private static String buildUrnsQuery(Enum urnPropertyEnum, List<String> urns) {
        StringBuilder queryBuilder = new StringBuilder();
        if (urns != null && !urns.isEmpty()) {
            queryBuilder.append("(");
            for (int i = 0; i < urns.size(); i++) {
                String urn = urns.get(i);
                if (i != 0) {
                    queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
                }
                queryBuilder.append(urnPropertyEnum).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(urn).append("\"");
            }
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    private static String buildSrmProcStatusQuery(Enum procStatusPropertyEnum, ProcStatus procStatus) {
        StringBuilder queryBuilder = new StringBuilder();
        if (procStatus != null) {
            queryBuilder.append("(");
            queryBuilder.append(procStatusPropertyEnum).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(procStatus).append("\"");
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    private static String buildIsLastVersionQuery(Enum propertyEnum, Boolean booleanValue) {
        StringBuilder queryBuilder = new StringBuilder();
        if (BooleanUtils.isTrue(booleanValue)) {
            queryBuilder.append("(");
            queryBuilder.append(propertyEnum).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(booleanValue).append("\"");
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    public static void appendConditionToQuery(StringBuilder queryBuilder, String condition) {
        if (queryBuilder.length() > 0) {
            queryBuilder.append(RestQueryConstants.BLANK).append(LogicalOperator.AND.name()).append(RestQueryConstants.BLANK);
        }
        queryBuilder.append(condition);
    }

    @SuppressWarnings("rawtypes")
    public static String fieldComparison(Enum field, ComparisonOperator operator, Object value) {
        StringBuilder conditionBuilder = new StringBuilder();
        conditionBuilder.append(field).append(RestQueryConstants.BLANK).append(operator.name()).append(RestQueryConstants.BLANK);
        if (ComparisonOperator.IN.equals(operator)) {
            conditionBuilder.append(RestQueryConstants.LEFT_PARENTHESIS).append(value).append(RestQueryConstants.RIGHT_PARENTHESIS);
        } else if (!ComparisonOperator.IS_NULL.equals(operator) && !ComparisonOperator.IS_NOT_NULL.equals(operator)) {
            conditionBuilder.append(RestQueryConstants.SINGLE_QUOTE).append(value).append(RestQueryConstants.SINGLE_QUOTE);
        }
        return conditionBuilder.toString();
    }
}
