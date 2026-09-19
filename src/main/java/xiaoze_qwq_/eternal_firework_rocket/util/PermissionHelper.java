package xiaoze_qwq_.eternal_firework_rocket.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

//? if >=1.21.11 {
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
//?}

/**
 * Central permission check for the {@code adminOnly} config option.
 * Call this from the server side only; the client cannot know the
 * player's permission level.
 */
public final class PermissionHelper {
    private PermissionHelper() {}

    public static boolean hasUsePermission(PlayerEntity user) {
        if (!ModConfig.CONFIG.adminOnly) {
            return true;
        }
        if (!(user instanceof ServerPlayerEntity serverPlayer)) {
            return true;
        }
        //? if >=1.21.11 {
        return serverPlayer.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS));
        //?} else {
        /*return serverPlayer.hasPermissionLevel(2);
        *///?}
    }
}
