package es.gobcan.istac.indicators.web.diffusion.view;

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
        super.doRender(model, request, response);
    }

}
