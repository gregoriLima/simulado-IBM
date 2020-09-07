package sincronizacaoreceita;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.bean.util.OpencsvUtils;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.log4j.Log4j2;
import sincronizacaoreceita.model.Conta;

@Log4j2
public class ConsumidorDeArquivos implements Runnable {

	private ExecutorService threadPool;
	private BlockingQueue<String> filaArquivosContas;
	private BlockingQueue<Conta> filaContas;
	private List<Conta> listaResultados = new ArrayList<>();
	
	
	public ConsumidorDeArquivos(ExecutorService threadPool, BlockingQueue<String> filaArquivosContas) {
		this.threadPool = threadPool;
		this.filaArquivosContas = filaArquivosContas;
	}

	private void iniciarConsumidoresDeContas() {
		
		int qtdConsumidores = filaContas.size()>2?2:filaContas.size(); //definição de que haverá no máximo duas threads rodando, consumindo as contas do arquivo
		
		Future<List> futureWS[] = new Future[qtdConsumidores];
		
		
		for (int i = 0; i < qtdConsumidores; i++) {
			ConsumidorDeContas consumidorDeContas = new ConsumidorDeContas(filaContas);
			futureWS[i] = this.threadPool.submit(consumidorDeContas);
		}	
		
		
			Arrays.asList(futureWS).forEach(l -> 
				{
					try {
						listaResultados.addAll(l.get(qtdConsumidores * 15, TimeUnit.SECONDS));
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						log.error("Erro ao atualizar contas");
						e.printStackTrace();
					} 
				});
			
	
		
	}
	
	@Override
	public void run() {
		
		try {
			
			String arquivoCSVPath = null;
			
			while( filaArquivosContas.size() != 0 && (arquivoCSVPath = filaArquivosContas.take()) != null) {
			
				log.info("Consumindo o arquivo " + arquivoCSVPath + " ," + Thread.currentThread().getName());
				
				//Lendo o arquivo
				Reader reader = Files.newBufferedReader(Paths.get(arquivoCSVPath));
	
					//Configuração para converter cada linha da tabela em um objeto 'Conta'
					List<Conta> csvToBeanList = new CsvToBeanBuilder(reader).withSeparator(';').withType(Conta.class)
							.withIgnoreLeadingWhiteSpace(true).withExceptionHandler(new ExceptionHandlerCSV()).build().parse();
					
					//adiciona cada conta em uma fila
					filaContas = new ArrayBlockingQueue<>((int)csvToBeanList.stream().count(), true, csvToBeanList);//csvToBean.stream().collect(Collectors.toList()));
	
					iniciarConsumidoresDeContas(); //inicia os consumidores de contas
			
				//definindo a ordem das colunas
				//poderiam ter sido pegos os nomes dos cabeçalhos via Reflection...
				String[] memberFieldsToBindTo = { "agencia", "conta", "saldo", "status", "resultado" };
				
				//caso não utilize-se esta estratégia de mapeamento para o arquivo, os cabeçalhos ficam fora de ordem
				ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
				mappingStrategy.setType(Conta.class);
				mappingStrategy.setColumnMapping(memberFieldsToBindTo);

				//definindo o caminho de salvamento como o caminha passado + 'Resultados'
				Path myPath = Paths.get(arquivoCSVPath.split("\\.")[0] + "Resultado.csv");

				try (var writer = Files.newBufferedWriter(myPath, StandardCharsets.UTF_8)) { //utiliado o try-with-resources para não precisar fechar o writer (writer.close())
					
					//definindo o cabeçalho. Pois como em ColumnPositionMappingStrategy utilizado para garantir a ordem das colunas, tem-se a suposição que não se utiliza cabeça-los, é necessário defini-los aqui
					//aqui também poderia ter sido utilizado Reflection, mas achei que o código ia ficar um pouco poluido...
					writer.append("agencia;conta;saldo;status;resultado" + System.getProperty("line.separator"));

					StatefulBeanToCsv<Conta> beanToCsv = new StatefulBeanToCsvBuilder<Conta>(writer)
							.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(';')
							.withLineEnd(System.getProperty("line.separator")).withMappingStrategy(mappingStrategy).build();

					log.info("Escrevendo arquivo de saída em: " + myPath);

				//	beanToCsv.setOrderedResults(true);
					beanToCsv.write(listaResultados.stream().sorted((c1, c2)-> c1.getAgencia().compareTo(c2.getAgencia())));
					
					
				} catch (Exception e) {
						log.error("Não foi possivel salvar o arquivo csv");
						e.printStackTrace();
				}
			
			}
			
		} catch (InterruptedException | IOException e) {
			System.out.println("sçalfkjaslfjaslçdfjkçsldfkjasçlfkjsadçlfkajsdçflkj");
			throw new RuntimeException(e);
		} 
		
	}

}
