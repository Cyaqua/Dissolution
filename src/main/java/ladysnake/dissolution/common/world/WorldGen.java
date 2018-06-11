package ladysnake.dissolution.common.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        switch (world.provider.getDimension()) {
            case -1:
                generateNether(world, chunkX * 16, chunkZ * 16, random);
                break;

            case 0:
                generateOverWorld(world, chunkX * 16, chunkZ * 16, random);
                break;

            case 1:
                generateEnd(world, chunkX * 16, chunkZ * 16, random);
                break;
        }

    }


    private void generateNether(World world, int i, int j, Random random) {
    }

    private void generateOverWorld(World world, int i, int j, Random random) {

        int Xpos = i + random.nextInt(8);
        int Ypos = 256;
        int Zpos = j + random.nextInt(8);

//        if (random.nextInt() % Dissolution.config.worldGen.spawnLamentStonesFreq == 0)
//            lamentStonesGenerator.generate(world, random, new BlockPos(Xpos, Ypos, Zpos));

    }

    private void generateEnd(World world, int i, int j, Random random) {
    }

}
