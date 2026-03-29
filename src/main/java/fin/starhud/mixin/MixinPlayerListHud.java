package fin.starhud.mixin;

import fin.starhud.condition.PlayerListHUD;
import fin.starhud.helper.Box;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerTabOverlay.class, priority = 500)
public class MixinPlayerListHud {

    @Unique
    private static final Box tempBox = new Box(0,0);

    @Inject(
            method = "extractRenderState",
            at = @At("HEAD")
    )
    private void resetBoundingBox(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective, CallbackInfo ci) {
        PlayerListHUD.boundingBox.setEmpty(true);
    }

    @Redirect(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"
            ),
            require = 0
    )
    private void captureFillBounds(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color) {
        tempBox.setBoundingBox(x1, y1, x2 - x1, y2 - y1);
        if (PlayerListHUD.boundingBox.isEmpty()) {
            PlayerListHUD.boundingBox.copyFrom(tempBox);
        } else {
            PlayerListHUD.boundingBox.mergeWith(tempBox);
        }

        instance.fill(x1, y1, x2, y2, color);
    }
}
