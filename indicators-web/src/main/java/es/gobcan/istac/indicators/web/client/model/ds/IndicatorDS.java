package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;


public class IndicatorDS extends DataSource {
    
    // IDENTIFIERS
    public static String UUID = "ind-uuid";
    public static String VERSION_NUMBER = "ind-version";
    public static String CODE = "ind-code";
    public static String PROC_STATUS = "ind-status";
    public static String TITLE = "ind-title";
    public static String ACRONYM = "ind-acron";
    // CONTENT CLASSIFIERS
    public static String SUBJECT_CODE = "ind-op-code";
    public static String SUBJECT_TITLE = "ind-op-title";
    // CONTENT DESCRIPTORS
    public static String CONCEPT_DESCRIPTION = "ind-concept";
    // PRODUCTION DESCRIPTORS
    public static String PRODUCTION_VERSION = "indprod-v-";
    public static String PRODUCTION_VALIDATION_DATE = "ind-prod-date";
    public static String PRODUCTION_VALIDATION_USER = "ind-prod-user";
    public static String QUANTITY = "ind-quant";
    // DIFUSSION DESCRIPTORS
    public static String DIFFUSION_VERSION = "ind-dif-v";
    public static String DIFFUSION_VALIDATION_DATE = "ind-diff-date";
    public static String DIFFUSION_VALIDATION_USER = "ind-diff-user";
    // PUBLICATION DESCRIPTORS
    public static String PUBLICATION_FAILED_DATE = "ind-pub-fail-date";
    public static String PUBLICATION_FAILED_USER = "ind-pub-fail-user";
    public static String PUBLICATION_DATE = "ind-pub-date";
    public static String PUBLICATION_USER = "ind-pub-user";
    public static String ARCHIVED_DATE = "ind-arch-date";
    public static String ARCHIVED_USER = "ind-arch-user";
    // ANNOTATIONS
    public static String NOTES = "ind-notes";
    public static String COMMENTS = "ind-comments";
    
    
    public IndicatorDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(UUID, getConstants().indicDetailIdentifier());
        uuid.setPrimaryKey(true);
        addField(uuid);
        
        DataSourceIntegerField code = new DataSourceIntegerField(CODE, getConstants().indicDetailIdentifier());
        addField(code);
        
        DataSourceTextField name = new DataSourceTextField(TITLE, getConstants().indicDetailTitle());
        addField(name);
    }

}
