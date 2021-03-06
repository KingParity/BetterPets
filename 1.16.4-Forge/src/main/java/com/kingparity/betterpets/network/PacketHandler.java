package com.kingparity.betterpets.network;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "1";
    
    public static SimpleChannel instance;
    private static int nextId = 0;
    
    public static void register()
    {
        instance = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(BetterPets.ID, "play"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();
    
        register(MessageAttachChest.class, new MessageAttachChest());
        register(MessageRemoveChest.class, new MessageRemoveChest());
        register(MessageOpenPetChest.class, new MessageOpenPetChest());
        register(MessageMovementSpeed.class, new MessageMovementSpeed());
        register(MessageUpdateFoodStats.class, new MessageUpdateFoodStats());
        register(MessageUpdateThirstStats.class, new MessageUpdateThirstStats());
    }
    
    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
        instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}