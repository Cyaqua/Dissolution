package ladysnake.dissolution;

import ladysnake.dissolution.commands.CommandDissolutionTree;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Dissolution.MODID,
        name = Dissolution.NAME,
        version = Dissolution.VERSION,
        acceptedMinecraftVersions = Dissolution.MCVERSION,
        dependencies = Dissolution.DEPENDENCIES
)
public class Dissolution {
    public static final String MODID = "dissolution";
    public static final String NAME = "Dissolution";
    public static final String VERSION = "@VERSION@";
    public static final String MCVERSION = "[1.12]";
    public static final String DEPENDENCIES = "after:albedo;after:baubles;";

    @SidedProxy(clientSide = "ladysnake.dissolution.client.ClientProxy", serverSide = "ladysnake.dissolution.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDissolutionTree());
    }
}
