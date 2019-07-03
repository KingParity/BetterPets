package com.kingparity.betterpets.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public abstract class PetHorizontalWaterloggedBlock extends PetWaterloggedBlock
{
    public static final DirectionProperty DIRECTION = HorizontalBlock.HORIZONTAL_FACING;
    
    public PetHorizontalWaterloggedBlock(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(DIRECTION, context.getPlacementHorizontalFacing());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.toRotation(state.get(DIRECTION)));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }
}
