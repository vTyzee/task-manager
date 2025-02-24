import java.util.List;

public interface HistoryManager {
    // Помечаем задачу как "просмотренную"
    void add(Task task);
    // Возвращаем список последних просмотренных задач
    List<Task> getHistory();
}
