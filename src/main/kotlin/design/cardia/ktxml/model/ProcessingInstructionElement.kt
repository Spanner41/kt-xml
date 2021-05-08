package design.cardia.ktxml.model

class ProcessingInstructionElement(
    val type: String,
    attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : NodeWithAttributes(attributes)
