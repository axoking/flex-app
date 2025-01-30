package flex.cli

fun splitInput(input: String): List<String> {
	/*
	Splits a CLI command into seperate parts
	Parts are separated by spaces, but strings between "" are counted as one part
	Escaping with \ is also possible

	just like every other shell known to mankind
	 */

	val parts: MutableList<String> = mutableListOf()

	var escaped = false
	var insideString = false
	var buf = StringBuilder()
	var skipSpaces = false
	for (c in input) {
		if (c == ' ' && skipSpaces) { continue }
		skipSpaces = false
		if (escaped) {
			buf.append(c)
			escaped = false
			continue
		}

		if ((insideString && (c == '"')) || (!insideString && (c == ' '))) {
			parts.add(buf.toString())
			buf = StringBuilder()
			skipSpaces = true
			insideString = false
			continue
		}

		when (c) {
			'\\' -> { escaped = true }
			'"' -> { insideString = true }
			else -> { buf.append(c) }
		}
	}
	val str = buf.toString()
	if (str != "") { parts.add(str) }

	return parts
}