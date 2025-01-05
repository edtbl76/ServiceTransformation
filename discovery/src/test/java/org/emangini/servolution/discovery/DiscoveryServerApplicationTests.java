package org.emangini.servolution.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class DiscoveryServerApplicationTests {

    @Value("${app.eureka-username}")
    private String username;
    @Value("${app.eureka-password}")
    private String password;

    TestRestTemplate testRestTemplate;

    @Autowired
    void setTestRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate.withBasicAuth(username, password);
    }

    @Test
    void validateLoadCatalog() {

        String expectedResponse =
                "{\"applications\":{\"versions__delta\":\"1\",\"apps__hashcode\":\"\",\"application\":[]}}";

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/eureka/apps", String.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

    }

    @Test
    void validateHealthCheck() {

        String expectedResponse = "{\"status\":\"UP\"}";

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/actuator/health", String.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
