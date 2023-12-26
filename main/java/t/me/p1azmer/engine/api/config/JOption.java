package t.me.p1azmer.engine.api.config;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.StringUtil;
import t.me.p1azmer.engine.utils.TriFunction;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class JOption<T> {

    public static final Reader<Boolean> READER_BOOLEAN = JYML::getBoolean;
    public static final Reader<Integer> READER_INT = JYML::getInt;
    public static final Reader<Double> READER_DOUBLE = JYML::getDouble;
    public static final Reader<Long> READER_LONG = JYML::getLong;
    public static final Reader<Set<String>> READER_SET_STRING = (cfg, path, def) -> StringUtil.color(cfg.getStringSet(path));
    public static final Reader<List<String>> READER_LIST_STRING = (cfg, path, def) -> StringUtil.color(cfg.getStringList(path));
    public static final Reader<ItemStack> READER_ITEM = JYML::getItem;
    public static final Reader<String> READER_STRING = (cfg, path, def) -> {
        if (!path.startsWith("Database.")) {
            return Colorizer.apply(cfg.getString(path, def));
        } else {
            return cfg.getString(path, def);
        }
    };

    protected final String path;
    protected final T defaultValue;
    protected final String[] description;
    protected T value;
    protected Writer<T> writer;
    protected Reader<T> reader;

    public JOption(@NotNull String path, @NotNull Reader<T> reader, @NotNull Supplier<T> defaultValue, @NotNull String... description) {
        this(path, reader, defaultValue.get(), description);
    }

    public JOption(@NotNull String path, @NotNull Reader<T> reader, @NotNull T defaultValue, @NotNull String... description) {
        this.path = path;
        this.description = description;
        this.reader = reader;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public static JOption<Boolean> create(@NotNull String path, boolean defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_BOOLEAN, defaultValue, description);
    }

    @NotNull
    public static JOption<Integer> create(@NotNull String path, int defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_INT, defaultValue, description);
    }

    @NotNull
    public static JOption<Double> create(@NotNull String path, double defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_DOUBLE, defaultValue, description);
    }

    @NotNull
    public static JOption<Long> create(@NotNull String path, long defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_LONG, defaultValue, description);
    }

    @NotNull
    public static JOption<String> create(@NotNull String path, @NotNull String defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_STRING, defaultValue, description);
    }

    @NotNull
    public static JOption<List<String>> create(@NotNull String path, @NotNull List<String> defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_LIST_STRING, defaultValue, description);
    }

    @NotNull
    public static JOption<Set<String>> create(@NotNull String path, @NotNull Set<String> defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_SET_STRING, defaultValue, description);
    }

    @NotNull
    public static JOption<ItemStack> create(@NotNull String path, @NotNull ItemStack defaultValue, @NotNull String... description) {
        return new JOption<>(path, READER_ITEM, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>> JOption<E> create(@NotNull String path, @NotNull Class<E> clazz, @NotNull E defaultValue, @NotNull String... description) {
        return new JOption<>(path, ((cfg, path1, def) -> cfg.getEnum(path1, clazz, defaultValue)), defaultValue, description)
                .setWriter((cfg, path1, type) -> cfg.set(path1, type.name()));
    }

    @NotNull
    public static <V> JOption<Set<V>> forSet(@NotNull String path, @NotNull Function<String, V> valFun,
                                             @NotNull Supplier<Set<V>> defaultValue, @NotNull String... description) {
        return forSet(path, valFun, defaultValue.get(), description);
    }

    @NotNull
    public static <V> JOption<Set<V>> forSet(@NotNull String path, @NotNull Function<String, V> valFun,
                                             @NotNull Set<V> defaultValue, @NotNull String... description) {
        return new JOption<>(path,
                (cfg, path1, def) -> cfg.getStringSet(path1).stream().map(valFun).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new)),
                defaultValue,
                description);
    }

    @NotNull
    public static <K, V> JOption<Map<K, V>> forMap(@NotNull String path,
                                                   @NotNull Function<String, K> keyFun,
                                                   @NotNull TriFunction<JYML, String, String, V> valFun,
                                                   @NotNull Supplier<Map<K, V>> defaultValue, @NotNull String... description) {
        return forMap(path, keyFun, valFun, defaultValue.get(), description);
    }

    @NotNull
    public static <K, V> JOption<Map<K, V>> forMap(@NotNull String path,
                                                   @NotNull Function<String, K> keyFun,
                                                   @NotNull TriFunction<JYML, String, String, V> valFun,
                                                   @NotNull Map<K, V> defaultValue, @NotNull String... description) {
        return new JOption<>(path,
                (cfg, path1, def) -> {
                    Map<K, V> map = new HashMap<>();
                    for (String id : cfg.getSection(path1)) {
                        K key = keyFun.apply(id);
                        V val = valFun.apply(cfg, path1, id);
                        if (key == null || val == null) continue;

                        map.put(key, val);
                    }
                    return map;
                },
                defaultValue,
                description);
    }

    @NotNull
    public static <V> JOption<Map<String, V>> forMap(@NotNull String path, @NotNull TriFunction<JYML, String, String, V> function,
                                                     @NotNull Supplier<Map<String, V>> defaultValue, @NotNull String... description) {
        return forMap(path, String::toLowerCase, function, defaultValue.get(), description);
    }

    @NotNull
    public static <V> JOption<Map<String, V>> forMap(@NotNull String path, @NotNull TriFunction<JYML, String, String, V> function,
                                                     @NotNull Map<String, V> defaultValue, @NotNull String... description) {
        return forMap(path, String::toLowerCase, function, defaultValue, description);
    }

    @NotNull
    public T read(@NotNull JYML cfg) {
        if (!cfg.contains(this.getPath())) {
            this.write(cfg);
        }
        return (this.value = this.reader.read(cfg, this.getPath(), this.getDefaultValue()));
    }

    public void write(@NotNull JYML cfg) {
        if (this.getWriter() != null) {
            this.getWriter().write(cfg, this.getPath(), this.get());
        } else {
            cfg.set(this.getPath(), this.get());
        }
    }

    public boolean remove(@NotNull JYML cfg) {
        return cfg.remove(this.getPath());
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public String[] getDescription() {
        return description;
    }

    @NotNull
    @Deprecated
    public JOption.Reader<T> getValueLoader() {
        return this.getReader();
    }

    @NotNull
    public Reader<T> getReader() {
        return reader;
    }

    @NotNull
    public JOption<T> mapReader(@NotNull UnaryOperator<T> operator) {
        if (this.reader == null) return this;

        Reader<T> readerHas = this.reader;
        this.reader = (cfg, path1, def) -> operator.apply(readerHas.read(cfg, path1, def));
        return this;
    }

    @NotNull
    public T getDefaultValue() {
        return defaultValue;
    }

    @NotNull
    public T get() {
        return this.value == null ? this.getDefaultValue() : this.value;
    }

    public void set(@NotNull T value) {
        this.value = value;
    }

    @Nullable
    public JOption.Writer<T> getWriter() {
        return writer;
    }

    @NotNull
    public JOption<T> setWriter(@Nullable JOption.Writer<T> writer) {
        this.writer = writer;
        return this;
    }

    public interface Reader<T> {

        @NotNull T read(@NotNull JYML cfg, @NotNull String path, @NotNull T def);
    }

    public interface Writer<T> {

        void write(@NotNull JYML cfg, @NotNull String path, @NotNull T obj);
    }
}