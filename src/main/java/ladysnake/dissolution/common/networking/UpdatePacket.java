package ladysnake.dissolution.common.networking;

import java.util.UUID;

import ladysnake.dissolution.common.capabilities.IIncorporealHandler;
import ladysnake.dissolution.common.capabilities.IncorporealDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdatePacket implements IMessageHandler<UpdateMessage, IMessage>
{
  
	@Override
	public IMessage onMessage(final UpdateMessage message, MessageContext ctx) 
	{
		//System.out.println("message get !");
	  // just to make sure that the side is correct
	  if (ctx.side.isClient())
	  {
		  Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
			  public void run() {
				  System.out.println("a packet has been processed");
				  final IIncorporealHandler playerCorp = IncorporealDataHandler.getHandler(Minecraft.getMinecraft().player);
				  playerCorp.setIncorporeal(message.isIncorporeal, Minecraft.getMinecraft().player);
				  System.out.println("client" + playerCorp);				  
			  }
			});
	  }
	  return null;
	}
}