package com.kingparity.betterpets;

import com.kingparity.betterpets.init.ModBlocks;
import com.kingparity.betterpets.init.ModItems;
import com.kingparity.betterpets.init.ModTileEntities;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.Proxy;
import com.kingparity.betterpets.proxy.ServerProxy;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.ID)
public class BetterPets
{
    public static final Proxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogManager.getLogger(Reference.ID);
    public static final ItemGroup TAB = new ItemGroup("tabBetterPets")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.APPLE);
        }
    };
    
    public BetterPets()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModTileEntities.TILE_ENTITY_TYPES.register(bus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }
    
    private void onCommonSetup(FMLCommonSetupEvent event)
    {
    
    }
    
    private void onClientSetup(FMLClientSetupEvent event)
    {
        PROXY.setupClient();
    }
}