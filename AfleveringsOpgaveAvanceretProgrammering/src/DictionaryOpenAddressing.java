public class DictionaryOpenAddressing<K, V> implements Dictionary<K, V> {

    private static class Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Sentinel der markerer en slettet plads (tombstone)
    @SuppressWarnings("unchecked")
    private final Entry<K, V> DELETED = (Entry<K, V>) new Entry<>(null, null);

    private Entry<K, V>[] table;
    private int size;
    private int capacity;

    private static final int DEFAULT_CAPACITY = 16;

    public DictionaryOpenAddressing() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public DictionaryOpenAddressing(int capacity) {
        this.capacity = capacity;
        this.table = (Entry<K, V>[]) new Entry[capacity];
        this.size = 0;
    }

    @Override
    public V put(K key, V value) {
        int startIndex = toIndex(key);
        int firstDeleted = -1;

        for (int probes = 0; probes < capacity; probes++) {
            int index = (startIndex + probes) % capacity;
            Entry<K, V> entry = table[index];

            if (entry == null) {
                int insertAt = (firstDeleted != -1) ? firstDeleted : index;
                table[insertAt] = new Entry<>(key, value);
                size++;
                return null;
            }

            if (entry == DELETED) {
                if (firstDeleted == -1) firstDeleted = index;
            } else if (entry.key.equals(key)) {
                V old = entry.value;
                entry.value = value;
                return old;
            }
        }
        
        if (firstDeleted != -1) {
            table[firstDeleted] = new Entry<>(key, value);
            size++;
            return null;
        }

        throw new IllegalStateException("Hash-tabellen er fuld");
    }

    @Override
    public V get(K key) {
        int index = findIndex(key);
        return (index == -1) ? null : table[index].value;
    }

    @Override
    public V remove(K key) {
        int index = findIndex(key);
        if (index == -1) return null;

        V old = table[index].value;
        table[index] = DELETED;
        size--;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Beregner indeks og håndterer negative hash-koder med bitmasking
    private int toIndex(K key) {
        return (key.hashCode() & Integer.MAX_VALUE) % capacity;
    }

    private int findIndex(K key) {
        if (key == null) return -1;
        int startIndex = toIndex(key);

        for (int probes = 0; probes < capacity; probes++) {
            int index = (startIndex + probes) % capacity;
            Entry<K, V> entry = table[index];

            if (entry == null) return -1; // Tom plads afslutter søgningen
            if (entry != DELETED && entry.key.equals(key)) return index;
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> entry : table) {
            if (entry != null && entry != DELETED) {
                if (!first) sb.append(", ");
                sb.append(entry.key).append("=").append(entry.value);
                first = false;
            }
        }
        return sb.append("}").toString();
    }
}
