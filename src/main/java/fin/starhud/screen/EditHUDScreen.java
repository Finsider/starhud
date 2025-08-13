package fin.starhud.screen;

import fin.starhud.Main;
import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.GroupedHUDSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.Box;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.GroupedHUD;
import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.*;

public class EditHUDScreen extends Screen {

    private static final Logger LOGGER = Main.LOGGER;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.EditHUDScreenSettings SETTINGS = Main.settings.generalSettings.screenSettings;

    private static final int PADDING = 25;
    private static final int WIDGET_WIDTH = 100;
    private static final int WIDGET_HEIGHT = 20;
    private static final int TEXT_FIELD_WIDTH = 40;
    private static final int SQUARE_WIDGET_LENGTH = 20;
    private static final int GAP = 5;

    public Screen parent;

    private final Map<String, AbstractHUD> individualHUDs;
    private final Map<String, GroupedHUD> groupedHUDs;

    private final Map<String, BaseHUDSettings> oldHUDSettings;
    private final Map<String, GroupedHUDSettings> oldGroupedHUDSettings;

    private final List<String> oldIndividualHudIds;
    private final List<GroupedHUDSettings> oldGroupedHUDs;

    private boolean dragging = false;
    private final List<AbstractHUD> selectedHUDs = new ArrayList<>();

    private boolean isHelpActivated = false;
    private boolean isMoreOptionActivated = false;
    private boolean canSelectedHUDsGroup = false;
    private boolean canSelectedHUDUngroup = false;

    // widgets
    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private TextFieldWidget scaleField;
    private ButtonWidget shouldRenderButton;
    private ButtonWidget alignmentXButton;
    private ButtonWidget alignmentYButton;
    private ButtonWidget directionXButton;
    private ButtonWidget directionYButton;
    private ButtonWidget hudDisplayButton;
    private ButtonWidget drawBackgroundButton;

    // special group buttons
    private TextFieldWidget gapField;
    private ButtonWidget groupAlignmentButton;
    private ButtonWidget groupUngroupButton;
    private ButtonWidget childAlignmentButton;

    private static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

    private record Help(Text key, Text info) {}

    private static final Help[] HELPS = {
            new Help(Text.translatable("starhud.help.key.move_1"), Text.translatable("starhud.help.info.move_1")),
            new Help(Text.translatable("starhud.help.key.move_5"), Text.translatable("starhud.help.info.move_5")),
            new Help(Text.translatable(isMac ? "starhud.help.key.alignment_mac" : "starhud.help.key.alignment_windows"), Text.translatable("starhud.help.info.alignment")),
            new Help(Text.translatable("starhud.help.key.direction"), Text.translatable("starhud.help.info.direction")),
            new Help(Text.translatable(isMac ? "starhud.help.key.revert_changes_mac" : "starhud.help.key.revert_changes_windows"), Text.translatable("starhud.help.info.revert_changes")),
            new Help(Text.translatable("starhud.help.key.group_ungroup"), Text.translatable("starhud.help.info.group_ungroup"))
    };

    private static int HELP_KEY_MAX_WIDTH;
    private static int HELP_INFO_MAX_WIDTH;
    private static final int HELP_HEIGHT = 5 + (HELPS.length * 9) + 5;

    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

        individualHUDs = HUDComponent.getInstance().getIndividualHUDs();
        groupedHUDs = HUDComponent.getInstance().getGroupedHUDs();

        Map<String, AbstractHUD> HUDMap = HUDComponent.getInstance().getHudMap();

        oldHUDSettings = new HashMap<>();
        for (AbstractHUD p : HUDMap.values()) {
            BaseHUDSettings settings = p.getSettings();
            oldHUDSettings.put(p.getId(), new BaseHUDSettings(settings.shouldRender, settings.x, settings.y, settings.alignmentX, settings.alignmentY, settings.growthDirectionX, settings.growthDirectionY, settings.scale, settings.displayMode, settings.drawBackground));
        }

        oldGroupedHUDSettings = new HashMap<>();
        for (GroupedHUD p : groupedHUDs.values()) {
            BaseHUDSettings settings = p.getSettings();
            oldGroupedHUDSettings.put(
                    p.getId(),
                    new GroupedHUDSettings(
                            new BaseHUDSettings(settings.shouldRender, settings.x, settings.y, settings.alignmentX, settings.alignmentY, settings.growthDirectionX, settings.growthDirectionY, settings.scale, settings.displayMode, settings.drawBackground),
                            p.getId(),
                            p.groupSettings.gap,
                            p.groupSettings.alignVertical,
                            p.groupSettings.getChildAlignment(),
                            p.groupSettings.boxColor,
                            p.groupSettings.hudIds
                    )
            );
        }

