package infchem.realrobots.connector;

import java.io.IOException;
import java.util.Arrays;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;

public class ConnectorHID {

	private static final int BUFSIZE = 8;

	/**
	 * Read 8 bytes from the WeDo Hub
	 * @param VENDOR_ID 
	 * @param PRODUCT_ID 
	 * @return: A byteArray
	 */
	protected static byte[] readHID(int VENDOR_ID, int PRODUCT_ID, String desc) throws Exception {
		HIDDevice dev = null;
		try {
			dev = HIDManager.getInstance().openById(VENDOR_ID,
					PRODUCT_ID, null);
		} catch (HIDDeviceNotFoundException e) {
			throw new Exception(desc+" not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[BUFSIZE];
		try {
			int n = dev.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 try {
			dev.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 	   return buf;
		
	}

	public ConnectorHID() {
		super();
	}

	

}
