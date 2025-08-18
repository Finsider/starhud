package fin.starhud.hud;

public interface HUDInterface {

    boolean appendDraw();

    boolean collect();

    boolean shouldRender();

    void update();

    String getId();
}
