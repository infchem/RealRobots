package infchem.realrobots.scratch;

//ScratchIO.java
//Andrew Davison, March 2009, ad@fivedots.coe.psu.ac.th

/* Uses remote sensor connections in Scratch v.1.3. They
allow Java to connect to it via a TCP socket at port 42001.
Details at:
  http://scratch.mit.edu/forums/viewtopic.php?id=9458
The two types of messages:
   broadcast "<name-string>"
   sensor-update <name-string> <value-string>
*/

import infchem.realrobots.RealRobots;

import java.io.*;
import java.net.*;

import cpw.mods.fml.common.FMLCommonHandler;


public class ScratchIO
{
private static final int SCRATCH_PORT = 42001;
private static final int NUM_BYTES_SIZE = 4;


private Socket scratchSocket;
private InputStream in = null;
private OutputStream out = null;


public ScratchIO()
{
 try {
   scratchSocket = new Socket(RealRobots.scratchIp, SCRATCH_PORT);
   in = scratchSocket.getInputStream();
   out = scratchSocket.getOutputStream();
 }
 catch (UnknownHostException e) {
   System.err.println("Scratch port (" + SCRATCH_PORT + ") not found");
  // FMLCommonHandler.instance().exitJava(1, false);
 }
 catch (IOException e) {
   System.err.println("Scratch IO link could not be created");
  // FMLCommonHandler.instance().exitJava(1, false);
 }
}  // end of ScratchIO()


public void closeDown()
{
 InputStream in = null;
 OutputStream out = null;
 try {
   scratchSocket.close();
 }
 catch (IOException e) {}
}  // end of closeDown()



// ------------- message sending methods --------------------


public boolean broadcastMsg(String msg)
// send a broadcast message
{
 String scratchMsg = "broadcast \"" + msg + "\"";
 return sendMsg(scratchMsg);
}  // end of broadcastMsg()


public boolean updateMsg(String name, String value)
// send a sensor-update message
{
 String scratchMsg = "sensor-update " + name + " " + value;
 return sendMsg(scratchMsg);
}  // end of updateMsg()



private boolean sendMsg(String msg)
/* Sends msg to Scratch using the format described in
     http://scratch.mit.edu/forums/viewtopic.php?id=9458  
*/
{
 if (out == null) {
   System.err.println("Output stream error");
   return false;
 }

 // System.out.println("Sending: " + msg);
 try {
   byte[] sizeBytes = intToByteArray( msg.length() );
   for (int i = 0; i < NUM_BYTES_SIZE; i++)                 
     out.write(sizeBytes[i]);
   out.write(msg.getBytes());
 }
 catch (IOException e) {
   System.err.println("Couldn't send: " + msg);
   return false;
 }
 return true;
}  // end of sendMsg()



private byte[] intToByteArray(int value)
// convert an integer into a 4-element byte array
{ return new byte[] { 
         (byte)(value >>> 24), (byte)(value >> 16 & 0xff), 
         (byte)(value >> 8 & 0xff), (byte)(value & 0xff) };
}


// ------------- message reading methods --------------------

public ScratchMessage readMsg()
/* receive a message from Scratch, returning it as a 
  ScratchMessage object, or null
*/
{
 if (in == null) {
   System.err.println("Input stream error");
   return null;
 }

 ScratchMessage scratchMsg = null;
 int msgSize = readMsgSize();
 if (msgSize > 0) {
	  try {
     byte[] buf = new byte[msgSize];
     in.read(buf, 0, msgSize);
     String msg = new String(buf); 
     // System.out.println("string: <" + msg + ">");
     scratchMsg = new ScratchMessage(msg);
   } 
   catch (IOException e) {
     System.err.println("Message read error: " + e);
    // FMLCommonHandler.instance().exitJava(0, false);
   }
 }
 return scratchMsg;
}  // end of readMsg()


private int readMsgSize()
// message size is encoded as NUM_BYTES_SIZE bytes at the start of the message
{
 int msgSize = -1;
	try {
   byte[] buf = new byte[NUM_BYTES_SIZE];
   in.read(buf, 0, NUM_BYTES_SIZE);
   msgSize = byteArrayToInt(buf);
   // System.out.println("message size: " + msgSize);
 } 
 catch (IOException e) {
   System.err.println("Header read error: " + e);
   //FMLCommonHandler.instance().exitJava(0, false);
 }
 return msgSize;
}  // end of readMsgSize()



private static int byteArrayToInt(byte [] b) 
// convert a byte array into an integer
{ 
 return (b[0] << 24) + ((b[1] & 0xFF) << 16) +
         ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
}

}  // end of ScratchIO class

