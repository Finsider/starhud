package fin.starhud.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.MathHelper;

public class TPSTracker {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final int SAMPLE_SIZE = 20;
    private static final double[] tickTimes = new double[SAMPLE_SIZE];
    private static int tickIndex = 0;
    private static int validSamples = 0;
    private static long lastTickTime = -1;
    private static long lastWorldTime = -1;

    private static double tps = 20;
    private static double mspt = -1;

    public static double getTPS() {
        return tps;
    }

    public static double getMSPT() {
        return mspt;
    }

    // this update implementation is based on @maurohon MiniHUD tps update implementation
    // see: https://github.com/maruohon/minihud/blob/5d2449886e26fba45646ea89f5a80f706d196e11/src/main/java/minihud/data/TpsDataManager.java#L70

    public static void onWorldTimeUpdate(long totalWorldTime) {

        if (totalWorldTime == lastWorldTime) {
            return;
        }

        if (CLIENT.getServer() != null) {
            mspt = CLIENT.getServer().getAverageTickTime();
            tps = (double) Math.round(MathHelper.clamp(TimeHelper.SECOND_IN_MILLIS / mspt, 0.0F, 20.0F) * 10) / 10;
            return;
        }

        // grab the tick by getting the lastUpdated with the new one, and divide it with the elapsedTick for more precision.

        long currTickTime = System.currentTimeMillis();
        long elapsedTicks = totalWorldTime - lastWorldTime;

        // this doesn't give the server's actual mspt, this only gives the total time after the time is getting updated, which is updated every 20 ticks usually, and not the time it takes for the server to process one tick.
        mspt = (double) (currTickTime - lastTickTime) / elapsedTicks;

        lastTickTime = currTickTime;
        lastWorldTime = totalWorldTime;

        // add mspt to sample

        tickTimes[tickIndex] = mspt;
        tickIndex = (tickIndex + 1) % SAMPLE_SIZE;
        if (validSamples < SAMPLE_SIZE) validSamples++;

        // tps calculation by getting the samples average.

        double total = 0;
        for (int i = 0; i < validSamples; i++) {
            total += tickTimes[i];
        }

        double avg = total / (double) validSamples;
        if (avg > 0)
            tps = (double) Math.round(Math.min(1000.0 / avg, 20.0) * 10) / 10;
    }
}
