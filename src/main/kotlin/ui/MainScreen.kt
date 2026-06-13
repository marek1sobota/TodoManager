package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import builder.ReportDirector
import builder.TextReportBuilder
import factory.ThemeFactory
import model.Priority
import model.Task
import repository.TaskRepository
import utils.ReportExporter

@Composable
fun MainScreen(
    darkMode: Boolean,
    themeFactory: ThemeFactory,
    onThemeChange: () -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    var searchText by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(TaskFilter.ALL) }
    var sortOption by remember { mutableStateOf(SortOption.CREATED_AT) }

    var editingTaskId by remember { mutableStateOf<Int?>(null) }
    var editingTitle by remember { mutableStateOf("") }
    var editingPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val tasks by TaskRepository.tasks.collectAsState()

    val visibleTasks = tasks
        .filter {
            it.title.contains(
                searchText,
                ignoreCase = true
            )
        }
        .filter {
            when (filter) {
                TaskFilter.ACTIVE -> !it.completed
                TaskFilter.COMPLETED -> it.completed
                TaskFilter.ALL -> true
            }
        }
        .sortedWith(taskComparator(sortOption))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeFactory.backgroundColor())
            .padding(16.dp)
    ) {
        TopBar(
            darkMode = darkMode,
            themeFactory = themeFactory,
            onThemeChange = onThemeChange,
            onExportReport = {
                val report = ReportDirector(
                    TextReportBuilder()
                ).createReport(tasks)

                ReportExporter.export(report)
            }
        )

        Spacer(Modifier.height(12.dp))

        FilterBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            filter = filter,
            onFilterChange = { filter = it },
            sortOption = sortOption,
            onSortChange = { sortOption = it },
            themeFactory = themeFactory
        )

        Spacer(Modifier.height(12.dp))

        AddTaskRow(
            taskTitle = taskTitle,
            onTaskTitleChange = { taskTitle = it },
            selectedPriority = selectedPriority,
            onPriorityChange = { selectedPriority = it },
            themeFactory = themeFactory,
            onAddTask = {
                if (taskTitle.isNotBlank()) {
                    TaskRepository.addTask(
                        taskTitle.trim(),
                        selectedPriority
                    )

                    taskTitle = ""
                    selectedPriority = Priority.MEDIUM
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = visibleTasks,
                key = { it.id }
            ) { task ->
                TaskCard(
                    task = task,
                    themeFactory = themeFactory,
                    isEditing = editingTaskId == task.id,
                    editingTitle = editingTitle,
                    onEditingTitleChange = { editingTitle = it },
                    editingPriority = editingPriority,
                    onEditingPriorityChange = { editingPriority = it },
                    onToggle = {
                        TaskRepository.toggleTask(task)
                    },
                    onEdit = {
                        editingTaskId = task.id
                        editingTitle = task.title
                        editingPriority = task.priority
                    },
                    onSave = {
                        if (editingTitle.isNotBlank()) {
                            TaskRepository.updateTask(
                                task.id,
                                editingTitle.trim(),
                                editingPriority
                            )

                            editingTaskId = null
                        }
                    },
                    onCancelEdit = {
                        editingTaskId = null
                    },
                    onDeleteRequest = {
                        taskToDelete = task
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        StatisticsBar(tasks)
    }

    taskToDelete?.let { task ->
        DeleteConfirmationDialog(
            task = task,
            onConfirm = {
                TaskRepository.deleteTask(task)
                taskToDelete = null

                if (editingTaskId == task.id) {
                    editingTaskId = null
                }
            },
            onDismiss = {
                taskToDelete = null
            }
        )
    }
}

@Composable
private fun TopBar(
    darkMode: Boolean,
    themeFactory: ThemeFactory,
    onThemeChange: () -> Unit,
    onExportReport: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Todo Manager",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "Export Report",
                themeFactory = themeFactory,
                onClick = onExportReport
            )

            AppButton(
                text = if (darkMode) "Light Theme" else "Dark Theme",
                themeFactory = themeFactory,
                onClick = onThemeChange
            )
        }
    }
}

@Composable
private fun FilterBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    filter: TaskFilter,
    onFilterChange: (TaskFilter) -> Unit,
    sortOption: SortOption,
    onSortChange: (SortOption) -> Unit,
    themeFactory: ThemeFactory
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search task")
            },
            colors = textFieldColors(themeFactory)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.entries.forEach { item ->
                    FilterButton(
                        text = item.label,
                        selected = filter == item,
                        themeFactory = themeFactory,
                        onClick = {
                            onFilterChange(item)
                        }
                    )
                }
            }

            SortMenu(
                selected = sortOption,
                onSortChange = onSortChange,
                themeFactory = themeFactory
            )
        }
    }
}

@Composable
private fun AddTaskRow(
    taskTitle: String,
    onTaskTitleChange: (String) -> Unit,
    selectedPriority: Priority,
    onPriorityChange: (Priority) -> Unit,
    themeFactory: ThemeFactory,
    onAddTask: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskTitle,
            onValueChange = onTaskTitleChange,
            modifier = Modifier.weight(1f),
            label = {
                Text("Task title")
            },
            colors = textFieldColors(themeFactory)
        )

        PriorityMenu(
            selectedPriority = selectedPriority,
            onPriorityChange = onPriorityChange,
            themeFactory = themeFactory
        )

        AppButton(
            text = "Add",
            themeFactory = themeFactory,
            onClick = onAddTask
        )
    }
}

