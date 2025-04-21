import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private Node head;  // голова списка
    private Node tail;  // хвост списка

    private final Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;
        // чтобы не было дубликатов
        remove(task.getId());
        // новый просмотр в конец списка
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        nodeMap.put(task.getId(), newNode);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev == null && next == null) {
            head = null;
            tail = null;
            return;
        }
        if (prev == null) {
            head = next;
            next.prev = null;
            return;
        }
        if (next == null) {
            tail = prev;
            prev.next = null;
            return;
        }
        prev.next = next;
        next.prev = prev;
    }

    // Проходим от head до tail, собирая задачи в ArrayList
    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.task);
            current = current.next;
        }
        return result;
    }
}
