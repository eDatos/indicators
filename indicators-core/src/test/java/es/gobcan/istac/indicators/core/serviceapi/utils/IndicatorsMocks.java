package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.siemac.edatos.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.utils.TypeExternalArtefactsEnumUtils;
import org.siemac.metamac.srm.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.TypeRelatedResourceEnum;

/**
 * Mocks
 */
public class IndicatorsMocks extends MetamacMocks {

    // -----------------------------------------------------------------
    // LISTS
    // -----------------------------------------------------------------
    public static List<String> mockUuidsList(Integer listSize) {
        List<String> randomUuidsList = new ArrayList<String>();

        for (int i = 0; i < listSize; i++) {
            randomUuidsList.add(UUID.randomUUID().toString());
        }
        return randomUuidsList;
    }

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING
    // -----------------------------------------------------------------

    public static InternationalString mockInternationalString() {
        InternationalString internationalString = new InternationalString();
        LocalisedString es = new LocalisedString();
        es.setLabel(mockString(10) + " en Espanol");
        es.setLocale("es");
        es.setVersion(Long.valueOf(0));
        LocalisedString en = new LocalisedString();
        en.setLabel(mockString(10) + " in English");
        en.setLocale("en");
        en.setVersion(Long.valueOf(0));
        internationalString.addText(es);
        internationalString.addText(en);
        internationalString.setVersion(Long.valueOf(0));
        return internationalString;
    }

