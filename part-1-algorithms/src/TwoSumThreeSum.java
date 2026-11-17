import java.util.Arrays;

public class TwoSumThreeSum {
    //basic
    public static int[] twoSum(int[] numbers, int k) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == k) {
                return new int[]{numbers[left], numbers[right]};
            }

            if (sum < k) {
                left++;
            } else {
                right--;
            }
        }
        return new int[0];
    }


    //advanced
    public static int[] threeSum(int[] numbers, int k) {
        for (int i = 0; i < numbers.length - 2; i++) { 
            int left = i + 1;
            int right = numbers.length - 1;

            while (left < right) {
                int sum = numbers[i] + numbers[left] + numbers[right];

                if (sum == k) {
                    return new int[]{numbers[i], numbers[left], numbers[right]};
    
                }

                if (sum < k) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 6};
        int k = 6;
        System.out.println("Basic: " + Arrays.toString(twoSum(numbers, k)));
        System.out.println("Advanced: " + Arrays.toString(threeSum(numbers, k)));
    }
}
