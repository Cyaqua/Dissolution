package ladysnake.dissolution.client;

import ladysnake.dissolution.CommonProxy;
import ladysnake.dissolution.client.gui.GuiManager;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    private GuiManager guiManager;

    @Override
    public void init() {
        super.init();
        this.guiManager = new GuiManager();
        this.guiManager.registerGuis();
        MinecraftForge.EVENT_BUS.register(guiManager);
    }

}
