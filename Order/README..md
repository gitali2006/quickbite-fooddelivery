# QuickBite — MEMBER 5 PACKAGE

## Your module: Order Management & Delivery Workflow

You own the files in **`to-copy-into-project/`** (backend + frontend of this module).

`team-shared-files/` contains the foundation the whole team shares — read it, but don't edit it alone.

> The complete runnable project (all 6 modules) is in `quickbite-food-delivery-system.zip`.

---

## 1. Your backend files (Java / Spring Boot)

- `src/main/java/com/example/fooddelivery/model/Order.java`
- `src/main/java/com/example/fooddelivery/model/OrderItem.java`
- `src/main/java/com/example/fooddelivery/model/enums/OrderStatus.java`
- `src/main/java/com/example/fooddelivery/repository/OrderRepository.java`
- `src/main/java/com/example/fooddelivery/repository/OrderItemRepository.java`
- `src/main/java/com/example/fooddelivery/service/OrderService.java`
- `src/main/java/com/example/fooddelivery/service/impl/OrderServiceImpl.java`
- `src/main/java/com/example/fooddelivery/controller/OrderController.java`
- `src/main/java/com/example/fooddelivery/controller/CustomerOrderController.java`

## 2. Your frontend files (HTML + CSS)

- `src/main/resources/templates/orders/list.html`
- `src/main/resources/templates/orders/form.html`
- `src/main/resources/templates/orders/form-edit.html`
- `src/main/resources/templates/orders/view.html`
- `src/main/resources/templates/customer/orders.html`
- `src/main/resources/templates/customer/order-detail.html`

Your CSS lives in the shared `style.css` — the classes used by your pages: `.status-placed/confirmed/preparing/out-for-delivery/delivered/cancelled`, `.stats-grid`, `.table`.

## 3. Your routes (URLs you implemented)

| Method | URL | What it does |
|---|---|---|
| GET | `/admin/orders` | All orders, filter by status (READ) |
| GET | `/admin/orders/new` | Staff "create order directly" form (CREATE) |
| POST | `/admin/orders` | Create order + items, total recalculated (CREATE) |
| GET | `/admin/orders/{id}` | Order tracking page: items, driver, payment (READ one) |
| GET | `/admin/orders/{id}/edit` | Edit form (UPDATE) |
| POST | `/admin/orders/{id}` | Save delivery details + status (UPDATE) |
| POST | `/admin/orders/{id}/status` | Quick status change (workflow) |
| POST | `/admin/orders/{id}/assign-driver` | Assign an available driver |
| POST | `/admin/orders/{id}/cancel` | Cancel an active order |
| POST | `/admin/orders/{id}/delete` | Delete order (DELETE) |
| GET | `/customer/orders` | MY personal order history (READ) - ownership checked |
| GET | `/customer/orders/{id}` | Track one of MY orders (READ one) - ownership checked |
| POST | `/customer/orders/{id}/cancel` | Cancel one of MY active orders (UPDATE) |

## 4. Your CRUD checklist

| Operation | You implemented |
|---|---|
| **Create** | from the cart at checkout, or directly by staff |
| **Read** | list + status filter, tracking page, customer history |
| **Update** | delivery details, status workflow, driver assignment, cancel |
| **Delete** | remove order (its OrderItems are removed with it – composition) |

## 5. OOP concepts you present in the viva

- **Composition (strong has-a)** – Order owns its OrderItems (cascade = ALL, orphanRemoval = true)
- **Encapsulation** – addItem()/removeItem()/recalculateTotal()/updateStatus() protect the data
- **Enum type safety** – OrderStatus makes an invalid status impossible
- **Polymorphism** – Order.getDisplayName() and OrderItem.getDisplayName() differ; totals computed per line

## 6. Live demo script (5 steps)

1. As the CUSTOMER: place an order from the cart (Member 4 demo ends here), then open My orders -> the tracking page (your own history!)
2. As the ADMIN: open /admin/orders and use the status filter dropdown (READ)
3. On the admin order page: change status Placed -> Confirmed -> Preparing (UPDATE)
4. Assign an available driver -> order becomes "Out for delivery", driver becomes busy
5. Set Delivered -> driver is freed + customer earns 10 loyalty points (check the driver page); back as the customer, try to open ANOTHER customer's order URL -> blocked (ownership rule)

## 7. How to use this package

1. **Study / submit:** read `to-copy-into-project/` top to bottom — it is exactly what you wrote.
2. **Integrate:** the full project is in `quickbite-food-delivery-system.zip`. Copy
   `to-copy-into-project/src/...` over the same paths inside the project folder
   (or the other way round if your copy is the master).
3. **Run:** open the project in IntelliJ (JDK 17 or 21!), run `FoodDeliveryApplication`,
   then open http://localhost:8080 and walk through your routes above.
   (No MySQL handy? `mvnw spring-boot:run -Dspring-boot.run.profiles=dev` runs everything on H2.)
4. For an appendix/print version of your whole module in ONE document, use the matching
   `-ALL-IN-ONE.md` file next to the zip.

## 8. The ONE login page (v2), the CUSTOMER sign-up page + demo accounts

**http://localhost:8080/register** = public **customer sign-up** (name, e-mail, phone,
password + confirm — no delivery address here, the customer adds that later in My profile). Unique e-mail and password-match rules are checked server-side, the password is
BCrypt-hashed, and the new customer is signed in immediately. **Administrators cannot sign up** -
an existing admin creates admin accounts in `/admin/admins`.

**http://localhost:8080/login** — one page for both roles:

| Type this e-mail | Password | You are sent to |
|---|---|---|
| `admin@quickbite.lk` | `admin123` | the **ADMIN area** `/admin` |
| `kasun@example.com` | `1234` | the **CUSTOMER area** `/customer` |
| `nimali@example.com` | `1234` | the **CUSTOMER area** `/customer` |

Passwords are stored in the database as **BCrypt hashes** (`$2a$10$...`, 60 characters) — never as
plain text: creating an account calls `encoder.encode(...)`, login calls `encoder.matches(...)`.
Open `/admin/admins` (Member 1) to show the stored hash of every admin account. Opening `/admin`
while signed in as a customer (or `/customer` as an admin) is refused -> `/login?denied`.
