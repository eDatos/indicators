package es.gobcan.istac.indicators.rest.types;

import java.util.List;

public class SubjectType extends SubjectBaseType {

    private static final long       serialVersionUID = -6642908737425959673L;

    private List<IndicatorBaseType> elements;

    public List<IndicatorBaseType> getElements() {
        return elements;
    }

    public void setElements(List<IndicatorBaseType> elements) {
        this.elements = elements;
    }
}
