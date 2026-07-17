package fin.starhud.screen.history;

import java.util.ArrayDeque;
import java.util.Deque;

public class HUDHistory {

    private final Deque<HUDAction> undoStack = new ArrayDeque<>();
    private final Deque<HUDAction> redoStack = new ArrayDeque<>();

    private static final int MAX_HISTORY = 512;

    public void execute(HUDAction action) {
        if (action == null) return;

        action.apply();
        commit(action);
    }

    public void commit(HUDAction action) {
        if (action == null) return;

        undoStack.push(action);
        redoStack.clear();

        while (undoStack.size() > MAX_HISTORY) {
            undoStack.removeLast();
        }
    }

    public void undo() {
        if (!canUndo()) return;

        HUDAction action = undoStack.pop();
        action.undo();
        redoStack.push(action);
    }

    public void redo() {
        if (!canRedo()) return;

        HUDAction action = redoStack.pop();
        action.apply();
        undoStack.push(action);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
