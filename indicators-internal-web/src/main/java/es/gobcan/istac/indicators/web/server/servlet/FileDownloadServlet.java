package es.gobcan.istac.indicators.web.server.servlet;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.opensaml.artifact.InvalidArgumentException;
import org.siemac.metamac.web.common.server.servlet.FileDownloadServletBase;
import org.siemac.metamac.web.common.shared.utils.SharedTokens;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;

public class FileDownloadServlet extends FileDownloadServletBase {

    private static final long serialVersionUID = 6267996624034326676L;

    @Override
    protected File getFileToDownload(HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty(request.getParameter(SharedTokens.PARAM_DOC))) {
            return getFileFromDocs(request.getParameter(SharedTokens.PARAM_DOC));
        } else if (!StringUtils.isEmpty(request.getParameter(SharedTokens.PARAM_FILE_NAME))) {
            return getFileFromTempDir(request.getParameter(SharedTokens.PARAM_FILE_NAME));
        } else {
            throw new InvalidArgumentException("You must specify some action");
        }

    }

    private File getFileFromDocs(String fileName) throws Exception {
        checkValidFileName(fileName);
        return new File(File.separatorChar + fileName);
    }
}
