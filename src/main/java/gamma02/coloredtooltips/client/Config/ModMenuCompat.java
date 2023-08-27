package gamma02.coloredtooltips.client.Config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create();
            builder.setTitle(MutableText.of(new TranslatableTextContent("config.colored-tooltips.name", null, TranslatableTextContent.EMPTY_ARGUMENTS)));
            builder.setSavingRunnable(() -> ModConfig.getInstance().save());
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory main = builder.getOrCreateCategory(MutableText.of(new TranslatableTextContent("config.colored-tooltips.category-main", null, TranslatableTextContent.EMPTY_ARGUMENTS)));
            main.addEntry(entryBuilder.startStrList(MutableText.of(new TranslatableTextContent("config.colored-tooltips.rarites-list", null, TranslatableTextContent.EMPTY_ARGUMENTS)), ModConfig.getInstance().getCustomRarityJsonString())
                    .setSaveConsumer((this::saveFunction))
                    .setTooltip(MutableText.of(new TranslatableTextContent("config.colored-tooltips.rarities-json-hint", null, TranslatableTextContent.EMPTY_ARGUMENTS)))
                    .build());
            main.addEntry(entryBuilder.startStrList(MutableText.of(new TranslatableTextContent("config.colored-tooltips.items-list", null, TranslatableTextContent.EMPTY_ARGUMENTS)), ModConfig.getInstance().getItemRarities())
                    .setSaveConsumer(this::saveItemFunction)
                    .setTooltip(MutableText.of(new TranslatableTextContent("config.colored-tooltips.items-json-hint-1", null, TranslatableTextContent.EMPTY_ARGUMENTS)), MutableText.of(new TranslatableTextContent("config.colored-tooltips.items-json-hint-2", null, TranslatableTextContent.EMPTY_ARGUMENTS)))
                    .build());
            main.addEntry(entryBuilder.startStrList(MutableText.of(new TranslatableTextContent("config.colored-tooltips.item-names-list", null, TranslatableTextContent.EMPTY_ARGUMENTS)), ModConfig.getInstance().getCustomItemNames())
                    .setSaveConsumer(this::saveItemNameFunction)
                    .setTooltip(MutableText.of(new TranslatableTextContent("config.colored-tooltips.item-names-json-hint-1", null, TranslatableTextContent.EMPTY_ARGUMENTS)), MutableText.of(new TranslatableTextContent("config.colored-tooltips.item-names-json-hint-2", null, TranslatableTextContent.EMPTY_ARGUMENTS)))
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
                ModConfig.getInstance().rarities.put(Registries.ITEM.get(new Identifier(j.get("id").getAsString().split(":", 2)[0], j.get("id").getAsString().split(":", 2)[1])), "color:" + j.get("color").getAsString());
            }else if(j.has("id") && j.has("rarity")) {
                ModConfig.getInstance().rarities.put(Registries.ITEM.get(new Identifier(j.get("id").getAsString().split(":", 2)[0], j.get("id").getAsString().split(":", 2)[1])), "rarity:" + j.get("rarity").getAsString());
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
