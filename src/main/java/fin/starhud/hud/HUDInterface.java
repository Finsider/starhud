package fin.starhud.hud;

public interface HUDInterface {

    void appendDraw();

    boolean collect();

    boolean shouldRender();

    void update();

    String getId();
}
