package infchem.realrobots.leonardo;

import infchem.realrobots.connector.ConnectorHID;

import java.io.IOException;
import java.util.Arrays;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;

public class ConnectorLeonardo extends ConnectorHID {
	

	static final int VENDOR_ID = 0x03EB;
	static final int PRODUCT_ID = 0x204F;
	private static final int BUFSIZE = 8;
	private static final String DESCR = "Arduino Leonardo";



	public static byte[] al = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	
	static void setPort() throws Exception {
		HIDDevice dev = null;
		try {
			dev = HIDManager.getInstance()
					.openById(VENDOR_ID, PRODUCT_ID, null);
		} catch (HIDDeviceNotFoundException e) {
			throw new Exception("Leonardo not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dev.write(al);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dev.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Object[] getInput(Double port) throws Exception {
		Object[] result = {""};
		byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESCR);
		String s = String.valueOf(buf[port.intValue()]);
		result[0] =  Integer.parseInt(s);
		return result;
	}


	public void setInputPort(int inputPort) throws Exception {
		al[inputPort+1] = 0x0E;	
		this.setPort();
	}
	
	public void setOutputHigh(int outputPort) throws Exception {
		al[outputPort+1] = 0x1A;	
		this.setPort();
	}
	
	public void setOutputLow(int outputPort) throws Exception {
		al[outputPort+1] = 0x0A;	
		this.setPort();
	}
	
	public  Object[] getRaw() throws Exception {

	    Object[] result = {""};
	    byte[] buf = readHID(VENDOR_ID, PRODUCT_ID, DESCR);
		result[0] = Arrays.toString(buf);
	    return result; 
}
}
