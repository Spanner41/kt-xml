package design.cardia.ktxml.builder

fun document(version: XmlVersion, encoding: XmlEncoding, lambda: Document.() -> Element) = Document(version, encoding, lambda)

fun element(type: String, lambda: Element.() -> Unit) = Element(type).apply(lambda)

fun Element.add(lambda: () -> Node) {
    children.add(lambda())
}

fun Element.element(type: String, lambda: Element.() -> Unit) = add { Element(type).apply(lambda) }

fun Element.text(text: String) = add { Text(text) }
