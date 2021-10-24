package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;



/**
 * Class that receives data from a client 
 * 
 * @author Laure Roussel
 */


public class Server extends Thread {

	/**
	 * Method which overrides the run() method from Thread
	 */
	
	public void run() {

		try {
			// Sets up a datagram socket and binds it to the specified port on the local host machine (here, 51008)
			DatagramSocket ds = new DatagramSocket(51008);
			System.out.println("Server port: " + ds.getLocalPort());
			// Creation of 2 buffer bytes arrays
			byte[] buffer = new byte[16];
			byte[] buffer2 = new byte[16];

			// Constructs a datagram packet. These are used to implement a connectionless packet delivery service
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);



			while (true) {
				try {

					// Receive data from the client
					ds.receive(packet);
					// Convert the data into a String
					String stringPacket = new String(buffer, 0, packet.getLength());
					// IP address of the client
					InetAddress address = packet.getAddress();
					// Port used by the client to send data to the server
					int receivedPort = packet.getPort();

					// Write in the command prompt
					System.out.println("Client IP address: " + address);
					System.out.println("Client port: " + receivedPort);
					System.out.println("Received message: " + stringPacket);


					// Fill the list with the values received by the server from the client
					double receivedValueY = Double.valueOf(stringPacket);
					receivedValuesY.add(receivedValueY);



					// Message to send to the client + conversion into byte array
					String message = "Message received";
					buffer2 = message.getBytes();

					// Constructs a datagram packet
					DatagramPacket request = new DatagramPacket(buffer2, buffer2.length, address, receivedPort);
					// Send message to the client
					ds.send(request);



				} catch (IOException ex) {
					System.out.println("Server run error: " + ex.getMessage());
					ex.printStackTrace();
				}

			}

		} catch (IOException ex) {
			System.out.println("Server error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}


	/**
	 * List of the values received by the server from a client
	 */

	private List<Double> receivedValuesY = new ArrayList<>();

	/**
	 * Method which get the values send by the client
	 * @return receivedValuesY 	
	 * @see receivedValuesY 
	 */
	public List<Double> getValuesY() {
		return receivedValuesY;
	}

}
