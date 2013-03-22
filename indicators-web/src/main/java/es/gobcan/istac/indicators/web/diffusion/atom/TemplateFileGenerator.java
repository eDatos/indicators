package es.gobcan.istac.indicators.web.diffusion.atom;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class TemplateFileGenerator {

    @Autowired
    private ConfigurationService configurationService;
    
    public boolean fileNeedsToBeReloaded(String atomFilePath, int ttlMinutes) {
        File file = new File(atomFilePath);
        if (file.exists()) {
            DateTime lastModified = new DateTime(file.lastModified());
            DateTime lastModifiedLimit = new DateTime().minusMinutes(ttlMinutes);
            if (lastModified.isAfter(lastModifiedLimit)) {
                return false;
            }
        }
        return true;
    }

    public void generateFile(String atomFilePath, Map<String, Object> parameters, Locale locale) throws Exception {
        File file = new File(atomFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        Writer writer = new FileWriter(file); 
        Exception exception = null;
        try {
            Template template = getTemplateFreemarker(locale);
            template.process(parameters, writer);
        } catch (Exception e) {
            exception = e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        if (exception != null) {
            if (file.exists()) {
                file.delete();
            }
            throw exception;
        }
    }
    
    private Template getTemplateFreemarker(Locale locale) throws Exception {
        Configuration cfg = new Configuration();
        URL url = Thread.currentThread().getContextClassLoader().getResource("templates/atom.ftl");
        String path = URLDecoder.decode(url.getPath(),"UTF-8");
        return new Template("atom", new FileReader(path),cfg);
    }
}
