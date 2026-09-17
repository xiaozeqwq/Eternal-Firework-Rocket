package xiaoze_qwq_.eternal_firework_rocket.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
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

    // 在服务器启动时调用
    public static void init(MinecraftServer server) {
        if (dataFile != null) return;

        // 获取世界根目录
        Path basePath = server.getSavePath(WorldSavePath.ROOT);
        Path modFolder = basePath.resolve("eternal_firework_rocket");
        try {
            Files.createDirectories(modFolder);
        } catch (IOException e) {
            LOGGER.error("Failed to create mod folder: " + modFolder, e);
        }
        dataFile = modFolder.resolve("conversion.json").toFile();

        // 加载现有数据，如果文件不存在则创建空文件
        load();
        if (!dataFile.exists()) {
            save(); // 创建空 json 文件
        }

        // 注册玩家登录事件，自动添加条目（false）
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server1) -> {
            ServerPlayerEntity player = handler.getPlayer();
            registerPlayer(player);
        });

        LOGGER.info("Conversion tracker initialized, file: " + dataFile.getAbsolutePath());
    }

    // 玩家登录时调用，若不存在则添加 false
    public static void registerPlayer(ServerPlayerEntity player) {
        String key = getPlayerKey(player);
        if (!conversionMap.containsKey(key)) {
            conversionMap.put(key, false);
            save();
            LOGGER.debug("Registered player {} with conversion=false", key);
        }
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