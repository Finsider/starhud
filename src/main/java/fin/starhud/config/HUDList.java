package fin.starhud.config;

import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import fin.starhud.hud.HUDId;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.*;
import java.util.stream.Collectors;

public class HUDList {

    @ConfigEntry.Gui.Excluded
    public List<String> individualHudIds = new ArrayList<>();

    @Comment("It's not recommended to modify grouped HUD directly on the configuration screen.")
    public List<GroupedHUDSettings> groupedHuds = new ArrayList<>();

    public HUDList(List<String> individualHudIds, List<GroupedHUDSettings> groupedHuds) {
        this.individualHudIds = individualHudIds;
        this.groupedHuds = groupedHuds;
    }

    public HUDList() {
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, 24, 0, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE, false),
                        1,
                        1,
                        true,
                        0xFFFFFF,
                        new ArrayList<>(List.of(HUDId.HELMET.toString(), HUDId.CHESTPLATE.toString(), HUDId.LEGGINGS.toString(), HUDId.BOOTS.toString()))
                )
        );
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, -24, 24, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN, false),
                        2,
                        2,
                        true,
                        0xd5feef,
                        new ArrayList<>(List.of(HUDId.POSITIVE_EFFECT.toString(), HUDId.NEGATIVE_EFFECT.toString()))
                )
        );
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, 0, 24, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP, GrowthDirectionX.CENTER, GrowthDirectionY.DOWN, false),
                        3,
                        3,
                        false,
                        0xFFFFFF,
                        new ArrayList<>(List.of(HUDId.BIOME.toString(), HUDId.CLOCK_INGAME.toString()))
                )
        );
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, -24, -24, ScreenAlignmentX.RIGHT, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP, false),
                        4,
                        3,
                        false,
                        0x85f290,
                        new ArrayList<>(List.of(HUDId.TPS.toString(), HUDId.PING.toString(), HUDId.CLOCK_SYSTEM.toString()))
                )
        );
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, 24, 24, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN, false),
                        5,
                        1,
                        true,
                        0xfc7871,
                        new ArrayList<>(List.of(HUDId.X_COORDINATE.toString(), HUDId.Y_COORDINATE.toString(), HUDId.Z_COORDINATE.toString(), HUDId.DIRECTION.toString()))
                )
        );
        groupedHuds.add(
                new GroupedHUDSettings(
                        new BaseHUDSettings(true, 0, -171, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.CENTER, GrowthDirectionY.UP, false),
                        6,
                        1,
                        true,
                        0xff9a76,
                        new ArrayList<>(List.of(HUDId.COMBO.toString(), HUDId.REACH.toString()))
                )
        );
    }


    public void onConfigSaved() {

        // remove invalid id if user acts silly and do something silly and everything became silly
        Set<String> validHudIds = Arrays.stream(HUDId.values())
                .map(HUDId::toString)
                .collect(Collectors.toSet());

        individualHudIds.removeIf(id -> !validHudIds.contains(id));

        for (Iterator<GroupedHUDSettings> it = groupedHuds.iterator(); it.hasNext();) {
            GroupedHUDSettings group = it.next();

            group.hudIds.removeIf(id -> !isValidHudOrGroupId(id, validHudIds));

            if (group.hudIds.isEmpty()) {
                it.remove();
            }
        }

        // count appearances of each HUD ID
        Map<String, Integer> appearanceMap = new HashMap<>();

        for (String id : individualHudIds) {
            appearanceMap.put(id, appearanceMap.getOrDefault(id, 0) + 1);
        }

        for (GroupedHUDSettings group : groupedHuds) {
            for (String id : group.hudIds) {
                appearanceMap.put(id, appearanceMap.getOrDefault(id, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : appearanceMap.entrySet()) {
            String id = entry.getKey();
            int count = entry.getValue();

            if (count == 1) continue;

            boolean inIndividual = individualHudIds.contains(id);
            boolean inGrouped = groupedHuds.stream()
                    .anyMatch(group -> group.hudIds.contains(id));

            // if there are 2 and in individual and in grouped, prioritize group.
            if (count == 2 && inIndividual && inGrouped) {
                individualHudIds.remove(id);
            } else {
                // if there are many duplicates, remove all.
                for (GroupedHUDSettings group : groupedHuds)
                    group.hudIds.removeIf(hudId -> hudId.equals(id));

                individualHudIds.removeIf(hudId -> hudId.equals(id));
            }
        }

        // this ensures that every hudIds that isn't found in groupedhud to be put in individual hud.
        Set<String> allRepresentedIds = new HashSet<>(individualHudIds);
        for (GroupedHUDSettings group : groupedHuds) {
            allRepresentedIds.addAll(group.hudIds);
        }

        for (HUDId id : HUDId.values()) {
            if (!allRepresentedIds.contains(id.toString())) {
                individualHudIds.add(id.toString());
            }
        }
    }

    private boolean isValidHudOrGroupId(String id, Set<String> validHudIds) {
        return validHudIds.contains(id) || id.matches("^group_\\d+$");
    }
}
