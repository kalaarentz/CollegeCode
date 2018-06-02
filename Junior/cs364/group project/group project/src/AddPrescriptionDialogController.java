

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.Tables.Prescription;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPrescriptionDialogController {

    public TextField rxTextField;
    public TextField nameTextField;
    public TextField numberSuppliedTextField;
    public TextField numberRefillsTextField;
    public TextField sideEffectsTextField;

    private Stage dialogStage;

    public Button positiveButton;
    public Label dialogTitle;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        positiveButton.setText("ADD");
        dialogTitle.setText("Add Prescription");
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onAddClicked() {
        Prescription prescription = null;
        try {
            prescription = new Prescription(
                    Integer.parseInt(rxTextField.getText()),
                    nameTextField.getText(),
                    Integer.parseInt(numberSuppliedTextField.getText()),
                    Integer.parseInt(numberRefillsTextField.getText()),
                    sideEffectsTextField.getText()
            );

            boolean inserted = JDBCTools.insertIntoPrescription(Main.getConnection(), prescription);
            if (inserted) {
                dialogStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("Error!");
                alert.setContentText("Prescription: " + nameTextField.getText() + "with RX: " + rxTextField.getText() + " is already in the system.");
                alert.showAndWait();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Error!");
            alert.setContentText("RX number, Number Supplied, and Number of Refills must be integers.");
            alert.showAndWait();
        }

    }

}