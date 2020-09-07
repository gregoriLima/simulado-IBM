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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import lombok.extern.log4j.Log4j2;
import sincronizacaoreceita.model.Conta;
import sincronizacaoreceita.service.ReceitaService;

@Log4j2
public class ConsumidorDeContas implements Callable<List> { //Utilizado Callable para poder se ter um retorno

	private BlockingQueue<Conta> filaContas;

	public ConsumidorDeContas(BlockingQueue<Conta> filaContas) {
		this.filaContas = filaContas;	
	}


	@Override
	public List<Conta> call() throws Exception {

			Conta conta = null;
			List<Conta> resultados = new ArrayList<>();
			
			while( filaContas.size() != 0 && (conta = filaContas.take()) != null ) {
				try {
						log.info("Atualizando conta: " + conta.getConta());	
						
						//fazendo a atualização e já salvando o resultado no objeto
						conta.setResultado(
								new ReceitaService()
									.atualizarConta(
										StringUtils.leftPad(conta.getAgencia(), 4, '0'), //formatando agência com 4 digitos
										StringUtils.leftPad(new StringBuilder(conta.getConta()).deleteCharAt(conta.getConta().indexOf("-")).toString(), 6, '0'), //removendo o '-' de conta e deixando com 6 digitos
										Double.parseDouble(conta.getSaldo().replace(',', '.')),  //substituindo o ',' do arquivo original por '.'
										conta.getStatus()) == true ? "sucesso":"falhou");
						
					} catch (RuntimeException | InterruptedException e) {
						conta.setResultado("erro");
						log.error("Falha na transação da conta: " + conta.getConta() + " ag. " + conta.getAgencia());
						e.printStackTrace();
					}
				
				resultados.add(conta);
			
			}
			
			return resultados;

	}
}
