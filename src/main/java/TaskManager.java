import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс-менеджер для управления всеми задачами
 */
public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int nextId = 1; // счетчик для ид


    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    /**
     * Создаёт эпик
     */
    public Epic createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    /**
     * Создаёт подзадачу
     */
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        // Привязываем подзадачу к её эпику
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }

        return subtask;
    }

    // --- Получение всех задач ---
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // --- Очистка всех задач ---
    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        // При удалении всех эпиков заодно удаляем и подзадачи
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        // Нужно очистить и ссылки в эпиках
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    // --- Получение задачи эпика подзадачи по id
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // --- Удаление задачи/эпика/подзадачи по id
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }
    }

    // Обновление задач
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            // Текущий эпик
            Epic currentEpic = epics.get(updatedEpic.getId());
            // Список подзадач не меняем "снаружи", поэтому берём из текущего эпика
            updatedEpic.getSubtaskIds().clear();
            updatedEpic.getSubtaskIds().addAll(currentEpic.getSubtaskIds());

            epics.put(updatedEpic.getId(), updatedEpic);

            // Пересчитываем статус
            updateEpicStatus(updatedEpic);
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Subtask oldSubtask = subtasks.get(updatedSubtask.getId());
            int oldEpicId = oldSubtask.getEpicId();
            int newEpicId = updatedSubtask.getEpicId();

            // Если эпик у подзадачи поменялся
            if (oldEpicId != newEpicId) {
                // Удалим из старого эпика
                Epic oldEpic = epics.get(oldEpicId);
                if (oldEpic != null) {
                    oldEpic.removeSubtaskId(oldSubtask.getId());
                    updateEpicStatus(oldEpic);
                }
                // Добавим к новому эпику
                Epic newEpic = epics.get(newEpicId);
                if (newEpic != null) {
                    newEpic.addSubtaskId(updatedSubtask.getId());
                }
            }

            // Сохраняем обновлённую подзадачу
            subtasks.put(updatedSubtask.getId(), updatedSubtask);

            // Пересчитываем статус эпика
            Epic epic = epics.get(updatedSubtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        }
    }


    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> result = new ArrayList<>();
        if (epic == null) return result;

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                result.add(subtask);
            }
        }
        return result;
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            // Если подзадач нет — ставим NEW
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                continue;
            }

            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
