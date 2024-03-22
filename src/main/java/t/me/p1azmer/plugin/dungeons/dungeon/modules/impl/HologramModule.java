package t.me.p1azmer.plugin.dungeons.dungeon.modules.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.plugin.dungeons.api.hologram.HologramHandler;
import t.me.p1azmer.plugin.dungeons.dungeon.impl.Dungeon;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.AbstractModule;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class HologramModule extends AbstractModule {
    private ChestModule chestModule;
    private final HologramHandler handler;
    public HologramModule(@NotNull Dungeon dungeon, @NotNull String id) {
        super(dungeon, id, true);
        this.handler = plugin().getHologramHandler();
    }

    @Override
    protected Predicate<Boolean> onLoad() {
        this.chestModule = dungeon().getModuleManager().getModule(ChestModule.class).orElse(null);
        return aBoolean -> (this.dungeon().getStage().isOpened() || this.dungeon().getStage().isOpening() || this.dungeon().getStage().isWaitingPlayers())
                && handler != null && this.chestModule != null && !this.chestModule.getChests().isEmpty() && dungeon().getLocation() != null;
    }

    @Override
    protected void onShutdown() {
        if (this.chestModule != null){
            this.chestModule = null;
        }
    }

    @Override
    public CompletableFuture<Boolean> onActivate(boolean force) {
        Location location = this.dungeon().getLocation();
        if (location == null || handler == null){
            return CompletableFuture.completedFuture(false);
        }
        handler.create(this.dungeon(), this.chestModule);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public boolean onDeactivate() {
        if (handler == null) return true;
        handler.delete(this.dungeon());
        return true;
    }
}
