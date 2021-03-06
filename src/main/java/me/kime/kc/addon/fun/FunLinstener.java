/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc.addon.fun;

import java.util.Iterator;
import me.kime.kc.KPlayer;
import me.kime.kc.addon.mine.Mine;
import me.kime.kc.task.BorderCheckTask;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;

public class FunLinstener implements Listener {

    private final Fun fun;
    private final int BreedLimit = 8;

    public FunLinstener(Fun fun) {
        this.fun = fun;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        KPlayer kPlayer = fun.getPlugin().getOnlinePlayer(player.getName());
        //add border check task
        int borderCheckTaskId = fun.getPlugin().getServer().getScheduler()
                .scheduleSyncRepeatingTask(fun.getPlugin(), new BorderCheckTask(kPlayer, fun.sRR), 100L, 100L);
        kPlayer.setBorderCheckTaskId(borderCheckTaskId);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        KPlayer kPlayer = fun.getPlugin().getOnlinePlayer(player.getName());
        //cancel border check task
        int id = kPlayer.getBorderCheckTaskId();
        fun.getPlugin().getServer().getScheduler().cancelTask(id);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Material id = event.getBlock().getType();
        switch (id) {
            case SKULL:
                event.getBlock().setType(Material.AIR);
                break;
            case BOOKSHELF:
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BOOK));
                break;
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        int id = event.getItem().getTypeId();
        if (id == 328) {
            Dispenser dispenser = (Dispenser) event.getBlock().getState().getData();

            switch (dispenser.getFacing()) {
                case UP:
                    Block block = event.getBlock().getRelative(BlockFace.UP, 2);
                    if (isRail(block.getTypeId())) {
                        spawnMinecart(block, block.getLocation());
                        event.setCancelled(true);
                    }
                    break;
                default:
                    block = event.getBlock().getRelative(dispenser.getFacing());
                    for (int i = 0; i < 2; i++) {
                        if (isRail(block.getTypeId())) {
                            spawnMinecart(block, block.getLocation());
                            event.setCancelled(true);
                            break;
                        }
                        block = block.getRelative(BlockFace.DOWN);
                    }
            }
        }
    }

    private void spawnMinecart(Block rail, Location loc) {
        if (rail.getData() <= 0x1) {
            rail.getWorld().spawn(loc.add(0.5, 0, 0.5), Minecart.class);
        } else {
            rail.getWorld().spawn(loc.add(0.5, 0.5, 0.5), Minecart.class);
        }
    }

    private boolean isRail(int id) {
        switch (id) {
            case 27:
            case 28:
            case 66:
            case 157:
                return true;
            default:
                return false;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        if (event.getVehicle() instanceof RideableMinecart) {
            if (event.getBlock().getTypeId() == 23) {
                event.getVehicle().eject();
                event.getVehicle().remove();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        if (event.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) event.getEntity();
            sheep.setSheared(true);
            event.setCancelled(true);
            int num = fun.getPlugin().getRandom().nextInt(3) + 2;
            sheep.getWorld().dropItemNaturally(sheep.getLocation(),
                    new ItemStack(Material.WOOL, num, sheep.getColor().getWoolData()));
            ItemStack item = event.getPlayer().getItemInHand();
            item.setDurability((short) (item.getDurability() + 1));
        }
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        switch (block.getTypeId()) {
            case 55:
            case 75:
            case 76:
            case 93:
            case 94:
                Location loc = block.getLocation();
                if (fun.getRedstone().containsKey(loc)) {
                    RedstoneC red = fun.getRedstone().get(loc);
                    if (event.getNewCurrent() == 0) {
                        red.add();
                    }
                    if (red.isOver(System.currentTimeMillis())) {
                        block.setTypeId(0);
                        loc.getWorld().createExplosion(loc, 3F, true);
                        fun.getRedstone().remove(loc);
                    }
                } else {
                    RedstoneC red = new RedstoneC();
                    red.setDate(System.currentTimeMillis());
                    fun.getRedstone().put(loc, red);
                }
                return;
            case 28:
                if (block.getRelative(BlockFace.DOWN).getTypeId() == 49) {
                    boolean isOn = false;
                    for (Entity e : event.getBlock().getChunk().getEntities()) {
                        if (!(e instanceof Player)) {
                            continue;
                        }
                        if (!((Player) e).isInsideVehicle()) {
                            continue;
                        }
                        if (e.getLocation().toVector().distance(event.getBlock().getLocation().toVector()) <= 1) {
                            isOn = true;
                            break;
                        }
                    }
                    if (!isOn) {
                        event.setNewCurrent(0);
                    }
                }
                break;
            case 87:
                if (event.getNewCurrent() == 0) {
                    if (block.getRelative(BlockFace.UP).getTypeId() == 51) {
                        block.getRelative(BlockFace.UP).setType(Material.AIR);
                    }
                } else {
                    if (block.getRelative(BlockFace.UP).getTypeId() == 0) {
                        block.getRelative(BlockFace.UP).setType(Material.FIRE);
                    }
                }
                break;
            case 86:
                if (event.getNewCurrent() > 0) {
                    block.setTypeId(91);
                }
                break;
            case 91:
                if (event.getNewCurrent() == 0) {
                    block.setTypeId(86);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Chunk chunk;
        int count;

        switch (event.getSpawnReason()) {
            case CHUNK_GEN:
                event.setCancelled(true);
                return;
            case BREEDING:
            case EGG:
                chunk = event.getLocation().getChunk();
                count = getChunksEntityNum(event.getLocation().getWorld(), chunk.getX(), chunk.getZ(), true);

                if (count > BreedLimit) {
                    event.setCancelled(true);
                    return;
                }
                break;
            case SPAWNER:
                if (event.getLocation().add(0, -1, 0).getBlock().isLiquid()) {
                    event.setCancelled(true);
                    event.getLocation().getWorld().createExplosion(event.getLocation(), 6F, true);
                    return;
                } else if (event.getLocation().add(0, -1, 0).getBlock().isEmpty()) {
                    event.setCancelled(true);
                    return;
                }
        }

        switch (event.getEntityType()) {
            case SHEEP:
                ((Sheep) event.getEntity()).setColor(DyeColor.values()[fun.getPlugin().getRandom().nextInt(DyeColor.values().length)]);
                break;
            case ZOMBIE:
                if (event.getEntity().getEntityId() % 100 == 13) {
                    event.setCancelled(true);
                    Location loc = event.getLocation();
                    loc.getWorld().spawnEntity(loc, EntityType.GIANT);
                }
                break;
            case SPIDER:
                if (event.getEntity().getEntityId() % 100 == 40) {
                    Entity skeleton = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.SKELETON);
                    event.getEntity().setPassenger(skeleton);
                }
                break;
            case WOLF:
                if (event.getEntity().getEntityId() % 200 != 140) {
                    Wolf wolf = (Wolf) event.getEntity();
                    wolf.setAngry(true);
                }
                break;
            case VILLAGER:
                chunk = event.getLocation().getChunk();
                count = getChunksEntityNum(event.getLocation().getWorld(), chunk.getX(), chunk.getZ(), true);

                if (count > 6) {
                    event.setCancelled(true);
                }
                break;
            case WITHER:
                World mineWorld = ((Mine) fun.getPlugin().getAddon("Mine")).getMineWorld();
                if (event.getLocation().getWorld() != mineWorld) {
                    event.setCancelled(true);
                }
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getLocation().getWorld() == fun.getPlugin().getDefaultWorld()) {
            Iterator<Block> destory = event.blockList().iterator();
            while (destory.hasNext()) {
                Block block = destory.next();
                if (canDamage(block)) {
                    destory.remove();
                }
            }
        }
    }

    private boolean canDamage(Block block) {
        switch (block.getTypeId()) {
            case 1:
            case 3:
            case 13:
            case 97:
            case 55:
            case 46:
            case 11:
            case 10:
            case 75:
            case 76:
            case 93:
            case 94:
            case 149:
            case 150:
            case 356:
                return false;
            case 52:
                block.setType(Material.AIR);
                return true;
            default:
                return true;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            EntityDamageEvent lastEvent = event.getEntity().getLastDamageCause();
            if (lastEvent == null) {
                return;
            }
            switch (lastEvent.getCause()) {
                case ENTITY_ATTACK:
                case PROJECTILE:
                    break;
                default:
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                    break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            if (event.getCause() == DamageCause.FALL) {
                event.setDamage(0);
            }
        }
    }

    private int getChunksEntityNum(World world, int x, int z, boolean isAnimal) {
        int count = 0;

        if ((x * x) + (z * z) > fun.sRR) {
            return 1000;
        }

        count += getEntityNumByChunk(world, x - 1, z - 1, isAnimal);
        count += getEntityNumByChunk(world, x - 1, z, isAnimal);
        count += getEntityNumByChunk(world, x - 1, z + 1, isAnimal);
        count += getEntityNumByChunk(world, x, z - 1, isAnimal);
        count += getEntityNumByChunk(world, x, z, isAnimal);
        count += getEntityNumByChunk(world, x, z + 1, isAnimal);
        count += getEntityNumByChunk(world, x + 1, z - 1, isAnimal);
        count += getEntityNumByChunk(world, x + 1, z, isAnimal);
        count += getEntityNumByChunk(world, x + 1, z + 1, isAnimal);

        return count;
    }

    private int getEntityNumByChunk(World world, int x, int z, boolean isAnimal) {
        Chunk chunk = world.getChunkAt(x, z);
        if (chunk != null) {
            if (isAnimal) {
                int count = 0;
                for (Entity e : chunk.getEntities()) {
                    if (e instanceof Animals) {
                        count++;
                    }
                }
                return count;
            } else {
                return chunk.getEntities().length;
            }
        }
        return 0;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block b : event.getBlocks()) {
            if (b.getType() == Material.SAND || b.getType() == Material.GRAVEL) {
                b.setType(Material.AIR);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isSticky()) {
            if (event.getBlock().getType() == Material.SAND || event.getBlock().getType() == Material.GRAVEL) {
                event.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (event.getVehicle() instanceof Boat) {
            Boat boat = (Boat) event.getVehicle();
            boat.setMaxSpeed(0.8D);
        }
    }
}
