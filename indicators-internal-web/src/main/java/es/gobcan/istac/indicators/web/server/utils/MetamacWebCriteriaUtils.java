package es.gobcan.istac.indicators.web.server.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.QuantityUnitCriteriaPropertyEnum;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;

public class MetamacWebCriteriaUtils {

    public static MetamacCriteriaRestriction buildMetamacCriteriaFromWebcriteria(IndicatorCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction indicatorCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                indicatorCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                indicatorCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
                indicatorCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(indicatorCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getTitle())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), criteria.getTitle(), OperationType.ILIKE));
            }

            if (criteria.getProductionVersionProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.PRODUCTION_PROC_STATUS.name(), criteria.getProductionVersionProcStatus(), OperationType.EQ));
            }

            if (criteria.getDiffusionVersionProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.DIFFUSION_PROC_STATUS.name(), criteria.getDiffusionVersionProcStatus(), OperationType.EQ));
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
                geoValueCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(geoValueCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getGranularityCode())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), criteria.getGranularityCode(), OperationType.EQ));
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
                quantityUnitCriteriaDisjuction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(QuantityUnitCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
                // TODO INDISTAC-877
            }
            conjunctionRestriction.getRestrictions().add(quantityUnitCriteriaDisjuction);
        }

        return conjunctionRestriction;
    }
}
