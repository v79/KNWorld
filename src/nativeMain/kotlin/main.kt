fun String.countDistinctCharacters() = toLowerCase().toList().distinct().count()


fun main() {
	println("NativeMain: Hello, enter your name:")
	val name = readLine()

	name?.replace(" ","")?.let {
		println("Your name contains ${it.length} letters")
		println("Your name contains ${it.countDistinctCharacters()} unique letters")
	} ?: error("Error while reading input from the terminal.")
}
