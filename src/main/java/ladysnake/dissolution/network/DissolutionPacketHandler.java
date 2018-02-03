package ladysnake.dissolution.network;

import ladysnake.dissolution.Dissolution;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class DissolutionPacketHandler {

    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(Dissolution.MODID);
    private static int id;

    public static void registerPackets() {
        NET.registerMessage(SoulStateMessageHandler.class, SoulStateMessage.class, id++, Side.CLIENT);
    }
}
