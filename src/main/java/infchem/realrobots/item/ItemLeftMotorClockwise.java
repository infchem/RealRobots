package infchem.realrobots.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemLeftMotorClockwise extends Item {

        public ItemLeftMotorClockwise(String string) {
        	super();
                maxStackSize = 1;
                setCreativeTab(CreativeTabs.tabRedstone);
                setUnlocalizedName(string);
                setTextureName("wedo:lmtr");
        }
        
        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister par1IconRegister)
        {
            this.itemIcon = par1IconRegister.registerIcon( "realrobots:wedoleftmotorclockwise");
        }
}