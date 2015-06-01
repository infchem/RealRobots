package infchem.realrobots.gui;

import infchem.realrobots.leonardo.ContainerLeonardo;
import infchem.realrobots.tileentity.TileEntityLeonardo;
import infchem.realrobots.tileentity.TileEntityWeDo;
import infchem.realrobots.wedo.ContainerWeDo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	//returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityWeDo){   
            	return new ContainerWeDo(player.inventory, (TileEntityWeDo) tileEntity);
            	        
            } else if (tileEntity instanceof TileEntityLeonardo){   
            	return new ContainerLeonardo(player.inventory, (TileEntityLeonardo) tileEntity);
            
            }
            return null;
            
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityWeDo){
                    return new GuiWeDo(player.inventory, (TileEntityWeDo) tileEntity);
            }
            else if (tileEntity instanceof TileEntityLeonardo){   
            	return new GuiLeonardo(player.inventory, (TileEntityLeonardo) tileEntity);
            
            }
            return null;

    }
}

