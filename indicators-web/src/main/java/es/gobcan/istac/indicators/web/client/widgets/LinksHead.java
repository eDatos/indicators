package es.gobcan.istac.indicators.web.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;

import es.gobcan.istac.indicators.web.client.NameTokens;

public class LinksHead extends HLayout {
	private ToolStripButton indSysBut;
	private ToolStripButton indBut;
	
	private PlaceManager placeManager;
	
	@Inject
	public LinksHead(PlaceManager placeManager) {
		super();
		this.placeManager = placeManager;

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		
		indSysBut = new ToolStripButton("Sistemas de Indicadores");
		toolStrip.addMember(indSysBut);
		
		toolStrip.addMember(new ToolStripSeparator());
		
		indBut = new ToolStripButton("Indicadores");
		toolStrip.addMember(indBut);
		
		this.addMember(toolStrip);
		
		bindEvents();
	}
	
	private void bindEvents() {
		indSysBut.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PlaceRequest systemListRequest = new PlaceRequest(NameTokens.systemListPage);
				List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
				placeRequestHierarchy.add(systemListRequest);
				placeManager.revealPlaceHierarchy(placeRequestHierarchy);
			}
		});
		
		indBut.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PlaceRequest indicatorListRequest = new PlaceRequest(NameTokens.indicatorListPage);
				List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
				placeRequestHierarchy.add(indicatorListRequest);
				placeManager.revealPlaceHierarchy(placeRequestHierarchy);
			}
		});
	}
}
