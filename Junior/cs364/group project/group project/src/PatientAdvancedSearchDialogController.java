

import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Patient;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;

import static DrugzLLC.JDBCTools.searchFromIfAvailable;
import static DrugzLLC.Main.showErrorAlertDialog;

public class PatientAdvancedSearchDialogController {
    public TextField ssnTextField;
    public TextField firstNameTextField;
    public TextField middleNameTextField;
    public TextField lastNameTextField;
    public TextField insuranceNameTextField;
    public TextField addressTextField;
    public DatePicker dobDatePicker;

    private Stage dialogStage;
    private OnAdvancedSearchComplete onAdvancedSearchComplete;

    public void setDialogStage(Stage dialogStage, OnAdvancedSearchComplete onAdvancedSearchComplete) {
        this.dialogStage = dialogStage;
        this.onAdvancedSearchComplete = onAdvancedSearchComplete;
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onSearchClicked() {
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        if (!ssnTextField.getText().equals("")) {
            attributes.add(Patient.SSN_JDBC_KEY);
            values.add(ssnTextField.getText());
        }
        if (!firstNameTextField.getText().equals("")) {
            attributes.add(Patient.FNAME_JDBC_KEY);
            values.add(firstNameTextField.getText());
        }
        if (!middleNameTextField.getText().equals("")) {
            attributes.add(Patient.MNAME_JDBC_KEY);
            values.add(middleNameTextField.getText());
        }
        if (!lastNameTextField.getText().equals("")) {
            attributes.add(Patient.LNAME_JDBC_KEY);
            values.add(lastNameTextField.getText());
        }
        if (dobDatePicker.getValue() != null) {
            attributes.add(Patient.DOB_JDBC_KEY);
            values.add(dobDatePicker.getValue().toString());
        }
        if (!insuranceNameTextField.getText().equals("")) {
            attributes.add(Patient.INSURANCE_JDBC_KEY);
            values.add(insuranceNameTextField.getText());
        }
        if (!addressTextField.getText().equals("")) {
            attributes.add(Patient.ADDRESS_JDBC_KEY);
            values.add(addressTextField.getText());
        }

        ResultSet resultSet = searchFromIfAvailable(Main.getConnection(), TableTools.Table.Patients.name(), attributes, values);
            if(resultSet != null) {
                onAdvancedSearchComplete.onPatientSearch(resultSet);
                dialogStage.close();
            } else {
                showErrorAlertDialog("No results found.");
            }
    }

    public interface OnAdvancedSearchComplete {
        void onPatientSearch(ResultSet resultSet);
    }

}
