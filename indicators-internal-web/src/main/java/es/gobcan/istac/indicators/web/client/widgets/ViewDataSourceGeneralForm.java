package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;

public class ViewDataSourceGeneralForm extends GroupDynamicForm {

    protected IndicatorUiHandler uiHandlers;

    public ViewDataSourceGeneralForm(String groupTitle) {
        super(groupTitle);

        ViewTextItem dataSourceQueryEnvironment = new ViewTextItem(DataSourceDS.QUERY_ENVIRONMENT, getConstants().dataSourceQueryEnvironment());
        
        ViewTextItem query = new ViewTextItem(DataSourceDS.QUERY_TEXT, getConstants().dataSourceQuery());
        query.setShowIfCondition(new FormItemIfFunction() {
            
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                String valueAsString = form.getValueAsString(DataSourceDS.QUERY_ENVIRONMENT);
                if (!StringUtils.isEmpty(valueAsString)) {
                    QueryEnvironmentEnum queryEnvironmentEnum = QueryEnvironmentEnum.valueOf(valueAsString);
                    if (QueryEnvironmentEnum.GPE.equals(queryEnvironmentEnum)) {
                        return true;
                    }
                }
                return false;
            }
        });
        
        ExternalItemLinkItem queryMetamac = new ExternalItemLinkItem(DataSourceDS.QUERY_METAMAC, getConstants().dataSourceQuery());
        queryMetamac.setShowIfCondition(new FormItemIfFunction() {
            
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                String valueAsString = form.getValueAsString(DataSourceDS.QUERY_ENVIRONMENT);
                if (!StringUtils.isEmpty(valueAsString)) {
                    QueryEnvironmentEnum queryEnvironmentEnum = QueryEnvironmentEnum.valueOf(valueAsString);
                    if (QueryEnvironmentEnum.METAMAC.equals(queryEnvironmentEnum)) {
                        return true;
                    }
                }
                return false;
            }
        });
        
        
        
        ViewTextItem surveyCode = new ViewTextItem(DataSourceDS.SOURCE_SURVEY_CODE, getConstants().dataSourceSurveyCode());

        ViewMultiLanguageTextItem surveyTitle = new ViewMultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_TITLE, getConstants().dataSourceSurveyTitle());

        ViewMultiLanguageTextItem surveyAcronym = new ViewMultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_ACRONYM, getConstants().dataSourceSurveyAcronym());

        ViewTextItem surveyUrl = new ViewTextItem(DataSourceDS.SOURCE_SURVEY_URL, getConstants().dataSourceSurveyUrl());

        ViewTextItem publishers = new ViewTextItem(DataSourceDS.PUBLISHERS, getConstants().dataSourcePublishers());

        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        timeVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !StringUtils.isBlank(value.toString());
            }
        });

        ViewTextItem timeValue = new ViewTextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        timeValue.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.TIME_VARIABLE).isVisible();
            }
        });

        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        geographicalVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !StringUtils.isBlank(value.toString());
            }
        });

        ViewTextItem geographicalValue = new ViewTextItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        geographicalValue.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.GEO_VARIABLE).isVisible();
            }
        });

        ViewTextItem measureVariable = new ViewTextItem(DataSourceDS.MEASURE_VARIABLE, getConstants().dataSourceMeasureVariable());
        measureVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !StringUtils.isBlank(value.toString());
            }
        });

        ViewVariableCanvasItem variables = new ViewVariableCanvasItem(DataSourceDS.OTHER_VARIABLES, getConstants().dataSourceOtherVariables());

        setFields(dataSourceQueryEnvironment, query, queryMetamac, surveyCode, surveyTitle, surveyAcronym, surveyUrl, publishers, timeVariable, timeValue, geographicalVariable, geographicalValue, measureVariable, variables);

    }

    public void setValue(DataSourceDto dataSourceDto) {
        setValue(DataSourceDS.QUERY_ENVIRONMENT, (dataSourceDto.getQueryEnvironment() != null) ? dataSourceDto.getQueryEnvironment().toString() : StringUtils.EMPTY);
        
        setValue(DataSourceDS.QUERY_TEXT, ""); // Set in method setDataDefinition
        if (!StringUtils.isBlank(dataSourceDto.getDataGpeUuid()) && QueryEnvironmentEnum.GPE.equals(dataSourceDto.getQueryEnvironment())) {
            uiHandlers.retrieveDataDefinition(dataSourceDto.getDataGpeUuid());
        }
        
        setValue(DataSourceDS.QUERY_METAMAC, dataSourceDto.getStatResource());

        setValue(DataSourceDS.SOURCE_SURVEY_CODE, dataSourceDto.getSourceSurveyCode());
        setValue(DataSourceDS.SOURCE_SURVEY_TITLE, dataSourceDto.getSourceSurveyTitle());
        setValue(DataSourceDS.SOURCE_SURVEY_ACRONYM, dataSourceDto.getSourceSurveyAcronym());
        setValue(DataSourceDS.SOURCE_SURVEY_URL, dataSourceDto.getSourceSurveyUrl());
        setValue(DataSourceDS.PUBLISHERS, CommonWebUtils.getStringListToString(dataSourceDto.getPublishers()));

        // ABSOLUTE_METHOD set in setDataStructure method in DataSourcesPanel
        // if (DataSourceDto.OBS_VALUE.equals(dataSourceDto.getAbsoluteMethod())) {
        // setValue(DataSourceDS.ABSOLUTE_METHOD, getConstants().dataSourceObsValue());
        // } else {
        // setValue(DataSourceDS.ABSOLUTE_METHOD, dataSourceDto.getAbsoluteMethod());
        // }

        setValue(DataSourceDS.TIME_VARIABLE, dataSourceDto.getTimeVariable());
        setValue(DataSourceDS.TIME_VALUE, dataSourceDto.getTimeValue());
        setValue(DataSourceDS.GEO_VARIABLE, dataSourceDto.getGeographicalVariable());
        setValue(DataSourceDS.GEO_VALUE, ""); // Set in method setGeographicalValue
        if (dataSourceDto.getGeographicalValueUuid() != null && !dataSourceDto.getGeographicalValueUuid().isEmpty()) {
            uiHandlers.retrieveGeographicalValueDS(dataSourceDto.getGeographicalValueUuid());
        }
        setValue(DataSourceDS.MEASURE_VARIABLE, ""); // Set in setMeasureVariable method

        // OTHER_VARIABLES set in setVariablesAndCategories method in ViewVariableCanvasItem
        // ((ViewVariableCanvasItem) getItem(DataSourceDS.OTHER_VARIABLES)).setValue(dataSourceDto.getOtherVariables());
    }

    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
