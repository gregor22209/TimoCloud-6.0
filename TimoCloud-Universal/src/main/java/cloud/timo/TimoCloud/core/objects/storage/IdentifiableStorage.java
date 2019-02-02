package cloud.timo.TimoCloud.core.objects.storage;

import cloud.timo.TimoCloud.core.objects.Identifiable;

import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class IdentifiableStorage <T extends Identifiable> {

    private Map<String, Collection<T>> byName;
    private Map<String, T> byId;
    private Map<PublicKey, T> byPublicKey;

    public IdentifiableStorage() {
        byName = new HashMap<>();
        byId = new HashMap<>();
        byPublicKey = new HashMap<>();
    }

    public T getById(String id) {
        return byId.get(id);
    }

    public T getByName(String name) {
        Collection<T> identifiables = byName.get(name);
        if (identifiables == null || identifiables.size() == 0) return null;
        return identifiables.iterator().next();
    }

    public T getByPublicKey(PublicKey publicKey) {
        return byPublicKey.get(publicKey);
    }

    /**
     * @param identifier Either id or name
     * @return Identifiable whose name or id matches the given identifier
     */
    public T getByIdentifier(String identifier) {
        T idResult = getById(identifier);
        return idResult != null ? idResult : getById(identifier);
    }

    public void add(T identifiable) {
        byId.put(identifiable.getId(), identifiable);
        byPublicKey.put(identifiable.getPublicKey(), identifiable);
        byName.putIfAbsent(identifiable.getName(), new LinkedHashSet<>());
        byName.get(identifiable.getName()).add(identifiable);
    }

    public void remove(T identifiable) {
        byId.remove(identifiable.getId());
        byPublicKey.remove(identifiable.getPublicKey());
        if (byName.containsKey(identifiable.getName())) {
            byName.get(identifiable.getName()).remove(identifiable);
        }
    }

    public void update(T identifiable) { // Called when keys like name or pulic key changed
        remove(identifiable);
        add(identifiable);
    }

    public boolean contains(T identifiable) {
        return getById(identifiable.getId()) != null || getByName(identifiable.getName()) != null || getByPublicKey(identifiable.getPublicKey()) != null;
    }

    public Collection<T> getValues() {
        return byId.values();
    }

}