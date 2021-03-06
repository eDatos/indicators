package es.gobcan.istac.indicators.web.client.view;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.ViewImpl;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;

public abstract class PaginationViewImpl<C extends UiHandlers> extends ViewImpl implements HasUiHandlers<C> {

    private C                 uiHandlers;

    protected int             numberOfElements;
    protected int             numberSelected;
    protected int             pageNumber;

    protected final StatusBar statusBar;

    @Inject
    public PaginationViewImpl(StatusBar statusBar) {
        super();

        this.statusBar = statusBar;

    }

    protected C getUiHandlers() {
        return uiHandlers;
    }

    @Override
    public void setUiHandlers(C uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumberSelected() {
        return numberSelected;
    }

    public void setNumberSelected(int numberSelected) {
        this.numberSelected = numberSelected;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void refreshStatusBar() {
        // update Selected label e.g "0 of 50 selected"
        String selectedLabel = IndicatorsWeb.getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
        getStatusBar().getSelectedLabel().setContents(selectedLabel);

        // update Page number label e.g "Page 1"
        String pageNumberLabel = IndicatorsWeb.getMessages().page(String.valueOf(getPageNumber()));
        getStatusBar().getPageNumberLabel().setContents(pageNumberLabel);
        getStatusBar().getPageNumberLabel().markForRedraw();
    }

}
