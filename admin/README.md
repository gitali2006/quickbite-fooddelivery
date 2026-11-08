# QuickBite — MEMBER 6 PACKAGE

## Your module: Payment, Driver & Review Management

You own the files in **`to-copy-into-project/`** (backend + frontend of this module).

`team-shared-files/` contains the foundation the whole team shares — read it, but don't edit it alone.

> The complete runnable project (all 6 modules) is in `quickbite-food-delivery-system.zip`.

---

## 1. Your backend files (Java / Spring Boot)

- `src/main/java/com/example/fooddelivery/model/Payment.java`
- `src/main/java/com/example/fooddelivery/model/Driver.java`
- `src/main/java/com/example/fooddelivery/model/Review.java`
- `src/main/java/com/example/fooddelivery/model/enums/PaymentMethod.java`
- `src/main/java/com/example/fooddelivery/model/enums/PaymentStatus.java`
- `src/main/java/com/example/fooddelivery/model/enums/DriverStatus.java`
- `src/main/java/com/example/fooddelivery/repository/PaymentRepository.java`
- `src/main/java/com/example/fooddelivery/repository/DriverRepository.java`
- `src/main/java/com/example/fooddelivery/repository/ReviewRepository.java`
- `src/main/java/com/example/fooddelivery/service/PaymentService.java`
- `src/main/java/com/example/fooddelivery/service/DriverService.java`
- `src/main/java/com/example/fooddelivery/service/ReviewService.java`
- `src/main/java/com/example/fooddelivery/service/impl/PaymentServiceImpl.java`
- `src/main/java/com/example/fooddelivery/service/impl/DriverServiceImpl.java`
- `src/main/java/com/example/fooddelivery/service/impl/ReviewServiceImpl.java`
- `src/main/java/com/example/fooddelivery/controller/PaymentController.java`
- `src/main/java/com/example/fooddelivery/controller/DriverController.java`
- `src/main/java/com/example/fooddelivery/controller/ReviewController.java`
- `src/main/java/com/example/fooddelivery/controller/CustomerPaymentController.java`
- `src/main/java/com/example/fooddelivery/controller/CustomerReviewController.java`

## 2. Your frontend files (HTML + CSS)

- `src/main/resources/templates/payments/list.html`
- `src/main/resources/templates/payments/view.html`
- `src/main/resources/templates/payments/form.html`
- `src/main/resources/templates/payments/form-edit.html`
- `src/main/resources/templates/drivers/list.html`
- `src/main/resources/templates/drivers/view.html`
- `src/main/resources/templates/drivers/form.html`
- `src/main/resources/templates/reviews/list.html`
- `src/main/resources/templates/reviews/form.html`
- `src/main/resources/templates/reviews/form-edit.html`
- `src/main/resources/templates/customer/payments.html`
- `src/main/resources/templates/customer/reviews.html`
- `src/main/resources/templates/customer/review-form.html`

Your CSS lives in the shared `style.css` — the classes used by your pages: `.status-pending/paid/failed/refunded/available/on-delivery/offline`, `.kv`, `.table`.

## 3. Your routes (URLs you implemented)

| Method | URL | What it does |
|---|---|---|
| GET | `/admin/payments` | Payment list + filter + revenue card (READ) |
| GET | `/admin/payments/new` | Create form (CREATE) |
| POST | `/admin/payments` | Create payment for an order – amount taken automatically (CREATE) |
| GET | `/admin/payments/{id}` | Payment detail + business actions (READ one) |
| GET | `/admin/payments/{id}/edit` | Edit form (UPDATE) |
| POST | `/admin/payments/{id}` | Save amount + method (UPDATE) |
| POST | `/admin/payments/{id}/pay|failed|refund` | Mark PAID / FAILED / REFUNDED (rules enforced) |
| POST | `/admin/payments/{id}/delete` | Delete payment (DELETE) |
| GET | `/customer/payments` | CUSTOMER PAYMENT PORTAL: my payments + pending count (READ) |
| POST | `/customer/payments/{id}/pay` | PAY NOW: pays MY pending payment - ownership checked (UPDATE) |
| GET | `/admin/drivers` | Fleet list + search (READ) |
| GET / POST | `/admin/drivers/new, /admin/drivers` | Add driver (CREATE) |
| GET | `/admin/drivers/{id}` | Profile + assigned deliveries (READ one) |
| GET / POST | `/admin/drivers/{id}/edit, /admin/drivers/{id}` | Edit driver (UPDATE) |
| POST | `/admin/drivers/{id}/status` | Available / On delivery / Offline (UPDATE) |
| POST | `/admin/drivers/{id}/delete` | Remove driver (DELETE) |
| GET | `/admin/reviews` | All reviews, filter by restaurant (READ) |
| GET / POST | `/admin/reviews/new, /admin/reviews` | Write a review 1-5 stars (CREATE) |
| GET / POST | `/admin/reviews/{id}/edit, /admin/reviews/{id}` | Edit review (UPDATE) |
| POST | `/admin/reviews/{id}/delete` | Delete review (DELETE) |
| GET | `/customer/reviews` | MY reviews (READ) - ownership checked |
| GET / POST | `/customer/reviews/new, /customer/reviews` | Write MY review 1-5 stars (CREATE) |
| GET / POST | `/customer/reviews/{id}/edit, /customer/reviews/{id}` | Edit MY review (UPDATE) |
| POST | `/customer/reviews/{id}/delete` | Delete MY review (DELETE) |

## 4. Your CRUD checklist

| Operation | You implemented |
|---|---|
| **Create** | payments for orders, drivers, reviews (three CRUDs, same pattern) |
| **Read** | status filters, revenue total, driver profiles with deliveries, restaurant reviews |
| **Update** | amount/method, driver details + availability, review rating/comment |
| **Delete** | payment record, driver, review – saving/deleting a review recalculates the restaurant rating |

## 5. OOP concepts you present in the viva

- **Multi-level inheritance** – BaseEntity -> Person -> Driver (the Customer branch sits beside it)
- **Polymorphism** – getRole() returns "DRIVER" vs "CUSTOMER"; getDisplayName() differs per class
- **Encapsulation + business rules** – markPaid() refuses double payment, refund() only works on a PAID payment, isAvailableForDelivery() hides the rule
- **Enums** – PaymentMethod, PaymentStatus, DriverStatus

## 6. Live demo script (5 steps)

1. As the CUSTOMER: open the Payment portal (/customer/payments), take a Cash-on-delivery order -> it is PENDING -> click PAY NOW -> it becomes PAID (portal=Member 6); try paying again -> blocked (rule)
2. As the ADMIN: open Payments (/admin/payments): show the revenue card and the filter; open a PAID payment -> Refund -> try Mark as PAID again -> blocked (rule)
3. Open Drivers: add a driver (CREATE), change status to Offline, open the profile to show deliveries (READ/UPDATE)
4. As the CUSTOMER: open My reviews -> write a 5-star review for a restaurant (CREATE)
5. Show the restaurant page – its average rating changed automatically; then edit and delete the review (UPDATE/DELETE)

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
