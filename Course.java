import java.util.ArrayList;

public class Course {

    private String courseId; // save courseID
    private String name; // save course name
    private int credit; // save course credit hour
    private  String preReq1; // save first course prerequisite if exist
    private  String preReq2; // save second course prerequisite if exist
    public static ArrayList<Course> courses = new ArrayList<>(); // save all courses

    // copy Course data
    public Course(String courseId, String name, int credit, String preReq1, String preReq2) {
        this.courseId = courseId;
        this.name = name;
        this.credit = credit;
        this.preReq1 = preReq1;
        this.preReq2 = preReq2;
    }

    // Setter and Getter
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getPreReq1() {
        return preReq1;
    }

    public void setPreReq1(String preReq1) {
        this.preReq1 = preReq1;
    }

    public String getPreReq2() {
        return preReq2;
    }

    public void setPreReq2(String preReq2) {
        this.preReq2 = preReq2;
    }

    // search for course by id and return it as object
    public static Box getCourseByID(String courseID){
        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseID)) {
                return new Box(course, true);
            }
        }
        return new Box(null, false);
    }

    public String toString() {
        return courseId + "-" + name;
    }


    // Box class is used when we want to return an object of type course and boolean
    static class Box{
        Course c;
        boolean found;

        public Box(Course c, boolean found) {
            this.c = c;
            this.found = found;
        }


    }
}

