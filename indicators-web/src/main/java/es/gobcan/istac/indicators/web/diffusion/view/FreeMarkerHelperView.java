package es.gobcan.istac.indicators.web.diffusion.view;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * FreeMarker view implementation to expose additional helpers
 */
public class FreeMarkerHelperView extends FreeMarkerView {

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int port = request.getServerPort();

        if (request.getScheme().equals("http") && port == 80) {
            port = -1;
        } else if (request.getScheme().equals("https") && port == 443) {
            port = -1;
        }

        URL serverURL = new URL(request.getScheme(), request.getServerName(), port, "");
        model.put("serverURL", serverURL.toString());

        super.doRender(model, request, response);
    }

}
