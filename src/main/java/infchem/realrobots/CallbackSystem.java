package infchem.realrobots;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CallbackSystem 
{
	public static HashMap<Integer, Callback> callbacks;
	
	public static Thread callbackThread;
	public static int lastCallback;
	
	public interface Callback
	{
		public long getExpiryTime();
		public void execute(Object[] returnObjects);
		public void abort(Object[] returnObjects);
	}
	
	public static void start()
	{
		callbackThread = new Thread( new Runnable() {
			public void run() 
			{
				while (true)
				{

					long time = System.currentTimeMillis();
					
					synchronized (callbacks)
					{
					
					    Iterator it = callbacks.entrySet().iterator();
					    while (it.hasNext())
					    {
					        Map.Entry<Integer, Callback> pairs = (Map.Entry)it.next();
					        Callback callback = pairs.getValue();
					        if (callback.getExpiryTime() < time)
					        {
					        	callback.abort(null);
					        	it.remove();
					        }
					    }
					}
				}
			}
		});
		
		callbacks = new HashMap();
		callbackThread.start();

	}
	
	public static void activateCallback(int callbackNum, Object[] returnObjects )
	{
		synchronized (callbacks)
		{
			Callback callback = callbacks.get(callbackNum);
			if (callback == null)
			{
				System.out.println("Callback has already been removed.");
				return;
			}
			
			callback.execute(returnObjects);
		}
	}
	
	public static void abortCallback(int callbackNum, Object[] returnObjects )
	{
		synchronized (callbacks)
		{
			Callback callback = callbacks.get(callbackNum);
			if (callback == null)
			{
				System.out.println("Callback has already been removed.");
				return;
			}
			
			callback.abort(returnObjects);
		}
	}
	
	public static int getCallbackID()
	{
		lastCallback++;
		return lastCallback;
	}
	
	public static int queueCallback(Callback callback)
	{
		int callbackID;
		synchronized (callbacks)
		{
			callbackID = getCallbackID();
			callbacks.put(callbackID, callback);
		}
		return callbackID;
	}
}