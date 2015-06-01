package infchem.realrobots.item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMotorBStop extends Item {

        public ItemMotorBStop(String string) {
        	super();
                maxStackSize = 1;
                setCreativeTab(CreativeTabs.tabRedstone);
                setUnlocalizedName(string);
        }
        
        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister par1IconRegister)
        {
            this.itemIcon = par1IconRegister.registerIcon( "nxt:mbs");
        }
}