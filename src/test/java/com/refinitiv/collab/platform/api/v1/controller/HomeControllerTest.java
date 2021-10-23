package com.refinitiv.collab.platform.api.v1.controller;

import com.refinitiv.collab.platform.WebapiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

    /**
     * @LocalServerPort same as @Value("${local.server.port}")
     */
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }

    @Test
    public void hello() throws Exception {
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/home/", String.class, "");
        System.out.println(String.format("output：%s", response.getBody()));
    }
}