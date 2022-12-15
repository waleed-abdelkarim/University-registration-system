import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class RegistrationServer {

    public static void main(String[] args) throws IOException {
        int studentId;
        String name;
        int age;
        String gender;
        String department;
        double GPA = 0;
        int totalHours;
        int regHour;
        String courseId;
        int credit;
        String preReq1;
        String preReq2;
        String found;
        Student s = null;

        // establish connection
        System.out.println("Server Running");
        ServerSocket Server = new ServerSocket(5000);
        Socket ClientSocket = Server.accept(); // well accept every client request
        System.out.println("Client Running");
        // create BufferedReader to read the data from clientSocket
        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        // create PrintWriter to send the data to clientSocket
        PrintWriter outToClient = new PrintWriter(ClientSocket.getOutputStream(), true);

        // read input from plan.csv file
        try {
            File PlanFile = new File("Plan.csv");
            Scanner inputStream1 = new Scanner(PlanFile);
            String x = inputStream1.next(); // to skip the header
            while (inputStream1.hasNext()) {
                try { // this try will make reading with single point of failure
                    String[] record = inputStream1.next().split(",");
                    courseId = record[0];
                    name = record[1].replace("-"," ");
                    credit = Integer.parseInt(record[2]);
                    preReq1 = record[3];
                    preReq2 = record[4];
                    Course c = new Course(courseId, name,credit, preReq1, preReq2);
                    Course.courses.add(c);
                } catch (Exception ignored) {}
            }
            inputStream1.close();
        } catch (Exception e) {
            System.out.println("File path not correct, please change it and re-run the program");
            outToClient.println("exit");
            Server.close();
            System.exit(0);
        }

        // read input from students.csv file
        try{
            File StudentFile = new File("students.csv");
            Scanner inputStream2 = new Scanner(StudentFile);
            String x = inputStream2.next(); // to skip the header
                while (inputStream2.hasNext()) {
                    try { // this try will make reading with single point of failure
                    String[] record = inputStream2.next().split(",");
                    studentId = Integer.parseInt(record[0]);
                    name = record[1].replace("-", " ");
                    age = Integer.parseInt(record[2]);
                    gender = record[3];
                    department = record[4];
                    GPA = Double.parseDouble(record[5]);
                    totalHours = Integer.parseInt(record[6]);
                    regHour = Integer.parseInt(record[7]);
                    String[] attemptedCoursesTemp = record[8].replace("[", "").replace("]", "").replace(".", ",").split(",");
                    String[] completedCoursesTemp = record[9].replace("[", "").replace("]", "").replace(".", ",").split(",");
                    ArrayList<String> attemptedCourses = new ArrayList<>();

                    // to transfer data from type (String[]) to type (ArrayList<String>)
                    for (String text : attemptedCoursesTemp) {
                        if (!text.equalsIgnoreCase(""))
                            attemptedCourses.add(text);
                    }
                    ArrayList<String> completedCourses = new ArrayList<>();
                    for (String text : completedCoursesTemp) {
                        if (!text.equalsIgnoreCase(""))
                            completedCourses.add(text);
                    }
                        s = new Student(studentId, name, age, gender, department, GPA, totalHours, regHour, attemptedCourses, completedCourses);
                        Student.students.add(s);
                    }catch (Exception ignored){}
                }
            inputStream2.close();
        } catch (Exception e) {
            System.out.println("File path not correct, please change it and re-run the program");
            outToClient.println("exit");
            Server.close();
            System.exit(0);
        }

        // start service
        outToClient.println("Enter your ID (9 digits): ");
        studentId = Integer.parseInt(inputFromClient.readLine());
        found = "null";
        name = null;

        // in this case will be Administrator
        if (studentId == 111111111){
            found = "Administrator";
            name = "Administrator";
        }

        else{ // in this case will be student
           s = Student.getStudentByID(studentId).s;
           if (Student.getStudentByID(studentId).found){
                found = "student";
                name = s.getName();
                GPA = s.getGPA();
           }
        }
        outToClient.println(found);
        if (!Student.getStudentByID(studentId).found) { // not found
            outToClient.println("You are not student, please contact university Administrator to add your information and try again later");
            Server.close();
            System.exit(0);
        }

        else if (found.equalsIgnoreCase("student")) { // student
            outToClient.println(name);
            outToClient.println(GPA);
            String response = inputFromClient.readLine();
            String choice;
            boolean first = true;
            do {
                if (!first)
                    choice = inputFromClient.readLine();
                else
                    choice = response;

                switch (choice) {
                    case "1":// Register Course
                        Registration r = new Registration();
                        outToClient.println(Registration.dispalyCanRegistreCourses(studentId));
                        outToClient.println("Enter Course ID (e.g. 370IS): ");
                        courseId = inputFromClient.readLine();
                        r.setStudentId(studentId);
                        r.setCourseId(courseId);
                        /* this method will register the course with courseId to student s
                        and well return a string represent the status of registration*/
                        String str = r.registerCourse(s, courseId);
                        if (str.equalsIgnoreCase("not found"))
                            outToClient.println("The courseId is wrong");
                        else if (str.equalsIgnoreCase("not completed"))
                            outToClient.println("You're not completed the prerequisites");
                        else if (str.equalsIgnoreCase("already registered"))
                            outToClient.println("You're already registered in this course");
                        else if (str.equalsIgnoreCase("completed before"))
                            outToClient.println("You're already passed this course");
                        else if (str.equalsIgnoreCase("completed"))
                            outToClient.println("Registration completed successfully");
                        first = false;
                        break;

                    case "2": // Unregister Course
                        outToClient.println("Enter Course ID (e.g. 370IS): ");
                        courseId = inputFromClient.readLine();
                        Course c = Course.getCourseByID(courseId).c;
                        boolean removed = false;
                        if (Course.getCourseByID(courseId).found) {
                            for (int i = 0; i < s.getAttemptedCourses().size(); i++) {
                                if (s.getAttemptedCourses().get(i).equalsIgnoreCase(courseId)) {
                                    s.getAttemptedCourses().remove(i);
                                    s.setRegHour(s.getRegHour() - c.getCredit());
                                    outToClient.println("Course deleted successfully");
                                    removed = true;
                                }
                            }
                            if (!removed)
                                outToClient.println("The student doesn't register this course");
                        } else
                            outToClient.println("The course not found");
                        first = false;
                        break;

                    case "3": // Display Your Courses
                    {
                        StringBuilder CompletedCourses = new StringBuilder("[");
                        for (int j = 0; j < s.getCompletedCourses().size(); j++) {
                            CompletedCourses.append(s.getCompletedCourses().get(j).toUpperCase(Locale.ROOT));
                            if (!(j == s.getCompletedCourses().size() - 1)) {
                                CompletedCourses.append(", ");
                            }
                        }
                        CompletedCourses.append("]");
                        outToClient.println(CompletedCourses);

                        StringBuilder AttemptedCourses = new StringBuilder("[");
                        for (int j = 0; j < s.getAttemptedCourses().size(); j++) {
                            AttemptedCourses.append(s.getAttemptedCourses().get(j).toUpperCase(Locale.ROOT));
                            if (!(j == s.getAttemptedCourses().size() - 1)) {
                                AttemptedCourses.append(", ");
                            }
                        }
                        AttemptedCourses.append("]");
                        outToClient.println(AttemptedCourses);
                    }
                    first = false;
                    break;
                    case "4": // Display Plan
                        StringBuilder plan = new StringBuilder();
                        for (int i = 0; i < Course.courses.size(); i++) {
                            plan.append(Course.courses.get(i).toString()).append("\t");
                        }
                        outToClient.println(plan);
                        first = false;
                        break;
                    case "5": // Exit
                        outToClient.println("Goodbye");
                        try { // write student data to students.csv file
                            PrintWriter StudentToCsv = new PrintWriter("students.csv");
                            StudentToCsv.println("studentId,name,age,gender,department,GPA,totalHours,regHour,attemptedCourses[],completedCourses[]");
                            for (int i = 0; i < Student.students.size(); i++) {
                                try { // ensure single point of failure
                                    StringBuilder AttemptedCourses = new StringBuilder("[");
                                    for (int j = 0; j < Student.students.get(i).getAttemptedCourses().size(); j++) {
                                        AttemptedCourses.append(Student.students.get(i).getAttemptedCourses().get(j).toUpperCase(Locale.ROOT));
                                        if (!(j == Student.students.get(i).getAttemptedCourses().size() - 1))
                                            AttemptedCourses.append(".");
                                    }
                                    AttemptedCourses.append("]");
                                    StringBuilder CompletedCourses = new StringBuilder("[");
                                    for (int j = 0; j < Student.students.get(i).getCompletedCourses().size(); j++) {
                                        CompletedCourses.append(Student.students.get(i).getCompletedCourses().get(j).toUpperCase(Locale.ROOT));
                                        if (!(j == Student.students.get(i).getCompletedCourses().size() - 1))
                                            CompletedCourses.append(".");
                                    }
                                    CompletedCourses.append("]");
                                    StudentToCsv.printf("%d,%s,%d,%s,%s,%f,%d,%d,%s,%s\n",
                                            Student.students.get(i).getStudentId(),
                                            Student.students.get(i).getName().replace(" ", "-"),
                                            Student.students.get(i).getAge(),
                                            Student.students.get(i).getGender(),
                                            Student.students.get(i).getDepartment(),
                                            Student.students.get(i).getGPA(),
                                            Student.students.get(i).getTotalHours(),
                                            Student.students.get(i).getRegHour(),
                                            AttemptedCourses,
                                            CompletedCourses);
                                } catch (Exception e) {
                                    System.out.println("File path not correct, please change it and re-run the program");
                                    Server.close();
                                    System.exit(0);
                                }
                            }
                            StudentToCsv.close();
                        } catch (Exception ignored) {
                        }
                        Server.close();
                        System.exit(0);
                        break;
                }
            } while (true);
        }

        // if Administrator
        else {
            outToClient.println(name);

            String choice = inputFromClient.readLine();
            boolean first = true;
            do {
                if (!first)
                    choice = inputFromClient.readLine();

                switch (choice) {
                    case "1": // Add Student
                        outToClient.println("Enter Student ID (e.g. 4411*****): ");
                        studentId = Integer.parseInt(inputFromClient.readLine());
                        if (!Student.getStudentByID(studentId).found) {
                            outToClient.println("Enter Student Name: ");
                            name = inputFromClient.readLine();
                            outToClient.println("Enter Student age in years: ");
                            age = Integer.parseInt(inputFromClient.readLine());
                            outToClient.println("Enter Student gender (M|F): ");
                            gender = inputFromClient.readLine();
                            department = "IS";
                            GPA = 5;
                            Student newStudent = new Student(studentId, name, age, gender, department, GPA);
                            Student.students.add(newStudent);
                            outToClient.println("Student was added successfully");
                        } else {
                            outToClient.println("The student with this ID already exist");
                        }
                        first = false;
                        break;
                    case "2": // Approve that student complete a course
                        outToClient.println("Enter Student ID (e.g. 4411*****): ");
                        studentId = Integer.parseInt(inputFromClient.readLine());
                        s = Student.getStudentByID(studentId).s;
                        if (!Student.getStudentByID(studentId).found) {
                            outToClient.println("We couldn't find the student");
                        } else {
                            outToClient.println("Enter Course ID (e.g. 370IS): ");
                            courseId = inputFromClient.readLine();
                            outToClient.println("Enter student score (0 to 100): ");
                            int score = Integer.parseInt(inputFromClient.readLine());
                            boolean done = false;
                            for (int i = 0; i < s.getAttemptedCourses().size(); i++) {
                                if (s.getAttemptedCourses().get(i).equalsIgnoreCase(courseId)) {
                                    Course c = Course.getCourseByID(courseId).c;
                                    if (score >= 60)
                                        s.getCompletedCourses().add(courseId);
                                    s.getAttemptedCourses().remove(i);
                                    s.updateGPA(score, c.getCredit());
                                    s.setRegHour(s.getRegHour() - c.getCredit());
                                    s.setTotalHours(s.getTotalHours() + c.getCredit());
                                    outToClient.println("Done");
                                    done = true;
                                    break;
                                }
                            }
                            if (!done) {
                                outToClient.println("Not done");
                            }
                        }
                        first = false;
                        break;
                    case "3": // Exit
                        outToClient.println("Goodbye");
                        try { // write student data to students.csv file
                            PrintWriter StudentToCsv = new PrintWriter("students.csv");
                            StudentToCsv.println("studentId,name,age,gender,department,GPA,totalHours,regHour,attemptedCourses[],completedCourses[]");

                            for (int i = 0; i < Student.students.size(); i++) {
                                try { // ensure single point of failure
                                    StringBuilder AttemptedCourses = new StringBuilder("[");
                                    for (int j = 0; j < Student.students.get(i).getAttemptedCourses().size(); j++) {
                                        AttemptedCourses.append(Student.students.get(i).getAttemptedCourses().get(j).toUpperCase(Locale.ROOT));
                                        if (!(j == Student.students.get(i).getAttemptedCourses().size() - 1))
                                            AttemptedCourses.append(".");
                                    }
                                    AttemptedCourses.append("]");
                                    StringBuilder CompletedCourses = new StringBuilder("[");
                                    for (int j = 0; j < Student.students.get(i).getCompletedCourses().size(); j++) {
                                        CompletedCourses.append(Student.students.get(i).getCompletedCourses().get(j).toUpperCase(Locale.ROOT));
                                        if (!(j == Student.students.get(i).getCompletedCourses().size() - 1))
                                            CompletedCourses.append(".");
                                    }
                                    CompletedCourses.append("]");
                                    StudentToCsv.printf("%d,%s,%d,%s,%s,%f,%d,%d,%s,%s\n",
                                            Student.students.get(i).getStudentId(),
                                            Student.students.get(i).getName().replace(" ", "-"),
                                            Student.students.get(i).getAge(),
                                            Student.students.get(i).getGender(),
                                            Student.students.get(i).getDepartment(),
                                            Student.students.get(i).getGPA(),
                                            Student.students.get(i).getTotalHours(),
                                            Student.students.get(i).getRegHour(),
                                            AttemptedCourses,
                                            CompletedCourses);
                                } catch (Exception e) {
                                    System.out.println("File path not correct, plese change it and re-run the program");
                                    Server.close();
                                    System.exit(0);
                                }
                            }
                            StudentToCsv.close();
                        } catch (Exception ignored) {
                        }
                        Server.close();
                        System.exit(0);
                        break;
                }
            } while (true);
        }

        }
    }
