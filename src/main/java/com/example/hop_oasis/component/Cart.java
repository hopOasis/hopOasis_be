//package com.example.hop_oasis.component;
//
//import com.example.hop_oasis.model.ItemType;
//
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
//import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;
//
//@AllArgsConstructor
//@Component
//@Scope(scopeName = SCOPE_SESSION, proxyMode = TARGET_CLASS)
//public class Cart {
//    private Map<Long, Integer> beers;
//    private Map<Long, Integer> snacks;
//    private Map<Long, Integer> productBundle;
//    private Map<Long, Integer> cider;
//
//    public Cart() {
//        this.beers = new HashMap<>();
//        this.snacks = new HashMap<>();
//        this.productBundle = new HashMap<>();
//        this.cider = new HashMap<>();
//    }
//
//    public Map<Long, Integer> getBeers() {
//        return Collections.unmodifiableMap(beers);
//    }
//
//    public Map<Long, Integer> getSnacks() {
//        return Collections.unmodifiableMap(snacks);
//    }
//
//    public Map<Long, Integer> getProductBundles() {
//        return Collections.unmodifiableMap(productBundle);
//    }
//
//    public Map<Long, Integer> getCider() {
//        return Collections.unmodifiableMap(cider);
//    }
//
//    public void add(Long itemId, int quantity, ItemType itemType) {
//        switch (itemType) {
//            case BEER:
//                beers.merge(itemId, quantity, Integer::sum);
//                break;
//            case SNACK:
//                snacks.merge(itemId, quantity, Integer::sum);
//                break;
//            case PRODUCT_BUNDLE:
//                productBundle.merge(itemId, quantity, Integer::sum);
//                break;
//            case CIDER:
//                cider.merge(itemId, quantity, Integer::sum);
//                break;
//        }
//
//    }
//
//    public void updateQuantity(Long itemId, int quantity, ItemType itemType) {
//        if (quantity <= 0) {
//            switch (itemType) {
//                case BEER:
//                    beers.remove(itemId);
//                    break;
//                case SNACK:
//                    snacks.remove(itemId);
//                    break;
//                case PRODUCT_BUNDLE:
//                    productBundle.remove(itemId);
//                    break;
//                case CIDER:
//                    cider.remove(itemId);
//                    break;
//            }
//        } else {
//            switch (itemType) {
//                case BEER:
//                    beers.put(itemId, quantity);
//                    break;
//                case SNACK:
//                    snacks.put(itemId, quantity);
//                    break;
//                case PRODUCT_BUNDLE:
//                    productBundle.put(itemId, quantity);
//                    break;
//                case CIDER:
//                    cider.put(itemId, quantity);
//            }
//        }
//    }
//
//    public void remove(Long itemId, ItemType itemType) {
//        switch (itemType) {
//            case BEER:
//                beers.remove(itemId);
//                break;
//            case SNACK:
//                snacks.remove(itemId);
//                break;
//            case PRODUCT_BUNDLE:
//                productBundle.remove(itemId);
//                break;
//            case CIDER:
//                cider.remove(itemId);
//                break;
//        }
//    }
//
//    public void delete() {
//        beers.clear();
//        snacks.clear();
//        productBundle.clear();
//        cider.clear();
//    }
//
//}