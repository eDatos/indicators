package es.gobcan.istac.indicators.rest.enume;

import java.io.Serializable;

public enum ExportFormatEnum implements Serializable {

    //@formatter:off
    TSV("tsv", "\t", "text/tab-separated-values"),
    CSV_COMMA("csv", ",", "text/csv"),
    CSV_SEMICOLON("csv", ";", "text/csv"),
    XLSX("xlsx" , null, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    //@formatter:on

    private String extension;
    private String separator;
    private String mimeType;

    /**
     */
    private ExportFormatEnum(String extension, String separator, String mimeType) {
        this.extension = extension;
        this.separator = separator;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getSeparator() {
        return separator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name();
    }

    public boolean isPlainText() {
        return this == CSV_COMMA || this == CSV_SEMICOLON || this == TSV;
    }

    public boolean isXlsx() {
        return this == XLSX;
    }
}
