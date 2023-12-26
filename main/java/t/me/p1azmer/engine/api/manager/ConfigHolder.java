package t.me.p1azmer.engine.api.manager;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.config.JYML;

import java.io.File;

public interface ConfigHolder {

    @NotNull JYML getConfig();

    @NotNull
    default File getFile() {
        return this.getConfig().getFile();
    }

    void onSave();

    default void save() {
        this.onSave();
        this.getConfig().save();
    }
}
