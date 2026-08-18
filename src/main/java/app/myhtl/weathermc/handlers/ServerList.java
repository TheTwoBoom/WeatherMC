package app.myhtl.weathermc.handlers;

import app.myhtl.weathermc.DataSources;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;

import static app.myhtl.weathermc.Server.jsonObject;

public class ServerList {
    public static void handle(ServerListPingEvent event) {
        assert event.getConnection() != null;
        var playerVersion = event.getConnection().getProtocolVersion();
        var playerIP = ((InetSocketAddress) event.getConnection().getRemoteAddress()).getAddress().getHostAddress();
        final String[] uuid = new String[1];
        var weather = Component.text("   Join to configure your forecast   ").color(TextColor.fromHexString("#555555"));

        jsonObject.asMap().forEach((s, j) -> {
            try {
                var obj = j.getAsJsonObject();
                if (obj.has("ipAddress") && obj.get("ipAddress").isJsonPrimitive()) {
                    var ip = obj.getAsJsonPrimitive("ipAddress").getAsString();
                    if (Objects.equals(ip, playerIP)) {
                        uuid[0] = s;
                    }
                }
            } catch (Exception ignored) {
            }
        });
        if (uuid[0] != null) {
            try {
                weather = Component.text("   ").append(DataSources.getWeather(UUID.fromString(uuid[0])))
                        .append(Component.text("   ").color(TextColor.fromHexString("#555555")));
            } catch (IOException ignored) {}
        }

        var description = Component.text()
                .append(Component.text("  WeatherMC")
                        .color(TextColor.fromHexString("#55FF55"))
                        .decorate(TextDecoration.BOLD)
                        .append(Component.text(" - ").color(TextColor.fromHexString("#555555")))
                        .append(Component.text("Realtime Weather forecasts! ").color(TextColor.fromHexString("#FFFF55"))))
                .appendNewline()
                .append(weather)
                .build();
        var status = new Status(
                description,
                new byte[0],
                new Status.VersionInfo("WeatherMC", playerVersion),
                new Status.PlayerInfo(1, 404),
                false
        );
        event.setStatus(status);
    }
}
