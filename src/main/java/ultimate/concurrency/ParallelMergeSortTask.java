package ultimate.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSortTask<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

    private final List<T> initialValues;


    public ParallelMergeSortTask(List<T> initialValues) {
        this.initialValues = initialValues;
    }

    @Override
    protected List<T> compute() {
        if (initialValues.size() < 2) {
            return initialValues;
        }

        ParallelMergeSortTask<T> left = new ParallelMergeSortTask<>(
                new ArrayList<>(initialValues.subList(0, initialValues.size() / 2)));
        ParallelMergeSortTask<T> right = new ParallelMergeSortTask<>(
                new ArrayList<>(initialValues.subList(initialValues.size() / 2, initialValues.size())));
        left.fork();
        List<T> sortRightPart = right.compute();
        List<T> sortLeftPart = left.join();
        merger(sortLeftPart, sortRightPart);
        return initialValues;
    }

    private void merger(List<T> left, List<T> right) {
        var leftIndex = 0;
        var rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 0) {
                initialValues.set(leftIndex + rightIndex, left.get(leftIndex++));
            } else {
                initialValues.set(leftIndex + rightIndex, right.get(rightIndex++));
            }
        }
        while (leftIndex < left.size()) {
            initialValues.set(leftIndex + rightIndex, left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            initialValues.set(rightIndex + leftIndex, right.get(rightIndex++));
        }
    }
}
