package app.myhtl.weathermc;

import app.myhtl.weathermc.handlers.*;
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

public class Server {
    public static void main(String[] args) {
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
