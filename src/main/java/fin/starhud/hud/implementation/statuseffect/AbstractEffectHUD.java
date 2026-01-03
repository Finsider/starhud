package fin.starhud.hud.implementation.statuseffect;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.Settings;
import fin.starhud.config.hud.statuseffect.EffectSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEffectHUD extends AbstractHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;
    private static final Settings.Effect SETTINGS = Main.settings.effectSettings;

    private static final Identifier STATUS_EFFECT_BAR_TEXTURE = Identifier.of("starhud", "hud/effect_bar.png");
    private static final Identifier STATUS_EFFECT_BAR_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/effect_bar_background.png");

    private static final Identifier STATUS_EFFECT_TEXTURE = Identifier.of("starhud", "hud/effect.png");
    private static final Identifier STATUS_EFFECT_AMBIENT_TEXTURE = Identifier.of("starhud", "hud/effect_ambient.png");
    private static final Identifier STATUS_EFFECT_AMBIENT_COMBINED_TEXTURE = Identifier.of("starhud", "hud/effect_ambient_combined.png");

    private static final int ICON_WIDTH = 24;
    private static final int ICON_HEIGHT = 24;

//    private static final int INFO_WIDTH = 24;
    private static final int INFO_HEIGHT = 7;

    private static final int STATUS_EFFECT_TEXTURE_HEIGHT = 32;
    private static final int STATUS_EFFECT_TEXTURE_WIDTH = 24;
    private static final int STATUS_EFFECT_BAR_TEXTURE_WIDTH = 21;
    private static final int STATUS_EFFECT_BAR_TEXTURE_HEIGHT = 3;

    private static final Map<RegistryEntry<StatusEffect>, Identifier> STATUS_EFFECT_TEXTURE_MAP = new HashMap<>();

    private final EffectSettings effectSettings;
    public int size;
    private int sameTypeGap;
    private int iconInfoGap;
    private boolean drawVertical;
    private boolean drawTimer;
    private boolean drawHidden;
    private boolean combineBackground;

    private HUDDisplayMode displayMode;

    private final List<String> effectDurationStrings = new ArrayList<>();
    private final List<Identifier> effectTextures = new ArrayList<>();
    private final List<Integer> effectWidths = new ArrayList<>();
    private final List<Integer> effectColors = new ArrayList<>();
    private final List<Float> effectAlphas = new ArrayList<>();

    private final List<Integer> effectSteps = new ArrayList<>();
    private final List<Boolean> effectAmbients = new ArrayList<>();
    private final List<String> effectAmplifiers = new ArrayList<>();

    public AbstractEffectHUD(EffectSettings effectSettings) {
        super(effectSettings.base);
        this.effectSettings = effectSettings;
    }

    public abstract boolean isEffectAllowedToRender(RegistryEntry<StatusEffect> registryEntry);

    @Override
    public boolean shouldRender() {
        return super.shouldRender();
    }

    @Override
    public boolean collectHUDInformation() {
        if (CLIENT.player == null) return false;

        effectDurationStrings.clear();
        effectColors.clear();
        effectTextures.clear();
        effectWidths.clear();
        effectAlphas.clear();

        effectSteps.clear();
        effectAmbients.clear();
        effectAmplifiers.clear();

        drawTimer = SETTINGS.drawTimer;
        drawHidden = SETTINGS.drawHidden;
        combineBackground = effectSettings.combineBackground;

        if (drawTimer)
            return collectTimerHUDInformation();
        else
            return collectBarHUDInformation();
    }

    public boolean collectBarHUDInformation() {

        size = 0;
        iconInfoGap = Math.min(HUD_SETTINGS.iconInfoGap, 1);

        for (StatusEffectInstance instance : CLIENT.player.getStatusEffects()) {
            if ((instance.shouldShowIcon() || drawHidden) && isEffectAllowedToRender(instance.getEffectType())) {
                ++size;

                StatusEffectAttribute statusEffectAttribute = StatusEffectAttribute.getStatusEffectAttribute(instance);

                Identifier effectTexture = getStatusEffectTexture(instance.getEffectType());

                boolean ambient = instance.isAmbient();

                int duration = instance.getDuration();
                int maxDuration = statusEffectAttribute.maxDuration();
                int step = instance.isInfinite() ? 7 : Helper.getStep(duration, maxDuration, 7);

                int color = getStatusEffectColor(instance, statusEffectAttribute) | 0xFF000000;
                float alpha = getStatusEffectAlpha(instance);

                int amplifier = instance.getAmplifier() + 1;
                String amplifierStr = amplifier <= 1 ? "" : Helper.toSubscript(Integer.toString(amplifier));

                effectTextures.add(effectTexture);
                effectSteps.add(step);
                effectColors.add(color);
                effectAlphas.add(alpha);
                effectAmplifiers.add(amplifierStr);
                effectAmbients.add(ambient);
            }
        }


        if (size == 0) return false;

        int width = getDynamicWidth(size);
        int height = getDynamicHeight(size);

        drawVertical = effectSettings.drawVertical;
        sameTypeGap = getSameTypeGap();

        setWidthHeight(width, height);

        return true;
    }

    public boolean collectTimerHUDInformation() {
        drawVertical = effectSettings.drawVertical;
        sameTypeGap = effectSettings.sameTypeGap;
        displayMode = effectSettings.base.displayMode;

        int width = -sameTypeGap, height = -sameTypeGap;
        for (StatusEffectInstance instance : CLIENT.player.getStatusEffects()) {
            if ((instance.shouldShowIcon() || drawHidden) && isEffectAllowedToRender(instance.getEffectType())) {

                StatusEffectAttribute statusEffectAttribute = StatusEffectAttribute.getStatusEffectAttribute(instance);
                int duration = instance.getDuration();
                String effectTimeStr = buildEffectDurationString(duration);
                Identifier effectTexture = getStatusEffectTexture(instance.getEffectType());
                int effectWidth = CLIENT.textRenderer.getWidth(effectTimeStr) - 1;
                int totalInstanceWidth = displayMode.calculateWidth(13, effectWidth);

                int color = getStatusEffectColor(instance, statusEffectAttribute) | 0xFF000000;
                float alpha = getStatusEffectAlpha(instance);

                effectDurationStrings.add(effectTimeStr);
                effectTextures.add(effectTexture);
                effectWidths.add(totalInstanceWidth);
                effectColors.add(color);
                effectAlphas.add(alpha);

                if (drawVertical) {
                    width = Math.max(totalInstanceWidth, width);
                    height += 13 + sameTypeGap;
                } else {
                    height = 13;
                    width += sameTypeGap + totalInstanceWidth;
                }
            }
        }

        size = effectDurationStrings.size();
        if (effectDurationStrings.isEmpty()) return false;

        setWidthHeight(width, height);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        if (CLIENT.player == null) return false;
        if (size == 0) return false;

        if (drawBackground && combineBackground) {
            RenderUtils.fillRounded(context, x, y, x + getWidth(), y + getHeight(), 0x80000000);
            drawBackground = false; // don't allow instances to draw background if they're combined.
        }

        if (drawTimer)
            return renderTimerHUD(context, x, y, drawBackground, drawTextShadow);
        else
            return renderBarHUD(context, x, y, drawBackground);
    }

    public boolean renderBarHUD(DrawContext context, int x, int y, boolean drawBackground) {

        for (int i = 0 ; i < size; ++i) {

            drawStatusEffectBarHUD(
                    context,
                    x, y,
                    effectTextures.get(i),
                    effectColors.get(i),
                    effectAlphas.get(i),
                    effectSteps.get(i),
                    effectAmplifiers.get(i),
                    effectAmbients.get(i),
                    drawBackground
            );

            if (drawVertical) {
                y += sameTypeGap;
            } else {
                x += sameTypeGap;
            }
        }

        return true;
    }

    public boolean renderTimerHUD(DrawContext context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

        for (int i = 0; i < size; ++i) {
            drawStatusEffectTimerHUD(
                    context,
                    x, y,
                    effectTextures.get(i),
                    effectDurationStrings.get(i),
                    effectWidths.get(i), 13,
                    effectColors.get(i),
                    ColorHelper.getWhite(effectAlphas.get(i)),
                    drawBackground,
                    drawTextShadow
            );

            if (drawVertical) {
                y += 13 + sameTypeGap;
            } else {
                x += effectWidths.get(i) + sameTypeGap;
            }
        }

        return true;
    }

    public boolean drawStatusEffectBarHUD(DrawContext context, int x, int y, Identifier effectTexture, int color, float alpha, int step, String amplifier, boolean isAmbient, boolean drawBackground) {
        int gap = iconInfoGap;

        if (drawBackground) {
            if (gap <= 0) {
                if (isAmbient) {
                    // evil stretching hack.
                    int iconHeight = ICON_HEIGHT + INFO_HEIGHT;
                    int stretchHeight = ICON_HEIGHT + gap + INFO_HEIGHT;
                    float uvScale = (float)stretchHeight / iconHeight;

                    RenderUtils.drawTextureHUD(
                            context,
                            STATUS_EFFECT_AMBIENT_COMBINED_TEXTURE,
                            x, y,
                            0.0F, 0.0F,
                            ICON_WIDTH, stretchHeight,
                            ICON_WIDTH, (int)(iconHeight * uvScale),
                            color
                    );
                } else {
                    RenderUtils.fillRounded(context, x, y, x + ICON_WIDTH, y + ICON_HEIGHT + gap + INFO_HEIGHT, 0x80000000);
                }
            } else {
                RenderUtils.drawTextureHUD(
                        context,
                        isAmbient ? STATUS_EFFECT_AMBIENT_TEXTURE : STATUS_EFFECT_TEXTURE,
                        x, y,
                        0.0F, 0.0F,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT,
                        STATUS_EFFECT_TEXTURE_WIDTH, STATUS_EFFECT_TEXTURE_HEIGHT,
                        isAmbient ? color : 0xFFFFFFFF
                );
            }
        }

        // draw timer bar
        RenderUtils.drawTextureHUD(
                context,
                STATUS_EFFECT_BAR_BACKGROUND_TEXTURE,
                x + 2, y + ICON_HEIGHT + gap + 2,
                0, 0,
                STATUS_EFFECT_BAR_TEXTURE_WIDTH, STATUS_EFFECT_BAR_TEXTURE_HEIGHT,
                STATUS_EFFECT_BAR_TEXTURE_WIDTH, STATUS_EFFECT_BAR_TEXTURE_HEIGHT
        );
        RenderUtils.drawTextureHUD(
                context,
                STATUS_EFFECT_BAR_TEXTURE,
                x + 2, y + ICON_HEIGHT + gap + 2,
                0, 0,
                3 * step, STATUS_EFFECT_BAR_TEXTURE_HEIGHT,
                STATUS_EFFECT_BAR_TEXTURE_WIDTH, STATUS_EFFECT_BAR_TEXTURE_HEIGHT,
                color
        );

        // draw effect texture.
        RenderUtils.drawTextureHUD(
                context,
                effectTexture,
                x + 3, y + 3,
                0,0,
                18, 18,
                18,18,
                ColorHelper.getWhite(alpha)
        );

        if (amplifier.isEmpty()) return true;

        RenderUtils.drawTextHUD(
                context,
                amplifier,
                x + 3 + 18 - CLIENT.textRenderer.getWidth(amplifier) + 1, y + 2 + 18 - 7,
                0xFFFFFFFF,
                true
        );

        return true;
    }

    public void drawStatusEffectTimerHUD(DrawContext context, int x, int y, Identifier effectTexture, String timeStr, int width, int height, int textColor, int iconColor, boolean drawBackground, boolean drawTextShadow) {

        // shrink the texture from 18x18 to 9x9.
        int from = 18;
        int to = 9;
        float rat = (float) to / from;

        drawSmallHUD(
                context,
                timeStr,
                x, y,
                width, height,
                effectTexture,
                0.0F, 0.0F,
                to, to,
                (int) (from * rat), (int) (from * rat),
                textColor, iconColor,
                displayMode,
                drawBackground,
                drawTextShadow
        );
    }

    public static boolean drawSmallHUD(DrawContext context, String infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (infoText == null || iconTexture == null || displayMode == null) return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        int iconXOffset = 2, iconYOffset = 2;

        switch (displayMode) {
            case ICON ->  {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + 13, y + 13, 0x80000000);
                RenderUtils.drawTextureHUD(context, iconTexture, x + iconXOffset, y + iconYOffset, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
            }
            case INFO ->  {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + width, y + height, 0x80000000);
                RenderUtils.drawTextHUD(context, infoText, x + padding, y + 3, color, drawTextShadow);
            }
            case BOTH ->  {
                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(context, x, y, x + width, y + height, 0x80000000);
                    else {
                        RenderUtils.fillRoundedLeftSide(context, x, y, x + 13, y + height, 0x80000000);
                        RenderUtils.fillRoundedRightSide(context, x + 13 + gap, y, x + width, y + height, 0x80000000);
                    }
                }
                RenderUtils.drawTextureHUD(context, iconTexture, x + iconXOffset, y + iconYOffset, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
                RenderUtils.drawTextHUD(context, infoText, x + 13 + gap + padding, y + 3, color, drawTextShadow);
            }
        }

        return true;
    }

    public int getDynamicWidth(int size) {
        // if we draw the HUD vertically, essentially the width should be the texture width
        return effectSettings.drawVertical ? STATUS_EFFECT_TEXTURE_WIDTH
                // else, the width should be the whole column of Effect HUDs.
                : (size * getSameTypeGap()) - effectSettings.sameTypeGap;
    }

    public int getDynamicHeight(int size) {
        // if the HUD is drawn Vertically, the Height should be the whole row of Effect HUDs
        return effectSettings.drawVertical ? (size * getSameTypeGap()) - effectSettings.sameTypeGap
                // else, the height is just the same as the texture height.
                : getInstanceHeight();
    }

    public int getInstanceHeight() {
        return ICON_HEIGHT + iconInfoGap + INFO_HEIGHT;
    }

    public int getSameTypeGap() {
        return (effectSettings.drawVertical ? getInstanceHeight(): STATUS_EFFECT_TEXTURE_WIDTH) + effectSettings.sameTypeGap;
    }

    public int getStatusEffectColor(StatusEffectInstance instance, StatusEffectAttribute attribute) {
        if (instance.isInfinite())
            return SETTINGS.infiniteColor;
        else if (instance.isAmbient())
            return SETTINGS.ambientColor;
        else {
            return switch (SETTINGS.getColorMode()) {
                case CUSTOM -> effectSettings.customColor;
                case EFFECT -> instance.getEffectType().value().getColor();
                case DYNAMIC -> Helper.getItemBarColor(instance.getDuration(), attribute.maxDuration());
            };
        }
    }

    public float getStatusEffectAlpha(StatusEffectInstance instance) {
        int duration = instance.getDuration();
        float alpha = 1.0f;

        if (duration <= 200 && !instance.isInfinite()) { // minecraft's status effect blinking.
            int n = 10 - duration / 20;
            alpha = MathHelper.clamp((float)duration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)duration * (float)Math.PI / 5.0F) * MathHelper.clamp((float)n / 10.0F * 0.25F, 0.0F, 0.25F);
            alpha = MathHelper.clamp(alpha, 0.0F, 1.0F);
        }

        return alpha;
    }

    public String buildEffectDurationString(int duration) {
        if (duration == -1) return "--:--";

        String l = "", r = "";

        int seconds = duration / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        if (hours > 99) return "--:--";
        else if (hours > 0) {

            if (hours < 10) l = "0";
            l += hours;

            minutes %= 60;
            if (minutes < 10) r = "0";
            r += minutes;

        } else {

            if (minutes < 10) l = "0";
            l += minutes;

            seconds %= 60;
            if (seconds < 10) r = "0";
            r += seconds;
        }


        return l + ':' + r;
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
