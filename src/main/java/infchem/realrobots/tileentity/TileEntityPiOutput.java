package infchem.realrobots.tileentity;

import infchem.realrobots.connector.ConnectorHTTP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public  class TileEntityPiOutput extends TileEntity {

	public  int outputPort;
	public  boolean outputStatus;
	public  ConnectorHTTP httpCon;
	public String playerName;

	public TileEntityPiOutput() {
		this.outputPort=0;
		this.outputStatus=false;
		this.httpCon = new ConnectorHTTP();
		this.playerName ="";
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setBoolean("outputStatus", getOutputStatus());
		par1.setInteger("outputPort", getOutputPort());
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		this.setOutputStatus(par1.getBoolean("outputStatus"));
		this.setOutputPort(par1.getInteger("outputPort"));
	}

	/**
	 * Returns the output status
	 */
	public  boolean getOutputStatus() {
		return this.outputStatus;
	}

	/**
	 * Sets the output status
	 */
	public  void setOutputStatus(boolean newOutputStatus) {
		this.outputStatus = newOutputStatus;
	}
	
	/**
	 * Returns the active output port
	 */
	public  int getOutputPort() {
		return this.outputPort;
	}
	
	/**
	 * returns the player name
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Sets the active output port
	 */
	public  void setOutputPort(int newOutput) {
		this.outputPort=newOutput; 
	}

	/**
	 * Sets the active output port automagically.
	 * The output port cycles from 0-7 incrementally.
	 */
	public  void setOutputPort() {
		if (this.outputPort==7) this.outputPort=0; else ++this.outputPort; 
	}

	@Override
	   public Packet getDescriptionPacket()
	   {
	       NBTTagCompound syncData = new NBTTagCompound();
	       this.writeToNBT(syncData);
	       return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
	   }
	   @Override
	   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	   {
	       this.readFromNBT(pkt.func_148857_g());
	   }

	/**
	 * Changes the output status for the active port.
	 */
	public void changeStatus() 
	{	
		this.setOutputStatus(!this.getOutputStatus());
	}



	/**
	 * Sends the output port and status to the bot via HTTP
	 */
	public  void talkToBot()
	{
		boolean output = this.getOutputStatus();
		String request = "";		
		if(output) {
			request="setoutput="+Integer.toString(outputPort)+"on";
		}
		else {
			request = "setoutput="+Integer.toString(outputPort)+"off";
		}
		try {		
			httpCon.request(request);				
		} catch (Exception e) {		
			e.printStackTrace();
		}	
	}
}
