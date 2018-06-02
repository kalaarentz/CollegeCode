

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class HaveDialogController {

    private Stage dialogStage;
    public Label dialogTitle;
    public TableView<Prescription> prescriptionTableView;
    private Patient patient;

    public void setDialogStage(Stage dialogStage, Patient patient) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Link what prescription patient : " + patient.getLastName() + " has.");
        this.patient = patient;
        TableTools.initPrescriptionTableView(prescriptionTableView);
        prescriptionTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onCreateLink() {
        if (prescriptionTableView.getSelectionModel().getSelectedItems() != null) {
            ObservableList<Prescription> prescriptionObservableList;
            prescriptionObservableList = prescriptionTableView.getSelectionModel().getSelectedItems();
            for (Prescription prescription : prescriptionObservableList) {
                Have have = new Have(patient.getSsn(), prescription.getRx());
                JDBCTools.insertIntoHave(Main.getConnection(), have);
            }

            dialogStage.close();
        }
        // else error??
        // unlink here too???
    }
}
