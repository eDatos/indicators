package com.springframework.test.web.context;

public class TestGenericWebXmlContextLoader extends GenericWebXmlContextLoader {

    public TestGenericWebXmlContextLoader() {
        super("src/main/resources", false);
    }

}