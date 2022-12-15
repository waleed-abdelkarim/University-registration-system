public class Registration {
	private int StudentId; // save StudentID
	private String CourseId;  // save courseID

	// default constructor
	public Registration(){
	}


    // register course
	public String registerCourse(Student s, String courseId){
        String prereq1;
        String prereq2;

		// course not found
		if (!Course.getCourseByID(courseId).found)
			return "not found";

		// course found
		Course c = Course.getCourseByID(courseId).c;
		prereq1 = c.getPreReq1();
		prereq2 = c.getPreReq2();

		for (int i=0; i < s.getAttemptedCourses().size();i++){ // course is already registered
			if (s.getAttemptedCourses().get(i).equalsIgnoreCase(courseId))
				return "already registered";
		}
		for (int i=0; i < s.getCompletedCourses().size();i++){ // course is complated before
			if (s.getCompletedCourses().get(i).equalsIgnoreCase(courseId))
				return "completed before";
		}

		// ensure that student complete the prerequisites

		boolean completedPre1 = false;
		boolean completedPre2 = false;

		if (prereq1.equalsIgnoreCase("null") && prereq2.equalsIgnoreCase("null")) {
			s.setRegHour(s.getRegHour()+c.getCredit());
			s.getAttemptedCourses().add(courseId);
			return "completed";
		}
		for (int i=0; i < s.getCompletedCourses().size();i++){
			if (prereq1.equalsIgnoreCase(s.getCompletedCourses().get(i)) || prereq1.equalsIgnoreCase("null"))
				completedPre1 = true;
			if (prereq2.equalsIgnoreCase(s.getCompletedCourses().get(i)) || prereq2.equalsIgnoreCase("null"))
				completedPre2 = true;
			if (completedPre1 && completedPre2){
				s.setRegHour(s.getRegHour()+c.getCredit());
				s.getAttemptedCourses().add(courseId);
				return "completed";
			}
            }
			// if student not complete the prerequisites
            return "not completed";
		}

	// search and display the courses that student can register them
	public static String dispalyCanRegistreCourses(int StudentId) {
		StringBuilder notCompletedCourses = new StringBuilder();
		Student s = Student.getStudentByID(StudentId).s;
		String prereq1;
		String prereq2;
		for (int i = 0; i < Course.courses.size(); i++) { // to go through full plan
			Course c = Course.courses.get(i);
			prereq1 = c.getPreReq1();
			prereq2 = c.getPreReq2();
			boolean notComplated = true;
			boolean notAttempted = true;
			boolean completedPre1 = false;
			boolean completedPre2 = false;

			if (prereq1.equalsIgnoreCase("null") && prereq2.equalsIgnoreCase("null")) { // there is no prerequisites
				completedPre1 = true;
				completedPre2 = true;
			}
			// ensure that the student doesn't take the course before, or he completed the prerequisites
			for (int j=0; j< s.getCompletedCourses().size();j++) {
				if (c.getCourseId().equalsIgnoreCase(s.getCompletedCourses().get(j)))
					notComplated = false;
				if (prereq1.equalsIgnoreCase(s.getCompletedCourses().get(j)) || prereq1.equalsIgnoreCase("null"))
					completedPre1 = true;
				if (prereq2.equalsIgnoreCase(s.getCompletedCourses().get(j)) || prereq2.equalsIgnoreCase("null"))
					completedPre2 = true;
			}
			// ensure that the student don't take the course now
			for (int j=0; j< s.getAttemptedCourses().size();j++) {
				if (c.getCourseId().equalsIgnoreCase(s.getAttemptedCourses().get(j))) {
					notAttempted = false;
					break;
				}
			}
			// if student can register the course
			if (notComplated && notAttempted && completedPre1 && completedPre2)
				notCompletedCourses.append(Course.courses.get(i).toString()).append("\t");
		}
		if (notCompletedCourses.toString().equalsIgnoreCase(""))
			return "";
		else
			return notCompletedCourses.toString();
	}

	// Setter and Getter
	public int getStudentId() {
		return StudentId;
	}

	public void setStudentId(int studentId) {
		StudentId = studentId;
	}

	public String getCourseId() {
		return CourseId;
	}

	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
}