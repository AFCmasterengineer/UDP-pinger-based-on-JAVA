
package UDP_classes;

import java.io.IOException;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;

public class UDP_Client implements Runnable {

	DatagramSocket Client_socket;
	int Listen_port;
	int toSend_port;
	InetAddress IP_addr;
	InetAddress IP_addr_to_send_ping;
	InetAddress[] ping_addresses = null;
	InetSocketAddress Address;
	byte[] buf = new byte[64];
	DatagramPacket ping_packet;
	int Time_out = 5000; //Millisecond
    int loop_number = 0;
    int sent_pack = 0;
    int received_pack = 0;
    int lost;
    long minimum;
    long maximum;
    long avarage = 0;
    int temp=0;
    boolean flag;
	
	public UDP_Client(String Hostname, int Listen_port,int toSend_port) throws SocketException, UnknownHostException{
		System.out.println("Client is open at IP : "+InetAddress.getByName(Hostname).toString() + " ,Port : "+Listen_port);
		this.Listen_port = Listen_port;
		Client_socket = new DatagramSocket(Listen_port);
		this.toSend_port = toSend_port; 
		this.IP_addr = InetAddress.getLocalHost();
		this.IP_addr_to_send_ping = InetAddress.getByName(Hostname);
		ping_packet = new DatagramPacket(buf,buf.length,IP_addr_to_send_ping,this.toSend_port);
		flag = true;
	}
		
	public UDP_Client(String Hostname) throws IOException{
		this.IP_addr = InetAddress.getLocalHost();
		IP_addr_to_send_ping = InetAddress.getByName(Hostname);
		flag = false;
	}
	
	@Override
	public void run() {
		
		if(flag) {
			while(loop_number<4)
			{
				long milisec_Send_time = new GregorianCalendar().getTimeInMillis();
				String ping_message = "PING " + loop_number + " " + milisec_Send_time + " \n";
				buf = ping_message.getBytes();
				try {
					System.out.println("Sending Ping Request to " + IP_addr_to_send_ping.toString());
					Client_socket.send(ping_packet);
					sent_pack++;
					try {
						Client_socket.setSoTimeout(Time_out);
						DatagramPacket response = new DatagramPacket(new byte[64], 64);
						Client_socket.receive(response);
						if(response != null)
						{
							long milisec_Received_time = new GregorianCalendar().getTimeInMillis();
							long delay = milisec_Received_time - milisec_Send_time;
							System.out.println("Received from " + response.getAddress().getHostAddress() + ": " + "------" + " Delay: " + delay );
							received_pack++;
							if(delay<minimum && temp>0) 
							{
								minimum = delay;
							}
							else if(temp==0){
								minimum = delay;
								temp++;
							}
							
							if(delay>maximum) {
								maximum = delay;
							}
							avarage += delay;
						}
						
						
					} catch (IOException e) {
						System.out.println("Timeout for packet " + (loop_number+1));
					}
					loop_number ++;
				} catch (IOException e) {
					e.printStackTrace();
				}
								
			}
			lost = sent_pack- received_pack;
			if (received_pack != 0)
				avarage = avarage/received_pack;
			
			System.out.println("Ping statstics for "+ IP_addr_to_send_ping.toString()+":" );
			System.out.println("\t"+"Packet: Sent = "+sent_pack+", Received = "+received_pack+", Lost = "+lost);
			System.out.println("Approximate round trip times in milli-seconds:\n");
			System.out.println("\t"+"Minimum = "+minimum+"ms, Maximum = "+maximum+"ms, Average = "+avarage+"ms\n");
			Client_socket.close();
		}	
		else {
			while(loop_number<4) {
				try {			 
				      System.out.println("Sending Ping Request to " + IP_addr_to_send_ping.toString());
				      long finish = 0;
				      long start = new GregorianCalendar().getTimeInMillis();
				      loop_number++;
				      sent_pack++;
				      if (IP_addr_to_send_ping.isReachable(5000)){
				        finish = new GregorianCalendar().getTimeInMillis();
				        long delay = finish - start;
				        System.out.println("Received from " + IP_addr_to_send_ping.toString()  + ": " + "------" + " Delay: " + delay  + "ms");
				        received_pack++;
				        if(delay<minimum && temp>0) 
						{
							minimum = delay;
						}
						else if(temp==0){
							minimum = delay;
							temp++;
						}
						
						if(delay>maximum) {
							maximum = delay;
						}
						avarage += delay;
				      } else {
				    	  System.out.println("Timeout for packet " + (loop_number+1));
				    	  lost++;
				      }
				    } catch ( Exception e ) {
				      System.out.println("Exception:" + e.getMessage());
				    }
			}
		
			if (received_pack != 0)
				avarage = avarage/received_pack;
			System.out.println("Ping statstics for "+ IP_addr_to_send_ping.toString()+":" );
			System.out.println("\t"+"Packet: Sent = "+sent_pack+", Received = "+received_pack+", Lost = "+lost);
			System.out.println("Approximate round trip times in milli-seconds:\n");
			System.out.println("\t"+"Minimum = "+minimum+"ms, Maximum = "+maximum+"ms, Average = "+avarage+"ms\n");
		}
	}

	
}
