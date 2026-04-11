package app.myhtl.weathermc.handlers;

import net.minestom.server.event.player.PlayerMoveEvent;

public class CancelMove {
    public static void handle(PlayerMoveEvent event) {
        event.setCancelled(true);
    }
}
