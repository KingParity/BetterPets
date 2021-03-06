package com.kingparity.betterpets;

import com.kingparity.betterpets.core.*;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.CommonProxy;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.ID)
public class BetterPetMod
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.ID);
    public static final ItemGroup GROUP = new BetterPetGroup(Reference.ID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public BetterPetMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModTileEntities.TILE_ENTITY_TYPES.register(eventBus);
        ModContainers.CONTAINER_TYPES.register(eventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);
        ModFluids.FLUIDS.register(eventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }
    
    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PROXY.onSetupCommon();
        LOGGER.debug("onCommonSetup method registered");

        ModLootFunctions.register();
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        PROXY.onSetupClient();
        LOGGER.debug("onClientSetup method registered");
    }
}