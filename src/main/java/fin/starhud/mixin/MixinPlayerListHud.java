package fin.starhud.mixin;

import fin.starhud.helper.Box;
import fin.starhud.helper.condition.PlayerListHUD;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {

    @Unique
    private static final Box tempBox = new Box(0,0);

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void resetBoundingBox(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        PlayerListHUD.boundingBox.setEmpty(true);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"
            )
    )
    private void captureFillBounds(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        tempBox.setBoundingBox(x1, y1, x2 - x1, y2 - y1);
        if (PlayerListHUD.boundingBox.isEmpty()) {
            PlayerListHUD.boundingBox.copyFrom(tempBox);
        } else {
            PlayerListHUD.boundingBox.mergeWith(tempBox);
        }

        instance.fill(x1, y1, x2, y2, color);
    }
}
