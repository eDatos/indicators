package es.gobcan.istac.indicators.rest.types;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"elements"})
public class IndicatorsSystemType extends IndicatorsSystemBaseType {

    /**
     * 
     */
    private static final long      serialVersionUID = -5856767616725665032L;

    private List<ElementLevelType> elements         = null;

    public List<ElementLevelType> getElements() {
        return elements;
    }

    public void setElements(List<ElementLevelType> elements) {
        this.elements = elements;
    }

}
