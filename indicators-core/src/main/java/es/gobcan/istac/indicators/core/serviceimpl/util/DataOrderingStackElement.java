package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.List;

public class DataOrderingStackElement {

    private final String dimensionId;
    private final int    dimensionPosition;
    private final List<String> dimCodes; 

    public DataOrderingStackElement(String dimensionId, int dimensionPosition, String dimensionCodeId, List<String> dimCodes) {
        super();
        this.dimensionId = dimensionId;
        this.dimensionPosition = dimensionPosition;
        this.dimCodes = dimCodes;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public int getDimensionPosition() {
        return dimensionPosition;
    }
    
    public List<String> getDimCodes() {
        return dimCodes;
    }
}