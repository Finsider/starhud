package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public interface AccessorBossBarHud {

    @Accessor("bossBars")
    Map<UUID, ClientBossBar> getBossBars();

}
