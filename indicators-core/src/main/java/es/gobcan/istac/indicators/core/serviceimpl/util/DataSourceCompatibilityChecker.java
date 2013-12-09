package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class DataSourceCompatibilityChecker {

    private static GeographicalValueRepository geographicalValueRepository = null;

    public static List<MetamacExceptionItem> check(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        items.addAll(checkGeographicVariableConstraints(dataSource, data));

        items.addAll(checkTimeVariableConstraints(dataSource, data));

        items.addAll(checkAbsoluteMethodConstraints(dataSource, data));

        items.addAll(checkRatesMethodConstraints(dataSource, data));

        items.addAll(checkOtherVariablesConstraints(dataSource, data));

        return items;
    }

    private static List<MetamacExceptionItem> checkGeographicVariableConstraints(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();
        String geographicVar = dataSource.getGeographicalVariable();
        // Has georaphic variable
        if (!StringUtils.isEmpty(geographicVar)) {
            // Must be an existent variable and must be marked as spatial variable
            if (data.getVariables().contains(geographicVar)) {
                // Must be marked as spatial variable
                if (data.getSpatialVariables() != null && data.getSpatialVariables().contains(geographicVar)) {
                    // Checking every value is valid
                    List<String> geoCodesProvided = data.getValueCodes().get(geographicVar);
                    List<String> unknownCodes = detectInvalidGeographicalCodes(geoCodesProvided);
                    if (unknownCodes.size() > 0) {
                        String codes = StringUtils.join(unknownCodes, ",");
                        items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VALUES_INVALID, dataSource.getUuid(), codes));
                    }
                } else {
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_GEOGRAPHIC, dataSource.getUuid()));
                }
            } else {
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_EXISTS, dataSource.getUuid()));
            }
        }
        // Has geographic value
        else {
            if (data.getSpatialVariables() != null && !data.getSpatialVariables().isEmpty()) { // Data has spatial variable, should have been specified as gepgraphic variable
                String geoVars = StringUtils.join(data.getSpatialVariables(), ", ");
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VALUE_ILLEGAL, dataSource.getUuid(), geoVars));
            }
        }
        return items;
    }

    private static List<String> detectInvalidGeographicalCodes(List<String> geoCodesProvided) {
        List<String> unknownCodes = new ArrayList<String>();
        for (String geoCode : geoCodesProvided) {
            GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
            if (geoValue == null) {
                unknownCodes.add(geoCode);
            }
        }
        return unknownCodes;
    }

    private static List<MetamacExceptionItem> checkTimeVariableConstraints(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();
        String timeVar = dataSource.getTimeVariable();
        // Has time variable
        if (!StringUtils.isEmpty(timeVar)) {
            // Must be an existent variable and must be marked as temporal variable
            if (data.getVariables().contains(timeVar)) {
                if (timeVar.equals(data.getTemporalVariable())) {
                    // Checking every value is valid
                    List<String> timeCodesProvided = data.getValueCodes().get(timeVar);
                    List<String> unknownCodes = detectInvalidTimeCodes(timeCodesProvided);
                    if (unknownCodes.size() > 0) {
                        String codes = StringUtils.join(unknownCodes, ",");
                        items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VALUES_INVALID, dataSource.getUuid(), codes));
                    }
                } else {
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VARIABLE_NOT_TEMPORAL, dataSource.getUuid()));
                }
            } else {
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VARIABLE_NOT_EXISTS, dataSource.getUuid()));
            }
        }
        // Has time value
        else {
            if (!StringUtils.isEmpty(data.getTemporalVariable())) { // Data has spatial variable, should have been specified as gepgraphic variable
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VALUE_ILLEGAL, dataSource.getUuid(), data.getTemporalVariable()));
            }
        }
        return items;
    }

    private static List<String> detectInvalidTimeCodes(List<String> timeCodesProvided) {
        List<String> unknownCodes = new ArrayList<String>();
        for (String timeCode : timeCodesProvided) {
            if (!TimeVariableUtils.isTimeValue(timeCode)) {
                unknownCodes.add(timeCode);
            }
        }
        return unknownCodes;
    }

    private static List<MetamacExceptionItem> checkAbsoluteMethodConstraints(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        String method = dataSource.getAbsoluteMethod();
        if (method != null) {
            if (data.hasContVariable()) {
                String contVariable = data.getContVariable();
                List<String> codes = data.getValueCodes().get(contVariable);
                if (!codes.contains(dataSource.getAbsoluteMethod())) { // Invalid value for absoluteMethod
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_CONTVARIABLE_ILLEGAL, dataSource.getUuid(), dataSource.getAbsoluteMethod()));
                }
            } else { // only possible correct value is OBS_VALUE
                if (!dataSource.isAbsoluteMethodObsValue()) {
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_NO_CONTVARIABLE_ILLEGAL, dataSource.getUuid(), dataSource.getAbsoluteMethod()));
                }
            }
        }
        return items;
    }

    private static List<MetamacExceptionItem> checkRatesMethodConstraints(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        if (dataSource.getAnnualPercentageRate() != null) {
            items.addAll(checkRateMethodConstraints(dataSource, ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE, dataSource.getAnnualPercentageRate(), data));
        }
        if (dataSource.getAnnualPuntualRate() != null) {
            items.addAll(checkRateMethodConstraints(dataSource, ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE, dataSource.getAnnualPuntualRate(), data));
        }
        if (dataSource.getInterperiodPercentageRate() != null) {
            items.addAll(checkRateMethodConstraints(dataSource, ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE, dataSource.getInterperiodPercentageRate(), data));
        }
        if (dataSource.getInterperiodPuntualRate() != null) {
            items.addAll(checkRateMethodConstraints(dataSource, ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE, dataSource.getInterperiodPuntualRate(), data));
        }
        return items;
    }

    private static List<MetamacExceptionItem> checkOtherVariablesConstraints(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        if (!StringUtils.isEmpty(data.getTemporalVariable())) {
            if (existsVariableInOtherVariables(dataSource, data.getTemporalVariable())) {
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_TEMPORAL_INCLUDED, dataSource.getUuid(), data.getTemporalVariable()));
            }
        }
        if (!StringUtils.isEmpty(data.getContVariable())) {
            if (existsVariableInOtherVariables(dataSource, data.getContVariable())) {
                items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_CONTVARIABLE_INCLUDED, dataSource.getUuid(), data.getContVariable()));
            }
        }

        items.addAll(checkUnspecifedVariables(dataSource, data));

        items.addAll(checkUnknownVariables(dataSource, data));

        return items;
    }

    private static List<MetamacExceptionItem> checkUnspecifedVariables(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        List<String> otherVariablesExpected = data.getVariables();

        // Geographic, time and cont variable are not expected to be in other variables
        if (data.getSpatialVariables() != null && !data.getSpatialVariables().isEmpty()) {
            if (data.getSpatialVariables().contains(dataSource.getGeographicalVariable())) {
                otherVariablesExpected.remove(dataSource.getGeographicalVariable());
            } else {
                otherVariablesExpected.removeAll(data.getSpatialVariables());
            }
        }
        if (!StringUtils.isEmpty(data.getTemporalVariable())) {
            otherVariablesExpected.remove(data.getTemporalVariable());
        }
        if (!StringUtils.isEmpty(data.getContVariable())) {
            otherVariablesExpected.remove(data.getContVariable());
        }

        List<String> unspecified = new ArrayList<String>();
        for (String var : otherVariablesExpected) {
            boolean found = false;
            for (DataSourceVariable variable : dataSource.getOtherVariables()) {
                if (variable.getVariable().equals(var)) {
                    found = true;
                }
            }
            if (!found) {
                unspecified.add(var);
            }
        }

        if (unspecified.size() > 0) {
            String vars = StringUtils.join(unspecified, ",");
            items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNSPECIFIED_VARIABLES, dataSource.getUuid(), vars));
        }

        return items;
    }

    private static List<MetamacExceptionItem> checkUnknownVariables(DataSource dataSource, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();

        List<String> unknown = new ArrayList<String>();
        for (DataSourceVariable variable : dataSource.getOtherVariables()) {
            boolean found = false;
            for (String var : data.getVariables()) {
                if (variable.getVariable().equals(var)) {
                    found = true;
                }
            }
            if (!found) {
                unknown.add(variable.getVariable());
            }
        }

        if (unknown.size() > 0) {
            String vars = StringUtils.join(unknown, ",");
            items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNKNOWN_VARIABLES, dataSource.getUuid(), vars));
        }

        return items;
    }

    private static boolean existsVariableInOtherVariables(DataSource dataSource, String variableName) {
        if (variableName == null) {
            return false;
        }
        for (DataSourceVariable variable : dataSource.getOtherVariables()) {
            if (variableName.equals(variable.getVariable())) {
                return true;
            }
        }
        return false;
    }

    private static List<MetamacExceptionItem> checkRateMethodConstraints(DataSource dataSource, String rateName, RateDerivation rate, Data data) {
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();
        if (RateDerivationMethodTypeEnum.LOAD.equals(rate.getMethodType())) {
            String method = rate.getMethod();
            if (data.hasContVariable()) {
                String contVariable = data.getContVariable();
                List<String> codes = data.getValueCodes().get(contVariable);
                if (!codes.contains(method)) { // Invalid value for absoluteMethod
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL, dataSource.getUuid(), rateName, method));
                }
            } else { // only possible correct value is OBS_VALUE
                if (!rate.isMethodObsValue()) {
                    items.add(new MetamacExceptionItem(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL, dataSource.getUuid(), rateName, method));
                }
            }
        }
        return items;
    }

    public static GeographicalValueRepository getGeographicalValueRepository() {
        if (geographicalValueRepository == null) {
            geographicalValueRepository = ApplicationContextProvider.getApplicationContext().getBean(GeographicalValueRepository.class);
        }
        return geographicalValueRepository;
    }
}
