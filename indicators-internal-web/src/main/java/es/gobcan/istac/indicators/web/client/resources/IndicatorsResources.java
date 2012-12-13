package es.gobcan.istac.indicators.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface IndicatorsResources extends ClientBundleWithLookup {

    public static final IndicatorsResources RESOURCE = GWT.create(IndicatorsResources.class);

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/ok_apply.png")
    ImageResource okApply();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/validate_production.png")
    ImageResource validateProduction();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/validate_diffusion.png")
    ImageResource validateDifussion();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/publish.png")
    ImageResource publish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reject.png")
    ImageResource reject();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/archive.png")
    ImageResource archive();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/version.png")
    ImageResource version();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reset.png")
    ImageResource reset();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext.png")
    ImageResource resultSetNext();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst.png")
    ImageResource resultSetFirst();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast.png")
    ImageResource resultSetLast();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious.png")
    ImageResource resultSetPrevious();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext_Disabled.png")
    ImageResource resultSetNextDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst_Disabled.png")
    ImageResource resultSetFirstDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast_Disabled.png")
    ImageResource resultSetLastDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious_Disabled.png")
    ImageResource resultSetPreviousDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/db_populate.png")
    ImageResource populateData();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/preview.png")
    ImageResource preview();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/export.png")
    ImageResource export();
}
