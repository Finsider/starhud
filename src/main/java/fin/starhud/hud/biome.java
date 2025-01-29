package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class biome {

    private static final Settings.BiomeSettings biome = Main.settings.biomeSettings;
    private static final Settings.BaseSettings base = biome.base;

    private static final Identifier DIMENSION_TEXTURE = Identifier.of("starhud", "hud/biome.png");

    private static String cachedFormattedBiomeStr = "";
    private static RegistryEntry<Biome> cachedBiome;
    private static int cachedTextWidth;

    private static final int width = 24;
    private static final int height = 13;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderBiomeIndicatorHUD(DrawContext context) {
        if (Helper.IsHideOn(base.hideOn)) return;

        TextRenderer textRenderer = client.textRenderer;

        BlockPos blockPos = client.player.getBlockPos();
        RegistryEntry<Biome> currentBiome = client.world.getBiome(blockPos);

        if (cachedBiome != currentBiome) {
            cachedFormattedBiomeStr = biomeNameFormatter(currentBiome.getIdAsString());
            cachedBiome = currentBiome;
            cachedTextWidth = textRenderer.getWidth(cachedFormattedBiomeStr);
        }

        int x = Helper.calculatePositionX(base, width)
                - Helper.getGrowthDirection(biome.textGrowth, cachedTextWidth);
        int y = Helper.calculatePositionY(base, height);

        int dimensionIcon = getDimensionIcon(client.world.getRegistryKey());
        int color = getTextColorFromDimension(dimensionIcon) | 0xFF000000;

        context.getMatrices().push();
        Helper.setHUDScale(context, base.scale);

        context.drawTexture(RenderLayer::getGuiTextured, DIMENSION_TEXTURE, x, y, 0.0F, dimensionIcon * height, 13, height, 13 ,52);
        Helper.fillRoundedRightSide(context, x + 14, y, x + 14 + cachedTextWidth + 9, y + height, 0x80000000);
        context.drawText(client.textRenderer, cachedFormattedBiomeStr, x + 19, y + 3, color, false);

        context.getMatrices().pop();
    }

    private static int getDimensionIcon(RegistryKey<World> registryKey) {
        if (registryKey == World.OVERWORLD) return 0;
        else if (registryKey == World.NETHER) return 1;
        else if (registryKey == World.END) return 2;
        else return 3;
    }

    private static int getTextColorFromDimension(int dimension) {
        return switch (dimension) {
          case 0 -> biome.color.overworld;
          case 1 -> biome.color.nether;
          case 2 -> biome.color.end;
          default -> biome.color.custom;
        };
    }

    private static String biomeNameFormatter(String oldString) {

        // trim every character from ':' until first index
        oldString = oldString.substring(oldString.indexOf(':') + 1);

        char[] chars = oldString.toCharArray();

        if (chars.length == 0) return "-";

        chars[0] = Character.toUpperCase(chars[0]);
        for (int i = 1; i < chars.length; ++i) {
            if (chars[i] != '_') continue;

            chars[i] = ' ';

            // capitalize the first character after spaces
            if (i + 1 < chars.length) {
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }
}
