package fin.starhud.integration;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Method;

public class ImmediatelyFastAPI {

    private static boolean isImmediatelyFastPresent;
    private static Object batchingAccessInstance;
    private static Method beginHudBatchingMethod;
    private static Method endHudBatchingMethod;

    public static void init() {
        isImmediatelyFastPresent = FabricLoader.getInstance().isModLoaded("immediatelyfast");

        if (isImmediatelyFastPresent) {
            try {
                Class<?> apiClass = Class.forName("net.raphimc.immediatelyfastapi.ImmediatelyFastApi");
                Method getApiImplMethod = apiClass.getMethod("getApiImpl");
                Object apiImplInstance = getApiImplMethod.invoke(null);

                Method getBatchingMethod = apiImplInstance.getClass().getMethod("getBatching");
                batchingAccessInstance = getBatchingMethod.invoke(apiImplInstance);

                Class<?> batchingAccessClass = Class.forName("net.raphimc.immediatelyfastapi.BatchingAccess");
                beginHudBatchingMethod = batchingAccessClass.getMethod("beginHudBatching");
                endHudBatchingMethod = batchingAccessClass.getMethod("endHudBatching");
            } catch (Exception ignored) {
                isImmediatelyFastPresent = false;
            }
        }
    }

    public static boolean isModPresent() {
        return isImmediatelyFastPresent;
    }

    public static void beginHudBatching() {
        if (batchingAccessInstance != null && beginHudBatchingMethod != null) {
            try {
                beginHudBatchingMethod.invoke(batchingAccessInstance);
            } catch (Exception ignored) {

            }
        }
    }

    public static void endHudBatching() {
        if (batchingAccessInstance != null && endHudBatchingMethod != null) {
            try {
                endHudBatchingMethod.invoke(batchingAccessInstance);
            } catch (Exception ignored) {

            }
        }
    }

}

