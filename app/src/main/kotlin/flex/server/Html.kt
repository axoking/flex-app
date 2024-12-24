package flex.server

import java.io.File

fun staticHtml(filename: String): String = File("src/main/resources/web/$filename.html").readText()

fun renderHtml(filename: String, data: Map<String, String>): String = buildString {
	val template = staticHtml(filename)
	val parts = template.split("sigma.")
	append(parts[0])
	for (part in parts.drop(1)) {
		val index = part.indexOf("!")
		val key = part.take(index)
		val plain = part.drop(index + 1)
		append(data[key])
		append(plain)
	}
}

fun escapeHtml(html: String): String = buildString {
	for (char in html.chars()) {
		append(when (char) {
			'>'.code -> "&gt;"
			'<'.code -> "&lt;"
			'&'.code -> "&amp;"
			'"'.code -> "&quot;"
			'\''.code -> "&apos;"
			else -> char.toChar()
		})
	}
}