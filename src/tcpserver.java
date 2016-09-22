import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Jason Bensel
 * @version 457 Lab 2
 *
 **/

class tcpserver {

	/**
	 * 
	 * Opens a socket on a specific port to allow for multiple client
	 * connections
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		final int DEFAULT_PORT = 9876;

		System.out.println("Listening on port: " + DEFAULT_PORT);

		ServerSocket listenerSocket = new ServerSocket(DEFAULT_PORT);

		try {

			while (true) {
				Socket clientSocket = listenerSocket.accept();

				try {

					Runnable r = new ClientHandler(clientSocket);
					Thread t = new Thread(r);
					t.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * 
 * 
 * Handles all communication with each individual client. Each Socket thread has
 * individual Write and Read streams to handle communication in any order
 * 
 * 
 * @author Jason Bensel
 *
 *
 */
class ClientHandler implements Runnable {

	Socket clientSocket;

	DataOutputStream outToClient;

	BufferedReader inFromUser;

	BufferedReader inFromClient;

	Thread readInData;

	Thread sendOutData;

	/**
	 * 
	 * Constructor which sets up all input and output streams based on in coming
	 * connection
	 * 
	 * @param connection
	 *            - Socket to connect to
	 * 
	 */
	ClientHandler(Socket connection) {
		clientSocket = connection;

		try {

			outToClient = new DataOutputStream(clientSocket.getOutputStream());
			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Send conformation of connection to client.
			outToClient.writeBytes("Connected\n");
			outToClient.flush();

		} catch (Exception e) {
			System.out.println("Something went wrong connecting");
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Handles both input and output threads from client.
	 * 
	 */
	public void run() {
		try {

			readInData = new Thread(new Runnable() {
				public void run() {
					try {
						while (true) {
							String clientMessage = inFromClient.readLine();

							if (clientMessage.equals("Quit")) {
								clientSocket.close();
								System.out.println("Client has left");

							} else {

								System.out.println("Client: " + clientMessage);
							}

						}
					} catch (Exception e) {
						System.out.println("Something went wrong while recieving");
						e.printStackTrace();
					}
				}
			});
			readInData.start();

			sendOutData = new Thread(new Runnable() {
				public void run() {
					try {
						while (true) {

							String userMessage = inFromUser.readLine();
							outToClient.writeBytes(userMessage + '\n');

						}
					} catch (Exception e) {
						System.out.println("Something went wrong while sending");
						e.printStackTrace();
					}
				}
			});
			sendOutData.start();

		} catch (Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}

	}
}
