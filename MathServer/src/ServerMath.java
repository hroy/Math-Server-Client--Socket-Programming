import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerMath{

	private ServerSocket server;
    private Socket client;
    protected boolean isStopped = false;

    private BufferedReader inStream;
    private PrintWriter outStream;
    
    private int listenerPort = 4444; 
    
    protected ExecutorService clientControlThreadPool = Executors.newFixedThreadPool(10);
    
    
    /**
     * Waits for request from clients
     */
    private void clientHandler() {
    	
    	// open port
    	try {
			server = new ServerSocket(listenerPort);
		} catch (IOException e1) {
			System.out.println("Exception@ServerMath.clientHandler(): "+ e1.getMessage());
		}
    	
    	while (!isStopped) {
	    	try {
	    		
	            client = server.accept();
	            
	            inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
	            outStream = new PrintWriter(client.getOutputStream(), true);
	                       
	            clientControlThreadPool.execute(new Dispatcher(client, inStream, outStream));
	
	        } catch (Exception ex) {        	
	            Logger.getLogger(ServerMath.class.getName()).log(Level.SEVERE, null, ex);
	        }
    	}
    	
    	this.clientControlThreadPool.shutdown();
        System.out.println("Server is OFF.") ;
	}
    
    public synchronized void stop(){
    	System.out.println("Server is going to stop.") ;
        this.isStopped = true;
        try {
            this.server.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			System.out.println("Parent is running..............");
			new ServerMath().clientHandler();
		}
		catch(Exception ex)
		{
			System.out.println("Exception @ServerMath.Main()!!: "+ex.getMessage());
		}
	}

}
