package UDP_classes;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
public class UDP_Server implements Runnable {

	DatagramSocket Server_Socket;
	int server_port ;
    InetAddress Server_IP;
    InetAddress Client_IP;
    int Client_port;
    byte[] buf = new byte[32];
    DatagramPacket Cacth_UDP_pack_buff;
    DatagramPacket Ping_reply;
    int x=0;

	public UDP_Server(int server_port) throws SocketException{
		this.server_port = server_port;
		Server_Socket = new DatagramSocket(this.server_port);
		Cacth_UDP_pack_buff = new DatagramPacket(buf, buf.length);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Server is Open IP : "+Inet4Address.getByName("localhost").toString()+ " ,Port : "+ server_port);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

				while(x<4) {
					
					try {
						Server_Socket.receive(Cacth_UDP_pack_buff);
						Client_IP = Cacth_UDP_pack_buff.getAddress();
						Client_port = Cacth_UDP_pack_buff.getPort();
					    byte[] buf = Cacth_UDP_pack_buff.getData();
					    Ping_reply = new DatagramPacket(buf, buf.length, Client_IP, Client_port);
					    Server_Socket.send(Ping_reply);
						x++;
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				Server_Socket.close();
				System.out.println("Server is closed!");
	}

}
