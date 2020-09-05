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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sincronizacaoreceita.ConsumidorDeContas;
import sincronizacaoreceita.model.Conta;

@Controller
public class MainController {

	//porta de escuta: 8081
	
	
	@RequestMapping(value="/atualiza", method = RequestMethod.POST, produces = "application/json")
	
	public @ResponseBody String configuracoes(@RequestBody String data) {

		//convertendo os dados recebidos:
		Gson gson = new GsonBuilder().create();
		Conta contas[] = gson.fromJson(data, Conta[].class);
		
		//adiciona cada conta em uma fila
		BlockingQueue filaContas = new ArrayBlockingQueue<>(contas.length, true, Arrays.asList(contas));//csvToBean.stream().collect(Collectors.toList()));
		
		//executando a atualização das contas passadas
		var result = Executors.newCachedThreadPool().submit(new ConsumidorDeContas(filaContas));
		
		//retornando os resultados
			try {
				String json = gson.toJson(result.get());
				return json;
			} catch (InterruptedException | ExecutionException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).toString();
			}
		
	}

	
	
	
	
	
	public static void main (String args[]) throws InterruptedException {
	
		Conta c = new Conta();
		c.setAgencia("1234");
		c.setConta("123-4");
		c.setSaldo("123,123");
		
		Conta c1 = new Conta();
		c1.setAgencia("1234");
		c1.setConta("123-4");
		c1.setSaldo("123,123");
		
		List l = new ArrayList();
		l.add(c);
		l.add(c1);
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(l);
		
		System.out.println(json);	
		
		Conta contas[] = gson.fromJson(json, Conta[].class);

		System.out.println(contas[0]);
		
		//adiciona cada conta em uma fila
		BlockingQueue filaContas = new ArrayBlockingQueue<>(contas.length, true, Arrays.asList(contas));//csvToBean.stream().collect(Collectors.toList()));
		
		var x = Executors.newCachedThreadPool().submit(new ConsumidorDeContas(filaContas));
		
		try {
			System.out.println(x.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

			
  
	
}
