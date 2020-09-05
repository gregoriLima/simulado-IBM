package sincronizacaoreceita;

import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.exceptions.CsvException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionHandlerCSV implements CsvExceptionHandler {

	@Override
	public CsvException handleException(CsvException e) throws CsvException {
		
		log.error(e.getMessage() + " na linha " + e.getLineNumber() + " do arquivo .csv");

		return new CsvException("Erro ao processar linha " + e.getLineNumber() + " do arquivo .csv\n" + e.getMessage());
	}

}
