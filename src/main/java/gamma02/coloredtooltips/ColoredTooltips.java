package gamma02.coloredtooltips;

import net.fabricmc.api.ModInitializer;

public class ColoredTooltips implements ModInitializer {
    @Override
    public void onInitialize() {
    }

    public static String getConfigTemplate(){
        return """
                {
                  "rarities":
                    [{
                      "id": "example",
                      "color": "00FF00"
                    }],
                  "items":
                    [{
                      "id": "minecraft:air", "rarity": "example"
                    }],
                  "names":
                    [{
                      "name": "example", "rarity": "example"
                    }]
                }
                                
                """;

    }
}
