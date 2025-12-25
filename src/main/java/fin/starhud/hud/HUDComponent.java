package fin.starhud.hud;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.GroupedHUDSettings;
import fin.starhud.config.HUDList;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.hud.implementation.armor.BootsHUD;
import fin.starhud.hud.implementation.armor.ChestplateHUD;
import fin.starhud.hud.implementation.armor.HelmetHUD;
import fin.starhud.hud.implementation.armor.LeggingsHUD;
import fin.starhud.hud.implementation.clock.ClockInGameHUD;
import fin.starhud.hud.implementation.clock.ClockSystemHUD;
import fin.starhud.hud.implementation.coordinate.nether.NetherXCoordinate;
import fin.starhud.hud.implementation.coordinate.nether.NetherYCoordinate;
import fin.starhud.hud.implementation.coordinate.nether.NetherZCoordinate;
import fin.starhud.hud.implementation.coordinate.normal.XCoordinateHUD;
import fin.starhud.hud.implementation.coordinate.normal.YCoordinateHUD;
import fin.starhud.hud.implementation.coordinate.normal.ZCoordinateHUD;
import fin.starhud.hud.implementation.hand.LeftHandHUD;
import fin.starhud.hud.implementation.hand.RightHandHUD;
import fin.starhud.hud.implementation.other.*;
import fin.starhud.hud.implementation.statuseffect.NegativeEffectHUD;
import fin.starhud.hud.implementation.statuseffect.PositiveEffectHUD;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HUDComponent {

    // singleton
    private static HUDComponent instance;

    private static final Logger LOGGER = Main.LOGGER;
    private GeneralSettings.HUDSettings HUD_SETTINGS;

    // Registered HUDs by ID
    private final Map<String, AbstractHUD> hudMap = new HashMap<>();

    // Active HUDs (selected in config)
    private final Map<String, AbstractHUD> individualHUDs = new HashMap<>();
    private final Map<String, GroupedHUD> groupedHUDs = new HashMap<>();

    // rendered HUD
    public final List<AbstractHUD> renderedHUDs = new ArrayList<>();

    private HUDComponent() {}

    public static HUDComponent getInstance() {
        if (instance == null) {
            instance = new HUDComponent();
        }
        return instance;
    }

    public void init() {
        registerBuiltInHUDs();
        loadActiveHUDsFromConfig();

        HUD_SETTINGS = Main.settings.generalSettings.hudSettings;
    }

    private void registerBuiltInHUDs() {
        registerHUD(new HelmetHUD());
        registerHUD(new ChestplateHUD());
        registerHUD(new LeggingsHUD());
        registerHUD(new BootsHUD());

        registerHUD(new XCoordinateHUD());
        registerHUD(new YCoordinateHUD());
        registerHUD(new ZCoordinateHUD());

        registerHUD(new NetherXCoordinate());
        registerHUD(new NetherYCoordinate());
        registerHUD(new NetherZCoordinate());

        registerHUD(new LeftHandHUD());
        registerHUD(new RightHandHUD());

        registerHUD(new ClockInGameHUD());
        registerHUD(new ClockSystemHUD());

        registerHUD(new BiomeHUD());
        registerHUD(new DayHUD());
        registerHUD(new DirectionHUD());
        registerHUD(new FPSHUD());
        registerHUD(new TPSHUD());
        registerHUD(new InventoryHUD());
        registerHUD(new PingHUD());
        registerHUD(new SpeedHUD());
        registerHUD(new TargetedCrosshairHUD());

        registerHUD(new PositiveEffectHUD());
        registerHUD(new NegativeEffectHUD());
    }

    public Map<String, AbstractHUD> getHudMap() {
        return hudMap;
    }

    public Map<String, AbstractHUD> getIndividualHUDs() {
        return individualHUDs;
    }

    public Map<String, GroupedHUD> getGroupedHUDs() {
        return groupedHUDs;
    }

    public List<AbstractHUD> getRenderedHUDs() {
        return renderedHUDs;
    }

    public void loadActiveHUDsFromConfig() {
        HUDList hudConfig = Main.settings.hudList;

        individualHUDs.clear();

        for (String id : hudConfig.individualHudIds) {
            AbstractHUD hud = hudMap.get(id);
            hud.setGroupId(null);
            individualHUDs.put(id, hud);
        }

        groupedHUDs.clear();

        for (GroupedHUDSettings settings : hudConfig.groupedHuds) {
            groupedHUDs.put(settings.id, new GroupedHUD(settings));
        }
    }


    private void registerHUD(AbstractHUD hud) {
        hudMap.put(hud.getId(), hud);
        LOGGER.info("{} Added to Hud Map.", hud.getId());
    }

    public AbstractHUD getHUD(String id) {
        AbstractHUD hud = hudMap.get(id);
        if (hud != null)
            return hud;

        hud = groupedHUDs.get(id);
        if (hud != null)
            return hud;

        LOGGER.warn("No such group with id: {} existed in the map, creating new one if available in config", id);

        List<GroupedHUDSettings> groupSettings = Main.settings.hudList.groupedHuds;
        for (GroupedHUDSettings setting : groupSettings) {
            if (id.equals(setting.id)) {
                hud = new GroupedHUD(setting);
                groupedHUDs.put(id, (GroupedHUD) hud);
                return hud;
            }
        }

        LOGGER.error("Group with ID: {} does not exist. returning null.", id);

        return null;
    }

    public AbstractHUD getHUD(HUDId id) {
        return hudMap.get(id.toString());
    }

    private long lastCollect = -1;

    private final List<AbstractHUD> invalidHUDs = new ArrayList<>();

    public void renderAll(DrawContext context) {

        long now = System.nanoTime();
        long intervalNanos = (long) (HUD_SETTINGS.dataCollectionInterval * 1_000_000_000L);

        if (now - lastCollect >= intervalNanos) {
            collectAll();
            lastCollect = now;
        }

        for (AbstractHUD hud : renderedHUDs) {
            if (!hud.render(context)) {
                LOGGER.warn("{} is collected but still failed! Removing from rendered hud.", hud.getName());
                invalidHUDs.add(hud);
            }
        }

        if (!invalidHUDs.isEmpty()) {
            renderedHUDs.removeAll(invalidHUDs);
            invalidHUDs.clear();
        }
    }

    public void collectAll() {
        renderedHUDs.clear();
        for (AbstractHUD hud : individualHUDs.values()) {
            if (hud.shouldRender() && hud.collect()) {
                renderedHUDs.add(hud);
            }
        }

        for (GroupedHUD group : groupedHUDs.values()) {
            if (!group.isInGroup() && group.shouldRender() && group.collect()) {
                renderedHUDs.add(group);
            }
        }
    }

    public void updateAll() {
        for (HUDInterface hud : hudMap.values()) {
            hud.update();
        }

        for (HUDInterface hud : groupedHUDs.values()) {
            hud.update();
        }
    }

    // follow up with the updated config.
    public void updateActiveHUDs() {
        loadActiveHUDsFromConfig();
        updateAll();
    }

    public void removeActiveHUDs() {
        individualHUDs.clear();
        groupedHUDs.clear();
    }

    public void clampAll() {
        for (AbstractHUD hud : renderedHUDs)
            hud.clampPos();
    }

    // grouping function, experimental, may crash.

    // hud in huds MUST be ungrouped. not doing so will crash.
    public GroupedHUDSettings group(List<AbstractHUD> huds) {
        GroupedHUDSettings newSettings = new GroupedHUDSettings();

        List<GroupedHUDSettings> groupedHUDs = Main.settings.hudList.groupedHuds;
        List<String> individualHUDs = Main.settings.hudList.individualHudIds;

        // remove hud from individualHUDs, and add hud to the group via settings.
        for (AbstractHUD hud : huds) {
            if (hud.isInGroup()) {
                throw new IllegalStateException("HUD " + hud.getId() + " is already in a group.");
            }

            if (!(hud instanceof GroupedHUD))
                individualHUDs.remove(hud.getId());
            newSettings.hudIds.add(hud.getId());
            hud.setGroupId(newSettings.id);

//            LOGGER.info("{} added to {}", hud.getName(), newSettings.id);
        }

        // we should copy the settings from the first selected hud. so that the position doesn't reset to 0,0.
        AbstractHUD firstHUD = huds.getFirst();
        newSettings.base.copySettings(firstHUD.getSettings());
        newSettings.base.drawBackground = false;
        newSettings.base.displayMode = HUDDisplayMode.BOTH;
        newSettings.boxColor = firstHUD.getBoundingBox().getColor() & 0x00FFFFFF;

        groupedHUDs.add(newSettings);
        HUDComponent.getInstance().updateActiveHUDs();

        return newSettings;
    }

    public void unGroup(GroupedHUD groupedHUD) {
        List<AbstractHUD> huds = groupedHUD.huds;

        List<GroupedHUDSettings> groupedHUDs = Main.settings.hudList.groupedHuds;
        List<String> individualHUDs = Main.settings.hudList.individualHudIds;

        for (AbstractHUD hud : huds) {
            if (!(hud instanceof GroupedHUD))
                individualHUDs.add(hud.getId());
            hud.setGroupId(null);
//            LOGGER.info("{} removed from {}", hud.getName(), groupedHUD.groupSettings.id);
        }

        groupedHUDs.removeIf(a -> a.id.equals(groupedHUD.groupSettings.id));
        HUDComponent.getInstance().updateActiveHUDs();
    }

    public String generateNextGroupId() {
        int index = 1;
        String id;
        do {
            id = "group_" + index++;
        } while (groupedHUDs.containsKey(id));
        return id;
    }
}
