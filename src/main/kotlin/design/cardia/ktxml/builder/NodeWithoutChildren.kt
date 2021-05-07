package design.cardia.ktxml.builder

interface NodeWithoutChildren : Node {
    override fun children() = emptyList<Node>()
}
