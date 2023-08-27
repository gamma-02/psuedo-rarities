package gamma02.coloredtooltips;

import gamma02.coloredtooltips.client.Config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class ColoredTooltips implements ModInitializer {
    @Override
    public void onInitialize() {

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ModConfig.getInstance().load());

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
