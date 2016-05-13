package infchem.realrobots.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMotorAClockwise extends Item {

	public ItemMotorAClockwise(String string) {
		super();
		maxStackSize = 1;
		this.setUnlocalizedName(string);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("nxt:matr");
	}
}
