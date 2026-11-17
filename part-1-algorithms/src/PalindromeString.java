public class PalindromeString {
    // Basic: Check if a string is a palindrome
    public static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }
        return true;
    }

    // Advanced: Count palindromic substrings
    public static int countPalindromes(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += expandAroundCenter(s, i, i);
            count += expandAroundCenter(s, i, i + 1);
        }
        return count;
    }

    // Expand from the center to count palindromes
    private static int expandAroundCenter(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() 
            && s.charAt(left) == s.charAt(right)) {
                
                count++;
                left--;
                right++;
        }
        return count;
    }


    public static void main(String[] args) {
        System.out.println("Basic radar: " + isPalindrome("radar"));
        System.out.println("Basic hello: " + isPalindrome("hello"));

        System.out.println("Advanced aaa: " + countPalindromes("aaa"));
        System.out.println("Advanced aba: " + countPalindromes("aba"));
        System.out.println("Advanced radar: " + countPalindromes("radar"));
    }
}
