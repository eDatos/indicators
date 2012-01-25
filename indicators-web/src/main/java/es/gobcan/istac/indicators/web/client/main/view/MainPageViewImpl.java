package es.gobcan.istac.indicators.web.client.main.view;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.widgets.ErrorMessagePanel;
import es.gobcan.istac.indicators.web.client.widgets.LinksHead;
import es.gobcan.istac.indicators.web.client.widgets.MasterHead;
import es.gobcan.istac.indicators.web.client.widgets.SuccessMessagePanel;

public class MainPageViewImpl extends ViewImpl implements MainPagePresenter.MainView {
	
	private static final int NORTH_HEIGHT = 85;
	
	private VLayout panel; // MAIN
	private VLayout northLayout;
	private HLayout southLayout;
	private HLayout footerLayout;
	
	private final MasterHead masterHead;
	private final LinksHead linksHead;
	private final SuccessMessagePanel successMessagePanel;
	private final ErrorMessagePanel errorMessagePanel;
		
	
	@Inject
	public MainPageViewImpl(MasterHead masterHead, LinksHead linksHead, SuccessMessagePanel successMessagePanel, ErrorMessagePanel errorMessagePanel) {
		this.masterHead = masterHead;
		this.linksHead = linksHead;
		this.successMessagePanel = successMessagePanel;
		this.errorMessagePanel = errorMessagePanel;
		
		panel = new VLayout();
		panel.setWidth100();
		panel.setHeight100();
		panel.setAlign(Alignment.CENTER);
		panel.setCanDrag(false);
		
		northLayout = new VLayout();
		northLayout.setHeight(NORTH_HEIGHT);
		northLayout.addMember(this.masterHead);
		northLayout.addMember(this.linksHead);
		northLayout.setHeight(65);
		
		// Initialize the South layout container
		southLayout = new HLayout();
		southLayout.setHeight100();
		
		//Footer
		footerLayout = new HLayout();
		footerLayout.addMember(successMessagePanel);
		footerLayout.addMember(errorMessagePanel);

		panel.addMember(northLayout);
		panel.addMember(southLayout);
		panel.addMember(footerLayout);
	}

	public Widget asWidget() {
		return panel;
	}
	

	/****************************************************
	 * Code for nested presenters.
	 ***************************************************/

	/*
	 * GWTP will call setInSlot when a child presenter asks to be added under this view
	 */
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPagePresenter.CONTENT_SLOT) {
			if (content != null) {
				//panel.add(content, "main");
				southLayout.removeMembers(southLayout.getMembers());
				southLayout.addMember(content);
			}
		} else {
			// To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call. 
			// Who knows, maybe the parent class knows what to do with this slot. 
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void removeFromSlot(Object slot, Widget content) {
		super.removeFromSlot(slot, content);
	}
	
	/****************************************************
	 * End code for nested presenters.
	 ***************************************************/

	@Override
	public void showMessage(List<String> messages, MessageTypeEnum type) {
		// Hide messages before showing the new ones
		hideMessages();
		if (MessageTypeEnum.SUCCESS.equals(type)) {
			successMessagePanel.showMessage(messages);
			Timer timer = new Timer() {
				@Override
				public void run() {
					successMessagePanel.animateHide(AnimationEffect.FADE);
				}
			};
			timer.schedule(6000);
		} else if (MessageTypeEnum.ERROR.equals(type)) {
			errorMessagePanel.showMessage(messages);
		}
	}

	@Override
	public void hideMessages() {
		successMessagePanel.hide();
		errorMessagePanel.hide();
	}
}
