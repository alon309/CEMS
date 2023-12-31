package handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.*;
import JDBC.DBController;
import javafx.collections.ObservableList;
import ClientAndServerLogin.ServerPortFrameController;
import ocsf.server.ConnectionToClient;

import static JDBC.DBController.getManualExamPath;

public class MessageHandler_Server {

	private static HashMap<String, ArrayList<Integer>> undergoingExams = new HashMap<>();


	/**
	 * Handles the received message and performs the necessary actions based on the message type.
	 *
	 * @param msg the received message object
	 * @param client the ConnectionToClient representing the client connection
	 */
	@SuppressWarnings("unchecked")
	public static void handleMessage(Object msg, ConnectionToClient client) {
		//System.out.println("Reached the handleMessage Method | Server");
	    MessageType messageType = getMessageType(msg);
	    if (messageType == null) {
	        return;
	    }

	    switch (messageType) {
	        case STRING:
	            handleStringMessage((String) msg, client);
	            break;
	        case ARRAY_LIST_STRING:
	            handleStringArrayListMessage((ArrayList<String>) msg, client);
	            break;
	        case ARRAY_LIST_QUESTION:
	            handleQuestionArrayListMessage((ArrayList<Question>) msg, client);
	            break;
	        case ARRAY_LIST_QUESTIONINEXAM:
	            handleQuestionInExamArrayListMessage((ArrayList<QuestionInExam>) msg, client);
	            break;
	        case ARRAY_LIST_EXAM:
	            handleExamArrayListMessage((ArrayList<Exam>) msg, client);
	            break;
	        case MAP_STRING_ARRAYLIST_STRING:
	            handleMapStringKeyArrayListStringValueMessage((Map<String, ArrayList<String>>) msg, client);
	            break;
	        case MAP_STRING_STRING:
	            handleMapStringStringValueMessage((Map<String, String>) msg, client);
	            break;
			case ARRAY_LIST_FINISHED_EXAM:
				handleFinishedExamArrayListValueMessage((ArrayList<FinishedExam>) msg, client);
				break;
			case MY_FILE:
				handleMyFileValueMessage((MyFile) msg, client);
	        default:
	            //System.out.println("Message type does not exist");
	            break;
	    }
	}

	/**
	 * Determines the MessageType based on the type of the received message object.
	 *
	 * @param msg the message object
	 * @return the MessageType
	 */
	private static MessageType getMessageType(Object msg) {
		//System.out.println("Reached the getMessageType Method | Server Handler");
	    if (msg instanceof String) {
	        return MessageType.STRING;
	    } else if (msg instanceof ArrayList) {
	        ArrayList<?> arrayList = (ArrayList<?>) msg;
	        if (!arrayList.isEmpty()) {
	            Object firstElement = arrayList.get(0);
	            if (firstElement instanceof String) {
	                return MessageType.ARRAY_LIST_STRING;
	            }
	             else if (firstElement instanceof QuestionInExam) {
		            return MessageType.ARRAY_LIST_QUESTIONINEXAM;
	            } else if (firstElement instanceof Question && !(firstElement instanceof QuestionInExam)) {
	                return MessageType.ARRAY_LIST_QUESTION;
	            } else if (firstElement instanceof Exam && !(firstElement instanceof FinishedExam)) {
	            	return MessageType.ARRAY_LIST_EXAM;
	            } else if (firstElement instanceof FinishedExam) {
					return MessageType.ARRAY_LIST_FINISHED_EXAM;
				}
			}
	    } else if (msg instanceof Map) {
	        Map<?, ?> map = (Map<?, ?>) msg;
	        if (!map.isEmpty()) {
	            Object firstKey = map.keySet().iterator().next();
	            Object firstValue = map.get(firstKey);
	            if (firstKey instanceof String && firstValue instanceof ArrayList
	                    && ((ArrayList<?>) firstValue).get(0) instanceof String) {
	                return MessageType.MAP_STRING_ARRAYLIST_STRING;
	            } else if (firstKey instanceof String && firstValue instanceof String) {
	                return MessageType.MAP_STRING_STRING;
	            }
	        }
	    } else if (msg instanceof MyFile) {
			return MessageType.MY_FILE;
		}
		return null;
	}

