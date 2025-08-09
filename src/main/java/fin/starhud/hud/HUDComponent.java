package fin.starhud.hud;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.GroupedHUDSettings;
import fin.starhud.config.HUDList;
import fin.starhud.hud.implementation.*;
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
            System.out.println(setting.id);
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

    public void renderAll(DrawContext context) {

        long now = System.nanoTime();
        long intervalNanos = (long) (HUD_SETTINGS.dataCollectionInterval * 1_000_000_000L);

        if (now - lastCollect >= intervalNanos) {
            collectAll();
            lastCollect = now;
        }

        for (HUDInterface hud : renderedHUDs)
            hud.render(context);
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

    public String generateNextGroupId() {
        int index = 1;
        String id;
        do {
            id = "group_" + index++;
        } while (groupedHUDs.containsKey(id));
        return id;
    }
}
