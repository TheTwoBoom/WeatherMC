import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.dialog.*;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;

import java.util.ArrayList;
import java.util.List;

public class AsyncPlayerConfig {
    public static void handle(AsyncPlayerConfigurationEvent event, InstanceContainer container) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(container);
        player.setRespawnPoint(new Pos(0, 42, 0));
    }
}
