package design.cardia.ktxml.builder

open class Element(
    val type: String,
    val canCollapseWhenEmpty: Boolean = true,
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    private val children: MutableList<Node> = mutableListOf()
) : NodeWithAttributes(attributes) {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int) = if (shouldCollapse(format)) {
        "${getSeparator(format, indentLevel)}<$type${attributesToXml(format, version)} />"
    } else {
        "${getSeparator(format, indentLevel)}<$type${attributesToXml(format, version)}>${childrenToXml(format, version, encoding, indentLevel + 1)}</$type>"
    }

    override fun children() = children

    protected fun childrenToXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String {
        if (children.size == 1 && children[0] is Text && format is PrettyFormat && !format.textOnNewLine) {
            return children[0].toXml(format, version, encoding, indentLevel).ifBlank { "" }
        }

        return children.joinToString(separator = "", postfix = getSeparator(format, indentLevel - 1)) { it.toXml(format, version, encoding, indentLevel) }.ifBlank { "" }
    }

    private fun getSeparator(format: XmlFormat, indentLevel: Int) =
        format.lineSeparator + if (format is PrettyFormat) format.indentString.repeat(indentLevel) else ""

    private fun shouldCollapse(format: XmlFormat) = canCollapseWhenEmpty && format.collapseEmptyTags && children.isEmpty()

    fun add(lambda: () -> Node) = add(lambda())
    fun add(node: Node) = node.also { children.add(it) }

    infix fun withText(text: String) = apply { add(Text(text)) }
}
