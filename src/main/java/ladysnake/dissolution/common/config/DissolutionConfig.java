package ladysnake.dissolution.common.config;

import ladysnake.dissolution.api.corporeality.ICorporealityStatus;
import ladysnake.dissolution.common.registries.CorporealityStatus;
import ladysnake.dissolution.common.registries.SoulCorporealityStatus;
import net.minecraftforge.common.config.Config;

public class DissolutionConfig {

    public Client client = new Client();

    public Entities entities = new Entities();

    public Respawn respawn = new Respawn();

    public Ghost ghost = new Ghost();

    public WorldGen worldGen = new WorldGen();

    @Config.RequiresWorldRestart
    @Config.Comment("If set to anything other than \"none\", will force a soul strength upon players and prevent the dialogue from appearing")
    public DissolutionConfigManager.EnforcedSoulStrength enforcedSoulStrength = DissolutionConfigManager.EnforcedSoulStrength.NONE;

    @Config.Comment("Because some people need dialogue to be explicit")
    public boolean technicianDialogue = false;

    public class Blocks {

//		@Config.LangKey("config.dissolution.blocks.doSablePop")
//		@Config.Comment("Whether machines should output items in world when there is no appropriate container available")
//		public boolean doSablePop = true;

    }

    public class Client {

        @Config.Comment("Whether this mod should use shaders as an attempt to make things prettier")
        public boolean useShaders = true;

        @Config.Comment("If set to true, a hud element displaying relevant locations will appear when incorporeal")
        public boolean soulCompass = true;

        @Config.Comment("The maximum distance at which lament stones will render on the soul compass. Set it <= 0 to disable")
        public int lamentStonesCompassDistance = 100;

    }

    public class Dialogues {
        //		@Config.LangKey("config.dissolution.dialogues.broadcastPlayerDialogue")
//		@Config.Comment("If set to true, every dialogue choice made by the player will be broadcasted to all other players")
//		public boolean broadcastPlayerDialogue = false;
//
//		@Config.LangKey("config.dissolution.dialogues.broadcastMajorNPCDialogue")
//		@Config.Comment("If set to true, dialogues emitted by global entities (gods) will be broadcasted to all players")
//		public boolean broadcastMajorNPCDialogue = false;
    }

    public class Entities {

        @Config.Comment("If set to false, Eye of the Undead's minions won't do kamikaze attacks on creepers")
        public boolean minionsAttackCreepers = true;

        @Config.Comment("If set to true, only the corpse's owner and admins will be able to interact with it")
        public boolean lockPlayerCorpses = false;

        @Config.Comment("If set to false, player bodies will not require any special circumstances to prevent decay")
        public boolean bodiesDespawn = true;

    }

    public class Ghost {

        @Sync
        @Config.Comment("Changes the way players fly as souls")
        public DissolutionConfigManager.FlightModes flightMode = DissolutionConfigManager.FlightModes.CUSTOM_FLIGHT;

        @Config.Comment("If set to true, dead players will be fully invisible")
        public boolean invisibleGhosts = false;

        @Sync
        @Config.RangeDouble(min = 0D, max = 1D)
        @Config.Comment("Any blocks having an average edge length below that value will let souls pass through")
        public double maxThickness = 0.9;

        @Config.Comment("A list of block IDs that ectoplasm can interact with. " +
                "If the name begins and ends with a \'/\', it will be interpreted as a regular expression.")
        public String[] authorizedBlocks = new String[]{"minecraft:lever", "/minecraft:.*door/",
                "minecraft:wooden_button"};

        @Config.Comment("A list of entity names that can be hurt by incorporeal players and that can attack ectoplasms. " +
                "If the name begins and ends with a \'/\', it will be interpreted as a regular expression.")
        public String[] authorizedEntities = new String[]{};

        @Config.Comment("If set to false, incorporeal players won't be able to use the /dissolution stuck command to get back to their spawnpoint")
        public boolean allowStuckCommand = true;

    }

    public class Respawn {

        @Config.Comment("If set to true, a player corpse will be created each time a player dies")
        public boolean spawnCorpses = false;

        @Config.Comment("If set to true, the player will respawn as a ghost at their spawnpoint. They will then have the choice to go to 0,0 to respawn without stuff or to reach their corpse under 5 minutes.")
        public boolean wowLikeRespawn = false;

        @Config.Comment("Whether player corpses hold their owner's inventory upon death")
        public boolean bodiesHoldInventory = true;

        @Config.Comment("Whether players should respawn in a specific dimension when they die")
        public boolean respawnInNether = false;

        @Config.Comment("If dimension respawn is on, the player will always respawn in this dimension")
        public int respawnDimension = -1;

        @Config.Comment("Controls players with strong souls' corporeal state when they respawn")
        public EnumCorporealityStatus respawnCorporealityStatus = EnumCorporealityStatus.SOUL;

        @Config.Comment("Whether players should respawn instantly as souls without showing death screen (could mess with other mods)")
        public boolean skipDeathScreen = false;

    }

    public class WorldGen {

        @Config.Comment("A Lament Stone has a 1 in N chances to spawn in a given chunk (the higher the number here, the less stones). -1 to disable.")
        public int spawnLamentStonesFreq = 50;

    }

    public class Wip {

    }

}
