package infchem.realrobots.pi;

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

public class PiMessage implements IMessage {

	private int outputPort;
	private boolean outputStatus;

	public PiMessage() {
	}

	public PiMessage(int outputPort, boolean outputStatus) {
		this.outputPort = outputPort;
		this.outputStatus = outputStatus;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.outputPort = buf.readInt();
		this.outputStatus = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.outputPort);
		buf.writeBoolean(this.outputStatus);
	}

	public static class Handler implements IMessageHandler<PiMessage, IMessage> {

		@Override
		public IMessage onMessage(PiMessage message, MessageContext ctx) {

			int outputPort = message.outputPort;
			boolean output = message.outputStatus;
			String request = "";
			if (output) {
				request = "setoutput=" + Integer.toString(outputPort) + "on";
			} else {
				request = "setoutput=" + Integer.toString(outputPort) + "off";
			}
			try {
				ConnectorHTTP httpCon = new ConnectorHTTP();
				httpCon.request(request);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null; // no response in this case
		}
	}
}
