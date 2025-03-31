import lombok.Getter;

@Getter
public class Subtask extends Task {
    private final int epicId; // ID эпика, к которому относится подзадача

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                '}';
    }
}