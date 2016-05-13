package infchem.realrobots.slot;

import infchem.realrobots.item.ItemDistanceLarge;
import infchem.realrobots.item.ItemDistanceMedium;
import infchem.realrobots.item.ItemDistanceShort;
import infchem.realrobots.item.ItemTiltBack;
import infchem.realrobots.item.ItemTiltForward;
import infchem.realrobots.item.ItemTiltLeft;
import infchem.realrobots.item.ItemTiltNormal;
import infchem.realrobots.item.ItemTiltRight;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWeDoSensor extends Slot {

	public SlotWeDoSensor(IInventory inventory, int par2, int par3, int par4) {
		super(inventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isSensor = false;
		if (itemstack.getItem() instanceof ItemDistanceShort)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemDistanceMedium)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemDistanceLarge)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemTiltNormal)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemTiltLeft)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemTiltRight)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemTiltForward)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemTiltBack)
			isSensor = true;
		return isSensor;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
