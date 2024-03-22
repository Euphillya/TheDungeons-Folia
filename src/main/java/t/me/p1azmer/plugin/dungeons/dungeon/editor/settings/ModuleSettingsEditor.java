package t.me.p1azmer.plugin.dungeons.dungeon.editor.settings;

import t.me.plazmer.engine.shaded.energie.model.SchedulerType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.menu.AutoPaged;
import t.me.p1azmer.engine.api.menu.click.ItemClick;
import t.me.p1azmer.engine.api.menu.impl.EditorMenu;
import t.me.p1azmer.engine.api.menu.impl.MenuOptions;
import t.me.p1azmer.engine.api.menu.impl.MenuViewer;
import t.me.p1azmer.engine.lang.LangManager;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.ItemReplacer;
import t.me.p1azmer.engine.utils.ItemUtil;
import t.me.p1azmer.plugin.dungeons.DungeonPlugin;
import t.me.p1azmer.plugin.dungeons.Placeholders;
import t.me.p1azmer.plugin.dungeons.config.Config;
import t.me.p1azmer.plugin.dungeons.dungeon.modules.ModuleId;
import t.me.p1azmer.plugin.dungeons.dungeon.settings.ModuleSettings;
import t.me.p1azmer.plugin.dungeons.editor.EditorLocales;

import java.util.List;
import java.util.stream.IntStream;

public class ModuleSettingsEditor extends EditorMenu<DungeonPlugin, ModuleSettings> implements AutoPaged<String> {

    public ModuleSettingsEditor(@NotNull ModuleSettings settings) {
        super(settings.dungeon().plugin(), settings, Config.EDITOR_TITLE_DUNGEON.get(), 36);

        this.addReturn(31).setClick((viewer, event) -> {
            NexPlugin.getScheduler().runTask(SchedulerType.SYNC, viewer.getPlayer(), task ->settings.dungeon().getEditor().open(viewer.getPlayer(), 1), null);
        });
        this.addNextPage(32);
        this.addPreviousPage(30);

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier(((viewer, item) -> {
                    ItemReplacer.replace(item, settings.replacePlaceholders());
                }));
            }
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.dungeon().save();
        NexPlugin.getScheduler().runTask(SchedulerType.SYNC, viewer.getPlayer(), task -> this.open(viewer.getPlayer(), viewer.getPage()), null);
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(10, 17).toArray();
    }

    @Override
    @NotNull
    public List<String> getObjects(@NotNull Player player) {
        return List.of(ModuleId.ANNOUNCE, ModuleId.CHEST, ModuleId.COMMAND, ModuleId.SCHEMATIC, ModuleId.HOLOGRAM, ModuleId.SPAWN);
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull String moduleId) {
        boolean enabled = this.object.isEnabled(moduleId);
        ItemStack item = enabled ?
                ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWMwMWY2Nzk2ZWI2M2QwZThhNzU5MjgxZDAzN2Y3YjM4NDMwOTBmOWE0NTZhNzRmNzg2ZDA0OTA2NWM5MTRjNyJ9fX0=") :
                ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjI1NTRkZGE4MGVhNjRiMThiYzM3NWI4MWNlMWVkMTkwN2ZjODFhZWE2YjFjZjNjNGY3YWQzMTQ0Mzg5ZjY0YyJ9fX0=");
        ItemReplacer.create(item)
                .readLocale(EditorLocales.MODULE_OBJECT)
                .trimmed()
                .hideFlags()
                .replace(s -> s
                        .replace(Placeholders.MODULE_ID, moduleId)
                        .replace(Placeholders.EDITOR_MODULE_ENABLED, LangManager.getBoolean(enabled))
                )
                .replace(this.object.replacePlaceholders())
                .replace(Colorizer::apply)
                .writeMeta();

        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull String moduleId) {
        return (viewer, event) -> {
            if (event.getClick().equals(ClickType.LEFT)) {
                this.object.setEnabled(moduleId, !this.object.isEnabled(moduleId));
                this.save(viewer);
            }
        };
    }
}