package mul_web_server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// 处理类
public class Handler implements Runnable{

	private Thread thread = null;
	private Socket socket = null;
	public Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread());
		try {
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			
			// Request 对象
			Request request = new Request(input);
			request.parse();
			
			// Response 对象
			Response response = new Response(output);
			response.setRequest(request);
			response.sendStaticResource();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
}
