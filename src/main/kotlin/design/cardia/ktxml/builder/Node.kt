package design.cardia.ktxml.builder

sealed class Node {
    abstract fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String
}

abstract class NodeWithAttributes(
    private val attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : Node() {
    operator fun String.invoke(value: () -> Any) {
        attributes[this] = value
    }

    infix fun String.to(other: Any) {
        attributes[this] = { other }
    }

    protected fun attributesToXml(): String =
        attributes.mapNotNull { (k, v) -> "$k=\"${v.invoke()}\"" }
            .joinToString(prefix = " ", separator = " ")
            .ifBlank { "" }
}

open class Text(var text: String = "") : Node() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = format.escape(text, version)
}

class CdataNode(private val text: String) : Node() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<![CDATA[${text.escape()}]]>"

    private fun String.escape() = replace("]]>", "]]&gt;")
}

class CommentNode(private val text: String) : Node() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<!-- $text -->"
}
