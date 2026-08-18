package app.myhtl.weathermc.commands;

import app.myhtl.weathermc.DataSources;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class WeatherCommand extends Command {
    public WeatherCommand() {
        super("weather");
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                try {
                    sender.sendMessage(DataSources.getWeather(player.getUuid()));
                } catch (Exception e) {
                    sender.sendMessage("Weather could not be fetched: " + e);
                }
            } else {
                System.out.println("Please execute this command as a player");
            }
        });
    }
}
