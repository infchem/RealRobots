package infchem.realrobots.block;

import infchem.realrobots.RealRobots;
import infchem.realrobots.tileentity.TileEntityLeonardo;
import infchem.realrobots.tileentity.TileEntityScratch;
import infchem.realrobots.tileentity.TileEntityWeDo;
import infchem.realrobots.wedo.WeDoVPLMessage;
import infchem.realrobots.leonardo.LeonardoVPLMessage;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class BlockLeonardo extends BlockContainer implements IPeripheralProvider {

	
	private IIcon greyIcon;

	public BlockLeonardo(Material material) {
		super(material);
		setBlockName("Leonardo");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister ir) {
		this.greyIcon = ir.registerIcon( "realrobots:leonardo_grey");
		this.blockIcon = ir.registerIcon( "realrobots:leonardo");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side)
	{
		TileEntityLeonardo tile = (TileEntityLeonardo) iba.getTileEntity(x, y, z);
		if (tile.getPlayerName().equals("")) 
		{
			return this.greyIcon;	
		}	else
		{		
			return this.blockIcon;
		}
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLeonardo();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/**
	 * Opens the GUI when the block is rightclicked. Binds the player name to the TileEntity 
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		
			TileEntityLeonardo tile = (TileEntityLeonardo) par1World.getTileEntity(par2, par3, par4);
			if(tile != null)
			{
				tile.setPlayerName(par5EntityPlayer.getDisplayName());
			}
				if (!par1World.isRemote)
				{
				par5EntityPlayer.openGui(RealRobots.instance, 0, par1World, par2, par3, par4);
				
				
		}
		par1World.markBlockForUpdate(par2,par3,par4);
		return true;
	}


	@Override
	public void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, Block neighborBlock) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(xCoord,yCoord,zCoord);
			if (tile!= null && tile instanceof TileEntityLeonardo)
			{
				TileEntityLeonardo tel = (TileEntityLeonardo) tile;
			//	System.out.println(this.isGettingInput(world, xCoord, yCoord, zCoord, 1) +" "+ !tel.getRedstoneStatus());
			//	 if (!this.isGettingInput(world, xCoord, yCoord, zCoord, 1) && !tel.getRedstoneStatus() ) {
				 if (world.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) ) {
					 EntityPlayer player = world.getPlayerEntityByName(tel.playerName);
					if (player != null)	{
						RealRobots.network.sendTo(new LeonardoVPLMessage(tel.getCommands(),tel.playerName,xCoord,yCoord,zCoord), (EntityPlayerMP) player);
					}		 
				}
			}
		}
		world.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof TileEntityLeonardo)
		{
			return (IPeripheral)entity;
		}
		return null;
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
    	TileEntityLeonardo tile = (TileEntityLeonardo) par1IBlockAccess.getTileEntity(par2, par3, par4);
    	return tile.getRedstoneStatus() ?  15 :  0;
    }

    
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }
    
	protected boolean isGettingInput(World p_149900_1_, int p_149900_2_, int p_149900_3_, int p_149900_4_, int p_149900_5_)
    {
        return this.getInputStrength(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_) > 0;
    }

	protected int getInputStrength(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_, int p_149903_5_)
    {
        int i1 = p_149903_5_;
        int j1 = p_149903_2_ + Direction.offsetX[i1];
        int k1 = p_149903_4_ + Direction.offsetZ[i1];
        int l1 = p_149903_1_.getIndirectPowerLevelTo(j1, p_149903_3_, k1, Direction.directionToFacing[i1]);
        return l1 >= 15 ? l1 : Math.max(l1, p_149903_1_.getBlock(j1, p_149903_3_, k1) == Blocks.redstone_wire ? p_149903_1_.getBlockMetadata(j1, p_149903_3_, k1) : 0);
    }

}
