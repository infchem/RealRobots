package infchem.realrobots.block;

import infchem.realrobots.tileentity.TileEntityPiInput;
import infchem.realrobots.tileentity.TileEntityPiOutput;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPiInput extends BlockContainer
{

	private IIcon[] iconArray;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	private IIcon greyIcon;


	public BlockPiInput(Material material) 
	{
		super(material);
		setBlockName("PiInput");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}


	/**
	 * Registriert die Texturen.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconArray = new IIcon[16];
		for (int i = 0; i < this.iconArray.length; ++i)
		{
			this.iconArray[i] = par1IconRegister.registerIcon("realrobots:pi" + i);
		}
		
		this.topIcon = par1IconRegister.registerIcon("realrobots:piinput");
		this.blockIcon = par1IconRegister.registerIcon( "realrobots:piinput");
		this.greyIcon = par1IconRegister.registerIcon( "realrobots:piinput_grey");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side)
	{
		TileEntityPiInput tile = (TileEntityPiInput) iba.getTileEntity(x,
				y, z);
		if (tile.getPlayerName() == "" )
		{
			return this.greyIcon;	
		}
		if(side == 1) {
			return topIcon;
			} else {
			
		
		int inputPort = tile.getInputPort();
		if ( tile.getInputStatus()) {
			return iconArray[inputPort]; 
		} else {
			return iconArray[inputPort+8];
		}
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
    	TileEntityPiInput tile = (TileEntityPiInput) par1IBlockAccess.getTileEntity(par2, par3, par4);
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
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityPiInput tile = (TileEntityPiInput) par1World.getTileEntity(par2, par3, par4);
		tile.playerName = par5EntityPlayer.getDisplayName();
		tile.setInputPort();
		par1World.markBlockForUpdate(par2, par3, par4);
		return true;
	}

	
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
			par1World.scheduleBlockUpdate(par2, par3, par4, this,20);	
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this);
		par1World.markBlockForUpdate(par2, par3, par4);
		par1World.scheduleBlockUpdate(par2, par3, par4, this, 20);
	}
	
	

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityPiInput();
	}
}
