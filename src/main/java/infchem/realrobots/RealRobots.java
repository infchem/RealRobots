package infchem.realrobots;

import infchem.realrobots.block.BlockLeonardo;
import infchem.realrobots.block.BlockPiInput;
import infchem.realrobots.block.BlockPiOutput;
import infchem.realrobots.block.BlockScratch;
import infchem.realrobots.block.BlockWeDo;
import infchem.realrobots.commands.CommandSetPi;
import infchem.realrobots.commands.CommandSetScratch;
import infchem.realrobots.leonardo.LeonardoVPLMessage;
import infchem.realrobots.pi.PiMessage;
import infchem.realrobots.scratch.ScratchRRMessage;
import infchem.realrobots.wedo.WeDoCCMessage;
import infchem.realrobots.wedo.WeDoRedstoneMessage;
import infchem.realrobots.wedo.WeDoVPLMessage;
import java.io.File;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "realrobots", name = "Real Robots", version = "0.8", dependencies = "after:ComputerCraft", useMetadata = true)
public class RealRobots {
	public static SimpleNetworkWrapper network;

	@Instance("realrobots")
	public static RealRobots instance;

	@SidedProxy(clientSide = "infchem.realrobots.ClientProxy", serverSide = "infchem.realrobots.CommonProxy")
	public static CommonProxy proxy;

	// block declarations
	public static class Blocks {
		public static BlockWeDo wedoBlock;
		public static BlockLeonardo leonardoBlock;
		public static BlockPiInput piInputBlock;
		public static BlockPiOutput piOutputBlock;
		public static BlockScratch scratchBlock;
	}

	// block IDs
	public static int blockID;
	public static boolean piEnabled;
	public static String piIp;
	public static boolean scratchEnabled;
	public static String scratchIp;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		/**
		 * Registering network stuff
		 */
		network = NetworkRegistry.INSTANCE
				.newSimpleChannel("RealRobotsChannel");
		network.registerMessage(WeDoVPLMessage.Handler.class,
				WeDoVPLMessage.class, 0, Side.CLIENT);
		network.registerMessage(LeonardoVPLMessage.Handler.class,
				LeonardoVPLMessage.class, 1, Side.CLIENT);
		network.registerMessage(PiMessage.Handler.class, PiMessage.class, 2,
				Side.CLIENT);
		network.registerMessage(ScratchRRMessage.Handler.class,
				ScratchRRMessage.class, 3, Side.CLIENT);
		network.registerMessage(WeDoCCMessage.Handler.class,
				WeDoCCMessage.class, 4, Side.CLIENT);
		network.registerMessage(WeDoCCMessage.Handler.class,
				WeDoCCMessage.class, 5, Side.SERVER);
		network.registerMessage(WeDoRedstoneMessage.Handler.class,
				WeDoRedstoneMessage.class, 6, Side.SERVER);

		/**
		 * Loading configuration file
		 */
		File newFile = new File(Loader.instance().getConfigDir(),
				"RealRobots.cfg");
		Configuration config = new Configuration(newFile);
		config.load();
		// Pi
		config.setCategoryComment("piface",
				"Change IP:Port and enable communication here...");
		this.piEnabled = config.get("piface", "active", false).getBoolean();
		this.piIp = config.get("piface", "IP", "127.0.0.1").getString();
		// Scratch
		config.setCategoryComment("scratch",
				"Change IP and enable communication here...");
		this.piEnabled = config.get("scratch", "active", false).getBoolean();
		this.piIp = config.get("scratch", "IP", "127.0.0.1").getString();
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.load();
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		/**
		 * Registering the commands
		 */
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		manager.registerCommand(new CommandSetPi());
		manager.registerCommand(new CommandSetScratch());

	}

	public String getVersion() {
		return "1.0";
	}

	public String getPiIp() {
		return this.piIp;
	}

	public String getScratchIp() {
		return this.scratchIp;
	}
}