import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Row {
    static char[] based = new char[] { '*', ':', '+', '~', '(', ')', '-'};

    public static String solve(String line){
        byte correctLine = correctFormat(line);
        if (correctLine == 0){
            return "Ошибка. Деление на 0.";
        } else if (correctLine < 6) {
            return "Ошибка. Некорректное выражение";
        } else {
            String preparedLine = openBrackets(performLine(line));
            return executeOperations(preparedLine);
        }
//        return performLine(line).toString();
    }

    private static String executeOperations(String expression){
        StringBuilder result = new StringBuilder(expression);
        char[] allChars = expression.toCharArray();

        //для начала находим количество операторов
        int countOperations = 0;
        for (char i : allChars) {
            if ((i == based[0]) || (i == based[1]) || (i == based[2]) || (i == based[3])) {
                countOperations++;
            }
        }

        //выполним все операции
        while (countOperations > 0) {
            allChars = result.toString().toCharArray();

            //найдём преоритетную операцию
            byte priority = 0;
            char operator = 'o';
            int operationPosition = -5;
            int operatorNumber = 0;
            for (int j = 0; j < allChars.length; j++) {
                if ((allChars[j] == based[0]) || (allChars[j] == based[1])) {
                    priority = 2;
                    operationPosition = j;
                    operatorNumber++;
                    operator = allChars[j];
                    break;
                } else if ((allChars[j] == based[2]) || (allChars[j] == based[3])) {
                    operatorNumber++;
                    if (priority == 0) {
                        priority = 1;
                        operationPosition = j;
                        operator = allChars[j];
                    }
                }
            }

            if (priority != 2){ operatorNumber = 1; }

            //извлечём данные из строки
            String left = "";
            int leftBorder = -5;
            String right = "";
            int rightBorder = -5;

            if (countOperations == 1) {
                leftBorder = 0;
                rightBorder = allChars.length;
                left = new String(allChars, 0, operationPosition);
                right = new String(allChars, operationPosition + 1, allChars.length - operationPosition - 1);

            } else if (operatorNumber == 1) {
                leftBorder = 0;
                rightBorder = nextOperator(allChars, operationPosition);
                left = new String(allChars, 0, operationPosition);
                right = new String(allChars, operationPosition + 1, rightBorder - operationPosition - 1);

            } else if (operatorNumber == countOperations) {
                leftBorder = lastOperator(allChars, operationPosition);
                rightBorder = allChars.length;
                left = new String(allChars, leftBorder + 1, operationPosition - leftBorder - 1);
                right = new String(allChars, operationPosition + 1, allChars.length - operationPosition - 1);

            } else {
                leftBorder = lastOperator(allChars, operationPosition);
                rightBorder = nextOperator(allChars, operationPosition);
                left = new String(allChars, leftBorder + 1, operationPosition - leftBorder - 1);
                right = new String(allChars, operationPosition + 1, rightBorder - operationPosition - 1);
            }

            //выполним операцию
            switch (operator) {
                case '*':
                    result = result.replace(leftBorder,rightBorder,Fraction.multiplication(new Fraction(left), new Fraction(right)));
                    countOperations--;
                    break;
                case ':':
                    result = result.replace(leftBorder,rightBorder, Fraction.division(new Fraction(left), new Fraction(right)));
                    countOperations--;
                    break;
                case '~':
                    result = result.replace(leftBorder,rightBorder,Fraction.subtraction(new Fraction(left), new Fraction(right)));
                    countOperations--;
                    break;
                case '+':
                    result = result.replace(leftBorder,rightBorder, Fraction.summation(new Fraction(left), new Fraction(right)));
                    countOperations--;
                    break;
            }
        }
        return result.toString();
    }

    private static String openBrackets(StringBuilder changeLine) {
        while (changeLine.indexOf("(") != -1){
            //найдём самые глубоко вложенные скобки
            int[] bracketsData = {0, -5, 0, -5, -5}; // максимальное и текущее кол-во "(" и их положение; положение ближайшей ")"
            char[] bracketChars = changeLine.toString().toCharArray();
            for (int i = 0; i < bracketChars.length; i++) {
                if (bracketChars[i] == based[4]) {
                    bracketsData[2]++;
                    bracketsData[3] = i;
                } else if (bracketChars[i] == based[5]) {
                    if (bracketsData[2] == bracketsData[0]) {
                        bracketsData[4] = i;
                    }
                    bracketsData[2]--;
                }
                if (bracketsData[2] > bracketsData[0]) {
                    bracketsData[0] = bracketsData[2];
                    bracketsData[1] = bracketsData[3];
                }
            }

            //извлечём внутреннюю скобку и заменим её на результат её операций
            changeLine = changeLine.replace(bracketsData[1], bracketsData[4]+1, executeOperations(new String(bracketChars, bracketsData[1] + 1, bracketsData[4] - bracketsData[1]-1)));
        }
        return changeLine.toString();
    }

    private static byte correctFormat(String inputText){
        StringBuilder text = new StringBuilder(inputText);

        //деление на ноль
        if (text.indexOf("/0") != -1){ return 0; }

        //двойные операторы
        Pattern doubleOperators = Pattern.compile("[\\*\\-\\+\\:]{2}");
        Matcher firstMask = doubleOperators.matcher(text);
        if ((firstMask.find() )|| (text.indexOf("//") != -1)){ return 1; }

        //недопустимые символы
        Pattern validSymbol = Pattern.compile("[^\\d\\*\\:\\-\\+\\/\\ \\(\\)]");
        Matcher secondMask = validSymbol.matcher(text);
        if (secondMask.find()){ return 2; }

        char[] textChars = inputText.toCharArray();
        //начинается или оканчивается на оператор
        if ((textChars[0] == based[0]) || (textChars[0] == based[1]) || (textChars[0] == based[2])) {
            return 4;
        } else if ((textChars[textChars.length-1] == based[0]) || (textChars[textChars.length-1] == based[1]) || (textChars[textChars.length-1] == based[2]) || (textChars[textChars.length-1] == based[6])) {
            System.out.println("cvhgcmg");
            return 5;
        }
        return 6;
    }

    private static StringBuilder performLine(String correctLine){
        char[] performedChars = correctLine.toCharArray();
        StringBuilder doneLine = new StringBuilder(correctLine);
        for (int i = performedChars.length-1; i > -1; i--) {
            if (performedChars[i] == ' '){
                doneLine.delete(i,i+1);
            } else if ((performedChars[i] == '-')&&(i != 0)) {
                if ((performedChars[i-1] == '(') || (performedChars[i-1] == '/')){
                    continue;
                } else {
                    doneLine.replace(i, i+1, "~");
                }
            }
        }
        return doneLine;
    }

    private static int nextOperator(char[] arr, int position) { // найдём позицию следущего оператора
        for (int n = position+1; n < arr.length; n++) {
            if ((arr[n] == based[0]) || (arr[n] == based[1]) || (arr[n] == based[2]) || (arr[n] == based[3])) {
                return n;
            }
        }
        return -5; //невозможное условие
    }

    private static int lastOperator(char[] arr, int position) {// найдём позицию предыдущего оператора
        for (int n = position-1; n > 0; n--) {
            if ((arr[n] == based[0]) || (arr[n] == based[1]) || (arr[n] == based[2]) || (arr[n] == based[3])) {
                return n;
            }
        }
        return -5; //невозможное условие
    }
}
