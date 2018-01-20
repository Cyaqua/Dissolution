package ladysnake.dissolution.util;

import net.minecraft.util.EnumActionResult;
import net.minecraftforge.fml.common.eventhandler.Event;

@FunctionalInterface
public interface IEventCallback<T extends Event> {

    /**
     * @return
     *          {@link EnumActionResult#SUCCESS} if the event has been correctly handled and no further processing is expected of the caller
     *          {@link EnumActionResult#FAIL} if the event was not handled but no other callbacks should attempt to process it
     *          {@link EnumActionResult#PASS} if the event was not handled and other callbacks should attempt to process it
     */
    EnumActionResult call(T event);

}
