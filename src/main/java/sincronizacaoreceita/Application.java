package sincronizacaoreceita;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.filter.Filters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;


import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import sincronizacaoreceita.model.Conta;

@Log4j2
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements CommandLineRunner  {

	@Value("${spring.profiles.active:Unknown}")
	private String activeProfile;
	
	public static void main(String[] args) throws IOException {
		    
		SpringApplication.run(Application.class, args);
		
	}

	@Override
	public void run(String... inputFilePath) throws Exception {
	
		//caso o profile seja de teste, busca o valor da propriedade de sistema 'path'
		if(activeProfile.equals("test"))
			inputFilePath = new String[]{System.getProperty("path")};
			
		if (inputFilePath.length == 0) {
			 log.error("Adicione ao menos um arquivo csv para atualização.");
			return;
		}
		
						//verifica se o arquivo passado é um .csv, caso não seja, tira da lista passada
		inputFilePath = Arrays.stream(inputFilePath).filter(s -> s.endsWith(".csv")).toArray(String[]::new);
	
		
		if (inputFilePath.length == 0) {
			 log.error("Adicione um arquivo csv válido");
			return;
		}

		log.info("Arquivos válidos para processamento" + Arrays.asList(inputFilePath));
		
		new ServidorDeAtualizacoes(inputFilePath);
		
	}

}
