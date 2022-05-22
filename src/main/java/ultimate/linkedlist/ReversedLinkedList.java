package ultimate.linkedlist;

import java.util.Stack;

public class ReversedLinkedList {
    public static void main(String[] args) {
        var head = createLinkedList(4, 3, 9, 1);
        printReversedRecursively(head);
        printReversedUsingStack(head);
    }

    /**
     * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
     *
     * @param elements an array of elements that should be added to the list
     * @param <T>      elements type
     * @return head of the list
     */
    public static <T> Node<T> createLinkedList(T... elements) {
        var headNode = new Node<>(elements[0]);
        Node<T> curr = headNode;
        for (int i = 1; i < elements.length; i++) {
            curr.next = new Node<>(elements[i]);
            curr = curr.next;
        }
        return headNode;
    }

    /**
     * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
     * just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedRecursively(Node<T> head) {
        if (head.next != null) {
            printReversedRecursively(head.next);
        }
        System.out.println(head.element);
    }

    /**
     * Prints a list in a reserved order using a {@link Stack} instance. Please note that it should not change
     * the list, just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedUsingStack(Node<T> head) {
        Stack<T> stack = new Stack<>();
        while (head != null){
            stack.push(head.element);
            head = head.next;
        }
        while (!stack.isEmpty()){
            System.out.println(stack.pop());
        }
    }
}
