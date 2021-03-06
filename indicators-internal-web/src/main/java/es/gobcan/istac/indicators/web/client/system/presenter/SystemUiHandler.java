package es.gobcan.istac.indicators.web.client.system.presenter;

import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public interface SystemUiHandler extends UiHandlers {

    void createDimension(IndicatorsSystemDtoWeb system, DimensionDto dimension);
    void updateDimension(DimensionDto dimension);
    void deleteDimension(DimensionDto dimension);

    void createIndicatorInstance(IndicatorsSystemDtoWeb system, IndicatorInstanceDto instance);
    void updateIndicatorInstance(IndicatorInstanceDto instance);
    void deleteIndicatorInstance(IndicatorInstanceDto instance);
    void searchIndicator(IndicatorCriteria criteria);

    void moveSystemStructureNodes(String systemUuid, String targetUuid, ElementLevelDto level, Long newOrder);

    void retrieveIndSystem(String indSystemCode);
    void retrieveSystemStructure();

    void retrieveDiffusionIndSystem(String indSystemCode, String versionNumber);

    void retrieveIndicator(String uuid);

    void retrieveGeographicalValue(String geographicalValueUuid);

    void sendToProductionValidation(IndicatorsSystemDtoWeb indicatorsSystemDto);
    void sendToDiffusionValidation(IndicatorsSystemDtoWeb indicatorsSystemDto);
    void rejectValidation(IndicatorsSystemDtoWeb indicatorsSystemDto);
    void publish(IndicatorsSystemDtoWeb indicatorsSystemDto);
    void archive(IndicatorsSystemDtoWeb indicatorsSystemDto);

    void versioningIndicatorsSystem(IndicatorsSystemDtoWeb indicatorsSystemDto, VersionTypeEnum versionType);

    void exportIndicatorsSystemInDspl(IndicatorsSystemDtoWeb indicatorsSystemDto, boolean mergingTimeGranularities);

    // void populateIndicatorData(String uuid, String version);

    void previewData(String instanceCode, String systemCode);

    // Geographical and temporal values and variables for indicators (in instance creation)
    void retrieveTimeGranularitiesInIndicator(String indicatorUuid, String indicatorVersion);
    void retrieveTimeValuesWithGranularityInIndicator(String indicatorUuid, String indicatorVersion, IstacTimeGranularityEnum timeGranularity);
    void retrieveGeographicalGranularitiesInIndicator(String indicatorUuid, String indicatorVersion);
    void retrieveGeographicalValuesWithGranularityInIndicator(String indicatorUuid, String indicatorVersion, String geographicalGranularityUuid);

    void retrieveGeographicalGranularity(String geographicalGranularityUuid);
}
