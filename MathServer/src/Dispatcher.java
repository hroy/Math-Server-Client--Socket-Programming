import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Dispatcher implements Runnable{

	private Socket client;

    private BufferedReader inStream;
    private PrintWriter outStream;
    public int reqCounter = 0;
    public int numberOfReqTotal = 1000;
    
    protected ExecutorService requestControlThreadPool = Executors.newFixedThreadPool(20);
    
    public Dispatcher(Socket paraClient, BufferedReader paraInStream, PrintWriter paraOutStream)
    {
    	this.client = paraClient;
    	this.inStream = paraInStream;
    	this.outStream = paraOutStream;
    	this.reqCounter = 0;
    }
    
    private String processRequest(String in)
    {
    	int replyI; 
    	double replyD;
    	String ret = "";
    	
    	try
    	{
	    	FunctionalUnit obFU = new FunctionalUnit();
	    	
	    	String []inputArray = in.split(",");
	    	
	    	if(inputArray[0].equalsIgnoreCase("add"))
	    	{
	    		replyD = obFU.magicAdd(Double.parseDouble(inputArray[1]), Double.parseDouble(inputArray[2]));
	    		ret = ret + replyD;
	    	}
	    	else if(inputArray[0].equalsIgnoreCase("sub"))
	    	{
	    		replyD = obFU.magicSubtract(Double.parseDouble(inputArray[1]), Double.parseDouble(inputArray[2]));
	    		ret = ret + replyD;
	    	}
	    	else if(inputArray[0].equalsIgnoreCase("min"))
	    	{
	    		replyI = obFU.magicFindMin(Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]));
	    		ret = ret + replyI;
	    	}
	    	else
	    	{
	    		replyI = obFU.magicFindMax(Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]));
	    		ret = ret + replyI;
	    	}
	    	
	    	System.out.println("[Result]: " + ret);
	    	return ret;
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Exception @Dispatcher.processRequest(): "+ex.getMessage());
    		return ret;
    	}
    	
    }
    
    private void dipatchAll(Socket paraClient, BufferedReader paraInStream, PrintWriter paraOutStream) throws InterruptedException
    {
    	while (true) {
    		final String inMsg;
    		
            try {
            	
//            	System.out.println("C: "+ this.reqCounter);
//            	if(this.reqCounter==this.numberOfReqTotal)
//        		{
//        			String outputToClient = "[Try 2] Add: "+ FunctionalUnit.getTotalAddOP() + ", Sub: "+ FunctionalUnit.getTotalSubOP() + ", Min: " + FunctionalUnit.getTotalMinOP()+", Max: "+ FunctionalUnit.getTotalMaxOP();
//            		
//            		paraOutStream.println(outputToClient);
//            		System.out.println(outputToClient);
//        		}
            	
            	inMsg = paraInStream.readLine();
            	//System.out.println("[Server<-]: " + inMsg);
            	
            	if(inMsg.equalsIgnoreCase("stop"))
            	{
            		System.out.println("Request processing done!");
            		return;
            	}
            	else if(inMsg.isEmpty() || inMsg == null || inMsg.equalsIgnoreCase("getNumberofOp"))
            	{
            		requestControlThreadPool.shutdown();
            		requestControlThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            		
            		String outputToClient = "Add: "+ FunctionalUnit.getTotalAddOP() + ", Sub: "+ FunctionalUnit.getTotalSubOP() + ", Min: " + FunctionalUnit.getTotalMinOP()+", Max: "+ FunctionalUnit.getTotalMaxOP();
            		
            		paraOutStream.println(outputToClient);
            		System.out.println(outputToClient);
            	}
            	else
            	{    
            		//processRequest(inMsg);
            		
            		requestControlThreadPool.execute(new ProcessingRequest(inMsg, this));
            	}
             
            } catch (IOException e) {
                System.out.println("Exception @Dispatcher.dispatchAll(): "+e.getMessage());
                System.exit(-1);
            }
        }    	
    }
    
    public void stopClient() {
		System.out.println("@stop: counter: "+ this.reqCounter);
		String outputToClient = "[Try 2] Add: "+ FunctionalUnit.getTotalAddOP() + ", Sub: "+ FunctionalUnit.getTotalSubOP() + ", Min: " + FunctionalUnit.getTotalMinOP()+", Max: "+ FunctionalUnit.getTotalMaxOP();
		
		outStream.println(outputToClient);
		System.out.println(outputToClient);
	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			dipatchAll(this.client, this.inStream, this.outStream);
		}
		catch(Exception ex)
		{
			System.out.println("Exception@Dispatcher.run(): "+ex.getMessage());
		}
	}

}
