package fin.starhud.screen;

import fin.starhud.Main;
import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.GroupedHUDSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.*;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.GroupedHUD;
import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
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
    private ButtonWidget clampPosButton;

    private final List<ButtonWidget> moreOptionButtons = new ArrayList<>();
    private final List<TextFieldWidget> moreOptionTexts = new ArrayList<>();

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
            new Help(Text.translatable("starhud.help.key.group_ungroup"), Text.translatable("starhud.help.info.group_ungroup")),
            new Help(Text.translatable("starhud.help.key.clamp_all"), Text.translatable("starhud.help.info.clamp_all"))
    };

    private static int HELP_KEY_MAX_WIDTH;
    private static int HELP_INFO_MAX_WIDTH;
    private static final int HELP_HEIGHT = 5 + (HELPS.length * 9) + 5;

    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

        groupedHUDs = HUDComponent.getInstance().getGroupedHUDs();

        Map<String, AbstractHUD> HUDMap = HUDComponent.getInstance().getHudMap();

        oldHUDSettings = new HashMap<>();
        for (AbstractHUD p : HUDMap.values()) {
            BaseHUDSettings settings = p.getSettings();
            oldHUDSettings.put(p.getId(), new BaseHUDSettings(settings.shouldRender, settings.x, settings.y, settings.originX, settings.originY, settings.growthDirectionX, settings.growthDirectionY, settings.scale, settings.displayMode, settings.drawBackground));
        }

        oldGroupedHUDSettings = new HashMap<>();
        for (GroupedHUD p : groupedHUDs.values()) {
            BaseHUDSettings settings = p.getSettings();
            oldGroupedHUDSettings.put(
                    p.getId(),
                    new GroupedHUDSettings(
                            new BaseHUDSettings(settings.shouldRender, settings.x, settings.y, settings.originX, settings.originY,  settings.growthDirectionX, settings.growthDirectionY, settings.scale, settings.displayMode, settings.drawBackground),
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
        xField = new TextFieldWidget(
                CLIENT.textRenderer,
                xFieldX, CENTER_Y,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.translatable("starhud.screen.field.x")
        );

        int yFieldX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;
        yField = new TextFieldWidget(
                CLIENT.textRenderer,
                yFieldX, CENTER_Y,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.translatable("starhud.screen.field.y")
        );

        int alignmentXButtonX = CENTER_X - WIDGET_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP;
        int alignmentXButtonY = CENTER_Y - PADDING;
        alignmentXButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.x_alignment_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().originX = selectedHUD.getSettings().originX.next();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.recommendedScreenAlignment(selectedHUD.getSettings().originX);
                    selectedHUD.update();
                    alignmentXButton.setMessage(Text.translatable("starhud.screen.button.x_alignment", selectedHUD.getSettings().getOriginX().toString()));
                }
        ).dimensions(alignmentXButtonX, alignmentXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int alignmentYButtonX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;
        alignmentYButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.y_alignment_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().originY = selectedHUD.getSettings().originY.next();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.recommendedScreenAlignment(selectedHUD.getSettings().originY);
                    selectedHUD.update();
                    alignmentYButton.setMessage(Text.translatable("starhud.screen.button.y_alignment", selectedHUD.getSettings().getOriginY().toString()));
                }
        ).dimensions(alignmentYButtonX, alignmentXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int directionXButtonY = alignmentXButtonY - PADDING;
        directionXButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.x_direction_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.next();
                    selectedHUD.update();
                    directionXButton.setMessage(Text.translatable("starhud.screen.button.x_direction", selectedHUD.getSettings().getGrowthDirectionX().toString()));
                }
        ).dimensions(alignmentXButtonX, directionXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int scaleFieldWidth = SQUARE_WIDGET_LENGTH + 6;
        int scaleFieldX = CENTER_X - (scaleFieldWidth / 2);
        scaleField = new TextFieldWidget(
                CLIENT.textRenderer,
                scaleFieldX, directionXButtonY,
                scaleFieldWidth,
                SQUARE_WIDGET_LENGTH,
                Text.translatable("starhud.screen.field.scale")
        );

        directionYButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.y_direction_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.next();
                    selectedHUD.update();
                    directionYButton.setMessage(Text.translatable("starhud.screen.button.y_direction", selectedHUD.getSettings().getGrowthDirectionY().toString()));
                }
        ).dimensions(alignmentYButtonX, directionXButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int hudDisplayButtonY = directionXButtonY - PADDING;
        hudDisplayButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.display_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().displayMode = selectedHUD.getSettings().displayMode.next();
                    selectedHUD.update();
                    hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display", selectedHUD.getSettings().getDisplayMode().toString()));
                }
        ).dimensions(alignmentXButtonX, hudDisplayButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        drawBackgroundButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.background_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().drawBackground = !selectedHUD.getSettings().drawBackground;
                    selectedHUD.update();
                    drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background", 
                            selectedHUD.getSettings().drawBackground ? 
                                    Text.translatable("starhud.screen.status.on").getString() : 
                                    Text.translatable("starhud.screen.status.off").getString()));
                }
        ).dimensions(alignmentYButtonX, hudDisplayButtonY, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int wClampPosButton = SQUARE_WIDGET_LENGTH;
        int hClampPosButton = SQUARE_WIDGET_LENGTH;
        int xClampPosButton = (this.width - wClampPosButton) / 2;
        int yClampPosButton = hudDisplayButtonY;
        clampPosButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.clamp_pos"),
                button -> {
                    HUDComponent.getInstance().clampAll();
                }
        )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.clamp_pos")))
                .dimensions(xClampPosButton, yClampPosButton, wClampPosButton, hClampPosButton).build();


        int shouldRenderButtonX = CENTER_X - (SQUARE_WIDGET_LENGTH / 2);
        shouldRenderButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    selectedHUD.getSettings().shouldRender = !selectedHUD.getSettings().shouldRender;
                    selectedHUD.update();
                    button.setMessage(selectedHUD.getSettings().shouldRender ? 
                            Text.translatable("starhud.screen.status.on") : 
                            Text.translatable("starhud.screen.status.off"));
                }
        ).dimensions(shouldRenderButtonX, alignmentXButtonY, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH).build();

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

        scaleField.setTooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.scale")));
        scaleField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            try {
                selectedHUD.getSettings().scale = Float.parseFloat(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        int yBottom = this.height - SQUARE_WIDGET_LENGTH - GAP;

        int wConfigScreen = SQUARE_WIDGET_LENGTH;
        int hConfigScreen = wConfigScreen;
        int xConfigScreenButton = CENTER_X - (wConfigScreen / 2);
        ButtonWidget configScreenButton = ButtonWidget.builder(
                        Text.translatable("starhud.screen.button.config"),
                        button -> {
                            isHelpActivated = false;
                            isMoreOptionActivated = false;
                            selectedHUDs.clear();
                            if (this.client == null) return;
                            this.client.setScreen(AutoConfigClient.getConfigScreen(Settings.class, this).get());
                        }
                )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.config")))
                .dimensions(xConfigScreenButton, CENTER_Y, wConfigScreen, hConfigScreen)
                .build();

        int xHelpButton = CENTER_X - (GAP/2) - SQUARE_WIDGET_LENGTH;
        ButtonWidget helpButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.help"),
                button -> {
                    isHelpActivated = !isHelpActivated;
                    onHelpSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.help")))
                .dimensions(xHelpButton, yBottom, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();

        int xMoreOptionButton = CENTER_X + (GAP/2);
        ButtonWidget moreOptionButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.more_options"),
                button -> {
                    isMoreOptionActivated = !isMoreOptionActivated;
                    onMoreOptionSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.more_options")))
                .dimensions(xMoreOptionButton, yBottom, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();

        int terminatorWidth = 70;
        int xSaveAndQuitButton = xMoreOptionButton + GAP + SQUARE_WIDGET_LENGTH;
        ButtonWidget saveAndQuitButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.save_quit"),
                button -> {
                    AutoConfig.getConfigHolder(Settings.class).save();
                    onClose();
        }).dimensions(xSaveAndQuitButton, yBottom, terminatorWidth, WIDGET_HEIGHT).build();

        int xCancelButton = xHelpButton - GAP - terminatorWidth;

        ButtonWidget cancelButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.cancel"),
                button -> close()
        ).dimensions(xCancelButton, yBottom, terminatorWidth, WIDGET_HEIGHT).build();

        // special case: grouped hud buttons

        int yBottomGroup = CENTER_Y + PADDING;
        int xGroupUngroupButton = CENTER_X - (terminatorWidth / 2);

        groupUngroupButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (canSelectedHUDsGroup) {
                        HUDComponent.getInstance().group(selectedHUDs);
                    } else if (canSelectedHUDUngroup) {
                        HUDComponent.getInstance().unGroup((GroupedHUD) selectedHUDs.getFirst());
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
                Text.translatable("starhud.screen.field.gap")
        );

        gapField.setChangedListener(text -> {
            if (selectedHUDs.isEmpty()) return;
            if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;

            try {
                hud.groupSettings.gap = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        });

        int xChildAlignmentButton = xGroupUngroupButton - GAP - terminatorWidth;
        childAlignmentButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;
                    hud.groupSettings.childAlignment = hud.groupSettings.getChildAlignment().next();

                    button.setMessage(Text.of(hud.groupSettings.getChildAlignment().toString()));
                }
        )
                .dimensions(xChildAlignmentButton, yBottomGroup, terminatorWidth, SQUARE_WIDGET_LENGTH)
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.child_alignment")))
                .build();

        int xGroupAlignmentButton = xGroupUngroupButton + terminatorWidth + GAP;
        groupAlignmentButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;
                    hud.groupSettings.alignVertical = !hud.groupSettings.alignVertical;

                    groupAlignmentButton.setMessage(Text.translatable(
                            hud.groupSettings.alignVertical ? "starhud.screen.button.group_alignment.vertical" : "starhud.screen.button.group_alignment.horizontal"
                    ));
                }
        )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.group_alignment")))
                .dimensions(xGroupAlignmentButton, yBottomGroup, terminatorWidth, SQUARE_WIDGET_LENGTH).build();

        moreOptionButtons.add(alignmentXButton);
        moreOptionButtons.add(alignmentYButton);
        moreOptionButtons.add(directionXButton);
        moreOptionButtons.add(directionYButton);
        moreOptionButtons.add(hudDisplayButton);
        moreOptionButtons.add(drawBackgroundButton);
        moreOptionButtons.add(shouldRenderButton);
        moreOptionButtons.add(clampPosButton);

        moreOptionTexts.add(xField);
        moreOptionTexts.add(yField);
        moreOptionTexts.add(scaleField);

        for (ButtonWidget bw : moreOptionButtons) {
            addDrawableChild(bw);
        }

        for (TextFieldWidget tfw : moreOptionTexts) {
            addDrawableChild(tfw);
        }

        addDrawableChild(cancelButton);
        addDrawableChild(helpButton);
        addDrawableChild(moreOptionButton);
        addDrawableChild(saveAndQuitButton);

        addDrawableChild(configScreenButton);

        addDrawableChild(groupUngroupButton);
        addDrawableChild(gapField);
        addDrawableChild(childAlignmentButton);
        addDrawableChild(groupAlignmentButton);

        hideMoreOptionsButtons();
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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        // draw basic grid for convenience
        if (SETTINGS.drawGrid) {
            final int CENTER_X = this.width / 2;
            final int CENTER_Y = this.height / 2;
            int gridEdgePadding = Math.max(SETTINGS.gridEdgePadding, 0);
            if (gridEdgePadding > 0)
                RenderUtils.drawBorder(context, gridEdgePadding, gridEdgePadding, this.width - (gridEdgePadding * 2), this.height - (gridEdgePadding * 2), SETTINGS.gridColor);
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
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.x").getString(), xField.getX() - 5 - 2 - 3, xField.getY() + 6, 0xFFFFFFFF, true);
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.y").getString(), yField.getX() + yField.getWidth() + 3, yField.getY() + 6, 0xFFFFFFFF, true);
        }

        if (gapField.isVisible()) {
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.gap").getString(), gapField.getX() - 20 - 3, gapField.getY() + 6, 0xFFFFFFFF, true);
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
                RenderUtils.drawBorder(context, x1, y1, width, height, color | 0xFF000000);
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
            if (!hud.getSettings().shouldRender) continue;
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
            RenderUtils.drawBorder(context, x, y, width, height, color | 0xFF000000);
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
            RenderUtils.drawBorder(context, x, y, width, height, color);
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
    public boolean mouseClicked(Click click, boolean doubled) {
        if (super.mouseClicked(click, doubled))
            return true;

        if (click.button() == 0) {
            hasMovedSincePress = false;
            dragStartX = (int) click.x();
            dragStartY = (int) click.y();
            dragCurrentX = (int) click.x();
            dragCurrentY = (int) click.y();

            // find which HUD was clicked (if any)
            clickedHUD = getHUDAtPosition(click.x(), click.y());

            if (clickedHUD != null) {
                handleHUDClick(clickedHUD);
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

        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            if (hud.isHovered((int) mouseX, (int) mouseY))
                return hud;
        }

        return null;
    }

    private boolean pendingChildClick;
    private void handleHUDClick(AbstractHUD clickedHUD) {
        if (CLIENT.isShiftPressed()) {
            // shift click: Add to selection (don't remove if already selected)
            if (!selectedHUDs.contains(clickedHUD)) {
                selectedHUDs.add(clickedHUD);
            }
            // if already selected, we'll handle potential removal in mouseReleased
            pendingToggleHUD = selectedHUDs.contains(clickedHUD) ? clickedHUD : null;
        } else if (CLIENT.isCtrlPressed()) {
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
        if (!CLIENT.isShiftPressed() && !CLIENT.isCtrlPressed()) {
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
    public boolean mouseReleased(Click click) {
        if (click.button() == 0) {
            if (!hasMovedSincePress) {
                // if mouse hasn't moved since clicked to release, we handle non mouse moved operation
                handleClickRelease(click.x(), click.y());
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
        return super.mouseReleased(click);
    }

    private void handleClickRelease(double mouseX, double mouseY) {
        // Handle pending toggle operations (for ctrl click and shift click)
        if (pendingToggleHUD != null) {
            if (CLIENT.isShiftPressed()) {
                // shift click on already selected: remove from selection
                selectedHUDs.remove(pendingToggleHUD);
            } else if (CLIENT.isCtrlPressed()) {
                // ctrl click toggle: remove from selection
                selectedHUDs.remove(pendingToggleHUD);
            }
            updateFieldsFromSelectedHUD();
        }

        // Handle single-click deselection for multi-selection
        if (clickedHUD != null && !CLIENT.isShiftPressed() && !CLIENT.isCtrlPressed()) {
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
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (click.button() != 0) {
            return super.mouseDragged(click, deltaX, deltaY);
        }

        // check if we've moved enough to start drag operation
        if (!hasMovedSincePress) {
            int totalMovement = Math.abs((int)click.x() - dragStartX) + Math.abs((int)click.y() - dragStartY);
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
                updateDragBoxSelection(click.x(), click.y());
                return true;
            }
        }

        return super.mouseDragged(click, deltaX, deltaY);
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
            if (clickedHUD != null && !CLIENT.isShiftPressed() && !CLIENT.isCtrlPressed()) {
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

        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            if (hud.intersects(x1, y1, x2, y2)) {
                boxSelectedHUDs.add(hud);
            }
        }

        // Apply drag box selection based on modifier keys
        if (CLIENT.isShiftPressed()) {
            // shift drag box: Add new items to existing selection
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (!selectedHUDs.contains(hud)) { // only add if not already selected
                    selectedHUDs.add(hud);
                }
            }
        } else if (CLIENT.isCtrlPressed()) {
            // ctrl drag box: invert items in box
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (initialDragBoxSelection.contains(hud)) {
                    selectedHUDs.remove(hud); // remove if was initially selected
                } else if (!selectedHUDs.contains(hud)) {
                    selectedHUDs.add(hud); // add if not currently selected
                }
            }
        } else {
            // click: update selection, remove the ones that didn't get caught, add the one that did get caught
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
    public boolean keyPressed(KeyInput input) {
        if (isTextFieldsFocused())
            return super.keyPressed(input);

        if (!dragSelection && !dragging) {

            boolean handled = false;

            if (!selectedHUDs.isEmpty())
                for (AbstractHUD hud : selectedHUDs)
                    if (onKeyPressed(hud, input.key(), input.modifiers()))
                        handled = true;

            switch (input.key()) {
                case GLFW.GLFW_KEY_G -> {
                    if (selectedHUDs.isEmpty()) break;
                    if (selectedHUDs.size() > 1) {
                        if (canSelectedHUDsGroup) {
                            HUDComponent.getInstance().group(selectedHUDs);
                            selectedHUDs.clear();
                            handled = true;
                        }
                    } else {
                        if (canSelectedHUDUngroup) {
                            HUDComponent.getInstance().unGroup((GroupedHUD) selectedHUDs.getFirst());
                            selectedHUDs.clear();
                            handled = true;
                        }
                    }
                }
                case GLFW.GLFW_KEY_R -> {
                    if (input.hasCtrl()) {
                        revertChanges();
                        selectedHUDs.clear();
                        handled = true;
                    }
                }
                case GLFW.GLFW_KEY_C -> {
                    if (input.hasShift()) {
                        HUDComponent.getInstance().clampAll();
                    }
                }
            }

            if (handled) {
                updateFieldsFromSelectedHUD();
                return true;
            }
        }

        return super.keyPressed(input);
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
                    settings.originX = settings.originX.prev();
                    settings.growthDirectionX = settings.growthDirectionX.recommendedScreenAlignment(settings.originX);
                }
                else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.prev();
                else settings.x -= step;

                handled = true;
            }

            case GLFW.GLFW_KEY_RIGHT -> {
                if (isCtrl) {
                    settings.originX = settings.originX.next();
                    settings.growthDirectionX = settings.growthDirectionX.recommendedScreenAlignment(settings.originX);
                }
                else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.next();
                else settings.x += step;

                handled = true;
            }

            case GLFW.GLFW_KEY_UP -> {
                if (isCtrl) {
                    settings.originY = settings.originY.prev();
                    settings.growthDirectionY = settings.growthDirectionY.recommendedScreenAlignment(settings.originY);
                }
                else if (isAlt) {
                    settings.growthDirectionY = settings.growthDirectionY.prev();
                }
                else settings.y -= step;

                handled = true;
            }

            case GLFW.GLFW_KEY_DOWN -> {
                if (isCtrl) {
                    settings.originY = settings.originY.next();
                    settings.growthDirectionY = settings.growthDirectionY.recommendedScreenAlignment(settings.originY);
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
            if (this.client == null) return;
            this.client.setScreen(new ConfirmScreen(
                    result -> {
                        if (result) {
                            revertChanges();
                            onClose();
                        } else {
                            this.client.setScreen(this);
                        }
                    },
                    Text.translatable("starhud.screen.dialog.discard_title"),
                    Text.translatable("starhud.screen.dialog.discard_message")
            ));
        } else {
            onClose();
        }
    }

    public void onClose() {
        if (this.client == null) return;
        this.client.setScreen(this.parent);
    }

    private void onHelpSwitched() {
    }

    private void updateFieldsFromSelectedHUD() {
        super.setFocused(null);

        if (selectedHUDs.isEmpty()) {
            xField.setText(Text.translatable("starhud.screen.status.na").getString());
            yField.setText(Text.translatable("starhud.screen.status.na").getString());
            scaleField.setText(Text.translatable("starhud.screen.status.na").getString());

            alignmentXButton.setMessage(Text.translatable("starhud.screen.button.x_alignment_na"));
            directionXButton.setMessage(Text.translatable("starhud.screen.button.x_direction_na"));
            alignmentYButton.setMessage(Text.translatable("starhud.screen.button.y_alignment_na"));
            directionYButton.setMessage(Text.translatable("starhud.screen.button.y_direction_na"));
            hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display_na"));
            drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background_na"));
            shouldRenderButton.setMessage(Text.translatable("starhud.screen.status.na"));

            gapField.setText(Text.translatable("starhud.screen.status.na").getString());
            groupAlignmentButton.setMessage(Text.translatable("starhud.screen.status.na"));
            childAlignmentButton.setMessage(Text.translatable("starhud.screen.status.na"));

            gapField.setEditable(false);
            gapField.visible = false;
            groupAlignmentButton.visible = false;
            groupAlignmentButton.active = false;
            childAlignmentButton.visible = false;
            childAlignmentButton.active = false;

            for (ButtonWidget bw : moreOptionButtons) {
                if (bw == clampPosButton) continue;
                bw.active = false;
            }

            for (TextFieldWidget tfw : moreOptionTexts) {
                tfw.setEditable(false);
            }

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

            alignmentXButton.setMessage(Text.translatable("starhud.screen.button.x_alignment", settings.getOriginX().toString()));
            directionXButton.setMessage(Text.translatable("starhud.screen.button.x_direction", settings.getGrowthDirectionX().toString()));
            alignmentYButton.setMessage(Text.translatable("starhud.screen.button.y_alignment", settings.getOriginY().toString()));
            directionYButton.setMessage(Text.translatable("starhud.screen.button.y_direction", settings.getGrowthDirectionY().toString()));
            hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display", settings.getDisplayMode().toString()));
            drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background",
                    settings.drawBackground ?
                            Text.translatable("starhud.screen.status.on").getString() :
                            Text.translatable("starhud.screen.status.off").getString()));
            shouldRenderButton.setMessage(settings.shouldRender ?
                    Text.translatable("starhud.screen.status.on") :
                    Text.translatable("starhud.screen.status.off"));

            for (ButtonWidget bw : moreOptionButtons) {
                bw.active = true;
            }

            for (TextFieldWidget tfw : moreOptionTexts) {
                tfw.setEditable(true);
            }

            gapField.visible = false;
            groupAlignmentButton.visible = false;
            childAlignmentButton.visible = false;

            canSelectedHUDUngroup =  (selectedHUDs.size() == 1 && firstHUD instanceof GroupedHUD && !firstHUD.isInGroup());
            canSelectedHUDsGroup = (selectedHUDs.size() > 1 && selectedHUDs.stream().noneMatch(AbstractHUD::isInGroup));

            if (canSelectedHUDsGroup) {
                groupUngroupButton.setMessage(Text.translatable("starhud.screen.button.group"));
                groupUngroupButton.visible = true;
                groupUngroupButton.active = true;
            } else if (canSelectedHUDUngroup) {
                groupUngroupButton.setMessage(Text.translatable("starhud.screen.button.ungroup"));
                groupUngroupButton.visible = true;
                groupUngroupButton.active = true;
            } else {
                groupUngroupButton.visible = false;
                groupUngroupButton.active = false;
            }

            if (isMoreOptionActivated)
                showMoreOptionsButtons();
        }
    }

    private void onMoreOptionSwitched() {
        if (isMoreOptionActivated) {
            showMoreOptionsButtons();
        } else {
            hideMoreOptionsButtons();
        }
    }

    private void hideMoreOptionsButtons() {
        for (ButtonWidget bw : moreOptionButtons) {
            bw.visible = false;
        }

        for (TextFieldWidget tfw : moreOptionTexts) {
            tfw.visible = false;
        }

        gapField.visible = false;
        groupAlignmentButton.visible = false;
        childAlignmentButton.visible = false;
    }

    private void showMoreOptionsButtons() {
        for (ButtonWidget bw : moreOptionButtons) {
            bw.visible = true;
        }

        for (TextFieldWidget tfw : moreOptionTexts) {
            tfw.visible = true;
        }

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

            groupAlignmentButton.setMessage(Text.translatable(
                    hud.groupSettings.alignVertical ? "starhud.screen.button.group_alignment.vertical" : "starhud.screen.button.group_alignment.horizontal"
            ));

            childAlignmentButton.setMessage(Text.of(hud.groupSettings.getChildAlignment().toString()));
        }
    }
}
