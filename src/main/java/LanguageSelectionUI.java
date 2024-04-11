import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.sql.*;

public class LanguageSelectionUI extends Application {
    private ComboBox<String> languageComboBox;
    private Label firstNameLabel;
    private Label lastNameLabel;
    private Label emailLabel;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField emailTextField;
    private Button saveButton;


    // Language specific labels
    private String[] enLabels = {"First Name:", "Last Name:", "Email:", "Save"};
    private String[] faLabels = {"نام:", "نام خانوادگی:", "ایمیل:", "ذخیره"};
    private String[] jaLabels = {"名前:", "姓:", "メール:", "保存"};

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Language Selection");

        // Language selection combo box
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Farsi", "Japanese");
        languageComboBox.setOnAction(e -> updateLabels());

        // Labels and text fields
        firstNameLabel = new Label(enLabels[0]);
        firstNameTextField = new TextField();
        lastNameLabel = new Label(enLabels[1]);
        lastNameTextField = new TextField();
        emailLabel = new Label(enLabels[2]);
        emailTextField = new TextField();

        // Save button
        saveButton = new Button(enLabels[3]);
        saveButton.setOnAction(e -> saveData());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Select Language:"), 0, 0);
        grid.add(languageComboBox, 1, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameTextField, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameTextField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailTextField, 1, 3);
        grid.add(saveButton, 1, 4);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLabels() {
        int selectedIndex = languageComboBox.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case 0: // English
                firstNameLabel.setText(enLabels[0]);
                lastNameLabel.setText(enLabels[1]);
                emailLabel.setText(enLabels[2]);
                saveButton.setText(enLabels[3]);
                break;
            case 1: // Farsi
                firstNameLabel.setText(faLabels[0]);
                lastNameLabel.setText(faLabels[1]);
                emailLabel.setText(faLabels[2]);
                saveButton.setText(faLabels[3]);
                break;
            case 2: // Japanese
                firstNameLabel.setText(jaLabels[0]);
                lastNameLabel.setText(jaLabels[1]);
                emailLabel.setText(jaLabels[2]);
                saveButton.setText(jaLabels[3]);
                break;
        }
    }

    private void saveData() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();

        try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/demofx?user=javafxuser&password=password123");) {
            String languageTable = "";
            switch (languageComboBox.getSelectionModel().getSelectedIndex()) {
                case 0:
                    languageTable = "employee_en";
                    break;
                case 1:
                    languageTable = "employee_fa";
                    break;
                case 2:
                    languageTable = "employee_ja";
                    break;
            }

            // Insert data into the selected language table
            String insertQuery = "INSERT INTO " + languageTable + " (first_name, last_name, email) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Data saved successfully.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while saving data: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
