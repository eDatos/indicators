package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class IndicatorsSystemsDS extends DataSource {
    
    // IDENTIFIERS
    public static String UUID = "indsys-uuid";
    public static String VERSION = "indsys-version";
    public static String CODE = "indsys-code";
    public static String URI = "indsys-uri";
    public static String TITLE = "indsys-title";
    public static String ACRONYM = "indsys-acron";
    public static String PROC_STATUS = "indsys-status";
    // PRODUCTION DESCRIPTORS
    public static String PROD_VERSION = "indsys-prod-v";
    public static String PROD_VALID_DATE = "indsys-prod-date";
    public static String PROD_VALID_USER = "indsys-prod-user";
    // DIFFUSION DESCRIPTORS
    public static String DIFF_VERSION = "indsys-diff-v";
    public static String DIFF_VALID_DATE = "indsys-diff-date";
    public static String DIFF_VALID_USER = "indsys-diff-user";
    // CONTENT DESCRIPTORS
    public static String DESCRIPTION = "indsys-desc";
    public static String OBJECTIVE = "indsys-obj";
    // PUBLICATION DESCRIPTORS
    public static String PUBL_DATE = "indsys-publ-date";
    public static String PUBL_USER = "indsys-publ-user";
    public static String ARCH_DATE = "indsys-arch-date";
    public static String ARCH_USER = "indsys-arch-user";
    
    public IndicatorsSystemsDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().indicDetailIdentifier());
        uuid.setPrimaryKey(true);
        addField(uuid);
        
        DataSourceIntegerField code = new DataSourceIntegerField(CODE, getConstants().indicDetailIdentifier());
        addField(code);
        
        DataSourceTextField title = new DataSourceTextField(TITLE, getConstants().indicDetailName());
        addField(title);
        
        setClientOnly(true);
    }
    
}
