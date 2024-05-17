package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;

import java.util.function.Consumer;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;

    public LoginForm(Stage stage, MainApp mainApp) { // Pass MainApp instance to constructor
        this.stage = stage;
        this.mainApp = mainApp; // Store MainApp instance
    }

    private Scene createLoginForm() {
        // Implementasi method untuk menampilkan komponen form login
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label welcomeLabel = new Label("Welcome to DepeFood");
        welcomeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        grid.add(welcomeLabel, 0, 0, 2, 1);
        GridPane.setHalignment(welcomeLabel, HPos.CENTER);

        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, 1);

        nameInput = new TextField();
        grid.add(nameInput, 1, 1);

        Label phoneLabel = new Label("Phone Number:");
        grid.add(phoneLabel, 0, 2);

        phoneInput = new TextField();
        grid.add(phoneInput, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());
        grid.add(loginButton, 1, 3);

        return new Scene(grid, 400, 600);
    }


    private void handleLogin() {
        // Implementasi validasi isian form login
        String name = nameInput.getText();
        String phone = phoneInput.getText();

        User user = DepeFood.handleLogin(name, phone);
        // Ganti scene ke menu yang sesuai dengan role
        if (user != null) {
            if (user.getRole().equals("Admin")) {
                Scene adminMenu = new AdminMenu(stage, mainApp, user).getScene();
                // Save scene admin menu untuk kembali
                mainApp.addScene("Admin Menu", adminMenu);
                stage.setScene(adminMenu);
            } else if (user.getRole().equals("Customer")) {
                Scene customerMenu = new CustomerMenu(stage, mainApp, user).getScene();
                //SAve scene customer menu untuk kembali
                mainApp.addScene("Customer Menu", customerMenu);
                stage.setScene(customerMenu);
            }
        } else {
            // Jika user tidak ditemukan, tampilkan pesan error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setContentText("User not found!");
            alert.showAndWait();
        }
    }

    public Scene getScene(){
        return this.createLoginForm();
    }

}
