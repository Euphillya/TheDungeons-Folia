package t.me.p1azmer.engine.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.utils.CollectionsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
public abstract class AbstractMenuAuto<P extends NexPlugin<P>, I> extends AbstractMenu<P> {

    public AbstractMenuAuto(@NotNull P plugin, @NotNull JYML cfg, @NotNull String path) {
        super(plugin, cfg, path);
    }

    public AbstractMenuAuto(@NotNull P plugin, @NotNull String title, int size/*, int[] slots*/) {
        super(plugin, title, size);
    }

    protected abstract int[] getObjectSlots();

    @NotNull
    protected abstract List<I> getObjects(@NotNull Player player);

    @NotNull
    protected List<I> fineObjects(@NotNull List<I> objects, @NotNull Player player) {
        return objects;
    }
    @NotNull
    protected abstract ItemStack getObjectStack(@NotNull Player player, @NotNull I object);

    @NotNull
    protected abstract MenuClick getObjectClick(@NotNull Player player, @NotNull I object);

    @Override
    public boolean onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
        int len = this.getObjectSlots().length;
        List<I> list = new ArrayList<>(this.getObjects(player));
        List<List<I>> split = CollectionsUtil.split(list, len);

        int pages = split.size();
        int page = Math.min(pages, this.getPage(player));
        if (pages < 1) list = Collections.emptyList();
        else list = split.get(page - 1);

        list = this.fineObjects(list, player);

        int count = 0;
        for (I object : list) {
            ItemStack item = this.getObjectStack(player, object);
            WeakMenuItem menuItem = new WeakMenuItem(player, item, this.getObjectSlots()[count++]);
            menuItem.setClickHandler(this.getObjectClick(player, object));
            this.addItem(menuItem);
        }
        this.setPage(player, page, pages);
        return true;
    }
}