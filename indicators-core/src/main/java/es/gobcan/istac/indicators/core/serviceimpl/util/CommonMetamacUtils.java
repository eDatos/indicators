package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSdmxCodelistUrn;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;

import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;

public class CommonMetamacUtils {

    public static String extractSpecificDimensionFromDimensions(Dimensions dimensions, DimensionType dimensionType) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimensionType.equals(dimension.getType())) {
                return dimension.getId();
            }
        }

        return null;
    }

    public static String extractValueForDefaultLanguage(InternationalString internationalString) {
        // Find in Dimensions or Attributes
        if (internationalString != null && !internationalString.getTexts().isEmpty()) {
            // Only one locale was received in the API, the default locale. Therefore, this code is valid.
            LocalisedString localisedString = internationalString.getTexts().iterator().next();
            return localisedString.getValue();
        }
        return null;
    }

    public static List<String> extractVariablesFromDimensions(Dimensions dimensions) {
        List<String> result = new ArrayList<String>();
        for (Dimension dimension : dimensions.getDimensions()) {
            result.add(dimension.getId());
        }

        return result;
    }

    public static List<String> extractCodeUrnOfSpecificTypeAttribute(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
        List<String> spatialValues = new ArrayList<String>();
        if (attributes == null || dataAttributes == null) {
            return spatialValues;
        }

        for (Attribute attribute : attributes.getAttributes()) {
            if (componentType.equals(attribute.getType())) {
                AttributeValues attributeValues = attribute.getAttributeValues();
                if (attributeValues instanceof EnumeratedAttributeValues) {

                    for (EnumeratedAttributeValue value : ((EnumeratedAttributeValues) attributeValues).getValues()) {
                        spatialValues.add(value.getUrn());
                    }

                }
            }
        }

        return spatialValues;
    }

    public static String extractSpecificAttributeValuesByType(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
        if (attributes == null || dataAttributes == null) {
            return null;
        }

        for (Attribute attribute : attributes.getAttributes()) {
            if (componentType.equals(attribute.getType())) {
                for (DataAttribute dataAttribute : dataAttributes.getAttributes()) {
                    if (attribute.getId().equals(dataAttribute.getId())) {
                        return dataAttribute.getValue();
                    }
                }
            }
        }

        return null;
    }

    public static String extractUrnCodelistFromUrnCode(String urnCode) {
        String[] params = UrnUtils.splitUrnItem(urnCode);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        String[] agenciesID = agencyId.contains(".") ? agencyId.split(".") : new String[]{agencyId};
        return generateSdmxCodelistUrn(agenciesID, resourceId, version);
    }

    public static GeographicalValueRepository getGeographicalValueRepository() {
        return ApplicationContextProvider.getApplicationContext().getBean(GeographicalValueRepository.class);
    }

    public static Do2DtoMapper getDo2DtoMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(Do2DtoMapper.class);
    }
}
