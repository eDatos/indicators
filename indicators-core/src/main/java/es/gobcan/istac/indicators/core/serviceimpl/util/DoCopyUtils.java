package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;

public class DoCopyUtils {

    /**
     * Create a new IndicatorsSystemVersion copying values from a source
     */
    public static IndicatorsSystemVersion copy(IndicatorsSystemVersion source) {
        IndicatorsSystemVersion target = new IndicatorsSystemVersion();
        target.setUriGopestat(source.getUriGopestat());
        target.setTitle(copy(source.getTitle()));
        target.setAcronym(copy(source.getAcronym()));
        target.setObjetive(copy(source.getObjetive()));
        target.setDescription(copy(source.getDescription()));
        copyElementsLevels(source, target);

        return target;
    }

    /**
     * Create a new IndicatorVersion copying values from a source
     */
    public static IndicatorVersion copy(IndicatorVersion source) {
        IndicatorVersion target = new IndicatorVersion();
        target.setTitle(copy(source.getTitle()));
        target.setAcronym(copy(source.getAcronym()));
        target.setSubjectCode(source.getSubjectCode());
        target.setSubjectTitle(copy(source.getSubjectTitle()));
        target.setNotesUrl(source.getNotesUrl());
        target.setNotes(copy(source.getNotes()));
        target.setConceptDescription(copy(source.getConceptDescription()));
        target.setComments(copy(source.getComments()));
        target.setCommentsUrl(source.getCommentsUrl());
        target.setQuantity(copy(source.getQuantity()));
        target.getDataSources().addAll(copyDataSources(source.getDataSources(), target));

        return target;
    }

    private static Quantity copy(Quantity source) {
        Quantity target = new Quantity();
        target.setQuantityType(source.getQuantityType());
        target.setUnit(source.getUnit());
        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setSignificantDigits(source.getSignificantDigits());
        target.setDecimalPlaces(source.getDecimalPlaces());
        target.setMinimum(source.getMinimum());
        target.setMaximum(source.getMaximum());
        target.setNumerator(source.getNumerator());
        target.setDenominator(source.getDenominator());
        target.setIsPercentage(source.getIsPercentage());
        target.setPercentageOf(copy(source.getPercentageOf()));
        target.setBaseValue(source.getBaseValue());
        target.setBaseTime(source.getBaseTime());
        target.setBaseLocation(source.getBaseLocation());
        target.setBaseQuantity(source.getBaseQuantity());
        return target;
    }

    /**
     * 
     */
    private static void copyElementsLevels(IndicatorsSystemVersion indicatorsSystemVersionSource, IndicatorsSystemVersion indicatorsSystemVersionTarget) {
        List<ElementLevel> targets = new ArrayList<ElementLevel>();
        List<ElementLevel> sources = indicatorsSystemVersionSource.getChildrenFirstLevel();
        for (ElementLevel source : sources) {
            ElementLevel target = copy(source, indicatorsSystemVersionTarget);
            target.setParent(null);
            target.setIndicatorsSystemVersion(indicatorsSystemVersionTarget);
            target.setIndicatorsSystemVersionFirstLevel(indicatorsSystemVersionTarget);
            indicatorsSystemVersionTarget.getChildrenFirstLevel().add(target);
            indicatorsSystemVersionTarget.getChildrenAllLevels().add(target);
            targets.add(target);
        }
    }

