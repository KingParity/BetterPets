package com.kingparity.betterpets.client.render.blockentity;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.kingparity.betterpets.block.entity.WaterCollectorTileEntity;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.BlockRenderView;

public class WaterCollectorRenderer extends BlockEntityRenderer<WaterCollectorTileEntity>
{
    public WaterCollectorRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
    
    @Override
    public void render(WaterCollectorTileEntity waterCollector, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        matrixStack.push();
        {
            Direction direction = waterCollector.getCachedState().get(HorizontalFacingBlock.FACING);
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getHorizontal() * -90F - 90F));
            matrixStack.translate(-0.5, -0.5, -0.5);
            //FluidTank tank = waterCollector.getTankWater();
            //float height = (float) ((tank.getCapacity() / 1000) * (((double)tank.getBuckets()) / ((double)tank.getCapacity())));
            float height = (float) ((waterCollector.fluidInv.getMaxAmount(0) / FluidVolume.BUCKET) * (((double)waterCollector.fluidInv.getInvFluid(0).getAmount()) / ((double)waterCollector.fluidInv.getMaxAmount(0))));
            System.out.println(waterCollector.fluidInv.getInvFluid(0).getAmount());
            if(height > 0)
            {
                drawFluid(waterCollector, matrixStack, vertexConsumers, 2.01F * 0.0625F, 8.01F * 0.0625F, 2.01F * 0.0625F, (12 - 0.02F) * 0.0625F, height * 0.0625F, (12 - 0.02F) * 0.0625F);
            }
        }
        matrixStack.pop();
    }
    
    private void drawFluid(WaterCollectorTileEntity waterCollector, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, float x, float y, float z, float width, float height, float depth)
    {
        //Fluid fluid = waterCollector.getTankWater().getFluid();
        Fluid fluid = waterCollector.fluidInv.getInvFluid(0).getRawFluid();
        if(fluid == Fluids.EMPTY)
        {
            return;
        }
        
        Sprite sprite = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(waterCollector.getWorld(), waterCollector.getPos(), fluid.getDefaultState())[0];
        int waterColor = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(waterCollector.getWorld(), waterCollector.getPos(), fluid.getDefaultState());
        
        float minU = sprite.getMinU();
        float maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
        float minV = sprite.getMinV();
        float maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
        float red = (float)(waterColor >> 16 & 255) / 255.0F;
        float green = (float)(waterColor >> 8 & 255) / 255.0F;
        float blue = (float)(waterColor & 255) / 255.0F;
        int light = getCombinedLight(waterCollector.getWorld(), waterCollector.getPos());
        
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        Matrix4f matrix = matrixStack.peek().getModel();
        
        //left side
        buffer.vertex(matrix, x + width, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //right side
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());
        
        //back side
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //front side
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());
        
        //top
        buffer.vertex(matrix, x, y + height, z).color(red, green, blue, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //bottom
        buffer.vertex(matrix, x + width, y, z + depth).color(red, green, blue, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z).color(red, green, blue, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red, green, blue, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z + depth).color(red, green, blue, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
    }
    
    public static int getCombinedLight(BlockRenderView lightReader, BlockPos pos)
    {
        int i = WorldRenderer.getLightmapCoordinates(lightReader, pos);
        int j = WorldRenderer.getLightmapCoordinates(lightReader, pos.up());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
}