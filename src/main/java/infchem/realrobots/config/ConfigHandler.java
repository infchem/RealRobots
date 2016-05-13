package infchem.realrobots.config;

import java.io.File;


import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;
	
	public static String piIP = "127.0.0.1";
	public static boolean piEnabled = false;
	public static String scratchIP = "127.0.0.1";
	public static boolean scratchEnabled = false;
	public static boolean cannybotEnabled = false;

	public static class Category { 
		public static String RASPI = "raspberrypi";
		public static String SCRATCH = "scratch";
		public static String CANNYBOT = "cannybot";
	}
	
	public static void init(File file) {
		if (config == null)
		{
			config = new Configuration(file);
			try 
			{
				config.load();
			}
			catch (Exception e)
			{
			}
			updateConfig();
		}
	}

	private static void updateConfig()
	{
		
		piIP = config.getString("IP address", Category.RASPI, piIP,
				"Here you set the IP adress of your PI.");
		piEnabled = config.getBoolean("Enabled", Category.RASPI, piEnabled,
				"Set to true to enable the communication.");

		scratchIP = config.getString("IP address", Category.SCRATCH, scratchIP,
				"Here you set the IP adress of the computer where Scratch runs.");
		scratchEnabled = config.getBoolean("Enabled", Category.SCRATCH, scratchEnabled,
				"Set to true to enable the communication.");

		cannybotEnabled = config.getBoolean("Enabled", Category.CANNYBOT, cannybotEnabled,
				"Set to true to enable the communication.");

	
		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equalsIgnoreCase("realrobots"))
		{
			updateConfig();
		}
	}
}
