package sincronizacaoreceita;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sincronizacaoreceita.model.Conta;


@SpringBootTest
@AutoConfigureMockMvc
class APITestWithMock {

	 @Autowired
	  private MockMvc mockMvc;
	
	 
	@Test //Teste utilizando o mockMVC
	void testaUmaContaOK() throws Exception {
		
			Conta c1 = new Conta();
			c1.setAgencia("111");
			c1.setConta("123-4");
			c1.setSaldo("123,12");
			c1.setStatus("A");
			
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(c1);
		
			var s = mockMvc.perform(post("http://localhost:8081/atualiza")
					.contentType("application/json")
		            .content(json))
					.andExpect(status().isOk()) //verifica status 200 OK
					.andExpect(content().contentType(MediaType.APPLICATION_JSON)) //verifica retorno JSON
					.andExpect(jsonPath("$.resultado", is("sucesso")));


			
		//	System.out.println("\nResposta do servidor: " + s.andReturn().getResponse().getContentAsString());
		//	System.out.println();
		
		}
		
		
	@Test //Teste utilizando o mockMVC
	void testaUmaContaNOK() throws Exception {
	
		Conta c1 = new Conta();
		c1.setAgencia("111");
		c1.setConta("123-4");
		c1.setSaldo("123,124"); //colocado um digito a mais no saldo
		c1.setStatus("A");
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(c1);
	
		var s = mockMvc.perform(post("http://localhost:8081/atualiza")
				.contentType("application/json")
	            .content(json))
				.andExpect(status().isNotAcceptable()); //verifica status 406 NotAcceptable
				

		
		System.out.println("\nResposta do servidor: " + s.andReturn().getResponse().getContentAsString());
		System.out.println();
	
	}
	
//	
//	@Test 
//	void testaListaDeContas() throws Exception {
//	
//		Conta c1 = new Conta();
//		//c1.setAgencia("abc");
//		c1.setConta("123-4");
//		c1.setSaldo("123,123");
//		
//		Conta c2 = new Conta();
//		c2.setAgencia("1234");
//		c2.setConta("123-4");
//		c2.setSaldo("123,123");
//		
//		List l = new ArrayList();
//		l.add(c1);
//		l.add(c2);
//		
//		Gson gson = new GsonBuilder().create();
//		String json = gson.toJson(l);
//	
//		mockMvc.perform(post("http://localhost:8081/atualiza")
//				.contentType("application/json")
//	            .content(json))
//				.andExpect(status().isOk()) //verifica status 200 OK
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON)); //verifica retorno JSON
//
//	}
//	


}
