package sincronizacaoreceita;

import java.util.concurrent.ThreadFactory;

//classe utilizada para poder dar uma manipulada
//nos objetos Threads antes de eles serem utilizados
public class FabricaDeThreads implements ThreadFactory {

	private static int numero;
	
	@Override
	public Thread newThread(Runnable runnable) {

		Thread thread = new Thread(runnable, "ThreadPool " + numero++);
		
		thread.setUncaughtExceptionHandler(new TratadorDeExceptions());
		
//		thread.setDaemon(true); //definindo uma thread de serviço que é finalizada automaticamente quando não houver
								//nenhuma outra thread rodando
		
		return thread;
	}

}
