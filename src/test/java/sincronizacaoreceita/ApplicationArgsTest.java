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


//teste passando um argumento para Application.java

@SpringBootTest
class ApplicationArgsTest {

	static {
			File resourcesDirectory = new File("src/main/resources/contas.csv"); //definindo o caminho do arquivo contas.csv
			System.setProperty("path", resourcesDirectory.getAbsolutePath()); //definindo a propriedade 'path' com o valor do caminho
			System.setProperty("spring.profiles.active", "test"); //definindo o profile de teste para o spring
	}
	
	@Test
	void test() throws InterruptedException {
		
		Thread.sleep(5000);
		
	}

}

