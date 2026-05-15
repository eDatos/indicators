package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;

import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.serviceimpl.util.InternationalStringUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.UnitUtils;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;
import es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants;
import es.gobcan.istac.indicators.rest.types.QuantityType;
import es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum;

public class MapperUtil {

    private MapperUtil() {
    }

    public static String getDefaultValue(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString, String defaultLanguage) {
        if (internationalString == null || CollectionUtils.isEmpty(internationalString.getTexts())) {
            return null;
        }

        for (org.siemac.metamac.core.common.ent.domain.LocalisedString text : internationalString.getTexts()) {
            if (Objects.equals(defaultLanguage, text.getLocale())) {
                return text.getLabel();
            }
        }

        return internationalString.getTexts().stream().findFirst().get().getLabel();
    }

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString, String defaultLanguage) {
        return InternationalStringUtils.getLocalisedLabel(internationalString, defaultLanguage, DEFAULT);
    }

    public static Map<String, String> getLocalisedLabel(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(defaultLanguage))
                    || (defaultLabel != null && defaultLabelLocale.equals(defaultLanguage) && localisedString.getLocale().startsWith(defaultLanguage))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static String getDefaultLabel(InternationalStringDto internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null) {
            return null;
        }
        for (LocalisedStringDto text : internationalString.getTexts()) {
            if (defaultLanguage.equals(text.getLocale())) {
                return text.getLabel();
            }
        }
        if (!internationalString.getTexts().isEmpty()) {
            return internationalString.getTexts().iterator().next().getLabel();
        }
        return null;
    }

    public static Map<String, String> getLocalisedLabel(InternationalStringDto internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedStringDto localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(defaultLanguage))
                    || (defaultLabel != null && defaultLabelLocale.equals(defaultLanguage) && localisedString.getLocale().startsWith(defaultLanguage))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static OperationIndicators getOperationIndicators(Operation operation, String defaultLanguage) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName(), defaultLanguage));
        target.setAcronym(getLocalisedLabel(operation.getAcronym(), defaultLanguage));
        target.setDescription(getLocalisedLabel(operation.getDescription(), defaultLanguage));
        target.setObjective(getLocalisedLabel(operation.getObjective(), defaultLanguage));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

    public static OperationIndicators getOperationIndicators(org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation operation, String defaultLanguage) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName(), defaultLanguage));
        target.setAcronym(getLocalisedLabel(operation.getAcronym(), defaultLanguage));
        target.setDescription(getLocalisedLabel(operation.getDescription(), defaultLanguage));
        target.setObjective(getLocalisedLabel(operation.getObjective(), defaultLanguage));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

    public static void setQuantityUnitMetadata(final Quantity source, QuantityType quantityType, MetadataProperties metadataProperties, SrmRestInternalFacade srmRestInternalFacade,
            SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        if (source.getUnit() != null) {

            Code codeQuantityUnit = getCodeQuantityUnit(srmRestInternalFacade, srmRestObjectsMapper, source.getUnit().getUrn());

            String quantityUnitSymbol = UnitUtils.getQuantityUnitSymbol(codeQuantityUnit, metadataProperties, IndicatorsRestApiConstants.DEFAULT);
            quantityType.setUnitSymbol(quantityUnitSymbol);

            String quantityUnitSymbolPosition = UnitUtils.getQuantityUnitSymbolPosition(codeQuantityUnit, metadataProperties, IndicatorsRestApiConstants.DEFAULT);

            if (quantityUnitSymbolPosition != null) {
                if (QuantityUnitSymbolPositionEnum.START.name().equals(quantityUnitSymbolPosition)) {
                    quantityType.setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
                } else {
                    quantityType.setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.END);
                }
            }
        }

    }

    private static Code getCodeQuantityUnit(SrmRestInternalFacade srmRestInternalFacade, SrmRestObjectsMapper srmRestObjectsMapper, String urnUnit) throws MetamacException {
        Code codeQuantityUnit = srmRestObjectsMapper.getCodeOfCodelistByUrn().get(urnUnit);

        if (codeQuantityUnit == null) {
            codeQuantityUnit = srmRestInternalFacade.retrieveCodeOfCodelistByUrn(urnUnit);
            srmRestObjectsMapper.getCodeOfCodelistByUrn().put(urnUnit, codeQuantityUnit);
        }
        return codeQuantityUnit;
    }
}