    /**
     * Copy a ElementLevel
     */
    private static ElementLevel copy(ElementLevel source, IndicatorsSystemVersion indicatorsSystemVersionTarget) {
        ElementLevel target = new ElementLevel();
        if (source.getDimension() != null) {
            Dimension dimensionTarget = copy(source.getDimension());
            dimensionTarget.setElementLevel(target);
            target.setDimension(dimensionTarget);
        } else if (source.getIndicatorInstance() != null) {
            IndicatorInstance indicatorInstanceTarget = copy(source.getIndicatorInstance());
            indicatorInstanceTarget.setElementLevel(target);
            target.setIndicatorInstance(indicatorInstanceTarget);
        }
        target.setOrderInLevel(source.getOrderInLevel());

        for (ElementLevel childrenSource : source.getChildren()) {
            ElementLevel childrenTarget = copy(childrenSource, indicatorsSystemVersionTarget);
            childrenTarget.setParent(target);
            childrenTarget.setIndicatorsSystemVersion(indicatorsSystemVersionTarget);
            childrenTarget.setIndicatorsSystemVersionFirstLevel(null);
            target.addChildren(childrenTarget);
            indicatorsSystemVersionTarget.getChildrenAllLevels().add(childrenTarget);
        }
        return target;
    }

    /**
     * Copy a dimension
     */
    public static Dimension copy(Dimension source) {
        Dimension target = new Dimension();
        target.setTitle(copy(source.getTitle()));
        return target;
    }

    /**
     * Copy an indicator instance
     */
    public static IndicatorInstance copy(IndicatorInstance source) {
        IndicatorInstance target = new IndicatorInstance();
        target.setTitle(copy(source.getTitle()));
        target.setIndicator(source.getIndicator());
        target.setGeographicalGranularityId(source.getGeographicalGranularityId());
        target.setGeographicalValue(source.getGeographicalValue());
        target.setTemporalGranularity(source.getTemporalGranularity());
        target.setTemporalValue(source.getTemporalValue());

        return target;
    }

    /**
     * Copy data sources of an indicator
     */
    private static List<DataSource> copyDataSources(List<DataSource> sources, IndicatorVersion indicatorVersionTarget) {
        List<DataSource> targets = new ArrayList<DataSource>();
        for (DataSource source : sources) {
            DataSource target = copy(source);
            target.setIndicatorVersion(indicatorVersionTarget);
            targets.add(target);
        }
        return targets;
    }

    /**
     * Copy a data source
     */
    private static DataSource copy(DataSource source) {
        DataSource target = new DataSource();
        target.setQueryGpe(source.getQueryGpe());
        target.setPx(source.getPx());
        target.setTemporalVariable(source.getTemporalVariable());
        target.setGeographicalVariable(source.getGeographicalVariable());
        target.getOtherVariables().addAll(copyDataSourceVariables(source.getOtherVariables()));
        target.setInterperiodRate(copyRateDerivation(source.getInterperiodRate()));
        target.setAnnualRate(copyRateDerivation(source.getAnnualRate()));
        return target;
    }

    /**
     * Copy a rate derivation
     */
    private static RateDerivation copyRateDerivation(RateDerivation source) {
        RateDerivation target = new RateDerivation();
        target.setMethodType(source.getMethodType());
        target.setMethod(source.getMethod());
        target.setQuantity(copy(source.getQuantity()));
        target.setRounding(source.getRounding());
        return target;
    }

    /**
     * Copy variables of a data source
     */
    private static List<DataSourceVariable> copyDataSourceVariables(List<DataSourceVariable> sources) {
        List<DataSourceVariable> targets = new ArrayList<DataSourceVariable>();
        for (DataSourceVariable source : sources) {
            DataSourceVariable target = copy(source);
            targets.add(target);
        }
        return targets;
    }

    /**
     * Copy a data source variable
     */
    private static DataSourceVariable copy(DataSourceVariable source) {
        DataSourceVariable target = new DataSourceVariable();
        target.setVariable(source.getVariable());
        target.setCategory(source.getCategory());
        return target;
    }

    private static InternationalString copy(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalString target = new InternationalString();
        target.getTexts().addAll(copyLocalisedStrings(source.getTexts()));
        return target;
    }

    private static Set<LocalisedString> copyLocalisedStrings(Set<LocalisedString> sources) {
        Set<LocalisedString> targets = new HashSet<LocalisedString>();
        for (LocalisedString source : sources) {
            LocalisedString target = new LocalisedString();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            targets.add(target);
        }
        return targets;
    }
}