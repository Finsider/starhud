package fin.starhud.mixin;

import fin.starhud.helper.StatusEffectAttribute;
import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.statuseffect.NegativeEffectHUD;
import fin.starhud.hud.implementation.statuseffect.PositiveEffectHUD;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    // this is the way to ENSURE that for every change in status effect, the old value is removed.
    // I don't know if this is safe tho...

    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD")
    )
    private void onSetMobEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        final PositiveEffectHUD POSITIVE_EFFECT_HUD = (PositiveEffectHUD) HUDComponent.getInstance().getHUD(HUDId.POSITIVE_EFFECT);
        final NegativeEffectHUD NEGATIVE_EFFECT_HUD = (NegativeEffectHUD) HUDComponent.getInstance().getHUD(HUDId.NEGATIVE_EFFECT);

        if (!POSITIVE_EFFECT_HUD.shouldRender() && !NEGATIVE_EFFECT_HUD.shouldRender())
            return;

        StatusEffectAttribute statusEffectAttribute = StatusEffectAttribute.getStatusEffectAttribute(effect);

        if (StatusEffectAttribute.shouldUpdate(effect, statusEffectAttribute))
            StatusEffectAttribute.updateStatusEffectAttribute(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient());
    }

// using removeMobEffectInternal() instead of removeMobEffect() because the former worked and the latter didn't, I don't know why.
    // remove status effect from the player status effect list. Reason is just to delete unused effect from the map.
    @Inject(method = "removeEffectNoUpdate", at = @At("RETURN"))
    private void onMobEffectRemoved(Holder<MobEffect> effect, CallbackInfoReturnable<MobEffectInstance> cir) {
        StatusEffectAttribute.removeStatusEffectAttribute(effect);
    }
}
