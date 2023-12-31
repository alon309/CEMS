package lecturer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import ClientAndServerLogin.SceneManagment;
import Config.Exam;
import Config.HeadOfDepartment;
import Config.Lecturer;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ManageExam_ChangeTimeFrameController implements Initializable {
	
	@FXML
	private TextField txtExamDuration; // can be negative
	@FXML
	private TextField txtExplanationExamDurationChange;
	
	@FXML
	private JFXComboBox<String> hodSelectBox;
	@FXML 
	private AnchorPane root;
	@FXML
	private JFXSnackbar snackbar;
	private JFXSnackbarLayout snackbarLayout;

	private static Exam exam;
	private static Lecturer lecturer;
	
	private static ManageExam_ChangeTimeFrameController instance;
	
	public ManageExam_ChangeTimeFrameController() {
		instance = this;
	}
	
	public static ManageExam_ChangeTimeFrameController getInstance() {
		return instance;
	}

	/**
	 * Starts the exam management process by opening the "Manage Exam" GUI.
	 * 
	 * @param temp_exam The exam to be managed.
	 * @param temp_lecturer The lecturer managing the exam.
	 * @throws IOException If an error occurs while loading the GUI.
	 */
	public static void start(Exam temp_exam, Lecturer temp_lecturer) throws IOException {
	    exam = temp_exam; // save the exam
	    lecturer = temp_lecturer; // save the lecturer
		SceneManagment
				.createNewStage("/lecturer/ManageExam_ChangeTimeGUI.fxml", null, "Lecturer->ManageExam->ChangeTime")
				.show();
	}

	
	/**
	 * Handles the event when the "Send Request" button is clicked.
	 * 
	 * @param event The event triggered by the button click.
	 * @throws Exception If an error occurs during the request handling process.
	 */
	public void getBtnSendRequest(ActionEvent event) throws Exception {

	    String hodSelected = hodSelectBox.getSelectionModel().getSelectedItem();
	    
	    // Check if any required fields are missing
	    if (txtExplanationExamDurationChange.getText().trim().equals("") || txtExamDuration.getText().trim().equals("")
				|| hodSelected == null) {
			displayErrorMessage("Error: Missing fields!"); // error message
	    } else {    
	        try {
	            // Parse the exam duration as an integer
	            int number = Integer.parseInt(txtExamDuration.getText());
	            if (number <= 0) {
	                throw new NumberFormatException();
	            }
	            sendRequest(hodSelected); // send the request to hod
	            
	            ((Node) event.getSource()).getScene().getWindow().hide(); // Hide the primary window
	            LecturerDashboardFrameController.getInstance().showDashboardFrom_ChangeTime(true); // true if request sent

	        } catch (NumberFormatException e) {
				displayErrorMessage("Error: Minutes should be positive!"); // error message
	        }
	    }    
	}

	/**
	 * Sends a request to the selected Head of Department (HOD) to change the exam duration.
	 * 
	 * @param hodSelected The selected Head of Department.
	 */
	private void sendRequest(String hodSelected) {
	    String[] hod_name_id = hodSelected.split(" - "); // the hods in the list as: "hod name - hod id"
	    ArrayList<String> infoOfRequest_Arr = new ArrayList<>();
	    infoOfRequest_Arr.add("RequestToChangeAnExamDurationFromLecturerToHOD");
	    infoOfRequest_Arr.add(exam.getExamID());
	    infoOfRequest_Arr.add(exam.getSubjectName());
	    infoOfRequest_Arr.add(exam.getCourseName());
	    infoOfRequest_Arr.add(lecturer.getId());
	    infoOfRequest_Arr.add(lecturer.getName());
	    infoOfRequest_Arr.add(txtExplanationExamDurationChange.getText());
	    infoOfRequest_Arr.add(txtExamDuration.getText());
	    infoOfRequest_Arr.add(hod_name_id[1]);
	    ClientUI.chat.accept(infoOfRequest_Arr);
	}


	/**
	 * Handles the event when the "Back" button is clicked.
	 *
	 * @param event The event triggered by the button click.
	 * @throws Exception If an error occurs during the handling process.
	 */
	public void getBtnBack(ActionEvent event) throws Exception {
	    ((Node) event.getSource()).getScene().getWindow().hide(); // Hide the primary window
	    LecturerDashboardFrameController.getInstance().showDashboardFrom_ChangeTime(false); // false when press back (request was not sent)
	}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<String> hod_arr = new ArrayList<>();
    	hod_arr.add("GetRelevantHodForLecturer");
    	hod_arr.add(lecturer.getId());
    	ClientUI.chat.accept(hod_arr);
    }
    
    /**
     * Loads the list of Head of Departments (HODs) for the lecturer.
     *
     * @param hod_arr The ArrayList of HeadOfDepartment objects.
     */
    public void loadHODsForLecturer(ArrayList<HeadOfDepartment> hod_arr) {
        for (HeadOfDepartment hods : hod_arr) {
            hodSelectBox.getItems().add(hods.toString());
        }
    }
    
	/**
	 * Displays an error message using a Snackbar.
	 *
	 * @param message The error message to display.
	 */
	public void displayErrorMessage(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				snackbar = new JFXSnackbar(root);
				String css = this.getClass().getClassLoader().getResource("css/SnackbarError.css").toExternalForm();
				snackbar.setPrefWidth(root.getPrefWidth() - 40);
				snackbarLayout = new JFXSnackbarLayout(message);
				snackbarLayout.getStylesheets().add(css);
				snackbar.getStylesheets().add(css);
				snackbar.fireEvent(new SnackbarEvent(snackbarLayout, Duration.millis(3000), null));
			}
		});
	}

}

