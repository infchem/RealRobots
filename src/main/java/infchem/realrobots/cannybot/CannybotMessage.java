package infchem.realrobots.cannybot;

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

public class CannybotMessage implements IMessage {
	
	

	
	private int direction;


	public CannybotMessage() 
	{	
	}
	
	
	
	public CannybotMessage(int direction) {
		this.direction = direction;

	}



	@Override
	public void fromBytes(ByteBuf buf) {
		this.direction = buf.readInt();
	
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.direction);
	}
	
public static class Handler implements IMessageHandler<CannybotMessage, IMessage> {
        
        @Override
        public IMessage onMessage(CannybotMessage message, MessageContext ctx) {  
        	
        	int direction = message.direction;
 
        	if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
        		Runtime rt = Runtime.getRuntime();
        		try {
        			switch(direction) {
        				
        				case 0:
        					Process pr0 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe 100 100 350");
        					break;
        				case 1:
        					Process pr1 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe 0 100 700");
        					break;
        				case 2:
        					Process pr2 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe -100 100 350");
        					break;
        				case 3:
        					Process pr3 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe 100 -100 350");
        					break;
        				case 4:
        					Process pr4 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe 0 -100 700");
        					break;
        				case 5:
        					Process pr5 = rt.exec(System.getenv("APPDATA")+"\\CannybotsJoypadUtil.exe -100 -100 350");
        					break;
        			}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
            return null; // no response in this case
        }
    }
}
