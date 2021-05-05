package design.cardia.ktxml.builder

sealed class Node {
    abstract fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String
}

class Document(
    private val child: Document.() -> Element
) : Node() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding) =
        """<?xml version="${version.value}" encoding="${encoding.value}"?>${format.lineSeparator}${child().toXml(format, version, encoding)}"""
}

abstract class XmlContent : Node()

open class Element(
    val type: String,
    private val attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    val children: MutableList<XmlContent> = mutableListOf()
) : XmlContent() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding) = if (shouldCollapse(format)) {
        "<$type${attributesToXml()} />"
    } else {
        "<$type${attributesToXml()}>${childrenToXml(format, version, encoding)}</$type>"
    }

    operator fun String.invoke(value: () -> Any) {
        attributes[this] = value
    }

    infix fun String.to(other: Any) {
        attributes[this] = { other }
    }

    protected fun attributesToXml(): String =
        attributes.mapNotNull { (k, v) -> "$k=\"${v.invoke()}\"" }.joinToString(prefix = " ", separator = " ")
            .ifBlank { "" }

    protected fun childrenToXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String =
        children.joinToString(
            prefix = getChildSeparator(format),
            postfix = getChildPostfix(format),
            separator = getChildSeparator(format),
            transform = { it.toXml(format.childFormat(), version, encoding) }
        ).ifBlank { "" }

    private fun getChildSeparator(format: XmlFormat) =
        format.lineSeparator + if (format is PrettyFormat) format.tabCharacter.repeat(format.tabLevel + 1) else ""

    private fun getChildPostfix(format: XmlFormat) =
        format.lineSeparator + if (format is PrettyFormat) format.tabCharacter.repeat(format.tabLevel) else ""

    private fun shouldCollapse(format: XmlFormat) = format.collapseEmptyTags && children.isEmpty()
}
