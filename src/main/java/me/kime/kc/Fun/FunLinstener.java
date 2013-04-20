package me.kime.kc.Fun;

import java.util.Iterator;
import me.kime.kc.Task.ThreadTask.EntityFunTask;
import org.bukkit.Chunk;
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
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;

public class FunLinstener implements Listener {

    private Fun fun;
    private EntityFunTask entityFunTask;
    private final int BreedLimit = 8;

    public FunLinstener(Fun fun) {
        this.fun = fun;
        entityFunTask = new EntityFunTask();

        fun.getPlugin().registerTask(entityFunTask);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        int id = event.getBlock().getTypeId();
        switch (id) {
            case 144:
                event.getBlock().setTypeId(0);
                break;
            case 47:
                event.getBlock().setTypeId(0);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(340));
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
                        block.getWorld().spawn(block.getLocation(), Minecart.class);
                        event.setCancelled(true);
                    }
                    break;
                case DOWN:
                    block = event.getBlock().getRelative(BlockFace.DOWN);
                    if (isRail(block.getTypeId())) {
                        block.getWorld().spawn(block.getLocation(), Minecart.class);
                        event.setCancelled(true);
                    } else {
                        block = block.getRelative(BlockFace.DOWN);
                        if (isRail(block.getTypeId())) {
                            block.getWorld().spawn(block.getLocation(), Minecart.class);
                            event.setCancelled(true);
                        }
                    }
                    break;
                default:
                    block = event.getBlock().getRelative(dispenser.getFacing());
                    if (isRail(block.getTypeId())) {
                        block.getWorld().spawn(block.getLocation(), Minecart.class);
                        event.setCancelled(true);
                    } else {
                        block = block.getRelative(BlockFace.DOWN);
                        if (isRail(block.getTypeId())) {
                            block.getWorld().spawn(block.getLocation(), Minecart.class);
                            event.setCancelled(true);
                        }
                    }
            }

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
                entityFunTask.queue(event.getEntity());
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
                if(event.getLocation().getWorld() != fun.getPlugin().getMine().getMineWorld()){
                    event.setCancelled(true);
                }
            default:
                break;
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            Chunk chunk = event.getChunk();
            int x = chunk.getX();
            int z = chunk.getZ();
            if (((x * x) + (z * z)) >= fun.RR) {
                chunk.unload(false, false);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Iterator<Block> destory = event.blockList().iterator();
        while (destory.hasNext()) {
            Block block = destory.next();
            if (canDamage(block)) {
                destory.remove();
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
            if (b.getTypeId() == 12 || b.getTypeId() == 13) {
                b.setTypeId(0);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isSticky()) {
            if (event.getBlock().getTypeId() == 12 || event.getBlock().getTypeId() == 13) {
                event.getBlock().setTypeId(0);
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
