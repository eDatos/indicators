package es.gobcan.istac.indicators.web.diffusion.test;

import static es.gobcan.istac.indicators.web.diffusion.mocks.IndicatorsSystemHistoryTypeMock.mockIndicatorsSystemHistoryMonthsAgo;
import static es.gobcan.istac.indicators.web.diffusion.mocks.IndicatorsSystemTypeMock.mockPublishedIndicatorsSystemWithCode;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;

import org.apache.xerces.impl.xs.opti.DefaultNode;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.hibernate.type.DateType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.atom.AtomGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/indicators-web-applicationContext-test.xml"})
public class AtomGeneratorTest {

    @Autowired
    AtomGenerator atomGenerator;
    
    @Autowired
    ConfigurationService configurationService;
    
    @Autowired
    IndicatorSystemRestFacade indicatorSystemRestFacade;

    private static final String DATA_DIR;
    private static final String ATOMS_DIR;
    private static final String SYSTEM_1    = "OPER_0001";    
    private static final String BASE_URL_MOCK = "http://localhost:8090/indicators-web/";    
    private static final String FEED_URL_MOCK = "http://localhost:8090/indicators-web/";    
    private static final String SYSTEM_URL_MOCK = "http://localhost:8090/indicators-web/system/code";    
    
    private static final Locale LOCALE_ES = new Locale("es","ES");
    private static final Locale LOCALE_EN_UK = new Locale("en","UK");
    
    static {
        DATA_DIR = Thread.currentThread().getContextClassLoader().getResource("./data").getPath();
        ATOMS_DIR = DATA_DIR + File.separator + WebConstants.ATOMS_DIR;
    }
    
    @Before
    public void setUpGlobalMocks() throws Exception {
        when(configurationService.getProperty(WebConstants.DATA_URL_PROPERTY)).thenReturn(DATA_DIR);
        when(configurationService.getProperty(WebConstants.ATOMS_TIME_TO_LIVE_MINS_PROPERTY)).thenReturn("10");
        when(configurationService.getProperty(WebConstants.ATOMS_NUM_ENTRIES_PROPERTY)).thenReturn("10");
        
        when(indicatorSystemRestFacade.retrieveIndicatorsSystem(startsWith("http://"), eq(SYSTEM_1))).thenReturn(mockPublishedIndicatorsSystemWithCode(SYSTEM_1));
        createDirectoryIfNotExists(ATOMS_DIR);
        deleteDirContent(ATOMS_DIR);
    }
    
