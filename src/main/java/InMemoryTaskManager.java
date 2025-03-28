import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task t = tasks.get(id);
        historyManager.add(t);
        return t;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic e = epics.get(id);
        historyManager.add(e);
        return e;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask s = subtasks.get(id);
        historyManager.add(s);
        return s;
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.remove(id) != null) {
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic e = epics.remove(id);
        if (e != null) {
            historyManager.remove(id);
            for (Integer sId : e.getSubtaskIds()) {
                subtasks.remove(sId);
                historyManager.remove(sId);
            }
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask s = subtasks.remove(id);
        if (s != null) {
            historyManager.remove(id);
            Epic e = epics.get(s.getEpicId());
            if (e != null) {
                e.removeSubtaskId(id);
                updateEpicStatus(e);
            }
        }
    }

    @Override
    public void updateTask(Task t) {
        if (tasks.containsKey(t.getId())) {
            tasks.put(t.getId(), t);
        }
    }

    @Override
    public void updateEpic(Epic e) {
        if (epics.containsKey(e.getId())) {
            Epic old = epics.get(e.getId());
            e.getSubtaskIds().clear();
            e.getSubtaskIds().addAll(old.getSubtaskIds());
            epics.put(e.getId(), e);
            updateEpicStatus(e);
        }
    }

    @Override
    public void updateSubtask(Subtask s) {
        if (subtasks.containsKey(s.getId())) {
            Subtask old = subtasks.get(s.getId());
            int oldE = old.getEpicId();
            int newE = s.getEpicId();
            if (oldE != newE) {
                Epic prevEpic = epics.get(oldE);
                if (prevEpic != null) {
                    prevEpic.removeSubtaskId(s.getId());
                    updateEpicStatus(prevEpic);
                }
                Epic newEpic = epics.get(newE);
                if (newEpic != null) {
                    newEpic.addSubtaskId(s.getId());
                }
            }
            subtasks.put(s.getId(), s);
            Epic e = epics.get(s.getEpicId());
            if (e != null) {
                updateEpicStatus(e);
            }
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic e = epics.get(epicId);
        List<Subtask> res = new ArrayList<>();
        if (e == null) return res;
        for (Integer sId : e.getSubtaskIds()) {
            Subtask s = subtasks.get(sId);
            if (s != null) {
                res.add(s);
            }
        }
        return res;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic e) {
        List<Integer> sIds = e.getSubtaskIds();
        if (sIds.isEmpty()) {
            e.setStatus(Status.NEW);
            return;
        }
        boolean allDone = true;
        boolean allNew = true;
        for (Integer id : sIds) {
            Subtask s = subtasks.get(id);
            if (s != null) {
                if (s.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (s.getStatus() != Status.NEW) {
                    allNew = false;
                }
            }
        }
        if (allDone) {
            e.setStatus(Status.DONE);
        } else if (allNew) {
            e.setStatus(Status.NEW);
        } else {
            e.setStatus(Status.IN_PROGRESS);
        }
    }
}