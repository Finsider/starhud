package fin.starhud.config;

import fin.starhud.Helper;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Background("cloth-config2:transparent")
@Config(name = "starhud")
public class Settings extends PartitioningSerializer.GlobalData {

    @Config(name = "base")
    public static class BaseSettings implements ConfigData {
        public boolean shouldRender;

        @EnumHandler(option = EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentX originX;
        @EnumHandler(option = EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY;

        public int x;
        public int y;

        @Comment("Set to 0 for default HUD Scale")
        @BoundedDiscrete(max = 6)
        public int scale;

        @CollapsibleObject
        public HideOn hideOn;

        public BaseSettings(Helper.ScreenAlignmentX originX, Helper.ScreenAlignmentY originY, int x, int y) {
            this.shouldRender = true;
            this.originX = originX;
            this.originY = originY;
            this.x = x;
            this.y = y;
            this.scale = 0;
            this.hideOn = new HideOn();
        }

        public BaseSettings(boolean shouldRender, Helper.ScreenAlignmentX originX, Helper.ScreenAlignmentY originY, int x, int y, int scale) {
            this.shouldRender = shouldRender;
            this.originX = originX;
            this.originY = originY;
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.hideOn = new HideOn();
        }
    }

    public static class HideOn {
        public boolean f3;
        public boolean chat;

        public HideOn() {
            this.f3 = false;
            this.chat = false;
        }
    }

    @Category("armor")
    @TransitiveObject
    public ArmorSettings armorSettings = new ArmorSettings();
    @Config(name = "armor")
    public static class ArmorSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.LEFT, Helper.ScreenAlignmentY.MIDDLE, 5, -20);

        @CollapsibleObject
        public BaseArmorSettings helmet = new BaseArmorSettings(true, 0 , 0);

        @CollapsibleObject
        public BaseArmorSettings chestplate = new BaseArmorSettings(true, 0 , 14);

        @CollapsibleObject
        public BaseArmorSettings leggings = new BaseArmorSettings(true, 0 , 28);

        @CollapsibleObject
        public BaseArmorSettings boots = new BaseArmorSettings(true, 0 , 42);

        public static class BaseArmorSettings {
            public boolean shouldRender;
            @Comment("X Offset to origin X location")
            public int xOffset;
            @Comment("Y Offset to origin Y location")
            public int yOffset;

            public BaseArmorSettings(boolean shouldRender, int xOffset, int yOffset) {
                this.shouldRender = shouldRender;
                this.xOffset = xOffset;
                this.yOffset = yOffset;
            }
        }
    }

    @Category("coord")
    @TransitiveObject
    public CoordSettings coordSettings = new CoordSettings();

