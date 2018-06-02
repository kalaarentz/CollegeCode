

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Doctor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UpdateDoctorDialogController {

    public TextField nameTextField;
    public TextField locationTextField;
    public Label idNumberLabel;

    private Stage dialogStage;

    public Button positiveButton;
    public Label dialogTitle;

    private Doctor doctorToUpdate;

    public void setDialogStage(Stage dialogStage, Doctor doctor) {
        this.dialogStage = dialogStage;
        positiveButton.setText("UPDATE");
        dialogTitle.setText("Update Doctor");
        doctorToUpdate = doctor;

        idNumberLabel.setText(doctor.getId());
        locationTextField.setText(doctor.getLocation());
        nameTextField.setText(doctor.getName());
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onUpdateClicked() {

        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        if (!nameTextField.getText().equals(doctorToUpdate.getName())) {
            attributes.add(Doctor.NAME_JDBC_KEY);
            values.add(nameTextField.getText());
        }
        if (!locationTextField.getText().equals(doctorToUpdate.getLocation())) {
            attributes.add(Doctor.LOCATION_JDBC_KEY);
            values.add(locationTextField.getText());
        }
        JDBCTools.updateFrom(Main.getConnection(), TableTools.Table.Doctors.name(), attributes, values, Doctor.ID_JDBC_KEY + " = '" + doctorToUpdate.getId() + "'");
        dialogStage.close();
    }
}
