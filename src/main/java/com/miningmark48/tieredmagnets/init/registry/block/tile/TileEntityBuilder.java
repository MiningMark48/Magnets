package com.miningmark48.tieredmagnets.init.registry.block.tile;

import com.miningmark48.tieredmagnets.init.registry.RegistryObjectBuilder;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class TileEntityBuilder<T extends TileEntity> extends RegistryObjectBuilder<TileEntityType<?>, TileEntityTypeBuilder<T>> {

    private Class<T> tileClass;
    private Supplier<Supplier<TileEntityRenderer<? super T>>> renderer;

    public TileEntityBuilder(String registryName) {
        super(registryName);
    }

    public TileEntityBuilder(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public TileEntityBuilder<T> factory(Function<TileEntityTypeBuilder<T>, TileEntityType<?>> factory) {
        return (TileEntityBuilder<T>) super.factory(factory);
    }

    @Override
    public TileEntityBuilder<T> builder(TileEntityTypeBuilder<T> builder) {
        return (TileEntityBuilder<T>) super.builder(builder);
    }

    public TileEntityBuilder<T> renderer(Class<T> clazz, Supplier<Supplier<TileEntityRenderer<? super T>>> renderer) {
        this.tileClass = Objects.requireNonNull(clazz);
        this.renderer = Objects.requireNonNull(renderer);
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return super.getRegistryName();
    }

    public TileEntityBuilder<T> addValidBlock(ResourceLocation... blocks) {
        getBuilder().addBlocks(blocks);
        return this;
    }

    private Class<T> getTileClass() {
        return tileClass;
    }

    @Nullable
    private TileEntityRenderer<? super T> getRenderer() {
        return renderer != null ? renderer.get().get() : null;
    }

    boolean hasRenderer() {
        return getRenderer() != null;
    }

    void registerRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(getTileClass(), getRenderer());
    }

}
