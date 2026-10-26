# QuickBite — MEMBER 4 PACKAGE

## Your module: Cart & Checkout

You own the files in **`to-copy-into-project/`** (backend + frontend of this module).

`team-shared-files/` contains the foundation the whole team shares — read it, but don't edit it alone.

> The complete runnable project (all 6 modules) is in `quickbite-food-delivery-system.zip`.

---

## 1. Your backend files (Java / Spring Boot)

- `src/main/java/com/example/fooddelivery/model/CartItem.java`
- `src/main/java/com/example/fooddelivery/repository/CartItemRepository.java`
- `src/main/java/com/example/fooddelivery/service/CartService.java`
- `src/main/java/com/example/fooddelivery/service/impl/CartServiceImpl.java`
- `src/main/java/com/example/fooddelivery/controller/CustomerCartController.java`

## 2. Your frontend files (HTML + CSS)

- `src/main/resources/templates/customer/cart.html`
- `src/main/resources/templates/customer/checkout.html`

Your CSS lives in the shared `style.css` — the classes used by your pages: `.totals .row`, `.totals .grand`, `.qty-input`, `.dish-actions`, `.empty`.

## 3. Your routes (URLs you implemented)

| Method | URL | What it does |
|---|---|---|
| GET | `/customer/cart` | Cart page: lines, subtotal, delivery fee, grand total, suggestions (READ) |
| POST | `/customer/cart/add` | Add dish to cart (CREATE) |
| POST | `/customer/cart/update` | Change quantity of a line (UPDATE) |
| POST | `/customer/cart/remove/{id}` | Remove one line (DELETE) |
| POST | `/customer/cart/clear` | Empty the whole cart (DELETE all) |
| GET | `/customer/checkout` | Checkout form: address + payment method |
| POST | `/customer/checkout` | Places the order (hand-over to Member 5) and creates the payment (Member 6) |

## 4. Your CRUD checklist

| Operation | You implemented |
|---|---|
| **Create** | add to cart; adding the same dish again increases its quantity |
| **Read** | cart page with money summary and "you may also like" suggestions |
| **Update** | quantity update per line with instant total recalculation |
| **Delete** | remove a line or clear the entire cart |

## 5. OOP concepts you present in the viva

- **Composition (has-a)** – CartItem holds a Customer AND a MenuItem
- **Encapsulation of rules** – one restaurant per cart, sold-out blocked, free delivery above Rs. 3,000 – all inside CartServiceImpl
- **Polymorphism in totals** – every line computes getLineTotal(); the cart just sums them
- **Modern collection handling** – suggestions built with streams/loops, no null leaks

## 6. Live demo script (5 steps)

1. Sign in as kasun@example.com (password 1234) -> you land on /customer
2. Open the menu and add Chicken Kottu x2 -> open My cart, show subtotal + Rs. 300 delivery fee (CREATE/READ)
3. Try adding a dish from ANOTHER restaurant -> the cart resets (business rule: one restaurant per cart)
4. Change the quantity to 3 with Update (UPDATE), then Remove a line (DELETE)
5. Checkout: fill the address, choose Cash on delivery, place the order -> you are redirected to the new order page

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
