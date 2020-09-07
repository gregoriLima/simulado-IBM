package sincronizacaoreceita.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.validation.Valid;

import org.assertj.core.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.log4j.Log4j2;
import sincronizacaoreceita.ConsumidorDeContas;
import sincronizacaoreceita.model.Conta;

@Controller
@Log4j2
public class MainController {

	//porta de escuta: 8081
	
	
	@RequestMapping(value="/atualiza", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
																			//erros customiza a mensagem de erro caso algum dado passado seja inválido
	public @ResponseBody ResponseEntity<String> configuracoes(@Valid @RequestBody Conta conta, Errors errors) {
			
		if (errors.hasErrors()) {
		      //caso haja erros de validação dos dados recebidos, retorna um 406
		       return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		   }
		
		log.info("Processando  " + conta);

		//adiciona a conta em uma fila
		BlockingQueue<Conta> filaContas = new ArrayBlockingQueue<>(1);//csvToBean.stream().collect(Collectors.toList()));
		filaContas.add(conta);
		System.out.println();
		//executando a atualização da conta passada
		var result = Executors.newCachedThreadPool().submit(new ConsumidorDeContas(filaContas));
		
		//retornando os resultados
		
			try { 
				String json = new GsonBuilder().create().toJson(result.get().get(0));
				return new ResponseEntity<>(json, HttpStatus.OK);
				
			} catch (InterruptedException | ExecutionException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		
	}
	
}
