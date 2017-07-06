package ladysnake.dissolution.common.init;

import ladysnake.dissolution.common.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSounds {
	lost_soul_ambient,
	lost_soul_pain,
	lost_soul_death;
		
	public final SoundEvent sound;
		
	ModSounds() {
		ResourceLocation soundLocation = new ResourceLocation(Reference.MOD_ID, this.name());
		this.sound = new SoundEvent(soundLocation);
		this.sound.setRegistryName(soundLocation);
	}
}
