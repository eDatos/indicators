package es.gobcan.istac.indicators.rest.util;

import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.GROUP_LEFT;
import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.GROUP_RIGHT;
import static org.siemac.edatos.core.common.constants.shared.XsdDataTypesRegExp.DATE;
import static org.siemac.edatos.core.common.constants.shared.XsdDataTypesRegExp.DATE_TIME;
import static org.siemac.edatos.core.common.constants.shared.XsdDataTypesRegExp.G_YEAR;
import static org.siemac.edatos.core.common.constants.shared.XsdDataTypesRegExp.G_YEAR_MONTH;

import org.siemac.metamac.core.common.constants.shared.SDMXCommonRegExpV2_1;

public class ExpandedSDMXCommonRegExpV2_1 extends SDMXCommonRegExpV2_1 {

    public static final String GREGORIAN_TIME_PERIOD_TYPE = DATE + "|" + G_YEAR_MONTH + "|" + G_YEAR;
    public static final String BASIC_TIME_PERIOD_TYPE     = DATE_TIME + "|" + GREGORIAN_TIME_PERIOD_TYPE;

    public static final String REPORTING_YEAR             = "\\d{4}";
    public static final String REPORTING_YEAR_TYPE        = REPORTING_YEAR + "-" + REPORTING_YEAR_PERIOD_INDICATOR + REPORTING_YEAR_TYPE_TIME_RANGE;
    public static final String REPORTING_SEMESTER_TYPE    = REPORTING_YEAR + "-" + REPORTING_SEMESTER_PERIOD_INDICATOR + REPORTING_SEMESTER_TYPE_TIME_RANGE;
    public static final String REPORTING_TRIMESTER_TYPE   = REPORTING_YEAR + "-" + REPORTING_TRIMESTER_PERIOD_INDICATOR + REPORTING_TRIMESTER_TYPE_TIME_RANGE;
    public static final String REPORTING_QUARTER_TYPE     = REPORTING_YEAR + "-" + REPORTING_QUARTER_PERIOD_INDICATOR + REPORTING_QUARTER_TYPE_TIME_RANGE;
    public static final String REPORTING_MONTH_TYPE       = REPORTING_YEAR + "-" + REPORTING_MONTH_PERIOD_INDICATOR + GROUP_LEFT + REPORTING_MONTH_TYPE_TIME_RANGE + GROUP_RIGHT;
    public static final String REPORTING_WEEK_TYPE        = REPORTING_YEAR + "-" + REPORTING_WEEK_PERIOD_INDICATOR + GROUP_LEFT + REPORTING_WEEK_TYPE_TIME_RANGE + GROUP_RIGHT;
    public static final String REPORTING_DAY_TYPE         = REPORTING_YEAR + "-" + REPORTING_DAY_PERIOD_INDICATOR + GROUP_LEFT + REPORTING_DAY_TYPE_TIME_RANGE + GROUP_RIGHT;

    public static final String REPORTING_TIME_PERIOD_TYPE = REPORTING_YEAR_TYPE + "|" + REPORTING_SEMESTER_TYPE + "|" + REPORTING_TRIMESTER_TYPE + "|" + REPORTING_QUARTER_TYPE + "|"
            + REPORTING_MONTH_TYPE + "|" + REPORTING_WEEK_TYPE + "|" + REPORTING_DAY_TYPE;

    public static final String TIME_RANGE_TYPE            = TIME_RANGE_TYPE_1 + "|" + TIME_RANGE_TYPE_2;

    public static final String OBSERVATIONAL_TIME_PERIOD  = REPORTING_TIME_PERIOD_TYPE + "|" + TIME_RANGE_TYPE + "|" + BASIC_TIME_PERIOD_TYPE;

}
