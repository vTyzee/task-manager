import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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