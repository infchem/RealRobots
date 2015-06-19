package infchem.realrobots.commands;

import infchem.realrobots.RealRobots;

import java.io.File;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CommandSetCannybot extends CommandBase{

	@Override
	public String getCommandName() {
		return "cannybot";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/cannybot active <true|false> activates or deactivates the communication";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		File newFile = new File(Loader.instance().getConfigDir(), "RealRobots.cfg");
		Configuration config = new Configuration(newFile);
		config.load();

		if (astring[0].equals("active") && !astring[1].isEmpty()) {
			Property refreshData = new Property("active",astring[1],Type.BOOLEAN);
			config.setCategoryComment("cannybot", "Enable communication here...");
			config.getCategory("cannybot").put("active", refreshData);   		
			config.save();
			RealRobots.cannybotEnabled = astring[1].equals("true");
		}
	}
}
