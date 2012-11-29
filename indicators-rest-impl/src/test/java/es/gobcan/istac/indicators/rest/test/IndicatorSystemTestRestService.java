package es.gobcan.istac.indicators.rest.test;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.springframework.test.web.context.TestGenericWebXmlContextLoader;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.clients.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.rest.test.mocks.IndicatorsSystemVersionMock;
import es.gobcan.istac.indicators.rest.test.mocks.OperationMock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = {"/spring/indicators-rest-applicationContext-test-web.xml", "/spring/indicators-rest-applicationContext-test.xml",
        "/spring/include/indicators-rest-applicationContext-beans.xml"})
public class IndicatorSystemTestRestService {

    @Autowired
    private WebApplicationContext wac     = null;

    private MockMvc               mockMvc = null;

    // TODO TESTEAR QUE AL FILTRAR SE OBTIENEN TANTAS OBSERVACIONES COMO SE ESPERAN

    // @SuppressWarnings("unchecked")
    @Before
    public void initMocks() throws Exception {
        mockMvc = webApplicationContextSetup(wac).build();

        IndicatorsSystemsService indicatorsSystemsService = wac.getBean(IndicatorsSystemsService.class);
        StatisticalOperationsRestInternalFacade statisticalOperations = wac.getBean(StatisticalOperationsRestInternalFacade.class);

        // MOCKS
        when(indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(any(ServiceContext.class), eq("CODIGO_0001"))).thenReturn(IndicatorsSystemVersionMock.mockIndicatorsSystemVersion1());
        when(statisticalOperations.retrieveOperationById(eq("CODIGO_0001"))).thenReturn(OperationMock.mockOperation1());

        // List<IndicatorsSystemVersion> indicatorsSystemVersions = new ArrayList<IndicatorsSystemVersion>();
        // indicatorsSystemVersions.add(IndicatorsSystemVersionMock.mockIndicatorsSystemVersion1());
        // indicatorsSystemVersions.add(IndicatorsSystemVersionMock.mockIndicatorsSystemVersion2());
        // PagedResult<IndicatorsSystemVersion> pagedResult = new PagedResult<IndicatorsSystemVersion>(indicatorsSystemVersions, 0, 2, 5);
        // when(indicatorsSystemsService.findIndicatorsSystemsPublished(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenReturn(pagedResult);
        //
        // when(metamacStatisticalOperationsExternalInterfaceV10.retrieveOperation(OperationBaseMock.mockOperationBase1().getCode())).thenReturn(OperationBaseMock.mockOperationBase1());
        // when(metamacStatisticalOperationsExternalInterfaceV10.retrieveOperation(OperationBaseMock.mockOperationBase2().getCode())).thenReturn(OperationBaseMock.mockOperationBase2());

        // when(indicatorsSystemsService.retrieveIndicatorInstance(any(ServiceContext.class), "UUID_INDICATOR_INSTANCE_1")).thenReturn(value)
    }

    @Ignore
    @Test
    public void findIndicatorsSystyems() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/indicators/v1.0/indicatorsSystems"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().encoding("UTF-8"));
        resultActions.andExpect(content().type(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        resultActions.andExpect(jsonPath("$[0]").exists());
        resultActions.andExpect(jsonPath("$[0].code", equalTo(OperationMock.mockOperation1().getId())));
        resultActions.andExpect(jsonPath("$[1]").exists());
        resultActions.andExpect(jsonPath("$[1].code", equalTo(OperationMock.mockOperation2().getId())));
        resultActions.andExpect(jsonPath("$[2]").doesNotExist());
        resultActions.andDo(print());
    }

    @Test
    public void retrieveRetrieveIndicatorsSystem() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/indicators/v1.0/indicatorsSystems/CODIGO_0001"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().encoding("UTF-8"));
        resultActions.andExpect(content().type(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.id", equalTo("CODIGO_0001")));
        resultActions.andExpect(jsonPath("$.selfLink", equalTo("http://localhost/api/indicators/v1.0/indicatorsSystems/CODIGO_0001")));

        resultActions.andDo(print());
    }

    @Ignore
    @Test
    public void retrieveIndicatorsInstance() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/indicators/v1.0/indicatorsSystems/CODE_INDICATOR_SYSTEM/indicatorsInstances/UUID_INDICATOR_INSTANCE_1"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().encoding("UTF-8"));
        resultActions.andExpect(content().type(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        resultActions.andExpect(jsonPath("$[0]").exists());
        resultActions.andExpect(jsonPath("$[0].code", equalTo(OperationMock.mockOperation1().getId())));
        resultActions.andExpect(jsonPath("$[1]").exists());
        resultActions.andExpect(jsonPath("$[1].code", equalTo(OperationMock.mockOperation2().getId())));
        resultActions.andExpect(jsonPath("$[2]").doesNotExist());
        resultActions.andDo(print());
    }
}
