package co.zsmb.staticsite

class Metadata {

    var title: String = "zsmb.co"
        set(value) {
            val trimmedValue = value.trim()
            field = if (!trimmedValue.endsWith("zsmb.co")) {
                "$trimmedValue - zsmb.co"
            } else {
                trimmedValue
            }
        }

    var description: String = "I'm an Android developer, a university student, and a Kotlin enthusiast. This is my site for all things tech."

    val author: String = "Márton Braun"

    var keywordList: MutableList<String> = mutableListOf("zsmb", "zsmb13", "Kotlin", "Android", "zsmbco", "zsmb.co", "Márton Braun")

    val keywords: String
        get() = keywordList.joinToString()

}
