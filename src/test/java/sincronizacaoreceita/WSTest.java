package sincronizacaoreceita;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.*;
import sincronizacaoreceita.model.Conta;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.lessThan;




class WSTest {

	private String jsonBodyReq;
	
	@BeforeEach
	void before(){
		
	 	RestAssured.port = 8081;
		
		Conta c1 = new Conta();
		c1.setAgencia("1234");
		c1.setConta("123-4");
		//c1.setSaldo("123,123");
		
		Conta c2 = new Conta();
		c2.setAgencia("1234");
		c2.setConta("123-4");
		c2.setSaldo("123,123");
		
		List l = new ArrayList();
		
		l.add(c1);
		l.add(c2);
		
		Gson gson = new GsonBuilder().create();
		jsonBodyReq = gson.toJson(l);
	}
	
	
	
	@Test
	void testeComRestAssured() {

				 given()
                .header("Accept", "application/json")
                .contentType("application/json")
                .body(jsonBodyReq)
                .expect()
                .statusCode(201)
                .then()
                .log();
             
	}
	
	
	
	@Test
	public void tempoDeResposta() { //tempo de resposta deve ser menor que 5s.
	  
		with()
		.body(jsonBodyReq)
		.post("/atualiza")
		.then()
		.time(lessThan(5L), TimeUnit.SECONDS);
		
	}
	
	
	
	@Test 
	void testeUsandoHttpAPI() throws IOException, InterruptedException, URISyntaxException{
	
		HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8081/atualiza"))
				.POST(BodyPublishers.ofString(jsonBodyReq)).header("content-type", "application/json").build();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> resp = httpClient.send(httpRequest, BodyHandlers.ofString()); 
				//poderia ter sido usado um sendAssync que retorna um CompletableFuture para não bloquear o código
		
		
		assertEquals(HttpStatus.OK.value(), resp.statusCode());
		
		assertTrue(resp.body().contains("resultado"));
	}


}
