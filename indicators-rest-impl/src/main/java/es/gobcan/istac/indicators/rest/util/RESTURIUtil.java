package es.gobcan.istac.indicators.rest.util;

public final class RESTURIUtil {

    /** The HTTP Link header field name. */
    public static final String LINK           = "Link";

    public static final String REL_COLLECTION = "collection";
    public static final String REL_NEXT       = "next";
    public static final String REL_PREV       = "prev";
    public static final String REL_FIRST      = "first";
    public static final String REL_LAST       = "last";
    public static final String LIMIT          = "limit";
    public static final String OFFSET         = "offset";

    private RESTURIUtil() {
        throw new AssertionError();
    }

    //

    public static String createLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    public static String gatherLinkHeaders(final String... uris) {
        final StringBuilder linkHeaderValue = new StringBuilder();
        for (final String uri : uris) {
            linkHeaderValue.append(uri);
            linkHeaderValue.append(", ");
        }
        return linkHeaderValue.substring(0, linkHeaderValue.length() - 2).toString();
    }

}
