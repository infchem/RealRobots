package infchem.realrobots.gui;

import infchem.realrobots.leonardo.ContainerLeonardo;
import infchem.realrobots.tileentity.TileEntityLeonardo;
import infchem.realrobots.tileentity.TileEntityWeDo;
import infchem.realrobots.wedo.ContainerWeDo;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiLeonardo extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("realrobots","textures/gui/leonardo.png");
	
	public GuiLeonardo (InventoryPlayer inventoryPlayer,
            TileEntityLeonardo tileEntity) {
    //the container is instanciated and passed to the superclass for handling
    super(new ContainerLeonardo(inventoryPlayer, tileEntity));
}

	 @Override
     public void initGui()
     {
            super.initGui();
             int posX = (this.width - xSize) / 2;
             int posY = (this.height - ySize) / 2;
    
     }


@Override
protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    //draw text and stuff here
    //the parameters for drawString are: string, x, y, color
    this.fontRendererObj.drawString("      If      then", 7, 21, 2273356);//3029133
 //   fontRenderer.drawString("Input", 8, 60, 4210752);
 //   fontRenderer.drawString("Input", 8, 60, 4210752);
    this.fontRendererObj.drawSplitString("RealRobots for Arduino Leonardo",3, 6,175, 15539236);//4210752


    //draws "Inventory" or your regional equivalent
    this.fontRendererObj.drawString("Your inventory", 90, ySize - 96 + 2, 4210752);
}

@Override
protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //draw your Gui here, only thing you need to change is the path
	
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
}

}
