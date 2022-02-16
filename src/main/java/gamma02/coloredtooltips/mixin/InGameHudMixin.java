package gamma02.coloredtooltips.mixin;

import gamma02.coloredtooltips.client.Config.ModConfig;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow private ItemStack currentStack;

    @Redirect(method = "renderHeldItemTooltip", at = @At(target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", value = "INVOKE", ordinal = 0))
    public MutableText getTooltip(MutableText instance, Formatting formatting){
        if(Formatting.WHITE.getColorValue() != null) {
            if(this.currentStack.hasCustomName() && ModConfig.getInstance().nameColors.containsKey(this.currentStack.getName().getString())) {
                return instance.setStyle(Style.EMPTY.withColor(ModConfig.getInstance().getColor(this.currentStack.getName().getString()).getRGB()));
            }
            return instance.setStyle(Style.EMPTY.withColor((ModConfig.getInstance().getColor(this.currentStack.getItem())).getRGB()));
        }
        return instance.formatted(this.currentStack.getRarity().formatting);
    }
}
