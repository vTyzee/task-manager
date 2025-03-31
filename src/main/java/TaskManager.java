import java.util.List;

public interface TaskManager {
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    void removeAllTasks();
    void removeAllEpics();
    void removeAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    void updateTask(Task updatedTask);
    void updateEpic(Epic updatedEpic);
    void updateSubtask(Subtask updatedSubtask);

    List<Subtask> getSubtasksOfEpic(int epicId);

    // Новый метод для получения истории
    List<Task> getHistory();
}
