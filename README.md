# Bank Management System — ATM Simulator

A desktop ATM simulator built with Core Java (Swing & AWT) that demonstrates realistic ATM workflows (authentication, withdraw, deposit, balance enquiry, PIN change, mini-statement) while persisting data in a MySQL database using JDBC. The project was developed in NetBeans and is intended as an educational, demo-ready banking application.

---

## Table of contents
- [About the project](#about-the-project)
- [Technologies used](#technologies-used)
- [Database (MySQL) & schema](#database-mysql--schema)
- [IDE (NetBeans) — how I used it](#ide-netbeans)
- [How JDBC connects the app to MySQL (concept + examples)](#how-jdbc-connects-the-app-to-mysql-concept--examples)
- [How the project works (runtime flow)](#how-the-project-works-runtime-flow)
- [Configuration & run steps (NetBeans + CLI)](#configuration--run-steps-netbeans--cli)
- [Security & best practices](#security--best-practices)
- [Project structure (typical)](#project-structure-typical)
- [Screenshots / assets](#screenshots--assets)
- [Contributing & license](#contributing--license)

---

## About the project
This Bank Management System simulates an ATM interface with a GUI built using Java Swing (and AWT where needed). It implements core banking operations, stores account and transaction data in MySQL, and uses JDBC to perform all database operations. The goal is to demonstrate GUI programming, application design (model–view–controller-ish separation), and safe database access (PreparedStatement, transactions).

---

## Technologies used
- Core Java (JDK 8+)
  - Java Swing for UI (frames, panels, dialogs)
  - AWT for some UI utilities (events, layout helpers)
- JDBC (Java Database Connectivity)
- MySQL (server) for persistence
- NetBeans IDE for development, building and packaging

---

## Database (MySQL) & schema
This project uses MySQL as the relational database backend.

Recommended MySQL connection details:
- Driver class: `com.mysql.cj.jdbc.Driver` (Connector/J 8+)
- Example JDBC URL:
  - jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC

Minimal example SQL schema (adjust types/constraints to your implementation):

```sql
-- create DB and user (run as root or a user with CREATE privileges)
CREATE DATABASE IF NOT EXISTS bankdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'bankuser'@'localhost' IDENTIFIED BY 'bankpass';
GRANT ALL PRIVILEGES ON bankdb.* TO 'bankuser'@'localhost';
FLUSH PRIVILEGES;

USE bankdb;

CREATE TABLE IF NOT EXISTS accounts (
  account_id INT AUTO_INCREMENT PRIMARY KEY,
  card_number VARCHAR(32) UNIQUE NOT NULL,
  pin VARCHAR(255) NOT NULL, -- store hashed PINs in production
  holder_name VARCHAR(100),
  balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions (
  tx_id INT AUTO_INCREMENT PRIMARY KEY,
  account_id INT NOT NULL,
  tx_type ENUM('DEPOSIT','WITHDRAW','PIN_CHANGE','OTHER') NOT NULL,
  amount DECIMAL(15,2),
  tx_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  description VARCHAR(255),
  FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);
```

Notes:
- For a demo, plain PIN storage may be used, but always prefer secure hashing (see Security section).
- Adjust decimal precision and constraints to your needs.

---

## IDE (NetBeans) — how I used it
- Project imported/created in NetBeans as a Java Application project.
- Source under `Source Packages` (NetBeans default).
- Added MySQL Connector/J JAR:
  - Right-click project → Properties → Libraries → Add JAR/Folder → select `mysql-connector-java-x.y.z.jar`.
- Run & Debug:
  - Use the green Run button to launch the project. NetBeans detects `main()` automatically (or set the run configuration to your main class).
- Build / Create executable:
  - Build Project → NetBeans produces `dist/YourProject.jar`.
  - If you added external libraries, NetBeans will create a `lib/` folder inside `dist/` with dependency JARs; run the JAR with both the main jar and libs on the classpath or use the provided script.

---

## How JDBC connects the app to MySQL — concept & code examples

Core JDBC concepts used in this project:
- Driver: the database-specific driver (Connector/J) implements JDBC.
- Connection: created with `DriverManager.getConnection(...)`.
- PreparedStatement: parameterized queries to avoid SQL injection.
- ResultSet: read SELECT query results.
- Transactions: use `setAutoCommit(false)` to make balance updates + transaction insert atomic.
- try-with-resources: always close Connection/Statement/ResultSet.

Example: establishing a connection (recommended via config):

```java
// Optional for modern drivers (Class.forName is usually not required)
Class.forName("com.mysql.cj.jdbc.Driver");

String url = "jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC";
String user = "bankuser";
String pass = "bankpass";

try (Connection conn = DriverManager.getConnection(url, user, pass)) {
    // use connection
} catch (SQLException e) {
    // handle
}
```

Example: secure account lookup (login):

```java
String sql = "SELECT account_id, balance FROM accounts WHERE card_number = ? AND pin = ?";
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, cardNumber);
    ps.setString(2, pin); // use hashed PIN in production
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            int accountId = rs.getInt("account_id");
            BigDecimal balance = rs.getBigDecimal("balance");
            // proceed
        } else {
            // invalid credentials
        }
    }
}
```

Example: withdraw operation with transaction (atomic):

```java
String updateBalanceSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
String insertTxSql = "INSERT INTO transactions (account_id, tx_type, amount, description) VALUES (?, 'WITHDRAW', ?, ?)";

try (Connection conn = DriverManager.getConnection(url, user, pass)) {
    conn.setAutoCommit(false);
    try (PreparedStatement psUpdate = conn.prepareStatement(updateBalanceSql);
         PreparedStatement psInsert = conn.prepareStatement(insertTxSql)) {

        psUpdate.setBigDecimal(1, amount);
        psUpdate.setInt(2, accountId);
        psUpdate.setBigDecimal(3, amount);
        int rows = psUpdate.executeUpdate();

        if (rows == 0) {
            conn.rollback();
            // insufficient funds or account missing — notify user
        } else {
            psInsert.setInt(1, accountId);
            psInsert.setBigDecimal(2, amount);
            psInsert.setString(3, "ATM withdrawal");
            psInsert.executeUpdate();
            conn.commit();
            // success
        }
    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
}
```

Key implementation details used across the project:
- Use PreparedStatement for all parameterized queries.
- Wrap multi-step updates within transactions to keep data consistent.
- Provide user-friendly error messages on SQL exceptions and handle connectivity failures gracefully.

---

## How the project works (runtime flow)
1. Startup
   - App reads DB config (from a `config.properties` or constants class) and initializes the GUI.
   - Optional: app checks/creates schema or shows a helpful message if DB is unreachable.

2. Login / Authentication
   - User enters card number and PIN.
   - The app validates credentials via a SELECT query.
   - On success, the session opens with account info (name, balance).

3. Main menu (ATM options)
   - Withdraw: validates amount, then performs atomic DB update + transaction record.
   - Deposit: updates balance and logs transaction.
   - Fast Cash: quick withdrawals of preset amounts.
   - Balance enquiry: reads current balance and shows it.
   - Mini-statement: shows recent transactions for the account.
   - PIN change: verifies old PIN and updates to new PIN (should hash in production).

4. Transaction logging & integrity
   - Every balance-changing action inserts a transaction row.
   - All updates use transactions to avoid partial state changes.

5. Exit
   - Session ends, GUI closes, and any open DB resources are closed automatically.

---

## Configuration & run steps (NetBeans + CLI)
1. Ensure JDK 8+ is installed:
   - java -version

2. Prepare MySQL:
   - Start MySQL server.
   - Create `bankdb`, user, and run the provided schema SQL.

3. Add MySQL connector to the project:
   - Download Connector/J (mysql-connector-java-x.y.z.jar).
   - NetBeans: Project → Properties → Libraries → Add JAR/Folder.

4. Configure DB connection:
   - Option A: config.properties (recommended)
     ```
     db.url=jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC
     db.user=bankuser
     db.password=bankpass
     ```
   - Option B: Hard-coded constants (not recommended for production)

5. Run in NetBeans:
   - Right-click project → Run.
   - If NetBeans asks for main class, set it to the class that contains `public static void main(String[] args)` (NetBeans usually detects this automatically).

6. Build distributable JAR (NetBeans):
   - Right-click project → Clean and Build.
   - `dist/YourProject.jar` and `dist/lib/` (for dependent jars) are created.
   - To run the jar:
     - If NetBeans copied dependencies into `dist/lib`:
       ```bash
       java -jar dist/YourProject.jar
       ```
     - Or explicitly set classpath including connector jar:
       ```bash
       java -cp "dist/YourProject.jar:dist/lib/*" com.yourpackage.MainClass
       ```

7. Run from command line (compile & run manually):
   - Compile:
     ```bash
     javac -d out src/**/*.java
     ```
   - Run (include connector JAR on classpath):
     ```bash
     java -cp "out:mysql-connector-java-8.0.xx.jar" com.yourpackage.MainClass
     ```

If you tell me the exact main class FQN (fully qualified name) I will add exact CLI commands and the run configuration.

---

## Security & best practices
- PIN storage: never store plain PINs in production. Use salted password hashing (bcrypt, PBKDF2). For a demo you may store plaintext or reversible encoding, but mention the risk.
- Always use PreparedStatement to avoid SQL injection.
- Always use transactions for multi-step updates.
- Keep DB credentials out of source control — use external config files or environment variables.
- Handle exceptions clearly: log details for debugging but show friendly messages to users.

---

## Project structure (typical)
Adjust to match your repository layout:

```
BankManagementSystem/
├─ src/                        # Java source packages
│  ├─ com/yourcompany/atm/
│  │   ├─ ui/                  # Swing frames / dialogs
│  │   ├─ dao/                 # JDBC DAOs (AccountDAO, TransactionDAO)
│  │   ├─ model/               # Account, Transaction classes
│  │   ├─ service/             # Business logic
│  │   └─ Main.java            # Application entry point
├─ lib/                        # External jars (mysql-connector-java.jar)
├─ sql/                        # Schema / seed data scripts
├─ assets/                     # Screenshots, demo GIFs
├─ config.properties           # DB config
└─ README.md
```

---
