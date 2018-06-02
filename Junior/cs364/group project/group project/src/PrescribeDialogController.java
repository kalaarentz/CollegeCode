

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class PrescribeDialogController {

        private Stage dialogStage;
        public Label dialogTitle;
        public TableView<Prescription> prescriptionTableView;
        private Doctor doctor;

        public void setDialogStage(Stage dialogStage, Doctor doctor) {
            this.dialogStage = dialogStage;
            dialogTitle.setText("Link what prescription patient : " + doctor.getName() + " prescribes.");
            this.doctor = doctor;
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
                    try {
                        Prescribe prescribe = new Prescribe(prescription.getRx(), Integer.parseInt(doctor.getId()));
                        JDBCTools.insertIntoPrescripe(Main.getConnection(), prescribe);
                    } catch (NumberFormatException ex) {
                        // bad data, why did we make prescribe int and not text????
                    }
                }
                dialogStage.close();
            }
        }


}
