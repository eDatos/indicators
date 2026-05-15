package es.gobcan.istac.indicators.rest.test.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;

/**
 * Test factory methods for REST layer tests.
 *
 * <p>Provides builders for {@code edatos-dataset-repository} DTOs and related types
 * used when testing the REST mapper and facade layers.</p>
 *
 * <p>These are intentionally separate from {@code IndicatorsMocks} (in indicators-core)
 * because the {@link InternationalStringDto} and {@link LocalisedStringDto} types here
 * come from {@code edatos-dataset-repository}, not from {@code metamac-core-common},
 * and are therefore a different class hierarchy.</p>
 */
public class IndicatorsRestMocks {

    private IndicatorsRestMocks() {
    }

    // -----------------------------------------------------------------
    // InternationalStringDto (edatos-dataset-repository)
    // -----------------------------------------------------------------

    /**
     * Builds an {@link InternationalStringDto} with a single locale.
     */
    public static InternationalStringDto mockDataRepoInternationalString(String locale, String label) {
        InternationalStringDto dto = new InternationalStringDto();
        dto.addText(new LocalisedStringDto(locale, label));
        return dto;
    }

    /**
     * Builds an {@link InternationalStringDto} with two locales.
     */
    public static InternationalStringDto mockDataRepoInternationalString(String locale1, String label1, String locale2, String label2) {
        InternationalStringDto dto = new InternationalStringDto();
        dto.addText(new LocalisedStringDto(locale1, label1));
        dto.addText(new LocalisedStringDto(locale2, label2));
        return dto;
    }

    /**
     * Builds an {@link InternationalStringDto} with three locales (typical es/ca/en pattern).
     */
    public static InternationalStringDto mockDataRepoInternationalString(String locale1, String label1, String locale2, String label2, String locale3, String label3) {
        InternationalStringDto dto = new InternationalStringDto();
        dto.addText(new LocalisedStringDto(locale1, label1));
        dto.addText(new LocalisedStringDto(locale2, label2));
        dto.addText(new LocalisedStringDto(locale3, label3));
        return dto;
    }

    // -----------------------------------------------------------------
    // AttributeInstanceDto — DATASET level
    // -----------------------------------------------------------------

    /**
     * Builds a DATASET-level (non-i18n) {@link AttributeInstanceDto} with a single Spanish label.
     */
    public static AttributeInstanceDto mockDatasetAttr(String id, String esLabel) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(mockDataRepoInternationalString("es", esLabel));
        inst.setCodesByDimension(new HashMap<String, List<String>>());
        return inst;
    }

    /**
     * Builds a DATASET-level (i18n) {@link AttributeInstanceDto} with es, ca and en labels.
     */
    public static AttributeInstanceDto mockDatasetI18nAttr(String id, String esLabel, String caLabel, String enLabel) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(mockDataRepoInternationalString("es", esLabel, "ca", caLabel, "en", enLabel));
        inst.setCodesByDimension(new HashMap<String, List<String>>());
        return inst;
    }

    // -----------------------------------------------------------------
    // AttributeInstanceDto — DIMENSION level (single dimension)
    // -----------------------------------------------------------------

    /**
     * Builds a DIMENSION-level (non-i18n) {@link AttributeInstanceDto} attached to one code
     * of a single dimension.
     *
     * @param dimId   indicator dimension name, e.g. {@code "GEOGRAPHICAL"}, {@code "MEASURE"}
     * @param dimCode code within that dimension, e.g. {@code "ES70"}, {@code "ABSOLUTE"}
     */
    public static AttributeInstanceDto mockDimAttr(String id, String dimId, String dimCode, String esLabel) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(mockDataRepoInternationalString("es", esLabel));
        Map<String, List<String>> codes = new HashMap<String, List<String>>();
        codes.put(dimId, Arrays.asList(dimCode));
        inst.setCodesByDimension(codes);
        return inst;
    }

    /**
     * Builds a DIMENSION-level (i18n) {@link AttributeInstanceDto} attached to one code
     * of a single dimension, with es and en labels.
     */
    public static AttributeInstanceDto mockDimI18nAttr(String id, String dimId, String dimCode, String esLabel, String enLabel) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(mockDataRepoInternationalString("es", esLabel, "en", enLabel));
        Map<String, List<String>> codes = new HashMap<String, List<String>>();
        codes.put(dimId, Arrays.asList(dimCode));
        inst.setCodesByDimension(codes);
        return inst;
    }

    // -----------------------------------------------------------------
    // AttributeInstanceDto — GROUP level (two dimensions)
    // -----------------------------------------------------------------

    /**
     * Builds a GROUP-level (non-i18n) {@link AttributeInstanceDto} attached to one code
     * of each of two dimensions (cartesian-product cell).
     */
    public static AttributeInstanceDto mockGroupAttr(String id, String dim1, String code1, String dim2, String code2, String esLabel) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(mockDataRepoInternationalString("es", esLabel));
        Map<String, List<String>> codes = new HashMap<String, List<String>>();
        codes.put(dim1, Arrays.asList(code1));
        codes.put(dim2, Arrays.asList(code2));
        inst.setCodesByDimension(codes);
        return inst;
    }
}
