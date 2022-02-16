package gamma02.psuedorarities.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
import net.minecraft.util.registry.DynamicRegistryManager;

@Environment(EnvType.CLIENT)
public class PsuedoRaritiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

    }


}
