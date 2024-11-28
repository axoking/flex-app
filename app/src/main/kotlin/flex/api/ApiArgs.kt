package flex.api

class ApiArgs {
	val args: Map<String, String>
	
	constructor(argList: List<Pair>) {
		args = argList.toMap()
	}

	fun makeHeaders(): HeadersBuilder = headers {
		for (val key in args) {
			append(key, args[key])
		}
	}
}
