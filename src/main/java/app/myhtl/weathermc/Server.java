package app.myhtl.weathermc;

import app.myhtl.weathermc.handlers.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.Difficulty;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class Server {
    public static JsonObject jsonObject;
    public static void loadPlayerData() {
        try (var in = new FileReader("playerData.json")) {
            jsonObject = new Gson().fromJson(in.readAllAsString(), JsonElement.class).getAsJsonObject();
        } catch (IOException e) {
            jsonObject = new JsonObject();
        }
    }
    public static void savePlayerData() {
        try (var out = new BufferedOutputStream(new FileOutputStream("playerData.json"))) {
            out.write(new Gson().toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
        } catch (IOException _) {}
    }
    public static Properties loadConfig() {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + "server.properties";

        Properties serverProps = new Properties();
        try {
            serverProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverProps;
    }
    public static void main(String[] args) {
        System.setProperty("minestom.tps", "5");
        loadPlayerData();
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Create the instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.BARRIER));

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(ServerListPingEvent.class, ServerList::handle);
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> AsyncPlayerConfig.handle(event, instanceContainer));
        globalEventHandler.addListener(PlayerMoveEvent.class, CancelMove::handle);
        globalEventHandler.addListener(PlayerSpawnEvent.class, ShowPrivacyPolicy::handle);
        globalEventHandler.addListener(PlayerCustomClickEvent.class, CustomClick::handle);

        // Start the server on port 25565
        MinecraftServer.setBrandName("WeatherMC");
        MinecraftServer.setDifficulty(Difficulty.PEACEFUL);
        MinecraftServer.setCompressionThreshold(64);
        minecraftServer.start("0.0.0.0", 25565);
    }
}
