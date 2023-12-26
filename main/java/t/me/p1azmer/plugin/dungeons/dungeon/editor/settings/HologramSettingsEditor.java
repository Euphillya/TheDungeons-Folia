package t.me.p1azmer.plugin.dungeons.dungeon.editor.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.api.editor.EditorLocale;
import t.me.p1azmer.engine.api.menu.AutoPaged;
import t.me.p1azmer.engine.api.menu.click.ItemClick;
import t.me.p1azmer.engine.api.menu.impl.EditorMenu;
import t.me.p1azmer.engine.api.menu.impl.MenuOptions;
import t.me.p1azmer.engine.api.menu.impl.MenuViewer;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.ItemReplacer;
import t.me.p1azmer.plugin.dungeons.DungeonPlugin;
import t.me.p1azmer.plugin.dungeons.Placeholders;
import t.me.p1azmer.plugin.dungeons.config.Config;
import t.me.p1azmer.plugin.dungeons.dungeon.chest.DungeonChestState;
import t.me.p1azmer.plugin.dungeons.dungeon.settings.HologramSettings;
import t.me.p1azmer.plugin.dungeons.editor.EditorLocales;
import t.me.p1azmer.plugin.dungeons.lang.Lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class HologramSettingsEditor extends EditorMenu<DungeonPlugin, HologramSettings> implements AutoPaged<DungeonChestState> {


    public HologramSettingsEditor(@NotNull HologramSettings settings) {
        super(settings.getDungeon().plugin(), settings, Config.EDITOR_TITLE_DUNGEON.get(), 36);

        this.addReturn(31).setClick((viewer, event) -> {
            this.plugin.runTask(rask -> settings.getDungeon().getEditor().open(viewer.getPlayer(), 1));
        });
        this.addNextPage(32);
        this.addPreviousPage(30);
        this.addItem(Material.FEATHER, EditorLocales.DUNGEON_HOLOGRAM_Y_OFFSET, 4).setClick((viewer, event) -> {
            this.handleInput(viewer, Lang.EDITOR_DUNGEON_ENTER_HOLOGRAM_OFFSET, wrapper -> {
                settings.setOffsetY(wrapper.asAnyDouble(1.5));
                this.save(viewer);
                return true;
            });
        });

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier(((viewer, item) -> {
                    ItemReplacer.replace(item, settings.replacePlaceholders());
                }));
            }
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.getDungeon().save();
        this.plugin.runTask(task -> this.open(viewer.getPlayer(), viewer.getPage()));
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(10, 16).toArray();
    }

    @Override
    @NotNull
    public List<DungeonChestState> getObjects(@NotNull Player player) {
        return new ArrayList<>(Arrays.stream(DungeonChestState.values()).toList());
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull DungeonChestState state) {
        ItemStack item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemReplacer.create(item)
                .readLocale(EditorLocales.HOLOGRAM_OBJECT)
                .trimmed()
                .hideFlags()
                .replace(this.object.replacePlaceholders())
                .replace(s -> s.replace(Placeholders.EDITOR_HOLOGRAM_TEXT, String.join("\n", this.object.getMessages(state))))
                .replace(state.replacePlaceholders())
                .replace(Colorizer::apply)
                .writeMeta();

        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull DungeonChestState state) {
        return (viewer, event) -> {
            Player player = viewer.getPlayer();
            if (event.isShiftClick() && event.isRightClick()) {
                this.object.setStateMessages(state, new ArrayList<>());
                this.save(viewer);
                return;
            }

            if (event.isLeftClick()) {
                EditorManager.prompt(player, plugin.getMessage(Lang.Editor_Hologram_Text).getLocalized());
                EditorManager.startEdit(player, wrapper -> {
                    String message = wrapper.getTextRaw();
                    List<String> current = this.object.getMessages(state);
                    current.add(message);
                    this.object.setStateMessages(state, current);
                    this.save(viewer);
                    return true;
                });
                plugin.runTask(task -> player.closeInventory());
                return;
            }
            if (event.isRightClick()) {
                List<String> current = this.object.getMessages(state);
                if (current.isEmpty()) return;
                current.remove(current.size() + 1);
                this.object.setStateMessages(state, current);
                this.save(viewer);
            }
        };
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);
        if (slotType == SlotType.PLAYER || slotType == SlotType.PLAYER_EMPTY) {
            event.setCancelled(false);
        }
    }
}