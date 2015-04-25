package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"code", "title", "attachmentLevel"})
public class MetadataAttributeType implements Serializable {

    private static final long                serialVersionUID = 7916356957929853567L;

    private String                           code             = null;
    private Map<String, String>              title            = null;
    private AttributeAttachmentLevelEnumType attachmentLevel  = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public AttributeAttachmentLevelEnumType getAttachmentLevel() {
        return attachmentLevel;
    }

    public void setAttachmentLevel(AttributeAttachmentLevelEnumType attachmentLevel) {
        this.attachmentLevel = attachmentLevel;
    }

}
