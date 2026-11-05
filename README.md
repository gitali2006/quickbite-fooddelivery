# QuickBite Quick Start

Each folder below is ONE member's complete slice of the project (Java backend + HTML/CSS frontend of that module).

Inside every member folder:

- `to-copy-into-project/` — the files THAT MEMBER wrote (their backend + frontend)
- `team-shared-files/` — the foundation everyone shares (entities, layout, CSS, config)
- `README.md` — their file list, routes, CRUD checklist, OOP points, demo script
- `QuickBite-Member-N-<name>.zip` — the same thing zipped for WhatsApp/e-mail

| # | Folder | Module |
|---|---|---|
| 1 | `QuickBite-Mithulan*` | Customer & Account Management (+ the single sign-in page + admin accounts) |
| 2 | `QuickBite-Vidhushan-*` | Restaurant Management |
| 3 | `QuickBite-Razan-*` | Menu & Food Item Management |
| 4 | `QuickBite-Asjadh Ali-*` | Cart & Checkout |
| 5 | `QuickBite-Yaasir*` | Order Management & Delivery Workflow |
| 6 | `QuickBite-Varya-*` | Payment, Driver & Review Management |

## The whole system in 30 seconds

One **Java 17 + Spring Boot + MySQL** web app ("QuickBite", prices in Rs.). The frontend is
**HTML + CSS only** (server-rendered with Thymeleaf — zero JavaScript lines anywhere).

* **ONE sign-in page** (`/login`) for everybody: `admin@quickbite.lk` / `admin123` lands in the
  **admin area** (`/admin/**`), `kasun@example.com` / `1234` lands in the **customer area**
  (`/customer/**`). New customers can also **sign themselves up** at `/register`
  (customer accounts only). Passwords are **BCrypt-hashed in the database** (never plain text).
* **Admin area** = full CRUD over customers, restaurants, menu, orders, drivers, payments,
  reviews **and admin e-mail accounts/passwords** (`/admin/admins`) — plus a dashboard.
* **Customer area** = **menu**, **personal order history**, **cart** and a **payment portal**
  (pay now), reviews and profile.
* The six modules below each own a real entity → repository → service → controller → HTML pages,
  with their own **Create / Read / Update / Delete** and their own **OOP concepts** for the viva.

## Run it

1. Open `food-delivery-system/` (inside `quickbite-food-delivery-system.zip`) in IntelliJ as a
   **Maven project** with **JDK 17 or 21**, or use `mvnw spring-boot:run` from a terminal.
2. MySQL is configured out of the box (`application.properties`); without MySQL use
   `-Dspring-boot.run.profiles=dev` and everything runs on the bundled H2 database.
3. Open http://localhost:8080 — sign in with either demo account above.
