public class Solutions {

    public static void solutionOne(int[] arr, int input) {
        int countAbove = 0, countBelow = 0;

        // doesn't look at edge case of equality (i.e. when num == input)
        for (int num : arr) {
            if (num > input) {
                countAbove++;
            } else if (num < input) {
                countBelow++;
            }
        }

        System.out.printf("above: %d, below: %d\n", countAbove, countBelow);
    }

    public static String solutionTwo(String str, int index) {
        int rotate = str.length() - index;

        // edge case of index being out of bounds
        if (index > str.length()) {
            return null;
        }

        return str.substring(rotate) + str.substring(0, rotate);
    }

    public static void solutionThree() {
        /*
         * Java is definitely my favorite language by far, just simply because
         * of how much it adheres to the principle of object-oriented design.
         * However, there's honestly nothing I would change about it. As it
         * was my first language, I guess it has become the language I compare
         * all other languages to. And the one thing almost every other
         * language is missing is the extensive API. Python is a great and easy
         * language to use, especially for beginners, but a nice, organized API like Java
         * has would greatly improve the language overall.
         */
    }

    public static void main(String[] args) {
        solutionOne(new int[] {1,5,2,1,10}, 10);

        System.out.println(solutionTwo("MyString", 2));
    }
}
