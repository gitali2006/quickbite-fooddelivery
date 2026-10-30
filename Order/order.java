# QuickBite — MEMBER 5: Order Management & Delivery Workflow — COMPLETE CODE

*Backend (Java / Spring Boot) + Frontend (HTML + CSS) of this module in one document.*

---

## PART A — BACKEND (Java)

### `src/main/java/com/example/fooddelivery/model/Order.java`
```java
package com.example.fooddelivery.model;

import com.example.fooddelivery.common.BaseEntity;
import com.example.fooddelivery.model.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MEMBER 5 - ORDER MODULE
 * The heart of the system: an order belongs to one customer, one restaurant
 * and contains many order items. A driver can be assigned to deliver it.
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    /** COMPOSITION: items live and die with the order (cascade + orphanRemoval). */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PLACED;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    private String deliveryAddress;

    @Column(length = 300)
    private String notes;

    private LocalDateTime placedAt;

    private LocalDateTime deliveredAt;

    public Order() {
    }

    public Order(Customer customer, Restaurant restaurant, String deliveryAddress) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.placedAt = LocalDateTime.now();
        this.orderNumber = "ORD-" + System.currentTimeMillis() % 1000000;
    }

    @Override
    public String getDisplayName() {
        return "Order " + orderNumber;
    }

    /** Keeps both sides of the relationship in sync (OOP + JPA best practice). */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
        recalculateTotal();
    }

    /** ENCAPSULATION: the total can never be set wrongly from outside. */
    public void recalculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getLineTotal());
        }
        this.totalAmount = total;
    }

    /** Status change rules live inside the object (business logic). */
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        if (newStatus == OrderStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }

    public boolean isActive() {
        return status != OrderStatus.DELIVERED && status != OrderStatus.CANCELLED;
    }

    public int getItemCount() {
        return items.size();
    }

    // ---------- Getters / Setters ----------

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}

```

