package hsrm.test.todolist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin(maxAge = 3600)
@RestController
public class TodoController {
    private final TodoRepository repository;

    public TodoController (TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/todos")
    Iterable<TodoItem> all(){
        return repository.findAll();
    }

    @GetMapping("/todos/{id}")
    TodoItem one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow( () -> new TodoItemNotFoundException(id));
    }

    @PostMapping("/todos/")
    public TodoItem createTodo(@RequestBody TodoItem todoItem){
        return repository.save(todoItem);
    }


    @PutMapping("/todos/{id}")
    public TodoItem updateTodo(@RequestBody TodoItem todoItem, @PathVariable("id") Long id){
        System.out.println(todoItem);
        if(repository.existsById(id)) {
            Optional<TodoItem> ti = repository.findById(id);
            if (ti.isPresent()) {
                TodoItem ti2 = ti.get();
                ti2.title = todoItem.title;
                ti2.state = todoItem.state;
                return repository.save(ti2);
            }
        }
        else
        {
            todoItem.id = id;
            return repository.save(todoItem);
        }
        return todoItem;
    }

    @DeleteMapping(path = "/todos/{id}")
    public void deleteTodo(@PathVariable("id") Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
        else throw new TodoItemNotFoundException(id);
    }

}
