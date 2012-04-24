package es.gobcan.istac.indicators.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;

public interface IndicatorsResources extends ClientBundleWithLookup {

    public static final IndicatorsResources RESOURCE = GWT.create(IndicatorsResources.class);

    @Source("images/ok_apply.png")
    ImageResource okApply();

    @Source("images/validate_production.png")
    ImageResource validateProduction();

    @Source("images/validate_diffusion.png")
    ImageResource validateDifussion();

    @Source("images/publish.png")
    ImageResource publish();

    @Source("images/reject.png")
    ImageResource reject();

    @Source("images/archive.png")
    ImageResource archive();

    @Source("images/version.png")
    ImageResource version();

    @Source("images/reset.png")
    ImageResource reset();

    @Source("images/resultsetnext.png")
    ImageResource resultSetNext();

    @Source("images/resultsetfirst.png")
    ImageResource resultSetFirst();

    @Source("images/resultsetlast.png")
    ImageResource resultSetLast();

    @Source("images/resultsetprevious.png")
    ImageResource resultSetPrevious();

    @Source("images/resultsetnext_Disabled.png")
    ImageResource resultSetNextDisabled();

    @Source("images/resultsetfirst_Disabled.png")
    ImageResource resultSetFirstDisabled();

    @Source("images/resultsetlast_Disabled.png")
    ImageResource resultSetLastDisabled();

    @Source("images/resultsetprevious_Disabled.png")
    ImageResource resultSetPreviousDisabled();

    @Source("images/db_populate.png")
    ImageResource populateData();

}
