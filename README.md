# 💸 TIW Money Transfer Web App

## 📖 Overview
This project implements a small banking application in **two variants**:
1. A **server-rendered "pure HTML"** version.  
2. A **JavaScript-enhanced** version with dynamic content loading and client-side validation.  

Both are backed by a **relational database** and use **session-based authentication**.  

Core features include:
- Login and authentication
- Viewing account details and movements
- Creating money transfers
- Failure and confirmation pages
- Logout available from every page

---

## ✨ Features
- **Two implementations**:  
  - Pure server-rendered HTML app  
  - JavaScript-enhanced app with asynchronous updates  

- **Authentication & Session Handling**:  
  - Login page with credential validation  
  - Homepage listing authenticated user’s bank accounts  

- **Banking Operations**:  
  - Account Summary page with account details and movement history  
  - Transfer creation form (recipient user code, account code, reason, amount)  
  - Failure page with clear error reasons  
  - Confirmation page showing **pre/post balances** for both sender and receiver  

- **Optional Features**:  
  - User registration or data editing/deletion (for testing convenience only, not graded)

---

## 🛠 Tech Stack
- **Frontend**:  
  - Pure HTML pages and forms  
  - JavaScript (dynamic content, inline updates, autocomplete, client-side validation)  

- **Backend**:  
  - Handles authentication, authorization, validation, transfer execution  
  - Renders HTML or returns JSON depending on the variant  

- **Database**:  
  - Relational database with atomic transactions and rollback support  

---

## 🗂 Data Model
- **User**: first name, last name, username (transfer identifier), email, password  
- **Bank Account**: code/identifier, balance, linked to a user, maintains incoming/outgoing transfers  
- **Transfer**: date, amount, origin account, destination account  

---

## 🔄 Application Flows
1. **Login** → validate credentials → start session → Homepage  
2. **Homepage** → lists user’s accounts → links to Account Summary  
3. **Account Summary** → details + movements (sorted by descending date) → transfer form  
4. **Create Transfer**:  
   - On failure → Failure page with explanation  
   - On success → Confirmation page with updated balances  
5. **Logout** → available everywhere  

---

## 🖥 Variants

### Pure HTML Version
- Traditional multi-page navigation  
- Validation & authorization enforced **server-side**  
- Transfer execution in a single transaction  

### JavaScript Version
- Single-page host, dynamically loaded content  
- **Client-side validation** (e.g., amount > 0) + **server-side enforcement**  
- Recipient autocomplete using persisted address book  
- Inline error/success messages  

---

## 🔐 Validation & Security
- Validate all inputs on both **client** and **server**  
- Reject unauthorized or invalid requests **server-side**  
- Failure cases provide clear explanations without altering data  

---

## ⚖️ Transactions & Consistency
- Each transfer executed within a **single atomic transaction**  
- Rollback on failure → no partial updates  
- Confirmation page shows **balances before & after transfer**  
- Movements list always reflects transaction history in descending order  

---

## 📂 Project Structure & Variants
- Two separate web apps:
  - **HTML-only**  
  - **JavaScript-enhanced**  
- Shared business logic & persistence layer  
- Secure logout present on all pages  

---

## 🎬 Demo & Deliverables
- Deliver both implementations + documentation  
- Database pre-populated with **sample data** for testing  
- Online demo should show:  
  - Login / Logout  
  - Account details & movements  
  - Transfer creation (failure + success)  
  - Confirmation with updated balances  

---

## 📝 Notes for Implementation
- Inputs: recipient code, account code, reason, amount (>0)  
- Movements must include **both incoming & outgoing transfers**, sorted by date  
- Registration / extra utilities optional (not graded)  

---

## ✅ Minimal Checklist
- [x] Two distinct web apps (HTML + JS)  
- [x] Authentication, authorization, validation, and transactional integrity  
- [x] JS client-side validation + autocomplete with address-book persistence  
- [x] Database with test data  
- [x] Movements sorted by date  
- [x] Confirmation page shows pre/post balances  
- [x] Logout available everywhere  

---
