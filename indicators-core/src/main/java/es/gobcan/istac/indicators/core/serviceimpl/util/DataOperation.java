package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;

public class DataOperation {

    private DataSource               dataSource;
    private MeasureDimensionTypeEnum measureDimension;

    public DataOperation(DataSource dataSource, MeasureDimensionTypeEnum measureDimension) {
        this.dataSource = dataSource;
        this.measureDimension = measureDimension;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public RateDerivationMethodTypeEnum getMethodType() {
        switch (measureDimension) {
            case ABSOLUTE:
                return RateDerivationMethodTypeEnum.LOAD;
            case ANNUAL_PERCENTAGE_RATE:
                return dataSource.getAnnualPercentageRate().getMethodType();
            case ANNUAL_PUNTUAL_RATE:
                return dataSource.getAnnualPuntualRate().getMethodType();
            case INTERPERIOD_PERCENTAGE_RATE:
                return dataSource.getInterperiodPercentageRate().getMethodType();
            case INTERPERIOD_PUNTUAL_RATE:
                return dataSource.getInterperiodPuntualRate().getMethodType();
            default:
                return null;
        }
    }

    public boolean shouldBeRounded() {
        return true;
    }

    public boolean isAnnualMethod() {
        return (MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.equals(measureDimension) || MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.equals(measureDimension));
    }

    public boolean isInterPeriodMethod() {
        return (MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.equals(measureDimension) || MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.equals(measureDimension));
    }

    public boolean isPercentageMethod() {
        return (MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.equals(measureDimension) || MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.equals(measureDimension));
    }

    public boolean isPuntualMethod() {
        return (MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.equals(measureDimension) || MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.equals(measureDimension));
    }

    public MeasureDimensionTypeEnum getMeasureDimension() {
        return measureDimension;
    }

    public String getMethod() {
        switch (measureDimension) {
            case ABSOLUTE:
                return dataSource.getAbsoluteMethod();
            case ANNUAL_PERCENTAGE_RATE:
                return dataSource.getAnnualPercentageRate().getMethod();
            case ANNUAL_PUNTUAL_RATE:
                return dataSource.getAnnualPuntualRate().getMethod();
            case INTERPERIOD_PERCENTAGE_RATE:
                return dataSource.getInterperiodPercentageRate().getMethod();
            case INTERPERIOD_PUNTUAL_RATE:
                return dataSource.getInterperiodPuntualRate().getMethod();
            default:
                return null;
        }
    }

    public RateDerivationRoundingEnum getRateRounding() {
        switch (measureDimension) {
            case ABSOLUTE:
                // Rounding for absolute measure can not be specified in the web application, so we decided upward
                return RateDerivationRoundingEnum.UPWARD;
            case ANNUAL_PERCENTAGE_RATE:
                return dataSource.getAnnualPercentageRate().getRounding();
            case ANNUAL_PUNTUAL_RATE:
                return dataSource.getAnnualPuntualRate().getRounding();
            case INTERPERIOD_PERCENTAGE_RATE:
                return dataSource.getInterperiodPercentageRate().getRounding();
            case INTERPERIOD_PUNTUAL_RATE:
                return dataSource.getInterperiodPuntualRate().getRounding();
            default:
                return null;
        }
    }

    public Quantity getQuantity() {
        switch (measureDimension) {
            case ABSOLUTE:
                return dataSource.getIndicatorVersion().getQuantity();
            case ANNUAL_PERCENTAGE_RATE:
                return dataSource.getAnnualPercentageRate().getQuantity();
            case ANNUAL_PUNTUAL_RATE:
                return dataSource.getAnnualPuntualRate().getQuantity();
            case INTERPERIOD_PERCENTAGE_RATE:
                return dataSource.getInterperiodPercentageRate().getQuantity();
            case INTERPERIOD_PUNTUAL_RATE:
                return dataSource.getInterperiodPuntualRate().getQuantity();
            default:
                return null;
        }
    }

    public String getDataSourceUuid() {
        return dataSource.getUuid();
    }

    public String getDataGpeUuid() {
        return dataSource.getDataGpeUuid();
    }

    public boolean hasGeographicalVariable() {
        return dataSource.getGeographicalVariable() != null;
    }

    public String getGeographicalVariable() {
        return dataSource.getGeographicalVariable();
    }

    public boolean hasTimeVariable() {
        return dataSource.getTimeVariable() != null;
    }

    public String getTimeVariable() {
        return dataSource.getTimeVariable();
    }

    public List<DataSourceVariable> getOtherVariables() {
        return dataSource.getOtherVariables();
    }

    public GeographicalValue getGeographicalValue() {
        return dataSource.getGeographicalValue();
    }

    public String getTimeValue() {
        return dataSource.getTimeValue();
    }

}
