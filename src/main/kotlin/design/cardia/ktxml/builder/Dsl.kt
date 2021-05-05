package design.cardia.ktxml.builder

fun document(lambda: Document.() -> Element) = Document(lambda)

fun element(type: String, lambda: Element.() -> Unit) = Element(type).apply(lambda)

fun Element.add(lambda: () -> XmlContent) {
    children.add(lambda())
}

fun Element.element(type: String, lambda: Element.() -> Unit) = add { Element(type).apply(lambda) }

fun Element.text(text: String) = add { Text(text) }