	/**
	 * Handles the received string message and performs the necessary actions.
	 *
	 * @param message the string message
	 * @param client the ConnectionToClient representing the client connection
	 */
    private static void handleStringMessage(String message, ConnectionToClient client) {
		//System.out.println("Reached the handleStringMessage Method");
        // Handle string messages
    	try {
	    	switch (message) {
			    case "getAllSubjectsNamesAndIdsFromDB":
			    	Map<String, String> subjects_name_id_map_arr = DBController.getAllSubjectsNamesAndIds();
			    	subjects_name_id_map_arr.put("HashMapWithSubjects_names_ids", "forchecking");
			    	client.sendToClient(subjects_name_id_map_arr);
			    	break;
			    	
			    case "getAllCoursesNamesAndIdsFromDB":
			    	Map<String, String> courses_name_id_map_arr = DBController.getAllCoursesNamesAndIds();
			    	courses_name_id_map_arr.put("HashMapWithCourses_names_ids", "forchecking");
			    	client.sendToClient(courses_name_id_map_arr);
			    	break;
			    	
			    default: break;
	    	}
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    }

	/**
	 * Handles the received ArrayList and performs the necessary actions based on the message type.
	 *
	 * @param arrayList the ArrayList containing the message and its parameters
	 * @param client the ConnectionToClient representing the client connection
	 *
	 * must client.sendToClient(obj); after handling the message from the client to get response from the server
	 */
    @SuppressWarnings("unchecked")
    private static void handleStringArrayListMessage(ArrayList<?> arrayList, ConnectionToClient client) {
		//System.out.println("Reached the handleStringArrayListMessage method | Server Handler");
            ArrayList<String> arrayListStr = (ArrayList<String>) arrayList;
			//System.out.println(arrayListStr);
            String messageType = arrayListStr.get(0);
            try {
	            switch (messageType) {
	                case "ClientConnecting":
	                    // Handle ClientConnecting message
	                	
						ServerPortFrameController.addConnectedClient(arrayListStr.get(1), arrayListStr.get(2), client, "User");
						client.sendToClient("client connected");
						
	                    break;
	                case "UserLogin":
	                    // Handle UserLogin message
	                	
	                	ArrayList<String> userDetails;
						userDetails = DBController.userExist(arrayListStr); // getting from DB details about the user
						// if the func return the details of the user -> succeed
						if(!(userDetails.get(0)).equals("UserAlreadyLoggedIn") && !(userDetails.get(0)).equals("UserEnteredWrondPasswwordOrUsername")) {
							ObservableList<ConnectedClient> connectedClients = ServerPortFrameController.getConnectedClients();
							for(int i = 0; i<connectedClients.size(); i++) {
								if(connectedClients.get(i).getIp().equals(arrayListStr.get(4)) && connectedClients.get(i).getRole().equals("User")) {
									connectedClients.get(i).setRole(arrayListStr.get(1));
								}
							}
							arrayListStr.remove(4); // remove the ip from info
							
							ArrayList<String> loginSucceedArr = new ArrayList<>();
							loginSucceedArr.add("UserLoginSucceed");
							loginSucceedArr.add(arrayListStr.get(1)); // send to client to know the correct dashboard to open
							loginSucceedArr.addAll(userDetails); // to send the details of the user to the user
							client.sendToClient(loginSucceedArr);
						}
						else {
							client.sendToClient(userDetails.get(0)); // send back to the client the reason he failed to login
						}
						
	                    break;
	                case "UpdateQuestionDataByID":
	                    // Handle UpdateQuestionDataByID message
	                	
						String returnStr = DBController.UpdateQuestionDataByID(arrayListStr);
						client.sendToClient(returnStr);
						
	                    break;
	                case "GetAllQuestionsFromDB":
	                    // Handle GetAllQuestionsFromDB message
	                	
	                	ArrayList<Question> questions = DBController.getAllQuestions(arrayListStr.get(1), null, null); // send the id of the user
	                	questions.add(0, new Question("LoadQuestionsFromDB",  null, null, null, null, null, null, null));
	                	client.sendToClient((ArrayList<Question>)questions);
						
	                    break;
	                case "RemoveQuestionFromDB":
	                    // Handle RemoveQuestionFromDB message
	                	
						// 1 - question id to remove
						if(DBController.removeQuestion(arrayListStr.get(1))) {
							client.sendToClient("question removed");
						}
						else {
							client.sendToClient("question not removed");
						}
						
	                    break;
	                case "UserLogout":
	                    // Handle UserLogout message
	                	
						// 1 - loggedAs
						// 2 - userID
						DBController.setUserIsLogin("0", arrayListStr.get(1), arrayListStr.get(2));
						client.sendToClient("logged out");
						
	                    break;
	                case "ClientQuitting":
	                    // Handle ClientQuitting message
	                	
						// 1 - HostAddress
						// 2 - HostName
						// 3 - UserID
						// 4 - userLoginAs
						// 5 - isLogged
						ServerPortFrameController.removeConnectedClientFromTable(arrayListStr.get(1), arrayListStr.get(2)); // call function to remove the client from the table
						DBController.setUserIsLogin("0", arrayListStr.get(4), arrayListStr.get(3));
						client.sendToClient("quit");
						
	                    break;
	                    
	                case "GetLecturerSubjectsAndCourses":
	                	// 1 - lecturer ID
				    	Map<String, ArrayList<String>> lecSubjectsCoursesHashMap = DBController.getLecturerSubjectCourses(arrayListStr.get(1));
				    	
						ArrayList<String> checkArr = new ArrayList<>();
						checkArr.add("forchecking");
						lecSubjectsCoursesHashMap.put("HashMapWithLecturerSubjectsAndCourses", checkArr);
				    	
				    	client.sendToClient(lecSubjectsCoursesHashMap);
				    	
				    	break;
				    	
	                case "GetMaxQuestionIdFromProvidedSubject":
	                	// 1 - subject ID
	                	String questionID;
	                	questionID = DBController.getMaxQuestionIdFromSubject(arrayListStr.get(1));
	                	
	                	ArrayList<String> questionIdArr = new ArrayList<>();
	                	questionIdArr.add("MaximunQuestionIdForSelectedSubject");
	                	questionIdArr.add(questionID);
	                	client.sendToClient(questionIdArr);
	                	break;
	                	
	                case "GetQuestionsForLecturerBySubjectAndCourseToCreateExamTable":
	                	// 1 - Subject selected
	                	// 2 - Course Select
	                	ArrayList<Question> questionArr = DBController.getAllQuestions(null, arrayListStr.get(2), arrayListStr.get(1));
	                	questionArr.add(0, new Question("LoadQuestionsFromDB_CreateExamTable",  null, null, null, null, null, null, null));
	                	client.sendToClient(questionArr);
	                	break;
	                	
	                case "GetUpdateMaxExamIdFromCourse":
	                	// 1 - Course ID
	                	ArrayList<String> maxexamnumbercourse_arr = new ArrayList<>();
	                	maxexamnumbercourse_arr.add("MaxExamNumberOfCourse");
	                	maxexamnumbercourse_arr.add(DBController.getMaxExamIdFromCourse(arrayListStr.get(1)));
	                	client.sendToClient(maxexamnumbercourse_arr);
	                	
	                	break;
	                	
					case "GetAllComputerizedExamsFromDB": // Getting all the computerized Exams from the DB
						ArrayList<Exam> computerizedExams = new ArrayList<>();
						computerizedExams.add(new Exam("computerizedExamsForStudentTable",null,null,null,null,null,null,null,0,null,null,null));
						computerizedExams.addAll(DBController.getExamsByActiveness("1", null));
						client.sendToClient(computerizedExams);
						break;
						
					case "ChangeExamActiveness":
						// 1 - exam ID
						// 2 - the activeness to change to: 1 / 0
						DBController.changeExamActivenessByID(arrayListStr.get(1), arrayListStr.get(2));
						// check if the exam closed (activeness == 0) -> interrupt all the users
						// send to all clients a message that an exam was closed with the examID
						if(arrayListStr.get(2).equals("2")) {
							ArrayList<String> examActivenessChanged_arr = new ArrayList<>();
							examActivenessChanged_arr.add("an exam has been closed");
							examActivenessChanged_arr.add(arrayListStr.get(1));
							
							ObservableList<ConnectedClient> connectedClients = ServerPortFrameController.getConnectedClients();
							
							for(int i = 0; i<connectedClients.size(); i++) {
								if(connectedClients.get(i).getRole().equals("Student")) {
									connectedClients.get(i).getClient().sendToClient(examActivenessChanged_arr);
								}
							}
							client.sendToClient("exam is close");
						}
						else {
							client.sendToClient("exam is open");
						}
						break;				
						
					case "RequestToChangeAnExamDurationFromLecturerToHOD":
						// 1 - exam ID
						// 2 - subject name
						// 3 - course name
						// 4 - lecturer id that sent the request
						// 5 - lecturer name
						// 6 - lecturer's explanation
						// 7 - add to exam duration
						// 8 - head of department ID
						
						DBController.saveRequestForHodInDB(arrayListStr);
						
						ObservableList<ConnectedClient> connectedClients = ServerPortFrameController.getConnectedClients();
						ArrayList<String> requestrecieved_arr = new ArrayList<>();
						requestrecieved_arr.add("SendToHeadOfDepartmentsThatRequestRecieved");
						requestrecieved_arr.add(arrayListStr.get(8)); // hod ID
						for(int i = 0; i<connectedClients.size(); i++) {
							if(connectedClients.get(i).getRole().equals("HeadOfDepartment")) {
								connectedClients.get(i).getClient().sendToClient(requestrecieved_arr);
							}
						}
						client.sendToClient("request sent");
						break;
					case "getQuestionsInExamById": //Get Questions for exam

						ArrayList<QuestionInExam> questionInExamArray= new ArrayList<>();
						questionInExamArray.add(new QuestionInExam("questionsByExamIdForClient",null,null,null));
						questionInExamArray.addAll(DBController.retrieveQuestionsInExamById(arrayListStr.get(1)));
						client.sendToClient(questionInExamArray);
						break;
						
				    case "GetAllExamsFromDBtoManageExamsTablesByLecturerID":
				    	// 1 - lecturer ID
				    	ArrayList<Exam> activeExams_arr = new ArrayList<>();
				    	activeExams_arr.add(0, new Exam("loadActiveExamsIntoLecturerTable", null, null, null, null, null, null, null, 0, null, null, null));
				    	activeExams_arr.addAll(DBController.getExamsByActiveness("1", arrayListStr.get(1)));
				    	
				    	ArrayList<Exam> inActiveExams_arr = new ArrayList<>();
				    	inActiveExams_arr.add(0, new Exam("loadInActiveExamsIntoLecturerTable", null, null, null, null, null, null, null, 0, null, null, null));
				    	inActiveExams_arr.addAll(DBController.getExamsByActiveness("0", arrayListStr.get(1)));
				    	
				    	client.sendToClient(activeExams_arr);
				    	client.sendToClient(inActiveExams_arr);
				    	
				    	
				    	break;
				    	
				    case "GetRelevantHodForLecturer":
				    	// 1 - lecturer ID
				    	
				    	ArrayList<HeadOfDepartment> hod_arr = new ArrayList<>();
				    	hod_arr.add(0, new HeadOfDepartment("LoadRelevantHodForLecturer", null, null, null, null));
				    	hod_arr.addAll(DBController.getHeadOfDepartmentsByLecturer(arrayListStr.get(1)));
				    	client.sendToClient(hod_arr);
				    	
				    	break;
				    	
				    case "GetAllRequestsOfHodFromDB":
				    	// 1 - Head of department ID
				    	ArrayList<String> requests_arr = new ArrayList<>();
				    	requests_arr.add("LoadAllRequestsForHOD");
				    	requests_arr.addAll(DBController.getRequestsForHod(arrayListStr.get(1)));
				    	client.sendToClient(requests_arr);
				    	break;
				    	
					case "getStudentGradesById":
						ArrayList<FinishedExam> studentGrades = new ArrayList<>();
						studentGrades.add(new FinishedExam("studentGradesForClient",null,null,0,null, null, null));
						studentGrades.addAll(DBController.getAllStudentGradesById(arrayListStr.get(1)));
						client.sendToClient(studentGrades);
						break;
						
					case "RequestForChangeTimeInExamAccepted":
						// 1 - headofdepartment ID
						// 2 - lecturer ID
						// 3 - exam ID
						// 4 - exam Duration to Add
						// 5 - txt Message from hod to lecturer
						
						DBController.removeRequestForHodFromDB(arrayListStr.get(1), arrayListStr.get(2), arrayListStr.get(3), 
								arrayListStr.get(4));
						
						ObservableList<ConnectedClient> connectedClients2 = ServerPortFrameController.getConnectedClients();

						ArrayList<String> confirmation_to_lecturer_arr = new ArrayList<>();
						confirmation_to_lecturer_arr.add("RequestForChangeTimeAcceptedByHodToLecturer");
						confirmation_to_lecturer_arr.add(arrayListStr.get(2)); // lecturer ID
						confirmation_to_lecturer_arr.add(arrayListStr.get(3)); // exam ID
						confirmation_to_lecturer_arr.add(arrayListStr.get(4)); // exam Duration to Add
						confirmation_to_lecturer_arr.add(arrayListStr.get(5)); // txt Message from hod to lecturer
						
						ArrayList<String> confirmation_to_student_arr = new ArrayList<>();
						confirmation_to_student_arr.add("RequestForChangeTimeAcceptedByHodToStudent");
						confirmation_to_student_arr.add(arrayListStr.get(3)); // exam ID
						confirmation_to_student_arr.add(arrayListStr.get(4)); // exam Duration to Add
						
						for(int i = 0; i<connectedClients2.size(); i++) {
							if(connectedClients2.get(i).getRole().equals("Lecturer")) {
								connectedClients2.get(i).getClient().sendToClient(confirmation_to_lecturer_arr);
							}
							if(connectedClients2.get(i).getRole().equals("Student")) {
								connectedClients2.get(i).getClient().sendToClient(confirmation_to_student_arr);
							}
						}
						client.sendToClient("confirmation has been sent to the lecturer and students");
						
						break;
						
					case "RequestForChangeTimeInExamDenied":
						// 1 - headofdepartment ID
						// 2 - lecturer ID
						// 3 - exam ID
						// 4 - exam Duration to Add
						// 5 - txt Message from hod to lecturer
						
						DBController.removeRequestForHodFromDB(arrayListStr.get(1), arrayListStr.get(2), arrayListStr.get(3), 
								arrayListStr.get(4));
						
						ObservableList<ConnectedClient> connectedClients3 = ServerPortFrameController.getConnectedClients();

						ArrayList<String> deniy_to_lecturer_arr = new ArrayList<>();
						deniy_to_lecturer_arr.add("RequestForChangeTimeDeniedByHodToLecturer");
						deniy_to_lecturer_arr.add(arrayListStr.get(2)); // lecturer ID
						deniy_to_lecturer_arr.add(arrayListStr.get(3)); // exam ID
						deniy_to_lecturer_arr.add(arrayListStr.get(4)); // exam Duration to Add
						deniy_to_lecturer_arr.add(arrayListStr.get(5)); // txt Message from hod to lecturer
						
						for(int i = 0; i<connectedClients3.size(); i++) {
							if(connectedClients3.get(i).getRole().equals("Lecturer")) {
								connectedClients3.get(i).getClient().sendToClient(deniy_to_lecturer_arr);
							}
						}
						client.sendToClient("denied message has been sent to the lecturer");
						
						break;
						
					case "GetAllLecturerExamsForChecking":
						// 1 - lecturer Id
						// 2 - exam activeness change
						ArrayList<Exam> examstocheck_arr = new ArrayList<>();
						examstocheck_arr.add(new Exam("LoadAllExamsToCheckForLecturer", null, null, null, null, null, null, null, 0, null, null, null));
						examstocheck_arr.addAll(DBController.getExamsByActiveness(arrayListStr.get(2), arrayListStr.get(1)));
						client.sendToClient(examstocheck_arr);
						break;
						
					case "GetAllExamsByExamIDForLecturerForChecking":
						// 1 - Exam ID
						ArrayList<FinishedExam> studentsexamstocheck_arr = new ArrayList<>();
						studentsexamstocheck_arr.add(new FinishedExam("LoadAllStudentsFinishedExamsToCheckForLecturer", null, null, 0, null, null, null));
						studentsexamstocheck_arr.addAll(DBController.getFinishedExamsByExamID(arrayListStr.get(1)));
						client.sendToClient(studentsexamstocheck_arr);	
						break;
						
					case "GetQuestionsInExamToAproove":
						// 1 - Exam ID
						ArrayList<QuestionInExam> questionInExamarray = new ArrayList<>();
						questionInExamarray.add(new QuestionInExam("questionsInExamForLecturerApproval",null,null,null));
						questionInExamarray.addAll(DBController.retrieveQuestionsInExamById(arrayListStr.get(1)));
						client.sendToClient(questionInExamarray);
						break;
						
					case "ApproveAndSetGradeForFinishedExamByLecturer":
						// 1 - Exam ID
						// 2 - Student ID
						// 3 - Grade
						// 4 - Course Name
						// 5 - lecturer Name
						// 6 - Comments for Student
						// 7 - Comment For New Grade
						
						DBController.setFinishedExamApproved(arrayListStr.get(1), arrayListStr.get(2), arrayListStr.get(3));
						String studentEmail = DBController.getStudentEmailByID(arrayListStr.get(2));
						
						ObservableList<ConnectedClient> connectedClients4 = ServerPortFrameController.getConnectedClients();

						ArrayList<String> newgrademessage_arr = new ArrayList<>();
						newgrademessage_arr.add("NewGradeIsAvailableForStudent");
						newgrademessage_arr.add(arrayListStr.get(2)); // Student ID
						newgrademessage_arr.add(arrayListStr.get(3)); // Grade
						newgrademessage_arr.add(arrayListStr.get(4)); // Course Name
						newgrademessage_arr.add(arrayListStr.get(5));// lecturer Name
						newgrademessage_arr.add(arrayListStr.get(6));// Comments for Student
						newgrademessage_arr.add(arrayListStr.get(7));// Comment For New Grade
						
						for(int i = 0; i<connectedClients4.size(); i++) {
							if(connectedClients4.get(i).getRole().equals("Student")) {
								connectedClients4.get(i).getClient().sendToClient(newgrademessage_arr);
							}
						}
						ArrayList<String> messageForLecturer_arr = new ArrayList<>();
						messageForLecturer_arr.add("Exam approved send message to lecturer");
						messageForLecturer_arr.add(studentEmail);
						client.sendToClient(messageForLecturer_arr);
						
						
						// send message to student with lecturer name and exam id and course name of exam
						break;
						
					case "GetAllInfoOfFinishedExamForLecturer":
						// 1 - lecturer ID
						
						ArrayList<FinishedExam> finishedexams_grades_forlecturer = new ArrayList<>();
						finishedexams_grades_forlecturer.add(new FinishedExam("Load all finished exams grades and info for lecturer", null, null, 0, null, null, null));
						finishedexams_grades_forlecturer.addAll(DBController.getFinishedExamsInfoByAuthorID(arrayListStr.get(1)));
						client.sendToClient(finishedexams_grades_forlecturer);
						break;

					case "notifyServerStudentBegunExam":

						if(!undergoingExams.containsKey(arrayListStr.get(1))){
							undergoingExams.put(arrayListStr.get(1),new ArrayList<Integer>(List.of(1,0,0)));
						}else{
							ArrayList<Integer> values = undergoingExams.get(arrayListStr.get(1));
							values.set(0,values.get(0) + 1);
						}
						//System.out.println(undergoingExams);
						client.sendToClient("notifyServerStudentBegunExam - Received");
						break;

					case "notifyServerStudentFinishedExam":

						ArrayList<Integer> values1 = undergoingExams.get(arrayListStr.get(1));
						values1.set(1,values1.get(1) +1 );

						if(values1.get(0) == (values1.get(1) + values1.get(2))){
							//Got to DB and set the exam to closed state, '2' and save participants for statistics
							DBController.saveExamStatisticsToDB(arrayListStr.get(1),arrayListStr.get(2),values1.get(0),values1.get(1),values1.get(2));
							DBController.changeExamActivenessByID(arrayListStr.get(1),"2");
						}
						//System.out.println(undergoingExams);
						client.sendToClient("notifyServerStudentFinishedExam - Received");
						break;

					case "notifyServerStudentNotFinishedExam":

						ArrayList<Integer> values2 = undergoingExams.get(arrayListStr.get(1));
						values2.set(2,values2.get(2)+1);

						if(values2.get(0) == (values2.get(1) + values2.get(2))){
							//Got to DB and set the exam to closed state, '2' and save participants for statistics
							DBController.saveExamStatisticsToDB(arrayListStr.get(1),arrayListStr.get(2),values2.get(0),values2.get(1),values2.get(2));
							DBController.changeExamActivenessByID(arrayListStr.get(1),"2");
						}
						//System.out.println(undergoingExams);
						client.sendToClient("notifyServerStudentNotFinishedExam - Received");
						break;
						
					case "GetStatisticsOfExamByExamIDFromDB":
						// 1 - exam ID
						ArrayList<String> sttistics_arr = new ArrayList<>();
						sttistics_arr.add("LoadStatisticsOfExamByIdForLecturer");
						sttistics_arr.addAll(DBController.getStatisticsOfExamByID(arrayListStr.get(1)));
						client.sendToClient(sttistics_arr);
						break;
						
					case "GetAllOptionForDataReport_HOD":
						// 1 - headofdepartment Id
						// 2 - chosen Report
						
						ArrayList<String> allOptionDataforreport_arr = new ArrayList<>();
						allOptionDataforreport_arr.add("LoadToListOfDataForHOD_report");
						
						switch(arrayListStr.get(2)) {
						
						case "Students":
							allOptionDataforreport_arr.addAll(DBController.getAllStudentsOfHod(arrayListStr.get(1)));
							
							break;
							
						case "Lecturers":
							allOptionDataforreport_arr.addAll(DBController.getAllLecturersOfHod(arrayListStr.get(1)));
							
							break;
						
						case "Courses":
							allOptionDataforreport_arr.addAll(DBController.getAllCoursesOfHod(arrayListStr.get(1)));
							
							break;

						}
						
						client.sendToClient(allOptionDataforreport_arr);
						
						break;
						
					case "GetAllGradesForReport_HOD":
						
						// 1 - chosen Report
						// 2 - id of selected report to show: studentID / lecturerID / courseID
						
						ArrayList<String> gradesforreport_arr = new ArrayList<>();
						gradesforreport_arr.add("LoadToGradesToChart_HOD");
						
						switch(arrayListStr.get(1)) {
						
						case "Students":
							gradesforreport_arr.addAll(DBController.getAllStudentGrades(arrayListStr.get(2)));
							
							break;
							
						case "Lecturers":
							gradesforreport_arr.addAll(DBController.getAllLecturerGrades(arrayListStr.get(2)));
							
							break;
						
						case "Courses":
							gradesforreport_arr.addAll(DBController.getAllCourseGrades(arrayListStr.get(2)));
							
							break;

						}
						
						client.sendToClient(gradesforreport_arr);
						
						break;
					case "GetAllManualExamsFromDB":
						//System.out.println("Reached GetAllManualExams From DB case");
						ArrayList<Exam> manualExams = new ArrayList<>();
						manualExams.add(new Exam("manualExamsForStudentTable",null,null,null,null,null,null,null,0,null,null,null));
						manualExams.addAll(DBController.getManualExamsByActiveness("1", null));
						client.sendToClient(manualExams);
						break;
					case "downloadManualExamFromServer":
						String path = getManualExamPath(arrayListStr.get(1));

						// Read the file and convert it to a byte array
						File file = new File(path);
						byte[] fileData;
						try {
							FileInputStream fileInputStream = new FileInputStream(file);
							fileData = new byte[(int) file.length()];
							fileInputStream.read(fileData);
							fileInputStream.close();
						} catch (IOException e) {
							// Handle any errors that occur during file reading
							e.printStackTrace();
							break;
						}

						// Create a new MyFile object
						MyFile myFile = new MyFile(file.getName());
						myFile.setSize(fileData.length);
						myFile.initArray(fileData.length);
						myFile.setMybytearray(fileData);

						// Send the MyFile object to the client
						client.sendToClient(myFile);
						break;
						
					case "GetAllGeneralInfo_HOD":
						
						// 1 - headofdepartment Id
						// 2 - chosen Info
						ArrayList<String> info_arr = new ArrayList<>();
						info_arr.add("LoadAllSelectedGeneralInfo_HOD");
						switch(arrayListStr.get(2)) {
						
						case "Subjects":
							info_arr.addAll(DBController.GeneralInformationSubjects(arrayListStr.get(1)));
							
							break;
							
						case "Courses":
							info_arr.addAll(DBController.getAllCoursesOfHod(arrayListStr.get(1)));
							
							break;
							
						case "Lecturers":
							info_arr.addAll(DBController.getAllLecturersOfHod(arrayListStr.get(1)));
							
							break;
							
						case "Students":
							info_arr.addAll(DBController.getAllStudentsOfHod(arrayListStr.get(1)));
							
							break;
							
						case "Exams":
							info_arr.addAll(DBController.GeneralInformationExams(arrayListStr.get(1)));
							
							break;
							
						case "Questions":
							info_arr.addAll(DBController.GeneralInformationQuestions(arrayListStr.get(1)));
							
							break;
						
						
						}
						client.sendToClient(info_arr);
						
						break;


				}
            }catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            }
        }

