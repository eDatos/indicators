package es.gobcan.istac.indicators.rest.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import es.gobcan.istac.indicators.rest.types.InternationalDataAttributeType;

/**
 * Static assertion helpers for REST API response types produced by {@code Do2TypeMapperImpl}.
 *
 * <p>Mirrors the role of {@code IndicatorsAsserts} in indicators-core but targets the
 * REST-specific type {@link InternationalDataAttributeType}.</p>
 */
public class IndicatorsRestAsserts {

    private IndicatorsRestAsserts() {
    }

    // -----------------------------------------------------------------
    // InternationalDataAttributeType — non-i18n position check
    // -----------------------------------------------------------------

    /**
     * Asserts that a specific position in an {@link InternationalDataAttributeType} value list
     * is a non-null map containing only the {@code __default__} key with the expected value.
     * Used for non-i18n attributes (DATASET or DIMENSION/GROUP).
     *
     * @param expectedValue the expected value at {@code __default__}
     * @param actual        the attribute entry to check
     * @param position      zero-based index within {@code actual.getValue()}
     */
    public static void assertAttributeDefault(String expectedValue, InternationalDataAttributeType actual, int position) {
        assertNotNull("InternationalDataAttributeType must not be null", actual);
        assertNotNull("InternationalDataAttributeType value must not be null", actual.getValue());
        Map<String, String> localeMap = actual.getValue().get(position);
        assertNotNull("value[" + position + "] must not be null", localeMap);
        assertEquals("value[" + position + "][__default__]", expectedValue, localeMap.get("__default__"));
    }

    // -----------------------------------------------------------------
    // InternationalDataAttributeType
    // -----------------------------------------------------------------

    /**
     * Asserts that an {@link InternationalDataAttributeType} entry has the expected id and
     * the expected number of positions in its {@code value} list.
     */
    public static void assertInternationalAttribute(String expectedId, int expectedValueSize, InternationalDataAttributeType actual) {
        assertNotNull("InternationalDataAttributeType must not be null", actual);
        assertEquals("InternationalDataAttributeType id", expectedId, actual.getId());
        assertNotNull("InternationalDataAttributeType value must not be null", actual.getValue());
        assertEquals("InternationalDataAttributeType value size", expectedValueSize, actual.getValue().size());
    }

    /**
     * Asserts that a specific position in an {@code attributes.value} list is
     * non-null and contains exactly the supplied locale/label pairs.
     *
     * <p>Usage:
     * <pre>
     *   assertI18nValue(attr.getValue(), 0, "es", "valor es", "ca", "valor ca", "__default__", "valor es");
     * </pre>
     * </p>
     *
     * @param values           the value list from {@link InternationalDataAttributeType#getValue()}
     * @param position         zero-based index to check
     * @param localeAndLabels  alternating locale/label pairs (must have even length)
     */
    public static void assertI18nValue(List<Map<String, String>> values, int position, String... localeAndLabels) {
        assertNotNull("values list must not be null", values);
        Map<String, String> localeMap = values.get(position);
        assertNotNull("values[" + position + "] must not be null", localeMap);
        for (int i = 0; i < localeAndLabels.length; i += 2) {
            String locale = localeAndLabels[i];
            String expectedLabel = localeAndLabels[i + 1];
            assertEquals("values[" + position + "][" + locale + "]", expectedLabel, localeMap.get(locale));
        }
    }

    /**
     * Asserts that a specific position in an {@code attributes.value} list is null
     * (no value for that dimension position).
     */
    public static void assertI18nValueNull(List<Map<String, String>> values, int position) {
        assertNotNull("values list must not be null", values);
        assertNull("values[" + position + "] must be null", values.get(position));
    }
}
