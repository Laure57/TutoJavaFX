package application;

import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;



/**
 * Class which sends data (10 random integers) to a server that must be specified in the code 
 * 
 * @author Laure Roussel
 */

public class Client extends Thread {


	/**
	 * Method which override the run() method from Thread
	 */
	
	public void run() {

		// hostname of the server 
		String hostname = "127.0.0.1" ;
		// port on which the server listen
		int port = 51008;

		try {
			// IP address of the server
			InetAddress address = InetAddress.getByName(hostname);
			// Construct a datagram socket for sending and receiving datagram packets
			DatagramSocket socket = new DatagramSocket();

			
			for (int i=0;i<10;i++) {

				// Get a random integer between 0-10, which is then converted into bytes
				Integer random = ThreadLocalRandom.current().nextInt(10);
				String r = random.toString();
				byte[] buf = r.getBytes();

				// Construct a datagram packet with the data to send
				DatagramPacket request = new DatagramPacket(buf, buf.length, address, port);
				// Send data to the server
				socket.send(request);

				// Construct a datagram packet which will contain the response of the server
				byte[] buffer = new byte[16];
				DatagramPacket response = new DatagramPacket(buffer, buffer.length);
				// Receive data from the server
				socket.receive(response);

				// Convert the response of the server into a string 
				String quote = new String(buffer, 0, response.getLength());

				// Write in the command prompt
				System.out.println(quote);
				System.out.println();
				Thread.sleep(1000);
			}

			//Close the datagram socket
			socket.close();



		} catch (SocketTimeoutException ex) {
			System.out.println("Timeout error: " + ex.getMessage());
			ex.printStackTrace();

		} catch (IOException ex) {
			System.out.println("Client error: " + ex.getMessage());
			ex.printStackTrace();

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}