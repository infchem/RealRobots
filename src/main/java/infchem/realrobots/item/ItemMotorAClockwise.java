package infchem.realrobots.item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMotorAClockwise extends Item {

        public ItemMotorAClockwise(String string) {
        	super();
                maxStackSize = 64;
                this.setCreativeTab(CreativeTabs.tabRedstone);
                this.setUnlocalizedName(string);
        }
        
        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister par1IconRegister)
        {
            this.itemIcon = par1IconRegister.registerIcon( "nxt:matr");
        }
}
