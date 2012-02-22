package es.gobcan.istac.indicators.core.serviceapi;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.common.test.MetamacBaseTests;

public abstract class IndicatorsBaseTest extends MetamacBaseTests {

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TBL_LOCALISED_STRINGS");
        tables.add("TBL_ELEMENTS_LEVELS");
        tables.add("TBL_INDICATOR_INSTANCES");
        tables.add("TBL_DIMENSIONS");
        tables.add("TBL_INDIC_SYSTEMS_VERSIONS");
        tables.add("TBL_INDICATORS_SYSTEMS");
        tables.add("TBL_DATA_SOURCE_VARIABLES");
        tables.add("TBL_DATA_SOURCES");
        tables.add("TBL_INDICATOR_VERSIONS");
        tables.add("TBL_INDICATORS");
        tables.add("TBL_INTERNATIONAL_STRINGS");
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_INDIC_SYSTEMS_VERSIONS");
        sequences.add("SEQ_INDICATORS_SYSTEMS");
        sequences.add("SEQ_DIMENSIONS");
        sequences.add("SEQ_DATA_SOURCES");
        sequences.add("SEQ_DATA_SOURCE_VARIABLES");
        sequences.add("SEQ_INDICATOR_VERSIONS");
        sequences.add("SEQ_INDICATORS");
        sequences.add("SEQ_INDICATOR_INSTANCES");
        sequences.add("SEQ_ELEMENTS_LEVELS");
        return sequences;
    }
}
