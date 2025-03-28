public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создаём задачи
        Task task1 = new Task("Task1", "Desc1");
        Task task2 = new Task("Task2", "Desc2");
        manager.createTask(task1);
        manager.createTask(task2);

        // Эпики и подзадачи
        Epic epic1 = new Epic("Epic1", "Epic desc");
        manager.createEpic(epic1);
        Subtask sub1 = new Subtask("Sub1", "Sub desc", epic1.getId());
        Subtask sub2 = new Subtask("Sub2", "Sub desc", epic1.getId());
        Subtask sub3 = new Subtask("Sub3", "Sub desc", epic1.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);
        manager.createSubtask(sub3);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(sub1.getId());
        manager.getSubtaskById(sub2.getId());
        manager.getSubtaskById(sub3.getId());
        manager.getTaskById(task1.getId());
        manager.getSubtaskById(sub2.getId());

        System.out.println("=== История после нескольких запросов ===");
        System.out.println(manager.getHistory());

        manager.removeTaskById(task2.getId());
        System.out.println("=== История после удаления task2 ===");
        System.out.println(manager.getHistory());

        manager.removeEpicById(epic1.getId());
        System.out.println("=== История после удаления epic1 (и его подзадач) ===");
        System.out.println(manager.getHistory());
    }
}
