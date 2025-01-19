import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CafeMochaSystem {

    private List<Customer> customers;
    private List<Product> products;
    private List<Order> orders;
    private Scanner scanner;
    private FileHandler fileHandler;

    public CafeMochaSystem() {
        this.customers = new ArrayList<>();
        this.products = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        // Use absolute or relative paths as per your requirement
        this.fileHandler = new FileHandler("D:\\Mocha\\cus.txt",
                                           "D:\\Mocha\\order.txt",
                                           "D:\\Mocha\\bill.txt",
                                           "D:\\Mocha\\product.txt");  // Add path for products

        loadExistingData();
    }

    private void loadExistingData() {
        // Load customers from file
        List<String> customerData = fileHandler.readCustomers();
        if (customerData != null) {
            for (String line : customerData) {
                String[] parts = line.split(",");
                int customerID = Integer.parseInt(parts[0].trim());
                String customerName = parts[1].trim();
                customers.add(new Customer(customerID, customerName));
            }
        }

        // Load products from file
        List<String> productData = fileHandler.readProducts();
        if (productData != null) {
            for (String line : productData) {
                String[] parts = line.split(",");
                int productID = Integer.parseInt(parts[0].trim());
                String productName = parts[1].trim();
                double productPrice = Double.parseDouble(parts[2].trim());
                String productCategory = parts[3].trim();
                products.add(new Product(productID, productName, productPrice, productCategory));
            }
        }
    }

    public void run() {
        while (true) {
            displayLoginScreen();
            String password = scanner.nextLine();
            if (password.equals("admin")) {
                break;
            } else {
                System.out.println("Invalid password. Please try again.");
            }
        }

        while (true) {
            displayAdminMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over

            switch (choice) {
                case 1:
                    createCustomer(scanner);
                    break;
                case 2:
                    addProduct(scanner);
                    break;
                case 3:
                    createOrder(scanner);
                    break;
                case 4:
                    displayOrders();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayLoginScreen() {
        System.out.println("==============================================");
        System.out.println("|           Café Mocha System - Admin Login  |");
        System.out.println("==============================================");
        System.out.print("| Enter password: ");
    }

    private void displayAdminMenu() {
        System.out.println("==============================================");
        System.out.println("|               Café Mocha System            |");
        System.out.println("|                  Admin Menu                |");
        System.out.println("==============================================");
        System.out.println("| 1. Create customer                         |");
        System.out.println("| 2. Add product                             |");
        System.out.println("| 3. Create order                            |");
        System.out.println("| 4. Display orders                          |");
        System.out.println("| 5. Exit                                    |");
        System.out.println("==============================================");
        System.out.print("| Enter your choice: ");
    }

    private void createCustomer(Scanner scanner) {
        System.out.println("==============================================");
        System.out.println("|             Create New Customer            |");
        System.out.println("==============================================");
        System.out.print("| Enter customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over
        System.out.print("| Enter customer name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer(customerID, customerName);
        customers.add(customer);
        System.out.println("| Customer created successfully!");
        System.out.println("==============================================");

        // Save customer to file
        fileHandler.saveCustomer(customerID + ", " + customerName);
    }

    private void addProduct(Scanner scanner) {
        System.out.println("==============================================");
        System.out.println("|                Add New Product             |");
        System.out.println("==============================================");
        System.out.print("| Enter product ID: ");
        int productID = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over
        System.out.print("| Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("| Enter product price: ");
        double productPrice = scanner.nextDouble();
        scanner.nextLine(); // consume newline left-over
        System.out.print("| Enter product category: ");
        String productCategory = scanner.nextLine();
        Product product = new Product(productID, productName, productPrice, productCategory);
        products.add(product);
        System.out.println("| Product added successfully!");
        System.out.println("==============================================");

        // Save product to file
        fileHandler.saveProduct(productID + ", " + productName + ", " + productPrice + ", " + productCategory);
    }

    private void createOrder(Scanner scanner) {
        System.out.println("==============================================");
        System.out.println("|                Create New Order            |");
        System.out.println("==============================================");
        System.out.print("| Enter customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over
        Customer customer = getCustomer(customerID);
        if (customer == null) {
            System.out.println("| Customer not found. Please try again.");
            System.out.println("==============================================");
            return;
        }

        List<Product> orderProducts = new ArrayList<>();
        while (true) {
            System.out.println("| Products:");
            for (int i = 0; i < products.size(); i++) {
                System.out.printf("| %d. %s - $%.2f\n", (i + 1), products.get(i).getProductName(), products.get(i).getProductPrice());
            }
            System.out.print("| Enter product ID to add to order (or 'd' to finish): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("d")) {
                break;
            }
            int productID = Integer.parseInt(input);
            Product product = getProduct(productID);
            if (product != null) {
                orderProducts.add(product);
                System.out.println("| Product added to order.");
            } else {
                System.out.println("| Product not found. Please try again.");
            }
        }

        double totalBill = 0.0;
        for (Product product : orderProducts) {
            totalBill += product.getProductPrice();
        }

        System.out.println("==============================================");
        System.out.println("|                  Order Details             |");
        System.out.println("==============================================");
        System.out.println("| Customer Name: " + customer.getCustomerName());
        System.out.println("| Order products:");
        for (Product product : orderProducts) {
            System.out.printf("| %-20s $%-10.2f\n", product.getProductName(), product.getProductPrice());
        }
        System.out.printf("| Total Bill: $%.2f\n", totalBill);
        System.out.println("==============================================");

        Order order = new Order(customer, orderProducts);
        orders.add(order);
        System.out.println("| Order created successfully!");
        System.out.println("==============================================");

        // Save order and bill to files
        fileHandler.saveOrder(order.getOrderDetails());
        fileHandler.saveBill(order.getOrderDetails());
    }

    private void displayOrders() {
        System.out.println("==============================================");
        System.out.println("|                  Orders                    |");
        System.out.println("==============================================");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("| " + (i + 1) + ". " + orders.get(i).getCustomer().getCustomerName() + " - " + orders.get(i).getOrderDetails());
        }
        System.out.println("==============================================");
    }

    private Customer getCustomer(int customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID() == customerID) {
                return customer;
            }
        }
        return null;
    }

    private Product getProduct(int productID) {
        for (Product product : products) {
            if (product.getProductID() == productID) {
                return product;
            }
        }
        return null;
    }

    class Customer {
        private int customerID;
        private String customerName;

        public Customer(int customerID, String customerName) {
            this.customerID = customerID;
            this.customerName = customerName;
        }

        public int getCustomerID() {
            return customerID;
        }

        public String getCustomerName() {
            return customerName;
        }
    }

    class Product {
        private int productID;
        private String productName;
        private double productPrice;
        private String productCategory;

        public Product(int productID, String productName, double productPrice, String productCategory) {
            this.productID = productID;
            this.productName = productName;
            this.productPrice = productPrice;
            this.productCategory = productCategory;
        }

        public int getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public String getProductCategory() {
            return productCategory;
        }
    }

    class Order {
        private Customer customer;
        private List<Product> orderProducts;

        public Order(Customer customer, List<Product> orderProducts) {
            this.customer = customer;
            this.orderProducts = orderProducts;
        }

        public Customer getCustomer() {
            return customer;
        }

        public String getOrderDetails() {
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("==============================================\n");
            orderDetails.append("               Café Mocha Receipt             \n");
            orderDetails.append("==============================================\n");
            orderDetails.append("Customer Name: ").append(customer.getCustomerName()).append("\n");
            orderDetails.append("Order Details:\n");
            for (Product product : orderProducts) {
                orderDetails.append(String.format("%-20s $%-10.2f\n", product.getProductName(), product.getProductPrice()));
            }
            orderDetails.append("==============================================\n");
            orderDetails.append(String.format("Total Bill: $%-10.2f\n", getTotalBill()));
            orderDetails.append("==============================================\n");
            orderDetails.append("Thank you for shopping at Café Mocha!\n");
            orderDetails.append("==============================================\n");
            return orderDetails.toString();
        }

        private double getTotalBill() {
            double totalBill = 0.0;
            for (Product product : orderProducts) {
                totalBill += product.getProductPrice();
            }
            return totalBill;
        }
    }

    public static void main(String[] args) {
        CafeMochaSystem system = new CafeMochaSystem();
        system.run();
    }
}
