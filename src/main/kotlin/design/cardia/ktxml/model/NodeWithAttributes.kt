package design.cardia.ktxml.model

import design.cardia.ktxml.printer.XmlFormat

abstract class NodeWithAttributes(
    val attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : Node {
    operator fun String.invoke(value: () -> Any) {
        require(this.isValidKey()) { "$this is not a valid attribute key" }
        attributes[this] = value
    }

    infix fun String.to(other: Any) {
        require(this.isValidKey()) { "$this is not a valid attribute key" }
        attributes[this] = { other }
    }

    operator fun get(key: String) = attributes[key]?.let { it() }

    fun attributesToXml(xmlFormat: XmlFormat, xmlVersion: XmlVersion): String =
        attributes.mapNotNull { (key, value) ->
            val result = value.invoke().toString()
            val escapedValue = xmlFormat.escape(result, xmlVersion)

            "$key=\"${escapedValue}\""
        }
            .joinToString(prefix = " ", separator = " ")
            .ifBlank { "" }

    private fun String.isValidKey() = "^[a-zA-Z_][a-zA-Z0-9_.-]*(:[a-zA-Z0-9_.-]*)?\$".toRegex().matches(this)
}
