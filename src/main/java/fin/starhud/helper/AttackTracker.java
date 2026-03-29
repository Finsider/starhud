package fin.starhud.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.UUID;

public class AttackTracker {

    private static double reach = -1;
    private static long combo = -1;

    private static UUID entityUuid;
    private static long lastHitTime = -1; // ticks
    private static int lastHurtTime = 0;

    public static double getReach() {
        return reach;
    }

    public static long getCombo() {
        return combo;
    }

    public static void onEndTick(Minecraft client) {
        if (client.level == null) return;
        if (client.player == null) return;

        int currentHurtTime = client.player.hurtTime;
        if (currentHurtTime > 0 && lastHurtTime == 0) {
            if (combo != -1)
                combo = 0;
        }
        lastHurtTime = currentHurtTime;

        long now = client.level.getGameTime(); // ticks
        if (lastHitTime != -1 && now - lastHitTime >= 4 * 20) { // 4 seconds
            combo = -1;
            reach = -1;
            lastHitTime = now;
            entityUuid = null;
        }
    }

    public static InteractionResult onAttack(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (!world.isClientSide()) return InteractionResult.PASS;

        long now = world.getGameTime(); // ticks

        boolean sameTarget = entity.getUUID().equals(entityUuid);
        boolean cooldownExpired = now - lastHitTime >= 10;

        if (sameTarget && !cooldownExpired) return InteractionResult.PASS;

        HitResult target = Minecraft.getInstance().hitResult;

        if (target instanceof EntityHitResult ehr) {
            reach = player.getEyePosition().distanceTo(ehr.getLocation());
        } else {
            reach = player.getEyePosition().distanceTo(entity.getEyePosition());
        }

        if (sameTarget) {
            ++combo;
        } else {
            combo = 1;
            entityUuid = entity.getUUID();
        }
        lastHitTime = now;

        return InteractionResult.PASS;
    }
}
