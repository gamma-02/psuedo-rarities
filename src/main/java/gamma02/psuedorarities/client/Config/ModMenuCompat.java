package gamma02.psuedorarities.client.Config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.util.mod.Mod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create();
            builder.setTitle(new TranslatableText("config.psuedo-rarities.name"));
            builder.setSavingRunnable(() -> ModConfig.getInstance().save());
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory main = builder.getOrCreateCategory(new TranslatableText("config.psuedo-rarities.category-main"));
            main.addEntry(entryBuilder.startStrList(new TranslatableText("config.psuedo-rarities.rarites-list"), ModConfig.getInstance().getCustomRarityJsonString())
                    .setSaveConsumer((this::saveFunction))
                    .setTooltip(new TranslatableText("config.psuedo-rarities.rarities-json-hint"))
                    .build());
            main.addEntry(entryBuilder.startStrList(new TranslatableText("config.psuedo-rarities.items-list"), ModConfig.getInstance().getItemRarities())
                    .setSaveConsumer(this::saveItemFunction)
                    .setTooltip(new TranslatableText("config.psuedo-rarities.items-json-hint"))
                    .build());
            main.addEntry(entryBuilder.startStrList(new TranslatableText("config.psuedo-rarities.item-names-list"), ModConfig.getInstance().getCustomItemNames())
                    .setSaveConsumer(this::saveItemNameFunction)
                    .setTooltip(new TranslatableText("config.psuedo-rarities.item-names-json-hint"))
                    .build());

            return builder.build();
        };
    }

    public void saveFunction(List<String> s){
        for (String e : s) {
            JsonObject j = (JsonObject) JsonParser.parseString(e);
            if(j.has("id") && j.has("color"))
                ModConfig.getInstance().customRarities.put(j.get("id").getAsString(), new Color(Integer.parseInt(j.get("color").getAsString(), 16)));
        }
    }

    public void saveItemFunction(List<String> s){
        for (String e : s) {
            JsonObject j = (JsonObject) JsonParser.parseString(e);
            if(j.has("id") && j.has("color")) {
                ModConfig.getInstance().rarities.put(Registry.ITEM.get(new Identifier(j.get("id").getAsString().split(":", 2)[0], j.get("id").getAsString().split(":", 2)[1])), "color:" + j.get("color").getAsString());
            }else if(j.has("id") && j.has("rarity")) {
                ModConfig.getInstance().rarities.put(Registry.ITEM.get(new Identifier(j.get("id").getAsString().split(":", 2)[0], j.get("id").getAsString().split(":", 2)[1])), "rarity:" + j.get("rarity").getAsString());
            }
        }
    }

    public void saveItemNameFunction(List<String> s){
        for(String e : s){
            JsonObject j = (JsonObject) JsonParser.parseString(e);
            if(j.has("name") && j.has("color")) {
                ModConfig.getInstance().nameColors.put(j.get("name").getAsString(), "color:" + j.get("color").getAsString());
            }else if(j.has("name") && j.has("rarity")){
                ModConfig.getInstance().nameColors.put(j.get("name").getAsString(), "rarity:" + j.get("rarity").getAsString());
            }
        }
    }
}
