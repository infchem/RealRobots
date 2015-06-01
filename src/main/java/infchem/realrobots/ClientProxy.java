package infchem.realrobots;

import infchem.realrobots.entity.EntityCannybot;
import infchem.realrobots.tileentity.TESRWeDo;
import infchem.realrobots.tileentity.TileEntityWeDo;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void load() {
		super.load();
	//	this.registerTESR();

	}
	
	public void registerTESR(){

        // 
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeDo.class, new TESRWeDo());
    }
	
	
}