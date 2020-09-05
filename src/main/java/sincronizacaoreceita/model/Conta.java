package sincronizacaoreceita.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import lombok.Data;

@Data //annotation do lombok que indica que as propriedades serão privadas e irão receber automaticamente os getters e setters em tempo de compilação. 
	  //Gera também o equals, hashCode e o toString e constructor 
public class Conta {
	
		@CsvBindByName(column = "agencia")
		@PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "\\d+")//digitos com no mínimo uma ocorrência 
		private String agencia;

	    @CsvBindByName(column = "conta")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "\\d+[-]{1}\\d{1}")//um digito ou mais, um '-', um digito 
	    private String conta;

	    @CsvBindByName(column = "saldo")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[-]?\\d+[,]?\\d{0,2}$") //pode ter ou nao -, um ou mais digitos, ',', zero ou dois digitos
	    private String saldo; 
	    
	    @CsvBindByName(column = "status")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "[A-Z]+")//um ou mais caracteres maiúsculos
	    private String status;
	    
	    @CsvBindByName(column = "resultado")
	    private String resultado;

}
