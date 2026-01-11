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
import fin.starhud.screen.history.CompositeAction;
import fin.starhud.screen.history.HUDAction;
import fin.starhud.screen.history.HUDHistory;
import fin.starhud.screen.history.ReversibleAction;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.*;

public class EditHUDScreen extends Screen {

    private static final Logger LOGGER = Main.LOGGER;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.EditHUDScreenSettings SETTINGS = Main.settings.generalSettings.screenSettings;

    public static final int PADDING = 25;
    public static final int WIDGET_WIDTH = 100;
    public static final int WIDGET_HEIGHT = 20;
    public static final int TEXT_FIELD_WIDTH = 35;
    public static final int SQUARE_WIDGET_LENGTH = 20;
    public static final int GAP = 5;
    public static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

    public Screen parent;

    private final Map<String, GroupedHUD> groupedHUDs = HUDComponent.getInstance().getGroupedHUDs();

    private final Map<String, BaseHUDSettings> oldHUDSettings = new HashMap<>();
    private final Map<String, GroupedHUDSettings> oldGroupedHUDSettings = new HashMap<>();

    private final List<String> oldIndividualHudIds = new ArrayList<>();
    private final List<GroupedHUDSettings> oldGroupedHUDs = new ArrayList<>();

    private boolean dragging = false;
    private final List<AbstractHUD> selectedHUDs = new ArrayList<>();

    private boolean isMoreOptionActivated = false;
    private boolean canSelectedHUDsGroup = false;
    private boolean canSelectedHUDUngroup = false;
    private boolean supressFieldEvents = false;

    // widgets
    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private TextFieldWidget scaleField;
    private ButtonWidget shouldRenderButton;
    private ButtonWidget hudDisplayButton;
    private ButtonWidget drawBackgroundButton;
    private ButtonWidget drawTextShadowButton;

    private final List<ButtonWidget> moreOptionButtons = new ArrayList<>();
    private final List<TextFieldWidget> moreOptionTexts = new ArrayList<>();

    // special group buttons
    private TextFieldWidget gapField;
    private ButtonWidget groupAlignmentButton;
    private ButtonWidget childAlignmentButton;
    private ButtonWidget childOrderingButton;
    private ButtonWidget groupUngroupButton;

    private final HUDHistory history = new HUDHistory();
    private final HelpWidget helpWidget = new HelpWidget();
    private final ActionBar actionBar = new ActionBar();
    private final Box selectedHUDBox = new Box();
    private SnapResult snapResult;


    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

