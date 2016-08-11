package es.gobcan.istac.indicators.core.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class DataContent {
    @JsonProperty("Valor")
    private String value;
    
    @JsonProperty("Comentario")
    private String comment;
    
    @JsonProperty("comentarioDATANOTECELL")
    private String commentDataNoteCell;
    
    @JsonProperty
    private List<String> dimCodes; 
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getCommentDataNoteCell() {
        return commentDataNoteCell;
    }
    
    public void setCommentDataNoteCell(String commentDataNoteCell) {
        this.commentDataNoteCell = commentDataNoteCell;
    }
    
    public List<String> getDimCodes() {
        return dimCodes;
    }
    
    public void setDimCodes(List<String> dimCodes) {
        this.dimCodes = dimCodes;
    }
}
