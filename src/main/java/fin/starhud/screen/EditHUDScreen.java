package fin.starhud.screen;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.Box;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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

    // widgets
    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private ButtonWidget originXButton;
    private ButtonWidget originYButton;
    private ButtonWidget scaleButton;

    public EditHUDScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;

        huds = new ArrayList<>(HUDComponent.getInstance().huds);
        huds.add(HUDComponent.getInstance().effectHUD);

        oldSettings = new ArrayList<>();
        for (AbstractHUD p : huds) {
            BaseHUDSettings settings = p.getSettings();
            oldSettings.add(new HUDCoordinate(settings.x, settings.y, settings.originX, settings.originY, settings.scale));
        }
    }

    @Override
    protected void init() {
        this.clearChildren();

        if (selectedHUD == null) return;

        BaseHUDSettings settings = selectedHUD.getSettings();

        int centerX = this.width / 2;
        int centerY = this.height;
        int spacing = 28;

        originXButton = ButtonWidget.builder(
                Text.of("Origin X: " + settings.originX),
                btn -> {
                    settings.originX = settings.originX.next();
                    originXButton.setMessage(Text.of("Origin X: " + settings.originX));
                    selectedHUD.update();
                }
        ).dimensions(centerX - 120 - 10, centerY - spacing * 2, 120, 20).build();
        addDrawableChild(originXButton);

        originYButton = ButtonWidget.builder(
                Text.of("Origin Y: " + settings.originY),
                btn -> {
                    settings.originY = settings.originY.next();
                    originYButton.setMessage(Text.of("Origin Y: " + settings.originY));
                    selectedHUD.update();
                }
        ).dimensions(centerX + 10, centerY - spacing * 2, 120, 20).build();
        addDrawableChild(originYButton);

        scaleButton = ButtonWidget.builder(
                Text.of("Scale: " + settings.scale),
                btn -> {
                    settings.scale = (settings.scale + 1) % 7;
                    scaleButton.setMessage(Text.of("Scale: " + settings.scale));
                    selectedHUD.update();
                }
        ).dimensions(centerX - 60, centerY - spacing, 120, 20).build();
        addDrawableChild(scaleButton);

        xField = new TextFieldWidget(CLIENT.textRenderer, centerX - 40 - 10, centerY - spacing * 3, 40, 20, Text.of("X"));
        xField.setText(String.valueOf(settings.x));
        xField.setChangedListener(text -> {
            try {
                settings.x = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });
        addDrawableChild(xField);

        yField = new TextFieldWidget(CLIENT.textRenderer, centerX + 10, centerY - spacing * 3, 40, 20, Text.of("Y"));
        yField.setText(String.valueOf(settings.y));
        yField.setChangedListener(text -> {
            try {
                settings.y = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });
        addDrawableChild(yField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

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
                    init();
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

            boolean moved = false;
            boolean isShiftPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

            int step = isShiftPressed ? 5 : 1;

            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                selectedHUD.getSettings().x -= step;
                moved = true;
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                selectedHUD.getSettings().x += step;
                moved = true;
            } else if (keyCode == GLFW.GLFW_KEY_UP) {
                selectedHUD.getSettings().y -= step;
                moved = true;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                selectedHUD.getSettings().y += step;
                moved = true;
            }

            if (moved) {
                selectedHUD.update();
                xField.setText(String.valueOf(selectedHUD.getSettings().x));
                yField.setText(String.valueOf(selectedHUD.getSettings().y));
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

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    private static class HUDCoordinate {
        int x, y;
        ScreenAlignmentX alignmentX;
        ScreenAlignmentY alignmentY;
        int scale;

        public HUDCoordinate(int x, int y, ScreenAlignmentX alignmentX, ScreenAlignmentY alignmentY, int scale) {
            this.x = x;
            this.y = y;
            this.alignmentX = alignmentX;
            this.alignmentY = alignmentY;
            this.scale = scale;
        }
    }
}
