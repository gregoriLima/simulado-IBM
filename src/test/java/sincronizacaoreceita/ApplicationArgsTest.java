package sincronizacaoreceita;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class ApplicationArgsTest {

	static {
			File resourcesDirectory = new File("src/main/resources/contas.csv");
			System.setProperty("path", resourcesDirectory.getAbsolutePath());
			System.setProperty("spring.profiles.active", "test");
	}
	
	@Test
	void test() throws InterruptedException {
		
		Thread.sleep(3000);
		
	}

}

