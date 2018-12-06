package mul_web_server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpServer{

	public static final String WEB_ROOT = "/private/var/www/eclipse/webroot";
	private static final int PORT = 8080;
	private static ThreadPoolExecutor executor = null;
	public static void main(String args[]) {
		init();
		HttpServer server = new HttpServer();
		server.await();
	}
	
	public static void init() {
		// 创建线程池
		executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
		
	}
	
	// 监听端口
	@SuppressWarnings("resource")
	public void await() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName("127.0.0.1"));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		int i = 1;
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println(i++);
				Handler handler = new Handler(socket);
				executor.execute(handler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
