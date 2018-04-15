import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
	public static void main(String args[]) {
		try {
			Socket skt = new Socket("localhost", 1234); // change localhost to server ip address if not loopback
			BufferedReader in = new BufferedReader(new
					InputStreamReader(skt.getInputStream()));
			Scanner kbReader = new Scanner(System.in);
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
			out.flush();
			String message="";
			String servermessage="";
			String username,password;
			String ContinueOption="no";
			
			String serverconnection=in.readLine();
			System.out.println("server>: "+serverconnection);
			if (!serverconnection.equals("You have already been banned")) {
				System.out.println("Preparing to chat...");
				out.println("I am now connected to you.");
				do{
					if (in.ready()){
						servermessage=in.readLine();
						System.out.println("server>: "+servermessage);
					}
					if (servermessage.equals("You have run out of tries, you have been banned")) {
						ContinueOption="bye";
					}
					else if (!servermessage.equals("Access Granted")) {
						System.out.println("Would you like to continue, enter 'bye' to exit or anything else to continue");
						ContinueOption=kbReader.nextLine();
						out.println(ContinueOption);
						if (!ContinueOption.equals("bye")) {
							System.out.println("Enter username:");
							username=kbReader.nextLine();
							out.println(username);
							System.out.println("Enter password:");
							password=kbReader.nextLine();
							out.println(password);
						}
					}
					else if (servermessage.equals("Access Granted")) {
						System.out.println("Would you like to continue, enter 'bye' to exit or anything else to continue");
						ContinueOption=kbReader.nextLine();
						out.println(ContinueOption);
					}
		
					//out.println("ok");
					//message="bye";
					//out.println("bye");
					Thread.currentThread().sleep(300); //give time for server to respond
				}while (!ContinueOption.equals("bye"));
			}
			System.out.println("client> Client closing");
			out.close();
			in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.print("Whoops! It didn't work!\n");
		}
	}
	
}