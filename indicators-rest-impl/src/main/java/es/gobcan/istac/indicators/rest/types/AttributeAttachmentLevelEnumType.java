package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

public enum AttributeAttachmentLevelEnumType implements Serializable {
    DATASET, DIMENSION, OBSERVATION;

    /**
     */
    private AttributeAttachmentLevelEnumType() {
    }

    public String getName() {
        return name();
    }
}