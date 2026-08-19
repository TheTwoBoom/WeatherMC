package app.myhtl.weathermc.handlers;

import app.myhtl.weathermc.ConfigDialog;
import app.myhtl.weathermc.DataSources;
import com.google.gson.JsonObject;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;

import static app.myhtl.weathermc.Server.jsonObject;
import static app.myhtl.weathermc.Server.savePlayerData;

public class CustomClick {
    public static void handle(PlayerCustomClickEvent event) {
        Player player = event.getPlayer();
        InetAddress ipAddress = ((InetSocketAddress) player.getPlayerConnection().getRemoteAddress()).getAddress();
        var playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
        switch (event.getKey().asMinimalString()) {
            case "weathermc:privacy_policy/accept":
                jsonObject.add(player.getUuid().toString(), new JsonObject());
                ConfigDialog.showGeneral(player);
                break;
            case "weathermc:privacy_policy/decline":
                player.kick(Component.text("In order to use this service, please accept our privacy policy!").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#EB7114")));
                break;
            case "weathermc:apply_loc":
                assert event.getPayload() != null;
                var content = (CompoundBinaryTag) event.getPayload().asBinaryTag();

                playerJson.addProperty("cityName", ((StringBinaryTag) Objects.requireNonNull(content.get("city_name"))).value());
                playerJson.addProperty("countryCode", ((StringBinaryTag) Objects.requireNonNull(content.get("country_code"))).value());
                playerJson.addProperty("ipAddress", ipAddress.getHostAddress());
                savePlayerData();
                player.kick(Component.text("Saved all changes"));
                break;
            case "weathermc:menu/automatic":
                DataSources.getLoc(player);

                playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
                ipAddress = ((InetSocketAddress) player.getPlayerConnection().getRemoteAddress()).getAddress();
                playerJson.addProperty("ipAddress", ipAddress.getHostAddress());

                ConfigDialog.showAutoLoc(player);
                break;
            case "weathermc:menu/loc":
                ConfigDialog.showLocMenu(player);
                break;
            case "weathermc:menu/general":
                ConfigDialog.showGeneral(player);
                break;
            case "weathermc:disconnect":
                savePlayerData();
                player.kick(Component.text("Saved all changes"));
                break;
            default:
                player.kick(Component.text("Not implemented yet: " + event.getKey().asMinimalString()).appendNewline().append(Component.text("This could be a bug. idk")));
                break;
        }
    }
}
