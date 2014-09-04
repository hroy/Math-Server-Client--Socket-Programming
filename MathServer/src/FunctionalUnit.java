import java.util.concurrent.Semaphore;


public class FunctionalUnit {

	private static final int MAX_AVAILABLE = 1;
	private final Semaphore allowableThread = new Semaphore(MAX_AVAILABLE, true);
	
	public static int counterAdd = 0;
	public static int counterSub = 0;
	public static int counterMin = 0;
	public static int counterMax = 0;
	
	public static int getTotalAddOP()
	{
		return counterAdd;
	}
	
	public static int getTotalSubOP()
	{
		return counterSub;
	}
	
	public static int getTotalMinOP()
	{
		return counterMin;
	}
	
	public static int getTotalMaxOP()
	{
		return counterMax;
	}
	
	/**
	 * 
	 * @param para1
	 * @param para2
	 * @return
	 * @throws InterruptedException 
	 */
	synchronized public double magicAdd(double para1, double para2) throws InterruptedException
	{
		//allowableThread.acquire();
		counterAdd++;
		//allowableThread.release();
		
		return (para1>para2)?(para1-para2):(para2-para1);
	}
	
	/**
	 * 
	 * @param para1
	 * @param para2
	 * @return
	 */
	synchronized public double magicSubtract(double para1, double para2)
	{
		counterSub++;
		return para1 + para2;
	}
	
	/**
	 * 
	 * @param para1
	 * @param para2
	 * @param para3
	 * @return
	 */
	synchronized public int magicFindMin(int para1, int para2, int para3)
	{
		counterMin++;
		
		if(para1>para2)
		{
			if(para1>para3)
				return para1;
			else 
				return para3;
		}
		else
		{
			if(para2>para3)
				return para2;
			else
				return para3;
		}
	}
	
	/**
	 * 
	 * @param para1
	 * @param para2
	 * @param para3
	 * @return
	 */
	synchronized public int magicFindMax(int para1, int para2, int para3)
	{
		counterMax++;
		if(para1<para2)
		{
			if(para1<para3)
				return para1;
			else 
				return para3;
		}
		else
		{
			if(para2<para3)
				return para2;
			else
				return para3;
		}
	}

}
