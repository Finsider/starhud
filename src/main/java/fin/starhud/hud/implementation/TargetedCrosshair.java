package fin.starhud.hud.implementation;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.ConditionalSettings;
import fin.starhud.config.hud.TargetedCrosshairSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


// HUD similar to JADE's. TargetedCrosshairHUD.
public class TargetedCrosshair extends AbstractHUD {

    private static final Identifier ICON_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/item.png");
    private static final Identifier ENTITY_ICON_TEXTURE = Identifier.of("starhud", "hud/targeted_icon_entity.png");

    // left padding + texture + right padding
    private static final int ICON_BACKGROUND_WIDTH = 3 + 16 + 3;
    private static final int ICON_BACKGROUND_HEIGHT = 3 + 16 + 3;

    private static final TargetedCrosshairSettings TARGETED_CROSSHAIR_SETTINGS = Main.settings.targetedCrosshairSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final int BASE_HUD_WIDTH =
            ICON_BACKGROUND_WIDTH
                    + 1     // gap
                    + 5     // left text padding
                    + 5;    // right text padding

    private static final int BASE_HUD_HEIGHT = ICON_BACKGROUND_HEIGHT;

    public TargetedCrosshair() {
        super(TARGETED_CROSSHAIR_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Targeted Crosshair HUD";
    }

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
    public boolean renderHUD(DrawContext context) {
        return switch (CLIENT.crosshairTarget.getType()) {
            case BLOCK -> renderBlockInfoHUD(context);
            case ENTITY -> renderEntityInfoHUD(context);
            default -> false;
        };
    }

    private Block cachedBlock = null;
    private OrderedText cachedBlockName = null;
    private String cachedBlockModName = null;
    private int cachedBlockMaxWidth = -1;

    public boolean renderBlockInfoHUD(DrawContext context) {
        BlockPos pos = ((BlockHitResult) CLIENT.crosshairTarget).getBlockPos();

        BlockState blockState = CLIENT.world.getBlockState(pos);
        Block block = blockState.getBlock();
        Item blockItem = block.asItem();
        ItemStack blockStack = blockItem.getDefaultStack();

        if (!block.equals(cachedBlock)) {
            cachedBlock = block;

            if (blockItem == Items.AIR) cachedBlockName = Text.translatable(block.getTranslationKey()).asOrderedText();
            else cachedBlockName = blockStack.getName().asOrderedText();

            cachedBlockModName = Helper.getModName(Registries.BLOCK.getId(block));

            int blockNameWidth = CLIENT.textRenderer.getWidth(cachedBlockName);
            int modNameWidth = CLIENT.textRenderer.getWidth(cachedBlockModName);
            cachedBlockMaxWidth = Math.max(modNameWidth, blockNameWidth) - 1;
        }

        int xTemp = x - TARGETED_CROSSHAIR_SETTINGS.base.growthDirectionX.getGrowthDirection(cachedBlockMaxWidth);

        RenderUtils.drawTextureHUD(
                context,
                ICON_BACKGROUND_TEXTURE,
                xTemp, y,
                0, 0,
                ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT,
                ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT
        );
        RenderUtils.fillRoundedRightSide(
                context,
                xTemp + ICON_BACKGROUND_WIDTH + 1, y,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5 + cachedBlockMaxWidth + 5, y + ICON_BACKGROUND_HEIGHT,
                0x80000000
        );

        context.drawItem(blockStack, xTemp + 3, y + 3);
        RenderUtils.drawTextHUD(
                context,
                cachedBlockName,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5, y + 3,
                TARGETED_CROSSHAIR_SETTINGS.targetedNameColor | 0xFF000000,
                false
        );
        RenderUtils.drawTextHUD(
                context,
                cachedBlockModName,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5,
                y + ICON_BACKGROUND_HEIGHT - 3 - 7,
                TARGETED_CROSSHAIR_SETTINGS.modNameColor | 0xFF000000,
                false
        );

        setBoundingBox(xTemp, y, ICON_BACKGROUND_WIDTH + 1 + 5 + cachedBlockMaxWidth + 5, ICON_BACKGROUND_HEIGHT);
        return true;
    }

    private Entity cachedTargetedEntity = null;
    private OrderedText cachedEntityName = null;
    private String cachedEntityModName = null;
    private int cachedEntityMaxWidth = -1;
    private int cachedIndex = -1;

    public boolean renderEntityInfoHUD(DrawContext context) {
        Entity targetedEntity = MinecraftClient.getInstance().targetedEntity;

        if (!targetedEntity.equals(cachedTargetedEntity)) {
            cachedTargetedEntity = targetedEntity;
            cachedEntityName = targetedEntity.getName().asOrderedText();
            cachedEntityModName = Helper.getModName(Registries.ENTITY_TYPE.getId(targetedEntity.getType()));

            int entityNameWidth = CLIENT.textRenderer.getWidth(cachedEntityName);
            int modNameWidth = CLIENT.textRenderer.getWidth(cachedEntityModName);
            cachedEntityMaxWidth = Math.max(entityNameWidth, modNameWidth) - 1;

            cachedIndex = getEntityIconIndex(targetedEntity);
        }

        int xTemp = x - TARGETED_CROSSHAIR_SETTINGS.base.growthDirectionX.getGrowthDirection(cachedEntityMaxWidth);

        int color = getEntityIconColor(cachedIndex) | 0xFF000000;

        RenderUtils.drawTextureHUD(
                context,
                ENTITY_ICON_TEXTURE,
                xTemp, y,
                0, 22 * cachedIndex,
                ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT,
                ICON_BACKGROUND_WIDTH, ICON_BACKGROUND_HEIGHT * 5,
                color
        );
        RenderUtils.fillRoundedRightSide(
                context,
                xTemp + ICON_BACKGROUND_WIDTH + 1, y,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5 + cachedEntityMaxWidth + 5, y + ICON_BACKGROUND_HEIGHT,
                0x80000000
        );

        RenderUtils.drawTextHUD(
                context,
                cachedEntityName,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5, y + 3,
                color,
                false
        );
        RenderUtils.drawTextHUD(
                context,
                cachedEntityModName,
                xTemp + ICON_BACKGROUND_WIDTH + 1 + 5, y + ICON_BACKGROUND_HEIGHT - 3 - 7,
                TARGETED_CROSSHAIR_SETTINGS.modNameColor | 0xFF000000,
                false
        );

        setBoundingBox(xTemp, y, ICON_BACKGROUND_WIDTH + 1 + 5 + cachedEntityMaxWidth + 5, ICON_BACKGROUND_HEIGHT, color);
        return true;
    }

    private int getEntityIconIndex(Entity e) {
        if (isHostileMob(e)) return 0;
        else if (isAngerableMob(e)) return 1;
        else if (isPassiveMob(e)) return 2;
        else if (isPlayerEntity(e)) return 3;
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

    private static boolean isHostileMob(Entity e) {
        return switch (e) {
            case EnderDragonEntity ignored -> true;
            case EnderDragonPart ignored -> true;
            case Monster ignored -> true;
            default -> false;
        };
    }

    private static boolean isAngerableMob(Entity e) {
        return e instanceof Angerable;
    }

    private static boolean isPassiveMob(Entity e) {
        return switch (e) {
            case PassiveEntity ignored -> true;
            case WaterCreatureEntity ignored -> true;
            case AllayEntity ignored -> true;
            case SnowGolemEntity ignored -> true;
            default -> false;
        };
    }

    private static boolean isPlayerEntity(Entity e) {
        return e instanceof PlayerEntity;
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
