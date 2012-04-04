package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.MasterHead;

public class IndicatorsMasterHead extends MasterHead {

	public IndicatorsMasterHead() {
	    super();
	    setTitleLabel(getConstants().appTitle());
	}
}
