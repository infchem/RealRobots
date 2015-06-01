package infchem.realrobots.item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemRightMotorClockwise extends Item {

        public ItemRightMotorClockwise(String string) {
        	super();
                maxStackSize = 64;
                setCreativeTab(CreativeTabs.tabRedstone);
                setUnlocalizedName(string);
        }
        
        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister par1IconRegister)
        {
            this.itemIcon = par1IconRegister.registerIcon( "realrobots:wedorightmotorclockwise");
        }
}