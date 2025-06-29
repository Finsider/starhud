package fin.starhud.hud.implementation;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.hud.EffectSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import fin.starhud.helper.StatusEffectAttribute;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// WIP
public class Effect extends AbstractHUD {

    private static final EffectSettings effectSettings = Main.settings.effectSettings;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final Identifier STATUS_EFFECT_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/effect.png");
    private static final Identifier STATUS_EFFECT_BAR_TEXTURE = Identifier.of("starhud", "hud/effect_bar.png");
    private static final Identifier STATUS_EFFECT_AMBIENT_TEXTURE = Identifier.of("starhud", "hud/effect_ambient.png");

    private static final int STATUS_EFFECT_TEXTURE_WIDTH = 24;
    private static final int STATUS_EFFECT_TEXTURE_HEIGHT = 32;
    private static final int STATUS_EFFECT_BAR_TEXTURE_WIDTH = 21;
    private static final int STATUS_EFFECT_BAR_TEXTURE_HEIGHT = 3;

    private static final Map<RegistryEntry<StatusEffect>, Identifier> STATUS_EFFECT_TEXTURE_MAP = new HashMap<>();

    public Effect() {
        super(effectSettings.base);
    }

    @Override
    public void renderHUD(DrawContext context) {

        // straight up copied from minecraft's own status effect rendering system.

        Collection<StatusEffectInstance> collection = CLIENT.player.getStatusEffects();
        if (collection.isEmpty())
            return;

        int beneficialIndex = 0;
        int harmIndex = 0;

        boolean drawVertical = effectSettings.drawVertical;
        int sameTypeGap = effectSettings.sameTypeGap;
        int differentTypeGap = ((drawVertical && effectSettings.base.originX == ScreenAlignmentX.RIGHT) || (!drawVertical && effectSettings.base.originY == ScreenAlignmentY.BOTTOM)) ? -effectSettings.differentTypeGap :effectSettings.differentTypeGap;

        // if originX = right, invert differentTypeGap
        // if originY = down, invert differentTypeGap

        int beneficialSize = getBeneficialSize();
        int harmSize = collection.size() - beneficialSize;

        int xBeneficial = x - effectSettings.growthDirectionX.getGrowthDirection(getDynamicWidth(true, beneficialSize, harmSize));
        int yBeneficial = y - effectSettings.growthDirectionY.getGrowthDirection(getDynamicHeight(true, beneficialSize, harmSize));

        int xHarm = (beneficialSize == 0 && drawVertical) ? xBeneficial : x - effectSettings.growthDirectionX.getGrowthDirection(getDynamicWidth(false, beneficialSize, harmSize));
        int yHarm = (beneficialSize == 0 && !drawVertical) ? yBeneficial : y - effectSettings.growthDirectionY.getGrowthDirection(getDynamicHeight(false, beneficialSize, harmSize));

        for (StatusEffectInstance statusEffectInstance : collection) {
            if (!statusEffectInstance.shouldShowIcon())
                continue;

            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
            StatusEffectAttribute statusEffectAttribute = StatusEffectAttribute.getStatusEffectAttribute(statusEffectInstance);

            int x2;
            int y2;

            if (registryEntry.value().isBeneficial()) {
                x2 = (xBeneficial) + ((drawVertical ? 0 : sameTypeGap) * beneficialIndex);
                y2 = (yBeneficial) + ((drawVertical ? sameTypeGap : 0) * beneficialIndex);
                ++beneficialIndex;
            } else {
                x2 = (xHarm) + (beneficialSize == 0 ? 0 : (drawVertical ? differentTypeGap : 0)) + ((drawVertical ? 0 : sameTypeGap) * harmIndex);
                y2 = (yHarm) + (beneficialSize == 0 ? 0 : (drawVertical ? 0 : differentTypeGap)) + ((drawVertical ? sameTypeGap : 0) * harmIndex);
                ++harmIndex;
            }

            if (statusEffectInstance.isAmbient()) {

                // draw soft blue outlined background...
                RenderUtils.drawTextureHUD(
                        context,
                        STATUS_EFFECT_AMBIENT_TEXTURE,
                        x2, y2,
                        0, 0,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT,
                        effectSettings.ambientColor | 0xFF000000
                );

            } else {

                // draw background
                RenderUtils.drawTextureHUD(
                        context,
                        STATUS_EFFECT_BACKGROUND_TEXTURE,
                        x2, y2,
                        0, 0,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT
                );
            }

            int step, color;
            if (statusEffectInstance.isInfinite()) {
                step = 7;
                color = effectSettings.infiniteColor | 0xFF000000;
            } else {
                int duration = statusEffectInstance.getDuration();
                int maxDuration = statusEffectAttribute.maxDuration();

                step = Helper.getStep(duration, maxDuration, 7);
                color = RenderUtils.getItemBarColor(step, 7) | 0xFF000000;
            }

            // draw timer bar
            RenderUtils.drawTextureHUD(
                    context,
                    STATUS_EFFECT_BAR_TEXTURE,
                    x2 + 2, y2 + 27,
                    0, 0,
                    3 * step, STATUS_EFFECT_BAR_TEXTURE_HEIGHT,
                    STATUS_EFFECT_BAR_TEXTURE_WIDTH, STATUS_EFFECT_BAR_TEXTURE_HEIGHT,
                    color
            );

            // draw effect texture.
            RenderUtils.drawTextureHUD(
                    context,
                    getStatusEffectTexture(registryEntry),
                    x2 + 3, y2 + 3,
                    0,0,
                    18, 18,
                    18,18
            );

            // draw amplifier text.
            int amplifier = statusEffectAttribute.amplifier() + 1;
            if (amplifier == 1)
                continue;

            String amplifierStr = Helper.toSubscript(Integer.toString(amplifier));

            RenderUtils.drawTextHUD(
                    context,
                    amplifierStr,
                    x2 + 3 + 18 - CLIENT.textRenderer.getWidth(amplifierStr) + 1, y2 + 2 + 18 - 7,
                    0xFFFFFFFF,
                    true
            );

        }
    }

    public int getDynamicWidth(boolean isBeneficial, int beneficialSize, int harmSize) {
         return effectSettings.drawVertical ? STATUS_EFFECT_TEXTURE_WIDTH : ((isBeneficial ? beneficialSize : harmSize) * effectSettings.sameTypeGap);
    }

    public int getDynamicHeight(boolean isBeneficial, int beneficialSize, int harmSize) {
        return effectSettings.drawVertical ? ((isBeneficial ? beneficialSize : harmSize) * effectSettings.sameTypeGap) : STATUS_EFFECT_TEXTURE_HEIGHT;
    }

    public int getBeneficialSize() {
        int size = 0;
        for (StatusEffectInstance collection : CLIENT.player.getStatusEffects()) {
            if (collection.getEffectType().value().isBeneficial())
                ++size;
        }
        return size;
    }

    // 0 because the width is dependent to how many status effect are present.

    @Override
    public int getBaseHUDWidth() {
        return 0;
    }

    @Override
    public int getBaseHUDHeight() {
        return 0;
    }

    public static Identifier getStatusEffectTexture(RegistryEntry<StatusEffect> effect) {
        return STATUS_EFFECT_TEXTURE_MAP.computeIfAbsent(
                effect,
                e -> e.getKey()
                        .map(RegistryKey::getValue)
                        .map(id -> Identifier.of(id.getNamespace(), "textures/mob_effect/" + id.getPath() + ".png"))
                        .orElseGet(MissingSprite::getMissingSpriteId)
        );
    }

}
