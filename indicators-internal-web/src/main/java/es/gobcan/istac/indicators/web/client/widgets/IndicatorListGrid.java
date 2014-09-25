package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.CustomListGrid;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridField;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class IndicatorListGrid extends CustomListGrid {

    public IndicatorListGrid() {
        setAutoFitData(Autofit.VERTICAL);
        setAutoFitMaxRecords(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
        setDataSource(new IndicatorDS());
        setUseAllDataSourceFields(false);
        setHeaderHeight(40);

        ListGridField fieldCode = new ListGridField(IndicatorDS.CODE, getConstants().indicListHeaderIdentifier());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        ListGridField version = new ListGridField(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ListGridField status = new ListGridField(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ListGridField needsUpdate = new ListGridField(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        needsUpdate.setWidth(140);
        needsUpdate.setType(ListGridFieldType.IMAGE);
        needsUpdate.setAlign(Alignment.CENTER);
        ListGridField diffusionVersion = new ListGridField(IndicatorDS.VERSION_NUMBER_DIFF, getConstants().indicDetailVersion());
        ListGridField diffusionStatus = new ListGridField(IndicatorDS.PROC_STATUS_DIFF, getConstants().indicDetailProcStatus());
        ListGridField diffusionNeedsUpdate = new ListGridField(IndicatorDS.NEEDS_UPDATE_DIFF, getConstants().indicatorUpdateStatus());
        diffusionNeedsUpdate.setWidth(140);
        diffusionNeedsUpdate.setType(ListGridFieldType.IMAGE);
        diffusionNeedsUpdate.setAlign(Alignment.CENTER);

        setFields(fieldCode, fieldName, version, status, needsUpdate, diffusionVersion, diffusionStatus, diffusionNeedsUpdate);
        setHeaderSpans(new HeaderSpan(getConstants().indicator(), new String[]{IndicatorDS.CODE, IndicatorDS.TITLE}), new HeaderSpan(getConstants().indicatorProductionEnvironment(), new String[]{
                IndicatorDS.VERSION_NUMBER, IndicatorDS.PROC_STATUS, IndicatorDS.NEEDS_UPDATE}), new HeaderSpan(getConstants().indicatorDiffusionEnvironment(), new String[]{
                IndicatorDS.VERSION_NUMBER_DIFF, IndicatorDS.PROC_STATUS_DIFF, IndicatorDS.NEEDS_UPDATE_DIFF}));
    }
}
