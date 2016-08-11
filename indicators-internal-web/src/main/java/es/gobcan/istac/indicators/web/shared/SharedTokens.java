package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.shared.SecurityCookie;

public class SharedTokens {

    public static final String FILE_DOWNLOAD_DIR_PATH = "files/download";
    public static final String PARAM_FILE_NAME        = "fileName";
    public static final String PARAM_DOC              = "doc";

    @SecurityCookie
    public static final String securityCookieName     = "securityCookieName";

}
