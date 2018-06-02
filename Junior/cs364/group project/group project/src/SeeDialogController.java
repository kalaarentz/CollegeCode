

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.Tables.Doctor;
import DrugzLLC.Tables.Patient;
import DrugzLLC.Tables.See;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import static DrugzLLC.TableTools.initDoctorTableView;

public class SeeDialogController {

    private Stage dialogStage;
    public Label dialogTitle;
    public TableView<Doctor> doctorTableView;
    private Patient patient;

    public void setDialogStage(Stage dialogStage, Patient patient) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Link who patient : " + patient.getLastName() + " sees.");
        this.patient = patient;
        initDoctorTableView(doctorTableView);
        doctorTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void onCancelClicked() {
        dialogStage.close();

    }

    public void onCreateLink() {
        if (doctorTableView.getSelectionModel().getSelectedItems() != null) {
            ObservableList<Doctor> doctorsObservableList;
                doctorsObservableList = doctorTableView.getSelectionModel().getSelectedItems();
                for (Doctor doctor : doctorsObservableList) {
                    // make connection between patient and doctors
                    See see = new See(doctor.getId(), patient.getSsn());
                    JDBCTools.insertIntoSee(Main.getConnection(), see);
                }

            dialogStage.close();
        }
        // else error??
        // unlink here too???
    }
}
