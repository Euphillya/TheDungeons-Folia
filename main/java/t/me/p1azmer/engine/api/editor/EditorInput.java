package t.me.p1azmer.engine.api.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

@Deprecated
public interface EditorInput<T,E extends Enum<E>> {

//    boolean onType(
//        @NotNull Player player, @NotNull T object,
//        @NotNull C type, @NotNull String msg);

    boolean handle(@NotNull Player player, @NotNull T object, @NotNull E type, @NotNull AsyncPlayerChatEvent e);
}
