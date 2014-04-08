package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

	/**
	 * WebServer constructor.
	 * @throws IOException 
	 */
	private static final Integer SERVER_PORT_NUMBER = 80;
	
	protected void start() throws IOException {
		ServerSocket s = null;

		System.out.println(String.format("Micro-webserver starting up on port %d", SERVER_PORT_NUMBER));
		System.out.println("Press <CTRL>-C to exit)");
		try {
			
			s = new ServerSocket(80);
			System.out.println("Waiting for connection");
			
			/***
			 * Infinitely listen to incoming connections
			 * **/
			for (;;) {
				try {
					// wait for a connection
					Socket remote = s.accept();
					// remote is now the connected socket
					System.out.println("Connection, sending data.");
					BufferedReader in = new BufferedReader(new InputStreamReader(
							remote.getInputStream()));
					PrintWriter out = new PrintWriter(remote.getOutputStream());

					// read the data sent. We basically ignore it,
					// stop reading once a blank line is hit. This
					// blank line signals the end of the client HTTP
					// headers.
					String str = ".";
					while (!str.equals(""))
						str = in.readLine();

					// Send the response
					// Send the headers
					out.println("HTTP/1.0 200 OK");
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
					remote.close();
				} 
				catch (Exception e) {
					System.out.println("Could not accept remote request, due to  " + e.getMessage());
				}
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
			s.close();
		}



	}

	/**
	 * Start the application.
	 * 
	 * @param args
	 *            Command line parameters are not used.
	 */
	public static void main(String args[]) {
		WebServer ws = new WebServer();
		try {
			ws.start();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error executing webserver due to: " + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception g) {
			System.out.println("Error executing webserver due to: " + g.getMessage());
		}
	}
}

