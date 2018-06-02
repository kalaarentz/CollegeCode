package DrugzLLC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Optional;

public class Main extends Application {

    // omg singleton :(
    private static Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Connect to the database
        connection = JDBCTools.connect("jdbc:sqlite:Pharmacy.db");

        setUpWindow(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setUpWindow(Stage primaryStage) {
        primaryStage.setTitle("Drugz LLC");
        primaryStage.getIcons().add(new Image("file:src/DrugzLLC/Images/DrugzIcon.png"));
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(primaryStage);
        });
    }

    private void closeProgram(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you would like to exit Drugz LLC?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            JDBCTools.disconnect(connection);
            primaryStage.close();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void showErrorAlertDialog(String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
