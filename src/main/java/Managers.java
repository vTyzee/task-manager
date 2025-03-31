public class Managers {

    // Возвращает реализацию менеджера задач
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Возвращает реализацию менеджера истории
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
