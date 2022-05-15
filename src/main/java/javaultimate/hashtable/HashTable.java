package javaultimate.hashtable;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class HashTable<K, V> {

    private final double loadFactor = 0.75d;
    private int arraySize = 16;
    private int nodeCount = 0;
    private int threshold = (int) (arraySize * loadFactor);
    @SuppressWarnings("unchecked")
    private Node<K, V>[] nodes = new Node[arraySize];

    /**
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new UnsupportedOperationException("You should implement a method that throws this exception");
        }
        var hash = hash(key);
        Node<K, V> existingNode = nodes[hash];
        var newVal = new Node<>(key, value);
        if (existingNode == null) {
            addNewValue(newVal, hash);
            return null;
        } else {
            while (true) {
                if (existingNode.key == newVal.key) {
                    var old = existingNode.value;
                    existingNode.value = newVal.value;
                    return old;
                }
                if (existingNode.next == null) {
                    existingNode.next = newVal;
                    return newVal.value;
                }
                existingNode = existingNode.next;
            }
        }
    }

    /**
     * Prints a content of the underlying table (array) according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
     * ...
     */
    public void printTable() {
        for (int i = 0; i < nodes.length; i++) {
            System.out.print(i + ": ");
            var val = nodes[i];
            while (val != null) {
                System.out.print(val.key + ":" + val.value);
                if (val.next != null) {
                    System.out.print(" -> ");
                }
                val = val.next;
            }
            System.out.print("\n");
        }
    }

    private void addNewValue(Node<K, V> newValue, int index) {
        if (nodeCount > threshold) {
            resize();
            index = hash(newValue.key);
        }
        nodes[index] = newValue;
        nodeCount++;
    }

    private void resize() {
        arraySize *= 2;
        threshold = (int) (arraySize * loadFactor);
        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[arraySize];
        Arrays.stream(nodes).filter(Objects::nonNull)
                .forEach(node -> newTab[hash(node.key)] = node);
        nodes = newTab;
    }

    private int hash(@NotNull K key) {
        return Math.abs(key.hashCode()) % arraySize;
    }
}
