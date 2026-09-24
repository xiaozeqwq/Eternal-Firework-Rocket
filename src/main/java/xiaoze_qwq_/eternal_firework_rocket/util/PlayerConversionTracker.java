package xiaoze_qwq_.eternal_firework_rocket.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerConversionTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("EternalFireworkRocket/Conversion");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Flush dirty data at most once every 30 seconds (20 ticks * 30). */
    private static final int SAVE_INTERVAL_TICKS = 600;

    private static File dataFile;
    private static final Map<String, Boolean> conversionMap = new ConcurrentHashMap<>();
    private static boolean dirty = false;
    private static int ticksSinceSave = 0;

    private PlayerConversionTracker() {}

    /** Registers world-independent events. Call once during mod initialization. */
    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> registerPlayer(handler.getPlayer()));
    }

    /** Called when the server finished loading a world. */
    public static void init(MinecraftServer server) {
        Path basePath = server.getWorldPath(LevelResource.ROOT);
        Path modFolder = basePath.resolve("eternal_firework_rocket");
        try {
            Files.createDirectories(modFolder);
        } catch (IOException e) {
            LOGGER.error("Failed to create mod folder: " + modFolder, e);
        }
        dataFile = modFolder.resolve("conversion.json").toFile();

        dirty = false;
        ticksSinceSave = 0;
        load();
        if (!dataFile.exists()) {
            save();
        }

        LOGGER.info("Conversion tracker initialized, file: " + dataFile.getAbsolutePath());
    }

    /** Called when the server stopped, flushing pending changes and clearing state. */
    public static void shutdown() {
        if (dirty) {
            save();
        }
        dataFile = null;
        conversionMap.clear();
        dirty = false;
        ticksSinceSave = 0;
    }

    /** Flushes pending changes periodically instead of on every change. */
    public static void tick(MinecraftServer server) {
        if (!dirty) {
            return;
        }
        if (++ticksSinceSave >= SAVE_INTERVAL_TICKS) {
            save();
        }
    }

    /** Player login: remember new players without writing to disk on every join. */
    public static void registerPlayer(ServerPlayer player) {
        String key = getPlayerKey(player);
        if (conversionMap.putIfAbsent(key, false) == null) {
            dirty = true;
            LOGGER.debug("Registered player {} with conversion=false", key);
        }
    }

    private static void load() {
        if (dataFile == null || !dataFile.exists()) {
            conversionMap.clear();
            return;
        }
        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, Boolean>>() {}.getType();
            Map<String, Boolean> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                conversionMap.clear();
                conversionMap.putAll(loaded);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load conversion data", e);
        }
    }

    private static void save() {
        if (dataFile == null) {
            return;
        }
        try (Writer writer = new FileWriter(dataFile)) {
            GSON.toJson(conversionMap, writer);
            dirty = false;
            ticksSinceSave = 0;
        } catch (IOException e) {
            LOGGER.error("Failed to save conversion data", e);
        }
    }

    private static String getPlayerKey(ServerPlayer player) {
        return player.getName().getString() + "," + player.getStringUUID();
    }

    public static boolean hasConverted(ServerPlayer player) {
        return conversionMap.getOrDefault(getPlayerKey(player), false);
    }

    /** Evolution is rare and meaningful, so write it through immediately. */
    public static void setConverted(ServerPlayer player, boolean converted) {
        conversionMap.put(getPlayerKey(player), converted);
        save();
    }
}
