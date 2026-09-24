package xiaoze_qwq_.eternal_firework_rocket.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

//? if >=1.21.11 {
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
//?}

/**
 * Central permission check for the {@code adminOnly} config option.
 * Call this from the server side only; the client cannot know the
 * player's permission level.
 */
public final class PermissionHelper {
    private PermissionHelper() {}

    public static boolean hasUsePermission(Player user) {
        if (!ModConfig.CONFIG.adminOnly) {
            return true;
        }
        if (!(user instanceof ServerPlayer serverPlayer)) {
            return true;
        }
        //? if >=1.21.11 {
        return serverPlayer.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS));
        //?} else {
        /*return serverPlayer.hasPermissions(2);
        *///?}
    }
}
