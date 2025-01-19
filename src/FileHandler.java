import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {

    private String customerFilePath;
    private String orderFilePath;
    private String billFilePath;
    private String productFilePath;  

    public FileHandler(String customerFilePath, String orderFilePath, String billFilePath, String productFilePath) {
        this.customerFilePath = customerFilePath;
        this.orderFilePath = orderFilePath;
        this.billFilePath = billFilePath;
        this.productFilePath = productFilePath;

        // Ensure files exist or create them
        createFileIfNotExists(customerFilePath);
        createFileIfNotExists(orderFilePath);
        createFileIfNotExists(billFilePath);
        createFileIfNotExists(productFilePath);  // Ensure product file exists
    }

    private void createFileIfNotExists(String filePath) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Create parent directories if they do not exist
            }
            if (!file.exists()) {
                file.createNewFile(); // Create the file if it doesn't exist
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + filePath);
            e.printStackTrace();
        }
    }

    public void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + filePath);
            e.printStackTrace();
        }
    }

    public List<String> readFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file: " + filePath);
            e.printStackTrace();
        }
        return null;
    }

    public void saveCustomer(String content) {
        writeToFile(customerFilePath, content);
    }

    public List<String> readCustomers() {
        return readFromFile(customerFilePath);
    }

    public void saveOrder(String content) {
        writeToFile(orderFilePath, content);
    }

    public List<String> readOrders() {
        return readFromFile(orderFilePath);
    }

    public void saveBill(String content) {
        writeToFile(billFilePath, content);
    }

    public List<String> readBills() {
        return readFromFile(billFilePath);
    }

    
    public void saveProduct(String content) {
        writeToFile(productFilePath, content);
    }

    public List<String> readProducts() {
        return readFromFile(productFilePath);
    }
}
