package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final WeakHashMap<K,V> cache;
    private final List<HwListener<K, V>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        V value = cache.get(key);
        cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, value, "remove"));
    }

    @Override
    public V get(K key) {
      return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
