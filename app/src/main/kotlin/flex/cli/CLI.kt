package flex.cli

import flex.values.VERSION

class CLI {
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
			"echo" -> {
				for (arg in args) { println(arg) }
			}
		}


		return true
	}

	fun run() {
		// Run a new command until the user types "exit"
		println("Flex $VERSION CLI prompt, type \"exit\" to quit")

		while (prompt());

		println("bye :)")
	}
}