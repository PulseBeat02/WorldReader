import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldReader {

    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream("results.txt"));
        printPigstepCoordinates(readWorldData(new File("world/region")));
        printPlayerData(readPlayerData(new File("world/playerdata")));
    }

    private static Set<ChunkCoordinate> readWorldData(final File folder) throws IOException {
        Set<ChunkCoordinate> coords = new HashSet<>();
        for (File f : Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(s -> s.toString().endsWith(".mca"))
                .sorted().collect(Collectors.toList())) {
            printMCAFile(f);
            try {
                MCAFile file = MCAUtil.read(f);
                for (int chunkX = 0; chunkX <= 31; chunkX++) {
                    for (int chunkZ = 0; chunkZ <= 31; chunkZ++) {
                        Chunk c = file.getChunk(chunkX, chunkZ);
                        try {
                            for (CompoundTag tag : c.getTileEntities()) {
                                if (tag.toString().contains("minecraft:music_disc_pigstep")) {
                                    System.out.println(tag);
                                }
                            }
                        } catch (final NullPointerException ignored) {}
                    }
                }
            } catch (final EOFException ignored) {
                printEmptyMCAFile(f);
            }
        }
        return coords;
    }

    private static Set<String> readPlayerData(final File folder) throws IOException {
        Set<String> data = new HashSet<>();
        for (File f : Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(s -> s.toString().endsWith(".dat"))
                .sorted().collect(Collectors.toList())) {
            Tag<?> tag = NBTUtil.read(f).getTag();
            if (tag.toString().contains("minecraft:music_disc_pigstep")) {
                String name = f.getName();
                data.add(name.substring(0, name.length() - 4));
            }
        }
        return data;
    }

    private static void printEmptyMCAFile(final File f) {
        System.out.println("====================================");
        System.out.println("FILE NAME: " + f.getName() + " IS EMPTY! IGNORING IT");
        System.out.println("====================================");
    }

    private static void printMCAFile(final File f) {
        System.out.println("====================================");
        System.out.println("FILE NAME: " + f.getName());
        System.out.println("====================================");
    }

    private static void printPlayerData(final Set<String> sets) {
        for (String str : sets) {
            System.out.println(str);
        }
    }

    private static void printPigstepCoordinates(final Set<ChunkCoordinate> coordinates) {
        System.out.println("====================================");
        System.out.println("              RESULTS               ");
        for (ChunkCoordinate c : coordinates) {
            System.out.println("====================================");
            System.out.println("FILE: " + c.getFileName());
            System.out.println("CHUNK COORDINATE: " + "[" + c.getChunkX() + "," + c.getChunkZ() + "]");
            System.out.println("COORDINATE" + "[" + c.X() + "," + c.Y() + "," + c.Z() + "]");
        }
        System.out.println("====================================");
    }

    private static class ChunkCoordinate {

        private final String fileName;
        private final int x;
        private final int y;
        private final int z;
        private final int chunkX;
        private final int chunkZ;

        public ChunkCoordinate(final String name, final int x, final int y, final int z, final int chunkX, final int chunkZ) {
            this.fileName = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
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

        public String getFileName() {
            return fileName;
        }

        public int getChunkX() {
            return chunkX;
        }

        public int getChunkZ() {
            return chunkZ;
        }

    }

}
