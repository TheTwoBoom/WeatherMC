package app.myhtl.weathermc;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.UUID;

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
                System.out.println(locJson);
                String cityName = locJson.getAsJsonPrimitive("city").getAsString();
                String countryCode = locJson.getAsJsonPrimitive("country").getAsString();
                jsonObject.getAsJsonObject(player.getUuid().toString()).addProperty("cityName", cityName);
                jsonObject.getAsJsonObject(player.getUuid().toString()).addProperty("countryCode", countryCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JsonObject getWeatherJson(UUID uuid) throws IOException {
        var playerJson = jsonObject.getAsJsonObject(uuid.toString());
        if (!playerJson.has("cityName") | !playerJson.has("countryCode")) { throw new IOException("Player Location not found"); }
        var countryCode = playerJson.getAsJsonPrimitive("countryCode").getAsString();
        var cityName = playerJson.getAsJsonPrimitive("cityName").getAsString();
        return JsonParser.parseString(getFromAPI(
                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&APPID=%s",
                        cityName,
                        countryCode,
                        Server.openWeatherKey
                ))).getAsJsonObject();
    }
    public static JsonObject getWeatherForcastJson(UUID uuid, int time) throws IOException {
        var playerJson = jsonObject.getAsJsonObject(uuid.toString());
        if (!playerJson.has("cityName") | !playerJson.has("countryCode")) { throw new IOException("Player Location not found"); }
        var countryCode = playerJson.getAsJsonPrimitive("countryCode").getAsString();
        var cityName = playerJson.getAsJsonPrimitive("cityName").getAsString();
        var weatherJson = JsonParser.parseString(getFromAPI(
                String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s,%s&APPID=%s",
                        cityName,
                        countryCode,
                        Server.openWeatherKey
                ))).getAsJsonObject().getAsJsonArray("list").asList();
        return weatherJson.get((time/3)-1).getAsJsonObject();
    }

    public static String parseWeatherJson(JsonObject weatherJson) {
        var temperatureCelcius = Math.round(weatherJson.getAsJsonObject("main").getAsJsonPrimitive("temp").getAsDouble() - 273.15);
        var weatherEmoji = switch (weatherJson.getAsJsonArray("weather").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt()) {
            case 800, 801 -> "☀";
            case 802, 803 -> "⛅";
            case 804 -> "☁";
            case 200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> "⚡";
            case 500, 501, 502, 503, 504, 520, 521, 522, 531 -> "💧";
            case 300, 301, 302, 310, 311, 312, 313, 314, 321 -> "💧";
            case 511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> "❄️";
            case 701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> "☄";
            default -> "⚙";
        };
        System.out.println(weatherJson.getAsJsonArray("weather").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt());
        return String.format(" %s℃ %s", temperatureCelcius, weatherEmoji);
    }

    public static TextComponent getWeather(UUID uuid) throws IOException {
        return Component
                .text("NOW:" + parseWeatherJson(getWeatherJson(uuid)))
                .append(Component.text(" - "))
                .append(Component.text("12h:" + parseWeatherJson(getWeatherForcastJson(uuid, 12))))
                .append(Component.text(" - "))
                .append(Component.text("3d:" + parseWeatherJson(getWeatherForcastJson(uuid, 72))))
                .append(Component.text(" - "))
                .append(Component.text("5d:" + parseWeatherJson(getWeatherForcastJson(uuid, 120))))
                .color(NamedTextColor.WHITE);
    }
}
