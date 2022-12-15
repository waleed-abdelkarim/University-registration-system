import java.util.ArrayList;

public class Student {

    private int studentId; // save StudentID
    private String name; // save Student name
    private int age; // save Student age
    private String gender; // save Student gender M|F
    private String department; // save Student department (IS)
    private double GPA; // save Student GPA
    private int totalHours; // save Student completed hours
    private int regHour; // save Student registered hours
    private ArrayList<String>attemptedCourses; // save Student attempted Courses
    private  ArrayList<String> completedCourses; // save Student completed Courses
    public static ArrayList<Student> students = new ArrayList<>(); // save All student


    // create new student
    public Student(int studentId, String name, int age, String gender, String department, double GPA) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.GPA = GPA;
        totalHours= 0;
        regHour=0;
        attemptedCourses = new ArrayList<>();
        completedCourses = new ArrayList<>();
    }

    // copy Student data
    public Student(int studentId, String name, int age, String gender, String department, double GPA, int totalHours, int regHour, ArrayList<String> attemptedCourses, ArrayList<String> completedCourses) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.GPA = GPA;
        this.totalHours = totalHours;
        this.regHour = regHour;
        this.attemptedCourses = attemptedCourses;
        this.completedCourses = completedCourses;
    }

    // Setter and Getter
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public int getRegHour() {
        return regHour;
    }

    public void setRegHour(int regHour) {
        this.regHour = regHour;
    }

    public ArrayList<String> getAttemptedCourses() {
        return attemptedCourses;
    }

    public void setAttemptedCourses(ArrayList<String> attemptedCourses) {
        this.attemptedCourses = attemptedCourses;
    }

    public ArrayList<String> getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(ArrayList<String> completedCourses) {
        this.completedCourses = completedCourses;
    }

    // search for student by id and return it as object
    public static Box getStudentByID(int studentId){
        for (Student student : students) {
            if (student.getStudentId() == studentId)
                return new Box(student, true);
        }
        return new Box(null, false);
    }

    // to update student GPA when finish a course
    public void updateGPA(int score, int credit){
        double tempGPA = GPA * totalHours;
        if(score >= 95)
            GPA = (tempGPA + (5 * credit))/(totalHours+credit);
        else if(score >= 90)
            GPA = (tempGPA + (4.75 * credit))/(totalHours+credit);
        else if(score >= 85)
            GPA = (tempGPA + (4.5 * credit))/(totalHours+credit);
        else if(score >= 80)
            GPA = (tempGPA + (4 * credit))/(totalHours+credit);
        else if(score >= 75)
            GPA = (tempGPA + (3.5 * credit))/(totalHours+credit);
        else if(score >= 70)
            GPA = (tempGPA + (3 * credit))/(totalHours+credit);
        else if(score >= 65)
            GPA = (tempGPA + (2.5 * credit))/(totalHours+credit);
        else if(score >= 60)
            GPA = (tempGPA + (2 * credit))/(totalHours+credit);
        else
            GPA = (tempGPA +(credit))/(totalHours+credit);
    }

    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", GPA=" + GPA +
                ", totalHours=" + totalHours +
                ", regHour=" + regHour +
                ", attemptedCourses=" + attemptedCourses +
                ", completedCourses=" + completedCourses +
                '}';
    }
    // Box class is used when we want to return an object of type course and boolean
    static class Box{
        Student s;
        boolean found;

        public Box(Student s, boolean found) {
            this.s = s;
            this.found = found;
        }


    }
}
