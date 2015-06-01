package infchem.realrobots.wedo;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import infchem.realrobots.tileentity.TileEntityWeDo;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class WeDoCCMessage implements IMessage {
	

	private int callbackID;

	private NBTTagCompound nbt;

	private int xCoord;

	private int yCoord;

	private int zCoord;

	public WeDoCCMessage() {}
	
	public WeDoCCMessage(int xCoord, int yCoord, int zCoord, int callbackID, NBTTagCompound nbt) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.callbackID = callbackID;
		this.nbt = nbt;
	}




	@Override
	public void fromBytes(ByteBuf buf) {

		this.xCoord = buf.readInt();
		this.yCoord = buf.readInt();
		this.zCoord = buf.readInt();
		this.callbackID = buf.readInt();
		this.nbt = ByteBufUtils.readTag(buf);

		
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(this.xCoord);
		buf.writeInt(this.yCoord);
		buf.writeInt(this.zCoord);
		buf.writeInt(this.callbackID);
		ByteBufUtils.writeTag(buf, this.nbt);
			
		
	}
	
public static class Handler implements IMessageHandler<WeDoCCMessage, IMessage> {
        
        @Override
        public IMessage onMessage(WeDoCCMessage message, MessageContext ctx) { 
        	
        	TileEntityWeDo tile = (TileEntityWeDo) Minecraft.getMinecraft().theWorld.getTileEntity(message.xCoord, message.yCoord, message.zCoord);		
			tile.handlePacket(message.callbackID,message.nbt);
            return null; // no response in this case
        }
    }
}
