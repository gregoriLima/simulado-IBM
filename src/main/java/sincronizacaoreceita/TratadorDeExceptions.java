package sincronizacaoreceita;

import java.lang.Thread.UncaughtExceptionHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TratadorDeExceptions implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		log.error("Exception na Thread: " + t.getName() + " " + e.getMessage());
		
	}

}
