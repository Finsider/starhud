package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.TargetedCrosshairSettings;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


// HUD similar to JADE's. TargetedCrosshairHUD.
public class TargetedCrosshair extends AbstractHUD {

    private static final TargetedCrosshairSettings TARGETED_CROSSHAIR_SETTINGS = Main.settings.targetedCrosshairSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public TargetedCrosshair() {
        super(TARGETED_CROSSHAIR_SETTINGS.base);
    }

    @Override
    public boolean shouldRender() {
        return baseHUDSettings.shouldRender &&
                CLIENT.crosshairTarget != null &&
                CLIENT.crosshairTarget.getType() != HitResult.Type.MISS &&
                shouldRenderOnCondition();
    }

    @Override
    public void renderHUD(DrawContext context) {
        switch (CLIENT.crosshairTarget.getType()) {
            case BLOCK -> renderBlockInfoHUD(context);
            case ENTITY -> renderEntityInfoHUD(context);
        }
    }

    public void renderBlockInfoHUD(DrawContext context) {
        BlockPos pos = ((BlockHitResult) CLIENT.crosshairTarget).getBlockPos();
        BlockState blockState = CLIENT.world.getBlockState(pos);

        ItemStack blockStack = blockState.getBlock().asItem().getDefaultStack();

        // draw block
        // draw block' name in text
        // draw block identifier in text under block'name
    }

    public void renderEntityInfoHUD(DrawContext context) {
        Entity targetedEntity = MinecraftClient.getInstance().targetedEntity;

        if (!(targetedEntity instanceof LivingEntity entity))
            return;

        // this is very, very broken, so I decided to give up on drawing the entity directly on the HUD.
        // InventoryScreen.drawEntity()

        // draw entity icon -> WIP
        // draw entity' name text
        // draw entity' identifier. text



    }

    @Override
    public int getBaseHUDWidth() {
        return 0;
    }

    @Override
    public int getBaseHUDHeight() {
        return 0;
    }
}
