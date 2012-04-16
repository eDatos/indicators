package es.gobcan.istac.indicators.core.serviceapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsServiceFacade. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class InsertsToWebTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    private static String             QUANTITY_UNIT_1      = "1";
    private static String             GEOGRAPHICAL_VALUE_1 = "1";
    private static String             GEOGRAPHICAL_VALUE_2 = "2";

    @Test
    public void testMultiplePublicationsToWeb() throws Exception {

        List<String> uuidIndicators = new ArrayList<String>();
        for (int i = 0; i < 200; i++) {
            IndicatorDto indicatorDto = new IndicatorDto();
            indicatorDto.setCode("code" + i);
            indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
            indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
            int subject = RandomUtils.nextInt(3) + 1;
            indicatorDto.setSubjectCode(String.valueOf(subject));
            indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática " + subject));
            indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
            indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
            indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
            indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
            indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
            indicatorDto.setQuantity(new QuantityDto());
            indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
            indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
            indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

            // Create
            IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            String uuidIndicator = indicatorDtoCreated.getUuid();
            uuidIndicators.add(uuidIndicator);
            // Create datasource
            DataSourceDto dataSourceDto = new DataSourceDto();
            dataSourceDto.setDataGpeUuid("queryGpe1");
            dataSourceDto.setPxUri("px1");
            dataSourceDto.setTimeVariable("timeVariable1");
            dataSourceDto.setGeographicalVariable("geographicalVariable1");
            dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
            dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalString());
            dataSourceDto.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalString());
            dataSourceDto.setSourceSurveyUrl("sourceSurveyUrl");
            dataSourceDto.getPublishers().add("ISTAC");
            dataSourceDto.getPublishers().add("INE");
            dataSourceDto.getPublishers().add("IBESTAT");            
            DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
            dataSourceVariableDto1.setVariable("variable1");
            dataSourceVariableDto1.setCategory("category1");
            dataSourceDto.addOtherVariable(dataSourceVariableDto1);
            DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
            dataSourceVariableDto2.setVariable("variable2");
            dataSourceVariableDto2.setCategory("category2");
            dataSourceDto.addOtherVariable(dataSourceVariableDto2);
            dataSourceDto.setAbsoluteMethod("absoluteMethod1");
            indicatorsServiceFacade.createDataSource(getServiceContext(), uuidIndicator, dataSourceDto);
            
            // Send to production validation
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), uuidIndicator);
            // Send to diffusion validation
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuidIndicator);
            // Publish
            indicatorsServiceFacade.publishIndicator(getServiceContext(), uuidIndicator);
        }
        
        for (int i = 0; i < 14; i++) {
            IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
            indicatorsSystemDto.setCode(String.valueOf(i));
            // Create
            IndicatorsSystemDto indicatorsSystemDtoCreated = indicatorsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            String uuid = indicatorsSystemDtoCreated.getUuid();

            // Create indicator instance 
            {
                IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(0));
                indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 1"));
                indicatorInstanceDto.setParentUuid(null);
                indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                indicatorInstanceDto.setTimeValue("2012");
                indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
            }
            // Create dimension
            {
                // Create dimension 1
                DimensionDto dimensionDto = new DimensionDto();
                dimensionDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 2"));
                dimensionDto.setParentUuid(null);
                dimensionDto.setOrderInLevel(Long.valueOf(2));
                dimensionDto = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDto);
                // Create indicator instance 1
                {
                    IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                    indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(0));
                    indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 2.1"));
                    indicatorInstanceDto.setParentUuid(dimensionDto.getUuid());
                    indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                    indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                    indicatorInstanceDto.setTimeValue("2012");
                    indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                }
                // Create indicator instance 2
                {
                    IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                    indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(1));
                    indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 2.2"));
                    indicatorInstanceDto.setParentUuid(dimensionDto.getUuid());
                    indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
                    indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                    indicatorInstanceDto.setTimeValue("2012");
                    indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                }
                // Create dimension
                {
                    DimensionDto dimensionDtoA = new DimensionDto();
                    dimensionDtoA.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 2.3"));
                    dimensionDtoA.setParentUuid(dimensionDto.getUuid());
                    dimensionDtoA.setOrderInLevel(Long.valueOf(3));
                    dimensionDtoA = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDtoA);
                    // Create indicator instance 2
                    {
                        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                        indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(3));
                        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 2.3.1"));
                        indicatorInstanceDto.setParentUuid(dimensionDtoA.getUuid());
                        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                        indicatorInstanceDto.setTimeValue("2012");
                        indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                    }
                    {
                        // Dimesion
                        DimensionDto dimensionDtoB = new DimensionDto();
                        dimensionDtoB.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 2.3.2"));
                        dimensionDtoB.setParentUuid(dimensionDtoA.getUuid());
                        dimensionDtoB.setOrderInLevel(Long.valueOf(2));
                        dimensionDtoB = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDtoB);
                        // Create indicator instance 2
                        {
                            IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                            indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(3));
                            indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 2.3.2.1"));
                            indicatorInstanceDto.setParentUuid(dimensionDtoB.getUuid());
                            indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                            indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                            indicatorInstanceDto.setTimeValue("2012");
                            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                        }
                        {
                            // Dimesion
                            DimensionDto dimensionDtoC = new DimensionDto();
                            dimensionDtoC.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 2.3.2.2"));
                            dimensionDtoC.setParentUuid(dimensionDtoB.getUuid());
                            dimensionDtoC.setOrderInLevel(Long.valueOf(2));
                            dimensionDtoC = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDtoC);
                        }
                    }
                }
            }
            // Create indicator instance 2 
            {
                IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(4));
                indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 3"));
                indicatorInstanceDto.setParentUuid(null);
                indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
                indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_2);
                indicatorInstanceDto.setTimeValue("2011");
                indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
            }
            // Create dimension
            {
                // Create dimension 1
                DimensionDto dimensionDto = new DimensionDto();
                dimensionDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 4"));
                dimensionDto.setParentUuid(null);
                dimensionDto.setOrderInLevel(Long.valueOf(4));
                dimensionDto = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDto);
                // Create indicator instance 1
                {
                    IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                    indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(0));
                    indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 4.1"));
                    indicatorInstanceDto.setParentUuid(dimensionDto.getUuid());
                    indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                    indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                    indicatorInstanceDto.setTimeValue("2012");
                    indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                }
                // Create indicator instance 2
                {
                    IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                    indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(1));
                    indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 4.2"));
                    indicatorInstanceDto.setParentUuid(dimensionDto.getUuid());
                    indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
                    indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                    indicatorInstanceDto.setTimeValue("2012");
                    indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                }
                // Create dimension
                {
                    DimensionDto dimensionDtoA = new DimensionDto();
                    dimensionDtoA.setTitle(IndicatorsMocks.mockInternationalString("es", "Dimensión 4.3"));
                    dimensionDtoA.setParentUuid(dimensionDto.getUuid());
                    dimensionDtoA.setOrderInLevel(Long.valueOf(3));
                    dimensionDtoA = indicatorsServiceFacade.createDimension(getServiceContext(), uuid, dimensionDtoA);
                    // Create indicator instance 2
                    {
                        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                        indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(3));
                        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 4.3.1"));
                        indicatorInstanceDto.setParentUuid(dimensionDtoA.getUuid());
                        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
                        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
                        indicatorInstanceDto.setTimeValue("2012");
                        indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
                    }
                }
            }
            // Create indicator instance 3
            {
                IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
                indicatorInstanceDto.setIndicatorUuid(uuidIndicators.get(5));
                indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString("es", "Indicator instance 5"));
                indicatorInstanceDto.setParentUuid(null);
                indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
                indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_2);
                indicatorInstanceDto.setTimeValue("2011");
                indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
            }

            // Send to production validation
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), uuid);
            // Send to diffusion validation
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            // Publish
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
        }
        
        int stop = 43;
        stop++;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