    /**
     * Mock an InternationalString with one locale
     */
    public static InternationalString mockInternationalString(String locale, String label) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLocale(locale);
        localisedString.setLabel(label);
        target.addText(localisedString);
        return target;
    }

    /**
     * Mock an InternationalString with two locales
     */
    public static InternationalString mockInternationalString(String locale01, String label01, String locale02, String label02) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString01 = new LocalisedString();
        localisedString01.setLocale(locale01);
        localisedString01.setLabel(label01);
        target.addText(localisedString01);

        LocalisedString localisedString02 = new LocalisedString();
        localisedString02.setLocale(locale02);
        localisedString02.setLabel(label02);
        target.addText(localisedString02);
        return target;
    }

    /**
     * Mock a InternationalString with one locale
     */
    public static InternationalStringDto mockInternationalStringDto(String locale, String label) {
        InternationalStringDto target = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale(locale);
        localisedStringDto.setLabel(label);
        target.addText(localisedStringDto);
        return target;
    }

    public static InternationalStringAvro mockInternationalStringAvro() {
        List<LocalisedStringAvro> avroLocalisedStrings = new ArrayList<>();
        LocalisedStringAvro es = new LocalisedStringAvro();
        es.setLabel(mockString(10) + " en Espanol");
        es.setLocale("es");

        LocalisedStringAvro en = new LocalisedStringAvro();
        en.setLabel(mockString(10) + " in English");
        en.setLocale("en");

        avroLocalisedStrings.add(es);
        avroLocalisedStrings.add(en);
        return InternationalStringAvro.newBuilder().setLocalisedStrings(avroLocalisedStrings).build();
    }

    // -----------------------------------------------------------------
    // GEOGRAPHIC GRANULARITY AND VALUE
    // -----------------------------------------------------------------

    /**
     * Mock a GeographicalGranularity
     */
    public static GeographicalGranularityDto mockGeographicalGranularity(String code) {
        GeographicalGranularityDto geographicalGranularityDto = new GeographicalGranularityDto();
        geographicalGranularityDto.setCode(code);
        geographicalGranularityDto.setTitle(mockInternationalStringDto());
        return geographicalGranularityDto;
    }

    /**
     * Mock a GeographicalValue
     */
    public static GeographicalValueDto mockGeographicalValue(String code, String order, String granularityUuid) {
        GeographicalValueDto geographicalValueDto = new GeographicalValueDto();
        geographicalValueDto.setCode(code);
        geographicalValueDto.setOrder(order);
        geographicalValueDto.setTitle(mockInternationalStringDto());
        geographicalValueDto.setLatitude(20.0656233);
        geographicalValueDto.setLongitude(-25.454564645);

        GeographicalGranularityDto granularity = new GeographicalGranularityDto();
        granularity.setUuid(granularityUuid);
        geographicalValueDto.setGranularity(granularity);

        return geographicalValueDto;
    }

    /**
     * Mock a GeographicalValue as RelatedResourceDto
     */
    public static RelatedResourceDto mockRelatedResourceAsGeographicalValue(String code, String geoValueUuid, String granularityUuid, String granularityCode) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setTitle(mockInternationalStringDto());
        relatedResourceDto.setType(TypeRelatedResourceEnum.GEOGRAPHICAL_VALUE);
        relatedResourceDto.setUuid(geoValueUuid);
        relatedResourceDto.setGranularityCode(granularityCode);
        relatedResourceDto.setGranularityUuid(granularityUuid);
        return relatedResourceDto;
    }

    /**
     * Mock a GeographicalValue
     */
    public static VariableElementAvro mockVariableElementAvro(String code, String variableCode, String granularityUuid) {
        VariableElementAvro variableElementAvro = new VariableElementAvro();
        variableElementAvro.setCode(code);
        variableElementAvro.setShortName(mockInternationalStringAvro());
        variableElementAvro.setLatitud(20.0656233);
        variableElementAvro.setLongitud(-25.454564645);
        variableElementAvro.setGeographicGranularities(mockRelatedResourceAvro(code, mockCodeUrn(code), granularityUuid));
        variableElementAvro.setVariable(mockRelatedResourceAvro(variableCode, GeneratorUrnUtils.generateSiemacStructuralResourcesVariableUrn(variableCode), UUID.randomUUID().toString()));

        return variableElementAvro;
    }

    public static RelatedResourceAvro mockRelatedResourceAvro(String code, String urn, String granularityUuid) {
        RelatedResourceAvro relatedResourceAvro = new RelatedResourceAvro();
        relatedResourceAvro.setCode(code + "_" + granularityUuid);
        relatedResourceAvro.setUuid(granularityUuid);
        relatedResourceAvro.setTitle(mockInternationalStringAvro());
        relatedResourceAvro.setUrn(urn);

        return relatedResourceAvro;
    }

    // -----------------------------------------------------------------
    // UNIT MULTIPLIER
    // -----------------------------------------------------------------

    /**
     * Mock a UnitMultiplier
     */
    public static UnitMultiplierDto mockUnitMultiplier(Integer unitMultiplierValue) {
        UnitMultiplierDto unitMultiplierDto = new UnitMultiplierDto();
        unitMultiplierDto.setUnitMultiplier(unitMultiplierValue);
        unitMultiplierDto.setTitle(mockInternationalStringDto());
        return unitMultiplierDto;
    }

    /**
     * Mock an ExternalItemDto
     */
    public static ExternalItemDto mockExternalItemDto(String code, String urn, TypeExternalArtefactsEnum type) {
        return mockExternalItemDtoComplete(code, urn, type);
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEMS
    // -----------------------------------------------------------------

    public static ExternalItem mockExternalItem(String code, String codeNested, String uri, String urnProvider, String urn, TypeExternalArtefactsEnum type) {
        ExternalItem target = new ExternalItem();
        target.setVersion(Long.valueOf(0));
        target.setCode(code);
        target.setCodeNested(codeNested);
        target.setUri(uri);
        target.setUrn(urn);
        target.setUrnProvider(urnProvider);
        target.setType(type);
        return target;
    }

    public static ExternalItem mockExternalItem(String code, String codeNested, String uri, String urnProvider, String urn, TypeExternalArtefactsEnum type, InternationalString title,
            String managementAppUrl) {
        ExternalItem target = mockExternalItem(code, codeNested, uri, urnProvider, urn, type);
        target.setTitle(title);
        target.setManagementAppUrl(managementAppUrl);
        return target;
    }

    public static ExternalItem mockExternalItem(String code, TypeExternalArtefactsEnum type) {
        String urn = mockCategoryElementUrn(code);
        return mockExternalItem(code, null, urn, type);
    }

    private static ExternalItem mockExternalItem(String code, String codeNested, String urn, TypeExternalArtefactsEnum type) {
        String uri = CoreCommonConstants.API_LATEST_WITH_SLASHES + code;
        String urnProvider = urn + ":provider";
        InternationalString title = mockInternationalString(code, "title");
        String managementAppUrl = CoreCommonConstants.URL_SEPARATOR + code;

        if (TypeExternalArtefactsEnumUtils.isExternalItemOfCommonMetadataApp(type) || TypeExternalArtefactsEnum.VARIABLE_ELEMENT.equals(type)
                || TypeExternalArtefactsEnum.CATEGORY_ELEMENT.equals(type)) {
            urnProvider = null;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfStatisticalOperationsApp(type)) {
            urnProvider = null;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfSrmApp(type)) {
            // nothing to do with urnInternal because it's ok for SrmExternalItems
            if (StringUtils.isBlank(codeNested)) {
                if (TypeExternalArtefactsEnum.AGENCY.equals(type) || TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
                    codeNested = code;
                }
            }
        } else {
            fail("Unexpected type of ExternalItem:" + type);
        }

        ExternalItem item = mockExternalItem(code, codeNested, uri, urnProvider, urn, type, title, managementAppUrl);
        return item;
    }

    public static ExternalItemDto mockQuantityUnitExternalItemDto(String code) {
        return mockExternalItemDto(code, mockCodeUrn(code), TypeExternalArtefactsEnum.CODE);

    }

}