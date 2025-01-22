package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class biome {

    private static final Settings.BiomeSettings biome = Main.settings.biomeSettings;

    private static final Identifier DIMENSION_TEXTURE = Identifier.of("starhud", "hud/biome.png");

    private static String cachedFormattedBiomeStr = "";
    private static String cachedBiomeStr = "";
    private static int cachedTextWidth;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderBiomeIndicatorHUD(DrawContext context) {
        if ((biome.hideOn.f3 && Helper.isDebugHUDOpen()) || (biome.hideOn.chat && Helper.isChatFocused())) return;

        TextRenderer textRenderer = client.textRenderer;

        BlockPos blockPos = client.player.getBlockPos();
        String currentBiomeStr = client.world.getBiome(blockPos).getIdAsString();

        if (!cachedBiomeStr.equals(currentBiomeStr)) {
            cachedFormattedBiomeStr = biomeNameFormatter(currentBiomeStr);
            cachedBiomeStr = currentBiomeStr;
            cachedTextWidth = textRenderer.getWidth(cachedFormattedBiomeStr);
        }

        int x = Helper.calculatePositionX(biome.x, biome.originX, 24, biome.scale)
                - Helper.getGrowthDirection(biome.textGrowth, cachedTextWidth);
        int y = Helper.calculatePositionY(biome.y, biome.originY, 13, biome.scale);

        int dimensionIcon = getDimensionIcon(client.world.getRegistryKey());
        int color = getTextColorFromDimension(dimensionIcon) | 0xFF000000;

        context.getMatrices().push();
        Helper.setHUDScale(context, biome.scale);

        context.drawTexture(RenderLayer::getGuiTextured, DIMENSION_TEXTURE, x, y, 0.0F, dimensionIcon * 13, 13, 13, 13 ,52);
        Helper.fillRoundedRightSide(context, x + 14, y, x + 14 + cachedTextWidth + 9, y + 13, 0x80000000);
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
