package app.myhtl.weathermc.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import static app.myhtl.weathermc.Server.jsonObject;

public class DataCollection {
    public static class LocationCommand extends Command {

        public LocationCommand() {
            super("loc");

            var cityName = ArgumentType.String("cityName");
            var countryCode = ArgumentType.String("countryCode");

            addSyntax((sender, context) -> {
                if (sender instanceof Player player) {
                    final String city = context.get(cityName);
                    final String country = context.get(countryCode);
                    var playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
                    playerJson.addProperty("cityName", city);
                    playerJson.addProperty("countryCode", country);
                }
            }, cityName, countryCode);
        }
    }
}
