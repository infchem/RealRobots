package infchem.realrobots.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOutputPortHigh0 extends Item {

	public ItemOutputPortHigh0(String string) {
		super();
		maxStackSize = 1;

		setUnlocalizedName(string);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("realrobots:leonardooutputporthigh0");
	}
}
