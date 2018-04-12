package org.springboot.demo;

public class testClassIdentity {

    public static void main(String[] args) throws Exception {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(2);
        ListNode node4 = new ListNode(4);
        node3.setNext(node4);
        node2.setNext(node3);
        node1.setNext(node2);

        deleteNode(node1);
    }

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) { val = x; }
     * }
     */
    public static void deleteNode(ListNode node) {
        while (true) {
            ListNode tem = node.getNext();
            if (tem.getVal() == 3) {
                node.setNext(tem.getNext());
                tem.setNext(null);
                return;
            }
        }
    }
}
