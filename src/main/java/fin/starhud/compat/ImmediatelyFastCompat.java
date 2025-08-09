package fin.starhud.compat;

import net.minecraft.client.gui.DrawContext;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;

public class ImmediatelyFastCompat {

    public static void runBatched(DrawContext context, Runnable runnable) {
        BatchingBuffers.runBatched(context, runnable);
    }
}
