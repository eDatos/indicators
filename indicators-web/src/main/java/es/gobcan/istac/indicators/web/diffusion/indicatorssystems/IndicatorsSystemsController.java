package es.gobcan.istac.indicators.web.diffusion.indicatorssystems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.atom.AtomGenerator;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private AtomGenerator        atomGenerator;

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicatorsSystems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems(UriComponentsBuilder uriComponentsBuilder) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        return modelAndView;
    }

    @RequestMapping(value = "/indicatorsSystems/{code}", method = RequestMethod.GET)
    public ModelAndView indicatorsSystem(UriComponentsBuilder uriComponentsBuilder, @PathVariable("code") String code, Model model) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEM_VIEW);
        modelAndView.addObject("indicatorsSystemCode", code);

        // Jaxi URL
        String jaxiUrlBase = configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY);
        if (jaxiUrlBase.endsWith("/")) {
            jaxiUrlBase = StringUtils.removeEnd(jaxiUrlBase, "/");
        }
        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);

        return modelAndView;
    }

    @RequestMapping(value = "/indicatorsSystems/{code}/atom.xml", method = RequestMethod.GET)
    public void indicatorsSystemAtom(UriComponentsBuilder uriComponentsBuilder, @PathVariable("code") String code, 
                                                                                @RequestParam(required = false, value = "language",defaultValue="es")String language, 
                                                                                HttpServletResponse response) throws Exception {
        String baseUrl = uriComponentsBuilder.build().toUriString();
        String feedUrl = uriComponentsBuilder.build().toUriString() + "/indicatorsSystems/"+code+"/atom.xml?language="+language;
        String systemUrl = uriComponentsBuilder.build().toUriString() + "/indicatorsSystems/"+code+"?language="+language;
        Locale locale = new Locale(language);
        String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContext(), locale, baseUrl, systemUrl, feedUrl, code);
        writeAtomFileInResponse(response, atomPath, code);
    }

    private void writeAtomFileInResponse(HttpServletResponse response, String filePath, String name) throws Exception {

        File file = new File(filePath);
        int length = 0;
        ServletOutputStream op = response.getOutputStream();
        DataInputStream in = null;
        String mimetype = "application/atom+xml";

        response.setContentType(mimetype);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + name+".xml" + "\"");
        try {
            byte[] bbuf = new byte[1000];
            in = new DataInputStream(new FileInputStream(file));
            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, length);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (op != null) {
                op.flush();
                op.close();
            }
        }
    }

}