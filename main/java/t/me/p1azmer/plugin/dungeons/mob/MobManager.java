package t.me.p1azmer.plugin.dungeons.mob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.manager.AbstractManager;
import t.me.p1azmer.engine.utils.LocationUtil;
import t.me.p1azmer.engine.utils.PDCUtil;
import t.me.p1azmer.engine.utils.StringUtil;
import t.me.p1azmer.engine.utils.random.Rnd;
import t.me.p1azmer.plugin.dungeons.DungeonPlugin;
import t.me.p1azmer.plugin.dungeons.Keys;
import t.me.p1azmer.plugin.dungeons.Placeholders;
import t.me.p1azmer.plugin.dungeons.api.mob.MobFaction;
import t.me.p1azmer.plugin.dungeons.api.mob.MobList;
import t.me.p1azmer.plugin.dungeons.dungeon.impl.Dungeon;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.impl.ChestModule;
import t.me.p1azmer.plugin.dungeons.mob.config.MobConfig;
import t.me.p1azmer.plugin.dungeons.mob.config.MobsConfig;
import t.me.p1azmer.plugin.dungeons.mob.kill.MobKillReward;

import java.util.*;

public class MobManager extends AbstractManager<DungeonPlugin> {

    public static final String DIR_MOBS = "/mobs/";

    private Map<String, MobConfig> mobConfigMap;
    private MobList mobList;

