import java.util.*;

public class Main {

    private String input;
    private Queue<String> queue = new LinkedList<>();
    private Stack<String> stack = new Stack<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.StringScanner();
        main.ShuntingYardAlgorithm(main.input);
        
        System.out.println("Postfix Notation: " + main.queue);
        
        int result = evaluatePostfix(main.queue);
        System.out.println("Ergebnis: " + result);
    }

    public void StringScanner() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Rechnung eingeben: ");
        input = sc.nextLine();
        sc.close();
    }

    public void ShuntingYardAlgorithm(String input) {
        String[] tokens = input.split("(?<=[-+*/^()])|(?=[-+*/^()])");
        System.out.println("Tokens: " + Arrays.toString(tokens));

        for (String token : tokens) {
            token = token.trim();

            if (token.matches("-?\\d+(\\.\\d+)?")) {  
                queue.add(token); 
            } 
            else if (token.equals("(")) {
                stack.push(token);
            } 
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    queue.add(stack.pop()); 
                }
                stack.pop(); // Entferne die öffnende Klammer
            } 
            else if (token.matches("[+\\-*/^]")) {  
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                       (   (token.equals("+") || token.equals("-")) && !stack.peek().equals("^")  
                        || (token.equals("*") || token.equals("/")) && (stack.peek().equals("*") || stack.peek().equals("/") || stack.peek().equals("^")) 
                        || (token.equals("^") && stack.peek().equals("^")))) {  
                    queue.add(stack.pop());  
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {  
            queue.add(stack.pop()); 
        }
    }

    static int evaluatePostfix(Queue<String> queue) {
        Stack<Integer> stack = new Stack<>();

        for (String token : queue) {
            if (token.matches("-?\\d+")) {  
                stack.push(Integer.parseInt(token));
            } 
            else {
                int val1 = stack.pop();
                int val2 = stack.pop();

                switch (token) {
                    case "+":
                        stack.push(val2 + val1);
                        break;
                    case "-":
                        stack.push(val2 - val1);
                        break;
                    case "*":
                        stack.push(val2 * val1);
                        break;
                    case "/":
                        if (val1 == 0) {
                            throw new ArithmeticException("Division durch 0 ist nicht erlaubt!");
                        }
                        stack.push(val2 / val1);
                        break;
                    case "^":
                        stack.push((int) Math.pow(val2, val1));
                        break;
                }
            }
        }
        return stack.pop();
    }
}
