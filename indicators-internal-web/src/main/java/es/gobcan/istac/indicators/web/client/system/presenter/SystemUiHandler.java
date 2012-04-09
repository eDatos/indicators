package es.gobcan.istac.indicators.web.client.system.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;

public interface SystemUiHandler extends UiHandlers {

    void createDimension(IndicatorsSystemDto system, DimensionDto dimension);
    void updateDimension(DimensionDto dimension);
    void deleteDimension(DimensionDto dimension);

    void createIndicatorInstance(IndicatorsSystemDto system, IndicatorInstanceDto instance);
    void updateIndicatorInstance(IndicatorInstanceDto instance);
    void deleteIndicatorInstance(IndicatorInstanceDto instance);

    void moveSystemStructureNodes(String systemUuid, String targetUuid, ElementLevelDto level, Long newOrder);

    void retrieveIndSystem(String indSystemCode);
    void retrieveSystemStructure();

    void retrieveIndicators();
    void retrieveIndicator(String uuid);

    void retrieveGeographicalValues(String geographicalGranularityUuid);
    void retrieveGeographicalValue(String geographicalValueUuid);

    void sendToProductionValidation(IndicatorsSystemDto indicatorsSystemDto);
    void sendToDiffusionValidation(String uuid);
    void rejectValidation(String uuid);
    void publish(String uuid);
    void archive(String uuid);

    void versioningIndicatorsSystem(String uuid, VersionTypeEnum versionType);

}
