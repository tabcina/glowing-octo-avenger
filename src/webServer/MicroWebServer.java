package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MicroWebServer is a tiny implementation that sets up
 * a webserver that returns a constant HTTP Response of 200
 * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes#2xx_Success">SuccessCodes</a>
 * It is a basic implementation that uses the ServerSocket class
 * @see			java.net.ServerSocket
 * various devices or onto off-screen images.
 * The aim of this class is to:
 * <ul>
 * <li>Illustrate a simple server implementation on Java
 * <li>How a server sockets listens on the server sides
 * <li>Process the client's request and returns a simple HTML page
 * </ul>
 * <p>
 * 
 * 
 * @author tabci
 */

public class MicroWebServer {

    /**
     * Default port number for the MicroWebServer to listen
     * to a client request 
     */
	private static final Integer SERVER_PORT_NUMBER = 80;
	
    /**
     * Starts the MicroWebServer
     * @exception		IOException  if an input/output error occurs when opening the socket.
     * @exception 		Exception thrown in case of any exceptional behavior  
     */
	protected void start() throws IOException, Exception {
		ServerSocket s = null;

		System.out.println(String.format("Micro-webserver starting up on port %d", SERVER_PORT_NUMBER));
		System.out.println("Press <CTRL>-C to exit)");
		try {
			
			s = new ServerSocket(SERVER_PORT_NUMBER);
			System.out.println("Waiting for connection");
			
			/***
			 * Infinitely listens to incoming connections
			 * **/
			while(true){
				serverListens(s);
			}
		} 
		catch (IOException io) {
			System.out.println("I/O Exception due to " + io.getMessage());
		}
		catch (Exception e) {
			System.out.println("Could not create main server socket, due to: " + e.getMessage());
			return;
		}
		finally {
			/***
			 * Closes the socket connection
			 * **/
			s.close();
		}

}
	
    /**
     * Listens to the incoming client requests
     * @param		serverSocket that accepts incoming client requests
     * @exception	IOException  if an input/output error occurs when opening the socket.
     * @exception	Exception thrown in case of any exceptional behavior
     * 
     * @see			java.net.ServerSocket
     */
	public void serverListens(ServerSocket serverSocket){
		try {
			
			Socket remote = serverSocket.accept();
			
			System.out.println("Connection established, sending data.");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					remote.getInputStream()));
			PrintWriter out = new PrintWriter(remote.getOutputStream());

			String str = ".";
			while (!str.equals(""))
				str = in.readLine();
			returnOKResponse(out);

			remote.close();
		} 
		catch (Exception e) {
			System.out.println("Could not accept remote request, due to  " + e.getMessage());
		}
	}
	
    /**
     * Prints the 200 status code and the HTML simple content to be sent back to the client
     */
	
	public void returnOKResponse(PrintWriter out){
		out.println("HTTP/1.0 200");
		out.println("Content-Type: text/html");
		out.println("Server: Bot");
		// this blank line signals the end of the headers
		out.println("");
		// Send the HTML page
		out.println("<H1>Micro-webserver</H1>");
		out.println("<br></br>");
		out.println("<br></br>");
		out.println("<H2>Oh ja!</H2>");
		out.flush();
	}

	/**
	 * Entry point of the MicroWebServer application
	 * @param args  Command line parameters are not used.
	 */
	public static void main(String args[]) {
		MicroWebServer ws = new MicroWebServer();
		
		try {
			ws.start();
		} 
		catch (IOException e) {
			System.out.println(String.format("Error executing webserver due to: [%s]",e.getMessage()));
			e.printStackTrace();
		}
		catch (Exception g) {
			System.out.println(String.format("Error executing webserver due to: [%s]",g.getMessage()));
			g.printStackTrace();
		}
	}
}

