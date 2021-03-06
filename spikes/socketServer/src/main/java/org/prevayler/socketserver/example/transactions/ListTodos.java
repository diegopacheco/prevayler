package org.prevayler.socketserver.example.transactions;

import org.prevayler.socketserver.example.server.TodoList;

/*
 * prevayler.socketServer, a socket-based server (and client library)
 * to help create client-server Prevayler applications
 * 
 * Copyright (C) 2003 Advanced Systems Concepts, Inc.
 * 
 * Written by David Orme <daveo@swtworkbench.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import org.prevayler.socketserver.transactions.RemoteTransaction;

import java.util.Date;

/**
 * Return the entire current list of Todos.
 *
 * @author djo
 */
public class ListTodos extends RemoteTransaction<TodoList, TodoList> {

  private static final long serialVersionUID = 6704075381825894559L;

  /**
   * @see org.prevayler.util.TransactionWithQuery#executeAndQuery(Object, Date)
   */
  public TodoList executeAndQuery(TodoList todoList, Date timestamp) throws Exception {
    return todoList;
  }

}
