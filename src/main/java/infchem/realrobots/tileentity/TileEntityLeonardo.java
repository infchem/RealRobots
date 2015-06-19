package infchem.realrobots.tileentity;

import infchem.realrobots.CallbackSystem;
import infchem.realrobots.CallbackSystem.Callback;
 




import infchem.realrobots.leonardo.ConnectorLeonardo;
import infchem.realrobots.leonardo.LeonardoVPLMessage;
import infchem.realrobots.leonardo.RunnableLeonardo;









import infchem.realrobots.wedo.ConnectorWeDo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileEntityLeonardo extends TileEntity implements IPeripheral, IInventory  {

	
	private ItemStack[] commands = new ItemStack[10];

	private boolean redstoneStatus=false;
	//private static final int BUFSIZE = 8;
	public String playerName = "";
	//static final int VENDOR_ID = 0x694;
	//static final int PRODUCT_ID = 0x3;
	//Double leftMotorPower = 0.0;
	//Double rightMotorPower = 0.0;


//	public static byte[] al = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	public TileEntityLeonardo() {
	}

	static {
		ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	public synchronized void detach(IComputerAccess icomputeraccess) {
	}

	public void attach(IComputerAccess icomputeraccess) {
	}

	public String getType() {
		return "Leonardo Peripheral";
	}

	public String[] getMethodNames() {
			return (new String[] { "getRaw", "setInput", "setOutputHigh", "setOutputLow", "getInput" });
	}

	
	
	public Object[] callMethod(IComputerAccess icomputeraccess,ILuaContext context, int i, Object aobj[])
	{
		if (playerName.equals(""))
		{
			Object[] result = {"No player associated with the controller. Please right click on the controller to associate yourself"};
			return result;
		}
	
		switch(i)
		{	
		case 0: //getRaw
			return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
		//arduino leonardo
		case 1: //setInput
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
		case 2: //setOutputHigh
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
		case 3: //setOutputLow
			return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, false)};
		case 4: //getInput
			try {
				checkDouble(aobj, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Object[]{callMethodOnClient(playerName,  icomputeraccess, i, aobj, true)};
		
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
						if (icomputeraccess instanceof IComputerAccess) icomputeraccess.queueEvent("leonardo_success", returnObjects);
					} 

					@Override
					public void abort(Object[] returnObjects)
					{
						if (icomputeraccess instanceof IComputerAccess) icomputeraccess.queueEvent("leonardo_failure", returnObjects);
					}
				});
				
				
		///		Packet packet = PacketSystem.createRealPacket(xCoord, yCoord, zCoord, callbackID, nbt, "WeDoChannel");
		///		player.playerNetServerHandler.sendPacketToPlayer(packet);
				return callbackID;
			}
			else
			{
		///		Packet packet = PacketSystem.createRealPacket(xCoord, yCoord, zCoord, -1, nbt, "WeDoChannel");
		///		player.playerNetServerHandler.sendPacketToPlayer(packet);

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
		ConnectorLeonardo cal = new ConnectorLeonardo();
		Double port;
		switch (i)
		{
			case 0:
				return cal.getRaw();
				//leonardo
			case 1: //setInput
				cal.setInputPort((Integer) aobj[0]);
			return null;
			case 2: //setOutputHigh		
				cal.setOutputHigh( (Integer) aobj[0]);
				return null;		
			case 3: //setOutputLow
				cal.setOutputLow( (Integer) aobj[0]);
				return null;
			case 4:   //getInput
				return cal.getInput((Double) aobj[0]);
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
			//case 7: return ((NBTTagByteArray) nbt).byteArray;
			//case 8: return ((NBTTagString) nbt).data;
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

	
	
	
	
	
	
//	public void handlePacket(MessageContext net, WeDoMessage pkt)
//	{
//		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
//		{
//			boolean success = true;
//			Object[] objArgs = (Object[]) NBTToObject(pkt.nbt); 
//			int callback = pkt.callbackID;
//			
//			int method = pkt.nbt.getByte("methodID");
//			
//			Object[] returnArgs;
//			
//			try {
//				returnArgs = clientCallMethod(method, objArgs);
//			} catch (Exception e) {
//				success = false;
//				returnArgs = new Object[] {e.getMessage()};
//			}
//			
//			if (callback >= 0)
//			{
//				NBTTagCompound nbt = (NBTTagCompound) objectToNBT("", returnArgs);
//				nbt.setBoolean("success", success);
//			//	Packet packet = PacketSystem.createRealPacket(xCoord, yCoord, zCoord, callback, nbt, "WeDoChannel" );
//			//	net.addToSendQueue(packet);
//			}
//		}
//		else
//		{
//			Object[] returnObjects = (Object[]) NBTToObject(pkt.nbt);
//			int callback = pkt.callbackID;
//			boolean success = pkt.nbt.getBoolean("success");
//			
//			if (success)
//			{
//				CallbackSystem.activateCallback(callback, returnObjects);
//			}
//			else
//			{
//				CallbackSystem.abortCallback(callback, returnObjects);
//			}
//			
//		}
//	}
	
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
	   

//	public void handleVPLPacket(LeonardoVPLMessage pkt) {
//		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
//		{				
//				Runnable r = new RunnableLeonardo(this.commands);
//				new Thread(r).start();			
//		}        
//	}


	public String getPlayerName() {
		return playerName;
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

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		
	}
	
	public void setRedstoneStatus(boolean status) {
		if(!this.worldObj.isRemote) {
			this.redstoneStatus = status;
		}
	}


	public boolean getRedstoneStatus() {
		return this.redstoneStatus;
	}
}
