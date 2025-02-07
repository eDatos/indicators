package es.gobcan.istac.indicators.rest.mapper;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;

public class SrmRestObjectsMapper {

    Map<String, String>                       geographicalVariableElementsByCode = new HashMap<>();
    Map<String, String>                       geographicalCodesByVariableElement = new HashMap<>();
    HashMap<String, CategoryResourceInternal> categoriesByCategoryElement        = new HashMap<>();
    HashMap<String, Code>                     codeOfCodelistByUrn                = new HashMap<>();

    public HashMap<String, Code> getCodeOfCodelistByUrn() {
        return codeOfCodelistByUrn;
    }

    public void setCodeOfCodelistByUrn(HashMap<String, Code> codeOfCodelistByUrn) {
        this.codeOfCodelistByUrn = codeOfCodelistByUrn;
    }

    public Map<String, String> getGeographicalVariableElementsByCode() {
        return geographicalVariableElementsByCode;
    }

    public void setGeographicalVariableElementsByCode(Map<String, String> geographicalVariableElementsByCode) {
        this.geographicalVariableElementsByCode = geographicalVariableElementsByCode;
    }

    public Map<String, String> getGeographicalCodesByVariableElement() {
        return geographicalCodesByVariableElement;
    }

    public void setGeographicalCodesByVariableElement(Map<String, String> geographicalCodesByVariableElement) {
        this.geographicalCodesByVariableElement = geographicalCodesByVariableElement;
    }

    public HashMap<String, CategoryResourceInternal> getCategoriesByCategoryElement() {
        return categoriesByCategoryElement;
    }

    public void setCategoriesByCategoryElement(HashMap<String, CategoryResourceInternal> categoriesByCategoryElement) {
        this.categoriesByCategoryElement = categoriesByCategoryElement;
    }
}
