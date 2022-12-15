import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

public class RegistrationClient {
	
	// methods to handle errors
	static boolean checkStudentId(String studentId) {
		Pattern pattern = Pattern.compile("^[0-9]{9}$");
		Matcher matcher = pattern.matcher(studentId);
		boolean matchFound = matcher.find();
		return !matchFound;
	}

	static boolean checkName(String name) {
		Pattern pattern = Pattern.compile("^[a-z|A-Z\\s]+$");
		Matcher matcher = pattern.matcher(name);
		boolean matchFound = matcher.find();
		return !matchFound;
	}

	static boolean checkAge(String age) {
		try {
			int ageTemp = Integer.parseInt(age);
			return ageTemp < 17 || ageTemp > 30;
		}catch (Exception ignored){}
		return true;
	}

	static boolean checkScore(String score) {
		try{
			int scoreTemp = Integer.parseInt(score);
			return scoreTemp < 0 || scoreTemp > 100;
		} catch (Exception ignored){}

		return true;
	}

	static boolean checkGender(String gender) {
		return !gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F");
	}

	static boolean checkCourseId(String courseId) {
		Pattern pattern = Pattern.compile("^[0-9]{3}[a-z|A-Z]");
		Matcher matcher = pattern.matcher(courseId);
		boolean matchFound = matcher.find();
		return !matchFound;
	}

	public static void main(String[] args) throws IOException {

		String studentId;
		String name;
		String age;
		String gender;
		String courseId;
		String found;
		String temp;
		String score;
		
		// establish connection
		Socket Client = new Socket("localhost", 5000);
		System.out.println("Client Running");
		// create PrintWriter to send the data to server
		PrintWriter outToServer = new PrintWriter(Client.getOutputStream(), true);
		// create BufferedReader to read the data from server
		BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(Client.getInputStream()));


		Scanner scan = new Scanner(System.in);
		
		// start service
		
		temp = inputFromServer.readLine();
		if (temp.equalsIgnoreCase("exit")) {
			Client.close();
			System.exit(0);
		}

		do {
			System.out.print(temp);
			studentId = scan.next();
			if (checkStudentId(studentId)) {
				System.err.println("Your ID is wrong, try again");
				try { // this sleep because err take time to print
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException ignored){
				}
			}
		} while (checkStudentId(studentId));

		outToServer.println(studentId);
		found = inputFromServer.readLine();

		// there's no student with this id
		if (found.equalsIgnoreCase("null")){
            System.out.println(inputFromServer.readLine());
            Client.close();
            System.exit(0);
		}

		// found and it's student
		else if (found.equalsIgnoreCase("student")){
            System.out.print("Welcome ");
            System.out.println(inputFromServer.readLine());
			System.out.print("Your GPA is: ");
			System.out.println(inputFromServer.readLine());

            do {
				System.out.println("----------------------------");
				System.out.println("----------------------------");

                System.out.println("What do you want to do?\n1- Register Course.\n2- Unregister Course.\n"+
						"3- Display Your Courses.\n4- Display Plan.\n5- Exit.");

				System.out.print("Enter a number from list: ");
                String choice = scan.next();
                if (!(choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2") || choice.equals("3")
                        || choice.equalsIgnoreCase("4") || choice.equalsIgnoreCase("5"))) {
					System.err.println("Error, enter number from 1 to 5");
					try { // this sleep because err take time to print
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException ignored){
					}
				}

                else {

					switch (choice) {
						case "1":  // Register Course
							outToServer.println("1");
							System.out.print(inputFromServer.readLine().replace("\t", "\n"));
							temp = inputFromServer.readLine();
							do {
								System.out.print(temp);
								courseId = scan.next();
								if (checkCourseId(courseId)) {
									System.err.println("Error, Try again");
									try { // this sleep because err take time to print
										TimeUnit.MILLISECONDS.sleep(500);
									} catch (InterruptedException ignored) {
									}
								}
							} while (checkCourseId(courseId));
							outToServer.println(courseId);
							System.out.println(inputFromServer.readLine());
							break;

						case "2": // Unregister Course
							outToServer.println("2");
							temp = inputFromServer.readLine();
							do {
								System.out.print(temp);
								courseId = scan.next();
								if (checkCourseId(courseId)) {
									System.err.println("Error, Try again");
									try { // this sleep because err take time to print
										TimeUnit.MILLISECONDS.sleep(500);
									} catch (InterruptedException ignored) {
									}
								}
							} while (checkCourseId(courseId));
							outToServer.println(courseId);
							System.out.println(inputFromServer.readLine());
							break;

						case "3": // Display Your Courses
							outToServer.println("3");
							System.out.print("Completed Courses: ");
							System.out.println(inputFromServer.readLine());
							System.out.print("Attempted Courses: ");
							System.out.println(inputFromServer.readLine());
							break;
						case "4": // Display Plan
							outToServer.println("4");
							System.out.print(inputFromServer.readLine().replace("\t", "\n"));
							break;
						case "5": // Exit
							outToServer.println("5");
							System.out.print(inputFromServer.readLine());
							Client.close();
							System.exit(0);
							break;
					}
                }
            } while (true);

	    }

