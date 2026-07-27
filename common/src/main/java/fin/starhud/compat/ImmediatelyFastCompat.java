package fin.starhud.compat;

import net.raphimc.immediatelyfastapi.ImmediatelyFastApi;

public class ImmediatelyFastCompat {

    public static void beginHudBatching() {
        ImmediatelyFastApi.getApiImpl().getBatching().beginHudBatching();
    }

    public static void endHudBatching() {
        ImmediatelyFastApi.getApiImpl().getBatching().endHudBatching();
    }
}
