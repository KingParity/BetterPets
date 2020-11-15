package com.kingparity.betterpets.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    private static final Random RANDOM = new Random();

    public static void writeInventoryToNBT(CompoundNBT compound, String tagName, IInventory inventory)
    {
        ListNBT tagList = new ListNBT();
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                CompoundNBT stackTag = new CompoundNBT();
                stackTag.putByte("Slot", (byte) i);
                stack.write(stackTag);
                tagList.add(stackTag);
            }
        }
        compound.put(tagName, tagList);
    }

    public static <T extends IInventory> T readInventoryToNBT(CompoundNBT compound, String tagName, T t)
    {
        if(compound.contains(tagName, Constants.NBT.TAG_LIST))
        {
            ListNBT tagList = compound.getList(tagName, Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < tagList.size(); i++)
            {
                CompoundNBT tagCompound = tagList.getCompound(i);
                byte slot = tagCompound.getByte("Slot");
                if(slot >= 0 && slot < t.getSizeInventory())
                {
                    t.setInventorySlotContents(slot, ItemStack.read(tagCompound));
                }
            }
        }
        return t;
    }

    public static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory)
    {
        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if(!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float offsetX = -0.25F + RANDOM.nextFloat() * 0.5F;
        float offsetY = RANDOM.nextFloat() * 0.8F;
        float offsetZ = -0.25F + RANDOM.nextFloat() * 0.5F;

        while(!stack.isEmpty())
        {
            ItemEntity entity = new ItemEntity(worldIn, x + offsetX, y + offsetY, z + offsetZ, stack.split(RANDOM.nextInt(21) + 10));
            entity.setMotion(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.05D + 0.2D, RANDOM.nextGaussian() * 0.05D);
            entity.setDefaultPickupDelay();
            worldIn.addEntity(entity);
        }
    }

    public static int getItemAmount(PlayerEntity player, Item item)
    {
        int amount = 0;
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() == item)
            {
                amount += stack.getCount();
            }
        }
        return amount;
    }

    public static boolean hasItemAndAmount(PlayerEntity player, Item item, int amount)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(stack != null && stack.getItem() == item)
            {
                count += stack.getCount();
            }
        }
        return amount <= count;
    }

    public static boolean removeItemWithAmount(PlayerEntity player, Item item, int amount)
    {
        if(hasItemAndAmount(player, item, amount))
        {
            for(int i = 0; i < player.inventory.getSizeInventory(); i++)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(!stack.isEmpty() && stack.getItem() == item)
                {
                    if(amount - stack.getCount() < 0)
                    {
                        stack.shrink(amount);
                        return true;
                    }
                    else
                    {
                        amount -= stack.getCount();
                        player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                        if(amount == 0) return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getItemStackAmount(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean hasItemStack(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return find.getCount() <= count;
    }

    public static boolean removeItemStack(PlayerEntity player, ItemStack find)
    {
        int amount = find.getCount();
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                if(amount - stack.getCount() < 0)
                {
                    stack.shrink(amount);
                    return true;
                }
                else
                {
                    amount -= stack.getCount();
                    player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                    if(amount == 0) return true;
                }
            }
        }
        return false;
    }

    public static boolean areItemStacksEqualIgnoreCount(ItemStack source, ItemStack target)
    {
        if(source.getItem() != target.getItem())
        {
            return false;
        }
        else if(source.getTag() == null && target.getTag() != null)
        {
            return false;
        }
        else
        {
            return (source.getTag() == null || source.getTag().equals(target.getTag())) && source.areCapsCompatible(target);
        }
    }
}