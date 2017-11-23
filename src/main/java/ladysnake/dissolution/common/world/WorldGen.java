package ladysnake.dissolution.common.world;

import ladysnake.dissolution.common.config.DissolutionConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;


public class WorldGen implements IWorldGenerator {

    //private WorldGenLakes mercuryLakesGenerator = new WorldGenLakes(ModFluids.MERCURY.fluidBlock());
    private WorldGenLamentStones lamentStonesGenerator = new WorldGenLamentStones();

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

        int xPos = i + random.nextInt(8);
        int yPos = 256;
        int zPos = j + random.nextInt(8);

        if (random.nextInt() % DissolutionConfig.worldGen.spawnLamentStonesFreq == 0)
            lamentStonesGenerator.generate(world, random, new BlockPos(xPos, yPos, zPos));

    }

    private void generateEnd(World world, int i, int j, Random random) {
    }

}
