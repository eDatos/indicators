package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.mapper.SrmRestObjectsMapper;

/**
 * The GeographicalValuesOldVersionCompatibilityUtils is implemented for compatibility with old indicators functionality where geographical codes were codes of codelist. Now, geographical codes are
 * variable elements of codelist codes.
 * But certain functionalities as indicators widgets that were created before the new functionality have still codes of codelists. Its necessary to convert geographical codes to variable elements.
 */
public class GeographicalValuesOldVersionCompatibilityUtils {

    protected static Logger logger                                      = LoggerFactory.getLogger(GeographicalValuesOldVersionCompatibilityUtils.class);
    Map<String, String>     variableElementsByGeographicalCode          = new HashMap<>();
    List<String>            originalSelectedGeographicalRepresentations = new ArrayList<>();
    private boolean         indicatorInstance                           = false;
    List<String>            selectedRepresentationOldCodes              = new ArrayList<>();                                                            // only for indicator systems not for individual
    SrmRestObjectsMapper    srmRestObjectsMapper                        = new SrmRestObjectsMapper();                                                   // indicators

    /**
     * if geographical representations exist, it must be checked that are variable element values. If the first value is not a variable element, the system supposes that is a geographical code and the
     * conversion to variable element is tried. if the conversion is not possible an error is thrown.
     * Precondition: The relation is one to one. One variable element to one geographical code. In widgets are only one geographical code.
     * 
     * @param indicatorsApiService the indicator service interface
     * @param srmRestInternalFacade srm api interface
     * @param indicatorVersion indicator version associated to the representation
     * @param selectedRepresentations each entry contains values of each dimension to search. the geographical entry is one of them. If codelist codes are detected in this geographical entry, its
     *            values are change to variable elements.
     */

    public GeographicalValuesOldVersionCompatibilityUtils(Map<String, List<String>> representation) {
        init(representation);
    }

    public GeographicalValuesOldVersionCompatibilityUtils(Map<String, List<String>> representation, SrmRestObjectsMapper srmRestObjectsMapper) {
        this.srmRestObjectsMapper = srmRestObjectsMapper;
        init(representation);
    }

    private void init(Map<String, List<String>> representation) {
        List<String> geographicalSelectedValues = representation.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        if (geographicalSelectedValues != null) {
            this.originalSelectedGeographicalRepresentations = geographicalSelectedValues;
        }
    }

    public GeographicalValuesOldVersionCompatibilityUtils(Map<String, List<String>> representation, boolean isIndicatorInstance, SrmRestObjectsMapper srmRestObjectsMapper) {
        this(representation);
        this.srmRestObjectsMapper = srmRestObjectsMapper;
        indicatorInstance = isIndicatorInstance;
        selectedRepresentationOldCodes = new ArrayList<>(originalSelectedGeographicalRepresentations);
    }

    public GeographicalValuesOldVersionCompatibilityUtils() {
        // without imp
    }

    public void sortOriginalGeographicalValues(List<String> geographicalVariableElements) {
        List<String> sortGeographicalCodes = new ArrayList<>();
        for (String variableElement : geographicalVariableElements) {
            sortGeographicalCodes.add(getGeographicalCodeByVariableElement(variableElement));
        }
        this.originalSelectedGeographicalRepresentations = sortGeographicalCodes;
    }

    public Map<String, String> getVariableElementsByGeographicalCode() {
        return variableElementsByGeographicalCode;
    }

    public boolean needsCompatibilityGeographicalCodes() {
        return !variableElementsByGeographicalCode.isEmpty();
    }

    public String getVariableElementBycode(String geographicalCode) {
        return variableElementsByGeographicalCode.get(geographicalCode);
    }

