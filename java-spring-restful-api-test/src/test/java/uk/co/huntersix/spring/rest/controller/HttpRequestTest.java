package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnError() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary1",
                        String.class
                )
        ).contains("No person not found with given firstname and lastname");
    }

    @Test
    public void shouldReturnPersonList() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/brown",
                        String.class
                )
        )
                .contains("Collin")
                .contains("Brown")
                .contains("Ian");
    }

    @Test
    public void shouldReturnErrorWhenLastnameDoesNotExists() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/mills",
                        String.class
                )
        )
                .contains("No person not found with given lastname");
    }

    @Test
    public void shouldReturnErrorWhenPersonAlreadyExists() throws Exception {
        assertThat(
                this.restTemplate.postForObject(
                        "http://localhost:" + port + "/person/smith/mary", null, String.class
                )
        )
                .contains("Person already exists with given firstname and lastname");
    }

    @Test
    public void shouldReturnSuccessWhenPersonAdded() throws Exception {
        assertThat(
                this.restTemplate.postForObject(
                        "http://localhost:" + port + "/person/smith/will", null, String.class
                )
        )
                .contains("Person Added");
    }
}