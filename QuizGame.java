import java.io.*;
import java.util.*;

class Question {
    String question, optionA, optionB, optionC, correctAnswer;

    public Question(String q, String a, String b, String c, String correct) {
        question = q;
        optionA = a;
        optionB = b;
        optionC = c;
        correctAnswer = correct; // expected: "a" or "b" or "c"
    }
}

public class QuizGame {
    static List<Question> questionList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadQuestions();
        playQuiz();
    }

    // Load questions from text file
    static void loadQuestions() {
        try (BufferedReader br = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;
                // Format: Question,OptionA,OptionB,OptionC,correctOptionLetter
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    questionList.add(new Question(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim().toLowerCase()));
                } else {
                    System.out.println("Warning: ignored malformed line in questions.txt -> " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("questions.txt not found. Please create the file in the same folder as QuizGame.java");
        } catch (IOException e) {
            System.out.println("Error reading questions file!");
        }
    }

    static void playQuiz() {
        if (questionList.isEmpty()) {
            System.out.println("No questions available! Add lines to questions.txt and run again.");
            return;
        }

        int score = 0;
        Collections.shuffle(questionList); // randomize the order

        for (Question q : questionList) {
            System.out.println("\n" + q.question);
            System.out.println("a) " + q.optionA);
            System.out.println("b) " + q.optionB);
            System.out.println("c) " + q.optionC);
            System.out.print("Enter your answer (a/b/c): ");
            String ans = sc.nextLine().trim().toLowerCase();

            // basic input validation: accept only a/b/c
            while (!(ans.equals("a") || ans.equals("b") || ans.equals("c"))) {
                System.out.print("Please enter a, b, or c: ");
                ans = sc.nextLine().trim().toLowerCase();
            }

            if (ans.equals(q.correctAnswer)) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong! Correct answer: " + q.correctAnswer);
            }
        }

        System.out.println("\nFinal Score: " + score + " / " + questionList.size());
        saveHighScore(score);
    }

    static void saveHighScore(int score) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt", true))) {
            bw.write("Score: " + score + " at " + new Date());
            bw.newLine();
            System.out.println("High score saved to highscore.txt");
        } catch (IOException e) {
            System.out.println("Error saving score!");
        }
    }
}