	/**
	 * Handles the received ArrayList of Question objects and performs the necessary actions.
	 *
	 * @param questionList the ArrayList of Question objects
	 * @param client the ConnectionToClient representing the client connection
	 */
    private static void handleQuestionArrayListMessage(ArrayList<Question> questionList, ConnectionToClient client) {
		//System.out.println("Reached the handleQuestionArrayListMessage method");
        // Handle ArrayList<Question> messages
    	// 1 - question to add
    	String messageType = questionList.get(0).getId();

    	try {
	    	switch (messageType) {
	    	
	    		case "AddNewQuestionToDB":

	    			// Handle AddNewQuestionToDB message
	    			// 1 - newQuestion
	    			DBController.addNewQuestion(questionList.get(1));
	    			client.sendToClient("new question was added");
	
					break;
	    	} 
	    	
        }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
        }
    	
    }

	/**
	 * Handles the received ArrayList of QuestionInExam objects and performs the necessary actions.
	 *
	 * @param questionInExamList the ArrayList of QuestionInExam objects
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleQuestionInExamArrayListMessage(ArrayList<QuestionInExam> questionInExamList, ConnectionToClient client) {
        // Handle ArrayList<QuestionInExam> messages
		//System.out.println("Reached the handleQuestionInExamArrayListMessage method");

    	String messageType = questionInExamList.get(0).getId();

    	try {
	    	switch (messageType) {
	    	
	    		case "SaveAllQuestionsInExam":
	    			// in the question 0, the question text saved the exam id
	    			// questionInExamList from 1 - all questions in exam to add
	    			DBController.addNewQuestionsInExam(questionInExamList);
	    			client.sendToClient("questions in exam added");
	
					break;
	    	} 
	    	
        }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
        }
	}
	/**
	 * Handles the received ArrayList of Exam objects and performs the necessary actions.
	 *
	 * @param examList the ArrayList of Exam objects
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleExamArrayListMessage(ArrayList<Exam> examList, ConnectionToClient client) {
        // Handle ArrayList<Exam> messages
		//System.out.println("Reached the handleExamArrayListMessage method");

    	String messageType = examList.get(0).getExamID();

    	try {
	    	switch (messageType) {
	    	
	    		case "SaveExamInDB":
	    			// Handle AddNewQuestionToDB message
	    			// 1 - new exam
	    			DBController.addNewExam(examList.get(1));
	    			client.sendToClient("new exam was added");
	
					break;
	    	} 
	    	
        }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
        }
    	
    }

	/**
	 * Handles the received Map of String keys and ArrayList<String> values.
	 *
	 * @param map the Map of String keys and ArrayList<String> values
	 * @param client the ConnectionToClient representing the client connection
	 */
    private static void handleMapStringKeyArrayListStringValueMessage(Map<String, ArrayList<String>> map, ConnectionToClient client) {
        // Handle Map<String, ArrayList<String>> messages
    	

    }

	/**
	 * Handles the received Map of String keys and String values.
	 *
	 * @param msg the Map of String keys and String values
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleMapStringStringValueMessage(Map<String, String> msg, ConnectionToClient client) {
		// Handle Map<String, String> messages
		
	}

	/**
	 * Handles the received ArrayList of FinishedExam objects and performs the necessary actions.
	 *
	 * @param finishedExam the ArrayList of FinishedExam objects
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleFinishedExamArrayListValueMessage(ArrayList<FinishedExam> finishedExam, ConnectionToClient client){
		//Handle ArrayList<FinishedExam> messages
		//System.out.println("Reached handleFinishedExamValueMessage | Server Handler");
		String messageType = finishedExam.get(0).getExamID();
		try{
		switch (messageType){
			case "saveFinishedExamToDB":
				//finishedExam.remove(0);
				if(DBController.saveFinishedExamToDB(finishedExam.get(1))) {
					client.sendToClient("Exam was saved in the DB");
				}
				else {
					client.sendToClient("The exam is already finished.");
				}
				break;
		}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles the received MyFile object and saves the file to the server.
	 *
	 * @param myFile the MyFile object containing the file data
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleMyFileValueMessage(MyFile myFile, ConnectionToClient client) {
		//System.out.println("Reached handleMyFileValueMessage | Server Handler");
		try {
			// Create the directory if it doesn't exist
			File directory = new File("C:\\SubmitedExams");
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Create the file path using the directory and the received file name
			String filePath = directory.getPath() + File.separator + myFile.getFileName();

			// Write the byte array to the file
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			fileOutputStream.write(myFile.getMybytearray());
			fileOutputStream.close();

			// Send a confirmation message to the client
			client.sendToClient("MyFile Received and Saved Successfully");
		} catch (IOException e) {
			// Handle any errors that occur during file saving
			e.printStackTrace();
			try {
				// Send an error message to the client
				client.sendToClient("Error in Saving MyFile");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

    
}
