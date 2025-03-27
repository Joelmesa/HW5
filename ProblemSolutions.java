/******************************************************************
 *
 *   Joel Mesa / Comp 271
 *
 *   This java file contains the problem solutions of isSubSet, findKthLargest,
 *   and sort2Arrays methods. You should utilize the Java Collection Framework for
 *   these methods.
 *
 ********************************************************************/

import java.util.*;

class ProblemSolutions {

    // isSubset: Use HashSet to check if every element in list2 exists in list1
    public boolean isSubset(int[] list1, int[] list2) {
        Set<Integer> set = new HashSet<>();
        for (int num : list1) {
            set.add(num);
        }
        for (int num : list2) {
            if (!set.contains(num)) return false;
        }
        return true;
    }

    // findKthLargest: Use PriorityQueue (min-heap) to track the k largest elements
    public int findKthLargest(int[] array, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : array) {
            pq.offer(num);
            if (pq.size() > k) pq.poll();
        }
        return pq.peek();
    }

    // sort2Arrays: Combine both arrays, sort, and return
    public int[] sort2Arrays(int[] array1, int[] array2) {
        int[] result = new int[array1.length + array2.length];
        int idx = 0;
        for (int num : array1) result[idx++] = num;
        for (int num : array2) result[idx++] = num;
        Arrays.sort(result);
        return result;
    }
}
