package com.miningmark48.tieredmagnets;

import com.miningmark48.tieredmagnets.client.events.EventStitchParticles;
import com.miningmark48.tieredmagnets.init.ModBlocks;
import com.miningmark48.tieredmagnets.init.ModItems;
import com.miningmark48.tieredmagnets.init.ModRegistry;
import com.miningmark48.tieredmagnets.network.PacketHandler;
import com.miningmark48.tieredmagnets.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = Reference.MOD_ID)
public class TieredMagnets {

//    @Mod.Instance(Reference.MOD_ID)
    public static TieredMagnets instance;

//    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
//    public static CommonProxy proxy;

    public TieredMagnets() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    public void setup(FMLCommonSetupEvent event) {
//        proxy.preInit(event);

        ModBlocks.init();
        ModTileEntities.init();
        ModItems.init();
        ModRegistry.init();

        MinecraftForge.EVENT_BUS.register(new ModRegistry());
//        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
//        MinecraftForge.EVENT_BUS.register(proxy);

        MinecraftForge.EVENT_BUS.register(new EventStitchParticles());

        PacketHandler.registerMessages();

    }

//    @Mod.EventHandler
//    public void init(FMLInitializationEvent event){
//        proxy.init(event);
//        proxy.registerRenders();
//        ModLogger.info(ModTranslate.toLocal("log.info.init"));
//    }
//
//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent event){
//        ModLogger.info(ModTranslate.toLocal("log.info.postinit"));
//    }

}
