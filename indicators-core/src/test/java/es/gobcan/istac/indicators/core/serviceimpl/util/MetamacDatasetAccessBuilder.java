package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataInternationalAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryMetadataBase;

/**
 * Static factory methods for building JAXB domain objects used in tests of
 * {@link CommonMetamacDatasetAccess} and related classes.
 *
 * <p>All methods are static so they can be imported and called without instantiation.</p>
 */
public class MetamacDatasetAccessBuilder {

    private MetamacDatasetAccessBuilder() {
    }

    // =========================================================================
    // Query / QueryMetamacDatasetAccess builders
    // =========================================================================

    /**
     * Builds a {@link QueryMetamacDatasetAccess} with the given dimensions, metadata attributes,
     * and data attributes. A placeholder observation value ("0") is set on the data.
     * Geographical dimension remapping is disabled (null spatial dims, empty variable elements map).
     */
    public static QueryMetamacDatasetAccess buildAccess(DimensionRepresentations dimensions, Attributes metadataAttributes,
            List<DataAttribute> plainDataAttrs, List<DataInternationalAttribute> internationalDataAttrs) throws MetamacException {

        Data data = new Data();
        data.setDimensions(dimensions);
        data.setObservations("0"); // placeholder; observation values are not under test

        DataAttributes dataAttributes = new DataAttributes();
        dataAttributes.getAttributes().addAll(plainDataAttrs);
        dataAttributes.getInternationalAttributes().addAll(internationalDataAttrs);
        data.setAttributes(dataAttributes);

        QueryMetadataBase metadata = new QueryMetadataBase();
        metadata.setAttributes(metadataAttributes);

        Query query = new Query();
        query.setData(data);
        query.setMetadata(metadata);

        return new QueryMetamacDatasetAccess(query, Collections.<String, String>emptyMap(), null);
    }

    /**
     * Builds a {@link Query} whose {@code data.attributes} is intentionally left null,
     * so that {@code extractDatasetAndDimensionAttributeInstances} returns an empty list.
     */
    public static Query buildQueryNoDataAttrs(DimensionRepresentations dimensions, Attributes metadataAttributes) {
        Data data = new Data();
        data.setDimensions(dimensions);
        data.setObservations("0");

        QueryMetadataBase metadata = new QueryMetadataBase();
        metadata.setAttributes(metadataAttributes);

        Query query = new Query();
        query.setData(data);
        query.setMetadata(metadata);

        return query;
    }

    // =========================================================================
    // Dimension builders
    // =========================================================================

    public static DimensionRepresentation dim(String dimId, String... codes) {
        CodeRepresentations reps = new CodeRepresentations();
        for (String code : codes) {
            CodeRepresentation cr = new CodeRepresentation();
            cr.setCode(code);
            reps.getRepresentations().add(cr);
        }
        DimensionRepresentation dim = new DimensionRepresentation();
        dim.setDimensionId(dimId);
        dim.setRepresentations(reps);
        return dim;
    }

    public static DimensionRepresentations dims(DimensionRepresentation... dimArray) {
        DimensionRepresentations result = new DimensionRepresentations();
        result.getDimensions().addAll(Arrays.asList(dimArray));
        return result;
    }

    // =========================================================================
    // Metadata attribute builders
    // =========================================================================

    public static Attribute datasetAttr(String id) {
        Attribute attr = new Attribute();
        attr.setId(id);
        attr.setAttachmentLevel(AttributeAttachmentLevelType.DATASET);
        return attr;
    }

    public static Attribute primaryMeasureAttr(String id) {
        Attribute attr = new Attribute();
        attr.setId(id);
        attr.setAttachmentLevel(AttributeAttachmentLevelType.PRIMARY_MEASURE);
        return attr;
    }

    /**
     * Creates a DIMENSION-level metadata attribute attached to one or more dimension IDs.
     * Passing a single dimension ID produces a plain DIMENSION attribute;
     * passing two or more produces a GROUP attribute.
     */
    public static Attribute dimensionAttr(String id, String... dimIds) {
        AttributeDimensions attrDims = new AttributeDimensions();
        for (String dimId : dimIds) {
            AttributeDimension ad = new AttributeDimension();
            ad.setDimensionId(dimId);
            attrDims.getDimensions().add(ad);
        }
        Attribute attr = new Attribute();
        attr.setId(id);
        attr.setAttachmentLevel(AttributeAttachmentLevelType.DIMENSION);
        attr.setDimensions(attrDims);
        return attr;
    }

    public static Attributes metaAttrs(Attribute... attrs) {
        Attributes result = new Attributes();
        result.getAttributes().addAll(Arrays.asList(attrs));
        return result;
    }

    // =========================================================================
    // Data attribute builders
    // =========================================================================

    public static DataAttribute dataAttr(String id, String value) {
        DataAttribute da = new DataAttribute();
        da.setId(id);
        da.setValue(value);
        return da;
    }

    public static List<DataAttribute> dataAttrs(DataAttribute... attrs) {
        return Arrays.asList(attrs);
    }

    public static List<DataAttribute> noDataAttrs() {
        return Collections.<DataAttribute>emptyList();
    }

    public static DataInternationalAttribute internDataAttr(String id, InternationalString... values) {
        DataInternationalAttribute ia = new DataInternationalAttribute();
        ia.setId(id);
        ia.getValues().addAll(Arrays.asList(values));
        return ia;
    }

    public static List<DataInternationalAttribute> internDataAttrs(DataInternationalAttribute... attrs) {
        return Arrays.asList(attrs);
    }

    public static List<DataInternationalAttribute> noInternDataAttrs() {
        return Collections.<DataInternationalAttribute>emptyList();
    }

    // =========================================================================
    // InternationalString (API domain) builders
    // =========================================================================

    public static InternationalString apiInternString(String lang1, String val1, String lang2, String val2) {
        InternationalString is = new InternationalString();
        is.getTexts().add(localisedString(lang1, val1));
        is.getTexts().add(localisedString(lang2, val2));
        return is;
    }

    public static LocalisedString localisedString(String lang, String value) {
        LocalisedString ls = new LocalisedString();
        ls.setLang(lang);
        ls.setValue(value);
        return ls;
    }

    // =========================================================================
    // sourceDimToIndicatorDim map builder
    // =========================================================================

    /**
     * Builds a {@code sourceDimToIndicatorDim} map from alternating key-value pairs.
     * Example: {@code dimMap("DIM_GEO", "GEOGRAPHICAL", "DIM_TIME", "TIME")}
     */
    public static Map<String, String> dimMap(String... keyValuePairs) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return map;
    }
}
