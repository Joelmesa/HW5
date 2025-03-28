/******************************************************************
 *
 *   Joel Mesa Comp 271
 *
 *   Note, additional comments provided throughout this source code
 *   is for educational purposes
 *
 ********************************************************************/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Math;

public class CuckooHash<K, V> {

    private int CAPACITY;
    private Bucket<K, V>[] table;
    private int a = 37, b = 17;

    private class Bucket<K, V> {
        private K bucKey = null;
        private V value = null;

        public Bucket(K k, V v) {
            bucKey = k;
            value = v;
        }

        private K getBucKey() {
            return bucKey;
        }

        private V getValue() {
            return value;
        }
    }

    private int hash1(K key) {
        return Math.abs(key.hashCode()) % CAPACITY;
    }

    private int hash2(K key) {
        return (a * b + Math.abs(key.hashCode())) % CAPACITY;
    }

    public CuckooHash() {
        this(10);
    }

    public CuckooHash(int size) {
        CAPACITY = size;
        table = new Bucket[CAPACITY];
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < CAPACITY; ++i) {
            if (table[i] != null)
                count++;
        }
        return count;
    }

    public void clear() {
        table = new Bucket[CAPACITY];
    }

    public int mapSize() {
        return CAPACITY;
    }

    public List<V> values() {
        List<V> allValues = new ArrayList<V>();
        for (int i = 0; i < CAPACITY; ++i) {
            if (table[i] != null) {
                allValues.add(table[i].getValue());
            }
        }
        return allValues;
    }

    public Set<K> keys() {
        Set<K> allKeys = new HashSet<K>();
        for (int i = 0; i < CAPACITY; ++i) {
            if (table[i] != null) {
                allKeys.add(table[i].getBucKey());
            }
        }
        return allKeys;
    }

    public void put(K key, V value) {
        Bucket<K, V> newEntry = new Bucket<>(key, value);

        for (int attempt = 0; attempt < CAPACITY; attempt++) {
            int pos1 = hash1(newEntry.bucKey);
            if (table[pos1] == null) {
                table[pos1] = newEntry;
                return;
            } else if (table[pos1].bucKey.equals(key) && table[pos1].value.equals(value)) {
                return;
            }

            Bucket<K, V> temp = table[pos1];
            table[pos1] = newEntry;
            newEntry = temp;

            int pos2 = hash2(newEntry.bucKey);
            if (table[pos2] == null) {
                table[pos2] = newEntry;
                return;
            } else if (table[pos2].bucKey.equals(key) && table[pos2].value.equals(value)) {
                return;
            }

            temp = table[pos2];
            table[pos2] = newEntry;
            newEntry = temp;
        }

        rehash();
        put(newEntry.bucKey, newEntry.value);
    }

    public V get(K key) {
        int pos1 = hash1(key);
        int pos2 = hash2(key);
        if (table[pos1] != null && table[pos1].getBucKey().equals(key))
            return table[pos1].getValue();
        else if (table[pos2] != null && table[pos2].getBucKey().equals(key))
            return table[pos2].getValue();
        return null;
    }

    public boolean remove(K key, V value) {
        int pos1 = hash1(key);
        int pos2 = hash2(key);
        if (table[pos1] != null && table[pos1].getValue().equals(value)) {
            table[pos1] = null;
            return true;
        } else if (table[pos2] != null && table[pos2].getValue().equals(value)) {
            table[pos2] = null;
            return true;
        }
        return false;
    }

    public String printTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < CAPACITY; ++i) {
            if (table[i] != null) {
                sb.append("<");
                sb.append(table[i].getBucKey());
                sb.append(", ");
                sb.append(table[i].getValue());
                sb.append("> ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private void rehash() {
        Bucket<K, V>[] tableCopy = table.clone();
        int OLD_CAPACITY = CAPACITY;
        CAPACITY = (CAPACITY * 2) + 1;
        table = new Bucket[CAPACITY];

        for (int i = 0; i < OLD_CAPACITY; ++i) {
            if (tableCopy[i] != null) {
                put(tableCopy[i].getBucKey(), tableCopy[i].getValue());
            }
        }
    }
}
