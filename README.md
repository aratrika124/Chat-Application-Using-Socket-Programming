# 💬 Chat Application Using Socket Programming (Server-Client GUI)

A **Java Swing-based Chat Application** using **Sockets** where a server can accept client connections and both can exchange real-time messages.  
The server displays the client’s username on connection and supports sending/receiving messages with proper username prefixes.

---

## 🚀 Features

- 🖥️ **Server GUI** with username setup and connection approval.
- 👤 **Client GUI** where users join with a username.
- ✅ Server can **accept or reject** incoming connections.
- ✉️ Messages are sent with **username prefixes**.
- 🔄 Supports continuous chat (single client connection at a time).
- ⚙️ Easily configurable **IP address & port**.

---

## 🛠 Requirements

- ☕ Java JDK 8 or above.
- 📝 Any IDE (IntelliJ, Eclipse, VS Code) or terminal.
- 🌐 Both Server and Client must be on the **same network** (or reachable via IP).

---

## 🏁 Getting Started

### 1️⃣ Compile the code
```bash
javac Server.java
javac Client.java
````

### 2️⃣ Run the Server

```bash
java Server
```

* Enter a **server username** in the GUI.
* Click **Start Server** → waits for client connections.

### 3️⃣ Run the Client

```bash
java Client
```

* Enter your **username** in the GUI.
* Click **Connect** → requests connection to the server.

---

## 🔧 Configuration

### Server Port

By default, the server listens on **2802**:

```java
server = new ServerSocket(2802);
```

➡️ Change the port number if needed.

### Client IP & Port

In **Client.java**, update the connection:

```java
socket = new Socket("192.168.x.x", 2802);
```

* Replace `192.168.x.x` with your **server machine’s IP**.
* Port must match the server’s port.

---

## 🌐 Finding Your IP

* **Windows**:

  ```bash
  ipconfig
  ```

  Look for **IPv4 Address**.

* **Linux/macOS**:

  ```bash
  ifconfig
  ```

  Check the active network adapter.

---

## ⚠️ Troubleshooting

1. ✅ **Correct IP Address**
   Ensure client uses the **server machine’s IP**.
   Use `127.0.0.1` if running both on the same machine.

2. 🔌 **Port Availability**

   * Default port: `2802`.
   * Ensure no firewall/antivirus blocks it.

3. 🌐 **Network**

   * Server & client must be on the **same subnet**.
     Example: `192.168.1.x`.

4. 🔥 **Firewall/Antivirus**
   Allow `java.exe` or open port **2802** in firewall settings.

5. 📡 **Ping Test**
   From client machine:

   ```bash
   ping 192.168.x.x
   ```

6. 🐛 **Debugging**
   Add logs in your code:

   ```java
   // In Server.java
   System.out.println("Server running on port 2802...");

   // In Client.java
   System.out.println("Connecting to server at 192.168.x.x:2802...");
   ```

---

## 🧩 Workflow

1. Client connects to server using IP + port.
2. Client sends its **username**.
3. Server asks user to **accept/reject connection**.
4. If accepted → chat begins.
5. Messages appear with **username prefixes**.

---

## ⚠️ Notes

* Server must **accept client manually** before chatting.
* Order of actions:

  1. Start server with username.
  2. Start client with username.
  3. Accept connection on server.
  4. Begin chatting.

---

## 👤 Author

Developed by Aratrika Samanta✨

### 🌐 Let's Connect!

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/aratrika-samanta-67a1b5268)  
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:aratrikasamanta060203@gmail.com)

