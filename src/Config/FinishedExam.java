package Config;

public class FinishedExam{

    private String examID;
    private String studentID;
    private String lecturerID;
    private double grade;
    private int approved;
    private int checkExam;
    private String answers;

    public FinishedExam(String examID,String lecturerID ,String studentID, double grade,String answers){
        this.examID = examID;
        this.studentID = studentID;
        this.lecturerID = lecturerID;
        this.grade = grade;
        this.answers = answers;
        this.approved = 0;
        this.checkExam = 0;
    }
    public String getExamID() {
        return examID;
    }

    public String getStudentID() {
        return studentID;
    }

    public double getGrade() {
        return grade;
    }

    public void approveGrade(){
        this.grade = 1;
    }
    public void checkExam(){
        this.checkExam = 1;
    }

    public int getApproved() {
        return approved;
    }

    public int getCheckExam() {
        return checkExam;
    }

    public String getAnswers() {
        return answers;
    }

    public String getLecturerID() {
        return lecturerID;
    }
}
