# QuickBite — MEMBER 3 PACKAGE

## Your module: Menu & Food Item Management

You own the files in **`to-copy-into-project/`** (backend + frontend of this module).

`team-shared-files/` contains the foundation the whole team shares — read it, but don't edit it alone.

> The complete runnable project (all 6 modules) is in `quickbite-food-delivery-system.zip`.

---

## 1. Your backend files (Java / Spring Boot)

- `src/main/java/com/example/fooddelivery/model/MenuItem.java`
- `src/main/java/com/example/fooddelivery/repository/MenuItemRepository.java`
- `src/main/java/com/example/fooddelivery/service/MenuItemService.java`
- `src/main/java/com/example/fooddelivery/service/impl/MenuItemServiceImpl.java`
- `src/main/java/com/example/fooddelivery/controller/MenuItemController.java`

## 2. Your frontend files (HTML + CSS)

- `src/main/resources/templates/menu/menu.html`
- `src/main/resources/templates/menu/manage.html`
- `src/main/resources/templates/menu/form.html`

Your CSS lives in the shared `style.css` — the classes used by your pages: `.dish-card`, `.price`, `.chip`, `.chip-veg`, `.qty-input`.

## 3. Your routes (URLs you implemented)

| Method | URL | What it does |
|---|---|---|
| GET | `/menu` | PUBLIC menu grouped per restaurant - guests see "Sign in to order" (READ) |
| GET | `/customer/menu` | The same page for a signed-in customer, with working Add-to-cart buttons (READ) |
| GET | `/admin/menu` | Admin table: search by name + filter by restaurant (READ) |
| GET | `/admin/menu/new` | Create form (CREATE) |
| POST | `/admin/menu` | Save dish with price validation (CREATE) |
| GET | `/admin/menu/{id}/edit` | Edit form (UPDATE) |
| POST | `/admin/menu/{id}` | Save changes (UPDATE) |
| POST | `/admin/menu/{id}/delete` | Delete dish (DELETE) |
| POST | `/admin/menu/{id}/toggle` | Sold-out / available toggle |

## 4. Your CRUD checklist

| Operation | You implemented |
|---|---|
| **Create** | name, price (BigDecimal), category, veg flag, restaurant link |
| **Read** | customer-facing menu + staff management table with search & filter |
| **Update** | edit everything, switch availability ("sold out") |
| **Delete** | removes dish; dishes used in orders are protected by the database |

## 5. OOP concepts you present in the viva

- **Association** – MenuItem @ManyToOne Restaurant (hand-over from Member 2)
- **Encapsulation** – totalFor(quantity) computes money inside the object
- **Inheritance** – extends BaseEntity
- **BigDecimal instead of double** – correct money arithmetic (no rounding errors)

## 6. Live demo script (5 steps)

1. Open Manage menu -> Add dish "Test Kottu", Rs. 999, Main, Rice & Curry House (CREATE)
2. Search "Test" in the search box and filter by restaurant (READ)
3. Open the public /menu page – the dish is there under its restaurant with an Add-to-cart button
4. Edit the price to Rs. 1099 (UPDATE)
5. Toggle it to "Sold out" -> show customers can no longer add it, then toggle back and Delete it (DELETE)

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
