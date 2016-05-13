package infchem.realrobots.proxy;

import infchem.realrobots.CallbackSystem;
import infchem.realrobots.RealRobots;
import infchem.realrobots.RealRobots.Blocks;
import infchem.realrobots.block.*;
import infchem.realrobots.gui.GuiHandler;
import infchem.realrobots.item.*;
import infchem.realrobots.tileentity.TileEntityCannybot;
import infchem.realrobots.tileentity.TileEntityLeonardo;
import infchem.realrobots.tileentity.TileEntityPiInput;
import infchem.realrobots.tileentity.TileEntityPiOutput;
import infchem.realrobots.tileentity.TileEntityScratch;
import infchem.realrobots.tileentity.TileEntityWeDo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class CommonProxy {

	public void load() {
		registerBlocks();
		registerTileEntities();
		registerItems();

		NetworkRegistry.INSTANCE.registerGuiHandler(RealRobots.instance, new GuiHandler());

		// CallbackSystem wird ben�tigt, damit Lua Befehle in ComputerCraft
		// f�r Real Robots Peripherals auf dem Client ausgef�hrt werden.
		CallbackSystem.start();
		// registerPeripheralProvider wird f�r ComputerCraft ben�tigt, damit
		// Real Robots Bl�cke als Peripherals verwendet werden k�nnen.
		ComputerCraftAPI.registerPeripheralProvider((IPeripheralProvider) RealRobots.Blocks.wedoBlock);
	}

	public void registerBlocks() {
		RealRobots.Blocks.wedoBlock = new BlockWeDo(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.wedoBlock.setCreativeTab(tabWeDo), "wedo");
		RealRobots.Blocks.leonardoBlock = new BlockLeonardo(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.leonardoBlock.setCreativeTab(tabLeonardo), "leonardo");
		RealRobots.Blocks.piInputBlock = new BlockPiInput(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.piInputBlock.setCreativeTab(tabPi), "piinput");
		RealRobots.Blocks.piOutputBlock = new BlockPiOutput(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.piOutputBlock.setCreativeTab(tabPi), "pioutput");
		RealRobots.Blocks.scratchBlock = new BlockScratch(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.scratchBlock.setCreativeTab(tabScratch), "scratch");
		RealRobots.Blocks.cannybotBlock = new BlockCannybot(Material.iron);
		GameRegistry.registerBlock(RealRobots.Blocks.cannybotBlock.setCreativeTab(tabCannybot), "cannybot");
	}

	public void registerItems() {

		// general purpose
		GameRegistry.registerItem(new ItemSleep("sleep").setCreativeTab(this.tabWeDo), "sleep");
		GameRegistry.registerItem(new ItemSendRedstone("sendredstone").setCreativeTab(this.tabWeDo), "sendredstone");
		GameRegistry.registerItem(new ItemDontSendRedstone("dontsendredstone").setCreativeTab(this.tabWeDo),
				"dontsendredstone");
		// wedo specific
		GameRegistry.registerItem(new ItemLeftMotorClockwise("wedoleftmotorclockwise").setCreativeTab(this.tabWeDo),
				"wedoleftmotorclockwise");
		GameRegistry.registerItem(
				new ItemLeftMotorCounterclockwise("wedoleftmotorcounterclockwise").setCreativeTab(this.tabWeDo),
				"wedoleftmotorcounterclockwise");
		GameRegistry.registerItem(new ItemRightMotorClockwise("wedorightmotorclockwise").setCreativeTab(this.tabWeDo),
				"wedorightmotorclockwise");
		GameRegistry.registerItem(
				new ItemRightMotorCounterclockwise("wedorightmotorcounterclockwise").setCreativeTab(this.tabWeDo),
				"wedorightmotorcounterclockwise");
		GameRegistry.registerItem(new ItemLeftMotorStop("wedoleftmotorstop").setCreativeTab(this.tabWeDo),
				"wedoleftmotorstop");
		GameRegistry.registerItem(new ItemRightMotorStop("wedorightmotorstop").setCreativeTab(this.tabWeDo),
				"wedorightmotorstop");
		GameRegistry.registerItem(new ItemDistanceShort("wedodistancesmall").setCreativeTab(this.tabWeDo),
				"wedodistancesmall");
		GameRegistry.registerItem(new ItemDistanceMedium("wedodistancemedium").setCreativeTab(this.tabWeDo),
				"wedodistancemedium");
		GameRegistry.registerItem(new ItemDistanceLarge("wedodistancelarge").setCreativeTab(this.tabWeDo),
				"wedodistancelarge");
		GameRegistry.registerItem(new ItemTiltNormal("wedotiltnormal").setCreativeTab(this.tabWeDo), "wedotiltnormal");
		GameRegistry.registerItem(new ItemTiltLeft("wedotiltleft").setCreativeTab(this.tabWeDo), "wedotiltleft");
		GameRegistry.registerItem(new ItemTiltRight("wedotiltright").setCreativeTab(this.tabWeDo), "wedotiltright");
		GameRegistry.registerItem(new ItemTiltForward("wedotiltforward").setCreativeTab(this.tabWeDo),
				"wedotiltforward");
		GameRegistry.registerItem(new ItemTiltBack("wedotiltback").setCreativeTab(this.tabWeDo), "wedotiltback");

		// arduino specific
		GameRegistry.registerItem(new ItemInputPort0("leonardoinputport0").setCreativeTab(this.tabLeonardo),
				"leonardoinputport0");
		GameRegistry.registerItem(new ItemInputPort1("leonardoinputport1").setCreativeTab(this.tabLeonardo),
				"leonardoinputport1");
		GameRegistry.registerItem(new ItemInputPort2("leonardoinputport2").setCreativeTab(this.tabLeonardo),
				"leonardoinputport2");
		GameRegistry.registerItem(new ItemInputPort3("leonardoinputport3").setCreativeTab(this.tabLeonardo),
				"leonardoinputport3");
		GameRegistry.registerItem(new ItemInputPort4("leonardoinputport4").setCreativeTab(this.tabLeonardo),
				"leonardoinputport4");
		GameRegistry.registerItem(new ItemInputPort5("leonardoinputport5").setCreativeTab(this.tabLeonardo),
				"leonardoinputport5");
		GameRegistry.registerItem(new ItemInputPort6("leonardoinputport6").setCreativeTab(this.tabLeonardo),
				"leonardoinputport6");
		GameRegistry.registerItem(new ItemInputPort7("leonardoinputport7").setCreativeTab(this.tabLeonardo),
				"leonardoinputport7");
		GameRegistry.registerItem(new ItemOutputPortHigh0("leonardooutputporthigh0").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh0");
		GameRegistry.registerItem(new ItemOutputPortHigh1("leonardooutputporthigh1").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh1");
		GameRegistry.registerItem(new ItemOutputPortHigh2("leonardooutputporthigh2").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh2");
		GameRegistry.registerItem(new ItemOutputPortHigh3("leonardooutputporthigh3").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh3");
		GameRegistry.registerItem(new ItemOutputPortHigh4("leonardooutputporthigh4").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh4");
		GameRegistry.registerItem(new ItemOutputPortHigh5("leonardooutputporthigh5").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh5");
		GameRegistry.registerItem(new ItemOutputPortHigh6("leonardooutputporthigh6").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh6");
		GameRegistry.registerItem(new ItemOutputPortHigh7("leonardooutputporthigh7").setCreativeTab(this.tabLeonardo),
				"leonardooutputporthigh7");
		GameRegistry.registerItem(new ItemOutputPortLow0("leonardooutputportlow0").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow0");
		GameRegistry.registerItem(new ItemOutputPortLow1("leonardooutputportlow1").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow1");
		GameRegistry.registerItem(new ItemOutputPortLow2("leonardooutputportlow2").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow2");
		GameRegistry.registerItem(new ItemOutputPortLow3("leonardooutputportlow3").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow3");
		GameRegistry.registerItem(new ItemOutputPortLow4("leonardooutputportlow4").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow4");
		GameRegistry.registerItem(new ItemOutputPortLow5("leonardooutputportlow5").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow5");
		GameRegistry.registerItem(new ItemOutputPortLow6("leonardooutputportlow6").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow6");
		GameRegistry.registerItem(new ItemOutputPortLow7("leonardooutputportlow7").setCreativeTab(this.tabLeonardo),
				"leonardooutputportlow7");
	}

	public void registerRenderInformation() {
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityWeDo.class, "TileEntityWeDo");
		GameRegistry.registerTileEntity(TileEntityLeonardo.class, "TileEntityLeonardo");
		GameRegistry.registerTileEntity(TileEntityPiInput.class, "TileEntityPiInput");
		GameRegistry.registerTileEntity(TileEntityPiOutput.class, "TileEntityPiOutput");
		GameRegistry.registerTileEntity(TileEntityScratch.class, "TileEntityScratch");
		GameRegistry.registerTileEntity(TileEntityCannybot.class, "TileEntityCannybot");
	}

	public static CreativeTabs tabWeDo = new CreativeTabs("tabWeDo") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.wedoBlock);
		}
	};

	public static CreativeTabs tabPi = new CreativeTabs("tabPi") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.piInputBlock);
		}
	};

	public static CreativeTabs tabLeonardo = new CreativeTabs("tabLeonardo") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.leonardoBlock);
		}
	};

	public static CreativeTabs tabScratch = new CreativeTabs("tabScratch") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.scratchBlock);
		}
	};

	public static CreativeTabs tabCannybot = new CreativeTabs("tabCannybot") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.cannybotBlock);
		}
	};
}
