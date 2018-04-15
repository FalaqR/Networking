import java.lang.*;
import java.io.*;
import java.net.*;

public class Server {
	public static void main(String args[])throws IOException  {
		String data = "Welcome";
		String message;
		String username, password;
		String ContinueOption="";
		String servermessage;
		try {
			//Detecting the localhost's ip address
			InetAddress localaddr = InetAddress.getLocalHost();
			System.out.println ("Local hostnameIP: " + localaddr );
			System.out.println ("Local IP Address : " + localaddr.getHostAddress());
			
			// Creating a server socket for connection
			ServerSocket srvr = new ServerSocket(1234);
			System.out.println("Waiting for connection on "+localaddr);
			// Accept incoming connection
			Socket skt = srvr.accept();
			System.out.print("Server has connected!\n");
			//IP address of client-Source:https://stackoverflow.com/questions/49494924/java-networking-how-to-permanently-ban-ip-address
			InetAddress clientInetAddress=skt.getInetAddress();
			String ipClient=clientInetAddress.getHostAddress();
			
			// get Input and Output streams
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
			out.flush();
			BufferedReader in = new BufferedReader(new
				InputStreamReader(skt.getInputStream()));
			
			boolean alreadyBanned=false;
			try{
				BufferedReader infile= new BufferedReader (new FileReader("IPaddresses.txt"));
				String line=infile.readLine(); 
				while ( line!=null){
					if (line.equals(ipClient)) {
						System.out.println(ipClient+ " has already been banned");
						alreadyBanned=true;
						out.println("You have already been banned");
					}
					line=infile.readLine();
				}
				infile.close();
			}
			catch (FileNotFoundException e){
				System.out.println("Incorrect filename or location. Please verify path and filename. ");
				System.out.println("In Eclipse, data files must be placed directly in the root of the project folder.");
			}
			
			if (alreadyBanned==false) {
				System.out.print("Sending string: '" + data + "'\n");
				out.println(data);
				message=in.readLine();
				System.out.println("client message>"+message);
				int tries=0;
				String correctUsername="username";
				String correctPassword="password";
				do {
					tries=tries+1;
					ContinueOption=in.readLine();
					System.out.println("client continueOption>"+ContinueOption);
					if (!ContinueOption.equals("bye")) {
						username=in.readLine();
						System.out.println("client username>"+username);
						password=in.readLine();
						System.out.println("client password>"+password);
						
						if (username.equals(correctUsername)&&password.equals(correctPassword)){
							out.println("Access Granted");
							System.out.println("Access Granted");
							System.out.println("Client IP address: "+ipClient);
							ContinueOption=in.readLine();
							System.out.println("client continueOption>"+ContinueOption);
						}
						else if (tries>2) {
							System.out.println("Client has ran out of tries");
							out.println("You have run out of tries, you have been banned");
							ContinueOption="bye";
							try{
								// reading from a file in order to count the number of lines so that an array can be made
								BufferedReader infileForLengthOfFile = new BufferedReader (new FileReader("IPaddresses.txt"));
								String line;
								int numberOfLines=0;
								line=infileForLengthOfFile.readLine();
								while ( line!=null){ 
									numberOfLines=numberOfLines+1;
									line=infileForLengthOfFile.readLine();
								}
								infileForLengthOfFile.close();
								
								String [] IPaddress=new String [numberOfLines];
								BufferedReader infileForTheActualIPaddress = new BufferedReader (new FileReader("IPaddresses.txt"));
								line=infileForTheActualIPaddress.readLine(); 
								int i=0;
								while ( line!=null){ //check that the line is not at the end before trying to read the next line.
									//Store contents of the file into memory as an ARRAY
									IPaddress[i]=line;
									i=i+1;
									line=infileForTheActualIPaddress.readLine();
								}
								infileForTheActualIPaddress.close();
								
								//Writing the old information back into the file
								BufferedWriter outFile = new BufferedWriter(new FileWriter("IPaddresses.txt"));
								for (i=0;i<IPaddress.length;i=i+1) {
									outFile.write(IPaddress[i]);
									outFile.newLine(); 
								}
								//Writing client IP address to the file so they can be banned permanently
								outFile.write(ipClient);
								outFile.close();
							}
							catch (FileNotFoundException e){
								System.out.println("Incorrect filename or location. Please verify path and filename. ");
								System.out.println("In Eclipse, data files must be placed directly in the root of the project folder.");
							}
						}
					}
					
					
				}while(!ContinueOption.equals("bye"));
			}
			System.out.println("server>Server closing");		
			out.close();
			skt.close();
			srvr.close();
		}
		catch(BindException e){
			//e.printStackTrace();
			System.out.print("A server is already running on the same port.");    	  
		}
		catch(SocketException e) {
			//e.printStackTrace();
			System.out.print("Client has disconnected rudely.");
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.print(":( Whoops! It didn't work!\n");
		}
	}
	
}