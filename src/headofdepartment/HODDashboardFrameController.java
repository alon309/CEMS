package headofdepartment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import ClientAndServerLogin.LoginFrameController;
import ClientAndServerLogin.SceneManagment;
import Config.HeadOfDepartment;
import client.ClientUI;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class HODDashboardFrameController implements Initializable{

	// -------------- Show Report PANEL --------------

	@FXML
	private JFXListView<String> listOfData_ShowReport;

	@FXML
	private JFXComboBox<String> typeSelectBox_ShowReport;
	
	private ArrayList<String> reprtsType = new ArrayList<>(Arrays.asList(
			"Students", 
			"Lecturers",
			"Courses"
			));
	
	private ObservableList<String> listOfData_observablelist = FXCollections.observableArrayList();
	
    private String chosenReport;
    
    private String selectedSpecificReport;

	// -------------- END Show Report PANEL --------------
    
	@FXML
	private JFXComboBox<String> typeSelectBox_GeneralInfo;
	
	@FXML
	private JFXListView<String> listInfo_Info;
	
	private String chosenInfo;
	
	private ObservableList<String> listOfGeneralInfo_observablelist = FXCollections.observableArrayList();
	
	private ArrayList<String> info_type_arr = new ArrayList<>(Arrays.asList(
			"Subjects", 
			"Courses",
			"Lecturers",
			"Students",
			"Exams",
			"Questions"
			));
    @FXML
    private TextField txtFilter_GeneralReport;

    @FXML
    private JFXButton btnApproveTimeChange;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private JFXButton currentSection;
    @FXML
    private JFXButton btnGeneralInformation;
    
    @FXML
	private JFXButton btnHelp;

	@FXML
    private TextField txtMessageToWriteToLecturer;

    @FXML
    private Label lbluserNameAndID;

	@FXML
	private Label lblHelp;

    @FXML
    private Pane pnlApproveTimeChange;
    @FXML
    private Pane pnlShowReport;
    @FXML
    private Pane pnlGreeting;
    @FXML
    private Pane pnlGeneralInformation;
    @FXML
    private Pane currentPane;

    @FXML
	private JFXSnackbar snackbar;

	private JFXSnackbarLayout snackbarLayout;

    @FXML
    private StackPane stackPane;
    
    @FXML
    private JFXListView<String> listRequests;
    
    
    private ObservableList<String> requests_observablelist = FXCollections.observableArrayList();

    private String chosenRequest;
    
    private String subjectName;
    private String courseName;
    private String lecturerID;
    private String examID;
    private String lecturerName;
    private String explanation;
    private String examDurationAdd;
    
    private ArrayList<String> allRequests = new ArrayList<>();
    
    private static HODDashboardFrameController instance;
    private static HeadOfDepartment headofdepartment;
    protected static Stage currentStage; // save current stage

	/**
	 * Constructor for the HODDashboardFrameController class.
	 * This constructor sets the instance variable to the current instance of the controller.
	 */
    public HODDashboardFrameController(){
        instance = this;
    }

	/**
	 * Returns the instance of the HODDashboardFrameController class.
	 *
	 * @return the instance of the HODDashboardFrameController class
	 */
    public static HODDashboardFrameController getInstance(){
        return instance;
    }

	/**
	 * Initializes the controller.
	 *
	 * @param arg0 the URL of the FXML file
	 * @param arg1 the resource bundle associated with the FXML file
	 */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	lbluserNameAndID.setText((headofdepartment.getName() + "\n(ID: " + headofdepartment.getId() + ")")); //Initializing the label
		chosenReport = null;
		selectedSpecificReport = null;
		listOfData_ShowReport.getSelectionModel().clearSelection();
		
		typeSelectBox_GeneralInfo.getItems().addAll(info_type_arr);
		typeSelectBox_GeneralInfo.getSelectionModel().clearSelection();
    	
    	getAllrequests();
    	
    	setReportsInComboBox();
    	
    	currentPane = pnlGreeting;
        pnlGreeting.toFront();
		
	}

	/**
	 * Sets the reports in the combo box for selecting report types.
	 * This method adds the report names from the "reprtsType" list to the combo box for selecting report types.
	 */
    private void setReportsInComboBox() {
    	for(String reportName : reprtsType) {
    		typeSelectBox_ShowReport.getItems().add(reportName);
    	}
	}

	/**
	 * Handles the event when the "Deny Request" button is clicked.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the process of denying the request
	 */
	@FXML
    public void getBtnDenyRequest(ActionEvent event) throws Exception{
    	
    	chosenRequest = listRequests.getSelectionModel().getSelectedItem();
    	if(chosenRequest == null) {
			displayErrorMessage("Error: Please choose request first!");
    	}
    	else if(txtMessageToWriteToLecturer.getText().trim().isEmpty()) {
			displayErrorMessage("Error: Please write explanation!");
    	}
    	else {
    		requestDenied();
			displaySuccessMessage("Request for Changing time was DENIED!");
    		getAllrequests();
    	}
    	chosenRequest = null;
    }

	/**
	 * Handles the event when the "Accept Request" button is clicked.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the process of accepting the request
	 */
	@FXML
    public void getBtnAcceptRequest(ActionEvent event) throws Exception{
    	chosenRequest = listRequests.getSelectionModel().getSelectedItem();
    	if(chosenRequest == null) {
			displayErrorMessage("Error: Please choose request first!");
    	}
    	else if(txtMessageToWriteToLecturer.getText().trim().isEmpty()) {
			displayErrorMessage("Error: Please write explanation!");
    	}
    	else {
    		requestAccepted();
			displaySuccessMessage("Request for changing time approved!");
    		getAllrequests();
    	}
    	chosenRequest = null;
    }

	/**
	 * Handles the acceptance of a request for change in exam time.
	 * This method retrieves the necessary information from the chosen request, creates a request accepted message, and sends it to the server.
	 */
    private void requestAccepted() {
    	
    	int requestId = Integer.parseInt(chosenRequest.split("\\)")[0]) - 1;
    	
    	String[] request_str = allRequests.get(requestId).split(","); // get the request by id
    	
        lecturerID = request_str[3];
        examID = request_str[4];
        examDurationAdd = request_str[7];
    	
		ArrayList<String> requestaccepted_arr = new ArrayList<>();
		requestaccepted_arr.add("RequestForChangeTimeInExamAccepted");
		requestaccepted_arr.add(headofdepartment.getId());
		requestaccepted_arr.add(lecturerID);
		requestaccepted_arr.add(examID);
		requestaccepted_arr.add(examDurationAdd);
		requestaccepted_arr.add(txtMessageToWriteToLecturer.getText());
		ClientUI.chat.accept(requestaccepted_arr);
	}

	/**
	 * Handles the denial of a request for change in exam time.
	 * This method retrieves the necessary information from the chosen request, creates a request denied message, and sends it to the server.
	 */
    private void requestDenied() {
    	
    	int requestId = Integer.parseInt(chosenRequest.split("\\)")[0]) - 1;
    	
    	String[] request_str = allRequests.get(requestId).split(","); // get the request by id
    	
        lecturerID = request_str[3];
        examID = request_str[4];
        examDurationAdd = request_str[7];
        
		ArrayList<String> requestaccepted_arr = new ArrayList<>();
		requestaccepted_arr.add("RequestForChangeTimeInExamDenied");
		requestaccepted_arr.add(headofdepartment.getId());
		requestaccepted_arr.add(lecturerID);
		requestaccepted_arr.add(examID);
		requestaccepted_arr.add(examDurationAdd);
		requestaccepted_arr.add(txtMessageToWriteToLecturer.getText());
		ClientUI.chat.accept(requestaccepted_arr);
        
	}

	/**
	 * Handles the event when the "Refresh" button is clicked.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the process of refreshing the requests
	 */
	@FXML
    public void getBtnRefresh(ActionEvent event) throws Exception{
    	getAllrequests();
    }

	/**
	 * Retrieves all requests of the head of department from the database.
	 * This method sends a request to the server to retrieve all requests associated with the head of department and updates the UI accordingly.
	 */
    public static void getAllrequests() {
		ArrayList<String> getallrequest_arr = new ArrayList<>();
		getallrequest_arr.add("GetAllRequestsOfHodFromDB");
		getallrequest_arr.add(headofdepartment.getId());
		ClientUI.chat.accept(getallrequest_arr);
	}

	/**
	 * Loads the requests from the database and updates the UI with the retrieved data.
	 *
	 * @param requests_arr an ArrayList containing the requests retrieved from the database
	 */
	public void loadRequestsFromDB(ArrayList<String> requests_arr) {
		allRequests = requests_arr;
		
		ArrayList<String> modifiedRequests = new ArrayList<>();
		int i = 1;
		for(String request : requests_arr) {
			
	    	String[] request_str = request.split(",");
	    	subjectName = request_str[1];
	        courseName = request_str[2];
	        lecturerID = request_str[3];
	        examID = request_str[4];
	        lecturerName = request_str[5];
	        explanation = request_str[6];
	        examDurationAdd = request_str[7];
	        
	        String modifiedRequest  = i + ") [ (" + subjectName + " - " + courseName + ") from " + lecturerName + " (" + lecturerID + ") on exam: " 
	        + examID + " ] " + "\nreason: "
	        + explanation + ", Minutes to add to the exam: " + examDurationAdd; 	
	        
	        modifiedRequests.add(modifiedRequest);
	        i++;
		}
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                requests_observablelist.setAll(modifiedRequests);	
				listRequests.setItems(requests_observablelist);
				listRequests.refresh();
				if(!listRequests.getItems().isEmpty()) {
					// pop up message that Change time request recieved
					displaySuccessMessage("New exam time change request recieved!");
				}
            }
        });
    }

	// -------------- Show Report PANEL --------------

	/**
	 * Handles the event when the "Show Report" button is clicked for the Show Report screen.
	 *
	 * @param event the action event representing the button click
	 */
	@FXML
	public void getBtnShowReport_ShowReport(ActionEvent event) {
		try {
			selectedSpecificReport = listOfData_ShowReport.getSelectionModel().getSelectedItem();
			String[] name_id_Report_arr = selectedSpecificReport.split(" - ");
			
			((Node) event.getSource()).getScene().getWindow().hide();
			ViewReportFrameController.start(name_id_Report_arr[0], name_id_Report_arr[1], chosenReport);
			
		} catch (Exception e) {
			displayErrorMessage("Error: You have to select specific report first!");
		}
	}

	/**
	 * Handles the event when the "View Data" button is clicked for the Show Report screen.
	 *
	 * @param event the action event representing the button click
	 */
	@FXML
	public void getViewDataBtn_ShowReport(ActionEvent event) {
		chosenReport = typeSelectBox_ShowReport.getSelectionModel().getSelectedItem();
		selectedSpecificReport = null;
		try {
			if(chosenReport == null || chosenReport.equals("")) {
				throw new NullPointerException();
			}
			
			ArrayList<String> getReportTypeHOD_arr = new ArrayList<>();
			getReportTypeHOD_arr.add("GetAllOptionForDataReport_HOD");
			getReportTypeHOD_arr.add(headofdepartment.getId());
			getReportTypeHOD_arr.add(chosenReport);
			ClientUI.chat.accept(getReportTypeHOD_arr);

			
		} catch (NullPointerException e){
			displayErrorMessage("Error: Please choose report first!");
		}	
		
	}

	/**
	 * Shows the stage for the View Report screen.
	 * This method clears the selection in the report list and shows the current stage.
	 */
	public void showStageFrom_ViewReport() {
		
		listOfData_ShowReport.getSelectionModel().clearSelection();
		
		currentStage.show();	
		
	}
	/**
	 * Handles the event when a specific report is selected.
	 *
	 * @param event the action event representing the selection event
	 * @throws Exception if an exception occurs during the process of handling the event
	 */
	@FXML
	public void getSelectedReport(ActionEvent event) throws Exception{
		selectedSpecificReport = null;
		listOfData_observablelist.clear();
		listOfData_ShowReport.setItems(listOfData_observablelist);
        listOfData_ShowReport.refresh();
	}


	// -------------- END Show Report PANEL --------------

	// -------------- Greeting PANEL --------------

	@FXML
	void showHelpInfo(MouseEvent event) {
		pnlGreeting.toFront();

	}

	// -------------- END Greeting PANEL --------------

	/**
	 * Starts the head of department application with the provided details.
	 *
	 * @param hodDetails an ArrayList containing the details of the head of department
	 * @throws IOException if an I/O exception occurs during the startup process
	 */
    public static void start(ArrayList<String> hodDetails) throws IOException {

        // Initialize the student with the provided details
        headofdepartment = new HeadOfDepartment(hodDetails.get(2), hodDetails.get(3), hodDetails.get(4), hodDetails.get(5), hodDetails.get(6));
        // -- hodDetails --
        // 1 - login As
        // 2 - user ID
        // 3 - userName
        // 4 - user Password
        // 5 - user Name
        // 6 - user Email

        // Run the following code on the JavaFX Application Thread using Platform.runLater()
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Save the current dashboard screen for returning back  , "/headofdepartment/HODDashboardGUI.fxml", "HOD Dashboard"
					currentStage = SceneManagment.createNewStage("/headofdepartment/HODDashboardGUI.fxml", null,
							"HeadOfDepartmentDashboard");
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

	/**
	 * Handles the event when the "Close" button is clicked.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the process of closing the window or sending a quit message to the server
	 */
    @FXML
    public void getCloseBtn(ActionEvent event) throws Exception{
        // Hide the primary window
        ((Node) event.getSource()).getScene().getWindow().hide();

        // Send a quit message to the server using the client's ID and role
        ClientUI.chat.client.quit(headofdepartment.getId(), "headofdepartment");
    }

	/**
	 * Handles the event when the "View Data" button is clicked for general information.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the process of retrieving and displaying the data
	 */
    @FXML
    public void getViewDataBtn_GeneralInfo(ActionEvent event) throws Exception{
    	
		chosenInfo = typeSelectBox_GeneralInfo.getSelectionModel().getSelectedItem();
		try {
			if(chosenInfo == null || chosenInfo.equals("")) {
				throw new NullPointerException();
			}
			
			ArrayList<String> getInfoTypeHOD_arr = new ArrayList<>();
			getInfoTypeHOD_arr.add("GetAllGeneralInfo_HOD");
			getInfoTypeHOD_arr.add(headofdepartment.getId());
			getInfoTypeHOD_arr.add(chosenInfo);
			ClientUI.chat.accept(getInfoTypeHOD_arr);

			
		} catch (NullPointerException e){
			displayErrorMessage("Error: Please choose info to show first!");
		}	
    	
    }

	/**
	 * Handles the event when the filtering word for the general report is entered.
	 *
	 * @param event the action event representing the event when the filtering word is entered
	 * @throws Exception if an exception occurs during the filtering process
	 */
    @FXML
    public void getFilteringWord_GeneralReport(ActionEvent event) throws Exception{
    	
    	if(txtFilter_GeneralReport.getText() == null || txtFilter_GeneralReport.getText().trim().equals("")) {
    		listInfo_Info.setItems(listOfGeneralInfo_observablelist);
    	}
    	else {
        	String inputText = txtFilter_GeneralReport.getText();
        	ObservableList<String> filteredList = FXCollections.observableArrayList();

        	for (String item : listOfGeneralInfo_observablelist) {
        	    if (item.contains(inputText)) {
        	        filteredList.add(item);
        	    }
        	}

        	listInfo_Info.setItems(filteredList);
    	}
    	
    }


	/**
	 * Handles the logout button click event in the user interface.
	 *
	 * @param event the action event representing the button click
	 * @throws Exception if an exception occurs during the logout process
	 */
    @FXML
    public void getLogoutBtn(ActionEvent event) throws Exception{
        // Hide the primary window
        ((Node) event.getSource()).getScene().getWindow().hide();

        // Send a logout message to the server to update the user's logged-in status
        ArrayList<String> qArr = new ArrayList<>();
        qArr.add("UserLogout");
        qArr.add("headofdepartment");
        qArr.add(headofdepartment.getId());
        ClientUI.chat.accept(qArr);

        // Start the login screen after logout
        LoginFrameController.start();
    }

	/**
	 * Handles button clicks in the user interface.
	 *
	 * @param actionEvent the action event representing the button click
	 */
    @FXML
    void handleClicks(ActionEvent actionEvent) {
    	if (actionEvent.getSource() == btnShowReport) {
    		
    		listOfData_observablelist.clear();
    		listOfData_ShowReport.setItems(listOfData_observablelist);
            listOfData_ShowReport.refresh();
    		typeSelectBox_ShowReport.getSelectionModel().clearSelection();
    		chosenReport = null;
    		selectedSpecificReport = null;
    		listOfData_ShowReport.getSelectionModel().clearSelection();
    		handleAnimation(pnlShowReport, btnShowReport);
	        pnlShowReport.toFront();
	    }
	    if (actionEvent.getSource() == btnApproveTimeChange) {
	    	handleAnimation(pnlApproveTimeChange, btnApproveTimeChange);
	    	getAllrequests();
	        pnlApproveTimeChange.toFront();
	    }
	    if (actionEvent.getSource() == btnGeneralInformation) {
	    	handleAnimation(pnlGeneralInformation, btnGeneralInformation);
	    	pnlGeneralInformation.toFront();
	    }
		if (actionEvent.getSource() == btnHelp) {
			handleAnimation(pnlGreeting, btnHelp);
			pnlGreeting.toFront();
		}

    }


	/**
	 * Handles the animation and transition between panes when clicking on buttons on the right side.
	 *
	 * @param newPane the new pane to transition to
	 * @param newSection the new section button that triggered the transition
	 */
    public void handleAnimation(Pane newPane, JFXButton newSection) {
    	if(newSection != currentSection) {
    		FadeTransition outgoingPane = new FadeTransition(Duration.millis(125), currentPane);
            outgoingPane.setFromValue(1);
            outgoingPane.setToValue(0);

            FadeTransition comingPane = new FadeTransition(Duration.millis(125), newPane);
            comingPane.setFromValue(0);
            comingPane.setToValue(1);

            SequentialTransition transition = new SequentialTransition();
            transition.getChildren().addAll(outgoingPane, comingPane);
            transition.play();

			if (newSection != btnHelp)
				newSection.setStyle("-fx-border-color: #FAF9F6");
			if (currentSection != null && currentSection != newSection)
				currentSection.setStyle("-fx-border-color: #242633");

            currentPane = newPane;
            currentSection = newSection;
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
				snackbar = new JFXSnackbar(stackPane);
				String css = this.getClass().getClassLoader().getResource("css/SnackbarError.css")
						.toExternalForm();
				snackbar.setPrefWidth(754);
				snackbarLayout = new JFXSnackbarLayout(message);
				snackbarLayout.getStylesheets().add(css);
				snackbar.getStylesheets().add(css);
				snackbar.fireEvent(new SnackbarEvent(snackbarLayout, Duration.millis(3000), null));
			}
		});
	}

	/**
	 * Displays a success message using a Snackbar.
	 *
	 * @param message The success message to display.
	 */
	public void displaySuccessMessage(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				snackbar = new JFXSnackbar(stackPane);
				String css = this.getClass().getClassLoader().getResource("css/SnackbarSuccess.css")
						.toExternalForm();
				snackbar.setPrefWidth(754);
				snackbarLayout = new JFXSnackbarLayout(message);
				snackbarLayout.getStylesheets().add(css);
				snackbar.getStylesheets().add(css);
				snackbar.fireEvent(new SnackbarEvent(snackbarLayout, Duration.millis(3000), null));
			}
		});
	}

	public void loadAllOptionsForChosenReport(ArrayList<String> options_arr) { // getting the option: "name - id"
	    	Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	
					listOfData_observablelist.setAll(options_arr);	
			        listOfData_ShowReport.setItems(listOfData_observablelist);
			        listOfData_ShowReport.refresh();
	            }
	        });
		
	}

	/**
	 * Loads all information for the chosen type of information into the view.
	 * This method updates the UI components in a JavaFX application by running the code on the JavaFX application thread.
	 *
	 * @param info_arr the ArrayList containing the information to be loaded
	 */
	public void loadAllInfoForChosenTypeInfo(ArrayList<String> info_arr) { // getting the information
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
				listOfGeneralInfo_observablelist.setAll(info_arr);	
				listInfo_Info.setItems(listOfGeneralInfo_observablelist);
				listInfo_Info.refresh();
            }
        });
	
	}

	

}
