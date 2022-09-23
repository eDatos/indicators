package es.gobcan.istac.indicators.rest.util;

import static es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils.parseTimeValue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.google.common.collect.ImmutableMap;

import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public class ConditionUtilTest {

    @Test
    public void testNormalizeAndFilterTimeDimension() throws MetamacException {
        // @formatter:off
        Map<String, List<String>> selectedRepresentations = ImmutableMap.of(
                "TIME", Arrays.asList("2005", "2006", "~range=2003;2011", "~last=4", "~last=2","~after=2004", "~after=2009"),
                "GEOGRAPHICAL", Arrays.asList("35003","35005"),
                "MEASURE", Arrays.asList("ABSOLUTE")

        );
        Map<String, List<String>> selectedGranularities = ImmutableMap.of(
                "TIME", Arrays.asList("MONTHLY", "YEARLY"),
                "GEOGRAPHICAL", Arrays.asList("MUNICIPALITIES","PROVINCES")
        );
        // @formatter:on

        IndicatorsDataTimeDimensionFilterVO timeDimensionFilter = ConditionUtil.normalizeAndFilterTimeDimension(selectedRepresentations, selectedGranularities);
        assertEquals(Arrays.asList("2005", "2006"), timeDimensionFilter.getCodes());
        assertEquals(new Integer(4), timeDimensionFilter.getConditionLast());
        assertEquals(parseTimeValue("2004").getTimeValue(), timeDimensionFilter.getConditionAfter().getTimeValue());
        assertEquals(parseTimeValue("2003").getTimeValue(), timeDimensionFilter.getConditionRange().getFirst().getTimeValue());
        assertEquals(parseTimeValue("2011").getTimeValue(), timeDimensionFilter.getConditionRange().getSecond().getTimeValue());
    }
}
