package com.todo.todoapp.Controller;
import com.todo.todoapp.Entity.Task;
import com.todo.todoapp.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Frontend'ten erişim için
public class TaskController {
    @Autowired
    private final TaskService taskService;
// test
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
    // Update a task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @PutMapping("/{id}/complete")
    public Task markTaskAsCompleted(@PathVariable Long id) {
        return taskService.markTaskAsCompleted(id);
    }
}
