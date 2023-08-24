
package sorting;


/** An interface that describes several algorithms for sorting a list */
public interface SortingInterface
{
    void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed);

    void iterativeMergeSort(Comparable[] array);

    void randomizedQuickSort(Comparable[] array, int lowindex, int highindex);

    void hybridSort(Comparable[] array, int lowindex, int highindex);

    void bucketSort(Elem[] array, int lowindex, int highindex, boolean reversed);

    void externalSort(String inputFile, String outputFile, int k, int m);
}
