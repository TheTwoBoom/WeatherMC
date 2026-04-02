import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.dialog.*;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class ShowPrivacyPolicy {
    public static void handle(PlayerSpawnEvent event) {
        DialogAction accept = new DialogAction.Custom(Key.key("weathermc", "privacy_policy/accept"), null);
        DialogAction decline = new DialogAction.Custom(Key.key("weathermc", "privacy_policy/decline"), null);
        Dialog dialog = new Dialog.Confirmation(
                new DialogMetadata(
                        Component.text("Privacy policy & Other stuff"),
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(new DialogBody.PlainMessage(Component.text("By using WeatherMC, you agree that we use & collect the following data:")
                                .appendNewline()
                                .appendNewline()
                                .append(Component.text(" • Minecraft Username/UUID"))
                                .appendNewline()
                                .append(Component.text(" • IP Address"))
                                .appendNewline()
                                .append(Component.text(" • Location Data")),
                                500)),
                        new ArrayList<DialogInput>()
                ),
                new DialogActionButton(
                        Component.text("Accept & Continue"),
                         null,
                        150,
                        accept
                ),
                new DialogActionButton(
                        Component.text("Decline"),
                        null,
                        150,
                        decline
                )
        );
        event.getPlayer().showDialog(dialog);
    }
}