@Composable
private fun TaskCard(
    task: Task,
    themeFactory: ThemeFactory,
    isEditing: Boolean,
    editingTitle: String,
    onEditingTitleChange: (String) -> Unit,
    editingPriority: Priority,
    onEditingPriorityChange: (Priority) -> Unit,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onCancelEdit: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    Card(
        backgroundColor = themeFactory.cardColor(),
        elevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = {
                    onToggle()
                }
            )

            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isEditing) {
                    TextField(
                        value = editingTitle,
                        onValueChange = onEditingTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Task title")
                        },
                        colors = textFieldColors(themeFactory)
                    )

                    PriorityMenu(
                        selectedPriority = editingPriority,
                        onPriorityChange = onEditingPriorityChange,
                        themeFactory = themeFactory
                    )
                } else {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.subtitle1,
                        textDecoration =
                            if (task.completed)
                                TextDecoration.LineThrough
                            else
                                TextDecoration.None
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriorityBadge(
                            priority = task.priority,
                            themeFactory = themeFactory
                        )

                        Text(
                            text = createdAtText(task.createdAt),
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isEditing) {
                    AppButton(
                        text = "Save",
                        themeFactory = themeFactory,
                        onClick = onSave
                    )

                    OutlinedButton(
                        onClick = onCancelEdit
                    ) {
                        Text("Cancel")
                    }
                } else {
                    AppButton(
                        text = "Edit",
                        themeFactory = themeFactory,
                        onClick = onEdit
                    )
                }

                OutlinedButton(
                    onClick = onDeleteRequest
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun PriorityBadge(
    priority: Priority,
    themeFactory: ThemeFactory
) {
    val color = when (priority) {
        Priority.HIGH -> themeFactory.priorityHighColor()
        Priority.MEDIUM -> themeFactory.priorityMediumColor()
        Priority.LOW -> themeFactory.priorityLowColor()
    }

    Surface(
        color = color.copy(alpha = 0.16f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = priority.name,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 3.dp
            )
        )
    }
}

@Composable
private fun PriorityMenu(
    selectedPriority: Priority,
    onPriorityChange: (Priority) -> Unit,
    themeFactory: ThemeFactory
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        AppButton(
            text = selectedPriority.name,
            themeFactory = themeFactory,
            onClick = {
                expanded = true
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    onClick = {
                        onPriorityChange(priority)
                        expanded = false
                    }
                ) {
                    Text(priority.name)
                }
            }
        }
    }
}

@Composable
private fun SortMenu(
    selected: SortOption,
    onSortChange: (SortOption) -> Unit,
    themeFactory: ThemeFactory
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        AppButton(
            text = "Sort: ${selected.label}",
            themeFactory = themeFactory,
            onClick = {
                expanded = true
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSortChange(option)
                        expanded = false
                    }
                ) {
                    Text(option.label)
                }
            }
        }
    }
}

@Composable
private fun FilterButton(
    text: String,
    selected: Boolean,
    themeFactory: ThemeFactory,
    onClick: () -> Unit
) {
    if (selected) {
        AppButton(
            text = text,
            themeFactory = themeFactory,
            onClick = onClick
        )
    } else {
        OutlinedButton(
            onClick = onClick
        ) {
            Text(text)
        }
    }
}

@Composable
private fun AppButton(
    text: String,
    themeFactory: ThemeFactory,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = themeFactory.buttonColor(),
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}

@Composable
private fun StatisticsBar(tasks: List<Task>) {
    val completed =
        tasks.count {
            it.completed
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Total: ${tasks.size}")
        Text("Completed: $completed")
        Text("Remaining: ${tasks.size - completed}")
    }
}

@Composable
private fun DeleteConfirmationDialog(
    task: Task,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Delete task")
        },
        text = {
            Text("Are you sure you want to delete \"${task.title}\"?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun textFieldColors(
    themeFactory: ThemeFactory
) = TextFieldDefaults.textFieldColors(
    backgroundColor = themeFactory.textFieldColor()
)

private fun taskComparator(
    sortOption: SortOption
): Comparator<Task> =
    when (sortOption) {
        SortOption.PRIORITY ->
            compareBy<Task> {
                priorityRank(it.priority)
            }.thenBy {
                it.title.lowercase()
            }

        SortOption.NAME ->
            compareBy {
                it.title.lowercase()
            }

        SortOption.CREATED_AT ->
            compareByDescending<Task> {
                it.createdAt
            }.thenBy {
                it.title.lowercase()
            }
    }

private fun priorityRank(priority: Priority): Int =
    when (priority) {
        Priority.HIGH -> 0
        Priority.MEDIUM -> 1
        Priority.LOW -> 2
    }

private fun createdAtText(createdAt: String): String =
    if (createdAt.isBlank())
        "Created: unknown"
    else
        "Created: $createdAt"

private enum class TaskFilter(
    val label: String
) {
    ALL("All"),
    ACTIVE("Active"),
    COMPLETED("Completed")
}

private enum class SortOption(
    val label: String
) {
    PRIORITY("Priority"),
    NAME("Name"),
    CREATED_AT("Created")
}
