package batect

import batect.config.Container
import batect.model.events.TaskEvent
import batect.model.events.TaskEventSink
import batect.model.steps.BuildImageStep
import batect.model.steps.CreateContainerStep
import batect.model.steps.DisplayTaskFailureStep
import batect.model.steps.RunContainerStep
import batect.model.steps.StartContainerStep
import batect.model.steps.TaskStep

class EventLogger(private val console: Console) : TaskEventSink {
    private val commands = mutableMapOf<Container, String?>()
    private val lock = Object()

    fun reset() {
        commands.clear()
    }

    fun logTaskDoesNotExist(taskName: String) {
        synchronized(lock) {
            console.withColor(ConsoleColor.Red) {
                print("The task ")
                printBold(taskName)
                println(" does not exist.")
            }
        }
    }

    fun logTaskFailed(taskName: String) {
        synchronized(lock) {
            console.withColor(ConsoleColor.Red) {
                println()
                print("The task ")
                printBold(taskName)
                println(" failed. See above for details.")
            }
        }
    }

    override fun postEvent(event: TaskEvent) {}

    fun logBeforeStartingStep(step: TaskStep) {
        synchronized(lock) {
            when (step) {
                is BuildImageStep -> logImageBuildStarting(step.container)
                is StartContainerStep -> logDependencyContainerStarting(step.container)
                is RunContainerStep -> logCommandStarting(step.container, commands[step.container])
                is DisplayTaskFailureStep -> logTaskFailure(step.message)
                is CreateContainerStep -> commands[step.container] = step.command
            }
        }
    }

    private fun logImageBuildStarting(container: Container) {
        console.withColor(ConsoleColor.White) {
            print("Building ")
            printBold(container.name)
            println("...")
        }
    }

    private fun logCommandStarting(container: Container, command: String?) {
        console.withColor(ConsoleColor.White) {
            print("Running ")

            if (command != null) {
                printBold(command)
                print(" in ")
            }

            printBold(container.name)
            println("...")
        }
    }

    private fun logDependencyContainerStarting(dependency: Container) {
        console.withColor(ConsoleColor.White) {
            print("Starting dependency ")
            printBold(dependency.name)
            println("...")
        }
    }

    private fun logTaskFailure(message: String) {
        console.withColor(ConsoleColor.Red) {
            println()
            println(message)
        }
    }
}