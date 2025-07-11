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
        int bottomY = this.height;
        int spacing = 28;

        xField = new TextFieldWidget(CLIENT.textRenderer, centerX - 40 - 10, bottomY - 20 - 5, 40, 20, Text.of("X"));
        xField.setText(String.valueOf(settings.x));
        xField.setChangedListener(text -> {
            try {
                settings.x = Integer.parseInt(text);
                selectedHUD.update();
            } catch (NumberFormatException ignored) {}
        });
        addDrawableChild(xField);

        yField = new TextFieldWidget(CLIENT.textRenderer, centerX + 10, bottomY - 20 - 5, 40, 20, Text.of("Y"));
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

            BaseHUDSettings settings = selectedHUD.getSettings();

            boolean handled = false;
            boolean isCtrl = (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
            boolean isShift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
            boolean isAlt = (modifiers & GLFW.GLFW_MOD_ALT) != 0;

            int step = isShift ? 5 : 1;

            switch (keyCode) {
                case GLFW.GLFW_KEY_LEFT -> {
                    if (isCtrl) settings.originX = settings.originX.prev();
                    else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.prev();
                    else settings.x -= step;

                    handled = true;
                }

                case GLFW.GLFW_KEY_RIGHT -> {
                    if (isCtrl) settings.originX = settings.originX.next();
                    else if (isAlt) settings.growthDirectionX = settings.growthDirectionX.next();
                    else settings.x += step;

                    handled = true;
                }

                case GLFW.GLFW_KEY_UP -> {
                    if (isCtrl) settings.originY = settings.originY.prev();
                    else if (isAlt) settings.growthDirectionY = settings.growthDirectionY.prev();
                    else settings.y -= step;

                    handled = true;
                }

                case GLFW.GLFW_KEY_DOWN -> {
                    if (isCtrl) settings.originY = settings.originY.next();
                    else if (isAlt) settings.growthDirectionY = settings.growthDirectionY.next();
                    else settings.y += step;

                    handled = true;
                }
            }

            if (handled) {
                selectedHUD.update();
                xField.setText(String.valueOf(settings.x));
                yField.setText(String.valueOf(settings.y));
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
