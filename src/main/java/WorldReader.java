import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WorldReader {

    public static void main(String[] args) throws IOException {
        printPigstepCoordinates(readWorldData(new File("world/region")));
    }

    private static Set<ChunkCoordinate> readWorldData(final File folder) throws IOException {
        Set<ChunkCoordinate> coords = new HashSet<>();
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            printMCAFile(f);
            MCAFile file = MCAUtil.read(f);
            for (int chunkX = 0; chunkX <= 31; chunkX++) {
                for (int chunkZ = 0; chunkZ <= 31; chunkZ++) {
                    Chunk c = file.getChunk(chunkX, chunkZ);
                    outer: for (int x = 0; x < 16; ++x) {
                        for (int y = 0; y < 256; ++y) {
                            for (int z = 0; z < 16; ++z) {
                                String tag;
                                try {
                                    tag = c.getBlockStateAt(x, y, z).toString();
                                } catch (final NullPointerException exception) {
                                    break outer;
                                }
                                if (tag.contains("minecraft:music_disc_pigstep")) {
                                    coords.add(new ChunkCoordinate(x, y, z));
                                }
                            }
                        }
                    }
                    logChunkCoordinate(chunkX, chunkZ);
                }
            }
        }
        return coords;
    }

    private static void printMCAFile(final File f) {
        System.out.println("====================================");
        System.out.println("FILE NAME: " + f.getName());
        System.out.println("====================================");
    }

    private static void logChunkCoordinate(final int x, final int z) {
        System.out.println("[" + x + "," + z + "]");
    }

    private static void printPigstepCoordinates(Set<ChunkCoordinate> coordinates) {
        System.out.println("====================================");
        System.out.println("               RESULTS              ");
        System.out.println("====================================");
        for (ChunkCoordinate c : coordinates) {
            System.out.println("[" + c.X() + "," + c.Y() + "," + c.Z() + "]");
        }
    }

    private static class ChunkCoordinate {

        private final int x;
        private final int y;
        private final int z;

        public ChunkCoordinate(final int x, final int y, final int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int X() {
            return x;
        }

        public int Y() {
            return y;
        }

        public int Z() {
            return z;
        }

    }

}
