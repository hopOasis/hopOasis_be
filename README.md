## 📌 Features
- Product management (create, edit, delete)
- Order processing and payments
- User authentication and role management
- Integration with external APIs (e.g., payment systems)

## 🚀 Tech Stack
- **Java**
- **Spring Boot**
- **Spring Security**
- **Hibernate**
- **PostgreSQL**
- **Docker**

---

## 👷‍♀️ Local Setup Instructions

### 1. Install Java 21 via Terminal

#### Linux
```sh
sudo apt update
sudo apt install openjdk-21-jdk
java -version  # Check installation
```

#### Windows
```sh
winget install --id Oracle.JDK.21
java -version
```

#### macOS
```sh
brew install java@21
java -version
```

---

### 2. Install Maven via Terminal

#### Linux
```sh
sudo apt update
sudo apt install maven
mvn -version
```

#### Windows
```sh
winget install Apache.Maven
mvn -version
```

#### macOS
```sh
brew install maven
mvn -version
```

---

### 3. Download and install IntelliJ IDEA (Community or Ultimate)
[Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

### 4. Open IntelliJ IDEA.

### 5. Clone the repository:
```sh
git clone https://github.com/hopOasis/hopOasis_be.git
```

![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2018-50-47.png)
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2018-40-39.png)

### 6. Open the project in IntelliJ IDEA.

### 7. Fetch all remote branches.
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2018-56-02.png)

### 8. Checkout to branch `render_deploy`:
```sh
git checkout render_deploy
```
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2018-52-53.png)

### 9. Open the file `application.properties`.
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2019-06-22.png)

### 10. Ask the PM for the `application.properties` file with environment variables.
Once you receive the file:
- Open it
- Copy all the content from the PM's file
- Open your `application.properties` file
- Remove all content
- Paste the copied content

### 11. Build the project via Terminal:
```sh
mvn install -DskipTests -Dcheckstyle.skip
```
If you encounter issues on Windows, try running Maven via IntelliJ IDEA.
![Screenshot](src/main/resources/images/photo_2025-02-05_20-14-36.jpg)

![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2020-34-47.png)

### 12. Verify build success
You should see `BUILD SUCCESSFUL` in the terminal.
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2020-10-33.png)

### 13. Run the app via Terminal:
```sh
mvn spring-boot:run -DskipTests=true -Dcheckstyle.skip=true
```

### 14. Open the browser and navigate to:
[http://localhost:8080/beers](http://localhost:8080/beers)

If everything works correctly, you will see a list of beers.
![Screenshot](src/main/resources/images/Screenshot%20from%202025-02-04%2020-29-05.png)

---

## 🔄 Basic Git Workflow Rules

### 1. Creating a New Branch
- Create a new branch from `render_deploy`.
- The branch name should be descriptive and reflect the task's purpose.

### 2. Commits
- Limit commits to **5 per task**.
- Each commit should be informative and clearly describe changes.

### 3. Pull Request (PR)
- After completing a task, create a **Pull Request**.
- A PR is successful if it receives **2 approvals** (one from a developer and one from the PM).
- Approved branches are merged into `render_deploy`.

---

✅ **Happy Coding!** 🚀
