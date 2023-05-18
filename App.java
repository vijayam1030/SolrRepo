public class MainClass {
    public static void main(String[] args) {
        // Check if there are any input arguments
        if (args.length > 0) {
            // Get the first input argument
            String input = args[0];

            // Trigger a method based on the input argument
            if (input.equals("method1")) {
                method1();
            } else if (input.equals("method2")) {
                method2();
            } else {
                System.out.println("Invalid input argument");
            }
        } else {
            System.out.println("No input arguments provided");
        }
    }

    public static void method1() {
        System.out.println("Method 1 triggered");
        // Add your code here
    }

    public static void method2() {
        System.out.println("Method 2 triggered");
        // Add your code here
    }
}



//// case
public class MainClass {
    public static void main(String[] args) {
        // Check if there are any input arguments
        if (args.length > 0) {
            // Get the first input argument
            String input = args[0];

            // Trigger a method based on the input argument
            switch (input) {
                case "method1":
                    method1();
                    break;
                case "method2":
                    method2();
                    break;
                default:
                    System.out.println("Invalid input argument");
                    break;
            }
        } else {
            System.out.println("No input arguments provided");
        }
    }
