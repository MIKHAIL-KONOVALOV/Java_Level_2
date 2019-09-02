package TwoThreadArrays;

import java.io.File;

/** Change array ellements
 * @author Mihail
 * Created on 29.08.2019
 */
public class Array extends Thread {
    private static final int SIZE = 10000000; //Array Size
    private static final int H = SIZE / 2;
    private float[] arr = new float[SIZE];

    public Array() {                        // Init array
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }
    }

    public void firstMethod() {
        long t1 = System.currentTimeMillis();   //Check start time
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) *
                    Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        long t2 = System.currentTimeMillis();  //Check finish time
        System.out.println("Time = " + (t2 - t1) + " millis.");
    }

    public void secondMethod() {
        float[] arr1 = new float[H];
        float[] arr2 = new float[H];

        long t1 = System.currentTimeMillis();  // Start Time
        System.arraycopy(arr, 0, arr1, 0, H);
        System.arraycopy(arr, H, arr2, 0, H);

        Thread thread1 = new Thread() {         // Create two threads:
            public void run() {
                for (int i = 0; i < H; i++) {
                    arr1[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) *
                              Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }

            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                for (int i = 0; i < H; i++) {
                    arr2[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) *
                            Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        };

        thread1.start();            // Start threads
        thread2.start();

        System.arraycopy(arr1, 0, arr, 0, H);
        System.arraycopy(arr2, 0, arr, H, H);


        long t2 = System.currentTimeMillis();       //Finish time
        System.out.println("Time = " + (t2 - t1) + " millis.");
    }
}
