package xiaoze_qwq_.eternal_firework_rocket.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private static File dataFile;
    private static final Map<String, Boolean> conversionMap = new ConcurrentHashMap<>();

    private static String getPlayerKey(ServerPlayerEntity player) {
        return player.getName().getString() + "," + player.getUuidAsString();
    }

    public static void init(MinecraftServer server) {
        if (dataFile != null) return; // 已初始化

        // 获取存储目录：专用服务器 -> server 目录，单人 -> 世界目录
        Path basePath;
        if (server.isDedicated()) {
            basePath = server.getRunDirectory().toPath();
        } else {
            basePath = server.getSavePath(LevelResource.ROOT);
        }
        Path modFolder = basePath.resolve("eternal_firework_rocket");
        try {
            Files.createDirectories(modFolder);
        } catch (IOException e) {
            LOGGER.error("Failed to create mod folder: " + modFolder, e);
        }
        dataFile = modFolder.resolve("conversion.json").toFile();

        load();
    }

    private static void load() {
        if (!dataFile.exists()) {
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
        if (dataFile == null) return;
        try (Writer writer = new FileWriter(dataFile)) {
            GSON.toJson(conversionMap, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save conversion data", e);
        }
    }

    public static boolean hasConverted(ServerPlayerEntity player) {
        return conversionMap.getOrDefault(getPlayerKey(player), false);
    }

    public static void setConverted(ServerPlayerEntity player, boolean converted) {
        conversionMap.put(getPlayerKey(player), converted);
        save();
    }
}