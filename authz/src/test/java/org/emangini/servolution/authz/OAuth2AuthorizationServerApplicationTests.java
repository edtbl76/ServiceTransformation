package org.emangini.servolution.authz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
@AutoConfigureMockMvc
class OAuth2AuthorizationServerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Disabled
    @Test
    void requestTokenUsingClientCredentialsGrantType() throws Exception {
        this.mockMvc.perform(post("/ouath2/token")
                .param("grant_type", "client_credentials")
                .header("Authorization", "Bearer cmVhZGVyOnNlY3JldA=="))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void requestOpenIdConfiguration() throws Exception {
        this.mockMvc.perform(post("/ouath2/.well-known/oauth-authorization-server"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void requestJwkSet() throws Exception {
        this.mockMvc.perform(post("/ouath2/jwks"))
                .andExpect(status().isOk());
    }
}