package t.me.p1azmer.engine.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class PDCUtil {

    public static final PersistentDataType<byte[], double[]> DOUBLE_ARRAY = new DoubleArray();
    public static final PersistentDataType<byte[], String[]> STRING_ARRAY = new StringArray(StandardCharsets.UTF_8);
    public static final PersistentDataType<byte[], UUID> UUID = new UUIDDataType();

    @Nullable
    @Deprecated
    public static <Z> Z getData(@NotNull PersistentDataHolder holder, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        return get(holder, type, key).orElse(null);
    }

    @NotNull
    public static <Z> Optional<Z> get(@NotNull ItemStack holder, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        ItemMeta meta = holder.getItemMeta();
        if (meta == null) return Optional.empty();

//        return get(meta, type, key);
        return null;
    }

    @NotNull
    public static <Z> Optional<Z> get(@NotNull PersistentDataHolder holder, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (container.has(key, type)) {
            return Optional.ofNullable(container.get(key, type));
        }
        return Optional.empty();
    }

    @Deprecated
    public static void setData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @NotNull Object value) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        if (value instanceof Boolean) {
            Boolean i = (Boolean) value;
            container.set(key, PersistentDataType.INTEGER, i ? 1 : 0);
        } else if (value instanceof Double) {
            Double i = (Double) value;
            container.set(key, PersistentDataType.DOUBLE, i);
        } else if (value instanceof Integer) {
            Integer i = (Integer) value;
            container.set(key, PersistentDataType.INTEGER, i);
        } else if (value instanceof Long) {
            container.set(key, PersistentDataType.LONG, (Long) value);
        } else if (value instanceof String[]) {
            container.set(key, STRING_ARRAY, (String[]) value);
        } else if (value instanceof double[]) {
            container.set(key, DOUBLE_ARRAY, (double[]) value);
        } else if (value instanceof UUID) {
            container.set(key, UUID, (UUID) value);
        } else {
            String i = value.toString();
            container.set(key, PersistentDataType.STRING, i);
        }

        if (holder instanceof BlockState) {
            BlockState state = (BlockState) holder;
            state.update();
        }
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, boolean value) {
        set(holder, PersistentDataType.INTEGER, key, value ? 1 : 0);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, boolean value) {
        set(holder, PersistentDataType.INTEGER, key, value ? 1 : 0);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, double value) {
        set(holder, PersistentDataType.DOUBLE, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, double value) {
        set(holder, PersistentDataType.DOUBLE, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, int value) {
        set(holder, PersistentDataType.INTEGER, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, int value) {
        set(holder, PersistentDataType.INTEGER, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, long value) {
        set(holder, PersistentDataType.LONG, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, long value) {
        set(holder, PersistentDataType.LONG, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, @Nullable String value) {
        set(holder, PersistentDataType.STRING, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @Nullable String value) {
        set(holder, PersistentDataType.STRING, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, String[] value) {
        set(holder, STRING_ARRAY, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, String[] value) {
        set(holder, STRING_ARRAY, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, double[] value) {
        set(holder, DOUBLE_ARRAY, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, double[] value) {
        set(holder, DOUBLE_ARRAY, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, @Nullable UUID value) {
        set(holder, UUID, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @Nullable UUID value) {
        set(holder, UUID, key, value);
    }

    public static <T, Z> void set(@NotNull ItemStack item,
                                  @NotNull PersistentDataType<T, Z> dataType,
                                  @NotNull NamespacedKey key,
                                  @Nullable Z value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        //set(meta, dataType, key, value);
        item.setItemMeta(meta);
    }

    public static <T, Z> void set(@NotNull PersistentDataHolder holder,
                                  @NotNull PersistentDataType<T, Z> dataType,
                                  @NotNull NamespacedKey key,
                                  @Nullable Z value) {
        if (value == null) {
            remove(holder, key);
            return;
        }

        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, dataType, value);

        if (holder instanceof BlockState) {
            BlockState state = (BlockState) holder;
            state.update();
        }
    }

    @Deprecated
    public static void removeData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        remove(holder, key);
    }

    public static void remove(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        ItemMeta meta = holder.getItemMeta();
        if (meta == null) return;

        //remove(meta, key);
    }

    public static void remove(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.remove(key);

        if (holder instanceof BlockState) {
            BlockState state = (BlockState) holder;
            state.update();
        }
    }

    @Nullable
    @Deprecated
    public static String getStringData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return getData(holder, PersistentDataType.STRING, key);
    }

    @NotNull
    public static Optional<String> getString(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.STRING, key);
    }

    @NotNull
    public static Optional<String> getString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.STRING, key);
    }

    @Nullable
    @Deprecated
    public static String[] getStringArrayData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return getData(holder, STRING_ARRAY, key);
    }

    @NotNull
    public static Optional<String[]> getStringArray(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, STRING_ARRAY, key);
    }

    @NotNull
    public static Optional<String[]> getStringArray(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, STRING_ARRAY, key);
    }

    @Deprecated
    public static double[] getDoubleArrayData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return getData(holder, DOUBLE_ARRAY, key);
    }

    @NotNull
    public static Optional<double[]> getDoubleArray(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, DOUBLE_ARRAY, key);
    }

    @NotNull
    public static Optional<double[]> getDoubleArray(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, DOUBLE_ARRAY, key);
    }

    @Deprecated
    public static int getIntData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        Integer value = getData(holder, PersistentDataType.INTEGER, key);
        return value == null ? 0 : value;
    }

    @NotNull
    public static Optional<Integer> getInt(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key);
    }

    @NotNull
    public static Optional<Integer> getInt(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key);
    }

    @Deprecated
    public static long getLongData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        Long value = getData(holder, PersistentDataType.LONG, key);
        return value == null ? 0 : value;
    }

    @NotNull
    public static Optional<Long> getLong(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.LONG, key);
    }

    @NotNull
    public static Optional<Long> getLong(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.LONG, key);
    }

    @Deprecated
    public static double getDoubleData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        Double value = getData(holder, PersistentDataType.DOUBLE, key);
        return value == null ? 0D : value;
    }

    @NotNull
    public static Optional<Double> getDouble(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.DOUBLE, key);
    }

    @NotNull
    public static Optional<Double> getDouble(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.DOUBLE, key);
    }

    @Deprecated
    public static boolean getBooleanData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        int value = getIntData(holder, key);
        return value == 1;
    }

    @NotNull
    public static Optional<Boolean> getBoolean(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key).map(i -> i != 0);
    }

    @NotNull
    public static Optional<Boolean> getBoolean(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key).map(i -> i != 0);
    }

    @Nullable
    public static UUID getUniqueIdData(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return getData(holder, UUID, key);
    }

    @NotNull
    public static Optional<UUID> getUUID(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, UUID, key);
    }

    @NotNull
    public static Optional<UUID> getUUID(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, UUID, key);
    }

    @Nullable
    @Deprecated
    public static <Z> Z getData(@NotNull ItemStack item, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        //return getData(meta, type, key);
        return null;
    }

    @Deprecated
    public static void setData(@NotNull ItemStack item, @NotNull NamespacedKey key, @NotNull Object value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        //setData(meta, key, value);
        item.setItemMeta(meta);
    }

    @Deprecated
    public static void removeData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