### `src/main/java/com/example/fooddelivery/model/OrderItem.java`
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
 * MEMBER 5 - ORDER MODULE
 * A single line inside an order: dish + quantity + the price at that moment
 * (we copy the price so old orders never change when the menu price changes).
 */
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    private int quantity = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    public OrderItem() {
    }

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = menuItem.getPrice();
    }

    @Override
    public String getDisplayName() {
        return menuItem.getName() + " x" + quantity;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // ---------- Getters / Setters ----------

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

```

### `src/main/java/com/example/fooddelivery/model/enums/OrderStatus.java`
```java
package com.example.fooddelivery.model.enums;

/**
 * Life cycle of an order. Using an enum keeps the status values safe
 * (a typo like "DELIVERD" can never happen).
 */
public enum OrderStatus {
    PLACED("Placed"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    OUT_FOR_DELIVERY("Out for delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** CSS class name used by the frontend badges. */
    public String getCssClass() {
        return "status-" + name().toLowerCase().replace('_', '-');
    }
}

```

### `src/main/java/com/example/fooddelivery/repository/OrderRepository.java`
```java
package com.example.fooddelivery.repository;

import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** MEMBER 5 - data access for orders. */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByIdDesc(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByDriverId(Long driverId);

    List<Order> findByRestaurantId(Long restaurantId);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByOrderByIdDesc();

    long countByStatus(OrderStatus status);
}

```

### `src/main/java/com/example/fooddelivery/repository/OrderItemRepository.java`
```java
package com.example.fooddelivery.repository;

import com.example.fooddelivery.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** MEMBER 5 - data access for the lines of an order. */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    long countByMenuItemId(Long menuItemId);
}

```

### `src/main/java/com/example/fooddelivery/service/OrderService.java`
```java
package com.example.fooddelivery.service;

import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    Order findById(Long id);

    List<Order> findByCustomer(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByDriver(Long driverId);

    List<Order> findActiveOrders();

    /** Takes everything from the customer's cart and turns it into a real order. */
    Order placeOrderFromCart(Long customerId, String deliveryAddress, String notes);

    Order updateStatus(Long orderId, OrderStatus status);

    Order assignDriver(Long orderId, Long driverId);

    Order save(Order order);

    Order cancel(Long orderId);

    void deleteById(Long id);

    long count();

    long countByStatus(OrderStatus status);

    /** Added together for the admin dashboard. */
    java.math.BigDecimal totalSalesOfDeliveredOrders();
}

```

### `src/main/java/com/example/fooddelivery/service/impl/OrderServiceImpl.java`
```java
package com.example.fooddelivery.service.impl;

import com.example.fooddelivery.common.ResourceNotFoundException;
import com.example.fooddelivery.model.CartItem;
import com.example.fooddelivery.model.Customer;
import com.example.fooddelivery.model.Driver;
import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.enums.DriverStatus;
import com.example.fooddelivery.model.enums.OrderStatus;
import com.example.fooddelivery.repository.CartItemRepository;
import com.example.fooddelivery.repository.CustomerRepository;
import com.example.fooddelivery.repository.DriverRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MEMBER 5 - business logic for the Order module.
 * The most important OOP point: the rules (totals, status changes, driver
 * assignment) are METHODS on the Order object, not scattered in the controller.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartItemRepository cartItemRepository,
                            CustomerRepository customerRepository,
                            DriverRepository driverRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", id));
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByIdDesc(customerId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findByDriver(Long driverId) {
        return orderRepository.findByDriverId(driverId);
    }

    @Override
    public List<Order> findActiveOrders() {
        List<Order> active = new ArrayList<>();
        for (Order order : findAll()) {
            if (order.isActive()) {
                active.add(order);
            }
        }
        return active;
    }

    /** CHECKOUT: cart items become order items, the cart is emptied. */
    @Override
    @Transactional
    public Order placeOrderFromCart(Long customerId, String deliveryAddress, String notes) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> ResourceNotFoundException.of("Customer", customerId));
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty - add some food first!");
        }

        Order order = new Order(customer, cartItems.get(0).getMenuItem().getRestaurant(), deliveryAddress);
        order.setNotes(notes);
        order.setPlacedAt(LocalDateTime.now());
        order.updateStatus(OrderStatus.PLACED);

        for (CartItem cartItem : cartItems) {
            order.addItem(new OrderItem(cartItem.getMenuItem(), cartItem.getQuantity()));
        }

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByCustomerId(customerId);
        return saved;
    }

    @Override
    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = findById(orderId);
        order.updateStatus(status);

        // When an order is delivered: free the driver + reward loyalty points.
        if (status == OrderStatus.DELIVERED) {
            if (order.getDriver() != null) {
                Driver driver = order.getDriver();
                driver.setStatus(DriverStatus.AVAILABLE);
                driverRepository.save(driver);
            }
            Customer customer = order.getCustomer();
            customer.addLoyaltyPoints(10);
            customerRepository.save(customer);
        }
        return orderRepository.save(order);
    }

    @Override
    public Order assignDriver(Long orderId, Long driverId) {
        Order order = findById(orderId);
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> ResourceNotFoundException.of("Driver", driverId));

        if (!driver.isAvailableForDelivery()) {
            throw new IllegalStateException(driver.getName() + " is not available right now.");
        }

        order.setDriver(driver);
        order.updateStatus(OrderStatus.OUT_FOR_DELIVERY);
        driver.setStatus(DriverStatus.ON_DELIVERY);
        driverRepository.save(driver);
        return orderRepository.save(order);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order cancel(Long orderId) {
        Order order = findById(orderId);
        if (!order.isActive()) {
            throw new IllegalStateException("Only active orders can be cancelled.");
        }
        order.updateStatus(OrderStatus.CANCELLED);
        if (order.getDriver() != null) {
            Driver driver = order.getDriver();
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Order", id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public BigDecimal totalSalesOfDeliveredOrders() {
        BigDecimal total = BigDecimal.ZERO;
        for (Order order : orderRepository.findByStatus(OrderStatus.DELIVERED)) {
            total = total.add(order.getTotalAmount());
        }
        return total;
    }
}

