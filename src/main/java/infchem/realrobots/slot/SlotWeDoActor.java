package infchem.realrobots.slot;

import infchem.realrobots.item.ItemDontSendRedstone;
import infchem.realrobots.item.ItemLeftMotorClockwise;
import infchem.realrobots.item.ItemLeftMotorCounterclockwise;
import infchem.realrobots.item.ItemLeftMotorStop;
import infchem.realrobots.item.ItemRightMotorClockwise;
import infchem.realrobots.item.ItemRightMotorCounterclockwise;
import infchem.realrobots.item.ItemRightMotorStop;
import infchem.realrobots.item.ItemSendRedstone;
import infchem.realrobots.item.ItemSleep;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWeDoActor extends Slot {

	public SlotWeDoActor(IInventory inventory, int par2, int par3, int par4) {
		super(inventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isSensor = false;
		if (itemstack.getItem() instanceof ItemLeftMotorClockwise)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemLeftMotorCounterclockwise)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemRightMotorClockwise)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemRightMotorCounterclockwise)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemLeftMotorStop)
			isSensor = true;
		if (itemstack.getItem() instanceof ItemRightMotorStop)
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
