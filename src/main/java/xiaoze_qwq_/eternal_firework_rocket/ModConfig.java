package xiaoze_qwq_.eternal_firework_rocket.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "eternal_firework_rocket.json");
    public static final Logger LOGGER = LoggerFactory.getLogger("EternalFireworkRocket/Config");
    
    public static ConfigData CONFIG = new ConfigData();
    
    public static class ConfigData {
        // 直接使用百分比值（0.01 表示 0.01%）
        public double chanceFlight1 = 0.1;      // 0.01%
        public double chanceFlight2 = 0.01;     // 0.001%
        public double chanceFlight3 = 0.001;    // 0.0001%
        
        // 冷却时间偏移量
        public float cooldownOffset = 0.5f;
        
        // 是否仅管理员可用
        public boolean adminOnly = false;
    }
    
    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                CONFIG = GSON.fromJson(reader, ConfigData.class);
                validateConfig();
            } catch (IOException e) {
                LOGGER.error("Failed to load config file", e);
            }
        } else {
            saveConfig();
        }
    }
    
    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config file", e);
        }
    }
    
    private static void validateConfig() {
        // 确保概率值在合理范围内（0到100之间）
        CONFIG.chanceFlight1 = Math.max(0, Math.min(CONFIG.chanceFlight1, 100.0));
        CONFIG.chanceFlight2 = Math.max(0, Math.min(CONFIG.chanceFlight2, 100.0));
        CONFIG.chanceFlight3 = Math.max(0, Math.min(CONFIG.chanceFlight3, 100.0));
        
        // 确保冷却偏移量合理
        CONFIG.cooldownOffset = Math.max(0, Math.min(CONFIG.cooldownOffset, 2.5f));
    }
}