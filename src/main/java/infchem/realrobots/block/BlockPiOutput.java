package infchem.realrobots.block;

import ibxm.Player;
import infchem.realrobots.RealRobots;
import infchem.realrobots.config.ConfigHandler;
import infchem.realrobots.pi.PiMessage;
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

public class BlockPiOutput extends BlockContainer {

	private IIcon[] iconArray;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	private IIcon greyIcon;

	public BlockPiOutput(Material material) {
		super(material);
		setBlockName("PiOutput");
		this.setCreativeTab(CreativeTabs.tabRedstone);

	}

	/**
	 * Registriert die Texturen.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.iconArray = new IIcon[16];
		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = par1IconRegister.registerIcon("realrobots:pi"
					+ i);
		}

		this.topIcon = par1IconRegister.registerIcon("realrobots:pioutput");
		this.blockIcon = par1IconRegister.registerIcon("realrobots:pioutput");
		this.greyIcon = par1IconRegister.registerIcon("realrobots:pioutput_grey");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side) {
		TileEntityPiOutput tile = (TileEntityPiOutput) iba.getTileEntity(x,
				y, z);
		if (tile.getPlayerName() == "" )
		{
			return this.greyIcon;	
		}
		if (side == 1) {
			return topIcon;
		} else {

			
			int inputPort = tile.getOutputPort();
			if (tile.getOutputStatus()) {
				return iconArray[inputPort];
			} else {
				return iconArray[inputPort + 8];
			}
		}
	}

	/**
	 * Bug: Textur is not reloaded.
	 */
	@Override
	public void onNeighborBlockChange(World world, int xCoord, int yCoord,
			int zCoord, Block neighborBlock) {

		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(xCoord, yCoord, zCoord);
			if (tile != null && tile instanceof TileEntityPiOutput) {

				TileEntityPiOutput tepo = (TileEntityPiOutput) tile;

				if ((world.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord) && !(tepo.getOutputStatus()))
						|| (!world.isBlockIndirectlyGettingPowered(xCoord,
								yCoord, zCoord) && tepo.getOutputStatus())) {

					tepo.changeStatus();
					if (ConfigHandler.piEnabled) {
						EntityPlayer player = world.getPlayerEntityByName(tepo.playerName);
						if (player != null)	{
							RealRobots.network.sendTo(new PiMessage(tepo.getOutputPort(),tepo.getOutputStatus()), (EntityPlayerMP) player);
						}
						
				}

			}
		}
		}
		world.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * Wird ausgeführt, wenn ein Rechtsklick auf den Block erfolgt. Ist output
	 * false, erscheint der Block rot. Ist output true, erscheint er grün.
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		TileEntityPiOutput tile = (TileEntityPiOutput) par1World.getTileEntity(par2, par3, par4);
		tile.playerName = par5EntityPlayer.getDisplayName();
		tile.setOutputPort();
		
		

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
		return new TileEntityPiOutput();
	}
}
