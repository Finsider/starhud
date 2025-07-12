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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class EditHUDScreen extends Screen {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public Screen parent;

    private final List<AbstractHUD> huds;
    private final List<HUDCoordinate> oldSettings;

    private boolean dragging = false;
    private AbstractHUD selectedHUD = null;

    private boolean isHelpActivated = false;

    // widgets
    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private ButtonWidget alignmentXButton;
    private ButtonWidget alignmentYButton;
    private ButtonWidget directionXButton;
    private ButtonWidget directionYButton;

    private static final String[] HELPS_KEY = {
            "[ARROW KEYS]",
            "[SHIFT + ARROWS]",
            "[CTRL + ARROWS]",
            "[ALT + ARROWS]",
            "[CTRL + R]",
            "[Click]",
            "[Drag]",
    };

    private static final String[] HELPS_INFO = {
            "Move HUD by 1",
            "Move HUD by 5",
            "Change Alignment",
            "Change Growth Direction",
            "Revert All Changes",
            "Select HUD",
            "Move HUD"
    };

    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

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

        HUDComponent.getInstance().updateAll();

        int centerX = this.width / 2;
        int bottomY = this.height;
        int padding = 25;

        int widgetWidth = 100;
        int widgetHeight = 20;

        int textFieldWidth = 40;

        xField = new TextFieldWidget(CLIENT.textRenderer, centerX - textFieldWidth - 10 - 5, bottomY - padding * 2, textFieldWidth, widgetHeight, Text.of("X"));
        yField = new TextFieldWidget(CLIENT.textRenderer, centerX + 10 + 5, bottomY - padding * 2, textFieldWidth, widgetHeight, Text.of("Y"));

        alignmentXButton = ButtonWidget.builder(
                Text.of("X Alignment: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().originX = selectedHUD.getSettings().originX.next();
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.recommendedScreenAlignment(selectedHUD.getSettings().originX);
                    selectedHUD.update();
                    alignmentXButton.setMessage(Text.of("X Alignment: " + selectedHUD.getSettings().originX));
                }
        ).dimensions(centerX - widgetWidth - 10 - 5, bottomY - padding * 3, widgetWidth, widgetHeight).build();

        directionXButton = ButtonWidget.builder(
                Text.of("X Direction: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().growthDirectionX = selectedHUD.getSettings().growthDirectionX.next();
                    selectedHUD.update();
                    directionXButton.setMessage(Text.of("X Direction: " + selectedHUD.getSettings().growthDirectionX));
                }
        ).dimensions(centerX - widgetWidth - 10 - 5, bottomY - padding * 4, widgetWidth, widgetHeight).build();

        alignmentYButton = ButtonWidget.builder(
                Text.of("Y Alignment: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().originY = selectedHUD.getSettings().originY.next();
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.recommendedScreenAlignment(selectedHUD.getSettings().originY);
                    selectedHUD.update();
                    alignmentYButton.setMessage(Text.of("Y Alignment: " + selectedHUD.getSettings().originY));
                }
        ).dimensions(centerX + 10 + 5, bottomY - padding * 3, widgetWidth, widgetHeight).build();

        directionYButton = ButtonWidget.builder(
                Text.of("Y Direction: N/A"),
                button -> {
                    if (selectedHUD == null) return;
                    selectedHUD.getSettings().growthDirectionY = selectedHUD.getSettings().growthDirectionY.next();
                    selectedHUD.update();
                    directionYButton.setMessage(Text.of("Y Direction: " + selectedHUD.getSettings().growthDirectionY));
                }
        ).dimensions(centerX + 10 + 5, bottomY - padding * 4, widgetWidth, widgetHeight).build();

        ButtonWidget helpButton = ButtonWidget.builder(
                Text.of("?"),
                button -> {
                    isHelpActivated = !isHelpActivated;
                    onHelpSwitched();
                }
        ).dimensions(centerX - 20 / 2, bottomY - padding, 20, 20).build();

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

        addDrawableChild(ButtonWidget.builder(Text.of("Save & Quit"), button -> {
            AutoConfig.getConfigHolder(Settings.class).save();
            this.client.setScreen(this.parent);
        }).dimensions(centerX + 10 + 5, bottomY - padding, widgetWidth, widgetHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.of("Cancel"), button -> {
            close();
        }).dimensions(centerX - widgetWidth - 10 - 5, bottomY - padding, widgetWidth, widgetHeight).build());

        alignmentXButton.visible = false;
        directionXButton.visible = false;
        alignmentYButton.visible = false;
        directionYButton.visible = false;

        addDrawableChild(xField);
        addDrawableChild(yField);
        addDrawableChild(helpButton);

        addDrawableChild(alignmentXButton);
        addDrawableChild(alignmentYButton);
        addDrawableChild(directionXButton);
        addDrawableChild(directionYButton);

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

            alignmentXButton.visible = false;
            directionXButton.visible = false;
            alignmentYButton.visible = false;
            directionYButton.visible = false;
        } else {
            BaseHUDSettings settings = selectedHUD.getSettings();
            xField.setEditable(true);
            yField.setEditable(true);
            xField.setText(String.valueOf(settings.x));
            yField.setText(String.valueOf(settings.y));

            alignmentXButton.setMessage(Text.of("X Alignment: " + selectedHUD.getSettings().originX));
            directionXButton.setMessage(Text.of("X Direction: " + selectedHUD.getSettings().growthDirectionX));
            alignmentYButton.setMessage(Text.of("Y Alignment: " + selectedHUD.getSettings().originY));
            directionYButton.setMessage(Text.of("Y Direction: " + selectedHUD.getSettings().growthDirectionY));

            if (isHelpActivated) {
                alignmentXButton.visible = true;
                directionXButton.visible = true;
                alignmentYButton.visible = true;
                directionYButton.visible = true;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // draw help
        if (isHelpActivated) {
//            if (selectedHUD != null)
//                renderHUDInformation(context, this.width / 2, this.height - 5 - (25 * 2) - 13);

            renderHelp(context, this.width / 2, this.height - 5 - (25 * 4) - 13 - 63 - 1);
        }

        // draw X and Y next to their textField.
        context.drawText(CLIENT.textRenderer, "X:", xField.getX() - 5 - 2 - 3, xField.getY() + 6, 0xFFFFFFFF, true);
        context.drawText(CLIENT.textRenderer, ":Y", yField.getX() + yField.getWidth() + 3, yField.getY() + 6, 0xFFFFFFFF, true);

        // draw all visible hud bounding boxes.
        renderBoundingBoxes(context, mouseX, mouseY);
    }

    private void renderBoundingBoxes(DrawContext context, int mouseX, int mouseY) {
        for (AbstractHUD p: huds) {
            if (!p.shouldRender()) continue;

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
        int maxKeyWidth = CLIENT.textRenderer.getWidth(HELPS_KEY[1]);
        int maxInfoWidth = CLIENT.textRenderer.getWidth(HELPS_INFO[3]);

        int width = padding + maxKeyWidth + padding + 1 + padding + maxInfoWidth + padding;
        int height = padding + (lineHeight * HELPS_KEY.length) + padding - 2;

        x -= width / 2;
        y -= padding;

        context.fill(x, y, x + width, y + height, 0x80000000);

        for (int i = 0; i < HELPS_KEY.length; ++i) {
            String key = HELPS_KEY[i];
            String info = HELPS_INFO[i];

            context.drawText(CLIENT.textRenderer, key, x + padding, y + padding, 0xFFFFFFFF, false);
            context.drawText(CLIENT.textRenderer, info, x + padding + maxKeyWidth + padding + 1 + padding, y + padding, 0xFFFFFFFF, false);

            y += lineHeight;
        }
    }

    private void renderHUDInformation(DrawContext context, int x, int y) {
        String text = selectedHUD.getName();
        String alignment = "Alignment [" + selectedHUD.getSettings().originX + "," + selectedHUD.getSettings().originY + "]";
        String direction = "Direction [" + selectedHUD.getSettings().growthDirectionX + "," + selectedHUD.getSettings().growthDirectionY + "]";

        int textWidth = CLIENT.textRenderer.getWidth(text);
        int alignmentWidth = CLIENT.textRenderer.getWidth(alignment);
        int directionWidth = CLIENT.textRenderer.getWidth(direction);

        int maxWidth = Math.max(textWidth, Math.max(alignmentWidth, directionWidth));
        int lineHeight = CLIENT.textRenderer.fontHeight;

        int xText = x - textWidth / 2;
        int xAlignment = x - alignmentWidth / 2;
        int xDirection = x - directionWidth / 2;

        y -= lineHeight * 2;
        x -= maxWidth / 2;

        int padding = 5;

        context.fill(x - padding, y - padding, x + maxWidth + padding, y + (lineHeight *  3) + padding - 1, 0x80000000);
        context.drawText(CLIENT.textRenderer, text, xText, y, 0xFFFFFFFF, false);
        context.drawText(CLIENT.textRenderer, alignment, xAlignment, y + lineHeight, 0xFFFFFFFF, false);
        context.drawText(CLIENT.textRenderer, direction, xDirection, y + (lineHeight * 2), 0xFFFFFFFF, false);
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
            boolean isCtrl = (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
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
                            this.client.setScreen(this.parent);
                        } else {
                            this.client.setScreen(this);
                        }
                    },
                    Text.of("Discard Changes?"),
                    Text.of("You have unsaved changes. Do you want to discard them?")
            ));
        } else {
            this.client.setScreen(this.parent);
        }
    }

    private void onHelpSwitched() {
        if (isHelpActivated) {
            alignmentXButton.visible = true;
            directionXButton.visible = true;
            alignmentYButton.visible = true;
            directionYButton.visible = true;
        } else {
            alignmentXButton.visible = false;
            directionXButton.visible = false;
            alignmentYButton.visible = false;
            directionYButton.visible = false;
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
