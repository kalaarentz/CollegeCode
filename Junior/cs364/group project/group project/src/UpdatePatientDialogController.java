

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Patient;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UpdatePatientDialogController {

    public Label ssnTextField;
    public TextField firstNameTextField;
    public TextField middleNameTextField;
    public TextField lastNameTextField;
    public TextField insuranceNameTextField;
    public TextField addressTextField;
    public DatePicker dobDatePicker;

    public Label dialogTitle;
    public Button positiveButton;

    private Stage dialogStage;

    private Patient patientToUpdate;

    public void setDialogStage(Stage dialogStage, Patient patient) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Update Patient");
        positiveButton.setText("UPDATE");
        patientToUpdate = patient;

        ssnTextField.setText(patient.getSsn());
        firstNameTextField.setText(patient.getFirstName());
        middleNameTextField.setText(patient.getMiddleName());
        lastNameTextField.setText(patient.getLastName());
        insuranceNameTextField.setText(patient.getInsuranceName());
        addressTextField.setText(patient.getAddress());
        dobDatePicker.setValue(getLocalDate(patient.getDateOfBirth()));
    }

    private static LocalDate getLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onUpdateClicked() {
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        if (!firstNameTextField.getText().equals(patientToUpdate.getFirstName())) {
            attributes.add(Patient.FNAME_JDBC_KEY);
            values.add(firstNameTextField.getText());
        }
        if (!middleNameTextField.getText().equals(patientToUpdate.getMiddleName())) {
            attributes.add(Patient.MNAME_JDBC_KEY);
            values.add(middleNameTextField.getText());
        }
        if (!lastNameTextField.getText().equals(patientToUpdate.getLastName())) {
            attributes.add(Patient.LNAME_JDBC_KEY);
            values.add(lastNameTextField.getText());
        }
        if (!insuranceNameTextField.getText().equals(patientToUpdate.getInsuranceName())) {
            attributes.add(Patient.INSURANCE_JDBC_KEY);
            values.add(insuranceNameTextField.getText());
        }
        if (!addressTextField.getText().equals(patientToUpdate.getAddress())) {
            attributes.add(Patient.ADDRESS_JDBC_KEY);
            values.add(addressTextField.getText());
        }
        if(!dobDatePicker.getValue().toString().equals(patientToUpdate.getDateOfBirth())) {
            attributes.add(Patient.DOB_JDBC_KEY);
            values.add(dobDatePicker.getValue().toString());
        }

        JDBCTools.updateFrom(Main.getConnection(), TableTools.Table.Patients.name(), attributes, values, Patient.SSN_JDBC_KEY + " = '" + patientToUpdate.getSsn() + "'");
        dialogStage.close();
    }
}
