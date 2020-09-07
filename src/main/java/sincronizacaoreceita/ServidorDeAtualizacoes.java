package sincronizacaoreceita;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.assertj.core.api.filter.Filters;

import ch.qos.logback.core.filter.Filter;

public class ServidorDeAtualizacoes {


	private BlockingQueue<String> filaArquivosContas; //utilizado BlockingQueue pois possue métodos blocantes para serem consumidos por mais de uma thread
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando = new AtomicBoolean(false);
	
	
	
	public ServidorDeAtualizacoes(String... inputFilePath) throws IOException {

			//Utilizada a FabricaDeThreads para poder definir uma classe tratadora de erros na criação de cada thread pelo Executor
		this.threadPool = Executors.newCachedThreadPool(new FabricaDeThreads());
			//Utilizado CachedThreadPool para o número de thread crescer ou diminuir dinâmicamente conforme necessidade
	
		this.estaRodando.set(true);
		
		//Adiciona cada path recebido em uma BlockingQueue, preferido um Array do que um List por questão de performance
		//e definido que o FIFO necessita ser consumido na ordem
		this.filaArquivosContas = new ArrayBlockingQueue<>(inputFilePath.length, true, Arrays.asList(inputFilePath).stream()
																												  .filter(c -> c.contains(".csv"))
																												  .collect(Collectors.toList()));
					
		
		iniciarConsumidoresDeArquivos();
		
	}

	
	private void iniciarConsumidoresDeArquivos() {
		int qtdConsumidores = filaArquivosContas.size()>2?2:filaArquivosContas.size(); //definição de que haverá no máximo duas threads rodando, consumindo os arquivos passados
		for (int i = 0; i < qtdConsumidores; i++) {
			ConsumidorDeArquivos consumidor = new ConsumidorDeArquivos(this.threadPool, filaArquivosContas);
			this.threadPool.execute(consumidor);
		}		
		
	}
	
	

	

}
