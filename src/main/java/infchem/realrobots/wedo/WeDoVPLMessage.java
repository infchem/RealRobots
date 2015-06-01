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
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class WeDoVPLMessage implements IMessage {
	

	public int callbackID;

	private ItemStack[] is = new ItemStack[10];

	private String playername;

	private int x;

	private int y;

	private int z;



	public WeDoVPLMessage() {}

	public WeDoVPLMessage(ItemStack[] itemStacks,String playername,int x,int y,int z) {

		this.is = itemStacks;
		this.playername = playername;
		this.x=x;
		this.y=y;
		this.z=z;
		
		
	}


	@Override
	public void fromBytes(ByteBuf buf) {

		for(int i=0;i<10;i++)
		{
			this.is[i] = ByteBufUtils.readItemStack(buf);
		}
		this.playername=ByteBufUtils.readUTF8String(buf);
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		
		
	}

	@Override
	public void toBytes(ByteBuf buf) {

	    for(int i=0;i<10;i++)
		{
			ByteBufUtils.writeItemStack(buf, this.is[i]);
		}
	    ByteBufUtils.writeUTF8String(buf, playername);
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	}
	
public static class Handler implements IMessageHandler<WeDoVPLMessage, IMessage> {
        
        @Override
        public IMessage onMessage(WeDoVPLMessage message, MessageContext ctx) {        	
        	Runnable r = new RunnableWeDo(message.is,message.playername,message.x,message.y,message.z);
        	new Thread(r).start();	
            return null; // no response in this case
        }
    }
}
