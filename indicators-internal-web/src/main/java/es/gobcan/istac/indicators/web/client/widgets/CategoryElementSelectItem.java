package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class CategoryElementSelectItem {

    private CustomDynamicForm            form;

    private IndicatorListUiHandler       uiHandlers;

    private SearchExternalItemSimpleItem categoryElement;

    public CategoryElementSelectItem(CustomDynamicForm form, Boolean isRequired, Integer width) {
        this.form = form;
        createCategoryElementsItem();
        categoryElement.setRequired(isRequired);
        if (width != null) {
            categoryElement.setWidth(width);
        }
    }

    public void createCategoryElementsItem() {
        categoryElement = new SearchExternalItemSimpleItem(IndicatorDS.CATEGORY_ELEMENT, getConstants().categoryElement(), IndicatorsWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                TypeExternalArtefactsEnum[] type = {TypeExternalArtefactsEnum.CATEGORY_ELEMENT};

                SrmItemRestCriteria restCriteria = new SrmItemRestCriteria();
                restCriteria.setExternalArtifactType(TypeExternalArtefactsEnum.CATEGORY_ELEMENT);
                restCriteria.setCriteria(webCriteria.getCriteria());

                getUiHandlers().retrieveItems(IndicatorDS.CATEGORY_ELEMENT, restCriteria, type, firstResult, maxResults);
            }
        };
    }

    public void setCategoryElementExternalItem(List<ExternalItemDto> categoryElementExternalItem, int firstResult, int totalResults) {
        categoryElement.setResources(categoryElementExternalItem, firstResult, totalResults);
    }

    public void setUiHandlers(IndicatorListUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public IndicatorListUiHandler getUiHandlers() {
        return this.uiHandlers;
    }

    public ExternalItemDto getValue() {
        return form.getValueAsExternalItemDto(IndicatorDS.CATEGORY_ELEMENT);
    }

    public SearchExternalItemSimpleItem getItem() {
        return categoryElement;
    }
}
