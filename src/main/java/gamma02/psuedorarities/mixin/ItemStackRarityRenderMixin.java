package gamma02.psuedorarities.mixin;

import gamma02.psuedorarities.client.ModConfig;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(ItemStack.class)
public abstract class ItemStackRarityRenderMixin {

    @Shadow public abstract Item getItem();

    @Redirect(method = "getTooltip", at = @At(target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", value = "INVOKE"))
    public MutableText getTooltip(MutableText instance, Formatting formatting){
        if(Formatting.WHITE.getColorValue() != null)
            return instance.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.getInstance().rarities.getOrDefault(this.getItem(), new Color(formatting.getColorValue() != null ? formatting.getColorValue() : Formatting.WHITE.getColorValue())).getRGB())));
        return instance.formatted(formatting);
    }
}
