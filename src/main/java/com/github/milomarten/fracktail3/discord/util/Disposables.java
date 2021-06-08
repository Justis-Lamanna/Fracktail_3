package com.github.milomarten.fracktail3.discord.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;

import java.util.*;

/**
 * Utility map that ensures removed Disposables are disposed of properly
 * @param <K>
 */
public class Disposables<K> extends AbstractMap<K, Disposable> {
    private Set<Map.Entry<K, Disposable>> entries = new DisposableSet<>(new HashSet<>());

    @Override
    public Set<Entry<K, Disposable>> entrySet() {
        return entries;
    }

    @Override
    public Disposable put(K key, Disposable value) {
        Optional<Entry<K, Disposable>> entry = entrySet().stream()
                .filter(e -> e.getKey().equals(key))
                .findFirst();
        if(entry.isPresent()) {
            return entry.get().setValue(value);
        } else {
            entries.add(new DisposablesNode<>(key, value));
            return null;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class DisposablesNode<K> implements Map.Entry<K, Disposable> {
        final K key;
        Disposable value;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public Disposable getValue() {
            return value;
        }

        @Override
        public Disposable setValue(Disposable value) {
            Disposable old = this.value;
            if(old != null && !old.isDisposed()) {
                old.dispose();
            }
            this.value = value;
            return old;
        }
    }

    @RequiredArgsConstructor
    static class DisposableSet<K> extends AbstractSet<Map.Entry<K, Disposable>> {
        private final Set<Map.Entry<K, Disposable>> backing;

        @Override
        public boolean add(Entry<K, Disposable> kDisposableEntry) {
            return backing.add(kDisposableEntry);
        }

        @Override
        public Iterator<Entry<K, Disposable>> iterator() {
            Iterator<Entry<K, Disposable>> backingIterator = backing.iterator();
            return new Iterator<Entry<K, Disposable>>() {
                private Entry<K, Disposable> last = null;

                @Override
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }

                @Override
                public Entry<K, Disposable> next() {
                    last = backingIterator.next();
                    return last;
                }

                @Override
                public void remove() {
                    Disposable d = last.getValue();
                    if(d != null && !d.isDisposed()) {
                        d.dispose();
                    }
                }
            };
        }

        @Override
        public int size() {
            return backing.size();
        }
    }
}
