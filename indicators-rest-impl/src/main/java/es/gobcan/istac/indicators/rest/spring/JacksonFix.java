package es.gobcan.istac.indicators.rest.spring;

import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import es.gobcan.istac.indicators.rest.jackson.DateTimeSerializer;

public class JacksonFix {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter = null;

    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof MappingJacksonHttpMessageConverter) {
                MappingJacksonHttpMessageConverter m = (MappingJacksonHttpMessageConverter) messageConverter;
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleModule stat4youModule = new SimpleModule("indicators", new Version(1, 0, 0, null));
                stat4youModule.addSerializer(DateTime.class, new DateTimeSerializer());
                objectMapper.registerModule(stat4youModule);
                m.setObjectMapper(objectMapper);
            }
        }
    }

}
