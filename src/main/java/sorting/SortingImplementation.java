
package sorting;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**  A class that implements SortingInterface. Has various methods
 *   to sort a list of elements. */
public class SortingImplementation implements SortingInterface
{
    /**
     * Sorts the sublist of the given list (from lowindex to highindex, inclusive)
     * using insertion sort
     * @param array array of Comparable-s
     * @param lowindex the beginning index of a sublist
     * @param highindex the end index of a sublist
     * @param reversed if true, the list should be sorted in descending order
     */
    @Override
    public void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        if (array.length < 2) return;

        for (int i = lowindex+1; i <= highindex; i++)
        {
            Comparable right = array[i];
            int leftIndex = i-1;
            while ((leftIndex >= lowindex) && (reversed ? (right.compareTo(array[leftIndex]) > 0) : (right.compareTo(array[leftIndex]) < 0)))
            {
                array[leftIndex+1] = array[leftIndex];
                leftIndex--;
            }
            array[leftIndex+1] = right;
        }
    }

    /**
     * Sorts a given array of 2^k elements using iterative
     * (non-recursive) merge sort.
     * @param array array to sort
     */
    @Override
    public void iterativeMergeSort(Comparable[] array)
    {
        for (int i = 2; i <= array.length; i *= 2)
        {
            Comparable[] tmpArray = new Comparable[array.length];

            for (int j = 0; j < array.length /  i; j++)
            {
                int leftIndex = 0;
                int rightIndex = 0;
                for (int k = 0; k < i; k++)
                {
                    boolean leftMin;
                    if (rightIndex >= i/2) leftMin = true;
                    else if (leftIndex >= i/2) leftMin = false;
                    else leftMin = array[j*i + leftIndex].compareTo(array[j*i + i/2 + rightIndex]) <= 0;

                    if (leftMin)
                    {
                        tmpArray[j*i + k] = array[j*i + leftIndex];
                        leftIndex++;
                    }
                    else
                    {
                        tmpArray[j*i + k] = array[j*i + i/2 + rightIndex];
                        rightIndex++;
                    }
                }
            }

            System.arraycopy(tmpArray, 0, array, 0, array.length);
        }
    }

    /**
     * Sorts the sublist of the given list (from lowindex to highindex, inclusive)
     * using the randomizedQuickSort
     * @param array array to sort
     * @param lowindex the beginning index of a sublist
     * @param highindex the end index of a sublist
     */
    @Override
    public void randomizedQuickSort(Comparable[] array, int lowindex, int highindex)
    {
        int length = highindex - lowindex + 1;
        if (length < 2) return;
        if (length == 2)
        {
            if (array[lowindex].compareTo(array[highindex]) > 0)
                swap(array, lowindex, highindex);
            return;
        }
        if (length == 3)
        {
            sort3(array, lowindex, lowindex+1, highindex);
            return;
        }

        int pivotIndex = median3(array, lowindex, highindex);
        swap(array, highindex, pivotIndex);

        while (true)
        {
            int fromLeftIndex = lowindex;
            while (array[fromLeftIndex].compareTo(array[highindex]) <= 0)
            {
                fromLeftIndex++;
                if (fromLeftIndex == highindex - 1) break;
            }

            int fromRightIndex = highindex - 1;
            while (array[fromRightIndex].compareTo(array[highindex]) > 0)
            {
                fromRightIndex--;
                if (fromRightIndex == lowindex) break;
            }

            if (fromLeftIndex >= fromRightIndex)
            {
                if (array[highindex].compareTo(array[fromLeftIndex]) >= 0)
                {
                    pivotIndex = highindex;
                    break;
                }

                pivotIndex = fromLeftIndex;
                swap(array, highindex, pivotIndex);
                break;
            }

            swap(array, fromLeftIndex, fromRightIndex);
        }

        randomizedQuickSort(array, lowindex, pivotIndex-1);
        randomizedQuickSort(array,pivotIndex+1, highindex);
    }

    private static void swap(Comparable[] array, int a, int b)
    {
        Comparable tmp = array[b];
        array[b] = array[a];
        array[a] = tmp;
    }

    private static int median3(Comparable[] array, int lowindex, int highindex)
    {
        int[] indexes = new int[3];
        {
            Random rand = new Random();
            int length = highindex - lowindex + 1;
            indexes[0] = lowindex + rand.nextInt(length);
            indexes[1] = lowindex + rand.nextInt(length);
            indexes[2] = lowindex + rand.nextInt(length);
        }

        if (array[indexes[0]].compareTo(array[indexes[1]]) >= 0)
        {
            if (array[indexes[0]].compareTo(array[indexes[2]]) >= 0)
                return array[indexes[1]].compareTo(array[indexes[2]]) >= 0 ? indexes[1] : indexes[2];
            else
                return indexes[0];
        }
        else if (array[indexes[1]].compareTo(array[indexes[2]]) >= 0)
        {
            return array[indexes[0]].compareTo(array[indexes[2]]) >= 0 ? indexes[0] : indexes[2];
        }
        else return indexes[1];
    }

    private static void sort3(Comparable[] a, int l, int i, int r)
    {
        Comparable leftval = a[l];
        Comparable interval = a[i];
        Comparable rightval = a[r];

        if (a[l].compareTo(a[i]) >= 0)
        {
            if (a[l].compareTo(a[r]) >= 0)
            {
                if (a[i].compareTo(a[r]) >= 0)
                {
                    //l > i > r
                    a[l] = rightval;
                    a[i] = interval;
                    a[r] = leftval;
                }
                else
                {
                    //l > r > i
                    a[l] = interval;
                    a[i] = rightval;
                    a[r] = leftval;
                }
            }
            else
            {
                //r > l > i
                a[l] = interval;
                a[i] = leftval;
                a[r] = rightval;
            }
        }
        //i > l
        else if (a[i].compareTo(a[r]) >= 0)
        {
            if (a[l].compareTo(a[r]) >= 0)
            {
                //i > l > r
                a[l] = rightval;
                a[i] = leftval;
                a[r] = interval;
            }
            else
            {
                //i > r > l
                a[l] = leftval;
                a[i] = rightval;
                a[r] = interval;
            }
        }
        else
        {
            //r > i > l
            a[l] = leftval;
            a[i] = interval;
            a[r] = rightval;
        }
    }

    /**
     * Sorts a given sublist using hybrid sort, where the list is sorted
     * using randomized quick sort; when sublists get small (size=10?),
     * they are sorted using insertion sort.
     * @param array array of Comparable-s to sort
     * @param lowindex the beginning index of the sublist
     * @param highindex the end index of the sublist (inclusive)
     */
    @Override
    public void hybridSort(Comparable[] array, int lowindex, int highindex)
    {
        int length = highindex - lowindex + 1;
        if (length < 2) return;
        if (length == 2)
        {
            if (array[lowindex].compareTo(array[highindex]) > 0)
                swap(array, lowindex, highindex);
            return;
        }
        if (length == 3)
        {
            sort3(array, lowindex, lowindex+1, highindex);
            return;
        }
        if (length <= 10)
        {
            insertionSort(array, lowindex, highindex, false);
            return;
        }

        int pivotIndex = median3(array, lowindex, highindex);
        swap(array, highindex, pivotIndex);

        while (true)
        {
            int fromLeftIndex = lowindex;
            while (array[fromLeftIndex].compareTo(array[highindex]) <= 0)
            {
                fromLeftIndex++;
                if (fromLeftIndex == highindex - 1) break;
            }

            int fromRightIndex = highindex - 1;
            while (array[fromRightIndex].compareTo(array[highindex]) > 0)
            {
                fromRightIndex--;
                if (fromRightIndex == lowindex) break;
            }

            if (fromLeftIndex >= fromRightIndex)
            {
                if (array[highindex].compareTo(array[fromLeftIndex]) >= 0)
                {
                    pivotIndex = highindex;
                    break;
                }

                pivotIndex = fromLeftIndex;
                swap(array, highindex, pivotIndex);
                break;
            }

            swap(array, fromLeftIndex, fromRightIndex);
        }

        hybridSort(array, lowindex, pivotIndex-1);
        hybridSort(array,pivotIndex+1, highindex);
    }

    /**
     * Sorts a sub-array of records using bucket sort.
     * @param array array of records
     * @param lowindex the beginning index of the sublist to sort
     * @param highindex the end index of the sublist to sort; inclusive
     * @param reversed if true, sort in descending (decreasing) order, otherwise ascending
     */
    @Override
    public void bucketSort(Elem[] array, int lowindex, int highindex, boolean reversed)
    {
        if (reversed)
        {
            rbs(array, lowindex, highindex);
            return;
        }

        int max = 0;
        for (int i = lowindex; i <= highindex; i++)
        {
            if (array[i].key() > max) max = array[i].key();
        }

        int length = highindex - lowindex + 1;
        int n_buckets = length / 2;
        int s_buckets = max / n_buckets;

        LinkedList<Elem> ll = new LinkedList<>();

        for (int b = 0; b < n_buckets; b++)
        {
            LinkedList<Elem> sub = new LinkedList<>();
            for (int i = lowindex; i <= highindex; i++)
            {
                if (b == n_buckets-1 && array[i].key() >= b*s_buckets)
                    sub.add(array[i]);
                else if (array[i].key() < (b+1)*s_buckets && array[i].key() >= b*s_buckets)
                    sub.add(array[i]);
            }

            if (sub.size() == 1)
            {
                ll.addAll(sub);
            }
            else if (sub.size() > 1)
            {
                Elem[] arr = sub.toArray(new Elem[0]);
                for (int i = 0; i < sub.size()-1; i++)
                {
                    for (int j = i+1; j < sub.size(); j++)
                    {
                        if (arr[j].key() < arr[i].key())
                        {
                            Elem e = arr[i];
                            arr[i] = arr[j];
                            arr[j] = e;
                        }
                    }
                }
                ll.addAll(List.of(arr));
            }
        }

        Elem[] arr = ll.toArray(new Elem[ll.size()]);
        for (int i = 0; i < length; i++)
        {
            array[i+lowindex] = new Elem(arr[i].key(), arr[i].data());
        }
    }

    private static void rbs(Elem[] array, int lowindex, int highindex)
    {
        boolean reversed = true;

        int max = 0;
        for (int i = lowindex; i <= highindex; i++)
        {
            if (array[i].key() > max) max = array[i].key();
        }

        int length = highindex - lowindex + 1;
        int n_buckets = length / 2;
        int s_buckets = max / n_buckets;

        LinkedList<Elem> ll = new LinkedList<>();


        for (int b = n_buckets-1; b >= 0; b--)
        {
            LinkedList<Elem> sub = new LinkedList<>();
            for (int i = lowindex; i <= highindex; i++)
            {
                if (b == n_buckets-1 && array[i].key() >= b*s_buckets)
                    sub.add(array[i]);
                else if (array[i].key() < (b+1)*s_buckets && array[i].key() >= b*s_buckets)
                    sub.add(array[i]);
            }

            if (sub.size() == 1)
            {
                ll.addAll(sub);
            }
            else if (sub.size() > 1)
            {
                List<Elem> s = sub.stream().sorted(Comparator.comparingInt(Elem::key).reversed()).collect(Collectors.toList());
                ll.addAll(s);
            }
        }

        Elem[] arr = ll.toArray(new Elem[ll.size()]);
        for (int i = 0; i < length; i++)
        {
            array[i+lowindex] = new Elem(arr[i].key(), arr[i].data());
        }
    }

    /**
     * Implements external sort method
     * @param inputFile The file that contains the input list
     * @param outputFile The file where to output the sorted list
     * @param k number of elements that fit into memory at once
     * @param m number of chunks
     */
    public void externalSort(String inputFile, String outputFile, int k, int m)
    { try {
        BufferedReader reader = Files.newBufferedReader(Path.of(inputFile));
        Integer[] buf = new Integer[k];

        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < k; j++)
            {
                String tmp = reader.readLine();
                if (tmp == null) break;
                buf[j] = Integer.parseInt(tmp);
            }

            hybridSort(buf, 0, buf.length-1);

            BufferedWriter tmp_file = new BufferedWriter(new FileWriter("temp" + i + ".txt"));
            tmp_file.flush();
            for (Integer n : buf)
                tmp_file.write(String.format("%d\n", n));

            tmp_file.close();
        }

        reader.close();

        BufferedReader[] readers = new BufferedReader[m];
        int[] currentVals = new int[m];
        for (int i = 0; i < m; i++)
        {
            readers[i] = new BufferedReader(new FileReader("temp" + i + ".txt"));
            String val = readers[i].readLine();
            if (val != null)
                currentVals[i] = Integer.parseInt(val);
        }

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

        for (int i = 0; i < m*k; i++)
        {
            int min = currentVals[0];
            int fileToPush = 0;

            for (int j = 0; j < m; j++)
            {
                if (currentVals[j] < min)
                {
                    min = currentVals[j];
                    fileToPush = j;
                }
            }

            writer.println(min);

            String val = readers[fileToPush].readLine();
            if (val != null)
                currentVals[fileToPush] = Integer.parseInt(val);
            else
                currentVals[fileToPush] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < m; i++)
        {
            readers[i].close();
        }
        writer.close();
    } catch (IOException e) { e.printStackTrace(); } }
}
