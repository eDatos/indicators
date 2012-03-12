package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.web.client.enums.GeographicalSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.QuantityIndexBaseTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.TimeSelectionTypeEnum;


public class CommonUtils {

    public static LinkedHashMap<String, String> getIndicatorsValueMap(List<IndicatorDto> indicatorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
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
        
    public static LinkedHashMap<String, String> getQuantityValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityTypeEnum type : QuantityTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + type.getName()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getQuantityIndexBaseMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
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
            valueMap.put(geographicalGranularityDto.getUuid(), InternationalStringUtils.getLocalisedString(geographicalGranularityDto.getTitle()));
        }
        return valueMap;
    }
    
    public static LinkedHashMap<String, String> getGeographicalValuesValueMap(List<GeographicalValueDto> geographicalValueDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (GeographicalValueDto geographicalValueDto : geographicalValueDtos) {
            valueMap.put(geographicalValueDto.getUuid(), InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
        }
        return valueMap;
    }
    
}
