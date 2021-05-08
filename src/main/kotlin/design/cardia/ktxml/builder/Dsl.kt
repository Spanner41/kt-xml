package design.cardia.ktxml.builder

import design.cardia.ktxml.model.CdataNode
import design.cardia.ktxml.model.Comment
import design.cardia.ktxml.model.Document
import design.cardia.ktxml.model.Element
import design.cardia.ktxml.model.ProcessingInstructionElement
import design.cardia.ktxml.model.Text
import design.cardia.ktxml.model.XmlEncoding
import design.cardia.ktxml.model.XmlVersion

fun document(version: XmlVersion, encoding: XmlEncoding, lambda: Document.() -> Element) = Document(version, encoding, lambda)

fun element(type: String, lambda: Element.() -> Unit = {}): Element = Element(type).apply(lambda)

fun Element.element(type: String, lambda: Element.() -> Unit = {}) = Element(type).apply(lambda).also(::add)

fun Element.attributes(attributes: Map<String, () -> Any>) = apply { attributes.forEach { it.key to it.value } }

fun Element.text(text: String) = Text(text).also(::add)

fun Element.comment(text: String) = Comment(text).also(::add)

fun Element.cdata(text: String) = CdataNode(text).also(::add)

fun Element.processingInstruction(type: String, lambda: ProcessingInstructionElement.() -> Unit) =
    ProcessingInstructionElement(type).apply(lambda).also(::add)
