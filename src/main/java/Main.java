public class Main {
    public static void main(String[] args) {
        // Теперь получаем менеджер через утилитный класс
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1.1", "Описание Подзадачи 1.1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 1.2", "Описание Подзадачи 1.2", epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2");
        manager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Подзадача 2.1", "Описание Подзадачи 2.1", epic2.getId());
        manager.createSubtask(subtask3);

        System.out.println("=== Список всех задач ===");
        System.out.println(manager.getAllTasks());

        System.out.println("=== Список всех эпиков ===");
        System.out.println(manager.getAllEpics());

        System.out.println("=== Список всех подзадач ===");
        System.out.println(manager.getAllSubtasks());

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);

        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);

        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);

        System.out.println("\n=== После обновления статусов ===");
        System.out.println("Task1 = " + manager.getTaskById(task1.getId()));
        System.out.println("Subtask1 = " + manager.getSubtaskById(subtask1.getId()));
        System.out.println("Subtask2 = " + manager.getSubtaskById(subtask2.getId()));
        System.out.println("Epic1 = " + manager.getEpicById(epic1.getId()));

        // Проверяем работу истории (вызываем несколько getById)
        System.out.println("\n=== Проверяем историю просмотров (после нескольких getById) ===");
        System.out.println(manager.getHistory());

        manager.removeTaskById(task2.getId());
        manager.removeEpicById(epic2.getId());

        System.out.println("\n=== После удаления task2 и epic2 ===");
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());

        // И ещё раз история:
        System.out.println("\n=== История просмотров (итог) ===");
        System.out.println(manager.getHistory());
    }
}
