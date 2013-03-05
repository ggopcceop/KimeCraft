package me.Kime.KC.Lander;

import me.Kime.KC.Task.ThreadTask.LanderChunkLoadTask;
import me.Kime.KC.Util.KCMessager;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * stop player action when non-auth
 *
 * @author Kime
 *
 */
public class LanderLinstener implements Listener {

    private Lander lander;
    private LanderChunkLoadTask task;

    public LanderLinstener(Lander instance) {
        this.lander = instance;

        task = new LanderChunkLoadTask(lander);
        lander.getPlugin().registerTask(task);

        World world = lander.getPlugin().getDefaultWorld();

        Chunk[] chunks = world.getLoadedChunks();
        for (int j = 0; j < chunks.length; j++) {
            task.queue(chunks[j]);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Chunk chunk = event.getBlock().getLocation().getChunk();
        KChunk kChunk = lander.getKChunk(chunk.getX(), chunk.getZ());
        if (kChunk == null) {
            return;
        }
        if (event.getPlayer().getName().equalsIgnoreCase(kChunk.getOwner())) {
            KCMessager.sentMessage(event.getPlayer(), "Your block", ChatColor.GREEN);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if ("world".equalsIgnoreCase(event.getWorld().getName())) {
            task.queue(event.getChunk());
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if ("world".equalsIgnoreCase(event.getWorld().getName())) {
            Chunk[] chunks = event.getWorld().getLoadedChunks();
            for (int i = 0; i < chunks.length; i++) {
                task.queue(chunks[i]);
            }
        }

    }
}
