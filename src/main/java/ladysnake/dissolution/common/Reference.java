package ladysnake.dissolution.common;

import net.minecraft.block.ITileEntityProvider;

public class Reference {
	public static final String MOD_ID = "dissolution";
	public static final String MOD_NAME = "Dissolution";
	public static final String MCVERSION = "[1.11.2]";
	public static final String VERSION = "0.4.2.1";
	
//	public static final String GUI_FACTORY = "ladysnake.dissolution.common.inventory.TartarosGuiFactory";	
	public static final String CLIENT_PROXY_CLASS = "ladysnake.dissolution.client.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "ladysnake.dissolution.server.proxy.ServerProxy";
	
	public static enum Items {
		BASE_RESOURCE("base_resource", "itemResource"),
		DEBUG("debug_item", "itemDebug"),
		ECTOPLASM("ectoplasm", "itemEctoplasm"),
		ECTOPLASMA("ectoplasma", "itemEctoplasma"),
		EYE_DEAD("eye_of_the_undead", "itemEyeOfUndead"),
		GRAND_FAUX("grand_faux", "itemGrandFaux"),
		SCARAB_OF_ETERNITY("scarab_of_eternity", "itemScarabOfEternity"),
		SCYTHE_IRON("iron_scythe", "itemIronScythe"),
		SOULGEM("soul_gem", "itemSoulGem"),
		SOULINABOTTLE("soul_in_a_bottle", "itemSoulInABottle"),
		SEPULTURE("sepulture", "itemSepulture"),
		SEPULTUREFRAMING("sepulture_framing", "itemSepultureFraming");

		
		
		private String unlocalizedName;
		private String registryName;
		
		Items(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
	}

	public static enum Blocks {
		CRYSTALLIZER("crystallizer", "blockCrystallizer"),
		DRIED_LAVA("dried_lava", "blockDriedLava"),
		ECTOPLASM("ectoplasm_block", "blockEctoplasm"),
		ECTOPLASMA("ectoplasma_block", "blockEctoplasma"),
		SOUL_ANCHOR("soul_anchor", "blockSoulAnchor"),
		MERCURY_CANDLE("mercury_candle", "blockMercuryCandle"),
		SULFUR_CANDLE("sulfur_candle", "blockSulfurCandle"),
		SOUL_EXTRACTOR("soul_extractor", "blockSoulExtractor"),
		MERCURIUS_WAYSTONE("mercurius_waystone", "blockMercuriusWaystone"),
		SEPULTURE("sepulture", "blockSepulture"),
		RESUSCITATOR("resuscitator","blockResuscitator");

		private String unlocalizedName;
		private String registryName;
		
		Blocks(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return this.registryName;
		}
		
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}
	}
}
