package fin.starhud.hud.implementation.other;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.hud.other.TargetedCrosshairSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;


// HUD similar to JADE's. TargetedCrosshairHUD.
public class TargetedCrosshairHUD extends AbstractHUD {

    private static final TargetedCrosshairSettings SETTINGS = Main.settings.targetedCrosshairSettings;
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final Identifier ENTITY_ICON_TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/targeted_icon_entity.png");

    // left padding + texture + right padding
    private static final int ICON_WIDTH = 3 + 16 + 3;
    private static final int ICON_HEIGHT = 3 + 16 + 3;

    private static final Minecraft CLIENT = Minecraft.getInstance();


    public TargetedCrosshairHUD() {
        super(SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Targeted Crosshair HUD";
    }

    @Override
    public String getId() {
        return HUDId.TARGETED_CROSSHAIR.toString();
    }

    public static boolean isShown() {

        if (!SETTINGS.base.shouldRender())
            return false;

        return CLIENT.hitResult != null && CLIENT.hitResult.getType() != HitResult.Type.MISS;
    }

    private int width;
    private int height;
    private HitResult.Type hitResultType;
    private HUDDisplayMode displayMode;
    private TargetedCrosshairSettings.InformationMode informationMode;

    @Override
    public boolean collectHUDInformation() {
        if (CLIENT.hitResult == null) return false;

        hitResultType = CLIENT.hitResult.getType();

        displayMode = getSettings().getDisplayMode();
        informationMode = SETTINGS.getInformationMode();

        return switch (hitResultType) {
            case BLOCK -> collectDataBlock();
            case ENTITY -> collectDataEntity();
            default -> false;
        };
    }

    private ItemStack blockStack;
    private Block cachedBlock = null;
    private FormattedCharSequence cachedBlockName = null;
    private String cachedBlockModName = null;
    private int cachedBlockMaxWidth = -1;
    private int targetedNameColor;
    private int modNameColor;

    public boolean collectDataBlock() {

        if (CLIENT.level == null) return false;

        if (!(CLIENT.hitResult instanceof BlockHitResult blockHitResult))
            return false;

        BlockPos pos = blockHitResult.getBlockPos();

        BlockState blockState = CLIENT.level.getBlockState(pos);
        Block block = blockState.getBlock();
        Item blockItem = block.asItem();
        blockStack = blockItem.getDefaultInstance();

        if (!block.equals(cachedBlock)) {
            cachedBlock = block;

            if (blockItem == Items.AIR) cachedBlockName = Component.translatable(block.getDescriptionId()).getVisualOrderText();
            else cachedBlockName = blockStack.getHoverName().getVisualOrderText();

            cachedBlockModName = Helper.getModName(BuiltInRegistries.BLOCK.getKey(block));

            int blockNameWidth = CLIENT.font.width(cachedBlockName);
            int modNameWidth = CLIENT.font.width(cachedBlockModName);
            switch (informationMode) {
                case TARGETED_NAME -> cachedBlockMaxWidth = blockNameWidth - 1;
                case MOD_NAME -> cachedBlockMaxWidth = modNameWidth - 1;
                default -> cachedBlockMaxWidth = Math.max(blockNameWidth, modNameWidth) - 1;
            }
        }

        targetedNameColor = SETTINGS.targetedNameColor | 0xFF000000;
        modNameColor = SETTINGS.modNameColor | 0xFF000000;
        width = displayMode.calculateWidth(ICON_WIDTH, cachedBlockMaxWidth);
        height = ICON_HEIGHT;

        setWidthHeightColor(width, height, targetedNameColor);
        return cachedBlockName != null && cachedBlockModName != null;
    }

    private Entity cachedTargetedEntity = null;
    private FormattedCharSequence cachedEntityName = null;
    private String cachedEntityModName = null;
    private int cachedEntityMaxWidth = -1;
    private int cachedIndex = -1;

    public boolean collectDataEntity() {

        if (!(CLIENT.hitResult instanceof EntityHitResult entityHitResult))
            return false;

        Entity targetedEntity = entityHitResult.getEntity();

        if (!targetedEntity.equals(cachedTargetedEntity)) {
            cachedTargetedEntity = targetedEntity;
            cachedEntityName = targetedEntity.getName().getVisualOrderText();
            cachedEntityModName = Helper.getModName(BuiltInRegistries.ENTITY_TYPE.getKey(targetedEntity.getType()));

            int entityNameWidth = CLIENT.font.width(cachedEntityName);
            int modNameWidth = CLIENT.font.width(cachedEntityModName);

            switch (informationMode) {
                case TARGETED_NAME -> cachedEntityMaxWidth = entityNameWidth - 1;
                case MOD_NAME -> cachedEntityMaxWidth = modNameWidth - 1;
                default -> cachedEntityMaxWidth = Math.max(entityNameWidth, modNameWidth) - 1;
            }

            cachedIndex = getEntityIconIndex(targetedEntity);
        }

        width = displayMode.calculateWidth(ICON_WIDTH, cachedEntityMaxWidth);
        height = ICON_HEIGHT;
        targetedNameColor = getEntityIconColor(cachedIndex) | 0xFF000000;
        modNameColor = SETTINGS.modNameColor | 0xFF000000;

        setWidthHeightColor(width, height, targetedNameColor);

        return cachedEntityName != null && cachedEntityModName != null;
    }

    @Override
    public boolean renderHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        if (hitResultType == null)
            return false;
        return switch (hitResultType) {
            case BLOCK -> renderBlockInfoHUD(context, x, y, drawBackground, drawTextShadow);
            case ENTITY -> renderEntityInfoHUD(context, x, y, drawBackground, drawTextShadow);
            default -> false;
        };
    }

