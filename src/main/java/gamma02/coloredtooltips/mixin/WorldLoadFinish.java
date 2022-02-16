package gamma02.coloredtooltips.mixin;

import gamma02.coloredtooltips.client.Config.ModConfig;
import net.minecraft.util.profiling.jfr.Finishable;
import net.minecraft.util.profiling.jfr.JfrProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JfrProfiler.class)
public class WorldLoadFinish {
    @Inject(method = "startWorldLoadProfiling", at = @At("HEAD"), cancellable = true)
    public void mixin(CallbackInfoReturnable<Finishable> cir){
        ModConfig.getInstance().load();
    }
}
