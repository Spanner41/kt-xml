package design.cardia.ktxml.builder

open class Element(
    val type: String,
    private val canCollapseWhenEmpty: Boolean = true,
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    val children: MutableList<Node> = mutableListOf()
) : NodeWithAttributes(attributes) {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding) = if (shouldCollapse(format)) {
        "<$type${attributesToXml(format, version)} />"
    } else {
        "<$type${attributesToXml(format, version)}>${childrenToXml(format, version, encoding)}</$type>"
    }

    protected fun childrenToXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String {
        val childFormat = format.childFormat()

        return children.joinToString(
            prefix = getSeparator(childFormat),
            separator = getSeparator(childFormat),
            postfix = getSeparator(format),
            transform = { it.toXml(format.childFormat(), version, encoding) }
        ).ifBlank { "" }
    }

    private fun getSeparator(format: XmlFormat) =
        format.lineSeparator + if (format is PrettyFormat) format.tabCharacter.repeat(format.tabLevel) else ""

    private fun shouldCollapse(format: XmlFormat) = canCollapseWhenEmpty && format.collapseEmptyTags && children.isEmpty()
}
