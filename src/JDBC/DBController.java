package JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Config.Exam;
import Config.Question;
import Config.QuestionInExam;

public class DBController {
	
	
	// func to get all questions from DB that created by specific lecturer and/or specific courseName and/or subjectID.
	public static ArrayList<Question> getAllQuestions(String lecturerID, String courseID, String subjectID) {
	    ArrayList<Question> questions = new ArrayList<Question>();
	    try {
	        try {
	            if (mysqlConnection.getConnection() != null) {
	                PreparedStatement ps = null;
	                if (lecturerID == null && courseID == null && subjectID == null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID");
	                } else if (lecturerID != null && courseID == null && subjectID == null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE q.lecturerID = ?");
	                    ps.setString(1, lecturerID);
	                } else if (lecturerID == null && courseID != null && subjectID == null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE qc.courseID = ?");
	                    ps.setString(1, courseID);
	                } else if (lecturerID == null && courseID == null && subjectID != null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE q.subjectID = ?");
	                    ps.setString(1, subjectID);
	                } else if (lecturerID != null && courseID != null && subjectID == null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE q.lecturerID = ? AND qc.courseID = ?");
	                    ps.setString(1, lecturerID);
	                    ps.setString(2, courseID);
	                } else if (lecturerID != null && courseID == null && subjectID != null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE q.lecturerID = ? AND q.subjectID = ?");
	                    ps.setString(1, lecturerID);
	                    ps.setString(2, subjectID);
	                } else if (lecturerID == null && courseID != null && subjectID != null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE qc.courseID = ? AND q.subjectID = ?");
	                    ps.setString(1, courseID);
	                    ps.setString(2, subjectID);
	                } else if (lecturerID != null && courseID != null && subjectID != null) {
	                    ps = mysqlConnection.getConnection().prepareStatement("SELECT q.*, qc.courseID, c.Name, s.SubjectID, s.Name FROM question q JOIN questioncourse qc ON q.id = qc.questionID JOIN course c ON qc.courseID = c.courseID JOIN subjects s ON q.subjectID = s.SubjectID WHERE qc.lecturerID = ? AND qc.courseID = ? AND q.subjectID = ?");
	                    ps.setString(1, lecturerID);
	                    ps.setString(2, courseID);
	                    ps.setString(3, subjectID);
	                }

	                ResultSet rs = ps.executeQuery();
	                while (rs.next()) {
	                    String questionId = rs.getString(1);

	                    // Check if the question with the same ID already exists
	                    Question existingQuestion = null;
	                    for (Question question : questions) {
	                        if (question.getId().equals(questionId)) {
	                            existingQuestion = question;
	                            break;
	                        }
	                    }

	                    // If the question exists, update its courses
	                    if (existingQuestion != null) {
	                        String courseid = rs.getString("courseID");
	                        String courseName = rs.getString("c.Name");
	                        if (courseid != null && courseName != null) {
	                            String[] courseArray = courseid.split(",");
	                            for (String course : courseArray) {
	                                existingQuestion.getCourses().put(course, courseName);
	                            }
	                        }
	                    } else {
	                        ArrayList<String> answers = new ArrayList<>();
	                        answers.add(rs.getString(5));
	                        answers.add(rs.getString(6));
	                        answers.add(rs.getString(7));
	                        answers.add(rs.getString(8));

	                        // Create a map to store the courses for the question
	                        Map<String, String> courses = new HashMap<>();
	                        String courseid = rs.getString("courseID");
	                        String courseName = rs.getString("c.Name");
	                        if (courseid != null && courseName != null) {
	                            String[] courseArray = courseid.split(",");
	                            for (String course : courseArray) {
	                                courses.put(course, courseName);
	                            }
	                        }
	                        
	                        // Create an array to store the subjects for the question
	                        ArrayList<String> subject = new ArrayList<>();
	                        String subjectId = rs.getString("SubjectID");
	                        String subjectName = rs.getString("s.Name");
	                        if (subjectId != null && subjectName != null) {
	                            subject.add(subjectId);
	                            subject.add(subjectName);
	                        }
	                        
	                        Question question = new Question(questionId, subject, courses, rs.getString(3), answers, rs.getString(4), rs.getString(9), rs.getString(10));


	                        questions.add(question);
	                    }
	                }
	                rs.close();
	            } else {
	                System.out.println("myConn is NULL!");
	            }
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return questions;
	}




	
	public static String UpdateQuestionDataByID(ArrayList<String> qArr) {
		// 1 - question ID
		// 2 - question text
		// 3 - correct answer
		// 4 - wrong answer1
		// 5 - wrong answer2
		// 6 - wrong answer3
		try {
			if (mysqlConnection.getConnection() != null) {
				PreparedStatement ps = mysqlConnection.getConnection().prepareStatement("UPDATE `question` SET `questionText` =?, "
						+ "`answerCorrect` =?, `answerWrong1` =?, `answerWrong2` =?, `answerWrong3` =? "
						+ "WHERE (`id` =?);");
				ps.setString(1,qArr.get(2));
				ps.setString(2,qArr.get(3));
				ps.setString(3,qArr.get(4));
				ps.setString(4,qArr.get(5));
				ps.setString(5,qArr.get(6));
				ps.setString(6,qArr.get(1));
		 		ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Question updated succesfully";
	}
	
	public static ArrayList<String> userExist(ArrayList<String> userInfoArr) throws ClassNotFoundException {
		// 1 - loginAs: Lecturer, Student, Head Of Department
		// 2 - Username
		// 3 - Password
		ArrayList<String> userCannotConnect = new ArrayList<>(); // an array if there was a problem with sign in - wrond pass/username or user already exist
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement("SELECT EXISTS (SELECT * FROM " + userInfoArr.get(1) + " WHERE UserName =? AND Password =?)");
	            ps.setString(1, userInfoArr.get(2));
	            ps.setString(2, userInfoArr.get(3));
	
	            ResultSet resultSet = ps.executeQuery();
	            if(resultSet.next()) {
	                if(resultSet.getInt(1) == 1) {
	                	ArrayList<String> getUserDetails_arr = getUserDetails(userInfoArr.get(1), userInfoArr.get(2), userInfoArr.get(3));
	                	// check if the user is already logged in by his ID and loginAs
	                	if(!UserAlreadyLoggedin(getUserDetails_arr.get(0), userInfoArr.get(1))) {
	                		// userInfoArr.get(1) - loginAs, getUserDetails_arr.get(0) - user ID
	                		setUserIsLogin("1", userInfoArr.get(1), getUserDetails_arr.get(0));
	                		return getUserDetails_arr;
	                	}
	                	else {
	                		userCannotConnect.add("UserAlreadyLoggedIn");
	                		return userCannotConnect;
	                	}	
	                }
	            }
	            userCannotConnect.add("UserEnteredWrondPasswwordOrUsername");
	            return userCannotConnect;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {}
		return null;
	}
	
	// set the user 'isLogin' in DB to 1 or 0 by his ID, loginAs = {Lecturer, Student, Head Of Department} - the table
	// if id is "all", do for all users in table
	public static void setUserIsLogin(String loggedInStatus, String loginAs, String id) {
		try {
			if (mysqlConnection.getConnection() != null) {
				PreparedStatement ps;
				if(id.equals("all")) {
					ps = mysqlConnection.getConnection().prepareStatement("UPDATE "+ loginAs +" SET `isLogged` =?;");	
					ps.setString(1, loggedInStatus);
				}
				else {
					String userId = loginAs + "id";
					ps = mysqlConnection.getConnection().prepareStatement("UPDATE "+ loginAs +" SET `isLogged` =? WHERE (`" + userId + "` =?);");
					ps.setString(1, loggedInStatus);
					ps.setString(2, id);
				}
		 		ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// func to return details of user by his username and password
	public static ArrayList<String> getUserDetails(String loginAs, String username, String password) {
	    String query = "SELECT * FROM " + loginAs + " WHERE username = ? AND password = ?";
	    ArrayList<String> userDetailsArr = new ArrayList<>();
	    try {
	    	if (mysqlConnection.getConnection() != null) {
	    		PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	    		ps.setString(1, username);
	    		ps.setString(2, password);
	    		ResultSet resultSet = ps.executeQuery();
	    		if(resultSet.next()) {
	    			for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
	    				userDetailsArr.add(resultSet.getString(i));
	    			}
	    		}
		    } 
	    } catch (SQLException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
		  // 0 - user ID
		  // 1 - userName
		  // 2 - user Password
		  // 3 - user Name
		  // 4 - user Email
		  // 5 - isLogged
	    return userDetailsArr;
	}
	
	// check if the user is already logged in
	public static boolean UserAlreadyLoggedin(String userID, String logginAs) {
		String userId = logginAs + "id";
		String query = "SELECT IF(isLogged = 1, true, false) AS isLoggedIn FROM " + logginAs + " WHERE " + userId + "= ?";
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            ps.setString(1, userID);
	            try (ResultSet resultSet = ps.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getBoolean("isLoggedIn"); // isLoggedIn: true / false
	                }
	            }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}

	public static boolean removeQuestion(String questionID) {
	    String deleteQuestionQuery = "DELETE FROM question WHERE id = ?";
	    String deleteQuestionCourseQuery = "DELETE FROM questioncourse WHERE questionID = ?";
	    try {
	        if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps1 = mysqlConnection.getConnection().prepareStatement(deleteQuestionCourseQuery);
	            ps1.setString(1, questionID);
	            ps1.executeUpdate();

	            PreparedStatement ps2 = mysqlConnection.getConnection().prepareStatement(deleteQuestionQuery);
	            ps2.setString(1, questionID);
	            ps2.executeUpdate();

	            return true;
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	public static void setAllUsersNotIsLogged() {
		setUserIsLogin("0", "lecturer", "all");
		setUserIsLogin("0", "student", "all");
		setUserIsLogin("0", "headofdepartment", "all");
	}

public static Map<String, ArrayList<String>> getLecturerSubjectCourses(String lecturerID) {
		
		Map<String, ArrayList<String>> lecDepartmentCoursesMap = new HashMap<>();
		
		String query = "SELECT subjects.Name AS SubjectName, course.Name AS CourseName "
				+ "FROM subjects "
				+ "JOIN coursesubject ON subjects.SubjectID = coursesubject.SubjectID "
				+ "JOIN lecturercourse ON coursesubject.CourseID = lecturercourse.CourseID "
				+ "JOIN course ON coursesubject.CourseID = course.CourseID "
				+ "WHERE lecturercourse.LecturerID = ?";
		
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            ps.setString(1, lecturerID);
	            try (ResultSet resultSet = ps.executeQuery()) {
	            	while (resultSet.next()) {
	            	    String subjectName = resultSet.getString("SubjectName");
	            	    String courseName = resultSet.getString("CourseName");

	            	    //Check if the subject is already in the map
	            	    if (lecDepartmentCoursesMap.containsKey(subjectName)) {
	            	        //Add the course to the existing list of courses for the subject
	            	    	lecDepartmentCoursesMap.get(subjectName).add(courseName);
	            	    } else {
	            	        //Create a new list and add the course
	            	        ArrayList<String> courses = new ArrayList<>();
	            	        courses.add(courseName);
	            	        lecDepartmentCoursesMap.put(subjectName, courses);
	            	    }
	            	}
	            }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Print the subjects and their corresponding courses
		/*for (Entry<String, ArrayList<String>> entry : lecDepartmentCoursesMap.entrySet()) {
		    String subject = entry.getKey();
		    ArrayList<String> courses = entry.getValue();

		    System.out.println("Subject: " + subject);
		    System.out.println("Courses:");

		    for (String course : courses) {
		        System.out.println("- " + course);
		    }
		    //Optional line to add a blank line between subjects
		    System.out.println();
		}*/
		
		return lecDepartmentCoursesMap;
	}

	public static String getMaxQuestionIdFromSubject(String subjectID) {
		String query = "SELECT subjects.MaxQuestionNumber "
		        + "FROM subjects "
		        + "WHERE SubjectID = ?";
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            ps.setString(1, subjectID);
	            try (ResultSet rs = ps.executeQuery()) {
	                if(rs.next()) {
	                	return rs.getString(1);
	                }
	            }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}

	public static void addNewQuestion(Question question) {
		String query = "INSERT INTO question (id, subjectID, questionText, questionNumber, answerCorrect, answerWrong1"
				+ ", answerWrong2, answerWrong3, lecturer, lecturerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int i = 0;
	    try {
	    	
	    	if (mysqlConnection.getConnection() != null) {
	    		PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	    		ps.setString(1, question.getId());
	    		ps.setString(2, question.getSubjectId());
	    		ps.setString(3, question.getQuestionText());
	    		ps.setString(4, question.getQuestionNumber());
	    		ps.setString(5, question.getAnswers().get(0));
	    		ps.setString(6, question.getAnswers().get(1));
	    		ps.setString(7, question.getAnswers().get(2));
	    		ps.setString(8, question.getAnswers().get(3));
	    		ps.setString(9, question.getLecturer());
	    		ps.setString(10, question.getLecturerID());
	    		ps.executeUpdate();
	    		
		    	for(String courseid : question.getCourses().keySet()) {
		    		PreparedStatement ps2 = mysqlConnection.getConnection().prepareStatement("INSERT INTO questioncourse (questionID, courseID) VALUES (?, ?)");
		    		ps2.setString(1, question.getId());
		    		ps2.setString(2, courseid);
		    		ps2.executeUpdate();
		    	}
	    		
	    	}

	    	updateSubjectMaxQuestionNumber(question.getSubjectId(), 1);
		
	    } catch (SQLException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	}

	private static void updateSubjectMaxQuestionNumber(String subjectID, int i) { // @@@@@@@@@@@@@@@@@@@@@
		
		String maxNumQuestion= getMaxQuestionIdFromSubject(subjectID); // 005
		// i = 4
		 // 009
		
		String query = "UPDATE subjects "
	             + "SET MaxQuestionNumber = ? "
	             + "WHERE SubjectID = ?";
		try {
			if (mysqlConnection.getConnection() != null) {
				PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
				
				maxNumQuestion = Integer.toString(Integer.parseInt(maxNumQuestion) + i);
				maxNumQuestion = String.format("%03d", Integer.parseInt(maxNumQuestion));
				
				
				ps.setString(1, maxNumQuestion);
				
				ps.setString(2, subjectID);
		 		ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, String> getAllSubjectsNamesAndIds() {
		
		String query = "SELECT subjects.SubjectID, subjects.Name FROM subjects";
		Map<String, String> map = new HashMap<>();
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            try (ResultSet rs = ps.executeQuery()) {
	                while(rs.next()) {
	                	map.put(rs.getString(1), rs.getString(2));
	                }
	            }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, String> getAllCoursesNamesAndIds() {
		
		String query = "SELECT course.CourseID, course.Name FROM course";
		Map<String, String> map = new HashMap<>();
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            try (ResultSet rs = ps.executeQuery()) {
	                while(rs.next()) {
	                	map.put(rs.getString(1), rs.getString(2));
	                }
	            }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String getMaxExamIdFromCourse(String courseID) {
		String query = "SELECT course.MaxExamNumber "
		        + "FROM course "
		        + "WHERE CourseID = ?";
		try {
			if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            ps.setString(1, courseID);
	            try (ResultSet rs = ps.executeQuery()) {
	                if(rs.next()) {
	                	updateCourseMaxExamNumber(courseID, rs.getString(1), 1);
	                	return rs.getString(1);
	                }
	            }
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void updateCourseMaxExamNumber(String courseID, String maxExamNumber_temp, int i) {
		
		String maxExamNumber = maxExamNumber_temp;
		
		String query = "UPDATE course "
	             + "SET MaxExamNumber = ? "
	             + "WHERE CourseID = ?";
		try {
			if (mysqlConnection.getConnection() != null) {
				PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
				
				maxExamNumber = Integer.toString(Integer.parseInt(maxExamNumber_temp) + i);
				maxExamNumber = String.format("%02d", Integer.parseInt(maxExamNumber));
				
				
				ps.setString(1, maxExamNumber);
				
				ps.setString(2, courseID);
		 		ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	public static void addNewExam(Exam exam) {
		
		
		/*
		 * String examID, String subjectID, String subjectName, String courseID, String courseName, ArrayList<QuestionInExam> questions, 
			String commentsForLecturer, String commentsForStudent, int duration, String author, String code)
		 * 
		 * 
		 */
		
		String query = "INSERT INTO exams (ID, subjectID, courseID, commentsLecturer, commentsStudents, duration"
				+ ", author, questionsInExam, code, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try {
	    	
	    	if (mysqlConnection.getConnection() != null) {
	    		PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	    		ps.setString(1, exam.getExamID());
	    		ps.setString(2, exam.getSubjectID());
	    		ps.setString(3, exam.getCourseID());
	    		ps.setString(4, exam.getCommentsForLecturer());
	    		ps.setString(5, exam.getCommentsForStudent());
	    		ps.setInt(6, exam.getDuration());
	    		ps.setString(7, exam.getAuthor());	
	    		ArrayList<String> questionsID = new ArrayList<>();
	    		for(int i = 0; i<exam.getQuestions().size(); i++) {
	    			questionsID.add(exam.getQuestions().get(i).getId());
	    		}		
	    		ps.setString(8, String.join(",", questionsID));
	    		ps.setString(9, exam.getCode());
	    		ps.setString(10, "0");
	    		ps.executeUpdate();
	    		
	    	}
		
	    } catch (SQLException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	}


	public static void addNewQuestionsInExam(ArrayList<QuestionInExam> questionsList) {
		String query = "INSERT INTO questionsexam (questionID, examID, questionText, answerCorrect, answerWrong1, answerWrong2"
				+ ", answerWrong3, points) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try {
	    	
	    	String examID = questionsList.get(0).getQuestionText(); // the exam id is here
	    	questionsList.remove(0);
	    	if (mysqlConnection.getConnection() != null) {
	    		
	    		
	    		for(QuestionInExam question : questionsList) {
	    			PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	    			ps.setString(1, question.getId());
	    			ps.setString(2, examID);
	    			ps.setString(3, question.getQuestionText());
	    			ps.setString(4, question.getAnswers().get(0));
	    			ps.setString(5, question.getAnswers().get(1));
	    			ps.setString(6, question.getAnswers().get(2));
	    			ps.setString(7, question.getAnswers().get(3));
	    			ps.setDouble(8, question.getPoints());
	    			ps.executeUpdate();
	    		}
	    		
	    	}
		
	    } catch (SQLException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
		
	}
	
	public static ArrayList<Exam> getExamsByActive(String active) {
	    String query = "SELECT exams.*, course.Name AS courseName " +
	                   "FROM exams " +
	                   "JOIN course ON exams.courseID = course.CourseID " +
	                   "WHERE exams.isActive = ?";
	    ArrayList<Exam> examsArr = new ArrayList<>();
	    try {
	        if (mysqlConnection.getConnection() != null) {
	            PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
	            ps.setString(1, active);
	            try (ResultSet rs = ps.executeQuery()) {
	                while(rs.next()) {
	                    examsArr.add(new Exam(rs.getString(1), rs.getString(2), null, rs.getString(3), rs.getString("courseName"), null, null, null, rs.getInt(6), rs.getString(7), rs.getString(9)));
	                }
	            }
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return examsArr;
	}


	public static ArrayList<Exam> getComputerizedExams() {
		ArrayList<Exam> activeExams = new ArrayList<>();

		try {
			if (mysqlConnection.getConnection() != null) {
				// Retrieve active exams
				String examQuery = "SELECT * FROM exams WHERE isActive = 1";
				PreparedStatement examPS = mysqlConnection.getConnection().prepareStatement(examQuery);

				ResultSet examRS = examPS.executeQuery();

				while (examRS.next()) {
					// Create an Exam object and populate its fields
					String examID = examRS.getString("ID");
					String subjectID = examRS.getString("subjectID");
					String subjectName = ""; // Set the subjectName accordingly from the database
					String courseID = examRS.getString("courseID");
					String courseName = ""; // Set the courseName accordingly from the database
					String commentsForLecturer = examRS.getString("commentsLecturer");
					String commentsForStudent = examRS.getString("commentsStudents");
					int duration = examRS.getInt("duration");
					String author = examRS.getString("author");
					String code = examRS.getString("code");

					// Parse the questionsInExam field to retrieve the questions and set them in the exam object
					String[] questionIDs = examRS.getString("questionsInExam").split(",");
					ArrayList<QuestionInExam> questions = new ArrayList<>();
					for (String questionID : questionIDs) {
						// Retrieve the Question object based on the questionID from the database or any other source
						QuestionInExam questionInExam = retrieveQuestionsByExamId(questionID,examID);

						if (questionInExam != null) {
							questions.add(questionInExam);
						}
					}

					Exam exam = new Exam(examID, subjectID, subjectName, courseID, courseName, questions, commentsForLecturer, commentsForStudent, duration, author, code);

					activeExams.add(exam);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return activeExams;
	}


	//A method to return the question by ID from the DB
	private static QuestionInExam retrieveQuestionsByExamId(String questionID, String examID) {
		QuestionInExam returnQuestions = new QuestionInExam(null, null, null, null);

		try {
			if (mysqlConnection.getConnection() != null) {
				String query = "SELECT * FROM questionsexam WHERE questionID = ? AND examID = ?";
				PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);
				ps.setString(1, questionID);
				ps.setString(2, examID);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					String id = rs.getString("questionID");
					String subjectID = rs.getString("examID");
					String questionText = rs.getString("questionText");
					String answerCorrect = rs.getString("answerCorrect");
					String answerWrong1 = rs.getString("answerWrong1");
					String answerWrong2 = rs.getString("answerWrong2");
					String answerWrong3 = rs.getString("answerWrong3");
					int points = rs.getInt("points");

					ArrayList<String> subject = new ArrayList<>();
					subject.add(subjectID);

					ArrayList<String> answers = new ArrayList<>();
					answers.add(answerCorrect);
					answers.add(answerWrong1);
					answers.add(answerWrong2);
					answers.add(answerWrong3);

					QuestionInExam questionInExam = new QuestionInExam(id, questionText, answers, null);
					questionInExam.setPoints((double) points);
					returnQuestions = questionInExam;
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return returnQuestions;
	}





	public static void changeExamActivenessByID(String examID, String activenessChangeTo) {

		String query = "UPDATE exams "
	             + "SET isActive = ? "
	             + "WHERE ID = ?";
		try {
			if (mysqlConnection.getConnection() != null) {
				PreparedStatement ps = mysqlConnection.getConnection().prepareStatement(query);		
				ps.setString(1, activenessChangeTo);
				ps.setString(2, examID);
		 		ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}






}

