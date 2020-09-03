package sincronizacaoreceita;

import java.io.Reader;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

	private BlockingQueue<String> filaArquivosContas;
	
	public static void main(String[] args) {
		//args = new String[]{"C:\\ibm\\contas.csv"};
		SpringApplication.run(Application.class, args);
		
	}

	@Override
	public void run(String... inputFilePath) throws Exception {

		if (inputFilePath.length == 0) {
			 log.error("Adicione um caminho válido para o arquivo csv");
			return;
		}

		new ServidorDeAtualizacoes(inputFilePath);
		
		
		
	}

}