    // The relation must be one to one. If more than one geographical value point to the same variable element the result is unpredictable.
    public String getGeographicalCodeByVariableElement(String variableElement) {
        for (Entry<String, String> entry : variableElementsByGeographicalCode.entrySet()) {
            if (entry.getValue().equals(variableElement)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<String> getOriginalSelectedGeographicalRepresentations() {
        return originalSelectedGeographicalRepresentations;
    }

    public boolean checkNeedCompatibility(GeographicalValuesRestFacade geographicalValuesRestFacade, Map<String, List<String>> selectedRepresentations) {
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        if (geographicalSelectedValues == null || geographicalSelectedValues.isEmpty()) {
            return false;
        }

        try {
            // check if one code is a variable element. If the code is not variable element returns null and needs compatibility.
            GeographicalValue geographicalValue = geographicalValuesRestFacade.findGeographicalValuesByCode(geographicalSelectedValues.get(0));
            return geographicalValue == null;

        } catch (MetamacException e) {
            logger.error("Error in codes compatibility. it has not been possible to check selected geographical value representation. ", e);
        }

        return false;

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

    public boolean allCodesConverted() {
        return selectedRepresentationOldCodes == null || selectedRepresentationOldCodes.isEmpty();
    }

    public void setNewCodesForSelectedRepresentation(Map<String, List<String>> selectedRepresentations) {
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        List<String> geographicalSelectedValuesTarget = new ArrayList<>();
        for (String geoOldCodeInSelectedRepresentation : geographicalSelectedValues) {
            geographicalSelectedValuesTarget.add(variableElementsByGeographicalCode.getOrDefault(geoOldCodeInSelectedRepresentation, geoOldCodeInSelectedRepresentation));
        }

        selectedRepresentations.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geographicalSelectedValuesTarget);

    }

    public void getGeoValuesWithOldCompatibility(List<GeographicalValueVO> geographicalValues) {
        if (needsCompatibilityGeographicalCodes()) {
            for (GeographicalValueVO geoValue : geographicalValues) {
                String variableElement = getGeographicalCodeByVariableElement(geoValue.getCode());
                if (variableElement != null) {
                    geoValue.setCode(variableElement);
                }
            }
        }
    }

    public void convertOldCodesForIndicatorsSystem(SrmRestInternalFacade srmRestInternalFacade, IndicatorVersion indicatorVersion) {
        try {
            Map<Long, Map<String, String>> geographicalValuesByDataSource = getGeographicalValuesByDataSource(srmRestInternalFacade, indicatorVersion);

            getVariableElementsByGeographicalCodes(geographicalValuesByDataSource);
        } catch (MetamacException e) {
            logger.error("it has not been possible to check selected geographical value representation. ", e);
        }

    }

    private List<String> getVariableElementsByGeographicalCodes(Map<Long, Map<String, String>> geographicalValuesByDataSource) {
        List<String> geographicalSelectedValues = new ArrayList<>(selectedRepresentationOldCodes);
        List<String> geographicalSelectedValuesTarget = new ArrayList<>();
        for (String geographicalCode : geographicalSelectedValues) {
            boolean existCode = false;
            for (Map.Entry<Long, Map<String, String>> geoValuesByCode : geographicalValuesByDataSource.entrySet()) {
                String variableElement = geoValuesByCode.getValue().get(geographicalCode);
                if (variableElement != null) {
                    geographicalSelectedValuesTarget.add(variableElement);
                    variableElementsByGeographicalCode.put(geographicalCode, variableElement);
                    selectedRepresentationOldCodes.remove(geographicalCode);
                    existCode = true;
                    break;
                }
            }
            if (!existCode) {
                existCode = getVariableElementByDefaultCodelist(geographicalCode, geographicalSelectedValuesTarget);
                if (existCode) {
                    selectedRepresentationOldCodes.remove(geographicalCode);
                }
            }

        }
        return geographicalSelectedValuesTarget;
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
            // since EDATOS-4698 we must check in default geographical codelist because temporal widgets are created with the relation between variable element and the codes of this codelist.
            if (!existCode && !getVariableElementByDefaultCodelist(geographicalCode, geographicalSelectedValuesTarget)) {

                geographicalSelectedValuesTarget.add(geographicalCode);
            }
        }
        return geographicalSelectedValuesTarget;
    }

    private boolean getVariableElementByDefaultCodelist(String geographicalCode, List<String> geographicalSelectedValuesTarget) {

        String variableElement = this.srmRestObjectsMapper.getGeographicalVariableElementsByCode().get(geographicalCode);
        if (variableElement != null) {
            geographicalSelectedValuesTarget.add(variableElement);
            variableElementsByGeographicalCode.put(geographicalCode, variableElement);
            return true;
        }
        return false;
    }

    public String setGeographicalValue(GeographicalValuesRestFacade geographicalValuesRestFacade, SrmRestInternalFacade srmRestInternalFacade, String geographicalValue,
            String defaultTerritoryVariableUrn) {

        if (geographicalValue == null) {
            return geographicalValue;
        }

        try {
            // check if it is a variable element.
            GeographicalValue geographicalVariableElement = geographicalValuesRestFacade.findGeographicalValuesByCode(geographicalValue);

            if ((geographicalVariableElement != null)) {
                return geographicalValue;
            }

            String[] paramsTerritoryVariable = UrnUtils.splitUrnItemScheme(defaultTerritoryVariableUrn);
            String variableCode = paramsTerritoryVariable[0];
            // search codes with contain this code
            Codes codes = srmRestInternalFacade.retrieveCodelistCodesByCode(geographicalValue, variableCode, 1);
            for (CodeResourceInternal code : codes.getCodes()) {
                // get the first code in a codelist with a Territory variable
                if (code.getVariableElement() != null) {
                    String[] params = UrnUtils.splitUrnItemScheme(code.getVariableElement().getUrn());
                    String[] nestedId = UrnUtils.splitUrnByDots(params[0]);

                    if (variableCode.equals(nestedId[0])) {
                        return code.getVariableElement().getId();
                    }
                }
            }

        } catch (MetamacException e) {
            logger.error("it has not been possible to check selected geographical value: " + geographicalValue, e);
        }
        return geographicalValue;
    }

    public boolean isIndicatorInstance() {
        return indicatorInstance;
    }

    public void setIndicatorInstance(boolean indicatorInstance) {
        this.indicatorInstance = indicatorInstance;
    }

}
