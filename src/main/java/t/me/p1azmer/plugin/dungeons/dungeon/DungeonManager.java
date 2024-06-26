package t.me.p1azmer.plugin.dungeons.dungeon;

import t.me.plazmer.engine.shaded.energie.model.SchedulerType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.manager.AbstractManager;
import t.me.p1azmer.engine.utils.StringUtil;
import t.me.p1azmer.plugin.dungeons.DungeonPlugin;
import t.me.p1azmer.plugin.dungeons.Keys;
import t.me.p1azmer.plugin.dungeons.config.Config;
import t.me.p1azmer.plugin.dungeons.dungeon.chest.DungeonChestBlock;
import t.me.p1azmer.plugin.dungeons.dungeon.impl.Dungeon;
import t.me.p1azmer.plugin.dungeons.dungeon.listener.DungeonListener;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.impl.ChestModule;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.impl.SpawnModule;
import t.me.p1azmer.plugin.dungeons.dungeon.stage.DungeonStage;
import t.me.p1azmer.plugin.dungeons.generator.config.GeneratorConfig;
import t.me.p1azmer.plugin.dungeons.integration.region.RegionHandlerWG;
import t.me.p1azmer.plugin.dungeons.task.DungeonTickTask;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonManager extends AbstractManager<DungeonPlugin> {
    private Map<String, Dungeon> dungeonMap;
    private DungeonTickTask dungeonTickTask;

    public DungeonManager(@NotNull DungeonPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.dungeonMap = new HashMap<>();
        this.plugin.getConfig().initializeOptions(GeneratorConfig.class);
        this.plugin.getConfigManager().extractResources(Config.DIR_DUNGEONS);

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_DUNGEONS, true)) {
            Dungeon dungeon = new Dungeon(this, cfg);
            if (dungeon.load()) {
                this.dungeonMap.put(dungeon.getId(), dungeon);
                if (plugin.getRegionHandler() != null && plugin.getRegionHandler().getClass().equals(RegionHandlerWG.class)) {
                    if (dungeon.getSchematicSettings().isUnderground() && dungeon.getDungeonRegion().isEnabled() && !dungeon.getDungeonRegion().getFlags().contains("build")) {
                        plugin.error("Please note that the dungeon '" + dungeon.getId() + "' is set to be underground, but its region does not have building rights!");
                    }
                }
                // setup managers
                dungeon.getModuleManager().setup();
            } else this.plugin.error("Dungeon not loaded: '" + cfg.getFile().getName() + "'.");
        }
        this.plugin.info("Loaded " + this.getDungeonMap().size() + " dungeons.");

        this.addListener(new DungeonListener(this));

        this.dungeonTickTask = new DungeonTickTask(this);
        this.dungeonTickTask.start();
    }

    @Override
    protected void onShutdown() {
        if (this.dungeonMap != null) {
            this.dungeonMap.values().forEach(dungeon -> {
                dungeon.clear();
                if (dungeon.getModuleManager() != null) {
                    dungeon.getModuleManager().shutdown();
                    dungeon.setModuleManager(null);
                }
            });
            this.dungeonMap = null;
        }
        if (this.dungeonTickTask != null) {
            this.dungeonTickTask.stop();
            this.dungeonTickTask = null;
        }
    }

    public boolean create(@NotNull String id) {
        id = StringUtil.lowerCaseUnderscore(id);
        if (this.getDungeonById(id) != null) {
            return false;
        }

        JYML cfg = new JYML(this.plugin.getDataFolder() + Config.DIR_DUNGEONS, id + ".yml");
        Dungeon dungeon = new Dungeon(this, cfg);
        dungeon.setName("&a&l" + StringUtil.capitalizeUnderscored(dungeon.getId()) + " Dungeon");
        dungeon.setWorld(plugin.getServer().getWorlds().stream().filter(f -> f.getEnvironment().equals(World.Environment.NORMAL)).findFirst().orElseThrow());
        dungeon.save();
        dungeon.load();

        this.getDungeonMap().put(dungeon.getId(), dungeon);
        return true;
    }

    public boolean delete(@NotNull Dungeon dungeon) {
        if (dungeon.getFile().delete()) {
            dungeon.clear();
            this.getDungeonMap().remove(dungeon.getId());
            return true;
        }
        return false;
    }

    @NotNull
    public List<String> getDungeonIds(boolean keyOnly) {
        return this.getDungeons().stream().filter(crate -> !crate.getKeyIds().isEmpty() || !keyOnly).map(Dungeon::getId).toList();
    }

    @NotNull
    public Map<String, Dungeon> getDungeonMap() {
        return this.dungeonMap;
    }

    @NotNull
    public Collection<Dungeon> getDungeons() {
        return this.getDungeonMap().values();
    }

    @Nullable
    public Dungeon getDungeonById(@NotNull String id) {
        return this.getDungeonMap().get(id.toLowerCase());
    }

    @Nullable
    public Dungeon getDungeonByBlock(@NotNull Block block) {
        return this.getDungeonByLocation(block.getLocation(), block);
    }

    @Nullable
    public Dungeon getDungeonByLocation(@NotNull Location location, @NotNull Block block) {
        return this.getDungeons().stream().filter(dungeon -> {
            ChestModule module = dungeon.getModuleManager().getModule(ChestModule.class).orElse(null);
            Block dungeonBlock = module != null ? module.getBlock(location) : null;

            return (dungeon.getDungeonCuboid() != null && dungeon.getDungeonCuboid().contains(location))
                    || (dungeonBlock != null
                    && (dungeonBlock.hasMetadata(dungeon.getId())
                    || dungeonBlock.equals(block)
                    || dungeonBlock.getLocation().equals(location)
                    || dungeonBlock.getLocation().distance(location) <= 1D))
                    || (plugin.getRegionHandler() != null && dungeon.getDungeonRegion().isEnabled()
                    && plugin.getRegionHandler().isDungeonRegion(location, dungeon.getDungeonRegion()));
        }).findFirst().orElse(null);
    }

    @NotNull
    public List<Dungeon> getActiveDungeons() {
        return this.getDungeons().stream()
                .filter(dungeon -> dungeon.getModuleManager().getModule(SpawnModule.class).isPresent() && dungeon.getModuleManager().getModule(SpawnModule.class).get().isSpawned() && !dungeon.getStage().isCancelled() || !dungeon.getStage().isRebooted() || !dungeon.getStage().isFreeze())
                .collect(Collectors.toList());
    }

    @Nullable
    public Dungeon getNearestDungeon() {
        return this.getDungeonMap().values()
                .stream().
                filter(f -> !f.getStage().isFreeze() && !f.getStage().isCancelled() && !f.getStage().isClosed()).min(Comparator.comparingInt(Dungeon::getNextStageTime))
                .orElse(null);
    }

    public boolean spawnDungeon(@NotNull Dungeon dungeon, @NotNull Location location) {
        SpawnModule module = dungeon.getModuleManager().getModule(SpawnModule.class).orElse(null);
        if (module == null) {
            plugin.error("Error spawning dungeon via command, because the dungeon spawning module is disabled or not loaded!");
            return false;
        }
        dungeon.cancel(false);
        NexPlugin.getScheduler().runDelayed(SchedulerType.SYNC, location, task -> {
            dungeon.setLocation(location);
            module.spawn(location);
            dungeon.getModuleManager().getModules().forEach(founder -> founder.activate(true));
            DungeonStage.call(dungeon, DungeonStage.OPENED, "Dungeon Manager with location");
        }, 5);
        return true;
    }

    public void interactDungeon(@NotNull Player player, @NotNull Dungeon dungeon, @NotNull Block block) {
        if (!block.hasMetadata(Keys.DUNGEON_CHEST_BLOCK.getKey())) return;
        this.openDungeonChest(dungeon, block, player);
    }

    public void openDungeonChest(@NotNull Dungeon dungeon, @NotNull Block block, @NotNull Player player) {
        dungeon.getModuleManager().getModule(ChestModule.class).ifPresent(module -> {
            DungeonChestBlock chest = module.getChestByBlock(block);
            if (chest != null) chest.click(player);

        });
    }
}
