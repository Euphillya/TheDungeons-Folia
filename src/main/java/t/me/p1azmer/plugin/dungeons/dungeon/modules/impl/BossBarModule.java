package t.me.p1azmer.plugin.dungeons.dungeon.modules.impl;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.plugin.dungeons.dungeon.impl.Dungeon;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.AbstractModule;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class BossBarModule extends AbstractModule {
    private BossBar bossBar;

    public BossBarModule(@NotNull Dungeon dungeon, @NotNull String id) {
        super(dungeon, id, true);
    }

    @Override
    protected Predicate<Boolean> onLoad() {
//        this.bossBar = Bukkit.createBossBar(Colorizer.apply(this.dungeon().replacePlaceholders().apply(Config.BOSSBAR_TITLE.get())), Config.BOSSBAR_COLOR.get(), Config.BOSSBAR_STYLE.get()); // rewrite for dungeon self bossbar
        return aBoolean -> dungeon().getStage().isPrepare();
    }

    @Override
    protected void onShutdown() {
        if (this.bossBar != null) {
            CompletableFuture.runAsync(this.bossBar::removeAll);
            this.bossBar = null;
        }
    }

    @Override
    public CompletableFuture<Boolean> onActivate(boolean force) {
        if (this.bossBar == null) {
            //if (!force)return false;
            //this.bossBar = Bukkit.createBossBar(Colorizer.apply(this.dungeon().replacePlaceholders().apply(Config.BOSSBAR_TITLE.get())), Config.BOSSBAR_COLOR.get(), Config.BOSSBAR_STYLE.get()); // rewrite for dungeon self bossbar
            CompletableFuture.completedFuture(false);
        }

        this.dungeon().getWorld().getPlayers().forEach(this.bossBar::addPlayer);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public void update() {
        if (this.bossBar == null) return;
//        this.bossBar.setProgress(this.dungeon().getNextStageTime() / 100F);

    }

    @Override
    public boolean onDeactivate() {
        CompletableFuture.runAsync(this.bossBar::removeAll);
        return true;
    }
}
