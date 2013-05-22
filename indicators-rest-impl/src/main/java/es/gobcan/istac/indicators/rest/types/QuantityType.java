package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;

@JsonPropertyOrder({
    "type",
    "unit",
    "unitSymbol",
    "unitSymbolPosition",
    "unitMultiplier",
    "significantDigits",
    "decimalPlaces",
    "min",
    "max",
    "denominatorLink",
    "numeratorLink",
    "isPercentage",
    "percentageOf",
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
    private Map<String, String>            unit               = null;
    private String                         unitSymbol         = null;
    private QuantityUnitSymbolPositionEnum unitSymbolPosition = null;
    private Map<String, String>            unitMultiplier     = null;
    private Integer                        significantDigits  = null;
    private Integer                        decimalPlaces      = null;

    private Integer                        min                = null;
    private Integer                        max                = null;
    private TitleLinkType                  denominatorLink    = null;
    private TitleLinkType                  numeratorLink      = null;
    private Boolean                        isPercentage       = null;
    private Map<String, String>            percentageOf       = null;
    private Integer                        baseValue          = null;
    private MetadataRepresentationType     baseTime           = null;
    private MetadataRepresentationType     baseLocation       = null;
    private TitleLinkType                  baseQuantityLink   = null;

    public QuantityTypeEnum getType() {
        return type;
    }

    public void setType(QuantityTypeEnum type) {
        this.type = type;
    }
    
    public Map<String, String> getUnit() {
        return unit;
    }
    
    public void setUnit(Map<String, String> unit) {
        this.unit = unit;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public QuantityUnitSymbolPositionEnum getUnitSymbolPosition() {
        return unitSymbolPosition;
    }

    public void setUnitSymbolPosition(QuantityUnitSymbolPositionEnum untiSymbolPosition) {
        this.unitSymbolPosition = untiSymbolPosition;
    }

    public Map<String, String> getUnitMultiplier() {
        return unitMultiplier;
    }

    public void setUnitMultiplier(Map<String, String> unitMultiplier) {
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

    public void setDenominatorLink(TitleLinkType denominatorLink) {
        this.denominatorLink = denominatorLink;
    }

    public LinkType getNumeratorLink() {
        return numeratorLink;
    }

    public void setNumeratorLink(TitleLinkType numeratorLink) {
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

    public MetadataRepresentationType getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(MetadataRepresentationType baseTime) {
        this.baseTime = baseTime;
    }
    
    public MetadataRepresentationType getBaseLocation() {
        return baseLocation;
    }
    
    public void setBaseLocation(MetadataRepresentationType baseLocation) {
        this.baseLocation = baseLocation;
    }

    public LinkType getBaseQuantityLink() {
        return baseQuantityLink;
    }

    public void setBaseQuantityLink(TitleLinkType baseQuantityLink) {
        this.baseQuantityLink = baseQuantityLink;
    }

}
