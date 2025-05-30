# Inventory Management App

A JavaFX-based desktop application for managing inventory, users, suppliers, and sales in a retail or warehouse environment.

## Features

- **User Authentication**: Secure login and registration with password hashing (bcrypt). User roles include SuperAdmin, Admin, Assistant, Customer, and NotApproved.
- **Role-Based Access Control**: Admin and SuperAdmin have access to management features; other roles have restricted access.
- **Product Management**: Add, update, delete, and view products. Each product has a barcode, name, sale price, buy price, quantity, supplier, and image.
- **Supplier Management**: Manage suppliers and associate them with products.
- **Sales Management**: Record sales, manage sale items, and track sales history.
- **Reports**: Generate and export sales reports (daily, weekly, monthly, custom range) to CSV.
- **User Management**: Admins can view and manage users.
- **Settings**: Users can update their profile, including password and contact information.
- **Responsive UI**: JavaFX FXML-based views with a modern, responsive layout.

## Project Structure

```
src/
  main/
    java/
      com/iyed_houhou/inventoryManagementApp/
        App.java
        Main.java
        config/
        controllers/
        customFxmlNodes/
        DAOs/
        managers/
        models/
        services/
        utils/
    resources/
      com/iyed_houhou/inventoryManagementApp/
        images/
        styles/
        view/
      META-INF/
        MANIFEST.MF
pom.xml
```

- **config/**: Application configuration and database setup.
- **controllers/**: JavaFX controllers for each page/view.
- **customFxmlNodes/**: Custom JavaFX UI components.
- **DAOs/**: Data Access Objects for database operations.
- **managers/**: Singleton managers for lists of products, users, suppliers, etc.
- **models/**: Data models (Product, User, Supplier, Sale, etc.).
- **services/**: Business logic and service layer.
- **utils/**: Utility classes (UI helpers, session management, validation, etc.).
- **resources/**: FXML views, stylesheets, images, and manifest.

## Getting Started

### Prerequisites

- Java 23 or later
- Maven 3.6+
- JavaFX SDK 23.0.1 (dependencies managed via Maven)

### Build & Run

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd inventoryManagmentApp
   ```

2. **Build the project:**
   ```sh
   ./mvnw clean package
   ```

3. **Run the application:**
   ```sh
   ./mvnw javafx:run
   ```
   Or run the generated JAR:
   ```sh
   java -jar target/InventoryManagementApp-1.0-SNAPSHOT-shaded.jar
   ```

### Database

- Uses SQLite (file-based, created automatically if not present).
- No manual setup required; tables are created on first run.

## Usage

- **Login/Register**: Start the app and register a new user or login with existing credentials.
- **Dashboard**: Overview of inventory and sales.
- **Products**: Add, edit, or remove products. Assign suppliers.
- **Suppliers**: Manage supplier information.
- **Sales**: Record new sales and view sales history.
- **Reports**: Generate and export sales reports.
- **Admin**: (Admin/SuperAdmin only) Manage users and roles.
- **Settings**: Update your profile and password.

## Technologies Used

- Java 23
- JavaFX 23.0.1
- SQLite (via [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc))
- SLF4J for logging
- BCrypt for password hashing
- Maven for build and dependency management

## Customization

- **Styles**: Modify CSS files in `src/main/resources/com/iyed_houhou/inventoryManagementApp/styles/`.
- **Views**: FXML files in `src/main/resources/com/iyed_houhou/inventoryManagementApp/view/`.

## Contributing

Pull requests are welcome! Please open an issue first to discuss major changes.

## License

This project is licensed under the MIT License.

---

**Main entry point:** [`Main.java`](src/main/java/com/iyed_houhou/inventoryManagementApp/Main.java)  
**App launcher:** [`App.java`](src/main/java/com/iyed_houhou/inventoryManagementApp/App.java)

For more details, see the code in the respective folders and packages.