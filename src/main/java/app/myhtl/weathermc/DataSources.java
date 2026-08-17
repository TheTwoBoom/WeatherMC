package app.myhtl.weathermc;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import static app.myhtl.weathermc.Server.jsonObject;

public class DataSources {
    public static String getFromAPI(String urlToRead) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }
    public static void getLoc(Player player) {
        String loc;
        JsonObject locJson;
        try {
            InetAddress ipAddress = ((InetSocketAddress) player.getPlayerConnection().getRemoteAddress()).getAddress();
            if (!(ipAddress.isAnyLocalAddress() | ipAddress.isMulticastAddress() | ipAddress.isLoopbackAddress())) {
                loc = getFromAPI("https://ipinfo.io/" + ipAddress.getHostAddress() + "/json");
                locJson = new Gson().fromJson(loc, JsonObject.class);
                if (locJson.has("bogon")) { return; }
                String cityName = locJson.getAsJsonPrimitive("city").getAsString();
                String countryCode = locJson.getAsJsonPrimitive("country").getAsString();
                jsonObject.getAsJsonObject(player.getUuid().toString()).addProperty("cityName", cityName);
                jsonObject.getAsJsonObject(player.getUuid().toString()).addProperty("countryCode", countryCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getWeather(Player player) throws IOException {
        var playerJson = jsonObject.getAsJsonObject(player.getUuid().toString());
        if (!playerJson.has("cityName") | !playerJson.has("countryCode")) { throw new IOException("Player Location not found"); }
        var countryCode = playerJson.getAsJsonPrimitive("countryCode").getAsString();
        var cityName = playerJson.getAsJsonPrimitive("cityName").getAsString();
        return getFromAPI(
                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&APPID=%s",
                        cityName,
                        countryCode,
                        Server.openWeatherKey
                ));
    }
    public static TextComponent parseWeatherJson(Player player) throws IOException {
        JsonArray weatherJson = JsonParser.parseString(getWeather(player)).getAsJsonObject().getAsJsonArray("weather");
        var weatherEmoji = switch (weatherJson.get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt()) {
            case 804 -> "☁";
            case 501 -> "\uD83C\uDF27️";
            default -> "?";
        };
        return Component.text(weatherEmoji + "");
    }
}