        saveCurrentState();
    }

    @Override
    protected void init() {

        final int CENTER_X = this.width / 2;
        final int CENTER_Y = (this.height - WIDGET_HEIGHT) / 2;

        HUDComponent.getInstance().updateAll();

        int y1 = CENTER_Y;
        int leftX = CENTER_X - (SQUARE_WIDGET_LENGTH / 2) - GAP - WIDGET_WIDTH;
        int rightX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;

        int yFieldX = CENTER_X - TEXT_FIELD_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP;
        yField = new TextFieldWidget(
                CLIENT.textRenderer,
                yFieldX, y1,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.translatable("starhud.screen.field.y")
        );

        int xFieldX = leftX + GAP + GAP + GAP;
        xField = new TextFieldWidget(
                CLIENT.textRenderer,
                xFieldX, y1,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.translatable("starhud.screen.field.x")
        );

        int hudDisplayButtonX = CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP;
        hudDisplayButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.display_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    HUDAction act = onHUDDisplayModeChanged(selectedHUD, selectedHUD.getSettings().getDisplayMode().next());
                    history.execute(act);
                    hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display", selectedHUD.getSettings().getDisplayMode().toString()));
                }
        ).dimensions(hudDisplayButtonX, y1, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int y2 = y1 - PADDING;

        int shouldRenderButtonX = CENTER_X - (SQUARE_WIDGET_LENGTH / 2);
        shouldRenderButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    HUDAction act = onShouldRenderChanged(selectedHUD, !selectedHUD.getSettings().shouldRender());
                    history.execute(act);
                    button.setMessage(selectedHUD.getSettings().shouldRender ?
                            Text.translatable("starhud.screen.status.on") :
                            Text.translatable("starhud.screen.status.off"));
                }
        ).dimensions(shouldRenderButtonX, y2, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH).build();

        drawTextShadowButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.shadow_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD hud = selectedHUDs.getFirst();
                    HUDAction act = onDrawTextShadowChanged(hud, !hud.getSettings().drawTextShadow);
                    history.execute(act);
                    drawTextShadowButton.setMessage(Text.translatable("starhud.screen.button.shadow",
                            hud.getSettings().drawTextShadow ?
                                    Text.translatable("starhud.screen.status.on").getString() :
                                    Text.translatable("starhud.screen.status.off").getString()
                    ));

                }
        ).dimensions(leftX, y2, WIDGET_WIDTH, WIDGET_HEIGHT).build();


        drawBackgroundButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.background_na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    AbstractHUD selectedHUD = selectedHUDs.getFirst();
                    HUDAction act = onDrawBackgroundChanged(selectedHUD, !selectedHUD.getSettings().drawBackground);
                    history.execute(act);
                    drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background",
                            selectedHUD.getSettings().drawBackground ?
                                    Text.translatable("starhud.screen.status.on").getString() :
                                    Text.translatable("starhud.screen.status.off").getString()));
                }
        ).dimensions(rightX, y2, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        int y3 = y2 - PADDING;

        int scaleFieldWidth = SQUARE_WIDGET_LENGTH + 6;
        int scaleFieldX = CENTER_X - (scaleFieldWidth / 2);
        scaleField = new TextFieldWidget(
                CLIENT.textRenderer,
                scaleFieldX, y3,
                scaleFieldWidth,
                SQUARE_WIDGET_LENGTH,
                Text.translatable("starhud.screen.field.scale")
        );

        int yBottom = this.height - SQUARE_WIDGET_LENGTH - GAP;

        int wConfigScreen = SQUARE_WIDGET_LENGTH;
        int hConfigScreen = wConfigScreen;
        int xConfigScreenButton = CENTER_X - (wConfigScreen / 2);
        ButtonWidget configScreenButton = ButtonWidget.builder(
                        Text.translatable("starhud.screen.button.config"),
                        button -> {
                            helpWidget.setActive(false);
                            isMoreOptionActivated = false;
                            selectedHUDs.clear();

                            this.client.setScreen(AutoConfig.getConfigScreen(Settings.class, this).get());
                        }
                )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.config")))
                .dimensions(xConfigScreenButton, CENTER_Y, wConfigScreen, hConfigScreen)
                .build();

        int xHelpButton = CENTER_X - (GAP/2) - SQUARE_WIDGET_LENGTH;
        ButtonWidget helpButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.button.help"),
                button -> {
                    helpWidget.setActive(!helpWidget.isActive());
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
        int xGroupUngroupButton = CENTER_X - terminatorWidth / 2;

        groupUngroupButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (canSelectedHUDsGroup) {
                        HUDAction act = onGroupChanged(selectedHUDs);
                        history.commit(act);
                    } else if (canSelectedHUDUngroup) {
                        HUDAction act = onUngroupChanged((GroupedHUD) selectedHUDs.getFirst());
                        history.commit(act);
                    }

                    selectedHUDs.clear();
                    updateFieldsFromSelectedHUD();
                    updateGroupFieldFromSelectedHUD();
                }
        ).dimensions(xGroupUngroupButton, yBottomGroup, terminatorWidth, SQUARE_WIDGET_LENGTH).build();

        int yBottomGroup2 = yBottomGroup + PADDING;
        int yBottomGroup3 = yBottomGroup2 + PADDING;

        int gapFieldWidth = terminatorWidth / 2;
        int xGapField = CENTER_X - (gapFieldWidth / 2);
        int yGapField = yBottomGroup3;
        gapField = new TextFieldWidget(
                CLIENT.textRenderer,
                xGapField, yGapField,
                gapFieldWidth, SQUARE_WIDGET_LENGTH,
                Text.translatable("starhud.screen.field.gap")
        );

        gapField.setChangedListener(text -> {
            if (supressFieldEvents) return;
            if (selectedHUDs.isEmpty()) return;
            if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;

            try {
                int newGap = Integer.parseInt(text);
                HUDAction act = onGapFieldChanged(hud, hud.groupSettings.gap, newGap);
                history.execute(act);
            } catch (NumberFormatException ignored) {}
        });

        xField.setChangedListener(text -> {
            if (supressFieldEvents) return;
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD hud = selectedHUDs.getFirst();
            try {
                int newX = Integer.parseInt(text);
                HUDAction act = onXFieldChanged(hud, hud.getSettings().getX(), newX);
                history.execute(act);
            } catch (NumberFormatException ignored) {}
        });

        yField.setChangedListener(text -> {
            if (supressFieldEvents) return;
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD hud = selectedHUDs.getFirst();
            try {
                int newY = Integer.parseInt(text);
                HUDAction act = onYFieldChanged(hud, hud.getSettings().getY(), newY);
                history.execute(act);
            } catch (NumberFormatException ignored) {}
        });

        scaleField.setTooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.scale")));
        scaleField.setChangedListener(text -> {
            if (supressFieldEvents) return;
            if (selectedHUDs.isEmpty()) return;
            AbstractHUD hud = selectedHUDs.getFirst();
            try {
                float newScale = Float.parseFloat(text);
                HUDAction act = onScaleFieldChanged(hud, hud.getSettings().getScale(), newScale);
                history.execute(act);
            } catch (NumberFormatException ignored) {}
        });

        int xChildAlignmentButton = CENTER_X - (terminatorWidth / 2);
        childAlignmentButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;

                    HUDAction act = onChildAlignmentChanged(hud, hud.groupSettings.getChildAlignment().next());
                    history.execute(act);

                    button.setMessage(Text.of(hud.groupSettings.getChildAlignment().toString()));
                }
        )
                .dimensions(xChildAlignmentButton, yBottomGroup2, terminatorWidth, WIDGET_HEIGHT)
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.child_alignment")))
                .build();
        
        int xChildOrderingButton = xChildAlignmentButton - GAP  - terminatorWidth;
        childOrderingButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;
                    
                    HUDAction act = onChildOrderingChanged(hud, hud.groupSettings.getChildOrdering().next());
                    history.execute(act);

                    button.setMessage(Text.of(hud.groupSettings.getChildOrdering().toString()));
                }
        )
                .dimensions(xChildOrderingButton, yBottomGroup2, terminatorWidth, WIDGET_HEIGHT)
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.child_ordering")))
                .build();

        int xGroupAlignmentButton = xChildAlignmentButton + terminatorWidth + GAP;
        groupAlignmentButton = ButtonWidget.builder(
                Text.translatable("starhud.screen.status.na"),
                button -> {
                    if (selectedHUDs.isEmpty()) return;
                    if (!(selectedHUDs.getFirst() instanceof GroupedHUD hud)) return;

                    HUDAction act = onGroupAlignmentChanged(hud, !hud.groupSettings.alignVertical);
                    history.execute(act);

                    groupAlignmentButton.setMessage(Text.translatable(
                            hud.groupSettings.alignVertical ? "starhud.screen.button.group_alignment.vertical" : "starhud.screen.button.group_alignment.horizontal"
                    ));
                }
        )
                .tooltip(Tooltip.of(Text.translatable("starhud.screen.tooltip.group_alignment")))
                .dimensions(xGroupAlignmentButton, yBottomGroup2, terminatorWidth, SQUARE_WIDGET_LENGTH).build();

        moreOptionButtons.clear();
        moreOptionButtons.add(hudDisplayButton);
        moreOptionButtons.add(drawBackgroundButton);
        moreOptionButtons.add(drawTextShadowButton);
        moreOptionButtons.add(shouldRenderButton);

        moreOptionTexts.clear();
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
        addDrawableChild(childOrderingButton);
        addDrawableChild(groupAlignmentButton);

        hideMoreOptionsButtons();
        updateFieldsFromSelectedHUD();
        updateGroupFieldFromSelectedHUD();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        if (SETTINGS.drawDarkBackground) {
            final float alpha = (float) SETTINGS.getDarkOpacity() / 100;
            final int color = ColorHelper.channelFromFloat(alpha) << 24;
            context.fill(0, 0, this.width, this.height, color);
        }

        // draw basic grid for convenience
        if (SETTINGS.drawGrid) {
            renderGrid(context);
        }

        // draw Snapping Line
        if (snapResult != null && (snapResult.snappedX || snapResult.snappedY)) {
            snapResult.render(context);
        }

        super.render(context, mouseX, mouseY, delta);

        // draw help
        if (helpWidget.isActive()) {
            final int CENTER_X = this.width / 2;
            final int CENTER_Y = this.height / 2 + (PADDING / 2);
            AbstractHUD hud = selectedHUDs.isEmpty() ? null : selectedHUDs.getFirst();
            helpWidget.render(context, hud, CENTER_X, CENTER_Y + GAP);
        }

        if (!selectedHUDBox.isEmpty() && selectedHUDs.size() > 1) {
            renderSelectedHUDBox(context);
        }

        // draw X and Y next to their textField.
        if (xField.isVisible() && yField.isVisible()) {
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.x"), xField.getX() - 5 - 2 - 3, xField.getY() + 6, 0xFFFFFFFF, true);
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.y"), yField.getX() - 5 - 2 - 3, yField.getY() + 6, 0xFFFFFFFF, true);
        }

        if (gapField.isVisible()) {
            context.drawText(CLIENT.textRenderer, Text.translatable("starhud.screen.label.gap"), gapField.getX() - 20 - 3, gapField.getY() + 6, 0xFFFFFFFF, true);
        }

        if (dragSelection && hasMovedSincePress) {
            renderDragBox(context);
        }

        // draw all visible hud with bounding boxes.
        HUDComponent.getInstance().collectAll();
        HUDComponent.getInstance().renderAll(context);

        renderBoundingBoxes(context, mouseX, mouseY);

        if (actionBar.isActive()) {
            final int CENTER_X = this.width / 2;
            final int Y = this.height - 50;
            actionBar.render(context, CENTER_X, Y);
        }
    }

    public void renderGrid(DrawContext context) {

        final Window WINDOW = this.client.getWindow();
        final int screenWidth = WINDOW.getWidth();
        final int screenHeight = WINDOW.getHeight();
        final int snapPadding = SETTINGS.getSnapPadding();
        final int color = SETTINGS.gridColor;

        final int CENTER_X = screenWidth / 2;
        final int CENTER_Y = screenHeight / 2;

        PixelPlacement.start(context);

        if (snapPadding > 0)
            RenderUtils.drawBorder(context, snapPadding, snapPadding, screenWidth - (snapPadding * 2), screenHeight - (snapPadding * 2), color);
        context.drawHorizontalLine((snapPadding + 1), screenWidth - (snapPadding + 2), CENTER_Y, color);
        context.drawVerticalLine(CENTER_X, (snapPadding), screenHeight - (snapPadding + 1), color);

        PixelPlacement.end(context);
    }

    private void renderSelectedHUDBox(DrawContext context) {
        int x = selectedHUDBox.getX();
        int y = selectedHUDBox.getY();
        int w = selectedHUDBox.getWidth();
        int h = selectedHUDBox.getHeight();

        if (w < 0 || h < 0) return;

        PixelPlacement.start(context);

        RenderUtils.drawBorder(context, x, y, w, h, -1);

        PixelPlacement.end(context);
    }

    private void renderDragBox(DrawContext context) {

        float guiScale = this.client.getWindow().getScaleFactor();

        int x1 = (int) (Math.min(dragStartX, dragCurrentX) * guiScale);
        int y1 = (int) (Math.min(dragStartY, dragCurrentY) * guiScale);
        int x2 = (int) (Math.max(dragStartX, dragCurrentX) * guiScale);
        int y2 = (int) (Math.max(dragStartY, dragCurrentY) * guiScale);

        int width = x2 - x1;
        int height = y2 - y1;
        int color = SETTINGS.dragBoxColor;

        if (width > 0 && height > 0) {
            PixelPlacement.start(context);

            context.fill(x1, y1, x1 + width, y1 + height, color | 0x40000000);

            if (SETTINGS.drawBorder)
                RenderUtils.drawBorder(context, x1, y1, width, height, color | 0xFF000000);

            PixelPlacement.end(context);
        }
    }

    private void renderBoundingBoxes(DrawContext context, int mouseX, int mouseY) {

        PixelPlacement.start(context);
        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            renderBoundingBox(context, hud, mouseX, mouseY);
        }

        for (AbstractHUD hud : selectedHUDs) {
            if (!hud.getSettings().shouldRender) continue;
            renderSelectedBox(context, hud);
        }
        PixelPlacement.end(context);
    }

    private void renderSelectedBox(DrawContext context, AbstractHUD hud) {
        int x = hud.getX();
        int y = hud.getY();
        int width = hud.getTrueWidth();
        int height = hud.getTrueHeight();
        int color = (hud instanceof GroupedHUD ? SETTINGS.selectedGroupBoxColor : SETTINGS.selectedBoxColor);

        if (hud.isInGroup()) {
            RenderUtils.drawBorder(context, x, y, width, height, color | 0xFF000000);
        } else {
            context.fill(x, y, x + width, y + height, color);
        }
    }

    private void renderBoundingBox(DrawContext context, AbstractHUD hud, int mouseX, int mouseY) {
        int x = hud.getX();
        int y = hud.getY();
        int width = hud.getTrueWidth();
        int height = hud.getTrueHeight();
        int color = hud.getColor();

        if (SETTINGS.drawBorder)
            RenderUtils.drawBorder(context, x, y, width, height, color);
        if (hud.isHovered(mouseX, mouseY)) {
            context.fill(x, y, x + width, y + height, (color & 0x00FFFFFF) | 0x80000000);
        }
    }

    boolean dragSelection = false;
    double dragStartX, dragStartY;
    double dragCurrentX, dragCurrentY;

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
            dragStartX = click.x();
            dragStartY = click.y();
            dragCurrentX = click.x();
            dragCurrentY = click.y();

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

    private boolean sameHUDClicked = false;

    private AbstractHUD getHUDAtPosition(double mouseX, double mouseY) {

        if (!selectedHUDs.isEmpty()) {
            AbstractHUD hud = selectedHUDs.getFirst();
            if (hud.isHovered(mouseX, mouseY)) {
                sameHUDClicked = true;
                return hud;
            }
        }
        sameHUDClicked = false;

        for (AbstractHUD hud : selectedHUDs) {
            if (hud.isHovered(mouseX, mouseY)) {
                return hud;
            }
        }

        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            if (hud.isHovered(mouseX, mouseY)) {
                return hud;
            }
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

        if (!pendingChildClick && !sameHUDClicked) {
            
            updateFieldsFromSelectedHUD();
            updateGroupFieldFromSelectedHUD();
        }
    }

    private void handleEmptySpaceClick() {
        if (!CLIENT.isShiftPressed() && !CLIENT.isCtrlPressed()) {
            // click on empty space - clear selection
            selectedHUDs.clear();
            
            updateFieldsFromSelectedHUD();
            updateGroupFieldFromSelectedHUD();
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
                dragging = false;
                handleClickRelease(click.x(), click.y());
            }

            // Finalize any drag operations
            if (hasMovedSincePress && dragging) {
                finalizeDragOperation();
            }

            dragging = false;
            dragSelection = false;

            if (!selectedHUDs.isEmpty()) {
                updateSelectedHUDBox();
            } else {
                selectedHUDBox.setEmpty(true);
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
            updateGroupFieldFromSelectedHUD();
        }

        // Handle single-click deselection for multi-selection
        if (clickedHUD != null && !CLIENT.isShiftPressed() && !CLIENT.isCtrlPressed()) {
            if (pendingChildClick && clickedHUD instanceof GroupedHUD group) {
                AbstractHUD hoveredChild = null;

                for (AbstractHUD hud : group.huds) {
                    if (hud.isHovered(mouseX, mouseY)) {
                        hoveredChild = hud;
                        break;
                    }
                }

                if (hoveredChild != null) {
                    selectedHUDs.clear();
                    selectedHUDs.add(hoveredChild);
                    
                    updateFieldsFromSelectedHUD();
                    updateGroupFieldFromSelectedHUD();
                }

            } else if (selectedHUDs.contains(clickedHUD) && selectedHUDs.size() > 1) {
                // Single click on item in multi-selection should select only that item
                selectedHUDs.clear();
                selectedHUDs.add(clickedHUD);
                
                updateFieldsFromSelectedHUD();
                updateGroupFieldFromSelectedHUD();
            }
        }
    }

    private void resetMouseState() {
        hasMovedSincePress = false;
        clickedHUD = null;
        pendingToggleHUD = null;
        initialDragBoxSelection.clear();

        pendingChildClick = false;
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (click.button() != 0) {
            return super.mouseDragged(click, deltaX, deltaY);
        }

        // check if we've moved enough to start drag operation
        if (!hasMovedSincePress) {
            int totalMovement = (int) (Math.abs(click.x() - dragStartX) + Math.abs(click.y() - dragStartY));
            if (totalMovement >= DRAG_THRESHOLD) {
                hasMovedSincePress = true;
                startDragOperation(click.x(), click.y());
            }
        }

        if (hasMovedSincePress) {
            dragCurrentX = click.x();
            dragCurrentY = click.y();

            if (dragging && !selectedHUDs.isEmpty() && !selectedHUDs.getFirst().isInGroup()) { // if we've moved and there are selected huds, we drag them, obviously
                dragSelectedHUDs(click.x(), click.y(), deltaX, deltaY);
                return true;
            } else if (dragSelection) { // otherwise it's just drag box
                updateDragBoxSelection(click.x(), click.y());
                return true;
            }
        }

        return super.mouseDragged(click, deltaX, deltaY);
    }

    private void startDragOperation(double mouseX, double mouseY) {
        // clear any pending toggle since we're now dragging
        pendingToggleHUD = null;

        // if moved + has hud selected -> potential hud(s) dragging.
        if (clickedHUD != null && selectedHUDs.contains(clickedHUD)) {
            for (AbstractHUD hud : selectedHUDs) {
                hud.setupStartDrag();
            }

            dragSelection = false;
            snapResult = null;

            updateSelectedHUDBox();
            startBoxX = selectedHUDBox.getX();
            startBoxY = selectedHUDBox.getY();
            beforeBoxX = startBoxX;
            beforeBoxY = startBoxY;

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

    private void finalizeDragOperation() {
        dragging = false;

        snapResult = null;

        // Update final positions in text fields
        if (!selectedHUDs.isEmpty()) {
            AbstractHUD selectedHUD = selectedHUDs.getFirst();
            supressFieldEvents = true;
            xField.setText(String.valueOf(selectedHUD.getSettings().x));
            yField.setText(String.valueOf(selectedHUD.getSettings().y));
            supressFieldEvents = false;

            List<HUDAction> acts = new ArrayList<>();
            for (AbstractHUD hud : selectedHUDs) {
                HUDAction actX = onXFieldChanged(hud, hud.getStartDragX(), hud.getSettings().x);
                HUDAction actY = onYFieldChanged(hud, hud.getStartDragY(), hud.getSettings().y);

                HUDAction actAlignmentX = onAlignmentXChanged(hud, hud.getStartDragAlignmentX(), hud.getSettings().getOriginX());
                HUDAction actAlignmentY = onAlignmentYChanged(hud, hud.getStartDragAlignmentY(), hud.getSettings().getOriginY());

                HUDAction actGrowthX = onDirectionXChanged(hud, hud.getStartDragGrowthX(), hud.getSettings().getGrowthDirectionX());
                HUDAction actGrowthY = onDirectionYChanged(hud, hud.getStartDragGrowthY(), hud.getSettings().getGrowthDirectionY());

                if (actX != null)
                    acts.add(actX);
                if (actY != null)
                    acts.add(actY);

                if (actAlignmentX != null)
                    acts.add(actAlignmentX);
                if (actAlignmentY != null)
                    acts.add(actAlignmentY);

                if (actGrowthX != null)
                    acts.add(actGrowthX);
                if (actGrowthY != null)
                    acts.add(actGrowthY);
            }
            if (!acts.isEmpty())
                history.commit(new CompositeAction(acts));
        }
    }

    private int startBoxX = -1;
    private int startBoxY = -1;
    private int beforeBoxX = -1;
    private int beforeBoxY = -1;

    private void dragSelectedHUDs(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (selectedHUDs.isEmpty()) return;

        final float guiScale = this.client.getWindow().getScaleFactor();

        final int totalDeltaX = (int) ((dragCurrentX - dragStartX) * guiScale);
        final int totalDeltaY = (int) ((dragCurrentY - dragStartY) * guiScale);

        selectedHUDBox.setX(startBoxX + totalDeltaX);
        selectedHUDBox.setY(startBoxY + totalDeltaY);

        snapResult = SnapResult.getSnap(selectedHUDBox, selectedHUDs);

        if (snapResult.snappedX) {
            selectedHUDBox.setX(selectedHUDBox.getX() + snapResult.snapDeltaX);
        }

        if (snapResult.snappedY) {
            selectedHUDBox.setY(selectedHUDBox.getY() + snapResult.snapDeltaY);
        }

        int dx = selectedHUDBox.getX() - beforeBoxX;
        int dy = selectedHUDBox.getY() - beforeBoxY;

        beforeBoxX = selectedHUDBox.getX();
        beforeBoxY = selectedHUDBox.getY();

        if (dx != 0 || dy != 0) {
            for (AbstractHUD hud : selectedHUDs) {
                if (hud.isInGroup()) continue;

                hud.getSettings().x += dx;
                hud.getSettings().y += dy;
                hud.update();

                updateHUDAlignment(hud);
            }

            updateFieldsFromSelectedHUD();
        }
    }


    private boolean updateHUDAlignment(AbstractHUD hud) {
        final int screenWidth = this.client.getWindow().getWidth();
        final int screenHeight = this.client.getWindow().getHeight();

        BaseHUDSettings settings = hud.getSettings();

        // Store old alignment values
        ScreenAlignmentX oldOriginX = settings.getOriginX();
        ScreenAlignmentY oldOriginY = settings.getOriginY();

        GrowthDirectionX oldGrowthX = settings.getGrowthDirectionX();
        GrowthDirectionY oldGrowthY = settings.getGrowthDirectionY();

        final boolean oddWidth = hud.getTrueWidth() % 2 == 1;
        final int halfWidth = hud.getTrueWidth() / 2;

        final boolean oddHeight = hud.getTrueHeight() % 2 == 1;
        final int halfHeight = hud.getTrueHeight() / 2;

        final int hudX = hud.getX() + halfWidth;
        final int hudY = hud.getY() + halfHeight;

        int additionalX = -1;
        switch (oldGrowthX) {
            case RIGHT -> additionalX = -halfWidth;
            case CENTER -> additionalX = 0;
            case LEFT -> additionalX = halfWidth + (oddWidth ? 1 : 0);
        }

        int additionalY = -1;
        switch (oldGrowthY) {
            case DOWN -> additionalY = -halfHeight;
            case MIDDLE -> additionalY = 0;
            case UP -> additionalY = halfHeight + (oddHeight ? 1 : 0);
        }

        // Determine new horizontal alignment based on which third of screen HUD is in
        ScreenAlignmentX newOriginX;
        GrowthDirectionX newGrowthX;

        if (hudX < screenWidth / 3) {
            // Left third
            newOriginX = ScreenAlignmentX.LEFT;
            newGrowthX = GrowthDirectionX.RIGHT;
        } else if (hudX > screenWidth * 2 / 3) {
            // Right third
            newOriginX = ScreenAlignmentX.RIGHT;
            newGrowthX = GrowthDirectionX.LEFT;
        } else {
            // Center third
            newOriginX = ScreenAlignmentX.CENTER;
            newGrowthX = GrowthDirectionX.CENTER;
        }

        // Determine new vertical alignment
        ScreenAlignmentY newOriginY;
        GrowthDirectionY newGrowthY;

        if (hudY < screenHeight / 3) {
            // Top third
            newOriginY = ScreenAlignmentY.TOP;
            newGrowthY = GrowthDirectionY.DOWN;
        } else if (hudY > screenHeight * 2 / 3) {
            // Bottom third
            newOriginY = ScreenAlignmentY.BOTTOM;
            newGrowthY = GrowthDirectionY.UP;
        } else {
            // Middle third
            newOriginY = ScreenAlignmentY.MIDDLE;
            newGrowthY = GrowthDirectionY.MIDDLE;
        }

        // Only update if alignment actually changed
        if (oldOriginX != newOriginX || oldOriginY != newOriginY) {
            // Calculate what the config x/y should be to maintain same screen position

            // Update alignment settings
            settings.originX = newOriginX;
            settings.originY = newOriginY;

            settings.growthDirectionX = newGrowthX;
            settings.growthDirectionY = newGrowthY;

            int growthDiffX = newGrowthX.getGrowthDirection(hud.getTrueWidth()) - oldGrowthX.getGrowthDirection(hud.getTrueWidth());
            int growthDiffY = newGrowthY.getGrowthDirection(hud.getTrueHeight()) - oldGrowthY.getGrowthDirection(hud.getTrueHeight());

            // Calculate new config values that maintain the same screen position
            settings.x = hudX - newOriginX.getAlignmentPos(screenWidth) + additionalX + growthDiffX;
            settings.y = hudY - newOriginY.getAlignmentPos(screenHeight) + additionalY + growthDiffY;

            hud.update();

            return true;
        }

        return false;
    }

    private void updateDragBoxSelection(double mouseX, double mouseY) {
        int x1 = (int) Math.min(dragStartX, dragCurrentX);
        int y1 = (int) Math.min(dragStartY, dragCurrentY);
        int x2 = (int) Math.max(dragStartX, dragCurrentX);
        int y2 = (int) Math.max(dragStartY, dragCurrentY);

        Set<AbstractHUD> boxSelectedHUDs = new HashSet<>();

        for (AbstractHUD hud : HUDComponent.getInstance().getRenderedHUDs()) {
            if (hud.intersects(x1, y1, x2, y2)) {
                boxSelectedHUDs.add(hud);
            }
        }

        boolean changed = false;
        AbstractHUD oldFirst = null;
        int oldSize = 0;
        if (!selectedHUDs.isEmpty()) {
            oldFirst = selectedHUDs.getFirst();
            oldSize = selectedHUDs.size();
        }

        // Apply drag box selection based on modifier keys
        if (CLIENT.isShiftPressed()) {
            // shift drag box: Add new items to existing selection
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (!selectedHUDs.contains(hud)) { // only add if not already selected
                    selectedHUDs.add(hud);
                    changed = true;
                }
            }
        } else if (CLIENT.isCtrlPressed()) {

            // ctrl drag box: invert items in box
            for (AbstractHUD hud : boxSelectedHUDs) {
                if (initialDragBoxSelection.contains(hud)) {
                    selectedHUDs.remove(hud); // remove if was initially selected
                    changed = true;
                } else if (!selectedHUDs.contains(hud)) {
                    selectedHUDs.add(hud); // add if not currently selected
                    changed = true;
                }
            }
        } else {

            // click: update selection, remove the ones that didn't get caught, add the one that did get caught
            changed = selectedHUDs.removeIf(hud -> !boxSelectedHUDs.contains(hud));

            for (AbstractHUD hud : boxSelectedHUDs) {
                 if (!selectedHUDs.contains(hud)) {
                     selectedHUDs.add(hud); // add if not currently selected
                     changed = true;
                }
            }
        }

        if (changed) {
            int newSize = selectedHUDs.size();
            if (oldSize != newSize)
                updateGroupFieldFromSelectedHUD();

            if (selectedHUDs.isEmpty() || oldFirst != selectedHUDs.getFirst()) {
                updateFieldsFromSelectedHUD();
            }
        }
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

            List<HUDAction> acts = new ArrayList<>();

            if (!selectedHUDs.isEmpty()) {
                for (AbstractHUD hud : selectedHUDs) {
                    HUDAction act = onKeyPressed(hud, input.key(), input.modifiers());
                    if (act == null) break;
                    acts.add(act);
                }
            }

            if (!acts.isEmpty()) {
                history.execute(acts.size() == 1 ? acts.getFirst() : new CompositeAction(acts));
                updateFieldsFromSelectedHUD();
                updateSelectedHUDBox();
                return true;
            }

            switch (input.key()) {
                case GLFW.GLFW_KEY_G -> {
                    if (selectedHUDs.isEmpty()) break;
                    if (selectedHUDs.size() > 1) {
                        if (canSelectedHUDsGroup) {
                            HUDAction act = onGroupChanged(selectedHUDs);
                            history.commit(act);
                            selectedHUDs.clear();
                            handled = true;
                        }
                    } else {
                        if (canSelectedHUDUngroup) {
                            HUDAction act = onUngroupChanged((GroupedHUD) selectedHUDs.getFirst());
                            history.commit(act);
                            selectedHUDs.clear();
                            handled = true;
                        }
                    }
                }

                case GLFW.GLFW_KEY_C -> {
                    if (input.hasShift()) {
                        int clampCount = HUDComponent.getInstance().clampAll();
                        if (clampCount > 0)
                            actionBar.setText(Text.translatable("starhud.screen.action.clamp_all_found", clampCount));
                        else
                            actionBar.setText(Text.translatable("starhud.screen.action.clamp_all_not_found"));
                        handled = true;
                    }
                }

                case GLFW.GLFW_KEY_Z -> {
                    if (input.hasCtrl() && history.canUndo()) {
                        history.undo();
                        selectedHUDs.clear();
                        handled = true;
                    }
                }

                case GLFW.GLFW_KEY_Y -> {
                    if (input.hasCtrl() && history.canRedo()) {
                        history.redo();
                        selectedHUDs.clear();
                        handled = true;
                    }
                }

                case GLFW.GLFW_KEY_S -> {
                    if (input.hasCtrl()) {
                        saveCurrentState();
                        actionBar.setText(Text.translatable("starhud.screen.action.save"));
                    }
                }

                case GLFW.GLFW_KEY_R -> {
                    if (input.hasCtrl() && input.hasShift()) {
                        this.client.setScreen(new ConfirmScreen(
                                result -> {
                                    if (result) {
                                        AutoConfig.getConfigHolder(Settings.class).resetToDefault();
                                        actionBar.setText(Text.translatable("starhud.screen.action.reset"));
                                    }
                                    this.client.setScreen(this);
                                },
                                Text.translatable("starhud.screen.dialog.reset_title"),
                                Text.translatable("starhud.screen.dialog.reset_message")
                        ));
                    }
                }
            }

            if (handled) {
                updateFieldsFromSelectedHUD();
                updateGroupFieldFromSelectedHUD();
                updateSelectedHUDBox();
                return true;
            }
        }

        return super.keyPressed(input);
    }

    public HUDAction onKeyPressed(AbstractHUD hud, int keyCode, int modifiers) {

        BaseHUDSettings settings = hud.getSettings();
        HUDAction act = null;

        boolean isCtrl = isMac
                ? (modifiers & GLFW.GLFW_MOD_SUPER) != 0
                : (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
        boolean isShift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
        boolean isAlt = (modifiers & GLFW.GLFW_MOD_ALT) != 0;

        int step = isShift ? 5 : 1;

        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT -> {
                if (isCtrl) act = onAlignmentXChangedWithRecommendation(hud, settings.getOriginX().prev());
                else if (isAlt) act = onDirectionXChanged(hud, settings.getGrowthDirectionX(), settings.getGrowthDirectionX().prev());
                else act = onXFieldChanged(hud, settings.x, settings.x - step);
            }

            case GLFW.GLFW_KEY_RIGHT -> {
                if (isCtrl) act = onAlignmentXChangedWithRecommendation(hud, settings.getOriginX().next());
                else if (isAlt) act = onDirectionXChanged(hud, settings.getGrowthDirectionX(), settings.getGrowthDirectionX().next());
                else act = onXFieldChanged(hud, settings.x, settings.x + step);
            }

            case GLFW.GLFW_KEY_UP -> {
                if (isCtrl) act = onAlignmentYChangedWithRecommendation(hud, settings.getOriginY().prev());
                else if (isAlt) act = onDirectionYChanged(hud, settings.getGrowthDirectionY(), settings.getGrowthDirectionY().prev());
                else act = onYFieldChanged(hud, settings.y, settings.y - step);
            }

            case GLFW.GLFW_KEY_DOWN -> {
                if (isCtrl) act = onAlignmentYChangedWithRecommendation(hud, settings.getOriginY().next());
                else if (isAlt) act = onDirectionYChanged(hud, settings.getGrowthDirectionY(), settings.getGrowthDirectionY().next());
                else act = onYFieldChanged(hud, settings.y, settings.y + step);
            }

            case GLFW.GLFW_KEY_MINUS ->  {
                if (!isShift) {
                    if (settings.scale <= 0) break;

                    act = onScaleFieldChanged(hud, settings.scale, settings.scale - 1);
                }
            }

            case GLFW.GLFW_KEY_EQUAL ->  {
                if (isShift) {
                    act = onScaleFieldChanged(hud, settings.scale, settings.scale + 1);
                }
            }
        }

        return act;
    }

    private void updateSelectedHUDBox() {
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;

        boolean updated = false;
        for (AbstractHUD hud : selectedHUDs) {
            if (hud.isInGroup()) continue;

            int hx1 = hud.getX();
            int hy1 = hud.getY();
            int hx2 = hx1 + hud.getTrueWidth();
            int hy2 = hy1 + hud.getTrueHeight();

            x1 = Math.min(x1, hx1);
            y1 = Math.min(y1, hy1);
            x2 = Math.max(x2, hx2);
            y2 = Math.max(y2, hy2);

            updated = true;
        }

        int x = x1;
        int y = y1;
        int w = x2 - x1;
        int h = y2 - y1;

        if (updated)
            selectedHUDBox.setBoundingBox(x, y, w, h);
        else
            selectedHUDBox.setEmpty(true);
    }

    private void saveCurrentState() {
        Map<String, AbstractHUD> HUDMap = HUDComponent.getInstance().getHudMap();

        oldHUDSettings.clear();
        oldGroupedHUDSettings.clear();
        oldIndividualHudIds.clear();
        oldGroupedHUDs.clear();

        for (AbstractHUD p : HUDMap.values()) {
            oldHUDSettings.put(p.getId(), p.getSettings().copy());
        }

        for (GroupedHUD p : groupedHUDs.values()) {
            oldGroupedHUDSettings.put(p.getId(), p.groupSettings.copy());
        }

        oldIndividualHudIds.addAll(Main.settings.hudList.individualHudIds);
        oldGroupedHUDs.addAll(Main.settings.hudList.groupedHuds);
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
                hud.getSettings().copyFrom(original);
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

    @Override
    public void tick() {
        super.tick();

        actionBar.tick();
    }

    public void onClose() {
        if (this.client == null) return;
        this.client.setScreen(this.parent);
    }

    private void updateGroupFieldFromSelectedHUD() {
        super.setFocused(null);

        if (selectedHUDs.isEmpty()) {
            canSelectedHUDUngroup = false;
            canSelectedHUDsGroup = false;
            groupUngroupButton.active = false;
            groupUngroupButton.visible = false;
        } else {
            AbstractHUD firstHUD = selectedHUDs.getFirst();

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
                hudDisplayButton.active = false;
                hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display_na"));
            } else {
                groupUngroupButton.visible = false;
                groupUngroupButton.active = false;
            }
        }
    }

    private void updateFieldsFromSelectedHUD() {
        super.setFocused(null);

        supressFieldEvents = true;
        if (selectedHUDs.isEmpty()) {
            xField.setText(Text.translatable("starhud.screen.status.na").getString());
            yField.setText(Text.translatable("starhud.screen.status.na").getString());
            scaleField.setText(Text.translatable("starhud.screen.status.na").getString());

            hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display_na"));
            drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background_na"));
            drawTextShadowButton.setMessage(Text.translatable("starhud.screen.button.shadow_na"));
            shouldRenderButton.setMessage(Text.translatable("starhud.screen.status.na"));

            gapField.setText(Text.translatable("starhud.screen.status.na").getString());
            groupAlignmentButton.setMessage(Text.translatable("starhud.screen.status.na"));
            childAlignmentButton.setMessage(Text.translatable("starhud.screen.status.na"));
            childOrderingButton.setMessage(Text.translatable("starhud.screen.status.na"));

            gapField.setEditable(false);
            gapField.visible = false;
            groupAlignmentButton.visible = false;
            groupAlignmentButton.active = false;
            childAlignmentButton.visible = false;
            childAlignmentButton.active = false;
            childOrderingButton.visible = false;
            childOrderingButton.active = false;

            for (ButtonWidget bw : moreOptionButtons) {
                bw.active = false;
            }

            for (TextFieldWidget tfw : moreOptionTexts) {
                tfw.setEditable(false);
            }
        } else {
            AbstractHUD firstHUD = selectedHUDs.getFirst();
            BaseHUDSettings settings = firstHUD.getSettings();
            xField.setText(String.valueOf(settings.x));
            yField.setText(String.valueOf(settings.y));
            scaleField.setText(String.valueOf(settings.getScale()));
            hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display", settings.getDisplayMode().toString()));
            drawBackgroundButton.setMessage(Text.translatable("starhud.screen.button.background",
                    settings.drawBackground ?
                            Text.translatable("starhud.screen.status.on").getString() :
                            Text.translatable("starhud.screen.status.off").getString()));
            drawTextShadowButton.setMessage(Text.translatable("starhud.screen.button.shadow",
                    settings.drawTextShadow ?
                            Text.translatable("starhud.screen.status.on").getString() :
                            Text.translatable("starhud.screen.status.off").getString()
            ));
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
            childOrderingButton.visible = false;

            if (isMoreOptionActivated)
                showMoreOptionsButtons();
        }
        supressFieldEvents = false;
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
        childOrderingButton.visible = false;
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
            childOrderingButton.visible = true;

            gapField.setEditable(true);
            groupAlignmentButton.active = true;
            childAlignmentButton.active = true;
            childOrderingButton.active = true;

            hudDisplayButton.active = false;
            hudDisplayButton.setMessage(Text.translatable("starhud.screen.button.display_na"));

            supressFieldEvents = true;
            gapField.setText(
                    Integer.toString(hud.groupSettings.gap)
            );
            supressFieldEvents = false;

            groupAlignmentButton.setMessage(Text.translatable(
                    hud.groupSettings.alignVertical ? "starhud.screen.button.group_alignment.vertical" : "starhud.screen.button.group_alignment.horizontal"
            ));

            childAlignmentButton.setMessage(Text.of(hud.groupSettings.getChildAlignment().toString()));
            childOrderingButton.setMessage(Text.of(hud.groupSettings.getChildOrdering().toString()));
        }
    }

    private HUDAction onAlignmentXChangedWithRecommendation(AbstractHUD hud, ScreenAlignmentX nextAlignment) {
        ScreenAlignmentX prevAlignment = hud.getSettings().getOriginX();

        if (prevAlignment == nextAlignment) return null;

        GrowthDirectionX prevGrowth = hud.getSettings().getGrowthDirectionX();
        GrowthDirectionX nextGrowth = hud.getSettings().getGrowthDirectionX().recommendedScreenAlignment(nextAlignment);

        return new ReversibleAction(
                () -> {
                    hud.getSettings().originX = nextAlignment;
                    hud.getSettings().growthDirectionX = nextGrowth;
                    hud.update();
                },
                () -> {
                    hud.getSettings().originX = prevAlignment;
                    hud.getSettings().growthDirectionX = prevGrowth;
                    hud.update();
                }
        );
    }

    private HUDAction onAlignmentYChangedWithRecommendation(AbstractHUD hud, ScreenAlignmentY nextAlignment) {
        ScreenAlignmentY prevAlignment = hud.getSettings().getOriginY();

        if (prevAlignment == nextAlignment) return null;

        GrowthDirectionY prevGrowth = hud.getSettings().getGrowthDirectionY();
        GrowthDirectionY nextGrowth = hud.getSettings().getGrowthDirectionY().recommendedScreenAlignment(nextAlignment);

        return new ReversibleAction(
                () -> {
                    hud.getSettings().originY = nextAlignment;
                    hud.getSettings().growthDirectionY = nextGrowth;
                    hud.update();
                },
                () -> {
                    hud.getSettings().originY = prevAlignment;
                    hud.getSettings().growthDirectionY = prevGrowth;
                    hud.update();
                }
        );
    }

    private HUDAction onAlignmentXChanged(AbstractHUD hud, ScreenAlignmentX prevAlignment, ScreenAlignmentX nextAlignment) {
        if (prevAlignment == nextAlignment) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().originX = nextAlignment;
                    hud.update();
                },
                () -> {
                    hud.getSettings().originX = prevAlignment;
                    hud.update();
                }
        );
    }

    private HUDAction onAlignmentYChanged(AbstractHUD hud, ScreenAlignmentY prevAlignment, ScreenAlignmentY nextAlignment) {
        if (prevAlignment == nextAlignment) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().originY = nextAlignment;
                    hud.update();
                },
                () -> {
                    hud.getSettings().originY = prevAlignment;
                    hud.update();
                }
        );
    }

    private HUDAction onDirectionXChanged(AbstractHUD hud, GrowthDirectionX prevGrowth, GrowthDirectionX nextGrowth) {
        if (prevGrowth == nextGrowth) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().growthDirectionX = nextGrowth;
                    hud.update();
                },
                () -> {
                    hud.getSettings().growthDirectionX = prevGrowth;
                    hud.update();
                }
        );
    }

    private HUDAction onDirectionYChanged(AbstractHUD hud, GrowthDirectionY prevGrowth, GrowthDirectionY nextGrowth) {
        if (prevGrowth == nextGrowth) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().growthDirectionY = nextGrowth;
                    hud.update();
                },
                () -> {
                    hud.getSettings().growthDirectionY = prevGrowth;
                    hud.update();
                }
        );
    }

    private HUDAction onScaleFieldChanged(AbstractHUD hud, float oldScale, float newScale) {

        if (oldScale == newScale) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().scale = newScale;
                    hud.update();
                },
                () -> {
                    hud.getSettings().scale = oldScale;
                    hud.update();
                }
        );

    }

    private HUDAction onHUDDisplayModeChanged(AbstractHUD hud, HUDDisplayMode newDisplayMode) {
        HUDDisplayMode oldDisplayMode = hud.getSettings().getDisplayMode();

        if (oldDisplayMode == newDisplayMode) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().displayMode = newDisplayMode;
                    hud.update();
                },
                () -> {
                    hud.getSettings().displayMode = oldDisplayMode;
                    hud.update();
                }
        );
    }

    private HUDAction onDrawTextShadowChanged(AbstractHUD hud, boolean newDrawTextShadow) {
        boolean oldDrawTextShadow = hud.getSettings().drawTextShadow;

        if (oldDrawTextShadow == newDrawTextShadow) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().drawTextShadow = newDrawTextShadow;
                },
                () -> {
                    hud.getSettings().drawTextShadow = newDrawTextShadow;
                }
        );
    }

    private HUDAction onDrawBackgroundChanged(AbstractHUD hud, boolean newDrawBackground) {
        boolean oldDrawBackground = hud.getSettings().drawBackground;

        if (oldDrawBackground == newDrawBackground) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().drawBackground = newDrawBackground;
                },
                () -> {
                    hud.getSettings().drawBackground = oldDrawBackground;
                }
        );
    }

    private HUDAction onShouldRenderChanged(AbstractHUD hud, boolean newShouldRender) {
        boolean oldShouldRender = hud.getSettings().shouldRender();

        if (oldShouldRender == newShouldRender) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().shouldRender = newShouldRender;
                    hud.update();
                },
                () -> {
                    hud.getSettings().shouldRender = oldShouldRender;
                    hud.update();
                }
        );
    }

    private HUDAction onXFieldChanged(AbstractHUD hud, int oldX, int newX) {
        if (oldX == newX) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().x = newX;
                    hud.update();
                },
                () -> {
                    hud.getSettings().x = oldX;
                    hud.update();
                }
        );
    }

    private HUDAction onYFieldChanged(AbstractHUD hud, int oldY, int newY) {
        if (oldY == newY) return null;

        return new ReversibleAction(
                () -> {
                    hud.getSettings().y = newY;
                    hud.update();
                },
                () -> {
                    hud.getSettings().y = oldY;
                    hud.update();
                }
        );
    }

    private HUDAction onGapFieldChanged(GroupedHUD hud, int oldGap, int newGap) {
        if (oldGap == newGap) return null;

        return new ReversibleAction(
                () -> {
                    hud.groupSettings.gap = newGap;
                },
                () -> {
                    hud.groupSettings.gap = oldGap;
                }
        );
    }

    private HUDAction onChildAlignmentChanged(GroupedHUD hud, GroupedHUDSettings.ChildAlignment newAlignment) {
        GroupedHUDSettings.ChildAlignment oldAlignment = hud.groupSettings.getChildAlignment();

        if (oldAlignment == newAlignment) return null;

        return new ReversibleAction(
                () -> {
                    hud.groupSettings.childAlignment = newAlignment;
                },
                () -> {
                    hud.groupSettings.childAlignment = oldAlignment;
                }
        );
    }
    
    private HUDAction onChildOrderingChanged(GroupedHUD hud, GroupedHUDSettings.ChildOrdering newOrdering) {
        GroupedHUDSettings.ChildOrdering oldOrdering = hud.groupSettings.getChildOrdering();

        if (oldOrdering == newOrdering) return null;

        return new ReversibleAction(
                () -> {
                    hud.groupSettings.childOrdering = newOrdering;
                },
                () -> {
                    hud.groupSettings.childOrdering = oldOrdering;
                }
        );
    }

    private HUDAction onGroupAlignmentChanged(GroupedHUD hud, boolean newAlignment) {
        boolean oldAlignment = hud.groupSettings.alignVertical;

        if (oldAlignment == newAlignment) return null;

        return new ReversibleAction(
                () -> {
                    hud.groupSettings.alignVertical = newAlignment;
                },
                () -> {
                    hud.groupSettings.alignVertical = oldAlignment;
                }
        );
    }

    private HUDAction onGroupChanged(List<AbstractHUD> huds) {

        // we should copy the settings from the first selected hud. so that the position doesn't reset to 0,0.

        List<GroupedHUDSettings> oldGroupedHUDs = new ArrayList<>(Main.settings.hudList.groupedHuds);
        List<String> oldIndividualHUDs = new ArrayList<>(Main.settings.hudList.individualHudIds);

        GroupedHUDSettings newSettings = HUDComponent.getInstance().group(huds);

        List<GroupedHUDSettings> newGroupedHUDs = new ArrayList<>(Main.settings.hudList.groupedHuds);
        List<String> newIndividualHUDs = new ArrayList<>(Main.settings.hudList.individualHudIds);

        return new ReversibleAction(
                () -> {
                    Main.settings.hudList.groupedHuds.clear();
                    Main.settings.hudList.individualHudIds.clear();
                    Main.settings.hudList.groupedHuds.addAll(newGroupedHUDs);
                    Main.settings.hudList.individualHudIds.addAll(newIndividualHUDs);
                    HUDComponent.getInstance().updateActiveHUDs();

                    // also update the setting
                    GroupedHUD newGroup = HUDComponent.getInstance().getGroupedHUDs().get(newSettings.id);
                    newGroup.groupSettings = newSettings;
                },
                () -> {
                    Main.settings.hudList.groupedHuds.clear();
                    Main.settings.hudList.individualHudIds.clear();
                    Main.settings.hudList.groupedHuds.addAll(oldGroupedHUDs);
                    Main.settings.hudList.individualHudIds.addAll(oldIndividualHUDs);
                    HUDComponent.getInstance().updateActiveHUDs();
                }
        );
    }

    private HUDAction onUngroupChanged(GroupedHUD hud) {
        
        // we should copy the settings from the first selected hud. so that the position doesn't reset to 0,0.
        GroupedHUDSettings oldSettings = hud.groupSettings.copy();

        List<GroupedHUDSettings> oldGroupedHUDs = new ArrayList<>(Main.settings.hudList.groupedHuds);
        List<String> oldIndividualHUDs = new ArrayList<>(Main.settings.hudList.individualHudIds);

        HUDComponent.getInstance().unGroup(hud);

        List<GroupedHUDSettings> newGroupedHUDs = new ArrayList<>(Main.settings.hudList.groupedHuds);
        List<String> newIndividualHUDs = new ArrayList<>(Main.settings.hudList.individualHudIds);

        return new ReversibleAction(
                () -> {
                    Main.settings.hudList.groupedHuds.clear();
                    Main.settings.hudList.individualHudIds.clear();
                    Main.settings.hudList.groupedHuds.addAll(newGroupedHUDs);
                    Main.settings.hudList.individualHudIds.addAll(newIndividualHUDs);
                    HUDComponent.getInstance().updateActiveHUDs();
                },
                () -> {
                    Main.settings.hudList.groupedHuds.clear();
                    Main.settings.hudList.individualHudIds.clear();
                    Main.settings.hudList.groupedHuds.addAll(oldGroupedHUDs);
                    Main.settings.hudList.individualHudIds.addAll(oldIndividualHUDs);
                    HUDComponent.getInstance().updateActiveHUDs();

                    // also update the setting
                    GroupedHUD newGroup = HUDComponent.getInstance().getGroupedHUDs().get(oldSettings.id);
                    newGroup.groupSettings = oldSettings;
                }
        );
    }

    // WIP
    private HUDAction onConfigScreenClicked() {
        return null;
    }
}
