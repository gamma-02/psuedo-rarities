package gamma02.psuedorarities.client.Config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.checkerframework.checker.units.qual.A;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class ModConfig {
    public static final HashMap<Item, String> DEFAULT_RARITIES = new HashMap<>();
    public static final HashMap<String, String> DEFAULT_NAME_COLORS = new HashMap<>();

    public final File configFile;
    public HashMap<Item, String> rarities;
    public HashMap<String, String> nameColors;
    public HashMap<String, Color> customRarities;

    public ArrayList<String> rawStrings;

    private static ModConfig INSTANCE;


    private ModConfig(){
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve("psuedo-rarities.json").toFile();
        this.rarities = DEFAULT_RARITIES;
        this.nameColors = DEFAULT_NAME_COLORS;
        rawStrings = new ArrayList<>();
    }

    public static ModConfig getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ModConfig();
            INSTANCE.load();
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
                            customRarities.put(a.get("id").getAsString(), new Color(Integer.parseInt(a.get("color").getAsString(), 16)));
                        }
                    }

                }
                this.customRarities = customRarities;
                if(j.has("items")){
                    for(JsonElement e : j.getAsJsonArray("items")){
                        if(e instanceof JsonObject a) {
                            String[] s = a.get("id").getAsString().split(":", 2);
                            rarities.put(Registry.ITEM.get(new Identifier(s[0], s[1])), (a.has("rarity") ? "rarity:" + a.get("rarity").getAsString() : "color:" + (a.get("color").getAsString())));
                        }
                    }
                }
                if(j.has("names")){
                    for(JsonElement e : j.getAsJsonArray("names")){
                        if(e instanceof JsonObject a){
                            String s = a.get("name").getAsString();
                            nameColors.put(s, (a.has("rarity") ? "rarity:" + a.get("rarity").getAsString() : "color:" + (a.get("color").getAsString())));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void save(){
        JsonObject toSave = new JsonObject();
        JsonArray rarities = new JsonArray();
        for(String s : this.customRarities.keySet()){
            JsonObject object = new JsonObject();
            object.addProperty("id", s);
            object.addProperty("color", this.customRarities.get(s).getRGB());
            rarities.add(object);
        }
        toSave.add("rarities", rarities);
        JsonArray items = new JsonArray();
        for(Item i : this.rarities.keySet()){
            JsonObject object = new JsonObject();
            object.addProperty("id", (Registry.ITEM.getId(i)).getNamespace() + ":" + (Registry.ITEM.getId(i)).getPath());
            if (this.rarities.get(i).startsWith("color:")) {
                object.addProperty("color", this.rarities.get(i).split(":", 2)[1]);
            } else if (this.rarities.get(i).startsWith("rarity:")) {
                    object.addProperty("rarity", this.rarities.get(i).split(":", 2)[1]);
            }
            items.add(object);
        }
        toSave.add("items", items);
        JsonArray names = new JsonArray();
        for(String s : this.nameColors.keySet()){
            JsonObject object = new JsonObject();
            object.addProperty("name", s);
            object.addProperty("color", this.nameColors.get(s));
        }
        toSave.add("names", names);
        try (PrintWriter out = new PrintWriter(configFile)) {
            out.println(toSave.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Color getColor(String i){
        if(!this.nameColors.get(i).equals(""))
            if(this.nameColors.get(i).startsWith("rarity:")){
                return this.customRarities.get(this.nameColors.get(i).split(":", 2)[1]);
            }else if(this.nameColors.get(i).startsWith("color:")){
                return new Color(Integer.parseInt(this.nameColors.get(i).split(":", 2)[1], 16));
            }else{
                return Color.WHITE;
            }
        return Color.WHITE;
    }

    public Color getColor(Item i){
        if(this.rarities.get(i) != null)
            if(this.rarities.get(i).startsWith("rarity:")){
                return this.customRarities.get(this.rarities.get(i).split(":", 2)[1]);
            }else if(this.rarities.get(i).startsWith("color:")){
                return new Color(Integer.parseInt(this.rarities.get(i).split(":", 2)[1]));
            }else{
                return Color.WHITE;
            }
        return Color.WHITE;
    }



    public static <T, K> ArrayList<String> getConjoinedJsonEntries(HashMap<T, K> map, Function<T, String> getTString, Function<K, String> getKString, String TJsonID, String KJsonID){
        ArrayList<String> returnVal = new ArrayList<>();
        for(T i : map.keySet()){
            returnVal.add("{" + '"' + TJsonID + '"' + ": " + '"' + getTString.apply(i) + '"' + ", " + '"' + KJsonID + '"' + ": " + '"' + getKString.apply(map.get(i)) + '"' + '}');
        }
        return returnVal;
    }

    public static <T, K extends String> ArrayList<String> getConjoinedJsonEntries(HashMap<T, K> map, Function<T, String> getTString, Function<K, String> getKString, String TJsonID, Function<String, String> KJsonID){
        ArrayList<String> returnVal = new ArrayList<>();
        for(T i : map.keySet()){
            returnVal.add("{" + '"' + TJsonID + '"' + ": " + '"' + getTString.apply(i) + '"' + ", " + '"' + KJsonID.apply(map.get(i)) + '"' + ": " + '"' + getKString.apply(map.get(i)) + '"' + '}');
        }
        return returnVal;
    }

    public ArrayList<String> getCustomRarityJsonString(){
        return getConjoinedJsonEntries(this.customRarities, (s -> s), (color -> Integer.toString(color.getRGB())), "id", "color");
    }

    public ArrayList<String> getItemRarities(){
        return getConjoinedJsonEntries(this.rarities, (item -> Registry.ITEM.getId(item).getNamespace() + ':' + Registry.ITEM.getId(item).getPath()), (s -> s.split(":", 2)[1]), "id", (s -> s.split(":", 2)[0]));
    }

    public ArrayList<String> getCustomItemNames(){
        return getConjoinedJsonEntries(this.nameColors, (s -> s), s -> s.split(":", 2)[1], "name", (s -> s.split(":", 2)[1]));
    }



}
