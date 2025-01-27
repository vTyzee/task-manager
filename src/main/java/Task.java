public class Task {
    private int id;             // Идентификатор задачи
    private String name;        // Название задачи
    private String description; // Описание задачи
    private Status status;      // Статус задачи

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW; // По умолчанию новая задача
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}