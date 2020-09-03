package sincronizacaoreceita.model;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NonNull;

@Data //annotation do lombok que indica que as propriedades serão privadas e irão receber automaticamente os getters e setters em tempo de compilação. 
		//Gera também o equals, hashCode e o toString e constructor 
public class Conta {
	
		@CsvBindByName(column = "agencia")
		private String agencia;

	    @CsvBindByName(column = "conta")
	    private String conta;

	    @CsvBindByName(column = "saldo")
	    private String saldo; 
	    
	    @CsvBindByName(column = "status")
	    private String status;
	    
	    @CsvBindByName(column = "resultado")
	    private String resultado;


}
