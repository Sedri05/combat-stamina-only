package com.sedri;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.time.TimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;

public class CombatStaminaSystem extends EntityTickingSystem<EntityStore>
{
    private final ComponentType<EntityStore, PlayerRef> playerComponentType = PlayerRef.getComponentType();
    private final ComponentType<EntityStore, EntityStatMap> entityStatMapComponentType = EntityStatMap.getComponentType();
    private final ComponentType<EntityStore, DamageDataComponent> damageComponentType = DamageDataComponent.getComponentType();
    private static final ResourceType<EntityStore, TimeResource> TIME_RESOURCE_TYPE = TimeResource.getResourceType();
    private final Query<EntityStore> QUERY = Query.and(this.playerComponentType, this.entityStatMapComponentType, this.damageComponentType);

    public CombatStaminaSystem() {
        super();
    }

    @Override
    public void tick(float v, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        PlayerRef ref = archetypeChunk.getComponent(index, playerComponentType);
        if (ref == null) return;
        EntityStatMap stats = archetypeChunk.getComponent(index, entityStatMapComponentType);
        if (stats == null) return;
        DamageDataComponent damage = archetypeChunk.getComponent(index, damageComponentType);
        if (damage == null) return;
        Instant timestamp = store.getResource(TIME_RESOURCE_TYPE).getNow().minusSeconds(10);
        if (damage.getLastCombatAction().isAfter(timestamp) || damage.getLastDamageTime().isAfter(timestamp)) return;
        int staminaIndex = DefaultEntityStatTypes.getStamina();
        EntityStatValue stat = stats.get(staminaIndex);
        if (damage.getLastChargeTime() != null && damage.getLastChargeTime().isAfter(timestamp) && stat.get() <= stat.getMax() * 0.95) return;
        stats.maximizeStatValue(staminaIndex);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
