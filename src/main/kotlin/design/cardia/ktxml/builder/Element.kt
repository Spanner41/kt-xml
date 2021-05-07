package design.cardia.ktxml.builder

open class Element(
    val type: String,
    val canCollapseWhenEmpty: Boolean = true,
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    private val children: MutableList<Node> = mutableListOf()
) : NodeWithAttributes(attributes) {
    override fun children() = children

    private fun shouldCollapse(format: XmlFormat) = canCollapseWhenEmpty && format.collapseEmptyTags && children.isEmpty()

    fun add(lambda: () -> Node) = add(lambda())
    fun add(node: Node) = node.also { children.add(it) }

    infix fun withText(text: String) = apply { add(Text(text)) }
}
