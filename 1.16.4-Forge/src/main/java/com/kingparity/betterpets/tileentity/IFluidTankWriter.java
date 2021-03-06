package com.kingparity.betterpets.tileentity;

import net.minecraft.nbt.CompoundNBT;

/**
 * Author: MrCrayfish
 */
public interface IFluidTankWriter
{
    void writeTanks(CompoundNBT compound);

    boolean areTanksEmpty();
}