    public MobManager(@NotNull DungeonPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.mobConfigMap = new HashMap<>();
        this.plugin.getConfigManager().extractResources(DIR_MOBS);

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + DIR_MOBS, false)) {
            MobConfig mob = new MobConfig(plugin, cfg);
            if (mob.load()) {
                this.mobConfigMap.put(mob.getId().toLowerCase(), mob);
            } else this.plugin.warn("Mob not loaded: '" + cfg.getFile().getName() + "'!");
        }
        plugin.info("Mobs Loaded: " + mobConfigMap.size());
        plugin.getConfig().initializeOptions(MobsConfig.class);
        this.mobList = new MobList();
        this.addListener(new MobListener(this));
    }

    @Override
    public void onShutdown() {
        if (this.mobConfigMap != null) {
            this.mobConfigMap.values().forEach(MobConfig::clear);
            this.mobConfigMap.clear();
        }
        this.killMobs();
    }

    @NotNull
    public MobList getMobs() {
        this.mobList.getEnemies().removeIf(mob -> !mob.isValid() || mob.isDead());
        return mobList;
    }

    public void killMobs() {
        for (MobFaction faction : MobFaction.values()) {
            this.killMobs(faction);
        }
    }

    public void killMobs(@NotNull MobFaction faction) {
        this.getMobs().removeAll(faction);
    }

    public boolean createMobConfig(@NotNull String id) {
        id = StringUtil.lowerCaseUnderscore(id);
        if (this.getMobConfigById(id) != null) return false;

        JYML cfg = new JYML(this.plugin.getDataFolder() + "/mobs/", id + ".yml");
        MobConfig mobConfig = new MobConfig(plugin, cfg);

        mobConfig.setEntityType(EntityType.ZOMBIE);
        mobConfig.setName(StringUtil.capitalizeUnderscored(mobConfig.getEntityType().name().toLowerCase()));
        mobConfig.setNameVisible(true);
        mobConfig.getAttributes().put(Attribute.GENERIC_MAX_HEALTH, 20D);

        mobConfig.save();
        mobConfig.load();
        this.getMobConfigMap().put(mobConfig.getId(), mobConfig);
        return true;
    }

    @NotNull
    public List<String> getMobIds() {
        return new ArrayList<>(this.mobConfigMap.keySet());
    }

    @NotNull
    public Map<String, MobConfig> getMobConfigMap() {
        return this.mobConfigMap;
    }

    @NotNull
    public Collection<MobConfig> getMobConfigs() {
        return this.mobConfigMap.values();
    }

    @Nullable
    public MobConfig getMobConfigById(@NotNull String id) {
        return this.mobConfigMap.get(id.toLowerCase());
    }

    @Nullable
    public LivingEntity spawnMob(@NotNull Dungeon dungeon, @NotNull MobFaction faction, @NotNull String mobId, @NotNull MobList mobList) {
        MobConfig customMob = this.getMobConfigById(mobId);
        if (customMob == null) {
            return null;
        }
        Location location = dungeon.getLocation();
        ChestModule module = dungeon.getModuleManager().getModule(ChestModule.class).orElse(null);
        if (module != null) {
            if (module.getBlocks().isEmpty()) return null;
            location = Rnd.get(module.getBlocks()).getLocation();
            location = LocationUtil.getPointOnCircle(location, Rnd.get(-5, 5), Rnd.get(-5, 5), Rnd.get(-5, 5));
            location = LocationUtil.getFirstGroundBlock(location);
        }
        if (location == null) return null;

        EntityType type = customMob.getEntityType();
        LivingEntity entity = this.spawnMob(plugin, faction, type, location);
        if (entity == null) {
            return null;
        }

        String riderId = customMob.getRiderId();
        if (riderId != null && !riderId.isEmpty()) {
            MobConfig rider = this.getMobConfigById(riderId);
            if (rider != null) {
                EntityType riderType = rider.getEntityType();
                LivingEntity riderEntity = this.spawnMob(plugin, faction, riderType, location);
                if (riderEntity != null) {
                    entity.addPassenger(riderEntity);

                    rider.applySettings(riderEntity);
                    rider.applyAttributes(riderEntity);
                    rider.applyPotionEffects(riderEntity);
                    this.setMobConfig(riderEntity, rider);
                    mobList.getEnemies().add(riderEntity);
                }
            }
        }

        customMob.applySettings(entity);
        customMob.applyAttributes(entity);
        customMob.applyPotionEffects(entity);
        this.setMobConfig(entity, customMob);
        mobList.getEnemies().add(entity);
        return entity;
    }

    public LivingEntity spawnMob(@NotNull DungeonPlugin plugin, @NotNull MobFaction faction, @NotNull EntityType type, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        Entity entity = location.getWorld().spawnEntity(location, type);
        if (!(entity instanceof LivingEntity bukkitEntity)) {
            return null;
        }

        plugin.runTask(sync -> bukkitEntity.teleport(location));
        return bukkitEntity;
    }

    private void setMobConfig(@NotNull LivingEntity entity, @NotNull MobConfig customMob) {
        PDCUtil.set(entity, Keys.ENTITY_MOB_ID, customMob.getId());
    }

    public static void setLevel(@NotNull LivingEntity entity, int level) {
        PDCUtil.set(entity, Keys.ENTITY_MOB_LEVEL, level);
    }

    @NotNull
    public static String getMobIdProvider(@NotNull LivingEntity entity) {
        return PDCUtil.getString(entity, Keys.ENTITY_MOB_ID).orElse("");
    }

    @NotNull
    public static String getMobId(@NotNull LivingEntity entity) {
        String[] split = getMobIdProvider(entity).split(":");
        return split.length == 2 ? split[1] : "";
    }

    @NotNull
    public static String getMobProvider(@NotNull LivingEntity entity) {
        String[] split = getMobIdProvider(entity).split(":");
        return split[0];
    }

    @Nullable
    public static MobKillReward getMobKillReward(@NotNull LivingEntity entity) {
        if (!MobsConfig.KILL_REWARD_ENABLED.get()) return null;

        var map = MobsConfig.KILL_REWARD_VALUES.get();
        return map.getOrDefault(getMobIdProvider(entity).toLowerCase(), map.get(Placeholders.DEFAULT));
    }

    public boolean isCustomMob(@NotNull Entity entity) {
        return this.getCustomEntity(entity);
    }

    public boolean getCustomEntity(@NotNull Entity entity) {
        String id = PDCUtil.getString(entity, Keys.ENTITY_MOB_ID).orElse(null);
        return id != null;
    }

    @Nullable
    public MobConfig getEntityMobConfig(@NotNull LivingEntity entity) {
        return this.getMobConfigById(getMobId(entity));
    }

    public static int getEntityLevel(@NotNull LivingEntity entity) {
        return PDCUtil.getInt(entity, Keys.ENTITY_MOB_LEVEL).orElse(0);
    }
}