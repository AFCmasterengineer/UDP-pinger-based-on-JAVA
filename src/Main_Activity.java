
import java.io.IOException;
import UDP_classes.*;


public class Main_Activity {

	public static void main(String[] args) throws IOException {
		/*
		
		if(args.length > 1) {
			
			UDP_Server server = new UDP_Server(Integer.parseInt(args[1]));
			UDP_Client client = new UDP_Client("localhost",Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			Thread thread_1 = new Thread(server);
			Thread thread_2 = new Thread(client);
			thread_1.start();
			thread_2.start();
			
		}
		else {
			
			UDP_Client client = new UDP_Client(args[0]);
			Thread thread_2 = new Thread(client);
			thread_2.start();
		}
		*/
		if(args[0].toLowerCase().equals("client"))
		{
			if(args.length<3) {
				UDP_Client client = new UDP_Client("localhost",Integer.parseInt(args[1]),Integer.parseInt(args[2]));
				Thread client_thread = new Thread(client);
				client_thread.start();
			}
			else {
				UDP_Client client = new UDP_Client(args[1].toString(),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
				Thread client_thread = new Thread(client);
				client_thread.start();
			}
		}
		else if(args[0].toLowerCase().equals("server")) {
			UDP_Server server = new UDP_Server(Integer.parseInt(args[1]));
			Thread server_thread = new Thread(server);
			server_thread.start();
		}

	}

}
