package com.kingparity.betterpets.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public interface IPetContainer extends ISidedInventory
{
    PetInventory getInventory();
    
    @Override
    default int[] getSlotsForFace(Direction side)
    {
        int[] slots = new int[getInventory().getSizeInventory()];
        for(int i = 0; i < getInventory().getSizeInventory(); i++)
        {
            slots[i] = i;
        }
        return slots;
    }
    
    @Override
    default boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    default boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return true;
    }
    
    @Override
    default int getSizeInventory()
    {
        return getInventory().getSizeInventory();
    }
    
    @Override
    default boolean isEmpty()
    {
        return getInventory().isEmpty();
    }
    
    @Override
    default ItemStack getStackInSlot(int index)
    {
        return getInventory().getStackInSlot(index);
    }
    
    @Override
    default ItemStack decrStackSize(int index, int count)
    {
        return getInventory().decrStackSize(index, count);
    }
    
    @Override
    default ItemStack removeStackFromSlot(int index)
    {
        return getInventory().removeStackFromSlot(index);
    }
    
    @Override
    default void setInventorySlotContents(int index, ItemStack stack)
    {
        getInventory().setInventorySlotContents(index, stack);
    }
    
    @Override
    default int getInventoryStackLimit()
    {
        return getInventory().getInventoryStackLimit();
    }
    
    @Override
    default void markDirty()
    {
        getInventory().markDirty();
    }
    
    @Override
    default boolean isUsableByPlayer(PlayerEntity player)
    {
        return getInventory().isUsableByPlayer(player);
    }
    
    @Override
    default void openInventory(PlayerEntity player)
    {
        getInventory().openInventory(player);
    }
    
    @Override
    default void closeInventory(PlayerEntity player)
    {
        getInventory().openInventory(player);
    }
    
    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return getInventory().isItemValidForSlot(index, stack);
    }

    /*@Override
    default int getField(int id)
    {
        return getInventory().getField(id);
    }

    @Override
    default void setField(int id, int value)
    {
        getInventory().setField(id, value);
    }

    @Override
    default int getFieldCount()
    {
        return getInventory().getFieldCount();
    }*/
    
    @Override
    default void clear()
    {
        getInventory().clear();
    }
    
    default boolean isPetItem(ItemStack stack)
    {
        return true;
    }
}
