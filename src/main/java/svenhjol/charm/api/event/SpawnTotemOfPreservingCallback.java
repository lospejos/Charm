package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/**
 * @version 1.0.0
 */
public interface SpawnTotemOfPreservingCallback {
    Event<SpawnTotemOfPreservingCallback> EVENT = EventFactory.createArrayBacked(SpawnTotemOfPreservingCallback.class, (listeners) -> (player, level, pos) -> {
        for (SpawnTotemOfPreservingCallback listener : listeners) {
            listener.interact(player, level, pos);
        }
    });

    void interact(ServerPlayer player, Level world, BlockPos pos);
}
