package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.dspl.DsplData.Row;
import es.gobcan.istac.indicators.core.dspl.DsplData.TextColumn;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public class UnitUtils {

    private static final Logger LOG           = LoggerFactory.getLogger(UnitUtils.class);
    private static final String DEFAULT_LABEL = "DEFAULT";

    private UnitUtils() {
    }

    public static void setQuantityUnitMetadata(String unitUrn, Row row, IndicatorsConfigurationService configurationService, SrmRestInternalService srmRestInternalFacade) {
        try {
            if (unitUrn != null) {
                Code codeQuantityUnit = srmRestInternalFacade.retrieveCodeOfCodelist(unitUrn);
                row.addColumn(new TextColumn("symbol"), StringEscapeUtils.escapeCsv(getQuantityUnitSymbol(codeQuantityUnit, configurationService, DEFAULT_LABEL)));
                row.addColumn(new TextColumn("symbol_position"), StringEscapeUtils.escapeCsv(getQuantityUnitSymbolPosition(codeQuantityUnit, configurationService, DEFAULT_LABEL)));

            }
        } catch (Exception e) {
            LOG.error("ERROR trying to get symbol and symbol_position of unit from srm of unit: {}", unitUrn, e);
        }
    }

    public static String getQuantityUnitSymbol(Code codeQuantityUnit, MetadataProperties metadataProperties, String defaultLabel) throws MetamacException {
        return getQuantityUnitSymbol(codeQuantityUnit, metadataProperties.getDefaultInternationalizationLanguage(), defaultLabel);
    }

    public static String getQuantityUnitSymbol(Code codeQuantityUnit, IndicatorsConfigurationService configurationService, String defaultLabel) throws MetamacException {
        return getQuantityUnitSymbol(codeQuantityUnit, configurationService.retrieveDefaultInternationalizationLanguage(), defaultLabel);
    }

    public static String getQuantityUnitSymbol(Code codeQuantityUnit, String defaultInternalizationLanguage, String defaultLabel) throws MetamacException {
        if (codeQuantityUnit.getShortName() != null) {
            Map<String, String> shortName = InternationalStringUtils.getLocalisedLabel(codeQuantityUnit.getShortName(), defaultInternalizationLanguage, defaultLabel);
            if (shortName != null) {
                return shortName.get(defaultLabel);
            }
        }
        return null;
    }

    public static String getQuantityUnitSymbolPosition(Code codeQuantityUnit, MetadataProperties metadataProperties, String defaultLabel) throws MetamacException {
        return getQuantityUnitSymbolPosition(codeQuantityUnit, metadataProperties.getDefaultInternationalizationLanguage(), metadataProperties.getCodelistAnnotationTypePositionUnit(), defaultLabel);
    }

    public static String getQuantityUnitSymbolPosition(Code codeQuantityUnit, IndicatorsConfigurationService configurationService, String defaultLabel) throws MetamacException {
        return getQuantityUnitSymbolPosition(codeQuantityUnit, configurationService.retrieveDefaultInternationalizationLanguage(), configurationService.retrieveCodelistAnnotationTypePositionUnit(),
                defaultLabel);
    }

    public static String getQuantityUnitSymbolPosition(Code codeQuantityUnit, String defaultInternalizationLanguage, String annotationTypePositionCode, String defaultLabel) throws MetamacException {
        if (codeQuantityUnit.getAnnotations() != null && !codeQuantityUnit.getAnnotations().getAnnotations().isEmpty()) {
            for (Annotation annotation : codeQuantityUnit.getAnnotations().getAnnotations()) {
                if (annotationTypePositionCode.equalsIgnoreCase(annotation.getType()) && annotation.getText() != null) {
                    Map<String, String> annotationText = InternationalStringUtils.getLocalisedLabel(annotation.getText(), defaultInternalizationLanguage, defaultLabel);
                    if (annotationText != null && QuantityUnitSymbolPosition.START.value().equals(annotationText.get(defaultLabel))) {
                        return QuantityUnitSymbolPosition.START.value();
                    } else if (annotationText != null && QuantityUnitSymbolPosition.END.value().equals(annotationText.get(defaultLabel))) {
                        return QuantityUnitSymbolPosition.END.value();
                    }
                }
            }
        }
        return null;
    }
}
