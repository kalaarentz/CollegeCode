

import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Prescription;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;

import static DrugzLLC.JDBCTools.searchFromIfAvailable;
import static DrugzLLC.Main.showErrorAlertDialog;

public class PrescriptionAdvancedSearchDialogController {
//    public TextField nameTextField;
//    public TextField locationTextField;
//    public TextField idNumberTextField;

    public TextField rxTextField;
    public TextField nameTextField;
    public TextField numberSuppliedTextField;
    public TextField numberRefillsTextField;
    public TextField sideEffectsTextField;


    private Stage dialogStage;
    private PrescriptionAdvancedSearchDialogController.OnAdvancedSearchComplete onAdvancedSearchComplete;

    public void setDialogStage(Stage dialogStage, PrescriptionAdvancedSearchDialogController.OnAdvancedSearchComplete onAdvancedSearchComplete) {
        this.dialogStage = dialogStage;
        this.onAdvancedSearchComplete = onAdvancedSearchComplete;
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onSearchClicked() {
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        if (!rxTextField.getText().equals("")) {
            attributes.add(Prescription.RX_JDBC_KEY);
            values.add(rxTextField.getText());
        }
        if (!nameTextField.getText().equals("")) {
            attributes.add(Prescription.NAME_JDBC_KEY);
            values.add(nameTextField.getText());
        }
        if (!numberSuppliedTextField.getText().equals("")) {
            attributes.add(Prescription.NUM_SUPPLIED_JDBC_KEY);
            values.add(numberSuppliedTextField.getText());
        }
        if (!numberRefillsTextField.getText().equals("")) {
            attributes.add(Prescription.NUM_REFILLS_JDBC_KEY);
            values.add(numberRefillsTextField.getText());
        }
        if (!sideEffectsTextField.getText().equals("")) {
            attributes.add(Prescription.SIDE_EFFECTS_JDBC_KEY);
            values.add(sideEffectsTextField.getText());
        }

        ResultSet resultSet = searchFromIfAvailable(Main.getConnection(), TableTools.Table.Prescriptions.name(), attributes, values);
        if(resultSet != null) {
            onAdvancedSearchComplete.onPrescriptionSearch(resultSet);
            dialogStage.close();
        } else {
            showErrorAlertDialog("No results found.");
        }
    }

    public interface OnAdvancedSearchComplete {
        void onPrescriptionSearch(ResultSet resultSet);
    }
}
