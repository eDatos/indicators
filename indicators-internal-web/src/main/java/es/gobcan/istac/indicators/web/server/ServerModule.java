package es.gobcan.istac.indicators.web.server;

import org.siemac.metamac.web.common.server.handlers.CloseSessionActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetLoginPageUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetNavigationBarUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.LoadConfigurationPropertiesActionHandler;
import org.siemac.metamac.web.common.server.handlers.MockCASUserActionHandler;
import org.siemac.metamac.web.common.shared.CloseSessionAction;
import org.siemac.metamac.web.common.shared.GetLoginPageUrlAction;
import org.siemac.metamac.web.common.shared.GetNavigationBarUrlAction;
import org.siemac.metamac.web.common.shared.LoadConfigurationPropertiesAction;
import org.siemac.metamac.web.common.shared.MockCASUserAction;
import org.siemac.metamac.web.common.shared.ValidateTicketAction;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.spring.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.ArchiveIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.ArchiveIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDataSourcesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteGeoGranularitiesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteGeoValuesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorsSystemsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteQuantityUnitsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteUnitMultipliersActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.ExportSystemInDsplActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.FindDataDefinitionsByOperationCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.FindIndicatorsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataDefinitionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataDefinitionsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataDefinitionsOperationsCodesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataSourceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataSourcesListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataStructureActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularityActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValueActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesByGranularityInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorInstancePreviewUrlActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorPreviewUrlActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemStructureActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetQuantityUnitsListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetQuantityUnitsPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetSubjectsListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetTimeGranularitiesInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetTimeValuesByGranularityInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetUnitMultipliersActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetUnitMultipliersPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetUserGuideUrlActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetValuesListsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.MoveSystemStructureContentActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PopulateIndicatorDataActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PublishIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PublishIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorsSystemDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorsSystemProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveDataSourceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveGeoGranularityActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveGeoValueActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveQuantityUnitActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveUnitMultiplierActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorToDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorToProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorsSystemToDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorsSystemToProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.ValidateTicketActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.VersioningIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.VersioningIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoValuesAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsAction;
import es.gobcan.istac.indicators.web.shared.DeleteUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.ExportSystemInDsplAction;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourceAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDimensionAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularityAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlAction;
import es.gobcan.istac.indicators.web.shared.GetValuesLists;
import es.gobcan.istac.indicators.web.shared.GetValuesListsAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoGranularityAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoValueAction;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitAction;
import es.gobcan.istac.indicators.web.shared.SaveUnitMultiplierAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorsSystemAction;

@Component
public class ServerModule extends HandlerModule {

    public ServerModule() {
    }