    @Config(name = "coord")
    public static class CoordSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.LEFT, Helper.ScreenAlignmentY.TOP, 5, 5);

        @CollapsibleObject
        public BaseCoordSettings coordXSettings = new BaseCoordSettings(true, 0, 0, 0xFC7871);

        @CollapsibleObject
        public BaseCoordSettings coordYSettings = new BaseCoordSettings(true, 0, 14, 0xA6F1AF);

        @CollapsibleObject
        public BaseCoordSettings coordZSettings = new BaseCoordSettings(true, 0, 28, 0x6CE1FC);

        public static class BaseCoordSettings {
            public boolean shouldRender;

            @Comment("X Offset to origin X location")
            public int xOffset;
            @Comment("Y Offset to origin Y location")
            public int yOffset;
            @ColorPicker
            public int color;

            public BaseCoordSettings(boolean shouldRender, int xOffset, int yOffset, int color) {
                this.shouldRender = shouldRender;
                this.xOffset = xOffset;
                this.yOffset = yOffset;
                this.color = color;
            }
        }
    }

    @Category("direction")
    @TransitiveObject
    public DirectionSettings directionSettings = new DirectionSettings();
    @Config(name = "direction")
    public static class DirectionSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.CENTER, Helper.ScreenAlignmentY.TOP, 26, 19);

        public boolean includeOrdinal = false;

        @CollapsibleObject
        public DirectionColorSettings directionColor = new DirectionColorSettings();
        public static class DirectionColorSettings {
            @ColorPicker
            public int s = 0xffb5b5;
            @ColorPicker
            public int sw = 0xffcbb3;
            @ColorPicker
            public int w = 0xffd1b7;
            @ColorPicker
            public int nw = 0xd8cae8;
            @ColorPicker
            public int n = 0xb7c9e9;
            @ColorPicker
            public int ne = 0xd4dbf0;
            @ColorPicker
            public int e = 0xffe5b4;
            @ColorPicker
            public int se = 0xffd0c4;
        }
    }

    @Category("fps")
    @TransitiveObject
    public FPSSettings fpsSettings = new FPSSettings();

    @Config(name = "fps")
    public static class FPSSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.LEFT, Helper.ScreenAlignmentY.BOTTOM, 5, -5);

        @ColorPicker
        public int color = 0xE5ECf8;
    }

    @Category("ping")
    @TransitiveObject
    public PingSettings pingSettings = new PingSettings();
    @Config(name = "ping")
    public static class PingSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.RIGHT, Helper.ScreenAlignmentY.BOTTOM, -57, -5);

        @Comment("Ping update interval, in seconds.")
        public double updateInterval = 5.0;

        @CollapsibleObject
        public PingColorSettings pingColor = new PingColorSettings();
        public static class PingColorSettings {
            @ColorPicker
            public int first = 0x85F290;
            @ColorPicker
            public int second = 0xECF285;
            @ColorPicker
            public int third = 0xFEBC49;
            @ColorPicker
            public int fourth = 0xFF5C71;
        }

    }

    @Category("clock")
    @TransitiveObject
    public ClockSettings clockSettings = new ClockSettings();
    @Config(name = "clock")
    public static class ClockSettings implements ConfigData {
        @CollapsibleObject
        public ClockSystemSettings systemSettings = new ClockSystemSettings();
        public static class ClockSystemSettings {

            @TransitiveObject
            public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.RIGHT, Helper.ScreenAlignmentY.BOTTOM, -5, -5);

            @TransitiveObject
            public BaseClockSettings clock = new BaseClockSettings();

            @ColorPicker
            public int color = 0xFFFFFF;
        }

        @CollapsibleObject
        public ClockInGameSettings inGameSettings = new ClockInGameSettings();
        public static class ClockInGameSettings {

            @TransitiveObject
            public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.CENTER, Helper.ScreenAlignmentY.TOP, -29, 19);

            @TransitiveObject
            public BaseClockSettings clock = new BaseClockSettings();

            @CollapsibleObject
            public ClockInGameColorSettings color = new ClockInGameColorSettings();
            public static class ClockInGameColorSettings {
                @ColorPicker
                public int day = 0xfff9b5;
                @ColorPicker
                public int night = 0xd6cbef;
                @ColorPicker
                public int rain = 0xb5d0e8;
                @ColorPicker
                public int thunder = 0x8faecb;
            }
        }

        public static class BaseClockSettings {
            public boolean use12Hour = false;
        }
    }

    @Category("biome")
    @TransitiveObject
    public BiomeSettings biomeSettings = new BiomeSettings();
    @Config(name = "biome")
    public static class BiomeSettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.CENTER, Helper.ScreenAlignmentY.TOP, 0, 5);

        @Comment("Which way should the HUD goes as the text increases?")
        @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.GrowthDirection textGrowth = Helper.GrowthDirection.CENTER;

        @CollapsibleObject
        public DimensionColorSettings color = new DimensionColorSettings();
        public static class DimensionColorSettings {
            @ColorPicker
            public int overworld = 0xFFFFFF;
            @ColorPicker
            public int nether = 0xfc7871;
            @ColorPicker
            public int end = 0xc9c7e3;
            @ColorPicker
            public int custom = 0xFFFFFF;
        }
    }

    @Category("inventory")
    @TransitiveObject
    public InventorySettings inventorySettings = new InventorySettings();
    @Config(name = "inventory")
    public static class InventorySettings implements ConfigData {

        @TransitiveObject
        public BaseSettings base = new BaseSettings(false, Helper.ScreenAlignmentX.RIGHT, Helper.ScreenAlignmentY.MIDDLE, -5, 0, 1);

        public boolean drawVertical = true;
    }

    @Category("hand")
    @TransitiveObject
    public HandSettings handSettings = new HandSettings();
    @Config(name = "hand")
    public static class HandSettings implements ConfigData {
        @CollapsibleObject
        public LeftHandSettings leftHandSettings = new LeftHandSettings();
        public static class LeftHandSettings {

            @TransitiveObject
            public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.CENTER, Helper.ScreenAlignmentY.BOTTOM, -108, -25);

            @TransitiveObject
            public BaseHandSettings hand = new BaseHandSettings(Helper.GrowthDirection.LEFT, true, true, 0xffb3b3);

        }

        @CollapsibleObject
        public RightHandSettings rightHandSettings = new RightHandSettings();
        public static class RightHandSettings {

            @TransitiveObject
            public BaseSettings base = new BaseSettings(Helper.ScreenAlignmentX.CENTER, Helper.ScreenAlignmentY.BOTTOM, 108, -25);

            @TransitiveObject
            public BaseHandSettings hand = new BaseHandSettings(Helper.GrowthDirection.RIGHT, true, true, 0x87ceeb);

        }

        public static class BaseHandSettings {
            @Comment("Which way should the HUD goes when a the texture increases?")
            @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.GrowthDirection textureGrowth;

            public boolean showCount;
            public boolean showDurability;
            @ColorPicker
            public int color;

            public BaseHandSettings(Helper.GrowthDirection textureGrowth, boolean showCount, boolean showDurability, int color) {
                this.textureGrowth = textureGrowth;
                this.showCount = showCount;
                this.showDurability = showDurability;
                this.color = color;
            }
        }
    }
}