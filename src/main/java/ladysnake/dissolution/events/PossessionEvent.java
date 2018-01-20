package ladysnake.dissolution.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PossessionEvent extends PlayerEvent {

    private boolean possessionStart;
    private EntityLivingBase possessed;

    public PossessionEvent(EntityPlayer player, EntityLivingBase possessed, boolean possessionStart) {
        super(player);
        this.possessed = possessed;
        this.possessionStart = possessionStart;
    }

    public boolean isPossessionStart() {
        return possessionStart;
    }

    public EntityLivingBase getPossessed() {
        return possessed;
    }
}
