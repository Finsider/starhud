package fin.starhud.hud.implementation.other;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.hud.other.BiomeSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class  BiomeHUD extends AbstractHUD {

    private static final BiomeSettings BIOME_SETTINGS = Main.settings.biomeSettings;

    private static final Identifier DIMENSION_TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/dimension.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static FormattedCharSequence cachedBiomeNameText;
    private static Holder<Biome> cachedBiome;
    private static int cachedTextWidth;

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public BiomeHUD() {
        super(BIOME_SETTINGS.base);
    }

    private int color;
    private int dimensionIndex;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        Font textRenderer = CLIENT.font;
        displayMode = getSettings().getDisplayMode();

        if (CLIENT.player == null || CLIENT.level == null) return false;

        BlockPos blockPos = CLIENT.player.getOnPos();
        Holder<Biome> currentBiome = CLIENT.level.getBiome(blockPos);

        if (cachedBiome != currentBiome) {
            Optional<ResourceKey<Biome>> biomeKey = currentBiome.unwrapKey();

            if (biomeKey.isPresent()) {
                Identifier biomeId = biomeKey.get().identifier();
                String translatableKey = "biome." + biomeId.getNamespace() + '.' + biomeId.getPath();

                // if it has translation we get the translation, else we just convert it to Pascal Case manually.
                if (Language.getInstance().has(translatableKey))
                    cachedBiomeNameText = Component.translatable(translatableKey).getVisualOrderText();
                else
                    cachedBiomeNameText = Component.nullToEmpty(Helper.idNameFormatter(currentBiome.getRegisteredName())).getVisualOrderText();

            } else {
                cachedBiomeNameText = Component.nullToEmpty("Unregistered").getVisualOrderText();
            }

            cachedBiome = currentBiome;
            cachedTextWidth = textRenderer.width(cachedBiomeNameText) - 1;
        }

        dimensionIndex = getDimensionIndex(CLIENT.level.dimension());
        color = getTextColorFromDimension(dimensionIndex) | 0xFF000000;

        int width = displayMode.calculateWidth(ICON_WIDTH, cachedTextWidth);

        setWidthHeightColor(width, ICON_HEIGHT, color);

        return cachedBiomeNameText != null;
    }

    @Override
    public String getName() {
        return "Biome HUD";
    }

    @Override
    public String getId() {
        return HUDId.BIOME.toString();
    }

    @Override
    public boolean renderHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

        return RenderUtils.drawSmallHUD(
                context,
                cachedBiomeNameText,
                x, y,
                getWidth(), getHeight(),
                DIMENSION_TEXTURE,
                0.0F, ICON_HEIGHT * dimensionIndex,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                0xFFFFFFFF,
                displayMode,
                drawBackground,
                drawTextShadow
        );

    }

    private static int getDimensionIndex(ResourceKey<Level> registryKey) {
        if (registryKey == Level.OVERWORLD) return 0;
        else if (registryKey == Level.NETHER) return 1;
        else if (registryKey == Level.END) return 2;
        else return 3;
    }

    private static int getTextColorFromDimension(int dimensionIndex) {
        return switch (dimensionIndex) {
            case 0 -> BIOME_SETTINGS.color.overworld;
            case 1 -> BIOME_SETTINGS.color.nether;
            case 2 -> BIOME_SETTINGS.color.end;
            default -> BIOME_SETTINGS.color.custom;
        };
    }
}
