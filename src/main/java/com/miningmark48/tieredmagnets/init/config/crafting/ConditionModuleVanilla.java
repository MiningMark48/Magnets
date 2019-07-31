package com.miningmark48.tieredmagnets.init.config.crafting;

import com.google.gson.JsonObject;
import com.miningmark48.tieredmagnets.init.config.ModConfig;
import net.minecraftforge.common.crafting.IConditionSerializer;

import java.util.function.BooleanSupplier;

public class ConditionModuleVanilla implements IConditionSerializer {

    @Override
    public BooleanSupplier parse(JsonObject json) {
        return ModConfig.MODULES.enableModuleVanilla::get;
    }
}
