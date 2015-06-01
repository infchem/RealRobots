package infchem.realrobots.wedo;

import infchem.realrobots.connector.ConnectorHID;

import java.io.IOException;
import java.util.Arrays;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;

public class ConnectorWeDo extends ConnectorHID {
	

	static final int VENDOR_ID = 0x694;
	static final int PRODUCT_ID = 0x3;
	private static final int BUFSIZE = 8;
	private static final String DESC = "LEGO WeDo";
	private Double leftMotorPower = 0.0;
	private Double rightMotorPower = 0.0;


	public static byte[] al = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	
	/**
	 * Reads tilt sensor data from report.
	 * If no tilt sensor is connected, it returns 0
	 * @throws Exception
	 */
	private int getTiltByte() throws Exception {
		byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
		String[] tiltSensor = new String[]{"38","39","40"};
		if (Arrays.asList(tiltSensor).contains(String.valueOf(buf[3]))) {
			return 3;
		} else if (Arrays.asList(tiltSensor).contains(String.valueOf(buf[5]))) {
			return 5;
		} else {
			return 0;
		}
	}
	
	/**
	 * Reads distance sensor data from report.
	 * If no distance sensor is connected, it returns 0
	 * @throws Exception
	 */
	private int getDistanceByte() throws Exception {
		byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
		String[] distanceSensor = new String[]{"-76","-77"};
		if (Arrays.asList(distanceSensor).contains(String.valueOf(buf[3]))) {
			return 3;
		} else if (Arrays.asList(distanceSensor).contains(String.valueOf(buf[5]))) {
			return 5;
		} else {
			return 0;
		}
	}
	
	
	public Object[] getTilt() throws Exception {
		Object[] result = {""};
		if (getTiltByte()==0) {
			throw new Exception("No tilt sensor found!");

		}
    	String[] normal = new String[]{"-128","-127","-126","-125","124","125","126","127","128"};
    	String[] forward = new String[]{"-79","-78","-77","-76","-75","-74","-73","-72"};
    	String[] backward = new String[]{"24","25","26","27"};
    	String[] left = new String[]{"-27","-26","-25","-24"};
    	String[] right = new String[]{"72","73","74","75","76","77","78","79"};
    	
    	byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
    		 
    	String s = String.valueOf(buf[getTiltByte()-1]);
    	
    	if (Arrays.asList(normal).contains(s)) result[0] = "0";// "normal";
    	if (Arrays.asList(forward).contains(s)) result[0] = "1";// "forward";
    	if (Arrays.asList(backward).contains(s)) result[0] = "3";//"back";
    	if (Arrays.asList(left).contains(s)) result[0] = "4";//"right";
    	if (Arrays.asList(right).contains(s)) result[0] = "2";//"left";
    	
        return result;
	}
	
	
	
	public Object[] getDistance() throws Exception {
		Object[] result = {""};
		if (getDistanceByte()==0) {
			throw new Exception("No distance sensor found!");
		}
   	
    	byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
    	
    	 
    	result[0] = String.valueOf((buf[getDistanceByte()-1]&0xFF)-69);
    
    	   return result;
	}

	public Object[] getLeftRotation() throws Exception {
	    Object[] result = {""};
	    byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
	    if (buf[2] > 0xE9)  result[0] = String.valueOf(buf[3]);
	    else result[0] = String.valueOf(buf[2]);
    
	    return result; 
}
	
	
	public synchronized void motorControl() throws Exception {
		HIDDevice dev = null;
		try {
			dev = HIDManager.getInstance()
					.openById(VENDOR_ID, PRODUCT_ID, null);
		} catch (HIDDeviceNotFoundException e) {
			throw new Exception("WeDo not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buf = new byte[BUFSIZE];

		String lmp_str = String.valueOf(getLeftMotorPower().intValue());
		byte lmp_byte = new Byte(lmp_str);

		String rmp_str = String.valueOf(getRightMotorPower().intValue());
		byte rmp_byte = new Byte(rmp_str);

		byte[] weco = { 0x00, 64, lmp_byte , rmp_byte, 0x00, 0x00, 0x00, 0x00, 0x00 };
		// Erstes Byte fuer die ReportID !! 
		
		try {
			dev.write(weco);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dev.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int locateTiltSensor() {
		return 0;		
	}

	public Double getLeftMotorPower() {
		return leftMotorPower;
	}

	public void setLeftMotorPower(Double leftMotorPower) {
		this.leftMotorPower = leftMotorPower;
	}

	public Double getRightMotorPower() {
		return rightMotorPower;
	}

	public void setRightMotorPower(Double rightMotorPower) {
		this.rightMotorPower = rightMotorPower;
	}
	
	public Object[] getRaw() throws Exception {
		Object[] result = {""};
		byte[] buf = readHID(VENDOR_ID, PRODUCT_ID,DESC);
		result[0] = Arrays.toString(buf);
		return result; 
	}
}
