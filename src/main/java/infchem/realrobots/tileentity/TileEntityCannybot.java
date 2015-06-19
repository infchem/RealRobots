package infchem.realrobots.tileentity;

import infchem.realrobots.connector.ConnectorHTTP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public  class TileEntityCannybot extends TileEntity {

	public  int direction;
	public String playerName;

	public TileEntityCannybot() {
		this.direction=0;
		this.playerName ="";
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setInteger("direction", this.getDirection());
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		this.setDirection(par1.getInteger("direction"));
	}
	
	private void setDirection(int newDirection) {
		this.direction = newDirection; 
		
	}

	/**
	 * Returns the direction
	 */
	public  int getDirection() {
		return this.direction;
	}
	
	/**
	 * returns the player name
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Sets the direction
	 */
	public  void setDirection() {
		if (this.direction==5) this.direction=0; else ++this.direction; 
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
	 * Sends the direction to the bot via BLE
	 */
	public  void talkToBot()
	{
		
	}
}
