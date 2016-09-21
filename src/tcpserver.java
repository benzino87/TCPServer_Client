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

class ClientHandler implements Runnable {
	Socket clientSocket;

	ClientHandler(Socket connection) {
		clientSocket = connection;
	}

	public void run() {
		try {
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while (true) {
				String message = inFromClient.readLine();

				System.out.println("The client said: " + message);

				outToClient.writeBytes("Message was received: " + message);

				if (message.equals("Quit")) {
					clientSocket.close();
				}
			}

		} catch (Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
	}
}
