

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.Tables.Doctor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDoctorDialogController {

    public TextField nameTextField;
    public TextField locationTextField;
    public TextField idNumberTextField;

    private Stage dialogStage;

    public Button positiveButton;
    public Label dialogTitle;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        positiveButton.setText("ADD");
        dialogTitle.setText("Add Doctor");
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onAddClicked() {
        Doctor doctor = new Doctor(idNumberTextField.getText(), locationTextField.getText(), nameTextField.getText());
        boolean inserted = JDBCTools.insertIntoDoctor(Main.getConnection(), doctor);
        if (inserted) {
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Error!");
            alert.setContentText("Doctor: " + nameTextField.getText() + "with the Id: " + idNumberTextField.getText() + " is already in the system.");
            alert.showAndWait();
        }
    }
}
