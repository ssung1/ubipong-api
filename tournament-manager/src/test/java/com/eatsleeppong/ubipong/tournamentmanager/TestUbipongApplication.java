package com.eatsleeppong.ubipong.tournamentmanager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
// only need to specify classes if enabling Swagger
// @SpringBootTest(classes = UbipongApplication.class)
@ActiveProfiles("test")
public class TestUbipongApplication {
	@Value("${challonge.api-key}")
	private String apiKey;

	@Test
	public void contextLoads() {
	}

	@Test
	public void canReadPropertiesFile() {
		assertThat(apiKey, is("this-is-a-test-api-key"));
	}
}
