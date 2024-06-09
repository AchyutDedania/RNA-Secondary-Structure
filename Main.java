import java.io.*;
import java.util.*;
import java.time.*;

/**
 * @brief Main class is used to implement our Algorithm to find the maximum number of matching pairs of base pairs in a given RNA sequence.
 * @author : Achyut Dedania, Meet Patel, Ayush Patel, Shrey Paunwala
 * @date : 26/04/2024
 */
public class Main {
    static Map<Pair<Integer, Integer>, Integer> pairMap = new HashMap<>();
    static int[][] dpMatrix;
    static List<Pair<Integer, Integer>> pairList = new ArrayList<>();

    /**
     * @param sequence This is the RNA sequence which is to be processed.
     * @param x This is the starting index of the pair.
     * @param y This is the ending index of the pair.
     * @return This returns whether the pair is valid or not.
     */
    static boolean checkMatch(String sequence, int x, int y) {
        return (sequence.charAt(x) == 'A' && sequence.charAt(y) == 'U') || (sequence.charAt(y) == 'A' && sequence.charAt(x) == 'U') ||
                (sequence.charAt(x) == 'C' && sequence.charAt(y) == 'G') || (sequence.charAt(y) == 'C' && sequence.charAt(x) == 'G');
    }

    /**
     * @param sequence This is the RNA sequence which is to be processed.
     * @param len This is the length of the sequence.
     */
    static void processFunc(String sequence, int len) {
        for (int gap = 5; gap <= len - 1; gap++) {
            int start = 0;
            int end = start + gap;
            while (end < len) {
                int value = dpMatrix[start][end - 1];
                int finalIndex = -1;
                for (int t = start; t < end - 4; t++) {
                    if (checkMatch(sequence, end, t)) {
                        if ((((t - 1 < 0) ? 0 : dpMatrix[start][t - 1]) + dpMatrix[t + 1][end - 1] + 1) > value) {
                            value = ((t - 1 < 0) ? 0 : dpMatrix[start][t - 1]) + dpMatrix[t + 1][end - 1] + 1;
                            finalIndex = t;
                        }
                    }
                }
                pairMap.put(new Pair<>(start, end), finalIndex);
                dpMatrix[start][end] = value;
                start++;
                end++;
            }
        }
    }
    /**
     * @param i This is the start index of the pair.
     * @param j This is the end index of the pair.
     */
    static void createPairs(int i, int j) {
        if (i >= j - 4) {
            return;
        }
        int t = pairMap.get(new Pair<>(i, j));
        if (t != -1) {
            pairList.add(new Pair<>(t, j));
            createPairs(i, t - 1);
            createPairs(t + 1, j - 1);
        } else {
            createPairs(i, j - 1);
        }
    }

    /**
     * @param args the command line arguments
     * @throws IOException if the file is not found.
     */
    public static void main(String[] args) throws IOException {
        File inputFile = new File("input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String input = reader.readLine();
        int len = input.length();
        dpMatrix = new int[len][len];
        long startTime = System.nanoTime();
        processFunc(input, len);
        System.out.println(dpMatrix[0][len - 1]);
        createPairs(0, len - 1);
        reader.close();
        long endTime = System.nanoTime();
        System.out.println("It took " + (endTime - startTime) + " nanoseconds!");
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        writer.write(input + "\n");
        for (Pair<Integer, Integer> i : pairList) {
            writer.write(i.getKey() + "," + i.getValue() + "\n");
        }
        writer.close();
    }
}

/**
 * @brief A class to store a pair of integers.
 */
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}