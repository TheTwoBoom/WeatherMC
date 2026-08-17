package app.myhtl.weathermc;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.dialog.*;
import net.minestom.server.entity.Player;

import java.util.List;

import static app.myhtl.weathermc.Server.jsonObject;

public class ConfigDialog {
    public static final Dialog mainMenuAdvanced = new Dialog.MultiAction(
            new DialogMetadata(
                    Component.text("WeatherMC Config"),
                    null,
                    false,
                    false,
                    DialogAfterAction.CLOSE,
                    List.of(new DialogBody.PlainMessage(Component.text("Note that changes are saved automatically"), 150)),
                    List.of()
            ),
            List.of(
                    new DialogActionButton(
                            Component.text("Location"),
                            null,
                            150,
                            new DialogAction.Custom(Key.key("weathermc", "menu/loc"), null)
                    ),
                    new DialogActionButton(
                            Component.text("Formatting"),
                            null,
                            150,
                            new DialogAction.Custom(Key.key("weathermc", "menu/loc"), null)
                    )
            ),
            new DialogActionButton(
                    Component.text("Exit"),
                    null,
                    150,
                    new DialogAction.Custom(Key.key("weathermc", "menu/general"), null)
            ),
            2
    );
    public static void showGeneral(Player player) {
        Dialog mainMenu = new Dialog.MultiAction(
                new DialogMetadata(
                        Component.text("WeatherMC Config"),
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(new DialogBody.PlainMessage(Component.text("Note that the automatic config may not be accurate and changes could be needed"), 265)),
                        List.of()
                ),
                List.of(
                        new DialogActionButton(
                                Component.text("Automatic Location Detection"),
                                null,
                                250,
                                new DialogAction.Custom(Key.key("weathermc", "automatic"), null)
                        ),
                        new DialogActionButton(
                                Component.text("Manual Mode"),
                                null,
                                250,
                                new DialogAction.Custom(Key.key("weathermc", "advanced"), null)
                        )
                ),
                new DialogActionButton(
                        Component.text("Save & Exit"),
                        null,
                        150,
                        new DialogAction.Custom(Key.key("weathermc", "disconnect"), null)
                ),
                1
        );
        player.showDialog(mainMenu);
    }
    public static void showAdvanced(Player player) {
        player.showDialog(mainMenuAdvanced);
    }
    public static void showAutoLoc(Player player) {
        var playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
        var locData = "[NO DATA AVAILABLE]";
        if (playerJson.has("cityName") && playerJson.has("countryCode")) {
            locData = playerJson.getAsJsonPrimitive("cityName").getAsString() + ", " + playerJson.getAsJsonPrimitive("countryCode").getAsString();
        }
        Dialog confirmLoc = new Dialog.Confirmation(
                new DialogMetadata(
                        Component.text("WeatherMC Config"),
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("Is this data correct?"), 265),
                                new DialogBody.PlainMessage(Component.text(locData), 265)
                        ),
                        List.of()
                ),
                new DialogActionButton(
                        Component.text("Yes"),
                        null,
                        150,
                        new DialogAction.Custom(Key.key("weathermc", "advanced"), null)
                ),
                new DialogActionButton(
                        Component.text("No"),
                        null,
                        150,
                        new DialogAction.Custom(Key.key("weathermc", "menu/loc"), null)
                )
        );
        player.showDialog(confirmLoc);
    }
    public static void showLocMenu(Player player) {
        var playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
        if (!playerJson.has("cityName")) { playerJson.addProperty("cityName", ""); }
        if (!playerJson.has("countryCode")) { playerJson.addProperty("countryCode", ""); }
        var countryCode = playerJson.getAsJsonPrimitive("countryCode").getAsString();
        var cityName = playerJson.getAsJsonPrimitive("cityName").getAsString();
        var locMenu = new Dialog.Notice(
                new DialogMetadata(
                        Component.text("WeatherMC Config -> Location"),
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(new DialogBody.PlainMessage(Component.text("Fields with ").append(Component.text("*", NamedTextColor.RED)).append(Component.text(" are mandatory")), 150)),
                        List.of(
                                new DialogInput.Text(
                                        "city_name",
                                        250,
                                        Component.text("City name").append(Component.text("*", NamedTextColor.RED)),
                                        true,
                                        cityName,
                                        250,
                                        null
                                ),
                                new DialogInput.Text(
                                        "country_code",
                                        250,
                                        Component.text("2-letter country code").append(Component.text("*", NamedTextColor.RED)),
                                        true,
                                        countryCode,
                                        250,
                                        null
                                )
                        )
                ),
                new DialogActionButton(
                        Component.text("Exit"),
                        null,
                        150,
                        new DialogAction.RunCommand("loc " + cityName + " " + countryCode)
                )
        );
        player.showDialog(locMenu);
    }
}