        oldIndividualHudIds = List.copyOf(Main.settings.hudList.individualHudIds);
        oldGroupedHUDs = List.copyOf(Main.settings.hudList.groupedHuds);
    }

    @Override
    protected void init() {

        final int CENTER_X = this.width / 2;
        final int CENTER_Y = (this.height - WIDGET_HEIGHT) / 2;

        HUDComponent.getInstance().updateAll();

        int xFieldX = CENTER_X - TEXT_FIELD_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP;
        int xFieldY = CENTER_Y;
        xField = new TextFieldWidget(
                CLIENT.textRenderer,
                xFieldX, xFieldY,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.of("X")
        );

        int yFieldX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;
        int yFieldY = xFieldY;
        yField = new TextFieldWidget(
                CLIENT.textRenderer,
                yFieldX, yFieldY,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.of("Y")
        );

        int alignmentXButtonX = CENTER_X - WIDGET_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP;
        int alignmentXButtonY = xFieldY - PADDING;
        alignmentXButton = ButtonWidget.builder(
                Text.of("X Alignment: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().alignmentX = selectedHUD.getSettings().alignmentX.next();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.recommendedScreenAlignment(selectedHUD.getSettings().alignmentX);
                    selectedHUD.update();
                    alignmentXButton.setMessage(Text.of("X Alignment: " + selectedHUD.getSettings().getAlignmentX()));
                }
        ).dimensions(alignmentXButtonX, alignmentXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int alignmentYButtonX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;
        int alignmentYButtonY = alignmentXButtonY;
        alignmentYButton = ButtonWidget.builder(
                Text.of("Y Alignment: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().alignmentY = selectedHUD.getSettings().alignmentY.next();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.recommendedScreenAlignment(selectedHUD.getSettings().alignmentY);
                    selectedHUD.update();
                    alignmentYButton.setMessage(Text.of("Y Alignment: " + selectedHUD.getSettings().getAlignmentY()));
                }
        ).dimensions(alignmentYButtonX, alignmentYButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int directionXButtonX = alignmentXButtonX;
        int directionXButtonY = alignmentXButtonY - PADDING;
        directionXButton = ButtonWidget.builder(
                Text.of("X Direction: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.next();
                    selectedHUD.update();
                    directionXButton.setMessage(Text.of("X Direction: " + selectedHUD.getSettings().getGrowthDirectionX()));
                }
        ).dimensions(directionXButtonX, directionXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int scaleFieldWidth = SQUARE_WIDGET_LENGTH + 6;
        int scaleFieldX = CENTER_X - (scaleFieldWidth / 2);
        int scaleFieldY = directionXButtonY;
        scaleField = new TextFieldWidget(
                CLIENT.textRenderer,
                scaleFieldX, scaleFieldY,
                scaleFieldWidth,
                SQUARE_WIDGET_LENGTH,
                Text.of("Scale")
        );

        int directionYButtonX = alignmentYButtonX;
        int directionYButtonY = directionXButtonY;
        directionYButton = ButtonWidget.builder(
                Text.of("Y Direction: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.next();
                    selectedHUD.update();
                    directionYButton.setMessage(Text.of("Y Direction: " + selectedHUD.getSettings().getGrowthDirectionY()));
                }
        ).dimensions(directionYButtonX, directionYButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int hudDisplayButtonX = directionXButtonX;
        int hudDisplayButtonY = directionXButtonY - PADDING;
        hudDisplayButton = ButtonWidget.builder(
                Text.of("Display: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().displayMode = selectedHUD.getSettings().displayMode.next();
                    selectedHUD.update();
                    hudDisplayButton.setMessage(Text.of("Display: " + selectedHUD.getSettings().getDisplayMode()));
                }
        ).dimensions(hudDisplayButtonX, hudDisplayButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int drawBackgroundButtonX = directionYButtonX;
        int drawBackgroundButtonY = hudDisplayButtonY;
        drawBackgroundButton = ButtonWidget.builder(
                Text.of("Background: N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().drawBackground = !selectedHUD.getSettings().drawBackground;
                    selectedHUD.update();
                    drawBackgroundButton.setMessage(Text.of("Background: " + (selectedHUD.getSettings().drawBackground ? "ON" : "OFF")));
                }
        ).dimensions(drawBackgroundButtonX, drawBackgroundButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int shouldRenderButtonY = alignmentXButtonY;
        int shouldRenderButtonX = CENTER_X - (SQUARE_WIDGET_LENGTH / 2);
        shouldRenderButton = ButtonWidget.builder(
                Text.of("N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().shouldRender = !selectedHUD.getSettings().shouldRender;
                    selectedHUD.update();
                    button.setMessage(Text.of(selectedHUD.getSettings().shouldRender ? "ON" : "OFF"));
                }
        ).dimensions(shouldRenderButtonX, shouldRenderButtonY, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH).build();

        xField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            try {
                selectedHUD.getSettings().x = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        yField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            try {
                selectedHUD.getSettings().y = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        scaleField.setTooltip(Tooltip.of(Text.of("HUD Scale (Set to 0 for default)")));
        scaleField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            try {
                selectedHUD.getSettings().scale = Float.parseFloat(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        int yBottom = this.height - SQUARE_WIDGET_LENGTH - GAP;

        int configScreenLength = SQUARE_WIDGET_LENGTH;
        int xConfigScreenButton = CENTER_X - (configScreenLength / 2);
        ButtonWidget configScreenButton = ButtonWidget.builder(
                        Text.of("🛠"),
                        button -> {
                            isHelpActivated = false;
                            isMoreOptionActivated = false;
                            selectedHUDs.clear();
                            this.client.setScreen(AutoConfig.getConfigScreen(Settings.class, this).get());
                        }
                )
                .tooltip(Tooltip.of(Text.of("Open Configuration Screen")))
                .dimensions(xConfigScreenButton, CENTER_Y, configScreenLength, configScreenLength)
                .build();

        int xHelpButton = CENTER_X - (GAP/2) - SQUARE_WIDGET_LENGTH;
        ButtonWidget helpButton = ButtonWidget.builder(
                Text.of("?"),
                button -> {
                    isHelpActivated = !isHelpActivated;
                    onHelpSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.of("Help")))
                .dimensions(xHelpButton, yBottom, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();

        int xMoreOptionButton = CENTER_X + (GAP/2);
        ButtonWidget moreOptionButton = ButtonWidget.builder(
                Text.of("+"),
                button -> {
                    isMoreOptionActivated = !isMoreOptionActivated;
                    onMoreOptionSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.of("More Options")))
                .dimensions(xMoreOptionButton, yBottom, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();

        int terminatorWidth = 70;
        int xSaveAndQuitButton = xMoreOptionButton + GAP + SQUARE_WIDGET_LENGTH;
        ButtonWidget saveAndQuitButton = ButtonWidget.builder(
                Text.of("Save & Quit"),
                button -> {
                    AutoConfig.getConfigHolder(Settings.class).save();
                    onClose();
        }).dimensions(xSaveAndQuitButton, yBottom, terminatorWidth, WIDGET_HEIGHT).build();

        int xCancelButton = xHelpButton - GAP - terminatorWidth;

        ButtonWidget cancelButton = ButtonWidget.builder(
                Text.of("Cancel"),
                button -> close()
        ).dimensions(xCancelButton, yBottom, terminatorWidth, WIDGET_HEIGHT).build();

        // special case: grouped hud buttons

        int yBottomGroup = CENTER_Y + PADDING;
        int xGroupUngroupButton = CENTER_X - (terminatorWidth / 2);

        groupUngroupButton = ButtonWidget.builder(
                Text.of("N/A"),
                button -> {
                    if (canSelectedHUDsGroup) {
                        group(selectedHUDs);
                    } else if (canSelectedHUDUngroup) {
                        unGroup((GroupedHUD) selectedHUDs.getFirst());
                    }

                    selectedHUDs.clear();
                    updateFieldsFromSelectedHUD();
                }
        ).dimensions(xGroupUngroupButton, yBottomGroup, terminatorWidth, SQUARE_WIDGET_LENGTH).build();

        int gapFieldWidth = terminatorWidth / 2;
        int xGapField = CENTER_X - (gapFieldWidth / 2);
        int yGapField = yBottomGroup + PADDING;
        gapField = new TextFieldWidget(
                CLIENT.textRenderer,
                xGapField, yGapField,
                gapFieldWidth, SQUARE_WIDGET_LENGTH,
                Text.of("Gap")
        );

        gapField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;

            try {
                hud.groupSettings.gap = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        });

        int xChildAlignmentButton = xGroupUngroupButton - GAP - terminatorWidth;
        int yChildAlignmentButton = yBottomGroup;
        childAlignmentButton = ButtonWidget.builder(
                Text.of("N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;
                    hud.groupSettings.childAlignment = hud.groupSettings.getChildAlignment().next();

                    button.setMessage(Text.of(hud.groupSettings.getChildAlignment().name()));
                }
        )
                .dimensions(xChildAlignmentButton, yChildAlignmentButton, terminatorWidth, SQUARE_WIDGET_LENGTH)
                .tooltip(Tooltip.of(Text.of("Child Alignment")))
                .build();

        int xGroupAlignmentButton = xGroupUngroupButton + terminatorWidth + GAP;
        groupAlignmentButton = ButtonWidget.builder(
                Text.of("N/A"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;
                    hud.groupSettings.alignVertical = !hud.groupSettings.alignVertical;

                    groupAlignmentButton.setMessage(Text.of(
                            hud.groupSettings.alignVertical ? "Vertical" : "Horizontal"
                    ));
                }
        ).dimensions(xGroupAlignmentButton, yBottomGroup, terminatorWidth, SQUARE_WIDGET_LENGTH).build();

        alignmentXButton.visible = false;
        directionXButton.visible = false;
        alignmentYButton.visible = false;
        directionYButton.visible = false;
        hudDisplayButton.visible = false;
        drawBackgroundButton.visible = false;
        shouldRenderButton.visible = false;
        xField.visible = false;
        yField.visible = false;
        scaleField.visible = false;

        gapField.visible = false;
        groupAlignmentButton.visible = false;
        childAlignmentButton.visible = false;

        addDrawableChild(cancelButton);
        addDrawableChild(helpButton);
        addDrawableChild(moreOptionButton);
        addDrawableChild(saveAndQuitButton);

        addDrawableChild(hudDisplayButton);
        addDrawableChild(drawBackgroundButton);

        addDrawableChild(directionXButton);
        addDrawableChild(scaleField);
        addDrawableChild(directionYButton);

        addDrawableChild(alignmentXButton);
        addDrawableChild(shouldRenderButton);
        addDrawableChild(alignmentYButton);

        addDrawableChild(xField);
        addDrawableChild(configScreenButton);
        addDrawableChild(yField);

        addDrawableChild(gapField);

        addDrawableChild(childAlignmentButton);
        addDrawableChild(groupUngroupButton);
        addDrawableChild(groupAlignmentButton);

        updateFieldsFromSelectedHUD();
        getHelpMaxWidths();
    }

    private void getHelpMaxWidths() {
        int maxKey = 0;
        int maxInfo = 0;

        for (Help h : HELPS) {
            maxKey = Math.max(maxKey, CLIENT.textRenderer.getWidth(h.key));
            maxInfo = Math.max(maxInfo, CLIENT.textRenderer.getWidth(h.info));
        }

        HELP_KEY_MAX_WIDTH = maxKey;
        HELP_INFO_MAX_WIDTH = maxInfo;
    }

    private void updateFieldsFromSelectedHUD() {
        super.setFocused(null);

        if (selectedHUDs.isEmpty()) {
            xField.setEditable(false);
            yField.setEditable(false);
            scaleField.setEditable(false);
            xField.setText("N/A");
            yField.setText("N/A");
            scaleField.setText("N/A");

            alignmentXButton.setMessage(Text.of("X Alignment: N/A"));
            directionXButton.setMessage(Text.of("X Direction: N/A"));
            alignmentYButton.setMessage(Text.of("Y Alignment: N/A"));
            directionYButton.setMessage(Text.of("Y Direction: N/A"));
            hudDisplayButton.setMessage(Text.of("Display: N/A"));
            drawBackgroundButton.setMessage(Text.of("Background: N/A"));
            shouldRenderButton.setMessage(Text.of("N/A"));

            gapField.setText("N/A");
            groupAlignmentButton.setMessage(Text.of("N/A"));
            childAlignmentButton.setMessage(Text.of("N/A"));

            gapField.setEditable(false);
            gapField.visible = false;
            groupAlignmentButton.visible = false;
            groupAlignmentButton.active = false;
            childAlignmentButton.visible = false;
            childAlignmentButton.active = false;

            alignmentXButton.active = false;
            directionXButton.active = false;
            alignmentYButton.active = false;
            directionYButton.active = false;
            hudDisplayButton.active = false;
            drawBackgroundButton.active = false;
            shouldRenderButton.active = false;

            groupUngroupButton.active = false;
            groupUngroupButton.visible = false;

            canSelectedHUDUngroup = false;
            canSelectedHUDsGroup = false;
        } else {
            AbstractHUD firstHUD = selectedHUDs.getFirst();
            BaseHUDSettings settings = firstHUD.getSettings();
            xField.setText(String.valueOf(settings.x));
            yField.setText(String.valueOf(settings.y));
            scaleField.setText(String.valueOf(settings.getScale()));

            alignmentXButton.setMessage(Text.of("X Alignment: " + settings.getAlignmentX()));
            directionXButton.setMessage(Text.of("X Direction: " + settings.getGrowthDirectionX()));
            alignmentYButton.setMessage(Text.of("Y Alignment: " + settings.getAlignmentY()));
            directionYButton.setMessage(Text.of("Y Direction: " + settings.getGrowthDirectionY()));
            hudDisplayButton.setMessage(Text.of("Display: " + settings.getDisplayMode()));
            drawBackgroundButton.setMessage(Text.of("Background: " + (settings.drawBackground ? "ON" : "OFF")));
            shouldRenderButton.setMessage(Text.of(settings.shouldRender ? "ON" : "OFF"));

            alignmentXButton.active = true;
            directionXButton.active = true;
            alignmentYButton.active = true;
            directionYButton.active = true;
            hudDisplayButton.active = true;
            drawBackgroundButton.active = true;
            shouldRenderButton.active = true;
            scaleField.setEditable(true);
            xField.setEditable(true);
            yField.setEditable(true);

            gapField.visible = false;
            groupAlignmentButton.visible = false;
            childAlignmentButton.visible = false;

            canSelectedHUDUngroup =  (selectedHUDs.size() == 1 && firstHUD instanceof GroupedHUD && !firstHUD.isInGroup());
            canSelectedHUDsGroup = (selectedHUDs.size() > 1 && selectedHUDs.stream().noneMatch(AbstractHUD::isInGroup));

            if (canSelectedHUDsGroup) {
                groupUngroupButton.setMessage(Text.of("Group"));
                groupUngroupButton.visible = true;
                groupUngroupButton.active = true;
            } else if (canSelectedHUDUngroup) {
                groupUngroupButton.setMessage(Text.of("Ungroup"));
                groupUngroupButton.visible = true;
                groupUngroupButton.active = true;
            } else {
                groupUngroupButton.visible = false;
                groupUngroupButton.active = false;
            }

            if (firstHUD instanceof GroupedHUD hud && isMoreOptionActivated) {
                gapField.visible = true;
                groupAlignmentButton.visible = true;
                childAlignmentButton.visible = true;

                gapField.setEditable(true);
                groupAlignmentButton.active = true;
                childAlignmentButton.active = true;

                gapField.setText(
                        Integer.toString(hud.groupSettings.gap)
                );

                groupAlignmentButton.setMessage(Text.of(
                        hud.groupSettings.alignVertical ? "Vertical" : "Horizontal"
                ));

                childAlignmentButton.setMessage(Text.of(hud.groupSettings.getChildAlignment().name()));

            }

            if (isMoreOptionActivated) {
                alignmentXButton.visible = true;
                directionXButton.visible = true;
                alignmentYButton.visible = true;
                directionYButton.visible = true;
                hudDisplayButton.visible = true;
                drawBackgroundButton.visible = true;
                shouldRenderButton.visible = true;
                xField.visible = true;
                yField.visible = true;
                scaleField.visible = true;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        // draw basic grid for convenience
        if (SETTINGS.drawGrid) {
            final int CENTER_X = this.width / 2;
            final int CENTER_Y = this.height / 2;
            int gridEdgePadding = Math.max(SETTINGS.gridEdgePadding, 0);
            if (gridEdgePadding > 0)
                context.drawBorder(gridEdgePadding, gridEdgePadding, this.width - (gridEdgePadding * 2), this.height - (gridEdgePadding * 2), SETTINGS.gridColor);
            context.drawHorizontalLine((gridEdgePadding + 1), this.width - (gridEdgePadding + 2), CENTER_Y, SETTINGS.gridColor);
            context.drawVerticalLine(CENTER_X, (gridEdgePadding), this.height - (gridEdgePadding + 1), SETTINGS.gridColor);
        }

        super.render(context, mouseX, mouseY, delta);

        // draw help
        if (isHelpActivated) {
            final int CENTER_X = this.width / 2;
            final int CENTER_Y = this.height / 2 + (PADDING / 2);
            renderHelp(context, CENTER_X, CENTER_Y + GAP);
            if (!selectedHUDs.isEmpty())
                renderHUDInformation(context, CENTER_X, CENTER_Y + GAP  + HELP_HEIGHT + GAP);
        }

        // draw X and Y next to their textField.
        if (xField.isVisible() && yField.isVisible()) {
            context.drawText(CLIENT.textRenderer, "X:", xField.getX() - 5 - 2 - 3, xField.getY() + 6, 0xFFFFFFFF, true);
            context.drawText(CLIENT.textRenderer, ":Y", yField.getX() + yField.getWidth() + 3, yField.getY() + 6, 0xFFFFFFFF, true);
        }

        if (gapField.isVisible()) {
            context.drawText(CLIENT.textRenderer, "GAP:", gapField.getX() - 20 - 3, gapField.getY() + 6, 0xFFFFFFFF, true);
        }

        if (dragSelection && hasMovedSincePress) {
            renderDragBox(context);
        }

        // draw all visible hud bounding boxes.
        renderBoundingBoxes(context, mouseX, mouseY);
    }

    private void renderDragBox(DrawContext context) {
        int x1 = Math.min(dragStartX, dragCurrentX);
        int y1 = Math.min(dragStartY, dragCurrentY);
        int x2 = Math.max(dragStartX, dragCurrentX);
        int y2 = Math.max(dragStartY, dragCurrentY);

        int width = x2 - x1;
        int height = y2 - y1;
        int color = SETTINGS.dragBoxColor;

        if (width > 0 && height > 0) {
            context.fill(x1, y1, x2, y2, color | 0x40000000);

            if (SETTINGS.drawBorder)
                context.drawBorder(x1, y1, width, height, color | 0xFF000000);
        }
    }

    private void renderBoundingBoxes(DrawContext context, int mouseX, int mouseY) {
        HUDComponent.getInstance().renderAll(context);

        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            if (hud.isScaled()) {
                context.getMatrices().pushMatrix();
                hud.scaleHUD(context);
                renderBoundingBox(context, hud, mouseX, mouseY);
                context.getMatrices().popMatrix();
            } else {
                renderBoundingBox(context, hud, mouseX, mouseY);
            }
        }

        for (AbstractHUD hud : selectedHUDs) {
            if (hud.isScaled()) {
                context.getMatrices().pushMatrix();
                hud.scaleHUD(context);
                renderSelectedBox(context, hud);
                context.getMatrices().popMatrix();
            } else {
                renderSelectedBox(context, hud);
            }
        }
    }

    private void renderSelectedBox(DrawContext context, AbstractHUD hud) {
        Box box = hud.getBoundingBox();

        int x = box.getX();
        int y = box.getY();
        int width = box.getWidth();
        int height = box.getHeight();
        int color = (hud instanceof GroupedHUD ? SETTINGS.selectedGroupBoxColor : SETTINGS.selectedBoxColor);

        if (hud.isInGroup()) {
            context.drawBorder(x, y, width, height, color | 0xFF000000);
        } else {
            context.fill(x, y, x + width, y + height, color);
        }
    }

    private void renderBoundingBox(DrawContext context, AbstractHUD hud, int mouseX, int mouseY) {
        Box boundingBox = hud.getBoundingBox();

        int x = boundingBox.getX();
        int y = boundingBox.getY();
        int width = boundingBox.getWidth();
        int height = boundingBox.getHeight();
        int color = boundingBox.getColor();

        if (SETTINGS.drawBorder)
            context.drawBorder(x, y, width, height, color);
        if (hud.isHovered(mouseX, mouseY)) {
            context.fill(x, y, x + width, y + height, (color & 0x00FFFFFF) | 0x80000000);
        }
    }

    private void renderHelp(DrawContext context, int x, int y) {
        int padding = 5;

        int lineHeight = CLIENT.textRenderer.fontHeight;

        int maxKeyWidth = HELP_KEY_MAX_WIDTH;
        int maxInfoWidth = HELP_INFO_MAX_WIDTH;

        int width = padding + maxKeyWidth + padding + 1 + padding + maxInfoWidth + padding;
        int height = padding + (lineHeight * HELPS.length) + padding - 2;

        x -= width / 2;
        y -= padding;

        context.fill(x, y, x + width, y + height, 0x80000000);

        for (Help h : HELPS) {
            Text key = h.key;
            Text info = h.info;

            context.drawText(CLIENT.textRenderer, key, x + padding, y + padding, 0xFFFFFFFF, false);
            context.drawText(CLIENT.textRenderer, info, x + padding + maxKeyWidth + padding + 1 + padding, y + padding, 0xFFFFFFFF, false);

            y += lineHeight;
        }
    }

    private void renderHUDInformation(DrawContext context, int x, int y) {
        String text = selectedHUDs.getFirst().getName();
        int textWidth = CLIENT.textRenderer.getWidth(text);
        int padding = 5;

        x -= (textWidth / 2);

        context.fill(x - padding, y - padding, x + textWidth + padding, y + CLIENT.textRenderer.fontHeight - 2 + padding, 0x80000000);
        context.drawText(CLIENT.textRenderer, text, x, y, 0xFFFFFFFF, false);
    }

    boolean dragSelection = false;
    int dragStartX, dragStartY;
    int dragCurrentX, dragCurrentY;

    private final Set<AbstractHUD> initialDragBoxSelection = new HashSet<>();
    private boolean hasMovedSincePress = false;
    private static final int DRAG_THRESHOLD = 3; // pixels
    private AbstractHUD clickedHUD = null;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        if (button == 0) {
            hasMovedSincePress = false;
            dragStartX = (int) mouseX;
            dragStartY = (int) mouseY;
            dragCurrentX = (int) mouseX;
            dragCurrentY = (int) mouseY;

            // find which HUD was clicked (if any)
            clickedHUD = getHUDAtPosition(mouseX, mouseY);

            if (clickedHUD != null) {
                handleHUDClick(clickedHUD, mouseX, mouseY);
            } else {
                handleEmptySpaceClick();
            }

            // store initial selection for drag box operations
            initialDragBoxSelection.clear();
            initialDragBoxSelection.addAll(selectedHUDs);
        }
        return true;
    }

    private AbstractHUD getHUDAtPosition(double mouseX, double mouseY) {
        for (AbstractHUD hud : selectedHUDs) {
            if (hud.isHovered((int) mouseX, (int) mouseY))
                return hud;
        }

        for (AbstractHUD hud : groupedHUDs.values()) {
            if (!hud.isInGroup())
                if (isHUDClickable(hud, mouseX, mouseY))
                    return hud;
        }

        for (AbstractHUD hud : individualHUDs.values()) {
            if (isHUDClickable(hud, mouseX, mouseY)) {
                return hud;
            }
        }

        return null;
    }

    private boolean isHUDClickable(AbstractHUD hud, double mouseX, double mouseY) {
        if (!hud.shouldRender()) return false;

        Box boundingBox = hud.getBoundingBox();
        if (boundingBox.isEmpty()) return false;

        return hud.isHovered(
                (int) mouseX,
                (int) mouseY
        );
    }

    private boolean pendingChildClick;
    private void handleHUDClick(AbstractHUD clickedHUD, double mouseX, double mouseY) {
        if (Screen.hasShiftDown()) {
            // shift click: Add to selection (don't remove if already selected)
            if (!selectedHUDs.contains(clickedHUD)) {
                selectedHUDs.add(clickedHUD);
            }
            // if already selected, we'll handle potential removal in mouseReleased
            pendingToggleHUD = selectedHUDs.contains(clickedHUD) ? clickedHUD : null;
        } else if (Screen.hasControlDown()) {
            // ctrl click: toggle selection
            if (selectedHUDs.contains(clickedHUD)) {
                pendingToggleHUD = clickedHUD; // remove on release if no drag
            } else {
                selectedHUDs.add(clickedHUD);
                pendingToggleHUD = null;
            }
        } else {
            // click
            if (selectedHUDs.contains(clickedHUD)) {
                // clicking on already selected item - don't change selection yet
                // (might be starting a multi-HUD drag)
                pendingToggleHUD = null;

                if (clickedHUD instanceof GroupedHUD)
                    pendingChildClick = true;
            } else {
                // clicking on unselected item - select only this one
                selectedHUDs.clear();
                selectedHUDs.add(clickedHUD);
                pendingToggleHUD = null;
            }
        }

        // Prepare for potential dragging
        dragging = true;
        updateFieldsFromSelectedHUD();
    }

    private void handleEmptySpaceClick() {
        if (!Screen.hasShiftDown() && !Screen.hasControlDown()) {
            // click on empty space - clear selection
            selectedHUDs.clear();
            updateFieldsFromSelectedHUD();
        }

        // prepare for drag box selection
        dragSelection = true;
        pendingToggleHUD = null;
    }

    public AbstractHUD pendingToggleHUD = null;

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (!hasMovedSincePress) {
                // if mouse hasn't moved since clicked to release, we handle non mouse moved operation
                handleClickRelease(mouseX, mouseY);
            }

            // Finalize any drag operations
            if (dragging) {
                finalizeDragOperation();
            }

            if (dragSelection) {
                dragSelection = false;
            }

            resetMouseState();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void handleClickRelease(double mouseX, double mouseY) {
        // Handle pending toggle operations (for ctrl click and shift click)
        if (pendingToggleHUD != null) {
            if (Screen.hasShiftDown()) {
                // shift click on already selected: remove from selection
                selectedHUDs.remove(pendingToggleHUD);
            } else if (Screen.hasControlDown()) {
                // ctrl click toggle: remove from selection
                selectedHUDs.remove(pendingToggleHUD);
            }
            updateFieldsFromSelectedHUD();
        }

        // Handle single-click deselection for multi-selection
        if (clickedHUD != null && !Screen.hasShiftDown() && !Screen.hasControlDown()) {
            if (pendingChildClick && clickedHUD instanceof GroupedHUD group) {
                AbstractHUD hoveredChild = null;

                for (AbstractHUD hud : group.huds) {
                    if (hud.isHovered((int) mouseX, (int) mouseY)) {
                        hoveredChild = hud;
                        break;
                    }
                }
                if (hoveredChild != null) {
                    selectedHUDs.clear();
                    selectedHUDs.add(hoveredChild);
                    updateFieldsFromSelectedHUD();
                }
            } else if (selectedHUDs.contains(clickedHUD) && selectedHUDs.size() > 1) {
                // Single click on item in multi-selection should select only that item
                selectedHUDs.clear();
                selectedHUDs.add(clickedHUD);
                updateFieldsFromSelectedHUD();
            }
        }
    }

    private void finalizeDragOperation() {
        dragging = false;

        // Update final positions in text fields
        if (!selectedHUDs.isEmpty()) {
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            xField.setText(String.valueOf(selectedHUD.getSettings().x));
            yField.setText(String.valueOf(selectedHUD.getSettings().y));
        }

        hudAccumulatedDelta.clear();
    }

    private void resetMouseState() {
        hasMovedSincePress = false;
        clickedHUD = null;
        pendingToggleHUD = null;
        initialDragBoxSelection.clear();

        pendingChildClick = false;
    }

    private final Map<AbstractHUD, Vector2d> hudAccumulatedDelta = new HashMap<>();

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button != 0) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        // check if we've moved enough to start drag operation
        if (!hasMovedSincePress) {
            int totalMovement = Math.abs((int)mouseX - dragStartX) + Math.abs((int)mouseY - dragStartY);
            if (totalMovement >= DRAG_THRESHOLD) {
                hasMovedSincePress = true;
                startDragOperation();
            }
        }

        if (hasMovedSincePress) {
            if (dragging && !selectedHUDs.isEmpty()) { // if we've moved and there are selected huds, we drag them, obviously
                dragSelectedHUDs(deltaX, deltaY);
                return true;
            } else if (dragSelection) { // otherwise it's just drag box
                updateDragBoxSelection(mouseX, mouseY);
                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void startDragOperation() {
        // clear any pending toggle since we're now dragging
        pendingToggleHUD = null;

        // if moved + has hud selected -> potential hud(s) dragging.
        if (clickedHUD != null && selectedHUDs.contains(clickedHUD)) {
            dragSelection = false;
        } else { // if moved but no hud selected -> potential drag box

            dragging = false;

            // if we clicked on a HUD, but it wasn't selected, and no modifiers,
            // clear selection first
            if (clickedHUD != null && !Screen.hasShiftDown() && !Screen.hasControlDown()) {
                selectedHUDs.clear();
                initialDragBoxSelection.clear();
            }
        }
    }

    private void dragSelectedHUDs(double deltaX, double deltaY) {
        for (AbstractHUD hud : selectedHUDs) {
            if (hud.isInGroup()) continue;

            double scaleFactor = hud.getSettings().getScaledFactor();

            Vector2d acc = hudAccumulatedDelta.computeIfAbsent(hud, h -> new Vector2d(0, 0));
            acc.x += deltaX;
            acc.y += deltaY;

            double scaledX = acc.x * scaleFactor;
            double scaledY = acc.y * scaleFactor;

            int dx = (int) scaledX;
            int dy = (int) scaledY;

            if (dx != 0 || dy != 0) {
                hud.getSettings().x += dx;
                hud.getSettings().y += dy;
                hud.update();

                acc.x -= dx / scaleFactor;
                acc.y -= dy / scaleFactor;
            }
        }

        if (!selectedHUDs.isEmpty()) {
            AbstractHUD firstSelected = selectedHUDs.getFirst();
            xField.setText(String.valueOf(firstSelected.getSettings().x));
            yField.setText(String.valueOf(firstSelected.getSettings().y));
        }
    }

    private void updateDragBoxSelection(double mouseX, double mouseY) {
        dragCurrentX = (int) mouseX;
        dragCurrentY = (int) mouseY;

        int x1 = Math.min(dragStartX, dragCurrentX);
        int y1 = Math.min(dragStartY, dragCurrentY);
        int x2 = Math.max(dragStartX, dragCurrentX);
        int y2 = Math.max(dragStartY, dragCurrentY);

        Set<AbstractHUD> boxSelectedHUDs = new HashSet<>();

        for (AbstractHUD hud : individualHUDs.values()) {
            if (hud.shouldRender() && !hud.getBoundingBox().isEmpty()) {
                if (intersectsBox(x1, y1, x2, y2, hud)) {
                    boxSelectedHUDs.add(hud);
                }
            }
        }

        for (AbstractHUD hud : groupedHUDs.values()) {
            if (!hud.isInGroup() && hud.shouldRender() && !hud.getBoundingBox().isEmpty()) {
                if (intersectsBox(x1, y1, x2, y2, hud)) {
                    boxSelectedHUDs.add(hud);
                }
            }
        }

        // Apply drag box selection based on modifier keys
        if (Screen.hasShiftDown()) {
            // shift drag box: Add new items to existing selection
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (!selectedHUDs.contains(hud)) { // only add if not already selected
                    selectedHUDs.add(hud);
                }
            }
        } else if (Screen.hasControlDown()) {
            // ctrl drag box: invert items in box
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (initialDragBoxSelection.contains(hud)) {
                    selectedHUDs.remove(hud); // remove if was initially selected
                } else if (!selectedHUDs.contains(hud)) {
                    selectedHUDs.add(hud); // add if not currently selected
                }
            }
        } else {
            // click: update selection, remove the ones that didnt get caught, add the one that did get caught
            selectedHUDs.removeIf(hud -> !boxSelectedHUDs.contains(hud));

            for (AbstractHUD hud : boxSelectedHUDs) {
                 if (!selectedHUDs.contains(hud)) {
                    selectedHUDs.add(hud); // add if not currently selected
                }
            }
        }

        updateFieldsFromSelectedHUD();
    }

    public boolean isTextFieldsFocused() {
        return xField.isFocused() || yField.isFocused() || gapField.isFocused() || scaleField.isFocused();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isTextFieldsFocused())
            return super.keyPressed(keyCode, scanCode, modifiers);

        if (!dragSelection && !dragging) {

            boolean handled = false;

            if (!selectedHUDs.isEmpty())
                for (AbstractHUD hud : selectedHUDs)
                    if (onKeyPressed(hud, keyCode, modifiers))
                        handled = true;

            boolean isCtrl = isMac
                    ? (modifiers & GLFW.GLFW_MOD_SUPER) != 0
                    : (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;

            switch (keyCode) {
                case GLFW.GLFW_KEY_G -> {
                    if (selectedHUDs.isEmpty()) break;
                    if (selectedHUDs.size() > 1) {
                        if (canSelectedHUDsGroup) {
                            group(selectedHUDs);
                            selectedHUDs.clear();
                            handled = true;
                        }
                    } else {
                        if (canSelectedHUDUngroup) {
                            unGroup((GroupedHUD) selectedHUDs.getFirst());
                            selectedHUDs.clear();
                            handled = true;
                        }
                    }
                }
                case GLFW.GLFW_KEY_R -> {
                    if (isCtrl) {
                        revertChanges();
                        selectedHUDs.clear();
                        handled = true;
                    }
                }
            }

            if (handled) {
                updateFieldsFromSelectedHUD();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean onKeyPressed(AbstractHUD hud, int keyCode, int modifiers) {

        BaseHUDSettings settings = hud.getSettings();

        boolean handled = false;
        boolean isCtrl = isMac
                ? (modifiers & GLFW.GLFW_MOD_SUPER) != 0
                : (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
        boolean isShift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
        boolean isAlt = (modifiers & GLFW.GLFW_MOD_ALT) != 0;

        int step = isShift ? 5 : 1;

        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT -> {
                if (isCtrl) {
                    settings.alignmentX = settings.alignmentX.prev();
                    settings.growthDirectionX = settings.growthDirectionX.recommendedScreenAlignment(settings.alignmentX);
                }
                else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.prev();
                else settings.x -= step;

                handled = true;
            }

            case GLFW.GLFW_KEY_RIGHT -> {
                if (isCtrl) {
                    settings.alignmentX = settings.alignmentX.next();
                    settings.growthDirectionX = settings.growthDirectionX.recommendedScreenAlignment(settings.alignmentX);
                }
                else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.next();
                else settings.x += step;

                handled = true;
            }

            case GLFW.GLFW_KEY_UP -> {
                if (isCtrl) {
                    settings.alignmentY = settings.alignmentY.prev();
                    settings.growthDirectionY = settings.growthDirectionY.recommendedScreenAlignment(settings.alignmentY);
                }
                else if (isAlt) {
                    settings.growthDirectionY = settings.growthDirectionY.prev();
                }
                else settings.y -= step;

                handled = true;
            }

            case GLFW.GLFW_KEY_DOWN -> {
                if (isCtrl) {
                    settings.alignmentY = settings.alignmentY.next();
                    settings.growthDirectionY = settings.growthDirectionY.recommendedScreenAlignment(settings.alignmentY);
                }
                else if (isAlt) settings.growthDirectionY = settings.growthDirectionY.next();
                else settings.y += step;

                handled = true;
            }

            case GLFW.GLFW_KEY_MINUS ->  {
                if (!isShift) {
                    if (settings.scale <= 0) break;

                    settings.scale -= 1;
                    handled = true;
                }
            }

            case GLFW.GLFW_KEY_EQUAL ->  {
                if (isShift) {
                    settings.scale += 1;
                    handled = true;
                }
            }
        }

        if (handled) {
            hud.update();
            updateFieldsFromSelectedHUD();
        }

        return handled;
    }

    private boolean intersectsBox(int x1, int y1, int x2, int y2, AbstractHUD hud) {
        Box box = hud.getBoundingBox();

        float scale = hud.getSettings().getScale() == 0 ? 1.0f : (float) MinecraftClient.getInstance().getWindow().getScaleFactor() / hud.getSettings().getScale();

        int scaledX1 = (int) (x1 * scale);
        int scaledY1 = (int) (y1 * scale);
        int scaledX2 = (int) (x2 * scale);
        int scaledY2 = (int) (y2 * scale);

        int hudLeft   = box.getX();
        int hudTop    = box.getY();
        int hudRight  = box.getX() + box.getWidth();
        int hudBottom = box.getY() + box.getHeight();

        return hudRight >= Math.min(scaledX1, scaledX2) &&
                hudLeft  <= Math.max(scaledX1, scaledX2) &&
                hudBottom >= Math.min(scaledY1, scaledY2) &&
                hudTop    <= Math.max(scaledY1, scaledY2);
    }


    private boolean isDirty() {
        List<String> individualIds = Main.settings.hudList.individualHudIds;
        List<GroupedHUDSettings> groupedHUDs = Main.settings.hudList.groupedHuds;

        if (!individualIds.equals(oldIndividualHudIds))
            return true;
        if (!groupedHUDs.equals(oldGroupedHUDs))
            return true;

        for (HUDId id : HUDId.values()) {
            AbstractHUD hud = HUDComponent.getInstance().getHUD(id);
            BaseHUDSettings current = hud.getSettings();
            BaseHUDSettings original = oldHUDSettings.get(id.toString());
            if (original == null || !current.isEqual(original)) {
                return true;
            }
        }

        for (GroupedHUDSettings current : groupedHUDs) {
            GroupedHUDSettings original = oldGroupedHUDSettings.get(current.id);
            if (!current.isEqual(original)) {
                return true;
            }
        }

        return false;
    }


    private void revertChanges() {
        Main.settings.hudList.individualHudIds.clear();
        Main.settings.hudList.individualHudIds.addAll(oldIndividualHudIds);
        Main.settings.hudList.groupedHuds.clear();
        Main.settings.hudList.groupedHuds.addAll(oldGroupedHUDs);

        HUDComponent.getInstance().updateActiveHUDs();

        for (HUDId id : HUDId.values()) {
            AbstractHUD hud = HUDComponent.getInstance().getHUD(id);
            BaseHUDSettings original = oldHUDSettings.get(id.toString());
            if (original != null) {
                hud.getSettings().copySettings(original);
//                LOGGER.info("Reverting {} Settings", hud.getName());
            } else {
                LOGGER.warn("Original Settings is not found! for {}", hud.getName());
            }
        }

        for (GroupedHUD hud : groupedHUDs.values()) {
            GroupedHUDSettings original = oldGroupedHUDSettings.get(hud.groupSettings.id);
            if (original != null) {
                hud.groupSettings.copyFrom(original);
//                LOGGER.info("Reverting Group ({}) Settings", hud.getName());
            } else {
                LOGGER.warn("Original Settings is not found for Group ({})!", hud.getName());
            }
        }

        AutoConfig.getConfigHolder(Settings.class).save();
    }


    @Override
    public void close() {
        if (isDirty()) {
            this.client.setScreen(new ConfirmScreen(
                    result -> {
                        if (result) {
                            revertChanges();
                            onClose();
                        } else {
                            this.client.setScreen(this);
                        }
                    },
                    Text.of("Discard Changes?"),
                    Text.of("You have unsaved changes. Do you want to discard them?")
            ));
        } else {
            onClose();
        }
    }

    public void onClose() {
        this.client.setScreen(this.parent);
    }

    private void onHelpSwitched() {
    }

    private void onMoreOptionSwitched() {
        if (isMoreOptionActivated) {
            alignmentXButton.visible = true;
            directionXButton.visible = true;
            alignmentYButton.visible = true;
            directionYButton.visible = true;
            hudDisplayButton.visible = true;
            drawBackgroundButton.visible = true;
            xField.visible = true;
            yField.visible = true;
            scaleField.visible = true;
            shouldRenderButton.visible = true;

            if (!selectedHUDs.isEmpty() && selectedHUDs.getFirst() instanceof GroupedHUD hud) {
                gapField.visible = true;
                groupAlignmentButton.visible = true;
                childAlignmentButton.visible = true;

                gapField.setEditable(true);
                groupAlignmentButton.active = true;
                childAlignmentButton.active = true;

                gapField.setText(
                        Integer.toString(hud.groupSettings.gap)
                );

                groupAlignmentButton.setMessage(Text.of(
                        hud.groupSettings.alignVertical ? "Vertical" : "Horizontal"
                ));

                childAlignmentButton.setMessage(Text.of(hud.groupSettings.getChildAlignment().name()));
            }
        } else {
            alignmentXButton.visible = false;
            directionXButton.visible = false;
            alignmentYButton.visible = false;
            directionYButton.visible = false;
            hudDisplayButton.visible = false;
            drawBackgroundButton.visible = false;
            xField.visible = false;
            yField.visible = false;
            scaleField.visible = false;
            shouldRenderButton.visible = false;

            gapField.visible = false;
            groupAlignmentButton.visible = false;
            childAlignmentButton.visible = false;
        }
    }

    // grouping function, experimental, may crash.

    // hud in huds MUST be ungrouped. not doing so will crash.
    public void group(List<AbstractHUD> huds) {
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
}
