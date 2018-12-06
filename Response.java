package mul_web_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;

// 响应
public class Response {

	private static final int BUFFER_SIZE = 200;
	Request request;
	OutputStream output;
	
	public Response(OutputStream output) {
		this.output = output;
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}
	
	// 发送静态资源
	public void sendStaticResource() throws Exception{
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			File file= new File(HttpServer.WEB_ROOT, request.getUri());
			if(file.exists()) {
				fis = new FileInputStream(file);
				int ch = fis.read(bytes, 0, BUFFER_SIZE);
				@SuppressWarnings("deprecation")
				String header = "HTTP/1.1 200 OK\r\n" + 
				"Server: " + System.getProperty("os.name").toUpperCase() + "\r\n" + 
				"Date: " + new Date().toGMTString() + "\r\n" + 
				"Last-Modified: " + new Date(file.lastModified()).toGMTString() + "\r\n" + 
				"Content-Length:" + file.length() + "\r\n" +
				"\r\n";
				output.write(header.getBytes("utf-8"));
				while(ch != -1) {
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);
				}
			} else {
				String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
				"Content-Type:text/html\r\n" +
				"Content-Length:23\r\n" +
				"\r\n" +
				"<h1>File Not Found</h1>";
				output.write(errorMessage.getBytes("utf-8"));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(fis != null){
				fis.close();
			}
		}
	}
}
