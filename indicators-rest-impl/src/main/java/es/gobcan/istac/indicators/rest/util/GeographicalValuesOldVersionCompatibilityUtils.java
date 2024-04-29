package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;

/**
 * The GeographicalValuesOldVersionCompatibilityUtils is implemented for compatibility with old indicators functionality where geographical codes were codes of codelist. Now, geographical codes are
 * variable elements of codelist codes.
 * But certain functionalities as indicators widgets that were created before the new functionality have still codes of codelists. Its necessary to convert geographical codes to variable elements.
 */
public class GeographicalValuesOldVersionCompatibilityUtils {

    protected static Logger logger                             = LoggerFactory.getLogger(GeographicalValuesOldVersionCompatibilityUtils.class);
    Map<String, String>     variableElementsByGeographicalCode = new HashMap<>();

    /**
     * if geographical representations exist, it must be checked that are variable element values. If the first value is not a variable element, the system supposes that is a geographical code and the
     * conversion to variable element is tried. if the conversion is not possible an error is thrown.
     *
     * @param indicatorsApiService the indicator service interface
     * @param srmRestInternalFacade srm api interface
     * @param indicatorVersion indicator version associated to the representation
     * @param selectedRepresentations each entry contains values of each dimension to search. the geographical entry is one of them. If codelist codes are detected in this geographical entry, its
     *            values are change to variable elements.
     */

    public GeographicalValuesOldVersionCompatibilityUtils() {
        // without impl.
    }

    public Map<String, String> getVariableElementsByGeographicalCode() {
        return variableElementsByGeographicalCode;
    }

    public void setGeographicalRepresentationByVariableElements(GeographicalValuesRestFacade geographicalValuesRestFacade, SrmRestInternalFacade srmRestInternalFacade,
            IndicatorVersion indicatorVersion, Map<String, List<String>> selectedRepresentations) {
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        if (geographicalSelectedValues == null || geographicalSelectedValues.isEmpty()) {
            return;
        }

        try {
            // check if one code is a variable element.
            GeographicalValue geographicalValue = geographicalValuesRestFacade.findGeographicalValuesByCode(geographicalSelectedValues.get(0));

            if ((geographicalValue != null)) {
                return;
            }

            Map<Long, Map<String, String>> geographicalValuesByDataSource = getGeographicalValuesByDataSource(srmRestInternalFacade, indicatorVersion);

            selectedRepresentations.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), getVariableElementsByGeographicalCodes(geographicalSelectedValues, geographicalValuesByDataSource));
        } catch (MetamacException e) {
            logger.error("it has not been possible to check selected geographical value representation. ", e);
        }

    }

    private Map<Long, Map<String, String>> getGeographicalValuesByDataSource(SrmRestInternalFacade srmRestInternalFacade, IndicatorVersion indicatorVersion) throws MetamacException {
        Map<Long, Map<String, String>> geographicalValuesByDataSource = new HashMap<>();
        for (DataSource dataSource : indicatorVersion.getDataSources()) {
            if (dataSource.getGeographicalCodelistUrn() != null) {
                Map<String, String> variableElementsByDatasourceByGeographicalCode = srmRestInternalFacade.retrieveVariableElementsIdByCodesOfCodelists(dataSource.getGeographicalCodelistUrn());
                geographicalValuesByDataSource.put(dataSource.getId(), variableElementsByDatasourceByGeographicalCode);
            }
        }
        return geographicalValuesByDataSource;
    }

    private List<String> getVariableElementsByGeographicalCodes(List<String> geographicalSelectedValues, Map<Long, Map<String, String>> geographicalValuesByDataSource) {
        List<String> geographicalSelectedValuesTarget = new ArrayList<>();
        for (String geographicalCode : geographicalSelectedValues) {
            boolean existCode = false;
            for (Map.Entry<Long, Map<String, String>> geoValuesByCode : geographicalValuesByDataSource.entrySet()) {
                String variableElement = geoValuesByCode.getValue().get(geographicalCode);
                if (variableElement != null) {
                    geographicalSelectedValuesTarget.add(variableElement);
                    variableElementsByGeographicalCode.put(geographicalCode, variableElement);
                    existCode = true;
                    break;
                }
            }
            // if it is not a geographical code, it is returned selected value and the behavior does not change respect normal functionality.
            if (!existCode) {
                geographicalSelectedValuesTarget.add(geographicalCode);
            }
        }
        return geographicalSelectedValuesTarget;
    }
}
