package fin.starhud.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

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

    public static void onEndTick(MinecraftClient client) {
        if (client.world == null) return;
        if (client.player == null) return;

        int currentHurtTime = client.player.hurtTime;
        if (currentHurtTime > 0 && lastHurtTime == 0) {
            if (combo != -1)
                combo = 0;
        }
        lastHurtTime = currentHurtTime;

        long now = client.world.getTime(); // ticks
        if (lastHitTime != -1 && now - lastHitTime >= 4 * 20) { // 4 seconds
            combo = -1;
            reach = -1;
            lastHitTime = now;
            entityUuid = null;
        }
    }

    public static ActionResult onAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!world.isClient()) return ActionResult.PASS;

        long now = world.getTime(); // ticks

        boolean sameTarget = entity.getUuid().equals(entityUuid);
        boolean cooldownExpired = now - lastHitTime >= 10;

        if (sameTarget && !cooldownExpired) return ActionResult.PASS;

        HitResult target = MinecraftClient.getInstance().crosshairTarget;

        if (target instanceof EntityHitResult ehr) {
            reach = player.getEyePos().distanceTo(ehr.getPos());
        } else {
            reach = player.getEyePos().distanceTo(entity.getEntityPos());
        }

        if (sameTarget) {
            ++combo;
        } else {
            combo = 1;
            entityUuid = entity.getUuid();
        }
        lastHitTime = now;

        return ActionResult.PASS;
    }
}
