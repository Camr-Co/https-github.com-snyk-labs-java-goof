/*
 * The MIT License
 *
 * Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.benas.todolist.web.pages.user;

import io.github.benas.todolist.web.common.util.TodolistUtils;
import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.domain.User;
import io.github.todolist.core.service.api.TodoService;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.List;

import static io.github.benas.todolist.web.common.util.TodolistUtils.DATE_FORMAT;

/**
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@SuppressWarnings("unused")
public class Home {

    @Inject
    private TodoService todoService;

    @SessionState
    private User loggedUser;

    @Property
    private List<Todo> todoList;

    @Property
    private Todo currentTodo;

    @Property
    private int doneCount;

    @Property
    private int todoCount;

    @Property
    private int totalCount;

    @OnEvent(value = EventConstants.ACTIVATE)
    public void init() {
        Long loggedUserId = loggedUser.getId();
        todoList = todoService.getTodoListByUser(loggedUserId);
        totalCount = todoList.size();
        doneCount = TodolistUtils.countTotalDone(todoList);
        todoCount = totalCount - doneCount;
    }

    @OnEvent(value = EventConstants.ACTION, component = "deleteTodoLink")
    public void deleteTodo(long todoId) {
        Todo todo = todoService.getTodoById(todoId);
        if (todo != null) {
            todoService.remove(todo);
        }
    }

    public String getCurrentStatusLabel() {
        return TodolistUtils.getStatusLabel(currentTodo.isDone());
    }

    public String getCurrentStatusStyle() {
        return TodolistUtils.getStatusStyle(currentTodo.isDone());
    }

    public String getCurrentPriorityIcon() {
        return TodolistUtils.getPriorityIcon(currentTodo.getPriority());
    }

    public String getCurrentDueDate() {
        return new SimpleDateFormat(DATE_FORMAT).format(currentTodo.getDueDate());
    }

}
