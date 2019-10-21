package es.gobcan.istac.indicators.core.task.repositoryimpl;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.task.domain.Task;

/**
 * Repository implementation for Task
 */
@Repository("taskRepository")
public class TaskRepositoryImpl extends TaskRepositoryBase {

    public TaskRepositoryImpl() {
        // NOTHING TO DO HERE
    }

    @Override
    public void deleteAndFlush(Task task) {
        delete(task);
        getEntityManager().flush();
    }
}
