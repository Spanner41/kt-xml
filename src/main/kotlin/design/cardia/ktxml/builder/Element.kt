package design.cardia.ktxml.builder

open class Element(
    val type: String,
    private val canCollapseWhenEmpty: Boolean = true,
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    val children: MutableList<Node> = mutableListOf()
) : NodeWithAttributes(attributes) {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding) = if (shouldCollapse(format)) {
        "<$type${attributesToXml()} />"
    } else {
        "<$type${attributesToXml()}>${childrenToXml(format, version, encoding)}</$type>"
    }

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

    private fun shouldCollapse(format: XmlFormat) = canCollapseWhenEmpty && format.collapseEmptyTags && children.isEmpty()
}
