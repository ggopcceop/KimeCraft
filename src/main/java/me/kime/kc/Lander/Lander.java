package me.kime.kc.Lander;

import java.sql.SQLException;
import java.util.HashMap;

import me.kime.kc.KimeCraft;

public class Lander {

    private KimeCraft plugin;
    private DataSource dataSource;
    private HashMap<Integer, HashMap<Integer, KChunk>> chunks;
    private HashMap<Integer, KLand> ownedLandById;
    private HashMap<String, KLand> ownedLandByName;

    public Lander(KimeCraft instance) {
        this.plugin = instance;

        try {
            dataSource = new DataSource();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        chunks = new HashMap<Integer, HashMap<Integer, KChunk>>();
        ownedLandById = new HashMap<Integer, KLand>();
        ownedLandByName = new HashMap<String, KLand>();

        //login command executor
        plugin.getCommand("land").setExecutor(new LanderCommand(this));

        plugin.getPluginManager().registerEvents(new LanderLinstener(this), plugin);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public KimeCraft getPlugin() {
        return plugin;
    }

    public KLand getLand(int id) {
        return ownedLandById.get(id);
    }

    public KLand getLand(int x, int z) {
        KLand kLand = null;
        KChunk kChunk = getKChunk(x, z);
        if (kChunk != null) {
            if (kChunk.getId() != -1) {
                kLand = ownedLandById.get(kChunk.getId());
            }
        }
        return kLand;
    }

    public KLand getLand(String name) {
        return ownedLandByName.get(name.toLowerCase());
    }

    public KChunk getKChunk(int x, int z) {
        KChunk kChunk = null;
        HashMap<Integer, KChunk> zs = chunks.get(x);
        if (zs != null) {
            kChunk = zs.get(z);
        }
        return kChunk;
    }

    public void loadChunk(int x, int z, KChunk kChunk) {
        if (kChunk != null) {
            HashMap<Integer, KChunk> zs = chunks.get(x);
            if (zs == null) {
                zs = new HashMap<Integer, KChunk>();
                chunks.put(x, zs);
            }
            zs.remove(z);
            zs.put(z, kChunk);
        }
    }

    public void loadLand(int id, String owner, String name, int x, int z) {
        KLand kLand = null;
        kLand = ownedLandById.get(id);
        if (kLand == null) {
            kLand = new KLand(id, owner, name);
            ownedLandById.put(id, kLand);
        }
        kLand.addChunk(getKChunk(x, z));

        KLand kl = ownedLandByName.get(name.toLowerCase());
        if (kl == null) {
            ownedLandByName.put(name.toLowerCase(), kLand);
        }
    }

    public void addChunkToLand(int x, int z, int id) {
        dataSource.addChunk(x, z, id);

        KChunk kChunk = getKChunk(x, z);
        if (kChunk == null) {
            kChunk = new KChunk(plugin.getDefaultWorld().getChunkAt(x, z));
        }
        kChunk.setId(id);

        loadChunk(x, z, kChunk);

        getLand(id).addChunk(kChunk);
    }

    public int addLand(int x, int z, String owner, String name) {
        int id = -1;
        if (ownedLandByName.containsKey(name.toLowerCase())) {
            id = ownedLandByName.get(name.toLowerCase()).getId();
        } else {
            id = dataSource.addLand(owner, name);
        }

        loadLand(id, owner, name, x, z);

        addChunkToLand(x, z, id);
        return id;
    }

    public KLand getKChunkByOwner(String owner) {
        return ownedLandByName.get(owner.toLowerCase() + "'s land");
    }

    public void deleteChunk(int x, int z) {
        HashMap<Integer, KChunk> zs = chunks.get(x);
        if (zs == null) {
            zs = new HashMap<Integer, KChunk>();
            chunks.put(x, zs);
        }
        zs.remove(z);
    }

    public void deleteLand(int id) {
        KLand kLand = ownedLandById.remove(id);
        ownedLandByName.remove(kLand.getName().toLowerCase());
    }

    public void deleteLand(String name) {
        KLand kLand = ownedLandByName.remove(name.toLowerCase());
        ownedLandById.remove(kLand.getId());
    }
}
