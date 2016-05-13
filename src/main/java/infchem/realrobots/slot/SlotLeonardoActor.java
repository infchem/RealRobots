package infchem.realrobots.slot;

import infchem.realrobots.item.ItemDontSendRedstone;
import infchem.realrobots.item.ItemOutputPortHigh0;
import infchem.realrobots.item.ItemOutputPortHigh1;
import infchem.realrobots.item.ItemOutputPortHigh2;
import infchem.realrobots.item.ItemOutputPortHigh3;
import infchem.realrobots.item.ItemOutputPortHigh4;
import infchem.realrobots.item.ItemOutputPortHigh5;
import infchem.realrobots.item.ItemOutputPortHigh6;
import infchem.realrobots.item.ItemOutputPortHigh7;
import infchem.realrobots.item.ItemOutputPortLow0;
import infchem.realrobots.item.ItemOutputPortLow1;
import infchem.realrobots.item.ItemOutputPortLow2;
import infchem.realrobots.item.ItemOutputPortLow3;
import infchem.realrobots.item.ItemOutputPortLow4;
import infchem.realrobots.item.ItemOutputPortLow5;
import infchem.realrobots.item.ItemOutputPortLow6;
import infchem.realrobots.item.ItemOutputPortLow7;
import infchem.realrobots.item.ItemSendRedstone;
import infchem.realrobots.item.ItemSleep;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLeonardoActor extends Slot {

	public SlotLeonardoActor(IInventory inventory, int par2, int par3, int par4) {
		super(inventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isSensor = false;
		if (itemstack.getItem() instanceof ItemOutputPortHigh0)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh1)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh2)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh3)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh4)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh5)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh6)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortHigh7)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow0)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow1)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow2)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow3)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow4)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow5)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow6)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemOutputPortLow7)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemSleep)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemSendRedstone)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemDontSendRedstone)
			isSensor = true;
		return isSensor;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
