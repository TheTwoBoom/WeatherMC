package app.myhtl.weathermc.handlers;

import app.myhtl.weathermc.ConfigDialog;
import app.myhtl.weathermc.DataSources;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;

import static app.myhtl.weathermc.Server.jsonObject;
import static app.myhtl.weathermc.Server.savePlayerData;

public class CustomClick {
    public static void handle(PlayerCustomClickEvent event) {
        Player player = event.getPlayer();
        switch (event.getKey().asMinimalString()) {
            case "weathermc:privacy_policy/accept":
                jsonObject.add(player.getUuid().toString(), new JsonObject());
                ConfigDialog.showGeneral(player);
                break;
            case "weathermc:privacy_policy/decline":
                player.kick(Component.text("In order to use this service, please accept our privacy policy!").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#EB7114")));
                break;
            case "weathermc:automatic":
                DataSources.getLoc(player);
                ConfigDialog.showAutoLoc(player);
                break;
            case "weathermc:menu/loc":
                ConfigDialog.showLocMenu(player);
                break;
            case "weathermc:menu/general":
                ConfigDialog.showGeneral(player);
                break;
            case "weathermc:advanced":
                ConfigDialog.showAdvanced(player);
                break;
            case "weathermc:disconnect":
                player.kick(Component.text("Saved all changes"));
                break;
            default:
                player.kick(Component.text("Not implemented yet: " + event.getKey().asMinimalString()).appendNewline().append(Component.text("This could be a bug. idk")));
                break;
        }
    }
}
