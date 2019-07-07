package com.miningmark48.magnets.init;

import com.miningmark48.magnets.tileentity.TileEntityMagneticInsulator;
import com.miningmark48.magnets.tileentity.renderer.RendererMagneticInsulator;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTileEntities {

    public static void init() {
        GameRegistry.registerTileEntity(TileEntityMagneticInsulator.class, "magnetic_insulator");
    }

    @SideOnly(Side.CLIENT)
    public static void bindTileRenders() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticInsulator.class, new RendererMagneticInsulator());
    }

}
