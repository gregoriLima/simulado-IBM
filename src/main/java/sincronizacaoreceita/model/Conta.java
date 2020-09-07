package sincronizacaoreceita.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //annotation do lombok que indica que as propriedades serão privadas e irão receber automaticamente os getters e setters em tempo de compilação. 
	  //Gera também o equals, hashCode e o toString e constructor 
public class Conta {
	
		@CsvBindByName(column = "agencia")
		@PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "\\d+")//digitos com no mínimo uma ocorrência 
		@Pattern(regexp = "\\d+", message="o número da agência deve ter no mínimo um digito")
		@NotNull
		private String agencia;

	    @CsvBindByName(column = "conta")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "\\d+[-]{1}\\d{1}")//um digito ou mais, um '-', um digito 
	    @Pattern(regexp = "\\d+[-]{1}\\d{1}", message="O número da conta deve seguir o padrão nnn-n")
	    @NotNull
	    private String conta;

	    @CsvBindByName(column = "saldo")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[-]?\\d+[,]?\\d{0,2}$") //pode ter ou nao -, um ou mais digitos, ',', zero ou dois digitos
	    @Pattern(regexp = "^[-]?\\d+[,]?\\d{0,2}$", message="O valor do saldo está incorreto")
	    @NotNull
	    private String saldo; 
	    
	    @CsvBindByName(column = "status")
	    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "[A-Z]+")//um ou mais caracteres maiúsculos
	    @Pattern(regexp = "[A-Z]+", message="O status está incorreto")
	    @NotNull
	    private String status;
	    
	    @CsvBindByName(column = "resultado")
	    private String resultado;

}
