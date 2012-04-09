package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.GeographicalSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.QuantityIndexBaseTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.TimeSelectionTypeEnum;


public class CommonUtils {

    public static LinkedHashMap<String, String> getIndicatorsValueMap(List<IndicatorDto> indicatorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (IndicatorDto indicatorDto : indicatorDtos) {
            valueMap.put(indicatorDto.getUuid(), indicatorDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(indicatorDto.getTitle()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getTimeGranularityValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(); 
        for (TimeGranularityEnum type : TimeGranularityEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().timeGranularityEnum() + type.getName()));
        }
        return valueMap;
    }
        
    public static LinkedHashMap<String, String> getQuantityTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityTypeEnum type : QuantityTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getDataSourceQuantityTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(QuantityTypeEnum.AMOUNT.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + QuantityTypeEnum.AMOUNT.getName()));
        valueMap.put(QuantityTypeEnum.CHANGE_RATE.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + QuantityTypeEnum.CHANGE_RATE.getName()));        
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getQuantityIndexBaseMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (QuantityIndexBaseTypeEnum type : QuantityIndexBaseTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityIndexBaseTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getTimeSelectionTypeMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (TimeSelectionTypeEnum type : TimeSelectionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().timeSelectionTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getGeographicalSelectionTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (GeographicalSelectionTypeEnum type : GeographicalSelectionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().geographicalSelectionTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getGeographicalGranularituesValueMap(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (GeographicalGranularityDto geographicalGranularityDto : geographicalGranularityDtos) {
            valueMap.put(geographicalGranularityDto.getUuid(), geographicalGranularityDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(geographicalGranularityDto.getTitle()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getGeographicalValuesValueMap(List<GeographicalValueDto> geographicalValueDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (GeographicalValueDto geographicalValueDto : geographicalValueDtos) {
            valueMap.put(geographicalValueDto.getUuid(), geographicalValueDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getSubjectsValueMap(List<SubjectDto> subjectDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (SubjectDto subjectDto : subjectDtos) {
            valueMap.put(subjectDto.getCode(), subjectDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(subjectDto.getTitle()));
        }
        return valueMap;
    }
    
    public static InternationalStringDto getSubjectTitleFromCode(List<SubjectDto> subjectDtos, String code) {
        if (code != null) {
            for (SubjectDto subjectDto : subjectDtos) {
                if (code.equals(subjectDto.getCode())) {
                    return subjectDto.getTitle();
                }
            }
        }
        return null;
    }
    
    public static LinkedHashMap<String, String> getVersionTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(); 
        for (VersionTypeEnum type : VersionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().versionTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getDataBasicValueMap(List<DataDefinitionDto> dataDefinitionDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(); 
        for (DataDefinitionDto dataBasic : dataDefinitionDtos) {
            valueMap.put(dataBasic.getUuid(), dataBasic.getName());
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getRateDerivationMethodTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (RateDerivationMethodTypeEnum type : RateDerivationMethodTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().rateDerivationMethodTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getRateDerivationRoundingValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (RateDerivationRoundingEnum type : RateDerivationRoundingEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().rateDerivationRoundingEnum() + type.getName()));
        }
        return valueMap;
    }
    
    /**
     * Returns null if UUID parameter is blank
     * 
     * @param uuid
     * @return
     */
    public static String getUuidString(String uuid) {
        return StringUtils.isBlank(uuid) ? null : uuid;
    }
    
    public static LinkedHashMap<String, String> getVariableCategoriesValueMap(List<String> codes, List<String> labels) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (int i = 0; i < codes.size(); i++) {
            valueMap.put(codes.get(i), codes.get(i) + " - " + labels.get(i));
        }
        return valueMap;
    }
    
}