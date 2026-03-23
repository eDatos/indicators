package es.gobcan.istac.indicators.rest.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import es.gobcan.istac.indicators.rest.types.DataAttributeType;
import es.gobcan.istac.indicators.rest.types.InternationalDataAttributeType;

/**
 * Static assertion helpers for REST API response types produced by {@code Do2TypeMapperImpl}.
 *
 * <p>Mirrors the role of {@code IndicatorsAsserts} in indicators-core but targets the
 * REST-specific types {@link DataAttributeType} and {@link InternationalDataAttributeType}.</p>
 */
public class IndicatorsRestAsserts {

    private IndicatorsRestAsserts() {
    }

    // -----------------------------------------------------------------
    // DataAttributeType
    // -----------------------------------------------------------------

    /**
     * Asserts that a {@link DataAttributeType} entry has the expected id and value.
     */
    public static void assertDataAttribute(String expectedId, String expectedValue, DataAttributeType actual) {
        assertNotNull("DataAttributeType must not be null", actual);
        assertEquals("DataAttributeType id", expectedId, actual.getId());
        assertEquals("DataAttributeType value", expectedValue, actual.getValue());
    }

    // -----------------------------------------------------------------
    // InternationalDataAttributeType
    // -----------------------------------------------------------------

    /**
     * Asserts that an {@link InternationalDataAttributeType} entry has the expected id and
     * the expected number of positions in its {@code values} list.
     */
    public static void assertInternationalAttribute(String expectedId, int expectedValuesSize, InternationalDataAttributeType actual) {
        assertNotNull("InternationalDataAttributeType must not be null", actual);
        assertEquals("InternationalDataAttributeType id", expectedId, actual.getId());
        assertNotNull("InternationalDataAttributeType values must not be null", actual.getValues());
        assertEquals("InternationalDataAttributeType values size", expectedValuesSize, actual.getValues().size());
    }

    /**
     * Asserts that a specific position in an {@code internationalAttribute.values} list is
     * non-null and contains exactly the supplied locale/label pairs.
     *
     * <p>Usage:
     * <pre>
     *   assertI18nValue(attr.getValues(), 0, "es", "valor es", "ca", "valor ca", "en", "value en");
     * </pre>
     * </p>
     *
     * @param values         the values list from {@link InternationalDataAttributeType#getValues()}
     * @param position       zero-based index to check
     * @param localeAndLabels alternating locale/label pairs (must have even length)
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
     * Asserts that a specific position in an {@code internationalAttribute.values} list is null
     * (no value for that dimension position).
     */
    public static void assertI18nValueNull(List<Map<String, String>> values, int position) {
        assertNotNull("values list must not be null", values);
        assertNull("values[" + position + "] must be null", values.get(position));
    }
}
