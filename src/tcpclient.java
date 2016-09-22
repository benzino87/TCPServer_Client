import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Jason Bensel
 * @version 457 Lab 2
 *
 **/

class tcpclient {

	/**
	 * Main method handles all operations with server.
	 * 
	 * Multi-threaded to handle both input and output streams from Server
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 */

	public static void main(String args[]) throws Exception {

		int portNumber = 9876;
		String IP = "127.0.0.1";
		// System.out.println("Port Number: " + portNumber);

		Socket clientSocket = new Socket(IP, portNumber);

		Thread sendToServer = new Thread(new Runnable() {
			public void run() {
				try {

					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

					while (true) {

						String messageToSend = inFromUser.readLine();

						outToServer.writeBytes(messageToSend + '\n');

						if (messageToSend.equals("Quit")) {
							clientSocket.close();
							break;
						}
					}

				} catch (Exception e) {
					System.out.println("Something went wrong sending message");
					e.printStackTrace();
				}

			}
		});

		sendToServer.start();

		Thread readFromServer = new Thread(new Runnable() {
			public void run() {
				try {

					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));

					while (true) {

						String message = inFromServer.readLine();

						System.out.println("Server: " + message);
					}
				} catch (Exception e) {
					System.out.println("Something went wrong recieving message");
					e.printStackTrace();
				}
			}
		});

		readFromServer.start();

		// clientSocket.close();
	}
}