    @Override
    protected void configureHandlers() {
        // App management
        bindHandler(GetValuesListsAction.class, GetValuesListsActionHandler.class);

        // Admin - Quantity units
        bindHandler(SaveQuantityUnitAction.class, SaveQuantityUnitActionHandler.class);
        bindHandler(DeleteQuantityUnitsAction.class, DeleteQuantityUnitsActionHandler.class);
        bindHandler(GetQuantityUnitsListAction.class, GetQuantityUnitsListActionHandler.class);
        bindHandler(GetQuantityUnitsPaginatedListAction.class, GetQuantityUnitsPaginatedListActionHandler.class);

        // Admin - Geo granularities
        bindHandler(SaveGeoGranularityAction.class, SaveGeoGranularityActionHandler.class);
        bindHandler(DeleteGeoGranularitiesAction.class, DeleteGeoGranularitiesActionHandler.class);
        bindHandler(GetGeographicalGranularitiesAction.class, GetGeographicalGranularitiesActionHandler.class);
        bindHandler(GetGeographicalGranularitiesPaginatedListAction.class, GetGeographicalGranularitiesPaginatedListActionHandler.class);

        // Admin - Geo values
        bindHandler(SaveGeoValueAction.class, SaveGeoValueActionHandler.class);
        bindHandler(DeleteGeoValuesAction.class, DeleteGeoValuesActionHandler.class);
        bindHandler(GetGeographicalValuesPaginatedListAction.class, GetGeographicalValuesPaginatedListActionHandler.class);

        // Admin - unit multipliers
        bindHandler(SaveUnitMultiplierAction.class, SaveUnitMultiplierActionHandler.class);
        bindHandler(DeleteUnitMultipliersAction.class, DeleteUnitMultipliersActionHandler.class);
        bindHandler(GetUnitMultipliersAction.class, GetUnitMultipliersActionHandler.class);
        bindHandler(GetUnitMultipliersPaginatedListAction.class, GetUnitMultipliersPaginatedListActionHandler.class);

        // Indicators System
        bindHandler(GetIndicatorsSystemPaginatedListAction.class, GetIndicatorsSystemPaginatedListActionHandler.class);
        bindHandler(GetIndicatorsSystemByCodeAction.class, GetIndicatorsSystemByCodeActionHandler.class);
        bindHandler(GetIndicatorsSystemStructureAction.class, GetIndicatorsSystemStructureActionHandler.class);
        bindHandler(DeleteIndicatorsSystemsAction.class, DeleteIndicatorsSystemsActionHandler.class);
        bindHandler(ExportSystemInDsplAction.class, ExportSystemInDsplActionHandler.class);

        // Indicators System Structure
        bindHandler(GetDimensionAction.class, GetDimensionActionHandler.class);
        bindHandler(CreateDimensionAction.class, CreateDimensionActionHandler.class);
        bindHandler(UpdateDimensionAction.class, UpdateDimensionActionHandler.class);
        bindHandler(DeleteDimensionAction.class, DeleteDimensionActionHandler.class);
        bindHandler(GetIndicatorInstanceAction.class, GetIndicatorInstanceActionHandler.class);
        bindHandler(CreateIndicatorInstanceAction.class, CreateIndicatorInstanceActionHandler.class);
        bindHandler(UpdateIndicatorInstanceAction.class, UpdateIndicatorInstanceActionHandler.class);
        bindHandler(DeleteIndicatorInstanceAction.class, DeleteIndicatorInstanceActionHandler.class);
        bindHandler(MoveSystemStructureContentAction.class, MoveSystemStructureContentActionHandler.class);

        // Indicators System life cycle
        bindHandler(ArchiveIndicatorsSystemAction.class, ArchiveIndicatorsSystemActionHandler.class);
        bindHandler(SendIndicatorsSystemToProductionValidationAction.class, SendIndicatorsSystemToProductionValidationActionHandler.class);
        bindHandler(SendIndicatorsSystemToDiffusionValidationAction.class, SendIndicatorsSystemToDiffusionValidationActionHandler.class);
        bindHandler(PublishIndicatorsSystemAction.class, PublishIndicatorsSystemActionHandler.class);
        bindHandler(RejectIndicatorsSystemDiffusionValidationAction.class, RejectIndicatorsSystemDiffusionValidationActionHandler.class);
        bindHandler(RejectIndicatorsSystemProductionValidationAction.class, RejectIndicatorsSystemProductionValidationActionHandler.class);
        bindHandler(VersioningIndicatorsSystemAction.class, VersioningIndicatorsSystemActionHandler.class);

        // Indicators
        bindHandler(CreateIndicatorAction.class, CreateIndicatorActionHandler.class);
        bindHandler(UpdateIndicatorAction.class, UpdateIndicatorActionHandler.class);
        bindHandler(GetIndicatorListAction.class, GetIndicatorListActionHandler.class);
        bindHandler(GetIndicatorPaginatedListAction.class, GetIndicatorPaginatedListActionHandler.class);
        bindHandler(GetIndicatorByCodeAction.class, GetIndicatorByCodeActionHandler.class);
        bindHandler(GetIndicatorAction.class, GetIndicatorActionHandler.class);
        bindHandler(DeleteIndicatorsAction.class, DeleteIndicatorsActionHandler.class);
        bindHandler(FindIndicatorsAction.class, FindIndicatorsActionHandler.class);

        // Indicators life cycle
        bindHandler(ArchiveIndicatorAction.class, ArchiveIndicatorActionHandler.class);
        bindHandler(SendIndicatorToProductionValidationAction.class, SendIndicatorToProductionValidationActionHandler.class);
        bindHandler(SendIndicatorToDiffusionValidationAction.class, SendIndicatorToDiffusionValidationActionHandler.class);
        bindHandler(PublishIndicatorAction.class, PublishIndicatorActionHandler.class);
        bindHandler(RejectIndicatorDiffusionValidationAction.class, RejectIndicatorDiffusionValidationActionHandler.class);
        bindHandler(RejectIndicatorProductionValidationAction.class, RejectIndicatorProductionValidationActionHandler.class);
        bindHandler(VersioningIndicatorAction.class, VersioningIndicatorActionHandler.class);

        // Indicators DataSources
        bindHandler(SaveDataSourceAction.class, SaveDataSourceActionHandler.class);
        bindHandler(GetDataSourcesListAction.class, GetDataSourcesListActionHandler.class);
        bindHandler(GetDataSourceAction.class, GetDataSourceActionHandler.class);
        bindHandler(DeleteDataSourcesAction.class, DeleteDataSourcesActionHandler.class);

        bindHandler(GetGeographicalValuesAction.class, GetGeographicalValuesActionHandler.class);
        bindHandler(GetGeographicalValueAction.class, GetGeographicalValueActionHandler.class);
        bindHandler(GetSubjectsListAction.class, GetSubjectsListActionHandler.class);

        bindHandler(PopulateIndicatorDataAction.class, PopulateIndicatorDataActionHandler.class);

        // Indicators geographical and temporal variables and values
        bindHandler(GetGeographicalGranularitiesInIndicatorAction.class, GetGeographicalGranularitiesInIndicatorActionHandler.class);
        bindHandler(GetGeographicalValuesByGranularityInIndicatorAction.class, GetGeographicalValuesByGranularityInIndicatorActionHandler.class);
        bindHandler(GetGeographicalGranularityAction.class, GetGeographicalGranularityActionHandler.class);
        bindHandler(GetTimeGranularitiesInIndicatorAction.class, GetTimeGranularitiesInIndicatorActionHandler.class);
        bindHandler(GetTimeValuesByGranularityInIndicatorAction.class, GetTimeValuesByGranularityInIndicatorActionHandler.class);

        // Data Sources
        bindHandler(GetDataDefinitionsAction.class, GetDataDefinitionsActionHandler.class);
        bindHandler(GetDataDefinitionAction.class, GetDataDefinitionActionHandler.class);
        bindHandler(GetDataDefinitionsOperationsCodesAction.class, GetDataDefinitionsOperationsCodesActionHandler.class);
        bindHandler(FindDataDefinitionsByOperationCodeAction.class, FindDataDefinitionsByOperationCodeActionHandler.class);
        bindHandler(GetDataStructureAction.class, GetDataStructureActionHandler.class);

        bindHandler(GetIndicatorPreviewUrlAction.class, GetIndicatorPreviewUrlActionHandler.class);
        bindHandler(GetIndicatorInstancePreviewUrlAction.class, GetIndicatorInstancePreviewUrlActionHandler.class);

        bindHandler(ValidateTicketAction.class, ValidateTicketActionHandler.class);
        bindHandler(GetLoginPageUrlAction.class, GetLoginPageUrlActionHandler.class);
        bindHandler(CloseSessionAction.class, CloseSessionActionHandler.class);
        bindHandler(GetNavigationBarUrlAction.class, GetNavigationBarUrlActionHandler.class);
        bindHandler(LoadConfigurationPropertiesAction.class, LoadConfigurationPropertiesActionHandler.class);

        bindHandler(GetUserGuideUrlAction.class, GetUserGuideUrlActionHandler.class);

        // This action should be removed to use CAS authentication
        bindHandler(MockCASUserAction.class, MockCASUserActionHandler.class);
    }
}
