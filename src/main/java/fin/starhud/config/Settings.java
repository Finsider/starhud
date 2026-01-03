package fin.starhud.config;

import fin.starhud.config.hud.DurabilitySettings;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.config.hud.clock.ClockInGameSettings;
import fin.starhud.config.hud.clock.ClockSystemSettings;
import fin.starhud.config.hud.coordinate.CoordSettings;
import fin.starhud.config.hud.hand.HandSettings;
import fin.starhud.config.hud.other.*;
import fin.starhud.config.hud.statuseffect.EffectSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config.Gui.Background("cloth-config2:transparent")
@Config(name = "starhud")
public class Settings implements ConfigData{

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralSettings generalSettings = new GeneralSettings();

    @ConfigEntry.Category("hudList")
    @ConfigEntry.Gui.TransitiveObject
    public HUDList hudList = new HUDList();

    @ConfigEntry.Category("fps")
    @ConfigEntry.Gui.TransitiveObject
    public FPSSettings fpsSettings = new FPSSettings();

    @ConfigEntry.Category("armor")
    @ConfigEntry.Gui.TransitiveObject
    public Armor armorSettings = new Armor();

    @ConfigEntry.Category("hand")
    @ConfigEntry.Gui.TransitiveObject
    public Hand handSettings = new Hand();

    @ConfigEntry.Category("coord")
    @ConfigEntry.Gui.TransitiveObject
    public Coord coordSettings = new Coord();

    @ConfigEntry.Category("effect")
    @ConfigEntry.Gui.TransitiveObject
    public Effect effectSettings = new Effect();

    @ConfigEntry.Category("direction")
    @ConfigEntry.Gui.TransitiveObject
    public DirectionSettings directionSettings = new DirectionSettings();

    @ConfigEntry.Category("ping")
    @ConfigEntry.Gui.TransitiveObject
    public PingSettings pingSettings = new PingSettings();

    @ConfigEntry.Category("tps")
    @ConfigEntry.Gui.TransitiveObject
    public TPSSettings tpsSettings = new TPSSettings();

    @ConfigEntry.Category("player_count")
    @ConfigEntry.Gui.TransitiveObject
    public PlayerCountSettings playerCountSettings = new PlayerCountSettings();

    @ConfigEntry.Category("clock")
    @ConfigEntry.Gui.TransitiveObject
    public Clock clockSettings = new Clock();

    @ConfigEntry.Category("combo")
    @ConfigEntry.Gui.TransitiveObject
    public ComboSettings comboSettings = new ComboSettings();

    @ConfigEntry.Category("reach")
    @ConfigEntry.Gui.TransitiveObject
    public ReachSettings reachSettings = new ReachSettings();

    @ConfigEntry.Category("speed")
    @ConfigEntry.Gui.TransitiveObject
    public SpeedSettings speedSettings = new SpeedSettings();

    @ConfigEntry.Category("biome")
    @ConfigEntry.Gui.TransitiveObject
    public BiomeSettings biomeSettings = new BiomeSettings();

    @ConfigEntry.Category("day")
    @ConfigEntry.Gui.TransitiveObject
    public DaySettings daySettings = new DaySettings();

    @ConfigEntry.Category("targeted")
    @ConfigEntry.Gui.TransitiveObject
    public TargetedCrosshairSettings targetedCrosshairSettings = new TargetedCrosshairSettings();

    @ConfigEntry.Category("inventory")
    @ConfigEntry.Gui.TransitiveObject
    public InventorySettings inventorySettings = new InventorySettings();

