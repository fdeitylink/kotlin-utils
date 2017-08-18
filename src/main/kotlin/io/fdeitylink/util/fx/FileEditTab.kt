/*
 * Copyright 2017 Brian Christian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * TODO:
 * protected fun rename(p: Path)?
 * Store undo pointer to call markUnchanged() on undo()/redo() if the same state as when save() was called is met
 * Create abstract class representing a savable object and use that rather than Path and delegating save() to subclasses?
 * Handle tabs that have a '*' in their name not as a marker of changes (is this already handled?)
 */

package io.fdeitylink.util.fx

import java.util.ArrayDeque

import java.nio.file.Path
import java.nio.file.Files

import javafx.scene.Node

import javafx.event.Event
import javafx.event.EventHandler

import javafx.scene.control.ButtonType

import javafx.scene.control.Tab
import javafx.scene.control.Tooltip

abstract class FileEditTab
@JvmOverloads protected constructor(path: Path, text: String? = null, content: Node? = null) : Tab(text, content) {
    //https://xkcd.com/853/
    private val undoQueue = ArrayDeque<UndoableEdit>()
    private val redoQueue = ArrayDeque<UndoableEdit>()

    val path: Path = path.toAbsolutePath()

    /*
     * Modifying isChanged is done via markChanged() and markUnchanged().
     * This is so that subclasses can increase the visibility of the
     * setter methods as needed, independently of one another. If the
     * visibility of only one method needs to be changed, that can be
     * done so other classes cannot arbitrarily change the boolean.
     */
    var isChanged = false
        private set

    init {
        require(Files.isRegularFile(path)) { "${this.path} is not a file" }

        id = this.path.toString()
        tooltip = Tooltip(this.path.toString())

        onCloseRequest = EventHandler<Event> { event ->
            if (isChanged) {
                val alert = createAlert(title = this.text?.substring(0, this.text.lastIndexOf('*')),
                                        message = "This tab has unsaved changes. Save before closing?")

                alert.buttonTypes.addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL)
                alert.showAndWait().ifPresent {
                    if (ButtonType.YES == it) {
                        save()
                    }
                    else if (ButtonType.CANCEL == it) {
                        event!!.consume()
                    }
                }
            }
        }
    }

    open fun undo() {
        if (!undoQueue.isEmpty()) {
            markChanged()
            val edit = undoQueue.removeFirst()
            redoQueue.addFirst(edit)
            edit.undo()
        }
    }

    open fun redo() {
        if (!redoQueue.isEmpty()) {
            markChanged()
            val edit = redoQueue.removeFirst()
            undoQueue.addFirst(edit)
            edit.redo()
        }
    }

    abstract fun save()

    protected open fun markChanged() {
        if (!isChanged) {
            isChanged = true
            if (!text.endsWith("*")) {
                text += '*'
            }
        }
    }

    protected open fun markUnchanged() {
        if (isChanged) {
            isChanged = false
            if (text.endsWith("*")) {
                text = text.substring(0, text.lastIndexOf("*"))
            }
        }
    }

    protected fun addUndo(edit: UndoableEdit) {
        markChanged()
        redoQueue.clear()
        undoQueue.addFirst(edit)
    }
}