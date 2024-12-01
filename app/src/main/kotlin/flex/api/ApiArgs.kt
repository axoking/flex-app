package flex.api

typealias ApiArgs = Map<String, String>

// Functions to (de)serialize API arguments
fun decodeArgs(text: String): ApiArgs = text.lines()
	.filter { it.length > 0 && it.contains("=") }
	.map {
		val s = it.split("=")
		s[0] to s[1]
	}.toMap()

fun encodeArgs(args: ApiArgs): String = buildString() {
	args.forEach() {
		append("${it.key}=${it.value}\n")
	}
}
