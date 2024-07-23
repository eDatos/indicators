package es.gobcan.istac.indicators.core.serviceapi.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;

/**
 * Mocks
 */
public class SrmResourcesMocks extends MetamacMocks {

    private static final String DEFAULT_LOCALE                = "es";

    public static final String  GEOGRAPHICAL_CODE_SUFIX       = "_VARIABLE_ELEMENT";
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES    = "ES" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES61  = "ES61" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES611 = "ES611" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES612 = "ES612" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES613 = "ES613" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES614 = "ES614" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES615 = "ES615" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES616 = "ES616" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES617 = "ES617" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES618 = "ES618" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES24  = "ES24" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES241 = "ES241" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES242 = "ES242" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES243 = "ES243" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES12  = "ES12" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES53  = "ES53" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES70  = "ES70" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES701 = "ES701" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES702 = "ES702" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES13  = "ES13" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES41  = "ES41" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES411 = "ES411" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES412 = "ES412" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES413 = "ES413" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES414 = "ES414" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES415 = "ES415" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES416 = "ES416" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES417 = "ES417" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES418 = "ES418" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES419 = "ES419" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES42  = "ES42" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES421 = "ES421" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES422 = "ES422" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES423 = "ES423" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES424 = "ES424" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES425 = "ES425" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES51  = "ES51" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES511 = "ES511" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES512 = "ES512" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES513 = "ES513" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES514 = "ES514" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES52  = "ES52" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES521 = "ES521" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES522 = "ES522" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES523 = "ES523" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES43  = "ES43" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES431 = "ES431" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES432 = "ES432" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES11  = "ES11" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES111 = "ES111" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES112 = "ES112" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES113 = "ES113" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES114 = "ES114" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES30  = "ES30" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES62  = "ES62" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES22  = "ES22" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES21  = "ES21" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES211 = "ES211" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES212 = "ES212" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES213 = "ES213" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES23  = "ES23" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES63  = "ES63" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_ES64  = "ES64" + GEOGRAPHICAL_CODE_SUFIX;
    public static final String  GEOGRAPHICAL_CODE_VALUE_FR    = "FR" + GEOGRAPHICAL_CODE_SUFIX;

    // -------------------------------------------------------------------------------------
    // COMMON
    // -------------------------------------------------------------------------------------

    private static ResourceLink buildResourceLink(TypeExternalArtefactsEnum type) {
        ResourceLink resourceLink = new ResourceLink();
        resourceLink.setHref(UUID.randomUUID().toString());
        resourceLink.setKind(type.getName());
        return resourceLink;
    }

    public static InternationalString buildInternationalStringResource(String label, String lang) {
        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().add(buildLocalisedStringResource(label, lang));
        return internationalString;
    }

    public static LocalisedString buildLocalisedStringResource(String label, String lang) {
        LocalisedString text = new LocalisedString();
        text.setLang(lang);
        text.setValue(label);
        return text;
    }

    // -------------------------------------------------------------------------------------
    // CODELIST
    // -------------------------------------------------------------------------------------

    public static Codelist buildCodelist(String id, String name, String lang, String urn) {
        Codelist scheme = new Codelist();
        scheme.setId(id);
        scheme.setUrn(urn);
        scheme.setName(buildInternationalStringResource(name, lang));
        return scheme;
    }

    public static ResourceInternal buildCodelistRef(String codelistUrn) {
        ResourceInternal ref = new ResourceInternal();
        ref.setUrn(codelistUrn);
        return ref;
    }

    // -------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------

    public static Codes buildCodes(int numCodes) {
        Codes codes = new Codes();
        for (int i = 1; i <= numCodes; i++) {
            codes.getCodes().add(buildCode("code-0" + i, "Code 0" + i, DEFAULT_LOCALE, "variableElement-0" + i));
        }
        return codes;
    }

    public static CodeResourceInternal buildCode(String id, String name, String lang, String variableElementCode) {
        CodeResourceInternal code = new CodeResourceInternal();
        code.setId(id);
        code.setUrn("urn:uuid:" + id);
        code.setUrnProvider("urn:uuid:provider:" + id);
        code.setSelfLink(buildResourceLink(TypeExternalArtefactsEnum.CODE));
        code.setName(buildInternationalStringResource(name, lang));
        code.setKind(TypeExternalArtefactsEnum.CODE.getValue());
        code.setVariableElement(buildVariableElementRef(variableElementCode));
        return code;
    }

    public static ResourceInternal buildVariableElementRef(String codeId) {
        ResourceInternal ref = new ResourceInternal();
        ref.setId(codeId);
        return ref;
    }

    public static Map<String, String> buildVariableElementsIdByCodeOfCodelist(List<GeographicalValue> geographicalValues) {
        Map<String, String> variableElementsByCodeOfCodelist = new HashMap<String, String>();

        for (GeographicalValue geoValue : geographicalValues) {
            String geoVariableElementCode = geoValue.getCode();
            String geoCode = geoVariableElementCode.replace(GEOGRAPHICAL_CODE_SUFIX, "");
            variableElementsByCodeOfCodelist.put(geoCode, geoVariableElementCode);

        }

        return variableElementsByCodeOfCodelist;

    }
}