package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;

public class GetIndicatorsSystemStructureHandler extends AbstractActionHandler<GetIndicatorsSystemStructureAction, GetIndicatorsSystemStructureResult>{

	public GetIndicatorsSystemStructureHandler() {
		super(GetIndicatorsSystemStructureAction.class);
	}

	@Override
	public GetIndicatorsSystemStructureResult execute(GetIndicatorsSystemStructureAction action, ExecutionContext context) throws ActionException {
		List<DimensionDto> dimensions = IndDatabase.getIndicatorsSystemDimensions(action.getUuid());
		List<IndicatorInstanceDto> indicatorInstances = IndDatabase.getIndicatorsSystemIndicatorInstances(action.getUuid());
		
		GetIndicatorsSystemStructureResult result = new GetIndicatorsSystemStructureResult(dimensions, indicatorInstances);
		return result;
	}

	@Override
	public void undo(GetIndicatorsSystemStructureAction action, GetIndicatorsSystemStructureResult result, ExecutionContext context) throws ActionException {
		
	}
	

}
