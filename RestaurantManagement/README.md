# QuickBite — MEMBER 2 PACKAGE

## Your module: Restaurant Management

You own the files in **`to-copy-into-project/`** (backend + frontend of this module).

`team-shared-files/` contains the foundation the whole team shares — read it, but don't edit it alone.

> The complete runnable project (all 6 modules) is in `quickbite-food-delivery-system.zip`.

---

## 1. Your backend files (Java / Spring Boot)

- `src/main/java/com/example/fooddelivery/model/Restaurant.java`
- `src/main/java/com/example/fooddelivery/repository/RestaurantRepository.java`
- `src/main/java/com/example/fooddelivery/service/RestaurantService.java`
- `src/main/java/com/example/fooddelivery/service/impl/RestaurantServiceImpl.java`
- `src/main/java/com/example/fooddelivery/controller/RestaurantController.java`

## 2. Your frontend files (HTML + CSS)

- `src/main/resources/templates/restaurants/list.html`
- `src/main/resources/templates/restaurants/form.html`
- `src/main/resources/templates/restaurants/view.html`

Your CSS lives in the shared `style.css` — the classes used by your pages: `.card-grid`, `.restaurant-head`, `.stars`, `.badge status-delivered/cancelled`.

## 3. Your routes (URLs you implemented)

| Method | URL | What it does |
|---|---|---|
| GET | `/admin/restaurants` | Card list with name search (READ) |
| GET | `/admin/restaurants/new` | Create form (CREATE) |
| POST | `/admin/restaurants` | Save new restaurant (CREATE) |
| GET | `/admin/restaurants/{id}` | Detail page + its whole menu (READ one) |
| GET | `/admin/restaurants/{id}/edit` | Edit form (UPDATE) |
| POST | `/admin/restaurants/{id}` | Save changes (UPDATE) |
| POST | `/admin/restaurants/{id}/delete` | Delete – blocked while menu items exist (DELETE) |
| POST | `/admin/restaurants/{id}/toggle` | Open / close the restaurant |

## 4. Your CRUD checklist

| Operation | You implemented |
|---|---|
| **Create** | add restaurant with cuisine, address, phone, rating, description |
| **Read** | searchable card list + detail page that also shows its menu |
| **Update** | edit all details, open/closed toggle |
| **Delete** | business rule: cannot delete a restaurant that still has dishes |

## 5. OOP concepts you present in the viva

- **Inheritance** – extends BaseEntity (id + createdAt/updatedAt for free)
- **Encapsulation** – getRatingStars() and getStatusLabel() keep formatting inside the object
- **Association** – one Restaurant has many MenuItems (@OneToMany, hand-over point to Member 3)
- **Business rule in service layer** – delete guarded by menu item count

## 6. Live demo script (5 steps)

1. Add restaurant "Test Bistro" (CREATE) – it appears in the list instantly
2. Open its detail page and show the (empty) menu section it owns
3. Edit it: change rating to 3.5 and save (UPDATE)
4. Try to Delete restaurant "Rice & Curry House" -> blocked: it has 12 dishes (business rule)
5. Delete "Test Bistro" instead (DELETE), then toggle another restaurant Closed/Open

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
