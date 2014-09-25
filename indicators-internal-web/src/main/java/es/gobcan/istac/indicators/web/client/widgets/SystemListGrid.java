package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants.SYSTEMS_LISTGRID_MAX_RESULTS;

import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridField;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;

public class SystemListGrid extends CustomListGrid {

    public SystemListGrid() {
        setAutoFitData(Autofit.VERTICAL);
        setAutoFitMaxRecords(SYSTEMS_LISTGRID_MAX_RESULTS);
        setDataSource(new IndicatorDS());
        setUseAllDataSourceFields(false);
        setHeaderHeight(40);

        ListGridField uuid = new ListGridField(IndicatorsSystemsDS.UUID, "UUID");
        uuid.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        ListGridField code = new ListGridField(IndicatorsSystemsDS.CODE, getConstants().systemListHeaderIdentifier());
        code.setAlign(Alignment.LEFT);
        ListGridField title = new ListGridField(IndicatorsSystemsDS.TITLE, getConstants().systemListHeaderTitle());
        ListGridField version = new ListGridField(IndicatorsSystemsDS.VERSION, getConstants().systemDetailVersion());
        ListGridField status = new ListGridField(IndicatorsSystemsDS.PROC_STATUS, getConstants().systemDetailProcStatus());
        ListGridField diffusionVersion = new ListGridField(IndicatorsSystemsDS.VERSION_DIFF, getConstants().systemDetailVersion());
        ListGridField diffusionStatus = new ListGridField(IndicatorsSystemsDS.PROC_STATUS_DIFF, getConstants().systemDetailProcStatus());
        setFields(uuid, code, title, version, status, diffusionVersion, diffusionStatus);
        setHeaderSpans(new HeaderSpan(getConstants().system(), new String[]{IndicatorsSystemsDS.CODE, IndicatorsSystemsDS.TITLE}), new HeaderSpan(getConstants().systemProductionEnvironment(),
                new String[]{IndicatorsSystemsDS.VERSION, IndicatorsSystemsDS.PROC_STATUS}), new HeaderSpan(getConstants().systemDiffusionEnvironment(), new String[]{IndicatorsSystemsDS.VERSION_DIFF,
                IndicatorsSystemsDS.PROC_STATUS_DIFF}));
    }
}
