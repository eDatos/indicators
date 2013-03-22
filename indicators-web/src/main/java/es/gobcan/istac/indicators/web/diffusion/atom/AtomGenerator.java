package es.gobcan.istac.indicators.web.diffusion.atom;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class AtomGenerator {
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade;
    
    @Autowired
    private HierarchicalMessageSource messageSource;
    
    @Autowired
    private TemplateFileGenerator atomFileManager;
    
    private static final String DEFAULT_LANGUAGE = "es";
    
    public String getIndicatorsSystemAtomFilePath(ServiceContext ctx, Locale locale, String baseUrl, String systemUrl, String feedUrl, String code) throws Exception {
        //Configurations
        String atomsDir = getAtomsDir();
        
        String atomFilePath = atomsDir + File.separator + code.toUpperCase() + "_" + locale.getLanguage().toUpperCase()+".xml"; 
        
        if (atomFileManager.fileNeedsToBeReloaded(atomFilePath, getAtomTimeToLiveMinutes())) {
            generateAtomFileIndicatorsSystem(ctx,locale,baseUrl,systemUrl,feedUrl,code,atomFilePath);
        }
        File file = new File(atomFilePath);
        return file.getAbsolutePath();
    }
    

    private void generateAtomFileIndicatorsSystem(ServiceContext ctx, Locale locale, String baseUrl, String systemUrl, String feedUrl, String code, String atomsFilePath) throws Exception {
        Integer numEntries = getAtomsNumEntries();
        
        IndicatorsSystemType indicatorsSystem = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseUrl, code);
        List<IndicatorsSystemHistoryType> indicatorsSystemHistory = indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(baseUrl,code, numEntries);
        
        exportIndicatorsSystemHistoryToAtom(atomsFilePath,locale,baseUrl,systemUrl,feedUrl,indicatorsSystem,indicatorsSystemHistory);
    }


    private void exportIndicatorsSystemHistoryToAtom(String atomFilePath, Locale locale, String baseUrl, String systemUrl, String feedUrl, IndicatorsSystemType indicatorsSystem, List<IndicatorsSystemHistoryType> indicatorsSystemHistory) throws Exception {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put("feedUrl", feedUrl);
        parameters.put("siteUrl", systemUrl);
        parameters.put("authorName", getMessage("atom.author.name", locale));
        parameters.put("indicatorsSystemTitle", getTitleForIndicatorsSystemBasedOnLocale(indicatorsSystem,locale));
        parameters.put("indicatorsSystem", indicatorsSystem);
        parameters.put("systemUpdateTitleText", getMessage("atom.entry.title", locale));
        parameters.put("systemUpdateContentText", getMessage("atom.entry.content", locale));
        parameters.put("indicatorsSystemHistories", indicatorsSystemHistory);
        parameters.put("lastUpdated",new Date());
        
        atomFileManager.generateFile(atomFilePath,parameters,locale);
    }
    
    private String getTitleForIndicatorsSystemBasedOnLocale(IndicatorsSystemType indicatorsSystem, Locale locale) {
        if (indicatorsSystem.getTitle().containsKey(locale.getLanguage())) {
            return indicatorsSystem.getTitle().get(locale.getLanguage());
        }
        if (indicatorsSystem.getTitle().containsKey(DEFAULT_LANGUAGE)) {
            return indicatorsSystem.getTitle().get(DEFAULT_LANGUAGE);
        } else {
            String lang = indicatorsSystem.getTitle().keySet().iterator().next();
            return indicatorsSystem.getTitle().get(lang);
        }
    }
    
    private String getMessage(String key, Locale locale) {
        try {
            return messageSource.getMessage(key, new Object[0], locale);
        } catch (NoSuchMessageException e) {
            Locale defaultLocale = new Locale(DEFAULT_LANGUAGE);
            return messageSource.getMessage(key, new Object[0], defaultLocale);
        }
    }
    
    private String getAtomsDir() {
        String dataDir = configurationService.getProperty(WebConstants.DATA_URL_PROPERTY);
        
        String path = null;
        if (dataDir.endsWith(File.separator)) {
            path = dataDir + WebConstants.ATOMS_DIR;
        } else {
            path = dataDir + File.separator + WebConstants.ATOMS_DIR;
        }
        
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return path;
        } else {
            throw new IllegalStateException("The ATOMS directory does not exist in data directory");
        }
    }        
    
    private int getAtomsNumEntries() {
        return Integer.parseInt(configurationService.getProperty(WebConstants.ATOMS_NUM_ENTRIES_PROPERTY));
    }
    
    private int getAtomTimeToLiveMinutes() {
        return Integer.parseInt(configurationService.getProperty(WebConstants.ATOMS_TIME_TO_LIVE_MINS_PROPERTY));
    }
    

    
}
