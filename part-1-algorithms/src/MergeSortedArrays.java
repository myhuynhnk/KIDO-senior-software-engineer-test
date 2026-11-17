import java.util.Arrays;

public class MergeSortedArrays {
    
    // Basic: Merge into a new array
    public static int[] merge(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                result[k] = a[i];
                i++;
            } else {
                result[k] = b[j];
                j++;
            }
            k++;
        }

        while (i < a.length) {
            result[k] = a[i];
            i++;
            k++;

        }

        while (j < b.length) {
            result[k] = b[j];
            j++;
            k++;
        }

        return result;
    }

    // Advanced: Merge directly into array A
    public static void mergeInPlace(int[] a, int m, int[] b, int n) {
        int i = m - 1;
        int j = n - 1;
        int k = m + n - 1;

        while (j >= 0) {
            if (i >= 0 && a[i] > b[j]) {
                a[k] = a[i];
                i--;
            } else {
                a[k] = b[j];
                j--;
            }
            k--;
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 5};
        int[] b = {2, 4, 6};

        // basic
        int[] result = merge(a, b);
        System.out.println("Basic: " + Arrays.toString(result));

        //advanced
        int[] advancedA = {1, 3, 5, 0, 0, 0};
        int[] advancedB = {2, 4, 6};
        mergeInPlace(advancedA, 3, advancedB, 3);
        System.out.println("Advanced: " + Arrays.toString(advancedA));
    }

}
