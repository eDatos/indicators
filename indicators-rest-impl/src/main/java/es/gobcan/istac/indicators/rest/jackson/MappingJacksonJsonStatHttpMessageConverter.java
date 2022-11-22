package es.gobcan.istac.indicators.rest.jackson;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class MappingJacksonJsonStatHttpMessageConverter extends MappingJacksonHttpMessageConverter {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public MappingJacksonJsonStatHttpMessageConverter() {
        setSupportedMediaTypes(Collections.singletonList(new MediaType("application", "jsonstat+json", DEFAULT_CHARSET)));
    }

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.getMessageConverters().add(this);
    }
}