```

### `src/main/java/com/example/fooddelivery/controller/OrderController.java`
```java
package com.example.fooddelivery.controller;

import com.example.fooddelivery.common.SessionHelper;
import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.enums.OrderStatus;
import com.example.fooddelivery.service.CustomerService;
import com.example.fooddelivery.service.DriverService;
import com.example.fooddelivery.service.MenuItemService;
import com.example.fooddelivery.service.OrderService;
import com.example.fooddelivery.service.PaymentService;
import com.example.fooddelivery.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MEMBER 5 - ORDER MODULE
 * CREATE -> GET /orders/new + POST /orders
 * READ   -> GET /orders (all + filter by status), GET /orders/{id}, GET /orders/my
 * UPDATE -> GET /orders/{id}/edit + POST /orders/{id}  (+ quick status / driver buttons)
 * DELETE -> POST /orders/{id}/delete
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final DriverService driverService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           RestaurantService restaurantService,
                           MenuItemService menuItemService,
                           DriverService driverService,
                           PaymentService paymentService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.driverService = driverService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) OrderStatus status, Model model) {
        if (status != null) {
            model.addAttribute("orders", orderService.findByStatus(status));
        } else {
            model.addAttribute("orders", orderService.findAll());
        }
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/list";
    }

    /** A customer's own order history. */
    @GetMapping("/my")
    public String myOrders(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long customerId = SessionHelper.getCustomerId(session);
        if (customerId == null) {
            redirectAttributes.addFlashAttribute("error", "Please sign in to see your orders.");
            return "redirect:/login";
        }
        model.addAttribute("orders", orderService.findByCustomer(customerId));
        model.addAttribute("customer", customerService.findById(customerId));
        return "orders/my-orders";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("restaurants", restaurantService.findAll());
        model.addAttribute("menuItems", menuItemService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/form";
    }

    @PostMapping
    public String create(@RequestParam Long customerId,
                         @RequestParam Long restaurantId,
                         @RequestParam Long menuItemId,
                         @RequestParam(defaultValue = "1") int quantity,
                         @RequestParam String deliveryAddress,
                         @RequestParam(required = false) String notes,
                         RedirectAttributes redirectAttributes) {
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Delivery address is required.");
            return "redirect:/orders/new";
        }
        try {
            var customer = customerService.findById(customerId);
            var restaurant = restaurantService.findById(restaurantId);
            var menuItem = menuItemService.findById(menuItemId);
            if (menuItem.getRestaurant() == null
                    || !menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalStateException("The selected dish does not belong to the selected restaurant.");
            }
            Order order = new Order(customer, restaurant, deliveryAddress);
            order.setNotes(notes);
            order.addItem(new OrderItem(menuItem, quantity));
            Order saved = orderService.save(order);
            redirectAttributes.addFlashAttribute("success", "Order " + saved.getOrderNumber() + " created.");
            return "redirect:/orders/" + saved.getId();
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders/new";
        }
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("drivers", driverService.findAvailable());
        model.addAttribute("payment", paymentService.findByOrder(id).orElse(null));
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("drivers", driverService.findAll());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("restaurants", restaurantService.findAll());
        return "orders/form-edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String deliveryAddress,
                         @RequestParam(required = false) String notes,
                         @RequestParam OrderStatus status,
                         RedirectAttributes redirectAttributes) {
        var order = orderService.findById(id);
        order.setDeliveryAddress(deliveryAddress);
        order.setNotes(notes);
        orderService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Order updated.");
        return "redirect:/orders/" + id;
    }

    // ---------------- quick actions ----------------
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Order status changed to " + status.getLabel() + ".");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/assign-driver")
    public String assignDriver(@PathVariable Long id,
                               @RequestParam Long driverId,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.assignDriver(id, driverId);
            redirectAttributes.addFlashAttribute("success", "Driver assigned - order is out for delivery.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancel(id);
            redirectAttributes.addFlashAttribute("success", "Order cancelled.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        orderService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Order deleted.");
        return "redirect:/orders";
    }
}

```