    public static class Armor {
        @ConfigEntry.Gui.CollapsibleObject
        public ArmorSettings helmet = new ArmorSettings(
                new BaseHUDSettings(true, 5, 0, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
        );

        @ConfigEntry.Gui.CollapsibleObject
        public ArmorSettings chestplate = new ArmorSettings(
                new BaseHUDSettings(true, 5, 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
        );

        @ConfigEntry.Gui.CollapsibleObject
        public ArmorSettings leggings = new ArmorSettings(
                new BaseHUDSettings(true, 5, 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
        );

        @ConfigEntry.Gui.CollapsibleObject
        public ArmorSettings boots = new ArmorSettings(
                new BaseHUDSettings(true, 5, 14 * 3, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
        );

        @ConfigEntry.Gui.TransitiveObject
        public DurabilitySettings durabilitySettings = new DurabilitySettings();
    }

    public static class Coord {
        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings X = new CoordSettings(
                new BaseHUDSettings(true, 5, 5, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0xFc7871
        );

        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings Y = new CoordSettings(
                new BaseHUDSettings(true, 5, 5 + 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0xA6F1AF
        );

        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings Z = new CoordSettings(
                new BaseHUDSettings(true, 5, 5 + 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0x6CE1FC
        );

        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings netherX = new CoordSettings(
                new BaseHUDSettings(false, 5, 100, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0xe89a9a
        );

        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings netherY = new CoordSettings(
                new BaseHUDSettings(false, 5, 100 + 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0xf1ae94
        );

        @ConfigEntry.Gui.CollapsibleObject
        public CoordSettings netherZ = new CoordSettings(
                new BaseHUDSettings(false, 5, 100 + 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                0xf9cba7
        );
    }

    public static class Effect {
        @ConfigEntry.Gui.CollapsibleObject
        public EffectSettings positiveSettings = new EffectSettings(
                new BaseHUDSettings(true, -5, 5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN),
                0xCFF5D2
        );

        @ConfigEntry.Gui.CollapsibleObject
        public EffectSettings negativeSettings = new EffectSettings(
                new BaseHUDSettings(true, -5, 39, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN),
                0xDCE8B5
        );

        @Comment("Draw the HUD in a clock like fashion instead of a bars.")
        public boolean drawTimer = false;

        @Comment("Also draw Status Effect that are supposedly Hidden.")
        public boolean drawHidden = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ColorMode colorMode = ColorMode.DYNAMIC;

        @ConfigEntry.ColorPicker
        public int ambientColor = 0xd5feef;

        @ConfigEntry.ColorPicker
        public int infiniteColor = 0xB5D0E8;

        public ColorMode getColorMode() {
            if (colorMode == null) colorMode = ColorMode.DYNAMIC;
            return colorMode;
        }

        public enum ColorMode {
            CUSTOM,
            EFFECT,
            DYNAMIC
        }
    }

    public static class Hand {
        @ConfigEntry.Gui.CollapsibleObject
        public HandSettings leftHandSettings = new HandSettings(true, -12, -120, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP,0xffb3b3);

        @ConfigEntry.Gui.CollapsibleObject
        public HandSettings rightHandSettings = new HandSettings(true, 12, -120, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP, 0x87ceeb);

        @ConfigEntry.Gui.TransitiveObject
        public DurabilitySettings durabilitySettings = new DurabilitySettings();

        public boolean showCount = true;
        public boolean showDurability = true;
    }

    public static class Clock {
        @ConfigEntry.Gui.CollapsibleObject
        public ClockSystemSettings systemSetting = new ClockSystemSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public ClockInGameSettings inGameSetting = new ClockInGameSettings();
    }

    @Override
    public void validatePostLoad() {
        if (generalSettings.inGameSettings == null)
            generalSettings.inGameSettings = new GeneralSettings.InGameHUDSettings();

        if (generalSettings.screenSettings == null)
            generalSettings.screenSettings = new GeneralSettings.EditHUDScreenSettings();

        ArmorSettings helmet = armorSettings.helmet;
        if (helmet.base == null) {
            armorSettings.helmet = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 0, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        }

        ArmorSettings chestplate = armorSettings.chestplate;
        if (chestplate.base == null) {
            armorSettings.chestplate = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        }

        ArmorSettings leggings = armorSettings.leggings;
        if (leggings.base == null) {
            armorSettings.leggings = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        }

        ArmorSettings boots = armorSettings.boots;
        if (boots.base == null) {
            armorSettings.boots = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14 * 3, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        }

        if (armorSettings.durabilitySettings == null) {
            armorSettings.durabilitySettings = new DurabilitySettings();
        }

        if (fpsSettings.base == null)
            fpsSettings = new FPSSettings();

        if (tpsSettings.base == null)
            tpsSettings = new TPSSettings();

        CoordSettings coordX = coordSettings.X;
        if (coordX.base == null) {
            coordSettings.X = new CoordSettings(
                    new BaseHUDSettings(true, 5, 5, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0xFc7871
            );
        }

        CoordSettings coordY = coordSettings.Y;
        if (coordY.base == null) {
            coordSettings.Y = new CoordSettings(
                    new BaseHUDSettings(true, 5, 5 + 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0xA6F1AF
            );
        }

        CoordSettings coordZ = coordSettings.Z;
        if (coordZ.base == null) {
            coordSettings.Z = new CoordSettings(
                    new BaseHUDSettings(true, 5, 5 + 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0x6CE1FC
            );
        }

        CoordSettings netherCoordX = coordSettings.netherX;
        if (netherCoordX.base == null) {
            coordSettings.netherX = new CoordSettings(
                    new BaseHUDSettings(false, 5, 100, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0xe89a9a
            );
        }

        CoordSettings netherCoordY = coordSettings.netherY;
        if (netherCoordY.base == null) {
            coordSettings.netherY = new CoordSettings(
                    new BaseHUDSettings(false, 5, 100 + 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0xCFF5D2
            );
        }

        CoordSettings netherCoordZ = coordSettings.netherZ;
        if (netherCoordZ.base == null) {
            coordSettings.netherZ = new CoordSettings(
                    new BaseHUDSettings(false, 5, 100 + 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN),
                    0xf9cba7
            );
        }

        if (directionSettings.base == null)
            directionSettings = new DirectionSettings();

        if (pingSettings.base == null)
            pingSettings = new PingSettings();

        ClockSystemSettings systemSetting = clockSettings.systemSetting;
        if (systemSetting.base == null)
            clockSettings.systemSetting = new ClockSystemSettings();

        ClockInGameSettings inGameSetting = clockSettings.inGameSetting;
        if (inGameSetting.base == null)
            clockSettings.inGameSetting = new ClockInGameSettings();

        if (daySettings.base == null)
            daySettings = new DaySettings();

        if (biomeSettings.base == null)
            biomeSettings = new BiomeSettings();

        if (inventorySettings.base == null)
            inventorySettings = new InventorySettings();

        HandSettings leftHand = handSettings.leftHandSettings;
        if (leftHand.base == null) {
            handSettings.leftHandSettings = new HandSettings(true, -12, -120, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP, 0xffb3b3);
        }

        HandSettings rightHand = handSettings.rightHandSettings;
        if (rightHand.base == null) {
            handSettings.rightHandSettings = new HandSettings(true, 12, -120, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP, 0x87ceeb);
        }

        if (handSettings.durabilitySettings == null) {
            handSettings.durabilitySettings = new DurabilitySettings();
        }

        EffectSettings positive = effectSettings.positiveSettings;
        if (positive.base == null)
            effectSettings.positiveSettings = new EffectSettings(
                    new BaseHUDSettings(true, -5, 5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN),
                    0xAEEDE6
            );

        EffectSettings negative = effectSettings.negativeSettings;
        if (negative.base == null)
            effectSettings.negativeSettings = new EffectSettings(
                    new BaseHUDSettings(true, -5, 39, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN),
                    0xDCE8B5
            );

        if (targetedCrosshairSettings.base == null)
            targetedCrosshairSettings = new TargetedCrosshairSettings();

        if (speedSettings.base == null)
            speedSettings = new SpeedSettings();

        if (playerCountSettings.base == null)
            playerCountSettings = new PlayerCountSettings();

        if (comboSettings.base == null)
            comboSettings = new ComboSettings();

        if (reachSettings.base == null)
            reachSettings = new ReachSettings();

        this.hudList.onConfigSaved();
    }
}