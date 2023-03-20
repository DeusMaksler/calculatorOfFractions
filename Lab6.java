import java.util.Scanner;

public class Lab6 {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите математическое выражение: ");
        String input_string = scan.next();
        while (!input_string.equals("exit")){
            System.out.printf("Ответ: %s\n",Row.solve(input_string));
            System.out.print("Введите новое выражение или exit: ");
            input_string = scan.next();
        }
        scan.close();
    }
}
