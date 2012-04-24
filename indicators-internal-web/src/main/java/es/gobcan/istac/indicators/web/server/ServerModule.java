package es.gobcan.istac.indicators.web.server;

import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.spring.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.ArchiveIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.ArchiveIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDataSourcesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataDefinitionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataDefinitionsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataSourceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataSourcesListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDataStructureActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValueActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesWithGranularityInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemPaginatedListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemStructureActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetQuantityUnitsListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetSubjectsListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetTimeGranularitiesInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetTimeValuesInIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetUserPrincipalActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.MoveSystemStructureContentActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PopulateIndicatorDataActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PublishIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.PublishIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorsSystemDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.RejectIndicatorsSystemProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveDataSourceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorToDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorToProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorsSystemToDiffusionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.SendIndicatorsSystemToProductionValidationActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.VersioningIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.VersioningIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourceAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDimensionAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesWithGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetUserPrincipalAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceAction;
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

        // Indicators System
        bindHandler(GetIndicatorsSystemPaginatedListAction.class, GetIndicatorsSystemPaginatedListActionHandler.class);
        bindHandler(GetIndicatorsSystemByCodeAction.class, GetIndicatorsSystemByCodeActionHandler.class);
        bindHandler(GetIndicatorsSystemStructureAction.class, GetIndicatorsSystemStructureActionHandler.class);

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

        bindHandler(GetQuantityUnitsListAction.class, GetQuantityUnitsListActionHandler.class);
        bindHandler(GetGeographicalGranularitiesAction.class, GetGeographicalGranularitiesActionHandler.class);
        bindHandler(GetGeographicalValuesAction.class, GetGeographicalValuesActionHandler.class);
        bindHandler(GetGeographicalValueAction.class, GetGeographicalValueActionHandler.class);
        bindHandler(GetSubjectsListAction.class, GetSubjectsListActionHandler.class);

        bindHandler(PopulateIndicatorDataAction.class, PopulateIndicatorDataActionHandler.class);

        // Indicators geographical and temporal variables and values
        bindHandler(GetGeographicalGranularitiesInIndicatorAction.class, GetGeographicalGranularitiesInIndicatorActionHandler.class);
        bindHandler(GetGeographicalValuesWithGranularityInIndicatorAction.class, GetGeographicalValuesWithGranularityInIndicatorActionHandler.class);
        bindHandler(GetTimeGranularitiesInIndicatorAction.class, GetTimeGranularitiesInIndicatorActionHandler.class);
        bindHandler(GetTimeValuesInIndicatorAction.class, GetTimeValuesInIndicatorActionHandler.class);

        // Data Sources
        bindHandler(GetDataDefinitionsAction.class, GetDataDefinitionsActionHandler.class);
        bindHandler(GetDataDefinitionAction.class, GetDataDefinitionActionHandler.class);
        bindHandler(GetDataStructureAction.class, GetDataStructureActionHandler.class);

        bindHandler(GetUserPrincipalAction.class, GetUserPrincipalActionHandler.class);

    }

}
