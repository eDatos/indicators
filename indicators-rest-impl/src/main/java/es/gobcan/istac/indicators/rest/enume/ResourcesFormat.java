package es.gobcan.istac.indicators.rest.enume;

import javax.ws.rs.core.MediaType;

import org.siemac.metamac.rest.common.export.mappers.PlainTextMessageBodyWritter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResourcesFormat {

    JSON(MediaType.APPLICATION_JSON),
    XML(MediaType.APPLICATION_XML),
    JSONSTAT("application/jsonstat+json"),
    TSV("text/tab-separated-values"),
    CSV("text/csv"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private static final Logger logger = LoggerFactory.getLogger(PlainTextMessageBodyWritter.class);

    private final String        mimeType;

    ResourcesFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static String getMimeType(String format) {
        try {
            return ResourcesFormat.valueOf(format.toUpperCase()).getMimeType();
        } catch (IllegalArgumentException e) {
            logger.warn("Unrecognized format: {}", format);
            return null;
        }
    }
}
