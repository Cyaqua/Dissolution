package ladysnake.dissolution.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoulStateMessageHandler implements IMessageHandler<SoulStateMessage, IMessage> {

    @Override
    public IMessage onMessage(SoulStateMessage message, MessageContext ctx) {
        return null;
    }
}
