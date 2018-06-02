

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.Tables.Patient;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddPatientDialogController {

    public TextField ssnTextField;
    public TextField firstNameTextField;
    public TextField middleNameTextField;
    public TextField lastNameTextField;
    public TextField insuranceNameTextField;
    public TextField addressTextField;
    public DatePicker dobDatePicker;

    public Label dialogTitle;
    public Button positiveButton;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Add Patient");
        positiveButton.setText("ADD");
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onAddClicked() {

        Patient patient = new Patient(ssnTextField.getText(),
                firstNameTextField.getText(),
                middleNameTextField.getText(),
                lastNameTextField.getText(),
                dobDatePicker.getValue().toString(),
                insuranceNameTextField.getText(),
                addressTextField.getText()
        );

        boolean inserted = JDBCTools.insertIntoPatient(Main.getConnection(), patient);
        if (inserted) {
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Error!");
            alert.setContentText("Patient: " + lastNameTextField.getText() + "with the Social Security Number: " + ssnTextField.getText() + " is already in the system.");
            alert.showAndWait();
        }
    }

}
