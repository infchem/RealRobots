package infchem.realrobots.slot;

import infchem.realrobots.item.ItemInputPort0;
import infchem.realrobots.item.ItemInputPort1;
import infchem.realrobots.item.ItemInputPort2;
import infchem.realrobots.item.ItemInputPort3;
import infchem.realrobots.item.ItemInputPort4;
import infchem.realrobots.item.ItemInputPort5;
import infchem.realrobots.item.ItemInputPort6;
import infchem.realrobots.item.ItemInputPort7;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLeonardoSensor extends Slot {

	public SlotLeonardoSensor(IInventory inventory, int par2, int par3, int par4) {
		super(inventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isSensor = false;
		if (itemstack.getItem() instanceof ItemInputPort0)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort1)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort2)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort3)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort4)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort5)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort6)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemInputPort7)
			isSensor = true;
		return isSensor;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
