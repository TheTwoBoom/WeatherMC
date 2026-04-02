import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

public class ServerList {
    public static void handle(ServerListPingEvent event) {
        assert event.getConnection() != null;
        var playerVersion = event.getConnection().getProtocolVersion();

        var credits = Component.text("   Join to configure your forecast   ").color(TextColor.fromHexString("#555555"));
        var description = Component.text()
                .append(Component.text("  WeatherMC")
                        .color(TextColor.fromHexString("#55FF55"))
                        .decorate(TextDecoration.BOLD)
                        .append(Component.text(" - ").color(TextColor.fromHexString("#555555")))
                        .append(Component.text("Realtime Weather forecasts! ").color(TextColor.fromHexString("#FFFF55"))))
                .appendNewline()
                .append(credits)
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
