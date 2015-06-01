package infchem.realrobots.tileentity;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import infchem.realrobots.RealRobots;
import infchem.realrobots.connector.ConnectorHTTP;
import infchem.realrobots.pi.PiMessage;
import infchem.realrobots.wedo.WeDoVPLMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPiInput extends TileEntity {

	public int inputPort;
	public boolean inputStatus;
	public ConnectorHTTP httpCon;
	public int delay;
	public String playerName;

	public TileEntityPiInput() {

		this.inputPort = 0;
		this.inputStatus = false;
		this.httpCon = new ConnectorHTTP();
		this.delay = 20;
		this.playerName = "";
	}

	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		par1.setBoolean("inputStatus", getInputStatus());
		par1.setInteger("inputPort", getInputPort());
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		this.setInputStatus(par1.getBoolean("inputStatus"));
		this.setInputPort(par1.getInteger("inputPort"));
	}

	@Override
	public void updateEntity() {
		if (delay > 0) {
			delay--;
			return;
		}
		delay = 20;
		if(!this.worldObj.isRemote)
		{		
			if (!getPlayerName().toString().equals("")) {
				String request="HIGH";
				try {
					request = httpCon.request("getinput="+Integer.toString(inputPort));
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.inputStatus = request.toString().equals("LOW");
			}
		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * Returns the input status
	 */
	public boolean getInputStatus() {
		return this.inputStatus;
	}

	/**
	 * Sets the input status
	 */
	public void setInputStatus(boolean newInputStatus) {
		this.inputStatus = newInputStatus;
	}

	/**
	 * returns the player name
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Returns the active input port
	 */
	public int getInputPort() {
		return this.inputPort;
	}

	/**
	 * Sets the active input port
	 */
	public void setInputPort(int newInput) {
		this.inputPort = newInput;
	}

	/**
	 * Sets the active input port automagically. The input port cycles from 0-7
	 * incrementally.
	 */
	public void setInputPort() {
		if (this.inputPort == 7)
			this.inputPort = 0;
		else
			++this.inputPort;
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

	public void changeStatus() {
		setInputPort();
	}
}
