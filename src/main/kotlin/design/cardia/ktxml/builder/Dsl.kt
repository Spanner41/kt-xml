package design.cardia.ktxml.builder

fun document(version: XmlVersion, encoding: XmlEncoding, lambda: Document.() -> Element) = Document(version, encoding, lambda)

fun element(type: String, lambda: Element.() -> Unit) = Element(type).apply(lambda)

fun Element.add(lambda: () -> Node) {
    children.add(lambda())
}

fun Element.element(type: String, lambda: Element.() -> Unit) = add { Element(type).apply(lambda) }

fun Element.text(text: String) = add { Text(text) }

fun Element.comment(text: String) = add { Comment(text) }

fun Element.cdata(text: String) = add { CdataNode(text) }

fun Element.processingInstruction(type: String, lambda: ProcessingInstructionElement.() -> Unit) = add { ProcessingInstructionElement(type).apply(lambda) }
