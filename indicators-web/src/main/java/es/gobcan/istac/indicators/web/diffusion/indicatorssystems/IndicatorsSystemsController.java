package es.gobcan.istac.indicators.web.diffusion.indicatorssystems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private ConfigurationService                          configurationService;

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicators-systems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems(UriComponentsBuilder uriComponentsBuilder) throws Exception {

        // Get json from API
        String urlPath = uriComponentsBuilder.path("/api/indicators/v1.0/indicatorsSystems?limit=1000").build().toUriString(); 
        String json = getJson(uriComponentsBuilder, urlPath);

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        modelAndView.addObject("indicatorsSystems", json);

        return modelAndView;
    }

    @RequestMapping(value = "/indicators-systems/{code}", method = RequestMethod.GET)
    public ModelAndView setupForm(UriComponentsBuilder uriComponentsBuilder, @PathVariable("code") String code, Model model) throws Exception {

        // Get json from API
        String urlPath = uriComponentsBuilder.path("/api/indicators/v1.0/indicatorsSystems/").path(code).build().toUriString(); 
        String json = getJson(uriComponentsBuilder, urlPath);
        
        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEM_VIEW);
        modelAndView.addObject("indicatorsSystem", json);
        // TODO propiedad de configuración en páginas ftl. Podría mejorarse accediendo al contexto de Spring desde la página, en lugar de añadir la propiedad en el modelo de vista
        String jaxiUrlBase = configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY);
        if (jaxiUrlBase.endsWith("/")) {
            jaxiUrlBase = StringUtils.removeEnd(jaxiUrlBase, "/");
        }
        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);

        return modelAndView;
    }
    
    
    // TODO Invocar REST desde página
    private String getJson(UriComponentsBuilder uriComponentsBuilder, String urlPath) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuffer json = new StringBuffer();
        String output = null;
        while ((output = br.readLine()) != null) {
            json.append(output);
        }
        conn.disconnect();
        return json.toString();
    }
}