package infchem.realrobots.block;

import infchem.realrobots.RealRobots;
import infchem.realrobots.pi.PiMessage;
import infchem.realrobots.scratch.ScratchIO;
import infchem.realrobots.scratch.ScratchRRMessage;
import infchem.realrobots.tileentity.TileEntityPiInput;
import infchem.realrobots.tileentity.TileEntityPiOutput;
import infchem.realrobots.tileentity.TileEntityScratch;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockScratch extends BlockContainer
{

	private IIcon northIcon;
	private IIcon greyIcon;

	public BlockScratch(Material material) 
	{
		super(material);
		setBlockName("Scratch");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}


	/**
	 * Registriert die Texturen.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.greyIcon = par1IconRegister.registerIcon( "realrobots:scratch_grey");
		this.blockIcon = par1IconRegister.registerIcon( "realrobots:scratch");
		this.northIcon = par1IconRegister.registerIcon( "realrobots:scratch_north");
		
	}

	/**
	 * Die Textur auf der Nordseite ist anders...
	 */
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess iba, int xCoord, int yCoord, int zCoord, int side)
	{
		TileEntityScratch tile = (TileEntityScratch) iba.getTileEntity(xCoord, yCoord, zCoord);
		if (tile.getPlayerName() == "" )
		{
			return this.greyIcon;	
		}	
		if(side == 2)
		{
			return this.northIcon;
		} else
		{
			return this.blockIcon;
		}
	}

	/**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side
     */
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	return this.isProvidingWeakPower( par1IBlockAccess,  par2,  par3,  par4,  par5);
    }
	
    
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side
     */
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	TileEntityScratch tile = (TileEntityScratch) par1IBlockAccess.getTileEntity(par2, par3, par4);
    	return tile.getInputStatus() ?  15 :  0;
    }

    
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    
	/**
	 * Wird ausgeführt, wenn ein Rechtsklick auf den Block erfolgt. Ist output HIGH, erscheint der Block rot. Ist output true, erscheint er grün.
	 */
	@Override
	public boolean onBlockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityScratch tile = (TileEntityScratch) world.getTileEntity(xCoord, yCoord, zCoord);
		tile.setPlayerName(par5EntityPlayer.getDisplayName());
		
		world.markBlockForUpdate(xCoord,yCoord,zCoord);
		return true;
	}

	/**
	 * Wenn ein Redstone-Signal ankommt, wird anhand der Blockseite und der Gewichtung 
	 * der passende Sensorwert an Scratch geschickt.
	 */
	@Override
	public void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, Block neighborBlock) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(xCoord, yCoord, zCoord);
			if (tile != null && tile instanceof TileEntityScratch) {

				TileEntityScratch tes = (TileEntityScratch) tile;
				if ((world.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord) && (tes.getRedstoneStatus()=="0"))
						|| (!world.isBlockIndirectlyGettingPowered(xCoord,
								yCoord, zCoord) && tes.getRedstoneStatus()=="1")) {

					tes.changeRedstoneStatus();
					if (RealRobots.scratchEnabled) {
						EntityPlayer player = world.getPlayerEntityByName(tes.getPlayerName());
						if (player != null)	{
							 
							RealRobots.network.sendTo(new ScratchRRMessage("RedstoneNorth",Integer.toString(this.getInputStrength(world, xCoord, yCoord, zCoord, 2))), (EntityPlayerMP) player);
							RealRobots.network.sendTo(new ScratchRRMessage("RedstoneEast",Integer.toString(this.getInputStrength(world, xCoord, yCoord, zCoord, 3))), (EntityPlayerMP) player);
							RealRobots.network.sendTo(new ScratchRRMessage("RedstoneSouth",Integer.toString(this.getInputStrength(world, xCoord, yCoord, zCoord, 0))), (EntityPlayerMP) player);
							RealRobots.network.sendTo(new ScratchRRMessage("RedstoneWest",Integer.toString(this.getInputStrength(world, xCoord, yCoord, zCoord, 1))), (EntityPlayerMP) player);
						}				
					}
			}
		}
		}
		world.markBlockForUpdate(xCoord, yCoord, zCoord);
}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityScratch();
	}
	
	protected int getInputStrength(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_, int p_149903_5_)
    {
        int i1 = p_149903_5_;
        int j1 = p_149903_2_ + Direction.offsetX[i1];
        int k1 = p_149903_4_ + Direction.offsetZ[i1];
        int l1 = p_149903_1_.getIndirectPowerLevelTo(j1, p_149903_3_, k1, Direction.directionToFacing[i1]);
        return l1 >= 15 ? l1 : Math.max(l1, p_149903_1_.getBlock(j1, p_149903_3_, k1) == Blocks.redstone_wire ? p_149903_1_.getBlockMetadata(j1, p_149903_3_, k1) : 0);
    }
	
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
			par1World.scheduleBlockUpdate(par2, par3, par4, this,20);	
	}

	@Override
	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random par5Random)
	{
		
		TileEntity tile = world.getTileEntity(xCoord, yCoord, zCoord);
		if (tile != null && tile instanceof TileEntityScratch)
		{
			TileEntityScratch tes = (TileEntityScratch) tile;
			if (RealRobots.scratchEnabled) {
						EntityPlayer player = world.getPlayerEntityByName(tes.getPlayerName());
						if (player != null)	{
							
							if(world.isRaining()) {
								RealRobots.network.sendTo(new ScratchRRMessage("IsRaining","1"), (EntityPlayerMP) player);
							} else {
								RealRobots.network.sendTo(new ScratchRRMessage("IsRaining","0"), (EntityPlayerMP) player);
							}
							
							//
							
							if(player.lastTickPosX<player.posX) RealRobots.network.sendTo(new ScratchRRMessage("PlayerX","1"), (EntityPlayerMP) player);
							if(player.lastTickPosX==player.posX) RealRobots.network.sendTo(new ScratchRRMessage("PlayerX","0"), (EntityPlayerMP) player);
							if(player.lastTickPosX>player.posX) RealRobots.network.sendTo(new ScratchRRMessage("PlayerX","-1"), (EntityPlayerMP) player);
							
							if(player.lastTickPosY<player.posY) RealRobots.network.sendTo(new ScratchRRMessage("PlayerY","1"), (EntityPlayerMP) player);
							if(player.lastTickPosY==player.posY) RealRobots.network.sendTo(new ScratchRRMessage("PlayerY","0"), (EntityPlayerMP) player);
							if(player.lastTickPosY>player.posY) RealRobots.network.sendTo(new ScratchRRMessage("PlayerY","-1"), (EntityPlayerMP) player);
							
							if(player.lastTickPosZ<player.posZ) RealRobots.network.sendTo(new ScratchRRMessage("PlayerZ","1"), (EntityPlayerMP) player);
							if(player.lastTickPosZ==player.posZ) RealRobots.network.sendTo(new ScratchRRMessage("PlayerZ","0"), (EntityPlayerMP) player);
							if(player.lastTickPosZ>player.posZ) RealRobots.network.sendTo(new ScratchRRMessage("PlayerZ","-1"), (EntityPlayerMP) player);
							//
						}
						world.notifyBlocksOfNeighborChange(xCoord,yCoord,zCoord, this);
						world.markBlockForUpdate(xCoord,yCoord,zCoord);
						world.scheduleBlockUpdate(xCoord,yCoord,zCoord, this, 20);		
					}	
				
			}
	}

}
