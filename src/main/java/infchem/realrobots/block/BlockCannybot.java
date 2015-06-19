package infchem.realrobots.block;

import ibxm.Player;
import infchem.realrobots.RealRobots;
import infchem.realrobots.cannybot.CannybotMessage;
import infchem.realrobots.pi.PiMessage;
import infchem.realrobots.tileentity.TileEntityCannybot;
import infchem.realrobots.tileentity.TileEntityPiInput;
import infchem.realrobots.tileentity.TileEntityPiOutput;
import infchem.realrobots.wedo.WeDoVPLMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCannybot extends BlockContainer {

	private IIcon[] iconArray;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	private IIcon greyIcon;

	public BlockCannybot(Material material) {
		super(material);
		setBlockName("cannybot");
		this.setCreativeTab(CreativeTabs.tabRedstone);

	}

	/**
	 * Registriert die Texturen.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.iconArray = new IIcon[6];
		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = par1IconRegister.registerIcon("realrobots:cannybot"
					+ i);
		}

		this.topIcon = par1IconRegister.registerIcon("realrobots:cannybot");
		this.blockIcon = par1IconRegister.registerIcon("realrobots:cannybot");
		this.greyIcon = par1IconRegister.registerIcon("realrobots:cannybot_grey");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side) {
		TileEntityCannybot tile = (TileEntityCannybot) iba.getTileEntity(x,
				y, z);
		if (tile.getPlayerName() == "" )
		{
			return this.greyIcon;	
		}
		if (side == 1) {
			return topIcon;
		} else {

			
			int direction = tile.getDirection();		
				return iconArray[direction];
			
		}
	}

	
	@Override
	public void onNeighborBlockChange(World world, int xCoord, int yCoord,
			int zCoord, Block neighborBlock) {

		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(xCoord, yCoord, zCoord);
			if (tile != null && tile instanceof TileEntityCannybot) {

				TileEntityCannybot tec = (TileEntityCannybot) tile;

				if (world.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord))  {

					if (RealRobots.cannybotEnabled) {
						EntityPlayer player = world.getPlayerEntityByName(tec.playerName);
						if (player != null)	{
							RealRobots.network.sendTo(new CannybotMessage(tec.getDirection()), (EntityPlayerMP) player);
						}
						
				}

			}
		}
		}
		world.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * Wird ausgeführt, wenn ein Rechtsklick auf den Block erfolgt. 
	 * Damit wird die Fahrrichtung des Cannybots festgelegt.
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		TileEntityCannybot tile = (TileEntityCannybot) par1World.getTileEntity(par2, par3, par4);
		tile.playerName = par5EntityPlayer.getDisplayName();
		tile.setDirection();
		if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
		} else {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
		}
		par1World.markBlockForUpdate(par2, par3, par4);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCannybot();
	}
}
