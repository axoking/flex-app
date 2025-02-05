package flex.cli

import flex.server.Server
import flex.values.VERSION

const val helpMessage = """
help message goes here
good luck
"""

class CLI {
	/* Class that provides a Command-line interface for Flex
	Commands:
	push [on / off]: enables or disables Server Push
	add [path]: add a file to push
	list: list files to push
	remove [0, 1, 2, 3...]: remove a file to push
	run: run the server
	exit: exit bruh
	 */

	private val serverInstance = Server()
	private var pushEnabled = false
	private var pullEnabled = false
	private val pushFiles: List<String> = listOf()

	private fun prompt(): Boolean {
		// Waits for a new command and executes it. Returns false on exit.
		print("> ")
		val input = readln()
		val parts = splitInput(input)

		if (parts.size == 0) { return true }
		val command = parts[0]
		val args = parts.drop(1)
		when (command) {
			"exit" -> return false
			"help" -> println(helpMessage)
			"echo" -> {
				for (arg in args) { println(arg) }
			}
			"push" -> serverInstance.isPushing = when (args.getOrNull(0)) {
				"on" -> true
				"off" -> false
				else -> { println("syntax: push <on | off>"); return true }
			}
			"list" -> println(pushFiles.withIndex().joinToString {
				"${it.index}: ${it.value}"
			})
			"add" -> addPushFile(args.getOrNull(0))
			else -> println("unknown command")
		}

		return true
	}

	fun run() {
		// Run a new command until the user types "exit"
		println("Flex $VERSION CLI prompt, type \"exit\" to quit")

		while (prompt());

		println("bye :)")
	}

	private fun addPushFile(path: String?) {
		try {
			path?.let { serverInstance.addPushFile(it) }
		}
		catch (e: Exception) {
			println("error: $e")
		}
	}
}