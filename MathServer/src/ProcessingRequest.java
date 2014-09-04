
public class ProcessingRequest implements Runnable{

	String inputMsg;
	Dispatcher obCounter;
	
	public ProcessingRequest(String paraMsg, Dispatcher paraCounter)
	{
		this.inputMsg = paraMsg;
		this.obCounter = paraCounter;
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
	    	
	    	//System.out.println("[Result]: " + ret + " of " + in);
	    	return ret;
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Exception @ProcessingRequest.processRequest(): "+ex.getMessage());
    		return ret;
    	}
    	
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		processRequest(this.inputMsg);
		
		synchronized (obCounter) {
			obCounter.reqCounter++;
			System.out.println("[Counter]: " + obCounter.reqCounter);
			
			if(obCounter.reqCounter==obCounter.numberOfReqTotal)
				obCounter.stopClient();			
		}
		
	}

}
