package gamma02.psuedorarities.mixin;

import gamma02.psuedorarities.client.Config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(ItemStack.class)
public abstract class ItemStackRarityRenderMixin {

    @Shadow public abstract Item getItem();

    @Shadow public abstract boolean hasCustomName();

    @Shadow public abstract Text getName();

    @Shadow public abstract Rarity getRarity();

    @Redirect(method = "getTooltip", at = @At(target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", value = "INVOKE"))
    public MutableText getTooltip(MutableText instance, Formatting formatting){
        if(Formatting.WHITE.getColorValue() != null) {
            if(this.hasCustomName() && ModConfig.getInstance().nameColors.containsKey(this.getName().getString())) {
                return instance.setStyle(Style.EMPTY.withColor(ModConfig.getInstance().getColor(this.getName().getString()).getRGB()));
            }
            return instance.setStyle(Style.EMPTY.withColor((ModConfig.getInstance().getColor(this.getItem())).getRGB()));
        }
        return instance.formatted(this.getRarity().formatting);
    }
}
