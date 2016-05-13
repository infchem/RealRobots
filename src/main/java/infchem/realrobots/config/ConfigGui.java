package infchem.realrobots.config;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import infchem.realrobots.RealRobots;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), "realrobots", false, false, "RealRobots configuration");
	}

	/** Compiles a list of config elements */
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		list.add(categoryElement(ConfigHandler.Category.RASPI, "RaspberryPi w/ PiFace", "config.category.raspi"));
		list.add(categoryElement(ConfigHandler.Category.SCRATCH, "Scratch", "config.category.scratch"));
		list.add(categoryElement(ConfigHandler.Category.CANNYBOT, "Cannybot", "config.category.cannybot"));
		return list;
	}

	/**
	 * Creates a button linking to another screen where all options of the
	 * category are available
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
		return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
				new ConfigElement(ConfigHandler.config.getCategory(category)).getChildElements());
	}
}
