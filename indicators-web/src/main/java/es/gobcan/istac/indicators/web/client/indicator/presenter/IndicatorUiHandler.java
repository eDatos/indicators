package es.gobcan.istac.indicators.web.client.indicator.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;


public interface IndicatorUiHandler extends UiHandlers {

    void saveIndicator(IndicatorDto indicator);
    
    void retrieveSubjects();
    void retrieveGeographicalValues(String geographicalGranularityUuid);
    void retrieveGeographicalValue(String geographicalValueUuid);
    
    void sendToProductionValidation(String uuid);
    void sendToDiffusionValidation(String uuid);
    void rejectValidation(String uuid);
    void publish(String uuid);
    void archive(String uuid);
    
    void versioningIndicator(String uuid, VersiontTypeEnum versionType);
    
}
