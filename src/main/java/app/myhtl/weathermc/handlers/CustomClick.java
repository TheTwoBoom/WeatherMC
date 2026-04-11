package app.myhtl.weathermc.handlers;

import app.myhtl.weathermc.ConfigDialog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.minestom.server.tag.Tag;

public class CustomClick {
    public static void handle(PlayerCustomClickEvent event) {
        switch (event.getKey().asMinimalString()) {
            case "weathermc:privacy_policy/accept":
                event.getPlayer().setTag(Tag.Boolean("privacy_policy"), true);
                ConfigDialog.showGeneral(event.getPlayer());
                break;
            case "weathermc:privacy_policy/decline":
                event.getPlayer().kick(Component.text("In order to use this service, please accept our privacy policy!").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#EB7114")));
                break;
            case "weathermc:disconnect":
                event.getPlayer().kick(Component.text("All changes were saved!").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#55FF55")));
                break;
            case "weathermc:advanced":
                ConfigDialog.showAdvanced(event.getPlayer());
                break;
            case "weathermc:automatic":
                ConfigDialog.showAutoLoc(event.getPlayer());
                break;
            case "weathermc:menu/loc":
                ConfigDialog.showLocMenu(event.getPlayer());
                break;
            default:
                event.getPlayer().kick(Component.text("Not implemented yet: " + event.getKey().asMinimalString()).appendNewline().append(Component.text("This could be a bug. idk")));
                break;
        }
    }
}
