package com.freezing.leetcode.jike;

import java.util.PriorityQueue;

public class Sort {
    /**
     * 快速排序
     *
     * @param array
     * @param begin
     * @param end
     */
    public void quickSort(int[] array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot - 1);
        quickSort(array, pivot + 1, end);
    }

    public int partition(int[] a, int begin, int end) {
        int pivot = end, counter = begin;
        for (int i = begin; i < end; i++) {
            if (a[i] < a[pivot]) {
                swap(a, i, counter);
                counter++;
            }
        }
        swap(a, pivot, counter);
        return counter;
    }

    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    /**
     * 冒泡排序
     *
     * @param a
     */
    public void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) swap(a, j, j + 1);
            }
        }
    }

    /**
     * 选择排序
     *
     * @param a
     */
    public void selectSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                min = a[min] < a[j] ? min : j;
            }
            swap(a, i, min);
        }
    }

    /**
     * 插入排序
     *
     * @param a
     */
    public void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int num = a[i];
            int j = i - 1;
            while (j >= 0 && num < a[j]) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = num;
        }
    }

    /**
     * 希尔排序
     *
     * @param a
     */
    public void shellSort(int[] a) {
        int len = a.length;
        for (int gap = len / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < len; i++) {
                int j = i, num = a[j];
                while (j - gap >= 0 && num < a[j - gap]) {
                    a[j] = a[j - gap];
                    j -= gap;
                }
                a[j] = num;
            }
        }
    }

    /**
     * 归并排序
     *
     * @param a
     */
    public void mergeSort(int[] a) {
        mergeSort(a, 0, a.length - 1);
    }

    public void mergeSort(int[] a, int left, int right) {
        if (right <= left) return;
        int mid = (left + right) >>> 1;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        int i = left, j = mid + 1, k = 0;
        int[] tmp = new int[right - left + 1];
        while (i <= mid && j <= right) tmp[k++] = a[i] < a[j] ? a[i++] : a[j++];
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= right) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, left, tmp.length);
    }

    /**
     * 堆排序
     *
     * @param a
     */
    public void heapSort(int[] a) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int i = 0; i < a.length; i++) {
            priorityQueue.add(a[i]);
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = priorityQueue.poll();
        }
    }

    public static void main(String[] args) {
        Sort sort = new Sort();
        int[] arr = new int[]{6, 3, 4, 2, 1, 5};
//        sort.quickSort(arr, 0, arr.length-1);
//        sort.bubbleSort(arr);
//        sort.selectSort(arr);
//        sort.insertSort(arr);
//        sort.shellSort(arr);
//        sort.mergeSort(arr);
        sort.heapSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }

    }
}
