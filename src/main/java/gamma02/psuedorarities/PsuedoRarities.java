package gamma02.psuedorarities;

import gamma02.psuedorarities.client.ModConfig;
import net.fabricmc.api.ModInitializer;

public class PsuedoRarities implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.getInstance().load();
    }
}
