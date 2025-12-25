package fin.starhud.screen;

public class ReversibleAction implements HUDAction {

    private final Runnable apply;
    private final Runnable undo;

    public ReversibleAction(Runnable apply, Runnable undo) {
        this.apply = apply;
        this.undo = undo;
    }

    @Override
    public void apply() {
        apply.run();
    }

    @Override
    public void undo() {
        undo.run();
    }
}
