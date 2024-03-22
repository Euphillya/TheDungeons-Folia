package t.me.p1azmer.plugin.dungeons.key.editor;

import t.me.plazmer.engine.shaded.energie.model.SchedulerType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.menu.AutoPaged;
import t.me.p1azmer.engine.api.menu.click.ItemClick;
import t.me.p1azmer.engine.api.menu.impl.EditorMenu;
import t.me.p1azmer.engine.api.menu.impl.MenuOptions;
import t.me.p1azmer.engine.api.menu.impl.MenuViewer;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.utils.ItemReplacer;
import t.me.p1azmer.plugin.dungeons.DungeonPlugin;
import t.me.p1azmer.plugin.dungeons.config.Config;
import t.me.p1azmer.plugin.dungeons.editor.EditorLocales;
import t.me.p1azmer.plugin.dungeons.key.Key;
import t.me.p1azmer.plugin.dungeons.key.KeyManager;
import t.me.p1azmer.plugin.dungeons.lang.Lang;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class KeyListEditor extends EditorMenu<DungeonPlugin, KeyManager> implements AutoPaged<Key> {

    public KeyListEditor(@NotNull KeyManager keyManager) {
        super(keyManager.plugin(), keyManager, Config.EDITOR_TITLE_KEY.get(), 45);

        this.addReturn(39).setClick((viewer, event) ->
                NexPlugin.getScheduler().runTask(SchedulerType.SYNC, viewer.getPlayer(), task -> this.plugin.getEditor().open(viewer.getPlayer(), 1), null));
        this.addNextPage(44);
        this.addPreviousPage(36);

        this.addCreation(EditorLocales.KEY_CREATE, 41).setClick((viewer, event) ->
                this.handleInput(viewer, Lang.EDITOR_DUNGEON_ENTER_KEY_ID, wrapper -> {
                    if (!keyManager.create(wrapper.getTextRaw())) {
                        EditorManager.error(viewer.getPlayer(), plugin.getMessage(Lang.DUNGEON_KEY_ERROR_EXISTS).getLocalized());
                        return false;
                    }
                    return true;
                }));
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    public List<Key> getObjects(@NotNull Player player) {
        return plugin.getKeyManager().getKeys().stream().sorted(Comparator.comparing(Key::getId)).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Key key) {
        ItemStack item = new ItemStack(key.getItem());
        ItemReplacer.create(item)
                .setDisplayName(EditorLocales.KEY_OBJECT.getLocalizedName())
                .setLore(EditorLocales.KEY_OBJECT.getLocalizedLore())
                .setHideFlags(true)
                .replace(key.replacePlaceholders())
                .writeMeta();
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Key key) {
        return (viewer, event) -> {
            Player player = viewer.getPlayer();
            if (event.isRightClick() && event.isShiftClick()) {
                if (this.plugin.getKeyManager().delete(key)) {
                    NexPlugin.getScheduler().runTask(SchedulerType.SYNC, viewer.getPlayer(), task -> this.open(player, viewer.getPage()), null);
                }
                return;
            }
            key.getEditor().open(player, 1);
        };
    }
}