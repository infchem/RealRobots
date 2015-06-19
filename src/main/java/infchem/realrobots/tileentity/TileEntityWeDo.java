package infchem.realrobots.tileentity;

import infchem.realrobots.CallbackSystem;
import infchem.realrobots.RealRobots;
import infchem.realrobots.CallbackSystem.Callback;
import infchem.realrobots.wedo.ConnectorWeDo;
import infchem.realrobots.wedo.WeDoCCMessage;
import infchem.realrobots.wedo.WeDoVPLMessage;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.codeminders.hidapi.ClassPathLibraryLoader;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileEntityWeDo extends TileEntity implements IPeripheral, IInventory   {

	
	private ItemStack[] commands = new ItemStack[10];
	public String playerName = "";
	private boolean redstoneStatus=false;

	
	public TileEntityWeDo() {
	}

	static {
		ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	public synchronized void detach(IComputerAccess icomputeraccess) {
	}

	public void attach(IComputerAccess icomputeraccess) {
	}

	public String getType() {
		return "WeDo Peripheral";
	}

	public String[] getMethodNames() {
			return (new String[] { "leftMotor", "rightMotor", "bothMotors",	
					"getTilt", "getDistance", "getRaw", "getLeftRotation"});//, 
					//"getByte"});
	}

	
	
	public Object[] callMethod(IComputerAccess icomputeraccess,ILuaContext context, int i, Object aobj[])
	{
		if (playerName.equals(""))
		{
//			try {
//				throw new Exception ("No player associated with the controller. Please right click on the controller to associate yourself.");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			Object[] result = {"No player associated with the controller. Please right click on the controller to associate yourself"};
			return result;
		}
	
		switch(i)
		{	
			case 0: //leftMotor
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
			case 1: //rightMotor
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
			case 2: //bothMotors
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				checkDouble(aobj, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
			case 3://getTilt
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
			case 4: //getDistance
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
			case 5: //getRaw
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
			case 6: //getLeftRotation
				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
//			case 7: //getByte
//				return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
			default:
				return null;
		}
	}

	private int callMethodOnClient(String string, final IComputerAccess icomputeraccess, int i, Object[] aobj, boolean requiresResponse)
	{
		
		
		EntityPlayer player = this.worldObj.getPlayerEntityByName(string);
		
		if (player != null)
		{
			NBTTagCompound nbt = (NBTTagCompound) objectToNBT("nbt", aobj);
			nbt.setByte("methodID", (byte) i);
			int callbackID = 0;
			if (requiresResponse)
			{
				final long expiryTime = System.currentTimeMillis() + 2000;
				callbackID = CallbackSystem.queueCallback(new Callback()
				{

					@Override
					public long getExpiryTime()
					{
						return expiryTime;
					}

					@Override
					public void execute(Object[] returnObjects)
					{
						if (icomputeraccess instanceof IComputerAccess) icomputeraccess.queueEvent("wedo_success", returnObjects);
					} 

					@Override
					public void abort(Object[] returnObjects)
					{
						if (icomputeraccess instanceof IComputerAccess) icomputeraccess.queueEvent("wedo_failure", returnObjects);
					}
				});
				
				
				RealRobots.network.sendTo(new WeDoCCMessage(xCoord, yCoord, zCoord, callbackID, nbt), (EntityPlayerMP) player);
				return callbackID;
			}
			else
			{
				RealRobots.network.sendTo(new WeDoCCMessage(xCoord, yCoord, zCoord, -1, nbt), (EntityPlayerMP) player);

			}
		}
		return -1;
	}

	
	public boolean canAttachToSide(int i) {
		return true;
	}
	
	private void checkDouble(Object[] objects, int index) throws Exception
	{
		if (objects.length >= index + 1 && objects[index] != null && objects[index] instanceof Double)
		{
			return;
		}
		
		throw new Exception("Number expected at index " + index);
	}
	
	public Object[] clientCallMethod(int i, Object aobj[]) throws Exception
	{
		ConnectorWeDo cwd = new ConnectorWeDo();
		switch (i)
		{
		case 0:
			
			cwd.setLeftMotorPower((Double) aobj[0]);
			cwd.motorControl();
			return null;
		case 1:
			cwd.setRightMotorPower((Double) aobj[0]);			
			cwd.motorControl();
			return null;
		case 2:
			cwd.setLeftMotorPower((Double) aobj[0]);
			cwd.setRightMotorPower((Double) aobj[1]);
			cwd.motorControl();
			return null;
		case 3:
			return cwd.getTilt();
		case 4:
			return cwd.getDistance();
		case 5:
			return cwd.getRaw();
		case 6:
			return cwd.getLeftRotation();
//		case 7:   //getByte
//		    Object[] result = {""};
//			result[0] = Arrays.toString(al);
//		    return result;
		default:
			return null;
		}
	}

	public static NBTBase objectToNBT(String name, Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		if (obj instanceof Byte)
		{
			return new NBTTagByte((Byte)obj);
		}
		else if (obj instanceof Short) 
		{
			return new NBTTagShort((Short)obj);
		}
		else if (obj instanceof Integer)
		{
			return new NBTTagInt((Integer)obj);
		}
		else if (obj instanceof Long)
		{
			return new NBTTagLong((Long)obj);
		}
		else if (obj instanceof Float)
		{
			return new NBTTagFloat((Float)obj);
		}
		else if (obj instanceof Double)
		{
			return new NBTTagDouble((Double)obj);
		}
		else if (obj instanceof Byte[])
		{
			return new NBTTagByteArray((byte[])obj);
		}
		else if (obj instanceof String)
		{
			return new NBTTagString(name);
		}
		else if (obj instanceof Integer)
		{
			return new NBTTagInt((Integer)obj);
		}
		else if (obj instanceof Object[])
		{
			NBTTagCompound tag = new NBTTagCompound();
			int tagIndex = 0;
			for (Object object:(Object[]) obj)
			{
				NBTBase nbt = objectToNBT(((Integer)tagIndex).toString(), object);
				if (nbt != null)
					tag.setTag(((Integer)tagIndex).toString(), nbt);
				tagIndex++;
			}
			return tag;
		}
		
		return null;
	}
	
	public static Object NBTToObject(NBTBase nbt)
	{
		if (nbt == null)
		{
			return null;
		}
		byte nbtType = nbt.getId();
		
		switch (nbtType)
		{
			case 1: return ((NBTTagByte) nbt).func_150290_f();
			case 2: return ((NBTTagShort) nbt).func_150290_f();
			case 3: return ((NBTTagInt) nbt).func_150287_d();
			case 4: return ((NBTTagLong) nbt).func_150291_c();
			case 5: return ((NBTTagFloat) nbt).func_150288_h();
			case 6: return ((NBTTagDouble) nbt).func_150286_g();
			case 7: return ((NBTTagByteArray) nbt).func_150292_c();
			case 8: return ((NBTTagString) nbt).func_150285_a_();
			case 10: 
				NBTTagCompound nbtTagCompound = (NBTTagCompound) nbt;
				int tagNum = 0;
				ArrayList arrayList = new ArrayList();
				NBTBase tag = nbtTagCompound.getTag("0");
				while (tag != null)
				{
					arrayList.add(NBTToObject(tag));
					tagNum++;
					tag = nbtTagCompound.getTag(((Integer)tagNum).toString());
				}
				
				return arrayList.toArray();
			default:
				return null;
		}
		
	}

	
	
	
	
	
	
	public void handlePacket(int callback,NBTTagCompound nbt)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			boolean success = true;
			Object[] objArgs = (Object[]) NBTToObject(nbt); 

			
			int method = nbt.getByte("methodID");
			
			
			Object[] returnArgs;
			
			try {
				returnArgs = clientCallMethod(method, objArgs);
				
			} catch (Exception e) {
				success = false;
				returnArgs = new Object[] {e.getMessage()};
			}
			
			if (callback >= 0)
			{
				//Minecraft.getMinecraft().thePlayer.sendChatMessage(returnArgs[0].toString());
				NBTTagCompound nbt1 = new NBTTagCompound();
				
				//NBTTagCompound nbt1  = (NBTTagCompound) objectToNBT("", returnArgs);
//				Minecraft.getMinecraft().thePlayer.sendChatMessage(Byte.toString(nbt1.getId()));
				nbt1.setString("value", returnArgs[0].toString());
				//Minecraft.getMinecraft().thePlayer.sendChatMessage(nbt1.getString("sensor"));
				nbt1.setBoolean("success", success);
//				Minecraft.getMinecraft().thePlayer.sendChatMessage(returnArgs[0].toString());
				RealRobots.network.sendToServer(new WeDoCCMessage(xCoord, yCoord, zCoord, callback, nbt1));
			}
		}
		else
		{
			Object[] returnObjects = {""};//) NBTToObject(nbt);
			returnObjects[0]=nbt.getString("value");
			
		//	Object[] returnObjects = (Object[]) NBTToObject(nbt);
			//Minecraft.getMinecraft().thePlayer.sendChatMessage(returnObjects[0].toString());
			boolean success = nbt.getBoolean("success");
		//	Minecraft.getMinecraft().thePlayer.sendChatMessage(Integer.toString(callback)); 
			if (success)
			{
				CallbackSystem.activateCallback(callback, returnObjects);
			//	Minecraft.getMinecraft().thePlayer.sendChatMessage("activated"); 
			}
			else
			{
				CallbackSystem.abortCallback(callback, returnObjects);
				//Minecraft.getMinecraft().thePlayer.sendChatMessage("aborted");
			}
			
		}
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}

	
	/**
     * Returns the number of slots in the inventory.
     */
	@Override
	public int getSizeInventory() {
	   if (commands != null) {
	      return commands.length;
	   } else {
	      return 0;
	   }
    }
	
	/**
     * Returns the stack in slot i
     */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.commands[slot];
	}
	
	/**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	   @Override
	   public ItemStack decrStackSize(int slot, int amt) {
	        ItemStack stack = getStackInSlot(slot);
	        if (stack != null) {
	                if (stack.stackSize <= amt) {
	                        setInventorySlotContents(slot, null);
	                } else {
	                        ItemStack newstack = stack.splitStack(stack.stackSize - amt);
	                        setInventorySlotContents(slot, newstack);
	                }
	        }
	        return stack;
	   }
	   
	   /**
	     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	     * like when you close a workbench GUI.
	     */
	   @Override
	   public ItemStack getStackInSlotOnClosing(int slot) {
	        ItemStack stack = getStackInSlot(slot);
	        if (stack != null) {
	                setInventorySlotContents(slot, null);
	        }
	        return stack;
	   }
	   
	   /**
	     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	     */
	   @Override
	   public void setInventorySlotContents(int slot, ItemStack stack) {
		   this.commands[slot] = stack;
		   if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        {
	            stack.stackSize = this.getInventoryStackLimit();
	        }
	   }
	   
	   /**
	     * Returns the name of the inventory
	     */
	   @Override
		public String getInventoryName() {
			return null;
		}
	   
	   /**
	     * Returns if the inventory is named
	     */
		@Override
		public boolean hasCustomInventoryName() {
			return false;
		}
		
		
	   public void readFromNBT(NBTTagCompound p_145839_1_)
	    {
	        super.readFromNBT(p_145839_1_);
	        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
	        this.commands = new ItemStack[this.getSizeInventory()];


	        for (int i = 0; i < nbttaglist.tagCount(); ++i)
	        {
	            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
	            int j = nbttagcompound1.getByte("Slot") & 255;

	            if (j >= 0 && j < this.commands.length)
	            {
	                this.commands[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	            }
	        }
	      //  this.setRedstoneStatus(p_145839_1_.getBoolean("redstoneStatus"));
	    }

	    public void writeToNBT(NBTTagCompound p_145841_1_)
	    {
	        super.writeToNBT(p_145841_1_);
	        NBTTagList nbttaglist = new NBTTagList();

	        for (int i = 0; i < this.commands.length; ++i)
	        {
	            if (this.commands[i] != null)
	            {
	                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	                nbttagcompound1.setByte("Slot", (byte)i);
	                this.commands[i].writeToNBT(nbttagcompound1);
	                nbttaglist.appendTag(nbttagcompound1);
	            }
	        }

	        p_145841_1_.setTag("Items", nbttaglist); 
	       
	      //  p_145841_1_.setBoolean("redstoneStatus", getRedstoneStatus());
	       // System.out.println(getRedstoneStatus());
	    	
	    }

	   
	    /**
	     * Returns the maximum stack size for a inventory slot.
	     */
	    public int getInventoryStackLimit()
	    {
	        return 1;
	    }
	   
	   @Override
	   public boolean isUseableByPlayer(EntityPlayer player) {
	        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
	        player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	   }
	   
	   @Override
		public void openInventory() {	
		}

		@Override
		public void closeInventory() {		
		}	
	   
		/**
	     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	     */
	   @Override
	   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
	      return false;
	   }

	/**
	 * Gets the IInventory with all command slots,
	 * including the empty ones. 
	 * 
	 * @return IInventory
	 */
	   public ItemStack[] getCommands() {
		return commands;
	}
	
	public String getCommandAtSlot(int slot) {
		ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
        	return(stack.getItem().getUnlocalizedName());
        }
        return "";
	}
	   
	public String getPlayerName() {
		return playerName;
	}
	
	public void setRedstoneStatus(boolean status) {
		if(!this.worldObj.isRemote) {
			this.redstoneStatus = status;
		}
	}


	public boolean getRedstoneStatus() {
		return this.redstoneStatus;
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

}
