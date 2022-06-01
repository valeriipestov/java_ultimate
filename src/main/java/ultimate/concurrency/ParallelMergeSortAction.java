package ultimate.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSortAction<T extends Comparable<? super T>> extends RecursiveAction {

    private final List<T> initialValues;

    public ParallelMergeSortAction(List<T> initialValues) {
        this.initialValues = initialValues;
    }

    @Override
    protected void compute() {
        if (initialValues.size() < 2) {
            return;
        }

        ParallelMergeSortAction<T> left = new ParallelMergeSortAction<>(
                new ArrayList<>(initialValues.subList(0, initialValues.size() / 2)));
        ParallelMergeSortAction<T> right = new ParallelMergeSortAction<>(
                new ArrayList<>(initialValues.subList(initialValues.size() / 2, initialValues.size())));

        left.fork();
        right.compute();
        left.join();
        merge(left, right);
    }

    public List<T> getInitialValues() {
        return initialValues;
    }

    private void merge(ParallelMergeSortAction<T> left, ParallelMergeSortAction<T> right) {
        var leftIndex = 0;
        var rightIndex = 0;
        var leftList = left.getInitialValues();
        var rightList = right.getInitialValues();
        while (leftIndex < leftList.size() && rightIndex < rightList.size()) {
            if (leftList.get(leftIndex).compareTo(rightList.get(rightIndex)) < 0) {
                initialValues.set(leftIndex + rightIndex, leftList.get(leftIndex++));
            } else {
                initialValues.set(leftIndex + rightIndex, rightList.get(rightIndex++));
            }
        }
        while (leftIndex < leftList.size()) {
            initialValues.set(leftIndex + rightIndex, leftList.get(leftIndex++));
        }

        while (rightIndex < rightList.size()) {
            initialValues.set(leftIndex + rightIndex, rightList.get(rightIndex++));
        }

    }
}
