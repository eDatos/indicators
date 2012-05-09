package es.gobcan.istac.indicators.web.client.indicator.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;

public interface IndicatorUiHandler extends UiHandlers {

    void saveIndicator(IndicatorDto indicator);

    void retrieveSubjects();
    void retrieveGeographicalValues(String geographicalGranularityUuid);
    void retrieveGeographicalValue(String geographicalValueUuid);

    void sendToProductionValidation(String uuid);
    void sendToDiffusionValidation(String uuid);
    void rejectValidation(IndicatorDto indicatorDto);
    void publish(String uuid);
    void archive(String uuid);

    void versioningIndicator(String uuid, VersionTypeEnum versionType);

    void populateData(String uuid, String version);
    
    // DataSource

    void retrieveDataDefinitions();
    void retrieveDataDefinition(String uuid);
    void retrieveDataStructure(String uuid);
    void retrieveGeographicalValueDS(String uuid);

    void saveDataSource(String indicatorUuid, DataSourceDto dataSourceDto);
    void deleteDataSource(List<String> uuid);

}
