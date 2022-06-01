package ultimate.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class MergeSortExecutor {

    public static void main(String[] args) {
        List<Integer> places = Arrays.asList(44, 12, -5, 12, 8875, 32, -12, 554, 1, 75, -32, 54,
                123, -7, 99, 0, 65, -23, 4, 6, 7, 2, -55, 786);
        ParallelMergeSortTask<Integer> sortTask = new ParallelMergeSortTask<>(places);
        ParallelMergeSortAction<Integer> sortAction = new ParallelMergeSortAction<>(places);
        ForkJoinPool pool = new ForkJoinPool();

        List<Integer> resultTasks = pool.invoke(sortTask);
        resultTasks.forEach(System.out::println);

        pool.invoke(sortAction);
        places.forEach(System.out::println);

    }
}
