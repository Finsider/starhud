package fin.starhud.screen;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.*;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDComponent;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class EditHUDScreen extends Screen {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final int PADDING = 25;
    private static final int WIDGET_WIDTH = 100;
    private static final int WIDGET_HEIGHT = 20;
    private static final int TEXT_FIELD_WIDTH = 40;
    private static final int SQUARE_WIDGET_LENGTH = 20;
    private static final int GAP = 5;

    public Screen parent;

    private final List<AbstractHUD> huds;
    private final List<HUDCoordinate> oldSettings;

    private boolean dragging = false;
    private AbstractHUD selectedHUD = null;

    private boolean isHelpActivated = false;
    private boolean isMoreOptionActivated = false;

    // widgets
    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private ButtonWidget alignmentXButton;
    private ButtonWidget alignmentYButton;
    private ButtonWidget directionXButton;
    private ButtonWidget directionYButton;
    private ButtonWidget scaleButton;

    private static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

    private static final String[] HELP_KEYS = {
            "[Arrow Keys]",
            "[⇧ Shift + Arrows]",
            isMac ? "[⌘ Cmd + Arrows]" : "[Ctrl + Arrows]",
            "[⌥ Alt + Arrows]",
            isMac ? "[⌘ Cmd + R]" : "[Ctrl + R]",
            "[Click]",
            "[Drag]"
    };

    private static final String[] HELP_INFOS = {
            "Move HUD by 1",
            "Move HUD by 5",
            "Change Alignment",
            "Change Growth Direction",
            "Revert All Changes",
            "Select HUD",
            "Move HUD"
    };

    private static final int HELP_HEIGHT = 5 + (HELP_KEYS.length * 9) + 5;

    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

        HUDComponent.getInstance().setShouldRenderInGameScreen(false);
        huds = new ArrayList<>(HUDComponent.getInstance().huds);
        huds.add(HUDComponent.getInstance().effectHUD);

        oldSettings = new ArrayList<>();
        for (AbstractHUD p : huds) {
            BaseHUDSettings settings = p.getSettings();
            oldSettings.add(new HUDCoordinate(settings.x, settings.y, settings.originX, settings.originY, settings.growthDirectionX, settings.growthDirectionY, settings.scale));
        }
    }

    @Override
    protected void init() {

        final int CENTER_X = this.width / 2;
        final int CENTER_Y = this.height / 2 + (PADDING / 2);

        HUDComponent.getInstance().updateAll();

        xField = new TextFieldWidget(
                CLIENT.textRenderer,
                CENTER_X - TEXT_FIELD_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP,
                CENTER_Y - PADDING,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.of("X")
        );
        yField = new TextFieldWidget(
                CLIENT.textRenderer,
                CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP,
                CENTER_Y - PADDING,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Text.of("Y")
        );

        alignmentXButton = ButtonWidget.builder(
                Text.of("X Alignment: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().originX = selectedHUD.getSettings().originX.next();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.recommendedScreenAlignment(selectedHUD.getSettings().originX);
                    selectedHUD.update();
                    alignmentXButton.setMessage(Text.of("X Alignment: " + selectedHUD.getSettings().originX));
                }
        ).dimensions(CENTER_X - WIDGET_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP, CENTER_Y - PADDING * 2, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        alignmentYButton = ButtonWidget.builder(
                Text.of("Y Alignment: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().originY = selectedHUD.getSettings().originY.next();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.recommendedScreenAlignment(selectedHUD.getSettings().originY);
                    selectedHUD.update();
                    alignmentYButton.setMessage(Text.of("Y Alignment: " + selectedHUD.getSettings().originY));
                }
        ).dimensions(CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP, CENTER_Y - PADDING * 2, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        scaleButton = ButtonWidget.builder(
                Text.of("N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().scale = (selectedHUD.getSettings().scale + 1) % 7;
                    selectedHUD.update();
                    scaleButton.setMessage(Text.of(Integer.toString(selectedHUD.getSettings().scale)));
                }
        ).tooltip(Tooltip.of(Text.of("Scale"))).dimensions(CENTER_X - (SQUARE_WIDGET_LENGTH / 2), CENTER_Y - PADDING * 2, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH).build();

        directionXButton = ButtonWidget.builder(
                Text.of("X Direction: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.next();
                    selectedHUD.update();
                    directionXButton.setMessage(Text.of("X Direction: " + selectedHUD.getSettings().growthDirectionX));
                }
        ).dimensions(CENTER_X - WIDGET_WIDTH - (SQUARE_WIDGET_LENGTH / 2) - GAP, CENTER_Y - PADDING * 3, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        directionYButton = ButtonWidget.builder(
                Text.of("Y Direction: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.next();
                    selectedHUD.update();
                    directionYButton.setMessage(Text.of("Y Direction: " + selectedHUD.getSettings().growthDirectionY));
                }
        ).dimensions(CENTER_X + (SQUARE_WIDGET_LENGTH / 2) + GAP, CENTER_Y - PADDING * 3, WIDGET_WIDTH, WIDGET_HEIGHT).build();

        xField.setChangedListener(text -> {
            if (selectedHUD == null) return;
            try {
                selectedHUD.getSettings().x = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        yField.setChangedListener(text -> {
            if (selectedHUD == null) return;
            try {
                selectedHUD.getSettings().y = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });

        ButtonWidget helpButton = ButtonWidget.builder(
                Text.of("?"),
                button -> {
                    isHelpActivated = !isHelpActivated;
                    onHelpSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.of("Help")))
                .dimensions(CENTER_X - SQUARE_WIDGET_LENGTH - (GAP / 2), this.height - SQUARE_WIDGET_LENGTH - GAP, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();

        ButtonWidget moreOptionButton = ButtonWidget.builder(
                Text.of("+"),
                button -> {
                    isMoreOptionActivated = !isMoreOptionActivated;
                    onMoreOptionSwitched();
                }
        )
                .tooltip(Tooltip.of(Text.of("More Options")))
                .dimensions(CENTER_X + (GAP / 2), this.height - SQUARE_WIDGET_LENGTH - GAP, SQUARE_WIDGET_LENGTH, SQUARE_WIDGET_LENGTH)
                .build();


        int terminatorWidth = 70;
        addDrawableChild(ButtonWidget.builder(Text.of("Save & Quit"), button -> {
            AutoConfig.getConfigHolder(Settings.class).save();
            onClose();
        }).dimensions(CENTER_X + (GAP / 2) + SQUARE_WIDGET_LENGTH + GAP, this.height - WIDGET_HEIGHT - GAP, terminatorWidth, WIDGET_HEIGHT).build());

        addDrawableChild(ButtonWidget.builder(Text.of("Cancel"), button -> {
            close();
        }).dimensions(CENTER_X - (GAP / 2) - terminatorWidth - SQUARE_WIDGET_LENGTH - GAP, this.height - WIDGET_HEIGHT - GAP, terminatorWidth, WIDGET_HEIGHT).build());

        alignmentXButton.visible = false;
        directionXButton.visible = false;
        alignmentYButton.visible = false;
        directionYButton.visible = false;
        xField.visible = false;
        yField.visible = false;
        scaleButton.visible = false;

        addDrawableChild(helpButton);
        addDrawableChild(moreOptionButton);

        addDrawableChild(alignmentXButton);
        addDrawableChild(alignmentYButton);
        addDrawableChild(directionXButton);
        addDrawableChild(directionYButton);
        addDrawableChild(scaleButton);
        addDrawableChild(xField);
        addDrawableChild(yField);

        updateFieldsFromSelectedHUD();
    }

    private void updateFieldsFromSelectedHUD() {
        if (xField == null || yField == null) return;

        if (selectedHUD == null) {
            xField.setEditable(false);
            yField.setEditable(false);
            xField.setText("N/A");
            yField.setText("N/A");

            alignmentXButton.setMessage(Text.of("X Alignment: N/A"));
            directionXButton.setMessage(Text.of("X Direction: N/A"));
            alignmentYButton.setMessage(Text.of("Y Alignment: N/A"));
            directionYButton.setMessage(Text.of("Y Direction: N/A"));

            scaleButton.setMessage(Text.of("N/A"));

            alignmentXButton.active = false;
            directionXButton.active = false;
            alignmentYButton.active = false;
            directionYButton.active = false;
            scaleButton.active = false;
        } else {
            BaseHUDSettings settings = selectedHUD.getSettings();
            xField.setText(String.valueOf(settings.x));
            yField.setText(String.valueOf(settings.y));

            alignmentXButton.setMessage(Text.of("X Alignment: " + selectedHUD.getSettings().originX));
            directionXButton.setMessage(Text.of("X Direction: " + selectedHUD.getSettings().growthDirectionX));
            alignmentYButton.setMessage(Text.of("Y Alignment: " + selectedHUD.getSettings().originY));
            directionYButton.setMessage(Text.of("Y Direction: " + selectedHUD.getSettings().growthDirectionY));
            scaleButton.setMessage(Text.of(Integer.toString(selectedHUD.getSettings().scale)));

            alignmentXButton.active = true;
            directionXButton.active = true;
            alignmentYButton.active = true;
            directionYButton.active = true;
            scaleButton.active = true;
            xField.setEditable(true);
            yField.setEditable(true);

            if (isMoreOptionActivated) {
                alignmentXButton.visible = true;
                directionXButton.visible = true;
                alignmentYButton.visible = true;
                directionYButton.visible = true;
                xField.visible = true;
                yField.visible = true;
                scaleButton.visible = true;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // draw help
        if (isHelpActivated) {
            final int CENTER_X = this.width / 2;
            final int CENTER_Y = this.height / 2 + (PADDING / 2);
            renderHelp(context, CENTER_X, CENTER_Y + GAP);
            if (selectedHUD != null)
                renderHUDInformation(context, CENTER_X, CENTER_Y + GAP  + HELP_HEIGHT + GAP);
        }

        // draw X and Y next to their textField.
        if (xField.isVisible() && yField.isVisible()) {
            context.drawText(CLIENT.textRenderer, "X:", xField.getX() - 5 - 2 - 3, xField.getY() + 6, 0xFFFFFFFF, true);
            context.drawText(CLIENT.textRenderer, ":Y", yField.getX() + yField.getWidth() + 3, yField.getY() + 6, 0xFFFFFFFF, true);
        }

        // draw all visible hud bounding boxes.
        renderBoundingBoxes(context, mouseX, mouseY);
    }

    private void renderBoundingBoxes(DrawContext context, int mouseX, int mouseY) {
        for (AbstractHUD p: huds) {
            if (!p.shouldRender()) continue; // if not rendered
            if (!p.render(context)) continue; // if ALSO not rendered (either failed or no information to render)

            Box boundingBox = p.getBoundingBox();

            int x = boundingBox.getX();
            int y = boundingBox.getY();
            int width = boundingBox.getWidth();
            int height = boundingBox.getHeight();
            int color = boundingBox.getColor();

            if (p.isScaled()) {
                context.getMatrices().pushMatrix();
                p.setHUDScale(context);

                context.drawBorder(x, y, width, height, color);
                if (isHovered(x, y, width, height, mouseX, mouseY, p.getSettings().scale)) {
                    context.fill(x, y, x + width, y + height, (color & 0x00FFFFFF) | 0x80000000);
                }
                if (p == selectedHUD) {
                    context.fill(x, y, x + width, y + height, 0x8087ceeb);
                }
                context.getMatrices().popMatrix();
                continue;
            }

            context.drawBorder(x, y, width, height, color);
            if (isHovered(x, y, width, height, mouseX, mouseY, p.getSettings().scale)) {
                context.fill(x, y, x + width, y + height, (color & 0x00FFFFFF) | 0x80000000);
            }
            if (p == selectedHUD) {
                context.fill(x, y, x + width, y + height, 0x8087ceeb);
            }
        }
    }

    private void renderHelp(DrawContext context, int x, int y) {
        int padding = 5;

        int lineHeight = CLIENT.textRenderer.fontHeight;
        int maxKeyWidth = CLIENT.textRenderer.getWidth(HELP_KEYS[1]);
        int maxInfoWidth = CLIENT.textRenderer.getWidth(HELP_INFOS[3]);

        int width = padding + maxKeyWidth + padding + 1 + padding + maxInfoWidth + padding;
        int height = padding + (lineHeight * HELP_KEYS.length) + padding - 2;

        x -= width / 2;
        y -= padding;

        context.fill(x, y, x + width, y + height, 0x80000000);

        for (int i = 0; i < HELP_KEYS.length; ++i) {
            String key = HELP_KEYS[i];
            String info = HELP_INFOS[i];

            context.drawText(CLIENT.textRenderer, key, x + padding, y + padding, 0xFFFFFFFF, false);
            context.drawText(CLIENT.textRenderer, info, x + padding + maxKeyWidth + padding + 1 + padding, y + padding, 0xFFFFFFFF, false);

            y += lineHeight;
        }
    }

    private void renderHUDInformation(DrawContext context, int x, int y) {
        String text = selectedHUD.getName();
        int textWidth = CLIENT.textRenderer.getWidth(text);
        int padding = 5;

        x -= (textWidth / 2);

        context.fill(x - padding, y - padding, x + textWidth + padding, y + CLIENT.textRenderer.fontHeight - 2 + padding, 0x80000000);
        context.drawText(CLIENT.textRenderer, text, x, y, 0xFFFFFFFF, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (AbstractHUD hud : huds) {
                if (!hud.shouldRender())
                    continue;

                Box boundingBox = hud.getBoundingBox();
                int scale = hud.getSettings().scale;
                int x = boundingBox.getX();
                int y = boundingBox.getY();
                int width = boundingBox.getWidth();
                int height = boundingBox.getHeight();


                if (isHovered(x, y, width, height, (int) mouseX, (int) mouseY, scale)) {
                    selectedHUD = hud;
                    dragging = true;
                    updateFieldsFromSelectedHUD();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            xField.setText(String.valueOf(selectedHUD.getSettings().x));
            yField.setText(String.valueOf(selectedHUD.getSettings().y));
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private double accumulatedX = 0, accumulatedY = 0;

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && selectedHUD != null && button == 0) {
            double scaleFactor = selectedHUD.getSettings().getScaledFactor();

            accumulatedX += deltaX * scaleFactor;
            accumulatedY += deltaY * scaleFactor;

            int dx = 0;
            int dy = 0;

            if (Math.abs(accumulatedX) >= 1) {
                dx = (int) accumulatedX;
                accumulatedX -= dx;
            }

            if (Math.abs(accumulatedY) >= 1) {
                dy = (int) accumulatedY;
                accumulatedY -= dy;
            }

            if (dx != 0 || dy != 0) {
                selectedHUD.getSettings().x += dx;
                selectedHUD.getSettings().y += dy;

                selectedHUD.update();
            }

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selectedHUD != null) {

            BaseHUDSettings settings = selectedHUD.getSettings();

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

                case GLFW.GLFW_KEY_R -> {
                    if (isCtrl) {
                        revertChanges();
                        handled = true;
                    }
                }

                case GLFW.GLFW_KEY_MINUS ->  {
                    if (!yField.isFocused() && !xField.isFocused()) {
                        settings.scale = (settings.scale + 6) % 7;
                        handled = true;
                    }
                }

                case GLFW.GLFW_KEY_EQUAL ->  {
                    if (isShift && !yField.isFocused() && !xField.isFocused()) {
                        settings.scale = (settings.scale + 1) % 7;
                        handled = true;
                    }
                }
            }

            if (handled) {
                selectedHUD.update();
                updateFieldsFromSelectedHUD();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY, int HUDScale) {
        float scale = HUDScale == 0 ? 1 : (float) CLIENT.getWindow().getScaleFactor() / HUDScale;
        int scaledMouseX = (int) (mouseX * scale);
        int scaledMouseY = (int) (mouseY * scale);
        return scaledMouseX >= x && scaledMouseX <= x + width &&
                scaledMouseY >= y && scaledMouseY <= y + height;
    }

    private boolean isDirty() {
        for (int i = 0; i < huds.size(); i++) {
            AbstractHUD hud = huds.get(i);
            BaseHUDSettings current = hud.getSettings();
            HUDCoordinate original = oldSettings.get(i);

            if (current.x != original.x || current.y != original.y
                    || current.originX != original.alignmentX
                    || current.originY != original.alignmentY
                    || current.scale != original.scale) {
                return true;
            }
        }
        return false;
    }

    private void revertChanges() {
        for (int i = 0; i < huds.size(); ++i) {
            BaseHUDSettings current = huds.get(i).getSettings();
            HUDCoordinate original = oldSettings.get(i);

            current.x = original.x;
            current.y = original.y;
            current.originX = original.alignmentX;
            current.originY = original.alignmentY;
            current.growthDirectionX = original.growthDirectionX;
            current.growthDirectionY = original.growthDirectionY;
            current.scale = original.scale;
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
        HUDComponent.getInstance().setShouldRenderInGameScreen(true);
    }

    private void onHelpSwitched() {
    }

    private void onMoreOptionSwitched() {
        if (isMoreOptionActivated) {
            alignmentXButton.visible = true;
            directionXButton.visible = true;
            alignmentYButton.visible = true;
            directionYButton.visible = true;
            xField.visible = true;
            yField.visible = true;
            scaleButton.visible = true;
        } else {
            alignmentXButton.visible = false;
            directionXButton.visible = false;
            alignmentYButton.visible = false;
            directionYButton.visible = false;
            xField.visible = false;
            yField.visible = false;
            scaleButton.visible = false;
        }
    }

    private static class HUDCoordinate {
        int x, y;
        ScreenAlignmentX alignmentX;
        ScreenAlignmentY alignmentY;
        GrowthDirectionX growthDirectionX;
        GrowthDirectionY growthDirectionY;
        int scale;

        public HUDCoordinate(int x, int y, ScreenAlignmentX alignmentX, ScreenAlignmentY alignmentY, GrowthDirectionX growthDirectionX, GrowthDirectionY growthDirectionY, int scale) {
            this.x = x;
            this.y = y;
            this.alignmentX = alignmentX;
            this.alignmentY = alignmentY;
            this.growthDirectionX = growthDirectionX;
            this.growthDirectionY = growthDirectionY;
            this.scale = scale;
        }
    }
}
