package app.myhtl.weathermc.handlers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static app.myhtl.weathermc.Server.jsonObject;

public class AsyncPlayerConfig {
    public static void handle(AsyncPlayerConfigurationEvent event, InstanceContainer container) {
        final Player player = event.getPlayer();
        InetAddress ipAddress = ((InetSocketAddress) player.getPlayerConnection().getRemoteAddress()).getAddress();
        jsonObject.getAsJsonObject(player.getUuid().toString()).addProperty("ipAddress", ipAddress.getHostAddress());
        event.setSpawningInstance(container);
        player.setRespawnPoint(new Pos(0, 42, 0));
    }
}
