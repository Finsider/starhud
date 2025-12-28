package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface AccessorChatHud {

    @Invoker("getHeight")
    int starhud$getHeight();
    @Invoker("getWidth")
    int starhud$getWidth();

}
