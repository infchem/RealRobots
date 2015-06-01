package infchem.realrobots.scratch;

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
import infchem.realrobots.RealRobots;
import infchem.realrobots.connector.ConnectorHTTP;
import infchem.realrobots.tileentity.TileEntityPiInput;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class ScratchRRMessage implements IMessage {

	private String sensor;
	private String value;
	
	public ScratchRRMessage() {	
	}
	
	public ScratchRRMessage(String sensor, String value) {
		this.sensor = sensor;
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.sensor = ByteBufUtils.readUTF8String(buf);
		this.value = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.sensor);
		ByteBufUtils.writeUTF8String(buf, this.value);
	}
	
	public static class Handler implements IMessageHandler<ScratchRRMessage, IMessage> {
        
        @Override
        public IMessage onMessage(ScratchRRMessage message, MessageContext ctx) {  
        	ScratchIO sio = new ScratchIO();
    		sio.updateMsg(message.sensor,message.value);
    				
    		return null; // no response in this case
        }
	}
}
	
