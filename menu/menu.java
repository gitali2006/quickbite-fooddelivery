## PART A — BACKEND (Java)

### `src/main/java/com/example/fooddelivery/model/MenuItem.java`
```java
package com.example.fooddelivery.model;

import com.example.fooddelivery.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * MEMBER 3 - MENU MODULE
 * OOP: INHERITANCE from BaseEntity, ENCAPSULATION (price can only change
 *      through methods), ASSOCIATION with Restaurant.
 */
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    private String category;      // Starters, Main, Dessert, Drinks

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    private boolean available = true;

    private boolean vegetarian = false;

    /** Many menu items belong to one restaurant. */
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public MenuItem() {
    }

    public MenuItem(String name, String category, BigDecimal price, Restaurant restaurant) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.restaurant = restaurant;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    /** BUSINESS LOGIC inside the object (ENCAPSULATION): price of N portions. */
    public BigDecimal totalFor(int quantity) {
        if (quantity <= 0) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public String getAvailabilityLabel() {
        return available ? "Available" : "Sold out";
    }

    // ---------- Getters / Setters ----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

```

### `src/main/java/com/example/fooddelivery/repository/MenuItemRepository.java`
```java
package com.example.fooddelivery.repository;

import com.example.fooddelivery.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** MEMBER 3 - data access for menu items. */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByCategoryIgnoreCase(String category);

    long countByRestaurantId(Long restaurantId);
}

```

### `src/main/java/com/example/fooddelivery/service/MenuItemService.java`
```java
package com.example.fooddelivery.service;

import com.example.fooddelivery.model.MenuItem;
import com.example.fooddelivery.model.Restaurant;

import java.util.List;

public interface MenuItemService {

    List<MenuItem> findAll();

    List<MenuItem> findByRestaurant(Long restaurantId);

    List<MenuItem> findAvailableByRestaurant(Long restaurantId);

    List<MenuItem> search(String keyword);

    List<MenuItem> findByCategory(String category);

    MenuItem findById(Long id);

    MenuItem save(MenuItem menuItem);

    void deleteById(Long id);

    void toggleAvailability(Long id);

    long count();

    /** Every dish grouped by its restaurant (used by the customer menu page). */
    java.util.Map<Restaurant, List<MenuItem>> groupByRestaurant();
}

```

### `src/main/java/com/example/fooddelivery/service/impl/MenuItemServiceImpl.java`
```java
package com.example.fooddelivery.service.impl;

import com.example.fooddelivery.common.ResourceNotFoundException;
import com.example.fooddelivery.model.MenuItem;
import com.example.fooddelivery.model.Restaurant;
import com.example.fooddelivery.repository.MenuItemRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** MEMBER 3 - business logic for the Menu module. */
@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                               RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItem> findByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<MenuItem> findAvailableByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    @Override
    public List<MenuItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return menuItemRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    public List<MenuItem> findByCategory(String category) {
        if (category == null || category.isBlank()) {
            return findAll();
        }
        return menuItemRepository.findByCategoryIgnoreCase(category.trim());
    }

    @Override
    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Menu item", id));
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Menu item", id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    public void toggleAvailability(Long id) {
        MenuItem item = findById(id);
        item.setAvailable(!item.isAvailable());
        menuItemRepository.save(item);
    }

    @Override
    public long count() {
        return menuItemRepository.count();
    }

    /** Groups menu items so the menu page can show one section per restaurant. */
    @Override
    public Map<Restaurant, List<MenuItem>> groupByRestaurant() {
        Map<Restaurant, List<MenuItem>> grouped = new LinkedHashMap<>();
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            List<MenuItem> items = menuItemRepository.findByRestaurantId(restaurant.getId());
            if (!items.isEmpty()) {
                grouped.put(restaurant, items);
            }
        }
        return grouped;
    }
}

```

