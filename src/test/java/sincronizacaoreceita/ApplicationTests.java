package sincronizacaoreceita;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sincronizacaoreceita.model.Conta;

@SpringBootTest
class ApplicationTests {


	@Test
	void testaAtualizacaoConta() throws URISyntaxException, IOException, InterruptedException {

		Conta c1 = new Conta();
		c1.setAgencia("1234");
		c1.setConta("123-4");
		c1.setSaldo("123,123");
		
		Conta c2 = new Conta();
		c2.setAgencia("1234");
		c2.setConta("123-4");
		c2.setSaldo("123,123");
		
		List l = new ArrayList();
		l.add(c1);
		l.add(c2);
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(l);
	
		HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8081/atualiza/"))
				.POST(BodyPublishers.ofString(json)).header("content-type", "application/json").build();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> resp = httpClient.send(httpRequest, BodyHandlers.ofString());
		
		assertEquals(200, resp.statusCode());
		assertTrue(resp.body().contains("resultado"));
		
		
	}
	
	


}
