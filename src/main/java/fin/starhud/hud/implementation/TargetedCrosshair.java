package fin.starhud.hud.implementation;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.ConditionalSettings;
import fin.starhud.config.hud.TargetedCrosshairSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


// HUD similar to JADE's. TargetedCrosshairHUD.
public class TargetedCrosshair extends AbstractHUD {

    private static final Identifier ICON_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/targeted_icon.png");
    private static final Identifier ENTITY_ICON_TEXTURE = Identifier.of("starhud", "hud/targeted_icon_entity.png");

    // left padding + texture + right padding
    private static final int ICON_BACKGROUND_WIDTH = 3 + 16 + 3;
    private static final int ICON_BACKGROUND_HEIGHT = 3 + 16 + 3;

    private static final TargetedCrosshairSettings TARGETED_CROSSHAIR_SETTINGS = Main.settings.targetedCrosshairSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public TargetedCrosshair() {
        super(TARGETED_CROSSHAIR_SETTINGS.base);
    }


    private static final int BASE_HUD_WIDTH =
            ICON_BACKGROUND_WIDTH
            + 1     // gap
            + 5     // left text padding
            + 5;    // right text padding

    private static final int BASE_HUD_HEIGHT = ICON_BACKGROUND_HEIGHT;

    @Override
    public boolean shouldRender() {
        return baseHUDSettings.shouldRender &&
                CLIENT.crosshairTarget != null &&
                CLIENT.crosshairTarget.getType() != HitResult.Type.MISS &&
                shouldRenderOnCondition();
    }

    public static boolean shouldHUDRender() {

        if (!TARGETED_CROSSHAIR_SETTINGS.base.shouldRender)
            return false;

        if (CLIENT.crosshairTarget == null || CLIENT.crosshairTarget.getType() == HitResult.Type.MISS)
            return false;

        for (ConditionalSettings condition: TARGETED_CROSSHAIR_SETTINGS.base.conditions) {
            if (!condition.shouldRender)
                return false;
        }

        return true;
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

        Text blockName;
        if (blockState.getBlock().asItem() == Items.AIR) {
            blockName = Text.translatable(blockState.getBlock().getTranslationKey());
        } else {
            blockName = blockStack.getName();
        }
        String modName = Helper.getModName(Registries.BLOCK.getId(blockState.getBlock()));

        int blockNameWidth = CLIENT.textRenderer.getWidth(blockName);
        int modNameWidth = CLIENT.textRenderer.getWidth(modName);

        int maxWidth = Math.max(modNameWidth, blockNameWidth);
        int xTemp = x - TARGETED_CROSSHAIR_SETTINGS.textureGrowth.getGrowthDirection(maxWidth);

        RenderUtils.drawTextureHUD(context, ICON_BACKGROUND_TEXTURE, xTemp, y, 0, 0, ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT, ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT);
        RenderUtils.fillRoundedRightSide(context, xTemp + 3 + 16 + 3 + 1, y, xTemp + 3 + 16 + 3 + 1 + 5 + maxWidth - 1 + 5, y + ICON_BACKGROUND_HEIGHT, 0x80000000);

        context.drawItem(blockStack, xTemp + 3, y + 3);
        RenderUtils.drawTextHUD(context, blockName.asOrderedText(), xTemp + 3 + 16 + 3 + 1 + 5, y + 3, 0xFFFFFFFF, false);
        RenderUtils.drawTextHUD(context, modName, xTemp + 3 + 16 + 3 + 1 + 5, y + ICON_BACKGROUND_HEIGHT - 3 - 7, TARGETED_CROSSHAIR_SETTINGS.modNameColor | 0xFF000000, false);
    }

    public void renderEntityInfoHUD(DrawContext context) {
        Entity targetedEntity = MinecraftClient.getInstance().targetedEntity;

        Text entityName = targetedEntity.getName();
        String modName = Helper.getModName(Registries.ENTITY_TYPE.getId(targetedEntity.getType()));

        int entityNameWidth = CLIENT.textRenderer.getWidth(entityName);
        int modNameWidth = CLIENT.textRenderer.getWidth(modName);
        int maxWidth = Math.max(entityNameWidth, modNameWidth);

        int xTemp = x - TARGETED_CROSSHAIR_SETTINGS.textureGrowth.getGrowthDirection(maxWidth);

        int index = getEntityIconIndex(targetedEntity);
        int color = getEntityIconColor(index) | 0xFF000000;

        RenderUtils.drawTextureHUD(context, ENTITY_ICON_TEXTURE, xTemp, y, 0, 22 * index, ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT, ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT * 5, color);
        RenderUtils.fillRoundedRightSide(context, xTemp + 3 + 16 + 3 + 1, y, xTemp + 3 + 16 + 3 + 1 + 5 + maxWidth - 1 + 5, y + ICON_BACKGROUND_HEIGHT, 0x80000000);

        // this is very, very broken, so I decided to give up on drawing the entity directly on the HUD.
        // InventoryScreen.drawEntity()

        RenderUtils.drawTextHUD(context, entityName.asOrderedText(), xTemp + 3 + 16 + 3 + 1 + 5, y + 3, color, false);
        RenderUtils.drawTextHUD(context, modName, xTemp + 3 + 16 + 3 +1 + 5, y + ICON_BACKGROUND_HEIGHT - 3 - 7, TARGETED_CROSSHAIR_SETTINGS.modNameColor | 0xFF000000, false);
    }

    private int getEntityIconIndex(Entity e) {
        if (e instanceof Monster || e.getType() == EntityType.ENDER_DRAGON) return 0;
        if (e instanceof Angerable) return 1;
        if (e instanceof PassiveEntity || e instanceof AxolotlEntity || e instanceof WaterCreatureEntity) return 2;
        if (e instanceof PlayerEntity) return 3;
        else return 4;
    }

    private int getEntityIconColor(int index) {
        return switch (index) {
            case 0 -> TARGETED_CROSSHAIR_SETTINGS.entityColors.hostile;
            case 1 -> TARGETED_CROSSHAIR_SETTINGS.entityColors.angerable;
            case 2 -> TARGETED_CROSSHAIR_SETTINGS.entityColors.passive;
            case 3 -> TARGETED_CROSSHAIR_SETTINGS.entityColors.player;
            default -> TARGETED_CROSSHAIR_SETTINGS.entityColors.unknown;
        };
    }

    @Override
    public int getBaseHUDWidth() {
        return BASE_HUD_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return BASE_HUD_HEIGHT;
    }
}
