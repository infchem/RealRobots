package infchem.realrobots.wedo;


import infchem.realrobots.slot.SlotWeDoActor;
import infchem.realrobots.slot.SlotWeDoSensor;
import infchem.realrobots.tileentity.TileEntityWeDo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerWeDo extends Container {

	private TileEntityWeDo tileEntity;
	private InventoryPlayer inventoryPlayer;

    public ContainerWeDo (InventoryPlayer _inventoryPlayer, TileEntityWeDo te){
            this.tileEntity = te;
            this.inventoryPlayer = _inventoryPlayer;

            //the Slot constructor takes the IInventory and the slot number in that it binds to
            //and the x-y coordinates it resides on-screen
            for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                            addSlotToContainer(new SlotWeDoActor(tileEntity, j + i * 3, 91 + j * 18, 17 + i * 18));
                    }
            }
            	
            // the conditional slot for starting the script
            addSlotToContainer(new SlotWeDoSensor(tileEntity, 9, 43, 17));
            
            //commonly used vanilla code that adds the player's inventory
            bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
            return tileEntity.isUseableByPlayer(player);
    }


    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
            for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 9; j++) {
                            addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                            8 + j * 18, 84 + i * 18));
                    }
            }

            for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
            }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    //merges the item into player inventory since its in the tileEntity
                    if (slot < 9) {
                            if (!this.mergeItemStack(stackInSlot, 0, 35, true)) {
                                    return null;
                            }
                    }
                    //places it into the tileEntity is possible since its in the player inventory
                    else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) {
                            return null;
                    }
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            return stack;
    }
    
    
  
}