    @Test
    public void testAtomMetadata() throws Exception {
        IndicatorsSystemType systemMock = mockPublishedIndicatorsSystemWithCode(SYSTEM_1);
        
        IndicatorsSystemHistoryType histMock1 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.050", 2);
        IndicatorsSystemHistoryType histMock2 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.011", 3);
        IndicatorsSystemHistoryType histMock3 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.000", 5);
        
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList(histMock1,histMock2,histMock3);
        
        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), anyInt())).thenReturn(mocks);
        
        Date beforeAtomCreationDate = removeMilliSeconds(new Date());
        Thread.sleep(1000);
        String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK, SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        
        Date afterAtomCreationDate = removeMilliSeconds(new Date());
        
        Document xmlDoc = parseXml(atomPath);
        List feedNodes = xmlDoc.selectNodes("/feed"); 
        assertEquals(1,feedNodes.size());
        
        Element title = ((Element)feedNodes.get(0)).element("title");
        assertTrue(title.getText().contains(systemMock.getCode()));
        
        Element id = ((Element)feedNodes.get(0)).element("id");
        assertEquals(SYSTEM_URL_MOCK,id.getText());
        
        Element updated = ((Element)feedNodes.get(0)).element("updated");
        Date updatedDate = getDateFromXmlDate(updated.getText());
        assertTrue("Atom update Date should be after or equal method call ",!beforeAtomCreationDate.before(updatedDate));
        assertTrue("Atom update Date should be before method call finished",!afterAtomCreationDate.before(updatedDate));
    }
    
    @Test
    public void testAtomEntries() throws Exception {
        IndicatorsSystemType systemMock = mockPublishedIndicatorsSystemWithCode(SYSTEM_1);
        
        IndicatorsSystemHistoryType histMock1 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.020", 2);
        IndicatorsSystemHistoryType histMock2 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.001", 3);
        IndicatorsSystemHistoryType histMock3 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.000", 5);
        
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList(histMock1,histMock2,histMock3);
        
        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), anyInt())).thenReturn(mocks);
        
        String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        
        Document xmlDoc = parseXml(atomPath);
        assertEquals(1,xmlDoc.selectNodes("/feed").size());
        List entries = xmlDoc.getRootElement().elements("entry");
        assertEquals(3,entries.size());
        {
            DefaultElement entry = (DefaultElement)entries.get(0);
            Date entryUpdatedDate = getDateFromXmlDate(entry.element("updated").getText());
            assertEquals(removeMilliSeconds(histMock1.getPublicationDate()),entryUpdatedDate);
        }
        {
            DefaultElement entry = (DefaultElement)entries.get(1);
            Date entryUpdatedDate = getDateFromXmlDate(entry.element("updated").getText());
            assertEquals(removeMilliSeconds(histMock2.getPublicationDate()),entryUpdatedDate);
        }
        {
            DefaultElement entry = (DefaultElement)entries.get(2);
            Date entryUpdatedDate = getDateFromXmlDate(entry.element("updated").getText());
            assertEquals(removeMilliSeconds(histMock3.getPublicationDate()),entryUpdatedDate);
        }
    }
    @Test
    public void testAtomLimitEntries() throws Exception {
        when(configurationService.getProperty(WebConstants.ATOMS_NUM_ENTRIES_PROPERTY)).thenReturn("2");
        
        IndicatorsSystemType systemMock = mockPublishedIndicatorsSystemWithCode(SYSTEM_1);
        
        IndicatorsSystemHistoryType histMock1 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.030", 2);
        IndicatorsSystemHistoryType histMock2 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.011", 3);
        
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList(histMock1,histMock2);
        
        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), eq(2))).thenReturn(mocks);
        
        String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        
        Document xmlDoc = parseXml(atomPath);
        assertEquals(1,xmlDoc.selectNodes("/feed").size());
        List entries = xmlDoc.getRootElement().elements("entry");
        assertEquals(2,entries.size());
        {
            DefaultElement entry = (DefaultElement)entries.get(0);
            Date entryUpdatedDate = getDateFromXmlDate(entry.element("updated").getText());
            assertEquals(removeMilliSeconds(histMock1.getPublicationDate()),entryUpdatedDate);
        }
        {
            DefaultElement entry = (DefaultElement)entries.get(1);
            Date entryUpdatedDate = getDateFromXmlDate(entry.element("updated").getText());
            assertEquals(removeMilliSeconds(histMock2.getPublicationDate()),entryUpdatedDate);
        }
    }
    
    @Test
    public void testAtomNotCreateIfExists() throws Exception {
        //Cache time 10 mins
        when(configurationService.getProperty(WebConstants.ATOMS_TIME_TO_LIVE_MINS_PROPERTY)).thenReturn("10");
        IndicatorsSystemType systemMock = mockPublishedIndicatorsSystemWithCode(SYSTEM_1);
        
        IndicatorsSystemHistoryType histMock1 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.000", 2);
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList(histMock1);
        
        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), anyInt())).thenReturn(mocks);
        
        String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);

        File atomFile = new File(atomPath);
        assertTrue(atomFile.exists());
        long lastModified = atomFile.lastModified();
        
        String atomPathSecTime = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        
        File secondAtomFile = new File(atomPathSecTime);
        assertTrue(secondAtomFile.exists());
        assertEquals(lastModified,secondAtomFile.lastModified());
    }
    
    @Test
    public void testAtomDeleteFileIfError() throws Exception {
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList();
        
        when(indicatorSystemRestFacade.retrieveIndicatorsSystem(startsWith("http://"), eq(SYSTEM_1))).thenReturn(null);
        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), anyInt())).thenReturn(mocks);
        
        try {
            String atomPath = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
            fail("Should throws Exception");
        } catch (Exception e) {
            File file = new File(ATOMS_DIR);
            assertTrue(file.exists());
            assertTrue(file.isDirectory());
            assertEquals(0,file.list().length);
        }
    }
    
    @Test
    public void testAtomCacheDifferentLocales() throws Exception {
        when(configurationService.getProperty(WebConstants.ATOMS_TIME_TO_LIVE_MINS_PROPERTY)).thenReturn("10");
        
        IndicatorsSystemType systemMock = mockPublishedIndicatorsSystemWithCode(SYSTEM_1);
        
        IndicatorsSystemHistoryType histMock1 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.030", 2);
        IndicatorsSystemHistoryType histMock2 = mockIndicatorsSystemHistoryMonthsAgo(systemMock, "01.011", 3);
        List<IndicatorsSystemHistoryType> mocks = Arrays.asList(histMock1,histMock2);

        when(indicatorSystemRestFacade.findIndicatorsSystemHistoryByCode(startsWith("http://"), eq(SYSTEM_1), anyInt())).thenReturn(mocks);
        
        
        String atomPathEN = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_EN_UK, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        File fileEnglish = new File(atomPathEN);
        assertTrue(fileEnglish.exists());
        
        String atomPathES = atomGenerator.getIndicatorsSystemAtomFilePath(getServiceContextAdministrador(), LOCALE_ES, BASE_URL_MOCK,  SYSTEM_URL_MOCK, FEED_URL_MOCK, SYSTEM_1);
        File fileES= new File(atomPathES);
        assertTrue(fileES.exists());
        
        assertFalse(atomPathEN.equals(atomPathES));
    }
    
 
    private void createDirectoryIfNotExists(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }
    
    private void deleteDirContent(String dirPath) {
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()) {
            for (String filename : file.list()) {
                File fileChild = new File(dirPath + File.separator + filename);
                fileChild.setWritable(true);
                if (!fileChild.delete()) {
                    throw new RuntimeException("Not all content in directory "+dirPath+ " could be deleted "+filename);
                }
            }
        } else {
            throw new RuntimeException("Specified paths does not exist or it's not a directory path "+dirPath);
        }
    }
    
    private Date removeMilliSeconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    private Date getDateFromXmlDate(String xmlDate) {
        return DatatypeConverter.parseDateTime(xmlDate).getTime();
    }
    
    private Document parseXml(String path) throws Exception {
        File file = new File(path);
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }
    
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = new ServiceContext("junit", "junit", "app");
        return serviceContext;
    }

}
