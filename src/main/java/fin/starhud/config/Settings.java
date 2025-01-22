package fin.starhud.config;

import fin.starhud.Helper;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config.Gui.Background("cloth-config2:transparent")
@Config(name = "starhud")
public class Settings implements ConfigData {

    @ConfigEntry.Category("armor")
    @ConfigEntry.Gui.TransitiveObject
    public ArmorSettings armorSettings = new ArmorSettings();
    public static class ArmorSettings {
        @Comment("Toggle Armor HUD")
        public boolean shouldRender = true;

        @Comment("Armor HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.LEFT;

        @Comment("Armor HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.MIDDLE;

        public int x = 5;
        public int y = -20;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

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

        @ConfigEntry.Gui.CollapsibleObject
        public ArmorHideSettings hideOn = new ArmorHideSettings();
        public static class ArmorHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
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
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.LEFT;

        @Comment("Coordinate HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.TOP;

        public int x = 5;
        public int y = 5;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

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

        @ConfigEntry.Gui.CollapsibleObject
        public CoordHideSettings hideOn = new CoordHideSettings();
        public static class CoordHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
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
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.CENTER;

        @Comment("Direction HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.TOP;

        public int x = 26;
        public int y = 19;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

        public boolean includeOrdinal = false;

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

        @ConfigEntry.Gui.CollapsibleObject
        public DirectionHideSettings hideOn = new DirectionHideSettings();
        public static class DirectionHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
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
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.LEFT;

        @Comment("FPS HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.BOTTOM;

        public int x = 5;
        public int y = -5;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

        @ConfigEntry.ColorPicker
        public int color = 0xE5ECf8;

        @ConfigEntry.Gui.CollapsibleObject
        public FPSHideSettings hideOn = new FPSHideSettings();
        public static class FPSHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
        }
    }

    @ConfigEntry.Category("ping")
    @ConfigEntry.Gui.TransitiveObject
    public PingSettings pingSettings = new PingSettings();
    public static class PingSettings {
        @Comment("Toggle Ping HUD")
        public boolean shouldRender = true;

        @Comment("Ping HUD default Horizontal location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.RIGHT;

        @Comment("Ping HUD default Vertical location")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.BOTTOM;

        public int x = -57;
        public int y = -5;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

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

        @ConfigEntry.Gui.CollapsibleObject
        public PingHideSettings hideOn = new PingHideSettings();
        public static class PingHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
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
            public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.RIGHT;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.BOTTOM;

            public int x = -5;
            public int y = -5;

            @ConfigEntry.BoundedDiscrete(max = 6)
            @Comment("Set to 0 for default GUI Scale")
            public int scale = 0;

            public boolean use12Hour = false;

            @ConfigEntry.ColorPicker
            public int color = 0xFFFFFF;

            @ConfigEntry.Gui.CollapsibleObject
            public ClockSystemHideSettings hideOn = new ClockSystemHideSettings();
            public static class ClockSystemHideSettings {
                public boolean f3 = false;
                public boolean chat = false;
            }
        }

        @ConfigEntry.Gui.CollapsibleObject
        public ClockInGameSettings inGameSettings = new ClockInGameSettings();
        public static class ClockInGameSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.CENTER;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.TOP;

            public int x = -29;
            public int y = 19;

            @ConfigEntry.BoundedDiscrete(max = 6)
            @Comment("Set to 0 for default GUI Scale")
            public int scale = 0;

            public boolean use12Hour = false;

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

            @ConfigEntry.Gui.CollapsibleObject
            public ClockInGameHideSettings hideOn = new ClockInGameHideSettings();
            public static class ClockInGameHideSettings {
                public boolean f3 = false;
                public boolean chat = false;
            }
        }
    }

    @ConfigEntry.Category("biome")
    @ConfigEntry.Gui.TransitiveObject
    public BiomeSettings biomeSettings = new BiomeSettings();
    public static class BiomeSettings {
        public boolean shouldRender = true;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.CENTER;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.TOP;

        public int x = 0;
        public int y = 5;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 0;

        @Comment("Which way should the HUD goes as the text increases?")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.GrowthDirection textGrowth = Helper.GrowthDirection.CENTER;

        @ConfigEntry.Gui.CollapsibleObject
        public DimensionColorSettings color = new DimensionColorSettings();
        public static class DimensionColorSettings {
            @ConfigEntry.ColorPicker
            public int overworld = 0xFFFFFF;
            @ConfigEntry.ColorPicker
            public int nether = 0xfc7871;
            @ConfigEntry.ColorPicker
            public int end = 0xc9c7e3;
            @ConfigEntry.ColorPicker
            public int custom = 0xFFFFFF;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public BiomeHideSettings hideOn = new BiomeHideSettings();
        public static class BiomeHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
        }
    }

    @ConfigEntry.Category("inventory")
    @ConfigEntry.Gui.TransitiveObject
    public InventorySettings inventorySettings = new InventorySettings();
    public static class InventorySettings {
        public boolean shouldRender = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.RIGHT;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.MIDDLE;

        public int x = -5;
        public int y = 0;

        @ConfigEntry.BoundedDiscrete(max = 6)
        @Comment("Set to 0 for default GUI Scale")
        public int scale = 1;

        public boolean drawVertical = true;

        @ConfigEntry.Gui.CollapsibleObject
        public InventoryHideSettings hideOn = new InventoryHideSettings();
        public static class InventoryHideSettings {
            public boolean f3 = false;
            public boolean chat = false;
        }
    }

    @ConfigEntry.Category("hand")
    @ConfigEntry.Gui.TransitiveObject
    public HandSettings handSettings = new HandSettings();
    public static class HandSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public LeftHandSettings leftHandSettings = new LeftHandSettings();
        public static class LeftHandSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.CENTER;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.BOTTOM;

            public int x = -108;
            public int y = -25;

            @ConfigEntry.BoundedDiscrete(max = 6)
            @Comment("Set to 0 for default GUI Scale")
            public int scale = 0;

            @Comment("Which way should the HUD goes when a the texture increases?")
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.GrowthDirection textureGrowth = Helper.GrowthDirection.LEFT;

            public boolean showCountOnly = false;

            @ConfigEntry.ColorPicker
            public int color = 0xffb3b3;

            @ConfigEntry.Gui.CollapsibleObject
            public LeftHandHideSettings hideOn = new LeftHandHideSettings();
            public static class LeftHandHideSettings {
                public boolean f3 = false;
                public boolean chat = false;
            }
        }

        @ConfigEntry.Gui.CollapsibleObject
        public RightHandSettings rightHandSettings = new RightHandSettings();
        public static class RightHandSettings {
            public boolean shouldRender = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentX originX = Helper.ScreenAlignmentX.CENTER;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.ScreenAlignmentY originY = Helper.ScreenAlignmentY.BOTTOM;

            public int x = 108;
            public int y = -25;

            @ConfigEntry.BoundedDiscrete(max = 6)
            @Comment("Set to 0 for default GUI Scale")
            public int scale = 0;

            @Comment("Which way should the HUD goes when a the texture increases?")
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public Helper.GrowthDirection textureGrowth = Helper.GrowthDirection.RIGHT;

            public boolean showCountOnly = false;

            @ConfigEntry.ColorPicker
            public int color = 0x87ceeb;

            @ConfigEntry.Gui.CollapsibleObject
            public RightHandHideSettings hideOn = new RightHandHideSettings();
            public static class RightHandHideSettings {
                public boolean f3 = false;
                public boolean chat = false;
            }
        }
    }
}