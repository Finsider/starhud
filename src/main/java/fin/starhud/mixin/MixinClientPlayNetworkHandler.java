package fin.starhud.mixin;

import fin.starhud.helper.TPSTracker;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPlayNetworkHandler {

    @Inject(method = "handleSetTime", at = @At("HEAD"))
    private void onLevelTimeUpdate(ClientboundSetTimePacket packet, CallbackInfo ci) {
        TPSTracker.onLevelTimeUpdate(packet.gameTime());
    }
}
