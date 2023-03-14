package design.cardia.ktxml.model

open class Element(
    val type: String,
    val canCollapseWhenEmpty: Boolean = true,
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    val children: MutableList<Node> = mutableListOf(),
) : NodeWithAttributes(attributes) {

    fun add(lambda: () -> Node) = add(lambda())
    fun add(node: Node) = node.also { children.add(it) }

    infix fun withText(text: String) = apply { add(Text(text)) }
}