    public boolean renderBlockInfoHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

        if (displayMode == null || cachedBlockName == null || cachedBlockModName == null || blockStack == null)
            return false;

        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + ICON_WIDTH, y + ICON_HEIGHT, 0x80000000);
                context.item(blockStack, x + 3, y + 3);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                RenderUtils.drawTextHUD(context, cachedBlockName, x + padding, y + 3, targetedNameColor, drawTextShadow);
                RenderUtils.drawTextHUD(context, cachedBlockModName, x + padding, y + h - 3 - 7, modNameColor, drawTextShadow);

                switch (informationMode) {
                    case TARGETED_NAME -> RenderUtils.drawTextHUD(context, cachedBlockName, x + padding, y + 7, targetedNameColor, drawTextShadow);
                    case MOD_NAME -> RenderUtils.drawTextHUD(context, cachedBlockModName, x + padding, y + 7, modNameColor, drawTextShadow);
                    case BOTH -> {
                        RenderUtils.drawTextHUD(context, cachedBlockName, x + padding, y + 3, targetedNameColor, drawTextShadow);
                        RenderUtils.drawTextHUD(context, cachedBlockModName, x + padding, y + h - 3 - 7, modNameColor, drawTextShadow);
                    }
                }
            }
            case BOTH -> {
                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(
                                context,
                                x, y,
                                x + w, y + h,
                                0x80000000
                        );
                    else {
                        RenderUtils.fillRoundedLeftSide(
                                context,
                                x, y,
                                x + ICON_WIDTH, y + h,
                                0x80000000
                        );
                        RenderUtils.fillRoundedRightSide(
                                context,
                                x + ICON_WIDTH + gap, y,
                                x + w, y + h,
                                0x80000000
                        );
                    }
                }

                context.item(blockStack, x + 3, y + 3);
                switch (informationMode) {
                    case TARGETED_NAME -> RenderUtils.drawTextHUD(context, cachedBlockName, x + ICON_WIDTH + gap + padding, y + 7, targetedNameColor, drawTextShadow);
                    case MOD_NAME -> RenderUtils.drawTextHUD(context, cachedBlockModName, x + ICON_WIDTH + gap + padding, y + 7, modNameColor, drawTextShadow);
                    case BOTH -> {
                        RenderUtils.drawTextHUD(context, cachedBlockName, x + ICON_WIDTH + gap + padding, y + 3, targetedNameColor, drawTextShadow);
                        RenderUtils.drawTextHUD(context, cachedBlockModName, x + ICON_WIDTH + gap + padding, y + h - 3 - 7, modNameColor, drawTextShadow);
                    }
                }
            }
        }

        return true;
    }

    public boolean renderEntityInfoHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

        if (displayMode == null || cachedEntityName == null || cachedEntityModName == null)
            return false;

        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + ICON_WIDTH, y + ICON_HEIGHT, 0x80000000);
                RenderUtils.drawTextureHUD(context, ENTITY_ICON_TEXTURE, x, y, 0, 22 * cachedIndex, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT * 5, targetedNameColor);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);

                switch (informationMode) {
                    case TARGETED_NAME -> RenderUtils.drawTextHUD(context, cachedEntityName, x + padding, y + 7, targetedNameColor, drawTextShadow);
                    case MOD_NAME -> RenderUtils.drawTextHUD(context, cachedEntityModName, x + padding, y + 7, modNameColor, drawTextShadow);
                    case BOTH -> {
                        RenderUtils.drawTextHUD(context, cachedEntityName, x + padding, y + 3, targetedNameColor, drawTextShadow);
                        RenderUtils.drawTextHUD(context, cachedEntityModName, x + padding, y + h - 3 - 7, modNameColor, drawTextShadow);
                    }
                }
            }
            case BOTH -> {
                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(
                                context,
                                x, y,
                                x + w, y + h,
                                0x80000000
                        );
                    else {
                        RenderUtils.fillRoundedLeftSide(
                                context,
                                x, y,
                                x + ICON_WIDTH, y + h,
                                0x80000000
                        );
                        RenderUtils.fillRoundedRightSide(
                                context,
                                x + ICON_WIDTH + gap, y,
                                x + w, y + h,
                                0x80000000
                        );
                    }
                }
                RenderUtils.drawTextureHUD(
                        context,
                        ENTITY_ICON_TEXTURE,
                        x, y,
                        0, 22 * cachedIndex,
                        ICON_WIDTH, ICON_HEIGHT,
                        ICON_WIDTH, ICON_HEIGHT * 5,
                        targetedNameColor
                );

                switch (informationMode) {
                    case TARGETED_NAME -> RenderUtils.drawTextHUD(context, cachedEntityName, x + ICON_WIDTH + gap + padding, y + 7, targetedNameColor, drawTextShadow);
                    case MOD_NAME -> RenderUtils.drawTextHUD(context, cachedEntityModName, x + ICON_WIDTH + gap + padding, y + 7, targetedNameColor, drawTextShadow);
                    case BOTH -> {
                        RenderUtils.drawTextHUD(context, cachedEntityName, x + ICON_WIDTH + gap + padding, y + 3, targetedNameColor, drawTextShadow);
                        RenderUtils.drawTextHUD(context, cachedEntityModName, x + ICON_WIDTH + gap + padding, y + h - 3 - 7, modNameColor, drawTextShadow);
                    }
                }
            }
        }

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
            case 0 -> SETTINGS.entityColors.hostile;
            case 1 -> SETTINGS.entityColors.angerable;
            case 2 -> SETTINGS.entityColors.passive;
            case 3 -> SETTINGS.entityColors.player;
            default -> SETTINGS.entityColors.unknown;
        };
    }

    private static boolean isHostileMob(Entity e) {
        return e instanceof Enemy;
    }

    private static boolean isAngerableMob(Entity e) {
        return e instanceof NeutralMob;
    }

    private static boolean isPassiveMob(Entity e) {
        return switch (e) {
            case Animal ignored -> true;
            case WaterAnimal ignored -> true;
            case Allay ignored -> true;
            case SnowGolem ignored -> true;
            default -> false;
        };
    }

    private static boolean isPlayerEntity(Entity e) {
        return e instanceof Player;
    }
}
