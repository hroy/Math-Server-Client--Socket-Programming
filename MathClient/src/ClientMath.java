import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class ClientMath {

	private int LISTENER_PORT = 4444;
	private int totalRequest = 1000;
    
	private String numberOfOp;

	private Socket socket;
	private PrintWriter outStream;
	private BufferedReader inStream;
	
	public ClientMath() throws UnknownHostException, IOException
    {
    	socket = new Socket("localhost", LISTENER_PORT);
    	outStream = new PrintWriter(socket.getOutputStream(), true);
    	inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
    }
    
	public void sendRequest()
	{
		try
		{
			Random r = new Random();	    	
	    	String sendData = "";
	    	
			for(int i=0;i<totalRequest;i++)
			{
				int value = r.nextInt(4-0) + 0;
				
				if(value%4==0)
		    	{
					sendData = "Add," + r.nextDouble()+ "," + r.nextDouble();
		    	}
		    	else if(value%4==1)
		    	{
		    		sendData = "Sub," + r.nextDouble() + "," + r.nextDouble();
		    	}
		    	else if(value%4==2)
		    	{
		    		sendData = "Min," + r.nextInt() + "," + r.nextInt() + "," + r.nextInt();
		    	}
		    	else
		    	{
		    		sendData = "Max," + r.nextInt() + "," + r.nextInt() + "," + r.nextInt();
		    	}
				
				outStream.println(sendData);
			}
			
			numberOfOp = inStream.readLine().trim();
			
			while(numberOfOp==null)
			{}
			
//			outStream.println("getNumberofOp");
//			
//			numberOfOp = inStream.readLine().trim();
			
			System.out.println("Server Status up to now: "+ numberOfOp);
			
			outStream.println("stop");
		}
		catch(Exception ex)
		{
			System.out.println("Exception @ClientMath.sendRequest(): "+ex.getMessage());
    		System.exit(-1);
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
    	{
			String userInput = "";
			
			while(true)
			{
				new ClientMath().sendRequest();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Want to exit (y/n)?: ");
				userInput = br.readLine();
				
				if(userInput.equalsIgnoreCase("y"))
				{
					System.out.println("You have entered 'y'. So, exiting now..");
					break;
				}
				System.out.println("You have not entered 'y'. So, sending request again.");
			}
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Exception @ClientMath.Main()!!: "+ex.getMessage());
    	}
	}

}
