package es.gobcan.istac.indicators.web.diffusion.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.web.diffusion.view.BreadcrumbList.Breadcrumb;

public class WebUtils {

    protected static Logger    logger         = LoggerFactory.getLogger(WebUtils.class);

    public static final String PARAM_APP_NAME = "appName";
    public static final String PARAM_APP_ID   = "appId";

    public static String buildHeaderQuery(String appName, BreadcrumbList breadcrumbList, String internationalizationUrlParamId, Locale locale) {
        // @formatter:off
        return buildQuery(
                buildBreadcrumbsQueryParams(breadcrumbList),
                buildAppNameQueryParam(appName),
                buildAppIdQueryParam(),
                buildLocaleQueryParam(internationalizationUrlParamId, locale)
        );
        // @formatter:on
    }

    /*
     * URL parameters are determined by what we are going to read on /complementos-apps/src/main/webapp/organisations/istac/portal/default/migas.jsp
     * Example: miga=hello|goodbye|myhome&enlace=http://hello.es|http://goodbye.es&
     */
    private static String buildBreadcrumbsQueryParams(BreadcrumbList breadcrumbList) {
        if (breadcrumbList == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("miga=");
        StringJoiner labelJoiner = new StringJoiner("|");
        for (Breadcrumb breadcrumb : breadcrumbList.getBreadcrumbs()) {
            labelJoiner.add(encodeParamValue(breadcrumb.getLabel()));
        }
        sb.append(labelJoiner.toString());

        sb.append("&");

        sb.append("enlace=");
        StringJoiner urlJoiner = new StringJoiner("|");
        for (Breadcrumb breadcrumb : breadcrumbList.getBreadcrumbs()) {
            urlJoiner.add(encodeParamValue(breadcrumb.getUrl()));
        }
        sb.append(urlJoiner.toString());

        return sb.toString();
    }

    private static String buildAppNameQueryParam(String appName) {
        return buildParam(PARAM_APP_NAME, appName);
    }

    private static String buildAppIdQueryParam() {
        return buildParam(PARAM_APP_ID, "indicators-visualizations");
    }

    private static String buildLocaleQueryParam(String internationalizationUrlParamId, Locale locale) {
        if (StringUtils.isBlank(internationalizationUrlParamId)) {
            return "";
        }
        return buildParam(internationalizationUrlParamId, locale.getLanguage());
    }

    private static String buildQuery(String... params) {
        StringJoiner joiner = new StringJoiner("&");
        for (String param : params) {
            if (param != null) {
                joiner.add(param);
            }
        }
        return joiner.length() > 0 ? "?" + joiner.toString() : "";
    }

    private static String buildParam(String param, String value) {
        if (value == null) {
            return "";
        }
        return param + "=" + encodeParamValue(value);
    }

    private static String encodeParamValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException encoding a constant. This is not supposed to EVER throw", e);
            return "";
        }
    }

}
