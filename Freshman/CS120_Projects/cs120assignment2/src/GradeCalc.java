/* 
 * This program will compute the final percentage grade for a college
 * course. The college course has four components to the final grade:
 * Assignments...... 45%
 * Quizzes.......... 15%
 * Midterm Final.... 20%
 * Final Exam....... 20%
 * 
 * Date Last Modified <September 18, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */
import java.util.Scanner;
import java.text.DecimalFormat;

public class GradeCalc {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double quiz1, quiz2, quiz3, quiz4, quiz5, assign1, assign2, assign3, assign4, assign5,
				midtermExam, finalExam, quizWeight, assignWeight, examWeight, finalGrade;
		DecimalFormat df;
		df = new DecimalFormat( "00.00");
		
		// Enter the grades for the 5 quizzes (out of 20)
		System.out.println("Plese enter the number of points scored on each of the following items:");
		System.out.print("Quiz 1" + '\t' + '\t' + "(out of 20)  : ");
		quiz1 = input.nextDouble();
		System.out.print("Quiz 2" + '\t' + '\t' + "(out of 20)  : ");
		quiz2 = input.nextDouble();
		System.out.print("Quiz 3" + '\t' + '\t' + "(out of 20)  : ");
		quiz3 = input.nextDouble();
		System.out.print("Quiz 4" + '\t' + '\t' + "(out of 20)  : ");
		quiz4 = input.nextDouble();
		System.out.print("Quiz 5" + '\t' + '\t' + "(out of 20)  : ");
		quiz5 = input.nextDouble();
		
		// Enter the grades for the 5 Assignments (out of 100)
		System.out.print("Assignment 1" + '\t' + "(out of 100) : ");
		assign1 = input.nextDouble();
		System.out.print("Assignment 2" + '\t' + "(out of 100) : ");
		assign2 = input.nextDouble();
		System.out.print("Assignment 3" + '\t' + "(out of 100) : ");
		assign3 = input.nextDouble();
		System.out.print("Assignment 4" + '\t' + "(out of 100) : ");
		assign4 = input.nextDouble();
		System.out.print("Assignment 5" + '\t' + "(out of 100) : ");
		assign5 = input.nextDouble();
		
		// Enter the grades for the Midterm Exam/Final Exam (out of 100)
		System.out.print("Midterm Exam" + '\t' + "(out of 100) : ");
		midtermExam = input.nextDouble();
		System.out.print("Final Exam" + '\t' + "(out of 100) : ");
		finalExam  = input.nextDouble();
		
		// Compute the individual percent for the each quiz/assignment/exam
		quizWeight = .15 * ((quiz1 + quiz2 + quiz3 + quiz4 + quiz5)/100);
		//System.out.println("DEBUGG:" + (quizWeight));
		assignWeight = .45 * ((assign1 + assign2 + assign3 + assign4 + assign5)/500);
		//System.out.println("DEBUGG:" + (assignWeight));
		examWeight = .40 * ((midtermExam + finalExam)/200);
		//System.out.println("DEBUGG:" + (examWeight));
		
		// Find the fianl percentage grade 
		finalGrade = (examWeight + assignWeight + quizWeight);
		//System.out.print("DEBUGG:" + (finalGrade));
		//System.out.println("DEBUGG:" + df.format(finalGrade*100));
		System.out.println("Final Percentage Grade: " + df.format(finalGrade * 100) + "%");
		
		
	}

}
