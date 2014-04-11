package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MicroWebServer is a tiny implementation that sets up
 * a webserver that returns a constant HTTP Response 
 * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes#2xx_Success">200 status code</a>.
 * It is a basic program that uses the ServerSocket class.
 * 
 * The aim of this class is to:
 * <ul>
 * <li>Illustrate a simple server implementation on Java
 * <li>How a server socket listens on the server side
 * <li>Process the client's request and return a simple HTML page
 * </ul>
 * <p>
 * @see			java.net.ServerSocket
 * 
 * @author tabcina
 */

public class MicroWebServer {

	/**
	 * Represents the server's listening port.
	 * Set to 80 as per convention
	 * 
	 */
	private static Integer SERVER_PORT_NUMBER;

	/**
	 * Starts the MicroWebServer
	 * @param			SERVER_PORT_NUMBER port on which the server listens to incoming requests (80 as per convention)
	 * @exception		IOException  if an input/output error occurs when opening the socket.
	 * @exception 		Exception thrown in case of any other abnormal behavior  
	 */
	public void initialize(Integer SERVER_PORT_NUMBER) throws IOException, Exception {
		ServerSocket serverSocket = null;

		System.out.println(String.format("Micro-webserver starting up on port [%d]", 
				SERVER_PORT_NUMBER));
		try {

			serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
			System.out.println("MicroWebServer..............waiting for connection");

			/***
			 * Infinitely listens to incoming connections
			 * **/
			while(true){
				processRequest(serverSocket);
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
			if (serverSocket != null) {
				serverSocket.close();
			}

		}

	}

	/**
	 * Processes the incoming client requests
	 * @param		serverSocket that accepts incoming client requests
	 * @exception	IOException  if an input/output error occurs when opening the socket.
	 * @exception	Exception thrown in case of any unexpected behavior
	 * 
	 * @see			java.net.ServerSocket
	 */
	public void processRequest(ServerSocket serverSocket){
		Socket connected = null;
		String request = null;        

		try {

			connected = serverSocket.accept();
			System.out.println("Connection established....");
			BufferedReader in = new BufferedReader(new InputStreamReader(connected.getInputStream()));

			/** For simplicity validates first header of the request **/
			request = in.readLine();
			readHeadersFromRequest(request);

			/** Sends back response to client**/
			PrintWriter out = new PrintWriter(connected.getOutputStream());
			returnOKResponse(out);

			/** Closes connected socket **/
			connected.close();
			System.out.println("===================================");
		} 
		catch (Exception e) {
			System.out.println("Could not accept remote request, due to  " + e.getStackTrace());
		}
	}

	/**
	 * Validates request with GET and HTTP/1.1 headers
	 * @param		request sent my client (socket)
	 *
	 **/
	public void readHeadersFromRequest(String request) {
		if (request!=null) {
			System.out.println(String.format("Request [%s]", request));
			if(request.startsWith("GET") && request.endsWith("HTTP/1.1")) {
				System.out.println(String.format("Well-formed request received, with [%s] and [%s]", 
						request.substring(0,3), 
						request.substring(request.length()-8, request.length())));
			}
			else{
				System.out.println(String.format("Request [%s] does not contain,"
						+ " at least \"GET\" and \"HTTP/.1.1\" ",request));
			}
		}
		else{
			System.out.println(String.format("Request is empty"));
		}
	}

	/**
	 * Sends back the response headers:
	 * <ul>
	 * <li>A hard-coded 200 status code, and 
	 * <li>The content-type of the resource (html)
	 * </ul>
	 * @param		out object to send the text-output 
	 * 				stream back to the client.
	 */

	public void returnOKResponse(PrintWriter out){
		System.out.println(String.format("Sending Response headers and resource"));

		out.println("HTTP/1.0 200");
		out.println("Content-Type: text/html");

		/** Blank line to end the headers and begin the body **/
		out.println("");

		/** HTML Resource page **/
		out.println("<HTML><BODY>");
		out.println("<H1>Micro-webserver</H1>");
		out.println("</br>");
		out.println("<H2>Oh ja!</H2>");
		out.println("</BODY></HTML>");
		out.flush();
	}

	/**
	 * Entry point of the MicroWebServer application
	 * @param args  Command line parameters are not used.
	 */
	public static void main(String args[]) {
		SERVER_PORT_NUMBER = 80;

		MicroWebServer ws = new MicroWebServer();

		try {
			ws.initialize(SERVER_PORT_NUMBER);
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

