package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.model.MessageRecord;

public class SuccessMessagePanel extends VLayout {

	private ListGrid grid;
	
	
	public SuccessMessagePanel() {
		super();
		setHeight(100);
		setVisibility(Visibility.HIDDEN);
		setOverflow(Overflow.AUTO);
		
		grid = new ListGrid();
		grid.setCellPadding(4);
		grid.setCellHeight(40);
		grid.setNormalCellHeight(40);
		grid.setAlternateRecordStyles(false);
		grid.setShowRollOverCanvas(true);
		grid.setAnimateRollUnder(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setBaseStyle("annotationCell");
        grid.setShowSelectionCanvas(false);
        grid.setAnimateSelectionUnder(true);
        grid.setWrapCells(true);
        grid.setShowHeader(false);
        grid.setBorder("1px solid white");
        grid.setCanEdit(false);
        grid.setEditEvent(ListGridEditEvent.NONE);
		Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#fff6c5");
        rollUnderCanvasProperties.setOpacity(50);
        grid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        Canvas background = new Canvas();
        background.setBackgroundColor("#FFFFE0");
        grid.setBackgroundComponent(background);
        
        
        // ListGrid Fields
        
        ListGridField messageField = new ListGridField(MessageRecord.TEXT, "message");
        grid.setFields(messageField);
        
        
		Img closeImg = new Img("close.png");
		closeImg.setTooltip("Cerrar");
		closeImg.setCursor(Cursor.POINTER);
		closeImg.setName("close-img");
		closeImg.setSize(12);
		closeImg.setAlign(Alignment.RIGHT);
		closeImg.setValign(VerticalAlignment.TOP);
		closeImg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SuccessMessagePanel.this.hide();
			}
		});
		
		Img successImg = new Img("success.png");
		successImg.setSize(24);
		successImg.setAlign(Alignment.LEFT);
		
		HLayout imgLayout = new HLayout();
		imgLayout.setAutoHeight();
		imgLayout.setBackgroundColor("#ffffe0");
		imgLayout.setLayoutMargin(5);
		imgLayout.addMember(successImg);
		imgLayout.addMember(new LayoutSpacer());
		imgLayout.addMember(closeImg);
        
        addMember(imgLayout);
        addMember(grid);
	}
	
	public void showMessage(List<String> messages) {
		grid.selectAllRecords();
		grid.removeSelectedData();
		grid.deselectAllRecords();
		for (String m : messages) {
			MessageRecord record = new MessageRecord(m);
			grid.addData(record);
		}
		show();
	}
	
}
