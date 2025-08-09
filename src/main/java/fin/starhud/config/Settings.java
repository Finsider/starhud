package fin.starhud.config;

import fin.starhud.config.hud.*;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config.Gui.Background("cloth-config2:transparent")
@Config(name = "starhud")
public class Settings implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralSettings generalSettings = new GeneralSettings();

    @ConfigEntry.Category("hudList")
    @ConfigEntry.Gui.TransitiveObject
    public HUDList hudList = new HUDList();

    @ConfigEntry.Category("armor")
    @ConfigEntry.Gui.TransitiveObject
    public Armor armorSettings = new Armor();

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
    }

    @ConfigEntry.Category("fps")
    @ConfigEntry.Gui.TransitiveObject
    public FPSSettings fpsSettings = new FPSSettings();

    @ConfigEntry.Category("tps")
    @ConfigEntry.Gui.TransitiveObject
    public TPSSettings tpsSettings = new TPSSettings();

    @ConfigEntry.Category("coord")
    @ConfigEntry.Gui.TransitiveObject
    public Coord coordSettings = new Coord();

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
    }

    @ConfigEntry.Category("direction")
    @ConfigEntry.Gui.TransitiveObject
    public DirectionSettings directionSettings = new DirectionSettings();

    @ConfigEntry.Category("ping")
    @ConfigEntry.Gui.TransitiveObject
    public PingSettings pingSettings = new PingSettings();

    @ConfigEntry.Category("clock")
    @ConfigEntry.Gui.TransitiveObject
    public Clock clockSettings = new Clock();

    public static class Clock {
        @ConfigEntry.Gui.CollapsibleObject
        public ClockSystemSettings systemSetting = new ClockSystemSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public ClockInGameSettings inGameSetting = new ClockInGameSettings();
    }

    @ConfigEntry.Category("day")
    @ConfigEntry.Gui.TransitiveObject
    public DaySettings daySettings = new DaySettings();

    @ConfigEntry.Category("biome")
    @ConfigEntry.Gui.TransitiveObject
    public BiomeSettings biomeSettings = new BiomeSettings();

    @ConfigEntry.Category("inventory")
    @ConfigEntry.Gui.TransitiveObject
    public InventorySettings inventorySettings = new InventorySettings();

    @ConfigEntry.Category("hand")
    @ConfigEntry.Gui.TransitiveObject
    public Hand handSettings = new Hand();

    public static class Hand {
        @ConfigEntry.Gui.CollapsibleObject
        public HandSettings leftHandSettings = new HandSettings(true, -96, -25, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP,0xffb3b3);

        @ConfigEntry.Gui.CollapsibleObject
        public HandSettings rightHandSettings = new HandSettings(true, 96, -25, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP, 0x87ceeb);
    }

    @ConfigEntry.Category("effect")
    @ConfigEntry.Gui.TransitiveObject
    public Effect effectSettings = new Effect();

    public static class Effect {
        @ConfigEntry.Gui.CollapsibleObject
        public EffectSettings positiveSettings = new EffectSettings(new BaseHUDSettings(true, -5, 5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN));

        @ConfigEntry.Gui.CollapsibleObject
        public EffectSettings negativeSettings = new EffectSettings(new BaseHUDSettings(true, -5, 39, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN));
    }

    @ConfigEntry.Category("targeted")
    @ConfigEntry.Gui.TransitiveObject
    public TargetedCrosshairSettings targetedCrosshairSettings = new TargetedCrosshairSettings();

    @ConfigEntry.Category("speed")
    @ConfigEntry.Gui.TransitiveObject
    public SpeedSettings speedSettings = new SpeedSettings();

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
        } else if (helmet.durabilitySettings == null) {
            armorSettings.helmet.durabilitySettings = new DurabilitySettings();
        }

        ArmorSettings chestplate = armorSettings.chestplate;
        if (chestplate.base == null) {
            armorSettings.chestplate = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        } else if (chestplate.durabilitySettings == null) {
            armorSettings.chestplate.durabilitySettings = new DurabilitySettings();
        }

        ArmorSettings leggings = armorSettings.leggings;
        if (leggings.base == null) {
            armorSettings.leggings = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14 * 2, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        } else if (leggings.durabilitySettings == null) {
            armorSettings.leggings.durabilitySettings = new DurabilitySettings();
        }

        ArmorSettings boots = armorSettings.boots;
        if (boots.base == null) {
            armorSettings.boots = new ArmorSettings(
                    new BaseHUDSettings(true, 5, 14 * 3, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE)
            );
        } else if (boots.durabilitySettings == null) {
            armorSettings.boots.durabilitySettings = new DurabilitySettings();
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
            handSettings.leftHandSettings = new HandSettings(true, -96, -25, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP, 0xffb3b3);
        } else if (leftHand.durabilitySettings == null) {
            handSettings.leftHandSettings.durabilitySettings = new DurabilitySettings();
        }

        HandSettings rightHand = handSettings.rightHandSettings;
        if (rightHand.base == null) {
            handSettings.rightHandSettings = new HandSettings(true, 96, -25, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP, 0x87ceeb);
        } else if (rightHand.durabilitySettings == null) {
            handSettings.rightHandSettings.durabilitySettings = new DurabilitySettings();
        }

        EffectSettings positive = effectSettings.positiveSettings;
        if (positive.base == null)
            effectSettings.positiveSettings = new EffectSettings(new BaseHUDSettings(true, -5, 5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN));

        EffectSettings negative = effectSettings.negativeSettings;
        if (negative.base == null)
            effectSettings.negativeSettings = new EffectSettings(new BaseHUDSettings(true, -5, 39, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN));

        if (targetedCrosshairSettings.base == null)
            targetedCrosshairSettings = new TargetedCrosshairSettings();

        if (speedSettings.base == null)
            speedSettings = new SpeedSettings();

        this.hudList.onConfigSaved();
    }
}