		// found and it's Administrator
        else if (found.equalsIgnoreCase("Administrator")){
			System.out.print("Welcome ");
			System.out.println(inputFromServer.readLine());
			System.out.println("----------------------------");
			System.out.println("----------------------------");
			do {
				System.out.println("What do you want to do?\n1- Add Student.\n2- Approve that student complete a course.\n3- Exit.");
				System.out.print("Enter a number from list: ");
				String choice = scan.next();
				if (!(choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2") || choice.equals("3"))){
					System.err.println("Error, enter number from 1 to 3");
					try { // this sleep because err take time to print
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException ignored) {}
				}
				else {

					switch (choice) {
						case "1": // Add Course
							outToServer.println("1");
							temp = inputFromServer.readLine();
							do {
								System.out.print(temp);
								studentId = scan.next();
								if (checkStudentId(studentId)) {
									System.err.println("Error, Try again");
									try { // this sleep because err take time to print
										TimeUnit.MILLISECONDS.sleep(500);
									} catch (InterruptedException ignored) {
									}
								}
							} while (checkStudentId(studentId));
							outToServer.println(studentId);
							temp = inputFromServer.readLine();
							if (temp.equalsIgnoreCase("Enter Student Name: ")) {
								scan.nextLine();
								do {
									System.out.print(temp);
									name = scan.nextLine();
									if (checkName(name)) {
										System.err.println("Error, Try again");
										try { // this sleep because err take time to print
											TimeUnit.MILLISECONDS.sleep(500);
										} catch (InterruptedException ignored) {
										}
									}
								} while (checkName(name));
								outToServer.println(name.replace(" ", "-"));
								temp = inputFromServer.readLine();
								do {
									System.out.print(temp);
									age = scan.next();
									if (checkAge(age)) {
										System.err.println("Error, Try again");
										try { // this sleep because err take time to print
											TimeUnit.MILLISECONDS.sleep(500);
										} catch (InterruptedException ignored) {
										}
									}
								} while (checkAge(age));
								outToServer.println(age);

								temp = inputFromServer.readLine();
								do {
									System.out.print(temp);
									gender = scan.next();
									if (checkGender(gender)) {
										System.err.println("Error, Try again");
										try { // this sleep because err take time to print
											TimeUnit.MILLISECONDS.sleep(500);
										} catch (InterruptedException ignored) {
										}
									}
								} while (checkGender(gender));
								outToServer.println(gender);
								System.out.println(inputFromServer.readLine());
							} else {
								System.out.println(temp);
							}
							break;
						case "2":
							outToServer.println("2");
							temp = inputFromServer.readLine();
							do {
								System.out.print(temp);
								studentId = scan.next();
								if (checkStudentId(studentId)) {
									System.err.println("Error, Try again");
									try { // this sleep because err take time to print
										TimeUnit.MILLISECONDS.sleep(500);
									} catch (InterruptedException ignored) {
									}
								}
							} while (checkStudentId(studentId));
							outToServer.println(studentId);
							temp = inputFromServer.readLine();
							if (temp.equalsIgnoreCase("We couldn't find the student"))
								System.out.println("We couldn't find the student");
							else {
								do {
									System.out.print(temp);
									courseId = scan.next();
									if (checkCourseId(courseId)) {
										System.err.println("Error, Try again");
										try { // this sleep because err take time to print
											TimeUnit.MILLISECONDS.sleep(500);
										} catch (InterruptedException ignored) {
										}
									}
								} while (checkCourseId(courseId));
								outToServer.println(courseId);
								temp = inputFromServer.readLine();
								do {
									System.out.print(temp);
									score = scan.next();
									if (checkScore(score)) {
										System.err.println("Error, Try again");
										try { // this sleep because err take time to print
											TimeUnit.MILLISECONDS.sleep(500);
										} catch (InterruptedException ignored) {
										}
									}
								} while (checkScore(score));
								outToServer.println(score);
								temp = inputFromServer.readLine();
								if (temp.equalsIgnoreCase("Done"))
									System.out.println("Done");
								else
									System.out.println("We couldn't find the course in student attempted courses");
							}
							break;
						case "3": // Exit
							outToServer.println("3");
							System.out.println(inputFromServer.readLine());
							Client.close();
							System.exit(0);
							break;
						}
				}
			} while (true);
        }

    }

}
