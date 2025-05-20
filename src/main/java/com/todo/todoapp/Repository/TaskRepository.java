package com.todo.todoapp.Repository;

import com.todo.todoapp.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
