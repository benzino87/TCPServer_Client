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

	public static void main(String args[]) throws Exception {

		// Scanner s = new Scanner(System.in);
		//
		// System.out.println("Enter an IP address (ex: 127.0.0.1)");
		// String IPAddress = s.nextLine();
		//
		// System.out.println("IP Address: " + IPAddress);
		//
		// System.out.println("Enter a port number");
		// String incomingPortNumber = s.nextLine();

		try {
			int portNumber = 9876;
			String IP = "127.0.0.1";
			System.out.println("Port Number: " + portNumber);

			Socket clientSocket = new Socket(IP, portNumber);

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Enter a message: ");

			String message = inFromUser.readLine();

			outToServer.writeBytes(message + '\n');

			while (true) {
				String returnMessage = inFromServer.readLine();

				System.out.println(returnMessage);
			}

			// clientSocket.close();

		} catch (Exception e) {
			System.out.println("Not a valid port number");
		}
	}
}