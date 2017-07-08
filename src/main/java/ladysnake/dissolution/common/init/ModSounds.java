package ladysnake.dissolution.common.init;

import ladysnake.dissolution.common.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum ModSounds {
	lost_soul_ambient,
	lost_soul_pain,
	lost_soul_death;
		
	public final SoundEvent sound;

	static final RegisterManager REGISTRY_MANAGER = new RegisterManager();
		
	ModSounds() {
		ResourceLocation soundLocation = new ResourceLocation(Reference.MOD_ID, this.name());
		this.sound = new SoundEvent(soundLocation);
		this.sound.setRegistryName(soundLocation);
	}

	public static final class RegisterManager {

	    public void onRegister() {
			for(ModSounds s : ModSounds.values())
				GameRegistry.register(s.sound);
		}
		
		private RegisterManager() {}

	}
}
