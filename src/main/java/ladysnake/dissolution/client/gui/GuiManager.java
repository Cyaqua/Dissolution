package ladysnake.dissolution.client.gui;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.client.events.RegisterSoulGuiEvent;
import ladysnake.dissolution.soulstates.SoulState;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private final Map<SoulState, Gui> customGuis = new HashMap<>();

    @SubscribeEvent
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {

    }

    public void registerGuis() {
        RegisterSoulGuiEvent event = new RegisterSoulGuiEvent(customGuis);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
