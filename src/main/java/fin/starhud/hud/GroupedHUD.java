package fin.starhud.hud;

import fin.starhud.Main;
import fin.starhud.config.GroupedHUDSettings;
import fin.starhud.helper.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class GroupedHUD extends AbstractHUD {

    private static final Logger LOGGER = Main.LOGGER;

    public GroupedHUDSettings groupSettings;
    public final List<AbstractHUD> huds = new ArrayList<>();

    public GroupedHUD(GroupedHUDSettings groupSettings) {
        super(groupSettings.base);
        this.groupSettings = groupSettings;

        List<String> invalidIds = new ArrayList<>();

        for (String id : groupSettings.hudIds) {
            AbstractHUD hud = HUDComponent.getInstance().getHUD(id);
            if (hud == null) {
                invalidIds.add(id);
            } else {
                huds.add(hud);
                hud.setGroupId(this.groupSettings.id);
            }
        }
        groupSettings.hudIds.removeAll(invalidIds);
    }

    @Override
    public String getName() {
        if (huds.isEmpty())
            return "Groupped HUD";

        StringBuilder name = new StringBuilder();

        name.append("(");

        for (AbstractHUD hud : huds)
            name.append(hud.getName()).append(',');

        name.deleteCharAt(name.length() - 1).append(')');

        return name.toString();
    }

    private final List<AbstractHUD> renderedHUDs = new ArrayList<>();
    private final List<Integer> xOffsets = new ArrayList<>();
    private final List<Integer> yOffsets = new ArrayList<>();

    @Override
    public boolean collectHUDInformation() {
        renderedHUDs.clear();
        xOffsets.clear();
        yOffsets.clear();

        int width = 0;
        int height = 0;

        int renderedCount = 0;

        for (AbstractHUD hud : huds) {
            if (!hud.shouldRender() || !hud.collectHUDInformation())
                continue;

            renderedCount++;

            if (groupSettings.alignVertical) {
                height += hud.getHeight();
                width = Math.max(width, hud.getWidth());
            } else {
                width += hud.getWidth();
                height = Math.max(height, hud.getHeight());
            }

            renderedHUDs.add(hud);
        }

        if (renderedCount > 0) {
            if (groupSettings.alignVertical) {
                height += groupSettings.gap * (renderedCount - 1);
            } else {
                width += groupSettings.gap * (renderedCount - 1);
            }
        } else
            return false;

        int xOffset = 0, yOffset = 0;
        for (AbstractHUD hud : renderedHUDs) {
            if (groupSettings.alignVertical) {
                xOffset = getAlignmentOffset(hud, width - hud.getWidth());
                xOffsets.add(xOffset);
                yOffsets.add(yOffset);

                yOffset += hud.getHeight() + groupSettings.gap;
            } else {
                yOffset = getAlignmentOffset(hud, height - hud.getHeight());
                xOffsets.add(xOffset);
                yOffsets.add(yOffset);

                xOffset += hud.getWidth() + groupSettings.gap;
            }
        }

        setWidthHeight(width, height);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {
        return renderHUD(context, x, y, drawBackground, false);
    }

    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground, boolean parentDrewBackground) {
        int w = getWidth();
        int h = getHeight();
        int size = renderedHUDs.size();

        boolean drewBackground = false;

        // Only draw background if this group is not inside another that already drew one
        if (!parentDrewBackground && drawBackground) {
            RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
            drewBackground = true;
        }

        boolean thisDrewBackground = parentDrewBackground || drewBackground;

        for (int i = 0; i < size; ++i) {
            AbstractHUD hud = renderedHUDs.get(i);
            int xOffset = xOffsets.get(i);
            int yOffset = yOffsets.get(i);

            // if grouped hud decided to draw background, child shouldn't.
            boolean childShouldDrawBackground = !thisDrewBackground && hud.shouldDrawBackground();

            hud.setXY(x + xOffset, y + yOffset);
            hud.setScale(getScale());

            if (hud instanceof GroupedHUD group) {
                group.renderHUD(context, hud.getX(), hud.getY(), childShouldDrawBackground, thisDrewBackground);
            } else {
                hud.renderHUD(context, hud.getX(), hud.getY(), childShouldDrawBackground);
            }
        }

        return true;
    }

    @Override
    public String getId() {
        return groupSettings.id;
    }

    @Override
    public void update() {
        super.update();

        for (AbstractHUD hud : huds){
            if (!hud.isInGroup()) {
                LOGGER.warn("{} IS NOT IN A GROUP! FORCING TO CHANGE THEM.", hud.getName());
                hud.setGroupId(groupSettings.id);
            }
        }

        getBoundingBox().setColor(groupSettings.boxColor | 0xFF000000);
    }

    public int getAlignmentOffset(AbstractHUD childHUD, int length) {
        return switch (groupSettings.getChildAlignment()) {
            case GROUP -> groupSettings.alignVertical ? getSettings().getOriginX().getAlignmentPos(length) : getSettings().getOriginY().getAlignmentPos(length);
            case CHILD -> groupSettings.alignVertical ? childHUD.getSettings().getOriginX().getAlignmentPos(length) : childHUD.getSettings().getOriginY().getAlignmentPos(length);
            case START -> 0;
            case CENTER -> length / 2;
            case END -> length;
        };
    }

    public void updateActiveHUDsFromConfig() {

        for (AbstractHUD hud : huds)
            hud.setGroupId(null);
        huds.clear();

        for (String id : groupSettings.hudIds) {
            AbstractHUD hud = HUDComponent.getInstance().getHUD(id);

            huds.add(hud);
            hud.setGroupId(groupSettings.id);
//            LOGGER.info("UpdateActiveHUDsFromConfig (added to group: {}) : {}", groupSettings.id, hud.getName());
        }
    }
}
