package com.eatsleeppong.ubipong;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UbipongApplicationTests {
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
