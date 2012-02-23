package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentResult;

public class MoveSystemStructureContentHandler extends AbstractActionHandler<MoveSystemStructureContentAction, MoveSystemStructureContentResult>{
	
	public MoveSystemStructureContentHandler() {
		super(MoveSystemStructureContentAction.class);
	}
	
	@Override
	public MoveSystemStructureContentResult execute(MoveSystemStructureContentAction action, ExecutionContext context) throws ActionException {
		List<Object> contents = action.getContents();
		Long order = action.getTargetOrderInLevel();
		for (Object node : contents) {
			if (node instanceof DimensionDto) {
				DimensionDto dim = (DimensionDto)node;
				IndDatabase.moveDimension(dim.getUuid(), action.getSystemUuid(), action.getTargetDimensionUuid(), order++);
			} else if (node instanceof IndicatorInstanceDto) {
				IndicatorInstanceDto inst = (IndicatorInstanceDto)node;
				IndDatabase.moveIndicatorInstance(inst.getUuid(), action.getSystemUuid(), action.getTargetDimensionUuid(), order++);
			}
		}
		return new MoveSystemStructureContentResult();
	}
	
	@Override
	public void undo(MoveSystemStructureContentAction action, MoveSystemStructureContentResult result, ExecutionContext context) throws ActionException {
		
	}
}
