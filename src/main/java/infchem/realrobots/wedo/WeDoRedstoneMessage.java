package infchem.realrobots.wedo;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import infchem.realrobots.RealRobots;
import infchem.realrobots.tileentity.TileEntityLeonardo;
import infchem.realrobots.tileentity.TileEntityWeDo;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class WeDoRedstoneMessage implements IMessage {
	

	private boolean redstoneStatus;
	public int x;
	public int y;
	public int z;



	public WeDoRedstoneMessage() {}

	public WeDoRedstoneMessage(boolean status,int x,int y,int z) {

		this.redstoneStatus = status;
		this.x=x;
		this.y=y;
		this.z=z;
		
		
		
	}


	@Override
	public void fromBytes(ByteBuf buf) {

		
		this.redstoneStatus = buf.readBoolean();
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {

	    buf.writeBoolean(redstoneStatus);
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	}
	
public static class Handler implements IMessageHandler<WeDoRedstoneMessage, IMessage> {
        
        @Override
        public IMessage onMessage(WeDoRedstoneMessage message, MessageContext ctx) { 
        	EntityPlayer player = ctx.getServerHandler().playerEntity;
        	TileEntity te =  player.worldObj.getTileEntity(message.x,message.y,message.z);
        	if (te instanceof TileEntityWeDo) {
        		TileEntityWeDo tewd = (TileEntityWeDo) te;
        	tewd.setRedstoneStatus(message.redstoneStatus);
        	tewd.getDescriptionPacket();
        	}
        	else if (te instanceof TileEntityLeonardo) {
        		TileEntityLeonardo tel = (TileEntityLeonardo) te;
        	tel.setRedstoneStatus(message.redstoneStatus);
        	tel.getDescriptionPacket();
        	}

        	player.worldObj.notifyBlocksOfNeighborChange(message.x,message.y,message.z, player.worldObj.getBlock(message.x,message.y,message.z));
        	
        	return null; // no response in this case
        }
    }
}
