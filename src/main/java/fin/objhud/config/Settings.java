package fin.objhud.config;

import fin.objhud.Helper;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config.Gui.Background("cloth-config2:transparent")
@Config(name = "objective-hud")
public class Settings implements ConfigData {

    @ConfigEntry.Category("armor")
    @ConfigEntry.Gui.TransitiveObject
    public ArmorSettings armorSettings = new ArmorSettings();
    public static class ArmorSettings {
        @Comment("Toggle Armor HUD")
        public boolean renderArmorHUD = true;

        @Comment("Armor HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

        @Comment("Armor HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY defY = Helper.ScreenLocationY.MIDDLE;

        public int x = 10;
        public int y = -24;
    }

    @ConfigEntry.Category("coord")
    @ConfigEntry.Gui.TransitiveObject
    public CoordSettings coordSettings = new CoordSettings();
    public static class CoordSettings {
        @Comment("Toggle Coordinate HUD")
        public boolean renderCoordinateHUD = true;

        @Comment("Coordinate HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

        @Comment("Coordinate HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY defY = Helper.ScreenLocationY.UPPER;

        public int x = 10;
        public int y = 10;

        @ConfigEntry.Gui.CollapsibleObject
        public CoordColorSettings color = new CoordColorSettings();
        public static class CoordColorSettings {
            @ConfigEntry.ColorPicker
            public int X = 0xFC7871;
            @ConfigEntry.ColorPicker
            public int Y = 0xA6F1AF;
            @ConfigEntry.ColorPicker
            public int Z = 0x6CE1FC;
        }
    }

    @ConfigEntry.Category("fps")
    @ConfigEntry.Gui.TransitiveObject
    public FPSSettings fpsSettings = new FPSSettings();
    public static class FPSSettings {
        @Comment("Toggle FPS HUD")
        public boolean renderFPSHUD = true;

        @Comment("FPS HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

        @Comment("FPS HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY defY = Helper.ScreenLocationY.UPPER;

        public int x = 80;
        public int y = 10;

        @ConfigEntry.ColorPicker
        public int color = 0xE5ECf8;
    }

    @ConfigEntry.Category("ping")
    @ConfigEntry.Gui.TransitiveObject
    public PingSettings pingSettings = new PingSettings();
    public static class PingSettings {
        @Comment("Toggle Ping HUD")
        public boolean renderPingHUD = true;

        @Comment("Ping HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

        @Comment("Ping HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY defY = Helper.ScreenLocationY.UPPER;

        public int x = 80;
        public int y = 24;

        @Comment("Ping update interval, in seconds.")
        public double updateInterval = 5.0;

        @ConfigEntry.Gui.CollapsibleObject
        public PingColorSettings color = new PingColorSettings();
        public static class PingColorSettings {
            @ConfigEntry.ColorPicker
            public int first = 0x85F290;
            @ConfigEntry.ColorPicker
            public int second = 0xECF285;
            @ConfigEntry.ColorPicker
            public int third = 0xFEBC49;
            @ConfigEntry.ColorPicker
            public int fourth = 0xFF5C71;
        }
    }

    @ConfigEntry.Category("clock")
    @ConfigEntry.Gui.TransitiveObject
    public ClockSettings clockSettings = new ClockSettings();
    public static class ClockSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public ClockSystemSettings systemSettings = new ClockSystemSettings();
        public static class ClockSystemSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationY defY = Helper.ScreenLocationY.UNDER;

            public int x = 10;
            public int y = -23;

            @ConfigEntry.ColorPicker
            public int color = 0xFFFFFF;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public ClockInGameSettings inGameSettings = new ClockInGameSettings();
        public static class ClockInGameSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationX defX = Helper.ScreenLocationX.LEFT;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationY defY = Helper.ScreenLocationY.UNDER;

            public int x = 64;
            public int y = -23;

            @ConfigEntry.Gui.CollapsibleObject
            public ClockInGameColorSettings color = new ClockInGameColorSettings();
            public static class ClockInGameColorSettings {
                @ConfigEntry.ColorPicker
                public int day = 0xfff9b5;
                @ConfigEntry.ColorPicker
                public int night = 0xd6cbef;
                @ConfigEntry.ColorPicker
                public int rain = 0xb5d0e8;
                @ConfigEntry.ColorPicker
                public int thunder = 0x8faecb;
            }
        }
    }

}


