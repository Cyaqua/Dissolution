package ladysnake.dissolution.client.events;

import ladysnake.dissolution.soulstates.SoulState;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;

public class RegisterSoulGuiEvent extends Event {

    private final Map<SoulState, Gui> customGUIs;

    public RegisterSoulGuiEvent(Map<SoulState, Gui> customGUIs) {
        this.customGUIs = customGUIs;
    }

    public void registerGui(SoulState state, Gui gui) {
        this.customGUIs.put(state, gui);
    }
}
