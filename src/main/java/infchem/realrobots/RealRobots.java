package infchem.realrobots;

import infchem.realrobots.block.BlockCannybot;
import infchem.realrobots.block.BlockLeonardo;
import infchem.realrobots.block.BlockPiInput;
import infchem.realrobots.block.BlockPiOutput;
import infchem.realrobots.block.BlockScratch;
import infchem.realrobots.block.BlockWeDo;
import infchem.realrobots.cannybot.CannybotMessage;
import infchem.realrobots.config.ConfigHandler;
import infchem.realrobots.leonardo.LeonardoVPLMessage;
import infchem.realrobots.pi.PiMessage;
import infchem.realrobots.proxy.CommonProxy;
import infchem.realrobots.scratch.ScratchRRMessage;
import infchem.realrobots.wedo.WeDoCCMessage;
import infchem.realrobots.wedo.WeDoRedstoneMessage;
import infchem.realrobots.wedo.WeDoVPLMessage;

import java.io.File;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "after:ComputerCraft", useMetadata = true, guiFactory = "infchem.realrobots.config.ConfigGuiFactory")
public class RealRobots {
	public static SimpleNetworkWrapper network;

	@Instance("realrobots")
	public static RealRobots instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	// block declarations
	public static class Blocks {
		public static BlockWeDo wedoBlock;
		public static BlockLeonardo leonardoBlock;
		public static BlockPiInput piInputBlock;
		public static BlockPiOutput piOutputBlock;
		public static BlockScratch scratchBlock;
		public static BlockCannybot cannybotBlock;
	}

	public static Configuration config;

	// block IDs
	public static int blockID;

	

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// Network stuff
		network = NetworkRegistry.INSTANCE.newSimpleChannel("RealRobotsChannel");
		network.registerMessage(WeDoVPLMessage.Handler.class, WeDoVPLMessage.class, 0, Side.CLIENT);
		network.registerMessage(LeonardoVPLMessage.Handler.class, LeonardoVPLMessage.class, 1, Side.CLIENT);
		network.registerMessage(PiMessage.Handler.class, PiMessage.class, 2, Side.CLIENT);
		network.registerMessage(ScratchRRMessage.Handler.class, ScratchRRMessage.class, 3, Side.CLIENT);
		network.registerMessage(WeDoCCMessage.Handler.class, WeDoCCMessage.class, 4, Side.CLIENT);
		network.registerMessage(WeDoCCMessage.Handler.class, WeDoCCMessage.class, 5, Side.SERVER);
		network.registerMessage(WeDoRedstoneMessage.Handler.class, WeDoRedstoneMessage.class, 6, Side.SERVER);
		network.registerMessage(CannybotMessage.Handler.class, CannybotMessage.class, 7, Side.CLIENT);


		ConfigHandler.init(new File(Loader.instance().getConfigDir(), "RealRobots.cfg"));
		FMLCommonHandler.instance().bus().register(new ConfigHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.load();
		FMLCommonHandler.instance().bus().register(instance);
	}


}
