package com.example.hop_oasis.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Component
@Scope(scopeName = SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class Cart {
    private Map<Long, Integer> beers;

    public Cart() {
        this.beers = new HashMap<>();
    }

    public Map<Long, Integer> getBeers() {
        return Collections.unmodifiableMap(beers);
    }

    public void add(Long beerId) {
        beers.merge(beerId, 1, (o1, o2) -> o1 + o2);
    }

    public void updateQuantity(Long beerId, int quantity) {
        if (quantity <= 0) {
            beers.remove(beerId);
        } else {
            beers.put(beerId, quantity);
        }
    }

    public void delete() {
        beers.clear();
    }
}
