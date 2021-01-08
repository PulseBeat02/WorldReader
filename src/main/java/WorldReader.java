import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WorldReader {

    public static void main(String[] args) throws IOException {
        readWorldData(new File("world"));
    }

    private static Set<ChunkCoordinate> readWorldData(final File folder) throws IOException {
        Set<ChunkCoordinate> coords = new HashSet<>();
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            MCAFile file = MCAUtil.read(f);
            for (int i = 0; i <= 31; i++) {
                Chunk c = file.getChunk(i);
                for (int x = 0; x <= 16; x++) {
                    for (int y = 0; y <= 256; y++) {
                        for (int z = 0; z <= 16; z++) {
                            CompoundTag tag = c.getBlockStateAt(x, y, z);
                            if (tag.containsKey("id")) {
                                if (tag.getInt("id") == 54) {
                                    for (Tag<?> item : tag.getListTag("Items")) {
                                        if (item.toString().contains("music_disc_pigstep")) {
                                            coords.add(new ChunkCoordinate(x, y, z));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return coords;
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

    }

}
