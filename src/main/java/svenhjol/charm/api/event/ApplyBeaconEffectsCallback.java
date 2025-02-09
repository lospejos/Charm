package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

/**
 * @version 1.0.1
 */
public interface ApplyBeaconEffectsCallback {
    Event<ApplyBeaconEffectsCallback> EVENT = EventFactory.createArrayBacked(ApplyBeaconEffectsCallback.class, (listeners) -> (world, level, pos, primary, secondary) -> {
        for (ApplyBeaconEffectsCallback listener : listeners) {
            listener.interact(world, level, pos, primary, secondary);
        }
    });

    void interact(Level world, BlockPos pos, int level, MobEffect primary, MobEffect secondary);
}
