package es.gobcan.istac.indicators.core.serviceimpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.dspl.DsplDataset;
import es.gobcan.istac.indicators.core.dspl.DsplTable;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DsplTransformer;
import es.gobcan.istac.indicators.core.serviceimpl.util.DsplTransformerTimeTranslator;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Implementation of DsplExporterService.
 */
@Service("dsplExporterService")
public class DsplExporterServiceImpl extends DsplExporterServiceImplBase {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Override
    public List<String> exportIndicatorsSystemPublishedToDsplFiles(ServiceContext ctx, String indicatorsSystemUuid, InternationalString title, InternationalString description,
            boolean mergeTimeGranularities) throws MetamacException {

        // Validator
        InvocationValidator.checkExportIndicatorsSystemPublishedToDsplFiles(indicatorsSystemUuid, title, description, null);

        DsplTransformer transformer = null;
        if (mergeTimeGranularities) {
            transformer = new DsplTransformerTimeTranslator(getIndicatorsSystemsService(), getIndicatorsDataService(), getIndicatorsCoverageService(), getIndicatorsService(), configurationService);
        } else {
            transformer = new DsplTransformer(getIndicatorsSystemsService(), getIndicatorsDataService(), getIndicatorsCoverageService(), getIndicatorsService(), configurationService);
        }

        List<DsplDataset> datasets = transformer.transformIndicatorsSystem(ctx, indicatorsSystemUuid, title, description);

        List<String> datasetArchives = new ArrayList<String>();
        try {
            for (DsplDataset dataset : datasets) {
                Set<String> datasetFiles = new HashSet<String>();

                File datasetDirectory = createTempDirectory();

                datasetFiles.add(generateDescriptorFile(datasetDirectory, dataset));

                for (DsplTable table : dataset.getTables()) {
                    datasetFiles.add(generateDataFile(datasetDirectory, table));
                }

                String zipFilename = zipFileNameZipDirectoryNonRecursively(dataset.getId(), datasetDirectory);
                datasetArchives.add(zipFilename);
            }
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DSPL_FILES_CREATE_ERROR, indicatorsSystemUuid);
        }
        return datasetArchives;
    }

    private String zipFileNameZipDirectoryNonRecursively(String datasetId, File dirToZip) throws IOException {

        byte[] buffer = new byte[1024];
        ZipOutputStream zos = null;
        FileInputStream in = null;

        File zipFile = File.createTempFile(datasetId + "_", ".zip");

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            if (dirToZip != null && dirToZip.list() != null) {
                for (String file : dirToZip.list()) {
                    in = null;
                    ZipEntry ze = new ZipEntry(file);
                    zos.putNextEntry(ze);

                    in = new FileInputStream(dirToZip.getAbsolutePath() + File.separatorChar + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (zos != null) {
                zos.closeEntry();
                zos.close();
            }
        }
        return zipFile.getName();
    }

    private File createTempDirectory() throws IOException {
        File temp = File.createTempFile("dataset", StringUtils.EMPTY);

        if (!temp.delete() || !temp.mkdir()) {
            throw new IOException("Could not create temp directory file: " + temp.getAbsolutePath());
        }

        return temp;
    }

    private String generateDescriptorFile(File directory, DsplDataset dataset) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dataset", dataset);

        String filename = "dataset-" + dataset.getId() + ".xml";

        String absFileName = directory.getAbsolutePath() + File.separatorChar + filename;
        File file = new File(absFileName);
        file.createNewFile();

        return generateFile(file, "dataset", parameters);
    }

    private String generateDataFile(File directory, DsplTable table) throws TemplateException, IOException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("table", table);

        String absFileName = directory.getAbsolutePath() + File.separatorChar + table.getData().getDataFileName();
        File file = new File(absFileName);
        file.createNewFile();

        return generateFile(file, "data", parameters);
    }

    private String generateFile(File file, String templateName, Map<String, Object> parameters) throws TemplateException, IOException {
        String filename = file.getAbsolutePath();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
        try {
            Template template = getTemplateFreemarker(templateName);
            template.process(parameters, writer);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return filename;
    }

    private Template getTemplateFreemarker(String templateName) throws FileNotFoundException, IOException {
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        URL url = Thread.currentThread().getContextClassLoader().getResource("templates/" + templateName + ".ftl");
        String path = URLDecoder.decode(url.getPath(), "UTF-8");
        return new Template(templateName, new FileReader(path), cfg);
    }

}
