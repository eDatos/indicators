package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;

@JsonPropertyOrder({
    "unitSymbol",
    "untiSymbolPosition",
    "unitMultiplier",
    "significantDigits",
    "decimalPlaces",
    "min",
    "max",
    "denominator",
    "numerator",
    "isPercentage",
    "baseValue",
    "baseTime",
    "baseLocation",
    "baseQuantity"
})
public class QuantityType implements Serializable {

    /**
     * 
     */
    private static final long              serialVersionUID   = -456489258156288381L;

    private QuantityTypeEnum               type               = null;
    private String                         unitSymbol         = null;
    private QuantityUnitSymbolPositionEnum untiSymbolPosition = null;
    private Integer                        unitMultiplier     = null;
    private Integer                        significantDigits  = null;
    private Integer                        decimalPlaces      = null;

    private Integer                        min                = null;
    private Integer                        max                = null;
    private LinkType                       denominatorLink    = null;
    private LinkType                       numeratorLink      = null;
    private Boolean                        isPercentage       = null;
    private Map<String, String>            percentageOf       = null;
    private Integer                        baseValue          = null;
    private String                         baseTime           = null;
    private String                         baseLocation       = null;
    private LinkType                       baseQuantityLink   = null;

    public QuantityTypeEnum getType() {
        return type;
    }

    public void setType(QuantityTypeEnum type) {
        this.type = type;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public QuantityUnitSymbolPositionEnum getUntiSymbolPosition() {
        return untiSymbolPosition;
    }

    public void setUntiSymbolPosition(QuantityUnitSymbolPositionEnum untiSymbolPosition) {
        this.untiSymbolPosition = untiSymbolPosition;
    }

    public Integer getUnitMultiplier() {
        return unitMultiplier;
    }

    public void setUnitMultiplier(Integer unitMultiplier) {
        this.unitMultiplier = unitMultiplier;
    }

    public Integer getSignificantDigits() {
        return significantDigits;
    }

    public void setSignificantDigits(Integer significantDigits) {
        this.significantDigits = significantDigits;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public LinkType getDenominatorLink() {
        return denominatorLink;
    }

    public void setDenominatorLink(LinkType denominatorLink) {
        this.denominatorLink = denominatorLink;
    }

    public LinkType getNumeratorLink() {
        return numeratorLink;
    }

    public void setNumeratorLink(LinkType numeratorLink) {
        this.numeratorLink = numeratorLink;
    }

    public Boolean getIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(Boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public Map<String, String> getPercentageOf() {
        return percentageOf;
    }

    public void setPercentageOf(Map<String, String> percentageOf) {
        this.percentageOf = percentageOf;
    }

    public Integer getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Integer baseValue) {
        this.baseValue = baseValue;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    public LinkType getBaseQuantityLink() {
        return baseQuantityLink;
    }

    public void setBaseQuantityLink(LinkType baseQuantityLink) {
        this.baseQuantityLink = baseQuantityLink;
    }

}
