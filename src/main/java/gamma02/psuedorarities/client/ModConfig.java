package gamma02.psuedorarities.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class ModConfig {
    public static final HashMap<Item, Color> DEFAULT_RARITIES = new HashMap<>();

    public final File configFile;
    public HashMap<Item, Color> rarities;

    private static ModConfig INSTANCE;


    private ModConfig(){
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve("psuedo-rarities.json").toFile();
        this.rarities = DEFAULT_RARITIES;

    }

    public static ModConfig getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ModConfig();
        }
        return INSTANCE;
    }

    public void load(){
        try{
            String stringJSON = new String(Files.readAllBytes(this.configFile.toPath()));
            if(!stringJSON.equals("")){
                HashMap<String, Color> customRarities = new HashMap<>();
                JsonObject j = (JsonObject) JsonParser.parseString(stringJSON);
                if(j.has("rarities")){
                    for (JsonElement e : j.getAsJsonArray("rarities")) {
                        if(e instanceof JsonObject a){
                            customRarities.put(a.get("id").getAsString(), new Color(a.get("color").getAsInt()));
                        }
                    }

                }
                if(j.has("items")){
                    for(JsonElement e : j.getAsJsonArray("items")){
                        if(e instanceof JsonObject a) {
                            String[] s = a.get("id").getAsString().split(":", 2);
                            rarities.put(Registry.ITEM.get(new Identifier(s[0], s[1])), customRarities.get(a.get("rarity").getAsString()));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
