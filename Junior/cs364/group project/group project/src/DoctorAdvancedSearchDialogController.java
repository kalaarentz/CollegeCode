

import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Doctor;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;

import static DrugzLLC.JDBCTools.searchFromIfAvailable;
import static DrugzLLC.Main.showErrorAlertDialog;

public class DoctorAdvancedSearchDialogController {

    public TextField nameTextField;
    public TextField locationTextField;
    public TextField idNumberTextField;

    private Stage dialogStage;
    private DoctorAdvancedSearchDialogController.OnAdvancedSearchComplete onAdvancedSearchComplete;

    public void setDialogStage(Stage dialogStage, DoctorAdvancedSearchDialogController.OnAdvancedSearchComplete onAdvancedSearchComplete) {
        this.dialogStage = dialogStage;
        this.onAdvancedSearchComplete = onAdvancedSearchComplete;
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onSearchClicked() {
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        if (!nameTextField.getText().equals("")) {
            attributes.add(Doctor.NAME_JDBC_KEY);
            values.add(nameTextField.getText());
        }
        if (!locationTextField.getText().equals("")) {
            attributes.add(Doctor.LOCATION_JDBC_KEY);
            values.add(locationTextField.getText());
        }
        if (!idNumberTextField.getText().equals("")) {
            attributes.add(Doctor.ID_JDBC_KEY);
            values.add(idNumberTextField.getText());
        }

        ResultSet resultSet = searchFromIfAvailable(Main.getConnection(), TableTools.Table.Doctors.name(), attributes, values);
        if(resultSet != null) {
            onAdvancedSearchComplete.onDoctorSearch(resultSet);
            dialogStage.close();
        } else {
            showErrorAlertDialog("No results found.");
        }
    }

    public interface OnAdvancedSearchComplete {
        void onDoctorSearch(ResultSet resultSet);
    }

}
