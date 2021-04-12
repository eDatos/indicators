package es.gobcan.istac.indicators.web.diffusion.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.WebUtils;
import org.siemac.metamac.core.common.util.swagger.SwaggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.util.FreeMarkerUtil;

/**
 * FreeMarker view implementation to expose additional helpers
 */
public class FreeMarkerHelperView extends FreeMarkerView {

    private static IndicatorsConfigurationService configurationService;

    protected Logger                              logger                  = LoggerFactory.getLogger(getClass());

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String indicatorsExternalApiUrlBase = getConfigurationService().retrieveIndicatorsExternalApiUrlBase();
        model.put("indicatorsExternalApiUrlBase", WebUtils.normalizeUrl(indicatorsExternalApiUrlBase));
        model.put("indicatorsExternalApiUrlBaseSwagger", SwaggerUtils.normalizeUrlForSwagger(indicatorsExternalApiUrlBase));
        fillOptionalApiStyleHeaderUrl(model);
        fillOptionalApiStyleFooterUrl(model);
        fillOptionalApiStyleCssUrl(model);

        super.doRender(model, request, response);
    }

    private void fillOptionalApiStyleCssUrl(Map<String, Object> model) {
        try {
            model.put("apiStyleCssUrl", getConfigurationService().retrieveApiStyleCssUrl());
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalApiStyleFooterUrl(Map<String, Object> model) throws UnsupportedEncodingException, IOException {
        try {
            model.put("apiStyleFooter", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleFooterUrl()));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalApiStyleHeaderUrl(Map<String, Object> model) throws UnsupportedEncodingException, IOException {
        try {
            model.put("apiStyleHeader", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleHeaderUrl()));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }

}
