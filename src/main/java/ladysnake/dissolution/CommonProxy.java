package ladysnake.dissolution;

import ladysnake.dissolution.soulstates.CapabilitySoulState;

public class CommonProxy {
    public void preInit() {
        CapabilitySoulState.register();
    }

    public void init() {

    }
}
