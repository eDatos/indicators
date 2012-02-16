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
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;

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
        target.getDimensions().addAll(copyDimensions(source.getDimensions(), target));
        
        return target;
    }
    
    /**
     * Create a new IndicatorVersion copying values from a source
     */
    public static IndicatorVersion copy(IndicatorVersion source) {
        IndicatorVersion target = new IndicatorVersion();
        target.setName(copy(source.getName()));
        target.setAcronym(copy(source.getAcronym()));
        target.setSubjectCode(source.getSubjectCode());
        target.setNoteUrl(source.getNoteUrl());
        target.setNotes(copy(source.getNotes()));
        target.setCommentary(copy(source.getCommentary()));
        target.getDataSources().addAll(copyDataSources(source.getDataSources(), target));
        
        return target;
    }

    /**
     * Copy dimensions of an indicators system
     */
    private static List<Dimension> copyDimensions(List<Dimension> sources, IndicatorsSystemVersion indicatorsSystemVersionTarget) {
        List<Dimension> targets = new ArrayList<Dimension>();
        for (Dimension source : sources) {
            Dimension target = copy(source);
            target.setParent(null);
            target.setIndicatorsSystemVersion(indicatorsSystemVersionTarget);
            targets.add(target);
        }
        return targets;
    }
    
    /**
     * Copy a dimension
     */
    private static Dimension copy(Dimension source) {
        Dimension target = new Dimension();
        target.setTitle(copy(source.getTitle()));
        target.setOrderInLevel(source.getOrderInLevel());
        for (Dimension subdimensionSource : source.getSubdimensions()) {
            Dimension subdimensionTarget = copy(subdimensionSource);
            subdimensionTarget.setParent(target);
            subdimensionTarget.setIndicatorsSystemVersion(null);
            target.addSubdimension(subdimensionTarget);
        }
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
        target.setTemporaryVariable(source.getTemporaryVariable());
        target.setGeographicVariable(source.getGeographicVariable());
        target.getOtherVariables().addAll(copyDataSourceVariables(source.getOtherVariables()));
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
        DataSourceVariable target = new DataSourceVariable(source.getVariable(), source.getCategory());
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