package infchem.realrobots.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;

public class TESRWeDo extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity te, double x,
			double y, double z, float p_147500_8_) {
		this.bindTexture(TextureMap.locationBlocksTexture);
		TileEntityWeDo tewd = (TileEntityWeDo)te;
	  //  Tessellator tessellator = Tessellator.instance;
	    GL11.glPushMatrix();
	    GL11.glTranslated(x, y+1, z); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
//	    tessellator.startDrawingQuads();
//	    tessellator.addVertexWithUV(0, 0, 0, 0, 0);
//	    tessellator.addVertexWithUV(0, 1, 0, 0, 1);
//	    tessellator.addVertexWithUV(1, 1, 0, 1, 1);
//	    tessellator.addVertexWithUV(1, 0, 0, 1, 0);
//
//	    tessellator.addVertexWithUV(0, 0, 0, 0, 0);
//	    tessellator.addVertexWithUV(1, 0, 0, 1, 0);
//	    tessellator.addVertexWithUV(1, 1, 0, 1, 1);
//	    tessellator.addVertexWithUV(0, 1, 0, 0, 1);
	    
//
//	    tessellator.draw();
	 //   GL11.glPopMatrix();
	    
	    renderLabel(new ChunkCoordinates(tewd.xCoord,tewd.yCoord,tewd.zCoord),tewd.playerName);
		
	}
	
	private void renderLabel(ChunkCoordinates position, String label)
	  {
	    RenderManager renderManager = RenderManager.instance;
	    MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
	    if ((mop != null) && (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (mop.blockX == position.posX) && (mop.blockY == position.posY) && (mop.blockZ == position.posZ))
	    {
	      FontRenderer fontrenderer = renderManager.getFontRenderer();
	      float scale = 0.02666667F;
	      GL11.glPushMatrix();

	      GL11.glTranslated(0.5D, 1.25D, 0.5D);
	      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	      GL11.glRotatef(-renderManager.playerViewX, 0.0F, 1.0F, 0.0F);
	      GL11.glRotatef(renderManager.playerViewY, 1.0F, 0.0F, 0.0F);
	      GL11.glScalef(-scale, -scale, scale);

	      GL11.glDisable(2896);
	      GL11.glDepthMask(false);
	      GL11.glDisable(2929);
	      GL11.glEnable(3042);
	      GL11.glBlendFunc(770, 771);

	      Tessellator tessellator = Tessellator.instance;

	      int yOffset = 0;
	      int xOffset = fontrenderer.getStringWidth(label) / 2;
	      GL11.glDisable(3553);
	      tessellator.startDrawingQuads();;
	      tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
	      tessellator.addVertex(-xOffset - 1, -1 + yOffset, 0.0D);
	      tessellator.addVertex(-xOffset - 1, 8 + yOffset, 0.0D);
	      tessellator.addVertex(xOffset + 1, 8 + yOffset, 0.0D);
	      tessellator.addVertex(xOffset + 1, -1 + yOffset, 0.0D);
	      tessellator.draw();
	      GL11.glEnable(3553);

	      fontrenderer.drawString(label, -fontrenderer.getStringWidth(label) / 2, yOffset, 553648127);

	      GL11.glEnable(2929);
	      GL11.glDepthMask(true);
	      fontrenderer.drawString(label, -fontrenderer.getStringWidth(label) / 2, yOffset, -1);
	      GL11.glEnable(2896);
	      GL11.glDisable(3042);
	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	      GL11.glPopMatrix();
	    }
	  }
	
	

}
