# 💳 Online Banking System (Java + Swing + MySQL)

An end-to-end **GUI-based Online Banking System** developed in Java using **Swing** for the interface and **MySQL** for persistent data storage. The system supports both **Customer** and **Admin** roles with secure login, fund transactions, interest calculation, and account management.

---

## 🔧 Features

### 👤 Customer Module
- 🔐 Login with password encryption (BCrypt)
- 💰 Open Checking and Savings accounts
- 💵 Deposit and Withdraw funds
- 🔄 Transfer money between accounts
- 📜 View transaction history
- 📊 View account balance with interest (Savings)

### 🛡️ Admin Module
- 🧑‍💼 Admin login panel
- 👁️ View all customers
- 📈 View all transactions
- 💹 Apply monthly interest to all savings accounts

---

## 📸 GUI Preview

> (Add screenshots here if you want)

---

## 🛠️ Technologies Used

| Tech         | Purpose                             |
|--------------|-------------------------------------|
| Java         | Core language                       |
| Swing        | GUI interface                       |
| JDBC         | Database communication              |
| MySQL        | Persistent data storage             |
| BCrypt       | Password hashing                    |
| JUnit        | Unit testing                        |
| Java Logger  | System logs                         |

---

## 🚀 How to Run

### 1. Clone the repo
```bash
git clone https://github.com/yourusername/Online_banking_system.git
cd Online_banking_system
```

### 2. Set up the Database
Import the schema.sql file (you can create one or ask me for it)  
Create database: `banking_system`  
Insert one admin user manually (use BCrypt hash)

### 3. Add Libraries
Place the following `.jar` files in the `lib/` directory:

- `mysql-connector-java-X.X.X.jar`
- `jbcrypt-0.4.jar`

Then add them to the classpath.

### 4. Compile and Run
Using terminal:

```bash
javac -cp "lib/*" src/**/*.java
java -cp "lib/*:src" Main
```

Or run directly from IntelliJ / Eclipse.

---

## 🧪 Test Credentials

**Admin**
```
Username: admin
Password: admin123 (hashed in DB)
```

**Test Customer**  
Register through GUI or manually add via SQL.

---

## ✅ Future Enhancements

- 🌐 REST API (Spring Boot)
- 📱 Android / Web client
- ✉️ Email alerts (JavaMail)
- 📄 PDF/CSV export
- ☁️ Cloud deployment (Docker + AWS)

---

## 🧑‍💻 Author

Your Name Kavindu Mihiranga

---
