package design.cardia.ktxml.builder

class ProcessingInstructionElement(
    val type: String,
    attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : NodeWithAttributes(attributes), NodeWithoutChildren
