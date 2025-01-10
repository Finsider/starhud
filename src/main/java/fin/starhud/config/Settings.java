package fin.starhud.config;

import fin.starhud.Helper;
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
        public boolean shouldRender = true;

        @Comment("Armor HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

        @Comment("Armor HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY originY = Helper.ScreenLocationY.MIDDLE;

        public int x = 10;
        public int y = -20;

        @ConfigEntry.Gui.CollapsibleObject
        public HelmetSettings helmet = new HelmetSettings();
        public static class HelmetSettings {
            public boolean shouldRender = true;
            public int xOffset = 0;
            public int yOffset = 0;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public ChestplateSettings chestplate = new ChestplateSettings();
        public static class ChestplateSettings {
            public boolean shouldRender = true;
            public int xOffset = 0;
            public int yOffset = 14;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public LeggingsSettings leggings = new LeggingsSettings();
        public static class LeggingsSettings {
            public boolean shouldRender = true;
            public int xOffset = 0;
            public int yOffset = 28;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public BootsSettings boots = new BootsSettings();
        public static class BootsSettings {
            public boolean shouldRender = true;
            public int xOffset = 0;
            public int yOffset = 42;
        }
    }

    @ConfigEntry.Category("coord")
    @ConfigEntry.Gui.TransitiveObject
    public CoordSettings coordSettings = new CoordSettings();
    public static class CoordSettings {
        @Comment("Toggle Coordinate HUD")
        public boolean shouldRender = true;

        @Comment("Coordinate HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

        @Comment("Coordinate HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY originY = Helper.ScreenLocationY.TOP;

        public int x = 10;
        public int y = 10;

        @ConfigEntry.Gui.CollapsibleObject
        public CoordXSettings coordXSettings = new CoordXSettings();
        public static class CoordXSettings {
            public boolean shouldRender = true;
            @Comment("X Offset to origin X location")
            public int xOffset = 0;
            @Comment("Y Offset to origin Y location")
            public int yOffset = 0;
            @ConfigEntry.ColorPicker
            public int color = 0xFC7871;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public CoordYSettings coordYSettings = new CoordYSettings();
        public static class CoordYSettings {
            public boolean shouldRender = true;
            @Comment("X Offset to origin X location")
            public int xOffset = 0;
            @Comment("Y Offset to origin Y location")
            public int yOffset = 14;
            @ConfigEntry.ColorPicker
            public int color = 0xA6F1AF;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public CoordZSettings coordZSettings = new CoordZSettings();
        public static class CoordZSettings {
            public boolean shouldRender = true;
            @Comment("X Offset to origin X location")
            public int xOffset = 0;
            @Comment("Y Offset to origin Y location")
            public int yOffset = 28;
            @ConfigEntry.ColorPicker
            public int color = 0x6CE1FC;
        }
    }

    @ConfigEntry.Category("direction")
    @ConfigEntry.Gui.TransitiveObject
    public DirectionSettings directionSettings = new DirectionSettings();
    public static class DirectionSettings {
        @Comment("Toggle Direction HUD")
        public boolean shouldRender = true;

        @Comment("Direction HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

        @Comment("Direction HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY originY = Helper.ScreenLocationY.TOP;

        public int x = 80;
        public int y = 10;

        @ConfigEntry.Gui.CollapsibleObject
        public DirectionColorSettings directionColor = new DirectionColorSettings();
        public static class DirectionColorSettings {
            @ConfigEntry.ColorPicker
            public int s = 0xffb5b5;
            @ConfigEntry.ColorPicker
            public int sw = 0xffcbb3;
            @ConfigEntry.ColorPicker
            public int w = 0xffd1b7;
            @ConfigEntry.ColorPicker
            public int nw = 0xd8cae8;
            @ConfigEntry.ColorPicker
            public int n = 0xb7c9e9;
            @ConfigEntry.ColorPicker
            public int ne = 0xd4dbf0;
            @ConfigEntry.ColorPicker
            public int e = 0xffe5b4;
            @ConfigEntry.ColorPicker
            public int se = 0xffd0c4;
        }
    }

    @ConfigEntry.Category("fps")
    @ConfigEntry.Gui.TransitiveObject
    public FPSSettings fpsSettings = new FPSSettings();
    public static class FPSSettings {
        @Comment("Toggle FPS HUD")
        public boolean shouldRender = true;

        @Comment("FPS HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

        @Comment("FPS HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY originY = Helper.ScreenLocationY.TOP;

        public int x = 80;
        public int y = 24;

        @ConfigEntry.ColorPicker
        public int color = 0xE5ECf8;
    }

    @ConfigEntry.Category("ping")
    @ConfigEntry.Gui.TransitiveObject
    public PingSettings pingSettings = new PingSettings();
    public static class PingSettings {
        @Comment("Toggle Ping HUD")
        public boolean shouldRender = true;

        @Comment("Ping HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

        @Comment("Ping HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenLocationY originY = Helper.ScreenLocationY.TOP;

        public int x = 80;
        public int y = 38;

        @Comment("Ping update interval, in seconds.")
        public double updateInterval = 5.0;

        @ConfigEntry.Gui.CollapsibleObject
        public PingColorSettings pingColor = new PingColorSettings();
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
            public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationY originY = Helper.ScreenLocationY.BOTTOM;

            public int x = 10;
            public int y = -10;

            @ConfigEntry.ColorPicker
            public int color = 0xFFFFFF;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public ClockInGameSettings inGameSettings = new ClockInGameSettings();
        public static class ClockInGameSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationX originX = Helper.ScreenLocationX.LEFT;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenLocationY originY = Helper.ScreenLocationY.BOTTOM;

            public int x = 64;
            public int y = -10;

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