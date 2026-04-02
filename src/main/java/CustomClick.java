import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.player.PlayerCustomClickEvent;

public class CustomClick {
    public static void handle(PlayerCustomClickEvent event) {
        switch (event.getKey().asMinimalString()) {
            case "weathermc:privacy_policy/accept":
                ConfigDialog.showGeneral(event.getPlayer());
                break;
            case "weathermc:privacy_policy/decline":
                event.getPlayer().kick(Component.text("In order to use this service, please accept our privacy policy!").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#EB7114")));
                break;
            default:
                break;
        }
    }
}
