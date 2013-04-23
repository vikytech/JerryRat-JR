package com.jerryrat.server;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertThat;


public class ServerCofigTest {

    ServerCofig serverCofig = new ServerCofig();

    @Test
    public void shouldGetStaticUrl() throws IOException, SAXException, ParserConfigurationException {
        assertThat(serverCofig.getContent("root"), IsEqual.equalTo("/forum/static"));
    }

    @Test
    public void shouldGetStaticFilePattern() throws IOException, SAXException, ParserConfigurationException {
        assertThat(serverCofig.getContent("static-file-pattern"), IsEqual.equalTo("*.js, *.jpeg, *.gif, *.jpg, *.css"));
    }

    @Test
    public void shouldGetDynamicUrl() throws IOException, SAXException, ParserConfigurationException {
       assertThat(serverCofig.getContent("url-pattern"), IsEqual.equalTo("/forum"));
    }
}
