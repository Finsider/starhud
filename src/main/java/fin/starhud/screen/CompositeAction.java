package fin.starhud.screen;

import java.util.List;

public class CompositeAction implements HUDAction {

    private final List<HUDAction> actions;

    public CompositeAction(List<HUDAction> actions) {
        this.actions = actions;
    }


    @Override
    public void apply() {
        actions.forEach(HUDAction::apply);
    }

    @Override
    public void undo() {
        for (int i = actions.size() - 1; i >= 0; --i) {
            actions.get(i).undo();
        }
    }
}
