package app.myhtl.weathermc.handlers;

import app.myhtl.weathermc.ConfigDialog;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.dialog.*;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

import static app.myhtl.weathermc.Server.jsonObject;

public class ShowPrivacyPolicy {
    static DialogAction accept = new DialogAction.Custom(Key.key("weathermc", "privacy_policy/accept"), null);
    static DialogAction decline = new DialogAction.Custom(Key.key("weathermc", "privacy_policy/decline"), null);
    public static Dialog dialog = new Dialog.Confirmation(
            new DialogMetadata(
                    Component.text("Privacy policy & Other stuff"),
                    null,
                    false,
                    false,
                    DialogAfterAction.CLOSE,
                    List.of(new DialogBody.PlainMessage(Component.text("By using WeatherMC, you agree that we do the following with your data:")
                            .appendNewline()
                            .appendNewline()
                            .append(Component.text(" • Send IP Address to IPInfo.io"))
                            .appendNewline()
                            .append(Component.text(" • Send the resulting geo data to OpenWeatherMap"))
                            .appendNewline()
                            .append(Component.text(" • Cache the results locally")),
                            500)),
                    new ArrayList<>()
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
    public static void handle(PlayerSpawnEvent event) {
        if (jsonObject.has(event.getPlayer().getUuid().toString())) {
            ConfigDialog.showGeneral(event.getPlayer());
        } else {
            event.getPlayer().showDialog(dialog);
        }
    }
}
