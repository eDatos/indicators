package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;

public class DoCopyUtils {

    private DoCopyUtils() {
    }

    /**
     * Create a new IndicatorsSystemVersion copying values from a source
     * 
     * @throws MetamacException
     */
    public static IndicatorsSystemVersion copy(IndicatorsSystemVersion source) throws MetamacException {
        IndicatorsSystemVersion target = new IndicatorsSystemVersion();
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
        target.setNotes(copy(source.getNotes()));
        target.setConceptDescription(copy(source.getConceptDescription()));
        target.setComments(copy(source.getComments()));
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
     * @throws MetamacException
     */
    private static void copyElementsLevels(IndicatorsSystemVersion indicatorsSystemVersionSource, IndicatorsSystemVersion indicatorsSystemVersionTarget) throws MetamacException {
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
     *
     * @throws MetamacException
     */
    private static ElementLevel copy(ElementLevel source, IndicatorsSystemVersion indicatorsSystemVersionTarget) throws MetamacException {
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
     *
     * @throws MetamacException
     */
    public static IndicatorInstance copy(IndicatorInstance source) throws MetamacException {
        IndicatorInstance target = new IndicatorInstance();
        // must remains with same code (but with different uuid)
        target.setCode(source.getCode());
        target.setTitle(copy(source.getTitle()));
        target.setIndicator(source.getIndicator());
        target.setGeographicalGranularity(source.getGeographicalGranularity());
        target.getGeographicalValues().clear();
        for (GeographicalValue geoValue : source.getGeographicalValues()) {
            target.addGeographicalValue(geoValue);
        }
        target.setTimeGranularity(source.getTimeGranularity());
        target.setTimeValuesAsList(source.getTimeValuesAsList());

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
        target.setQueryEnvironment(source.getQueryEnvironment());
        target.setQueryUuid(source.getQueryUuid());
        target.setQueryUrn(source.getQueryUrn());
        target.setStatResource(copy(source.getStatResource()));
        target.setTimeValue(source.getTimeValue());
        target.setTimeVariable(source.getTimeVariable());
        target.setGeographicalValue(source.getGeographicalValue());
        target.setGeographicalVariable(source.getGeographicalVariable());
        target.getOtherVariables().addAll(copyDataSourceVariables(source.getOtherVariables()));
        target.setAbsoluteMethod(source.getAbsoluteMethod());

        target.setSourceSurveyCode(source.getSourceSurveyCode());
        target.setSourceSurveyTitle(copy(source.getSourceSurveyTitle()));
        target.setSourceSurveyAcronym(copy(source.getSourceSurveyAcronym()));
        target.setSourceSurveyUrl(source.getSourceSurveyUrl());
        target.setPublishers(source.getPublishers());
        target.setAnnualPuntualRate(copyRateDerivation(source.getAnnualPuntualRate()));
        target.setAnnualPercentageRate(copyRateDerivation(source.getAnnualPercentageRate()));
        target.setInterperiodPuntualRate(copyRateDerivation(source.getInterperiodPuntualRate()));
        target.setInterperiodPercentageRate(copyRateDerivation(source.getInterperiodPercentageRate()));
        return target;
    }

    /**
     * Copy a rate derivation
     */
    private static RateDerivation copyRateDerivation(RateDerivation source) {
        if (source == null) {
            return null;
        }
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
        target.getTexts().addAll(copyLocalisedStrings(source.getTexts(), target));
        return target;
    }

    private static Set<LocalisedString> copyLocalisedStrings(Set<LocalisedString> sources, InternationalString intString) {
        Set<LocalisedString> targets = new HashSet<LocalisedString>();
        for (LocalisedString source : sources) {
            LocalisedString target = new LocalisedString();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            target.setInternationalString(intString);
            targets.add(target);
        }
        return targets;
    }

    /**
     * Copy a data external item
     */
    private static ExternalItem copy(ExternalItem source) {
        if (source == null) {
            return null;
        }
        ExternalItem target = new ExternalItem();
        target.setCode(source.getCode());
        target.setCodeNested(source.getCodeNested());
        target.setManagementAppUrl(source.getManagementAppUrl());
        target.setTitle(copy(source.getTitle()));
        target.setType(source.getType());
        target.setUri(source.getUri());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        return target;
    }
}