//        removeData(meta, key);
        item.setItemMeta(meta);
    }

    @Nullable
    @Deprecated
    public static String getStringData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? null : getStringData(meta, key);
        return null;
    }

    @Deprecated
    public static int getIntData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? 0 : getIntData(meta, key);
        return 0;
    }

    @Deprecated
    public static long getLongData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? 0 : getLongData(meta, key);
        return 0;
    }

    @Nullable
    @Deprecated
    public static String[] getStringArrayData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? null : getStringArrayData(meta, key);
        return new String[0];
    }

    @Deprecated
    public static double[] getDoubleArrayData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? null : getDoubleArrayData(meta, key);
        return new double[0];
    }

    @Deprecated
    public static double getDoubleData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? 0 : getDoubleData(meta, key);
        return 0;
    }

    @Deprecated
    public static boolean getBooleanData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta != null && getBooleanData(meta, key);
        return false;
    }

    @Nullable
    @Deprecated
    public static UUID getUniqueIdData(@NotNull ItemStack item, @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
//        return meta == null ? null : getUniqueIdData(meta, key);
        return null;
    }

    public static class DoubleArray implements PersistentDataType<byte[], double[]> {

        @Override
        @NotNull
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        @NotNull
        public Class<double[]> getComplexType() {
            return double[].class;
        }

        @Override
        public @NotNull byte[] toPrimitive(double[] complex, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.allocate(complex.length * 8);
            for (double d : complex) {
                bb.putDouble(d);
            }
            return bb.array();
        }

        @Override
        public @NotNull double[] fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            DoubleBuffer dbuf = bb.asDoubleBuffer(); // Make DoubleBuffer
            double[] a = new double[dbuf.remaining()]; // Make an array of the correct size
            dbuf.get(a);

            return a;
        }
    }

    public static class StringArray implements PersistentDataType<byte[], String[]> {

        private final Charset charset;

        public StringArray(Charset charset) {
            this.charset = charset;
        }

        @NotNull
        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @NotNull
        @Override
        public Class<String[]> getComplexType() {
            return String[].class;
        }

        @Override
        public @NotNull byte[] toPrimitive(String[] strings, @NotNull PersistentDataAdapterContext itemTagAdapterContext) {
            byte[][] allStringBytes = new byte[strings.length][];
            int total = 0;
            for (int i = 0; i < allStringBytes.length; i++) {
                byte[] bytes = strings[i].getBytes(charset);
                allStringBytes[i] = bytes;
                total += bytes.length;
            }

            ByteBuffer buffer = ByteBuffer.allocate(total + allStringBytes.length * 4); // stores integers
            for (byte[] bytes : allStringBytes) {
                buffer.putInt(bytes.length);
                buffer.put(bytes);
            }

            return buffer.array();
        }

        @Override
        public @NotNull String[] fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext itemTagAdapterContext) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            ArrayList<String> list = new ArrayList<>();

            while (buffer.remaining() > 0) {
                if (buffer.remaining() < 4)
                    break;
                int stringLength = buffer.getInt();
                if (buffer.remaining() < stringLength)
                    break;

                byte[] stringBytes = new byte[stringLength];
                buffer.get(stringBytes);

                list.add(new String(stringBytes, charset));
            }

            return list.toArray(new String[0]);
        }
    }

    public static class UUIDDataType implements PersistentDataType<byte[], UUID> {

        @NotNull
        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @NotNull
        @Override
        public Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public @NotNull byte[] toPrimitive(UUID complex, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @Override
        public @NotNull UUID fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }
}