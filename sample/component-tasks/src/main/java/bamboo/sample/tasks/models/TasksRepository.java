package bamboo.sample.tasks.models;

import java.util.ArrayList;
import java.util.List;


public class TasksRepository {

    private List<Task> tasks = new ArrayList<>();

    public void addTask(String name) {
        tasks.add(new Task(name));
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void deleteTask(String name) {
        for (int i = 0; i < tasks.size(); i++) {
            if (name.equals(tasks.get(i))) {
                tasks.remove(i);
                i--;
            }
        }
    }

}
