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

