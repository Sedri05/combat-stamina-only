package com.sedri;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class CombatStaminaOnly extends JavaPlugin {

    public CombatStaminaOnly(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getEntityStoreRegistry().registerSystem(new CombatStaminaSystem());
    }
}